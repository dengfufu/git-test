export enum OutcomeStatusEnum {
  // 待出库
  NO_OUTCOME = 10,
  NO_OUTCOME_NAME = '待出库',
  // 已出库
  HAD_OUTCOME = 20,
  HAD_OUTCOME_NAME = '已出库',
  // 未提交
  NOT_SUBMIT = 0,
  NOT_SUBMIT_NAME = '未提交'
}

export enum OutcomeTypeEnum {
  // 销售借用出库
  SALE_BORROW = 70 ,
  SALE_BORROW_NAME = '销售借用出库' ,
  // 公司销售出库
  CORP_SALE = 80,
  CORP_SALE_NAME = '公司销售出库',
  // 归还厂商出库
  RETURN_VENDOR = 90 ,
  RETURN_VENDOR_NAME = '归还厂商出库' ,
  // 物料领用出库
  BORROW_WARE = 100,
  BORROW_WARE_NAME = '物料领用出库'
}
export enum ValueEnum {
  MAX_INT = 2147483647
}

export enum WorkSysTypeEnum {
  // 维护工单
  MAINTENANCE = 1,
  PATROL= 2, // 巡检
  // 安装类型工单
  INSTALL = 3

}
