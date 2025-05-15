<template>
  <div class="reportFrameRoot gopha-bgcolor-deep" style="height: 100%">
    <div class="header">
      <div class="title" v-if="urlParams.title" v-show="false">{{ urlParams.title }}</div>
    </div>
    <div class="content">
      <div class="action-bar gopha-bgcolor-deep">
        <div class="reportItem" v-if="checkShowField('status')"
          ><label>审批状态：</label>
          <j-dict-select-tag style="width: 250px" :showChooseOption="false"  v-model:value="queryParam.status" dictCode="report_show_item_schedule_status"/>
          <!-- <a-select
            show-search
            :filter-option="filterOption"
            size="small"
            placeholder="请选择项目"
            v-model:value="queryParam.projectId"
            style="width: 250px"
            :options="projectDic"
          /> -->
        </div>
        <div class="reportItem" v-show="checkShowField('project')"
          ><label>项目：</label>
          <a-select
            show-search
            :filter-option="filterOption"
            placeholder="请选择项目"
            v-model:value="queryParam.projectId"
            style="width: 250px"
            :options="projectDic"
          />
        </div>
        <div class="reportItem" v-show="checkShowField('lyj')"
          ><label>林业局：</label>
          <a-select
            show-search
            :filter-option="filterOption"
            placeholder="请选择林业局"
            v-model:value="queryParam.lyjId"
            style="width: 250px"
            :options="lyjDic"
          />
        </div>
        <div class="reportItem" v-if="checkShowField('projectTypeRoad')"
          ><label>项目类型：</label>
          <a-select  placeholder="请选择项目类型" v-model:value="queryParam.projectType" style="width: 250px">
            <a-select-option value="防火应急道路新建和改建">防火应急道路新建和改建</a-select-option>
            <a-select-option value="防火应急道路恢复重建">防火应急道路恢复重建</a-select-option>
          </a-select>
        </div>
        <div class="reportItem" v-if="checkShowField('projectTypeLc')"
          ><label>项目类型：</label>
          <a-select  placeholder="请选择项目类型" v-model:value="queryParam.projectType" style="width: 250px">
            <a-select-option value="林场所恢复重建">林场所恢复重建</a-select-option>
            <a-select-option value="市政基础设施恢复重建">市政基础设施恢复重建</a-select-option>
          </a-select>
        </div>
        <div class="reportItem" v-show="checkShowField('dateRange')"
          ><label>时间范围：</label>
          <a-range-picker v-model:value="queryParam.dateRange" valueFormat="YYYY-MM-DD" />
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
            :disabled="!isReadyFlag || !preCheckOk"
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
              :disabled="!isReadyFlag || !preCheckOk"
              @click="exportReportSingle(exportOptions[0])"
            >
              导出{{ exportOptions[0] }}
            </a-button>
          </template>
        </div>
      </div>
      <a-empty v-if="!preCheckOk" style="margin-top: 100px" :description="preCheckDescription" />
      <iframe v-if="url" ref="reportFrame" :src="url" width="100%" height="100%" frameborder="0" @load="onIframeLoad"></iframe>
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
const preCheckDescription = ref('查无数据');
const lyjDic = ref([]);
const projectDic = ref([]);
let initFlag = true;
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
  status:"审批完成",
  userId: userId,
  projectType: null,
  lyjId: null,
  dateRange: null,
  projectId: null,
});
const isReadyFlag = computed(() => {
  const fields = urlParams.searchField ? urlParams.searchField.split(',') : [];
  let result = true;
  fields.forEach((item) => {
    if ((item == 'projectTypeLc'||item == 'projectTypeRoad') && !queryParam.projectType) {
      result = false;
    } else if (item == 'lyj' && !queryParam.lyjId) {
      result = false;
    } else if (item == 'dateRange' && !queryParam.dateRange) {
      result = false;
    } else if (item == 'project' && !queryParam.projectId) {
      result = false;
    }
  });
  return result;
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
function checkShowField(item) {
  if (urlParams.searchField) {
    return urlParams.searchField.split(',').includes(item);
  } else {
    return false;
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
      result.message && (preCheckDescription.value = result.message);
      return false;
    } else {
      preCheckOk.value = true;
    }
  }
  const urlParamsStr = Object.keys(frameParams)
    // .map((key) => `${encodeURIComponent(key)}=${encodeURIComponent(frameParams[key])}`)
    .map((key) => `${key}=${frameParams[key]}`)
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
watch(queryParam, () => {
  loadedFlag.value = false;
});
onMounted(async () => {
  defHttp.post({ url: '/autoApi/execute/private/getLyjDictListByDataPermission' }).then((res) => {
    lyjDic.value = res;
  });
  defHttp.post({ url: '/autoApi/execute/private/getProjectDictListByDataPermission' }).then((res) => {
    projectDic.value = res;
  });
  if (urlParams.exportOptions) {
    exportOptions.value = urlParams.exportOptions.split(',');
  }
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
