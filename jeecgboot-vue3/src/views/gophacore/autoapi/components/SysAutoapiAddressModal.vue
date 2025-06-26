<template>
  <a-modal
    :title="title"
    :width="width"
    :open="visible"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
    :destroyOnClose="true"
    :afterClose="reset"
    cancelText="关闭"
  >
    <a-spin :spinning="confirmLoading">
      <a-form
        :form="formRef"
        class="antd-modal-form"
        :labelCol="{ xs: { span: 24 }, sm: { span: 5 } }"
        :wrapperCol="{ xs: { span: 24 }, sm: { span: 16 } }"
      >
        <a-row>
          <a-col :span="24">
            <a-form-item label="地址" style="padding-top: 25px">
              <div class="flex justify-center">
                <a-input v-model:value="url" />
                <a-button type="primary" @click="handleCopy"> 复制 </a-button>
              </div>
              <!-- <a-input :value="url"></a-input> -->
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
    <template #footer>
      <a-button @click="handleCancel">关闭</a-button>
    </template>
  </a-modal>
</template>

<script lang="ts" setup>
  import { ref, unref } from 'vue';
  import { Form } from 'ant-design-vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useCopyToClipboard } from '/@/hooks/web/useCopyToClipboard';
  const formRef = ref();
  const useForm = Form.useForm;
  const title = ref('调用地址');
  const width = ref(750);
  const visible = ref(false);
  const url = ref('');
  const confirmLoading = ref<boolean>(false);
  const { createMessage } = useMessage();
  const { clipboardRef, copiedRef } = useCopyToClipboard();

  function open(recordparams) {
    url.value =
      window._CONFIG['domianURL'] + '/autoApi/execute/' + (recordparams.permission == 'disable' ? 'public' : 'private') + '/' + recordparams.name;
    visible.value = true;
  }
  function handleCopy() {
    if (!url.value) {
      createMessage.warning('请输入要拷贝的内容！');
      return;
    }
    clipboardRef.value = url.value;
    if (unref(copiedRef)) {
      createMessage.warning('复制成功');
    }
  }
  function handleCancel() {
    close();
  }
  function close() {
    visible.value = false;
  }
  function reset() {}

  defineExpose({
    open,
  });
</script>

<style></style>
