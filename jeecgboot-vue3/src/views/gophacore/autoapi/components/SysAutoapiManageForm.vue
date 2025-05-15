<template>
  <a-spin :spinning="confirmLoading">
    <a-form ref="formRef" class="antd-modal-form" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <a-row>
        <a-col :span="12">
          <a-form-item label="名称" v-bind="validateInfos.name">
            <a-input v-model:value="formData.name" placeholder="请输入名称" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="描述" v-bind="validateInfos.description">
            <a-input v-model:value="formData.description" placeholder="请输入描述" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="参数描述" v-bind="validateInfos.paramsDescription">
            <a-input v-model:value="formData.paramsDescription" placeholder="请输入参数描述" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="执行参数" v-bind="validateInfos.doParams">
            <a-input v-model:value="formData.doParams" placeholder="请输入执行参数" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="身份验证" v-bind="validateInfos.permission">
	          <j-dict-select-tag v-model:value="formData.permission" dictCode="autoapiPermissions" placeholder="请选择身份验证" :disabled="disabled"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="执行日志" v-bind="validateInfos.saveLog">
	          <j-dict-select-tag v-model:value="formData.saveLog" dictCode="autoapiPermissions" placeholder="请选择是否记录日志" :disabled="disabled"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="通用处理" v-bind="validateInfos.handleId">
	          <j-dict-select-tag v-model:value="formData.handleId" dictCode="sys_autoapi_handle,name,id" placeholder="请选择通用处理" :disabled="disabled"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item style="padding-left: 17px;" label="代码" v-bind="validateInfos.codeText" :label-col='{span:2}' :wrapper-col="{ offset: 0, span: 21 }">
            <JCodeEditor v-model:value="formData.codeText" mode="groovy" height="300px"  :fullScreen="true" theme="darcula"/>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-spin>
</template>

<script lang="ts" setup>
  import { ref, reactive, defineExpose, nextTick, defineProps, computed, onMounted } from 'vue';
  // import { CodeEditor } from '/@/components/CodeEditor';
  import {JCodeEditor } from '/@/components/Form';
  import { useMessage } from '/@/hooks/web/useMessage';
  import JDictSelectTag from '/@/components/Form/src/jeecg/components/JDictSelectTag.vue';
  import { getValueType } from '/@/utils';
  import { saveOrUpdate } from '../SysAutoapiManage.api';
  import { Form } from 'ant-design-vue';
  
  const props = defineProps({
    formDisabled: { type: Boolean, default: false },
    formData: { type: Object, default: ()=>{} },
    formBpm: { type: Boolean, default: true }
  });
  const formRef = ref();
  const useForm = Form.useForm;
  const emit = defineEmits(['register', 'ok']);
  const formData = reactive<Record<string, any>>({
    id: '',
    name: '',   
    description: '',   
    paramsDescription: '',   
    doParams: '',   
    permission: '',   
    handleId: '',   
    codeText: '',   
  });
  const { createMessage } = useMessage();
  const labelCol = ref<any>({ xs: { span: 24 }, sm: { span: 5 } });
  const wrapperCol = ref<any>({ xs: { span: 24 }, sm: { span: 16 } });
  const confirmLoading = ref<boolean>(false);
  //表单验证
  const validatorRules = {
    name: [{ required: true, message: '请输入名称!'},],
    permission: [{ required: true, message: '请输入身份验证!'},],
    saveLog: [{ required: true, message: '请输入是否记录日志!'},],
  };
  const { resetFields, validate, validateInfos } = useForm(formData, validatorRules, { immediate: true });

  // 表单禁用
  const disabled = computed(()=>{
    if(props.formBpm === true){
      if(props.formData.disabled === false){
        return false;
      }else{
        return true;
      }
    }
    return props.formDisabled;
  });

  
  /**
   * 新增
   */
  function add() {
    edit({permission:"disable",permission_dictText:"禁用",saveLog:"enable",saveLog_dictText:"启用","createTime":""});
  }

  /**
   * 编辑
   */
  function edit(record) {
    console.log(record);
    nextTick(() => {
      resetFields();
      //赋值
      Object.assign(formData, record);
    });
  }

  /**
   * 提交数据
   */
  async function submitForm() {
    // 触发表单验证
    await validate();
    confirmLoading.value = true;
    const isUpdate = ref<boolean>(false);
    //时间格式化
    let model = formData;
    if (model.id) {
      isUpdate.value = true;
    }
    //循环数据
    for (let data in model) {
      //如果该数据是数组并且是字符串类型
      if (model[data] instanceof Array) {
        let valueType = getValueType(formRef.value.getProps, data);
        //如果是字符串类型的需要变成以逗号分割的字符串
        if (valueType === 'string') {
          model[data] = model[data].join(',');
        }
      }
    }
    await saveOrUpdate(model, isUpdate.value)
      .then((res) => {
        if (res.success) {
          createMessage.success(res.message);
          emit('ok');
        } else {
          createMessage.warning(res.message);
        }
      })
      .finally(() => {
        confirmLoading.value = false;
      });
  }


  defineExpose({
    add,
    edit,
    submitForm,
  });
</script>

<style lang="less" scoped>
  .antd-modal-form {
    min-height: 500px !important;
    overflow-y: auto;
    padding: 24px 24px 24px 24px;
  }
</style>
