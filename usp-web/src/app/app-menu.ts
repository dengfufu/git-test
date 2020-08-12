import {MenuDataItem} from 'pro-layout';
import {ANYFIX_RIGHT, DEVICE_RIGHT, DIP_RIGHT, PAY_RIGHT, SYS_RIGHT, UAS_RIGHT, WMS_RIGHT} from '@core/right/right';

export const MenuData: MenuDataItem[] = [
  {
    name: '工作台', icon: 'dashboard', path: '/dashboard/workplace'
  },
  {
    name: '工单管理', icon: 'tool', path: '/anyfix', guard: {ability: [ANYFIX_RIGHT.WORK_MANAGE]},
    children: [
      {name: '工单处理', path: '/anyfix/work-list', guard: {ability: [ANYFIX_RIGHT.WORK_DEAL]}},
      // {name: '工单处理(新)', path: '/anyfix/work-list-new'},
      {name: '工单查询', path: '/anyfix/work-history'},
      {name: '工单预警', path: '/anyfix/work-remind-list', guard: {ability: [ANYFIX_RIGHT.WORK_REMIND_QUERY]}},
      {name: '金融设备工单', path: '/anyfix/work-list-atmcase', guard: {ability: [ANYFIX_RIGHT.WORK_ATMCASE_QUERY]}},
      {name: '服务审核', path: '/anyfix/service-check-list', guard: {ability: [ANYFIX_RIGHT.FINISH_SERVICE_CHECK, ANYFIX_RIGHT.FINISH_MANAGER_CHECK]}},
      {name: '费用审核', path: '/anyfix/fee-check-list', guard: {ability: [ANYFIX_RIGHT.FEE_SERVICE_CHECK, ANYFIX_RIGHT.FEE_MANAGER_CHECK]}},
      {name: '服务确认', path: '/anyfix/service-confirm-list', guard: {ability: [ANYFIX_RIGHT.WORK_FINISH_CONFIRM]}},
      {name: '费用确认', path: '/anyfix/fee-confirm-list', guard: {ability: [ANYFIX_RIGHT.WORK_FEE_CONFIRM]}},
      {name: '工单对账', path: '/anyfix/work-fee-verify', guard: {ability: [ANYFIX_RIGHT.WORK_FEE_VERIFY_LIST, ANYFIX_RIGHT.WORK_FEE_VERIFY_MANAGER]}},
      {name: '结算管理', path: '/anyfix/settle', guard: {ability: [ANYFIX_RIGHT.SETTLE_MANAGE]}},
      {name: '物品寄送', path: '/anyfix/goods-post', guard: {ability: [ANYFIX_RIGHT.GOODS_POST]}}
    ]
  },
  {
    name: '设备管理', icon: 'printer', path: '/device', guard: {ability: [DEVICE_RIGHT.SYS_DEVICE, ANYFIX_RIGHT.DEVICE_BRANCH]},
    children: [
      {name: '设备档案', path: '/device/device-info', guard: {ability: [DEVICE_RIGHT.DEVICE_INFO]}},
      {name: '设备网点', path: '/device/device-branch-list', guard: {ability: [ANYFIX_RIGHT.DEVICE_BRANCH]}}
    ]
  },
  {
    name: '备件管理', icon: 'slack', path: '/wms', guard: {ability: [WMS_RIGHT.SYS_WMS]},
    children: [
      {name: '基础数据', path: '/wms/ware-config'},
      {name: '入库管理', path: '/wms/ware-income-list'},
      {name: '出库管理', path: '/wms/ware-outcome-list'},
      {name: '转库管理', path: '/wms/ware-trans-config'},
      // {name: '库存盘点', path: ''},
      // {name: '维修管理', path: ''},
      {name: '表单模板管理', path: '/wms/form-template-list'},
      {name: '流程模板管理', path: '/wms/flow-template-list'}
    ]
  },
  {
    name: '客户管理', icon: 'bank', path: '/corp', guard: {ability: [ANYFIX_RIGHT.CORP_MANAGE]},
    children: [
      {name: '客户档案', path: '/corp/demander-custom', guard: {ability: [ANYFIX_RIGHT.CUSTOM_ARCHIVE]}},
      {name: '服务商管理', path: '/corp/demander-service', guard: {ability: [ANYFIX_RIGHT.SERVICE_ARCHIVE]}},
      {name: '委托商档案', path: '/corp/service-demander', guard: {ability: [ANYFIX_RIGHT.DEMANDER_ARCHIVE]}},
      {name: '服务网点', path: '/corp/service-branch', guard: {ability: [ANYFIX_RIGHT.SERVICE_BRANCH]}}
    ]
  },
  {
    name: '支付管理', icon: 'wallet', path: '/wallet', guard: {ability: [PAY_RIGHT.SYS_PAY]},
    children: [
      {name: '企业账户', path: '/wallet/corp/account-info', guard: {ability: [PAY_RIGHT.PAY_CORP_ACCOUNT_VIEW]}},
      {name: '企业支付订单', path: '/wallet/corp/pay-apply-list', guard: {ability: [PAY_RIGHT.PAY_CORP_APPLY_VIEW]}}
    ]
  },
  {
    name: '人员管理', icon: 'team', path: '/corp-user', guard: {ability: [UAS_RIGHT.CORP_USER_MANAGE]},
    children: [
      {name: '技能管理', path: '/corp-user/user-skill', guard: {ability: [ANYFIX_RIGHT.USER_SKILL]}},
      {name: '角色管理', path: '/corp-user/role-list', guard: {ability: [UAS_RIGHT.CORP_ROLE_MANAGE]}},
      {name: '员工管理', path: '/corp-user/user-list', guard: {ability: [UAS_RIGHT.CORP_USER_MANAGE]}},
      {name: '加入申请', path: '/corp-user/join-corp', guard: {ability: [UAS_RIGHT.JOIN_CORP_MANAGE]}}
    ]
  },
  {
    name: '系统设置', icon: 'setting', path: 'setting', guard: {ability: [ANYFIX_RIGHT.SETTING, DEVICE_RIGHT.DEVICE_CONFIG]},
    children: [
      {name: '设备设置', path: '/setting/device-config', guard: {ability: [DEVICE_RIGHT.DEVICE_CONFIG]}},
      {name: '工单设置', path: '/setting/work-config', guard: {ability: [ANYFIX_RIGHT.WORK_CONFIG]}},
      {name: '工单预警设置', path: '/setting/work-remind-config/work-remind', guard: {ability: [ANYFIX_RIGHT.WORK_REMIND_LIST]}},
      {name: '自动化设置', path: '/setting/automation-config', guard: {ability: [ANYFIX_RIGHT.AUTO_CONFIG]}},
      {name: '服务选项', path: '/setting/service-reason', guard: {ability: [ANYFIX_RIGHT.CUSTOM_REASON, ANYFIX_RIGHT.SERVICE_REASON]}},
      {name: '自定义字段', path: '/setting/custom-field-config', guard: {ability: [ANYFIX_RIGHT.CUSTOM_FILED]}},
      {name: '第三方应用', path: '/cloud/oauth-client', guard: {ability: [UAS_RIGHT.OAUTH_CLIENT]}}
    ]
  },
  {
    name: '平台管理', icon: 'cloud', path: 'cloud', guard: {ability: [SYS_RIGHT.CLOUD_MANAGE]},
    children: [
      {name: '企业认证', path: '/cloud/corp-verify', guard: {ability: [SYS_RIGHT.CORP_VERIFY]}},
      {name: '租户管理', path: '/cloud/tenant', guard: {ability: [SYS_RIGHT.TENANT]}},
      {name: '权限管理', path: '/cloud/right-list', guard: {ability: [SYS_RIGHT.RIGHT]}},
      {name: '鉴权管理', path: '/cloud/right-url', guard: {ability: [SYS_RIGHT.RIGHT_URL]}},
      {name: '凭证管理', path: '/cloud/token', guard: {ability: [SYS_RIGHT.TOKEN]}},
      {name: 'Redis管理', path: '/cloud/redis', guard: {ability: [SYS_RIGHT.REDIS]}},
      {name: '企业列表', path: '/cloud/corp-list', guard: {ability: [SYS_RIGHT.CORP_LIST]}},
      {name: '登录日志', path: '/cloud/login-log', guard: {ability: [SYS_RIGHT.LOGIN_LOG]}},
      {name: '操作日志', path: '/cloud/operation-log', guard: {ability: [SYS_RIGHT.OPERATION_LOG]}},
      {name: '用户管理', path: '/cloud/user-info', guard: {ability: [SYS_RIGHT.USER_INFO_MANAGE]}},
      // {name: '服务参数', path: '/cloud/app-config', guard: {ability: [SYS_RIGHT.PARAMS]}}
    ]
  },
  {
    name: '统计分析', icon: 'line-chart', path: '/dashboard/analysis', guard: {ability: [DIP_RIGHT.SYS_DIP]}
  }
  // {
  //   name: '个人中心', icon: 'slack', path: '',
  //   children: [
  //     {name: '个人资料', path: ''},
  //     {name: '企业设置', path: ''},
  //     {name: '工作状态', path: ''},
  //     {name: '个人轨迹', path: ''},
  //     {name: '安全设置', path: ''}
  //   ]
  // },
  // {
  //   name: '统计分析', icon: 'slack', path: '',
  //   children: [
  //     {name: '工单统计', path: ''},
  //     {name: '设备统计', path: ''},
  //     {name: '人员统计', path: ''},
  //   ]
  // },
];
