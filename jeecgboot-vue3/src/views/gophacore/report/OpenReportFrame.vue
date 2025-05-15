<template>
  <div class="reportFrameRoot gopha-bgcolor-deep" style="height: 100%">
    <div class="header">
      <div class="title" v-if="urlParams.title" v-show="false">{{ urlParams.title }}</div>
    </div>
    <div class="content">
      <div class="action-bar gopha-bgcolor-deep">
        <div class="reportItem" v-if="queryParam.searchType != '归档'">预览报表</div>
        <div class="reportItem" v-if="queryParam.searchType == '归档'">归档报表</div>
        <div class="reportItem" v-if="queryParam.searchType != '归档'"
          ><label>预览数据：</label>
          <a-select
            show-search
            :filter-option="filterOption"
            size="small"
            placeholder="请选择"
            v-model:value="queryParam.searchType"
            style="width: 150px"
            :options="searchTypeDic"
          />
        </div>
        <div class="reportItem" v-show="checkShowField('项目')"
          ><label>项目：</label>
          <a-select
            show-search
            :filter-option="filterOption"
            size="small"
            placeholder="请选择项目"
            v-model:value="queryParam.projectId"
            style="width: 250px"
            :options="projectDic"
          />
        </div>
        <div class="reportItem"  v-show="checkShowField('林场')"
          ><label>林场：</label>
          <a-select
            show-search
            :filter-option="filterOption"
            size="small"
            placeholder="请选择林场"
            v-model:value="queryParam.lcId"
            :options="lcDic"
            style="width: 150px"
          />
        </div>
        <div class="reportItem" v-show="checkShowField('道路')"
          ><label>道路：</label>
          <a-select
            show-search
            :filter-option="filterOption"
            size="small"
            placeholder="请选择道路"
            v-model:value="queryParam.roadId"
            :options="roadsDic"
            style="width: 200px"
          />
        </div>
        <div class="reportItem" v-show="checkShowField('期次')"
          ><label>期次：</label>
          <a-select
            show-search
            :filter-option="filterOption"
            size="small"
            placeholder="请选择期次"
            v-model:value="queryParam.periodId"
            :options="periodDic"
          />
        </div>
        <div class="reportItem" v-if="urlParams.chapterFlag == '是'"
          ><label>百章：</label>
          <a-select
            show-search
            :filter-option="filterOption"
            size="small"
            placeholder="请选择百章"
            v-model:value="queryParam.chapter"
            :options="chapterDic"
          />
        </div>
        <div class="reportItem" v-if="urlParams.pageFlag == '是' && urlParams.pageDataUrl"
          ><label>页数：</label>
          <a-select
            show-search
            :filter-option="filterOption"
            size="small"
            placeholder="请选择页数"
            v-model:value="queryParam.pageNumber"
            :options="pageDic"
            style="width: 170px"
          />
        </div>
        <div style="float: right; margin: 0px 15px">
          <a-button
            :icon="h(SearchOutlined)"
            size="small"
            type="primary"
            class="reportItem"
            style="background: #447407"
            @click="doCreateReport"
            :disabled="!isReadyFlag"
            v-if="!loadedFlag"
          >
            查询</a-button
          >
          <a-button :icon="h(RedoOutlined)" size="small" type="primary" class="reportItem" style="background: #bfa300" @click="doCreateReport" v-else>
            刷新</a-button
          >
          <a-button
            :icon="h(PrinterOutlined)"
            size="small"
            type="primary"
            class="reportItem"
            :disabled="!isReadyFlag||!preCheckOk"
            @click="doPrint(urlParams.printFlag)"
            v-if="urlParams.printFlag != '不打印'"
          >
            打印</a-button
          >
          <template v-if="urlParams.exportFlag == '是'">
            <a-dropdown v-if="exportOptions.length > 1">
              <template #overlay>
                <a-menu @click="exportReport">
                  <a-menu-item v-for="item in exportOptions" :key="item">{{ item }}</a-menu-item>
                </a-menu>
              </template>
              <a-button :icon="h(CloudDownloadOutlined)" size="small" type="primary" class="reportItem" :disabled="!isReadyFlag"> 导出 </a-button>
            </a-dropdown>
            <a-button
              v-if="exportOptions.length == 1"
              :icon="h(CloudDownloadOutlined)"
              size="small"
              type="primary"
              class="reportItem"
              :disabled="!isReadyFlag||!preCheckOk"
              @click="exportReportSingle(exportOptions[0])"
            >
              导出{{ exportOptions[0] }}
            </a-button>
          </template>
        </div>
      </div>
      <a-empty v-if="!preCheckOk" style="margin-top: 100px" :description="preCheckDescription"/>
      <iframe v-if="preCheckOk&&url" ref="reportFrame" :src="url" width="100%" height="100%" frameborder="0" @load="onIframeLoad"></iframe>
    </div>
  </div>
