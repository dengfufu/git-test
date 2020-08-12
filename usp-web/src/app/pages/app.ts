// 应用
export const App = {
  CLOUD: 10001,
  UAS: 10002,
  ANYFIX: 10003,
  WMS: 10004,
  DEVICE: 10005,
  DIP: 10006,
  PAY: 10007
};

// 应用列表
export const AppList = [
  {id: 10001, name: '系统平台'},
  {id: 10002, name: '账户系统'},
  {id: 10003, name: '工单系统'},
  {id: 10004, name: '物料系统'},
  {id: 10005, name: '设备系统'},
  {id: 10006, name: '数据系统'},
  {id: 10007, name: '支付系统'},
];

// 应用编码与名称映射
export const AppMap: { [key: number]: string; } = {
  10001: '系统平台',
  10002: '账户系统',
  10003: '工单系统',
  10004: '物料系统',
  10005: '设备系统',
  10006: '数据系统',
  10007: '支付系统'
};
