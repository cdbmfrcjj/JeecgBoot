<template>
  <a-modal :title="title" :width="width" :open="visible" :confirmLoading="confirmLoading" @ok="handleOk"
    @cancel="handleCancel" :destroyOnClose="true" :afterClose="reset" cancelText="关闭">
    <a-spin :spinning="confirmLoading">

      <a-form :form="formRef" class="antd-modal-form" :labelCol="{ xs: { span: 24 }, sm: { span: 5 } }"
        :wrapperCol="{ xs: { span: 24 }, sm: { span: 16 } }">
        <a-row>
          <a-col :span="24">
            <a-form-item label="参数">
              <JCodeEditor v-model:value="paramsText" mode="json" height="150px" :fullScreen="true" theme="darcula" />
            </a-form-item>
            <a-form-item label="执行结果">
              <JCodeEditor v-model:value="resultText" mode="json" height="150px" :fullScreen="true" theme="darcula" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>

    </a-spin>
    <template #footer>
      <a-button @click="handleCancel">关闭</a-button>
      <a-button type="primary" @click="handleOk">执行</a-button>
    </template>
  </a-modal>
</template>

<script lang="ts" setup>
import { ref, nextTick } from 'vue';
import { JCodeEditor } from '/@/components/Form';
import { defHttp } from '/@/utils/http/axios';
import { Form } from 'ant-design-vue';

const formRef = ref();
const useForm = Form.useForm;
const paramsText = ref("");
const resultText = ref("");
let record ;
const title = ref("操作");
const width = ref(550);
const visible = ref(false);
const confirmLoading = ref<boolean>(false);

function open(recordparams) {
  console.log(recordparams.permission_dictText);
  visible.value = true;
  record = recordparams;
}
function handleCancel() {
  confirmLoading.value=false;
  close();
}
function close() {
  confirmLoading.value=false;
  visible.value = false;
}
function reset() {
  paramsText.value = "";
  resultText.value = "";
  confirmLoading.value=false;
}
function handleOk() {
  confirmLoading.value = true;
  let id = record.id;
  let name = record.name;
  let permission = (record.permission == 'disable') ? 'public' : 'private';
  let url = "/autoApi/execute/" + permission + "/" + name;
  let paramsObj = {};
  if(paramsText.value&&paramsText.value!=""){
    paramsObj = JSON.parse(paramsText.value);
  }
  defHttp.post({ url: url, params: paramsObj }, {isTransformResponse:false }).then((res) => {
    resultText.value = JSON.stringify(res);
    console.log(res);
  }).finally(() => {
    confirmLoading.value = false;
  });

}
defineExpose({
  open
});
</script>

<style>
/**隐藏样式-modal确定按钮 */
.jee-hidden {
  display: none !important;
}
</style>