</template>
<script lang="ts" setup name="OpenReportFrame">
// 打开报表窗口，并用自己的业务覆盖报表框架
import { Ref, ref, reactive, onMounted, h, nextTick, watch, watchEffect, computed } from 'vue';
import { getToken } from '/@/utils/auth';
import { defHttp } from '/@/utils/http/axios';
import { useRoute } from 'vue-router';
import { getAppEnvConfig } from '/@/utils/env';
import { PrinterOutlined, CloudDownloadOutlined, SearchOutlined, RedoOutlined } from '@ant-design/icons-vue';
import { filterOption } from '/@/views/gophacore/utils/selectSearch';
import JDictSelectTag from '/@/components/Form/src/jeecg/components/JDictSelectTag.vue';
import JSearchSelect from '/@/components/Form/src/jeecg/components/JSearchSelect.vue';
import { useUserStore } from '/@/store/modules/user';
const userId = useUserStore().getUserInfo?.id;
const reportFrame = ref();
const loadedFlag = ref(false);
const preCheckOk = ref(true);
const preCheckDescription = ref("查无数据");
const projectDic = ref([]);
const lcDic = ref([]);
const roadsDic = ref([]);
const periodDic = ref([]);
const chapterDic = ref([]);
const pageDic = ref([]);
let initFlag=true;
const searchTypeDic = ref([
  { value: '全部', label: '我的全部' },
  { value: '已提交', label: '我的已提交' },
  { value: '待提交', label: '我的待提交' },
]);
const url = ref(null);
const exportOptions = ref([]);
const route = useRoute(); // 解构路由对象
const reportId = route.params.reportId;
const token = getToken();

let title: Ref<string | null> = ref(null);
let urlParams = reactive(route.query);
document.title = urlParams.title;
const queryParam = reactive({
  projectId: null,
  lcId: null,
  roadId: null,
  periodId: null,
  pageNumber: null,
  chapter: null,
  searchType: urlParams.searchType,
  userId: userId,
});
const isReadyFlag = computed(() => {
  if (
    queryParam.projectId &&
    queryParam.lcId &&
    queryParam.roadId &&
    queryParam.periodId &&
    (urlParams.pageFlag == '否' || queryParam.pageNumber) &&
    (urlParams.chapterFlag == '否' || queryParam.chapter)
  ) {
    return true;
  } else {
    return false;
  }
});

