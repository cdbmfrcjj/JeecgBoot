import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { rules } from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
//列表数据
export const columns: BasicColumn[] = [
  {
    title: '名称',
    align: 'center',
    dataIndex: 'name',
  },
  {
    title: '描述',
    align: 'center',
    dataIndex: 'description',
  },
  {
    title: '身份验证',
    align: 'center',
    dataIndex: 'permission',
    customRender: ({ value }) => {
      return value === 'enable' ? '启用' : '禁用';
    },
  },
  {
    title: '执行日志',
    align: 'center',
    dataIndex: 'saveLog',
    customRender: ({ value }) => {
      return value === 'enable' ? '启用' : '禁用';
    },
  },
  {
    title: '通用处理',
    align: 'center',
    dataIndex: 'handleId_dictText',
  },
  {
    title: '创建时间',
    align: 'center',
    dataIndex: 'createTime',
  },
];

//查询数据
export const searchFormSchema: FormSchema[] = [
  {
    label: '名称',
    field: 'name',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '描述',
    field: 'description',
    component: 'Input',
    colProps: { span: 6 },
  },
];

//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '名称',
    field: 'name',
    component: 'Input',
    dynamicRules: () => {
      return [{ required: true, message: '请输入名称!' }];
    },
  },
  {
    label: '描述',
    field: 'description',
    component: 'Input',
  },
  {
    label: '参数描述',
    field: 'paramsDescription',
    component: 'Input',
  },
  {
    label: '执行参数',
    field: 'doParams',
    component: 'Input',
  },
  {
    label: '身份验证',
    field: 'permission',
    component: 'Select',
    componentProps: {
      options: [
        {
          label: '启用',
          value: 'enable',
        },
        {
          label: '禁用',
          value: 'disable',
        },
      ],
    },
    dynamicRules: () => {
      return [{ required: true, message: '请输入身份验证!' }];
    },
  },
  {
    label: '执行日志',
    field: 'saveLog',
    component: 'JDictSelectTag',
    componentProps: {
      options: [
        {
          label: '启用',
          value: 'enable',
        },
        {
          label: '禁用',
          value: 'disable',
        },
      ],
    },
    dynamicRules: () => {
      return [{ required: true, message: '请输入是否记录日志!' }];
    },
  },
  {
    label: '通用处理',
    field: 'handleId',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'sys_autoapi_handle,name,id',
    },
  },
  {
    label: '代码',
    field: 'codeText',
    component: 'InputTextArea',
  },
  // TODO 主键隐藏字段，目前写死为ID
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false,
  },
];
