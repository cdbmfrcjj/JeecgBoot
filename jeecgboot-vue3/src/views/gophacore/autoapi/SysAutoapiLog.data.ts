import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
//列表数据
export const columns: BasicColumn[] = [
  {
    title: '接口名称',
    align: "center",
    dataIndex: 'name'
  },
  {
    title: '调用地址',
    align: "center",
    dataIndex: 'address'
  },
  {
    title: '调用参数',
    align: "center",
    dataIndex: 'params'
  },
  {
    title: '调用者IP',
    align: "center",
    dataIndex: 'ip'
  },
  {
    title: '数据内容',
    align: "center",
    dataIndex: 'data'
  },
  {
    title: '状态',
    align: "center",
    dataIndex: 'status'
  },
  {
    title: '创建时间',
    align: "center",
    dataIndex: 'createTime'
  },
];

//查询数据
export const searchFormSchema: FormSchema[] = [
  {
    label: "接口名称",
    field: 'name',
    component: 'Input',
    colProps: {span: 6},
  },
  {
    label: "调用者IP",
    field: 'ip',
    component: 'Input',
    colProps: {span: 6},
  },
  {
    label: "状态",
    field: 'status',
    component: 'Input',
    colProps: {span: 6},
  },
];

//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '接口名称',
    field: 'name',
    component: 'Input',
  },
  {
    label: '调用地址',
    field: 'address',
    component: 'Input',
  },
  {
    label: '调用参数',
    field: 'params',
    component: 'Input',
  },
  {
    label: '调用者IP',
    field: 'ip',
    component: 'Input',
  },
  {
    label: '数据内容',
    field: 'data',
    component: 'Input',
  },
  {
    label: '状态',
    field: 'status',
    component: 'Input',
  },
  {
    label: '创建时间',
    field: 'createTime',
    component: 'TimePicker',
  },
	// TODO 主键隐藏字段，目前写死为ID
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false,
  },
  
];