function doPrint() {
  const printBtn = reportFrame.value.contentWindow.document.querySelector('#默认打印');
  printBtn && printBtn.click();
}
function exportReport(e) {
  const btn = reportFrame.value.contentWindow.document.querySelector(`#${e.key}`);
  btn && btn.click();
}
function exportReportSingle(type) {
  const btn = reportFrame.value.contentWindow.document.querySelector(`#${type}`);
  btn && btn.click();
}
function checkShowField(item){
  if(urlParams.searchField){
    return urlParams.searchField.split(",").includes(item);
  }else{
    return false;
  }
}
async function getPageCount() {
  if (urlParams.pageFlag == '是' && urlParams.pageDataUrl) {
    let frameParams = { ...urlParams, ...queryParam };
    await defHttp.post({ url: urlParams.pageDataUrl, params: frameParams }).then((res) => {
      if (res) {
        preCheckOk.value=true;
        let pageLength = Number(urlParams.pageLength);
        let pageArray = [];
        let dataCount = res;
        let pageCount = Math.ceil(dataCount / pageLength);
        for (let i = 0; i < pageCount; i++) {
          let startPage = i * pageLength + 1;
          let endPage = (i + 1) * pageLength >= dataCount ? dataCount : (i + 1) * pageLength;
          pageArray.push({
            label: `第${startPage}页到第${endPage}页`,
            text: `第${startPage}页到第${endPage}页`,
            title: `第${startPage}页到第${endPage}页`,
            value: `${i + 1}`,
          });
        }
        pageDic.value = pageArray;
        queryParam.pageNumber = pageArray[0].value;
      } else {
        preCheckOk.value=false;
        pageDic.value = [];
        queryParam.pageNumber = null;
      }
    });
  }
}
function doCreateReport() {
  initFrame();
}
async function initFrame() {
  const { VITE_GLOB_API_URL } = getAppEnvConfig();
  let frameParams = { ...urlParams, ...queryParam };
  if (urlParams.preCheckFlag == '是' && urlParams.preCheckUrl) {
    const result = await defHttp.get({ url: urlParams.preCheckUrl, params: frameParams }, { isTransformResponse: false });
    if (!result.result) {
      preCheckOk.value = false;
      loadedFlag.value = true;
      result.message&&(preCheckDescription.value = result.message);
      return false;
    }else{
      preCheckOk.value = true;
    }
  }
  const urlParamsStr = Object.keys(frameParams)
    .map((key) => `${encodeURIComponent(key)}=${encodeURIComponent(frameParams[key])}`)
    .join('&');
  url.value = null;
  nextTick(() => {
    url.value = `${VITE_GLOB_API_URL}/jmreport/view/${reportId}?token=${token}&${urlParamsStr}`;
    loadedFlag.value = true;
  });
}
function onIframeLoad() {
  let count = 0;
  const time = 500;
  let interval = setInterval(() => {
    count += time;
    count >= 10000 && clearInterval(interval);
    if (reportFrame.value) {
      let ivuMessage = reportFrame.value.contentWindow.document.querySelector('.ivu-message');
      if (ivuMessage) {
        ivuMessage.style.top = '200px';
        clearInterval(interval);
      }
    }
  }, time);
}
watch(
  () => queryParam.projectId,
  async (value) => {
    if (value) {
      await defHttp.post({ url: '/autoApi/execute/private/getRoadsLcDictByProjectId', params: { projectId: value } }).then((res) => {
        res.unshift({ label: '全部', text: '全部', title: '全部', value: 'all' });
        lcDic.value = res;
      });
      await defHttp
        .post({ url: '/autoApi/execute/private/getPeriodDictByProjectId', params: { projectId: value, searchType: queryParam.searchType } })
        .then((res) => {
          if(res==null||res.length==0){
            initFlag=false;
          }
          periodDic.value = res;
        });
    }
  }
);
watch(
  () => queryParam.lcId,
  async (value) => {
    if (value) {
      await defHttp
        .post({ url: '/autoApi/execute/private/getRoadsDictByProjectId', params: { projectId: queryParam.projectId, lc: value } })
        .then((res) => {
          roadsDic.value = res;
        });
    }
  }
);
watch(
  () => queryParam.roadId,
  async (value) => {
    if (value) {
      if (urlParams.chapterFlag == '是') {
        await defHttp.post({ url: '/autoApi/execute/private/getChapterDictByRoadId', params: { roadId: value } }).then((res) => {
          chapterDic.value = res;
        });
      }
      if (urlParams.pageFlag == '是') {
        getPageCount();
      }
    }
  }
);
watch(
  () => queryParam.periodId,
  (value) => {
    let interval = setInterval(() => {
      if (value && queryParam.roadId) {
        clearInterval(interval);
        getPageCount();
      }
    }, 500);
  }
);
watch(
  () => queryParam.searchType,
  async (value) => {
    await defHttp
      .post({ url: '/autoApi/execute/private/getPeriodDictByProjectId', params: { projectId: queryParam.projectId, searchType: value } })
      .then((res) => {
        periodDic.value = res;
      });
  }
);
watch(projectDic, (value) => {
  queryParam.projectId = null;
  nextTick(() => {
    queryParam.projectId = urlParams.projectId || value[0].value;
    urlParams.projectId = null;
  });
});
watch(lcDic, (value) => {
  queryParam.lcId = null;
  nextTick(() => {
    queryParam.lcId = urlParams.lcId || (value[0] ? value[0].value : null);
    urlParams.lcId = null;
  });
});
watch(roadsDic, (value) => {
  queryParam.roadId = null;
  nextTick(() => {
    queryParam.roadId = urlParams.roadId || (value[0] ? value[0].value : null);
    urlParams.roadId = null;
  });
});
watch(periodDic, (value) => {
  queryParam.periodId = null;
  nextTick(() => {
    queryParam.periodId = urlParams.periodId || (value[0] ? value[0].value : null);
    urlParams.periodId = null;
  });
});
watch(chapterDic, (value) => {
  queryParam.chapter = null;
  nextTick(() => {
    queryParam.chapter = value[0] ? value[0].value : null;
  });
});
const stopIsReadyFlag = watch(isReadyFlag, (value) => {
  if (value&&initFlag) {
    initFrame();
    stopIsReadyFlag();
  }
});
watch(queryParam, () => {
  loadedFlag.value = false;
});
onMounted(async () => {
  if (urlParams.exportOptions) {
    exportOptions.value = urlParams.exportOptions.split(',');
  }
  defHttp.post({ url: '/autoApi/execute/private/getProjectDictListByDataPermission' }).then((res) => {
    projectDic.value = res;
  });
});
</script>
<style scoped lang="less">
.ant-col {
  width: 100%;
  height: 36px;
}

.reportFrameRoot {
  overflow: auto;
}

.content {
  // height: calc(100% - 48px);
  height: 100%;
  position: relative;
  overflow: hidden;
}

.header {
  .title {
    margin: 10px 22px;
  }

  font-size: 16px;
}

.action-bar {
  min-height: 40px;
  line-height: 40px;
  width: 100%;
  position: relative;
  display: inline-block;

  .reportItem {
    margin: 0px 0px 0px 20px;
    display: inline-block;

    label {
      font-size: 14px;
    }

    :deep(.ant-select-selection-item) {
      font-size: 12px;
    }
  }
}

iframe {
  margin-top: -40px;
}

:where(.css-dev-only-do-not-override-107n59g).ant-form-item {
  margin-bottom: 5px;
}
</style>