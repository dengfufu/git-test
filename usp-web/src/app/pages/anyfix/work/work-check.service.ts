import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class WorkCheckService {

  TO_FILL = 1; // 工单费用未录入
  FILLED = 2; // 工单费用已录入

  TO_CHECK = 1; // 待审核
  CHECK_PASS = 2; // 审核通过
  CHECK_REFUSE = 3; // 审核不通过

  TO_CONFIRM = 1; // 待确认
  CONFIRM_PASS = 2; // 确认通过
  CONFIRM_REFUSE = 3; // 确认不通过


  FINISH_CHECK_STATUS_LIST = [
    {value: 1, label: '待审核'},
    {value: 2, label: '审核通过'},
    {value: 3, label: '审核不通过'},
  ];
  FEE_CHECK_STATUS_LIST = [
    {value: 1, label: '待审核'},
    {value: 2, label: '审核通过'},
    {value: 3, label: '审核不通过'}
  ];
  FINISH_CONFIRM_STATUS_LIST = [
    {value: 1, label: '待确认'},
    {value: 2, label: '确认通过'},
    {value: 3, label: '确认不通过'},
  ];
  FEE_CONFIRM_STATUS_LIST = [
    {value: 1, label: '待确认'},
    {value: 2, label: '确认通过'},
    {value: 3, label: '确认不通过'}
  ];

  // 列表
  checkStatusList = [this.TO_CHECK, this.CHECK_PASS, this.CHECK_REFUSE];
  // 委托商确认状态映射
  demnanderCheckStatusMap: {[key: string]: string} = {};
  // 服务商审核状态映射
  serviceCheckStatusMap: {[key: string]: string} = {};

  constructor() {
    this.demnanderCheckStatusMap[this.TO_CHECK] = '待确认';
    this.demnanderCheckStatusMap[this.CHECK_PASS] = '已通过';
    this.demnanderCheckStatusMap[this.CHECK_REFUSE] = '不通过';
    this.serviceCheckStatusMap[this.TO_CHECK] = '待审核';
    this.serviceCheckStatusMap[this.CHECK_PASS] = '已通过';
    this.serviceCheckStatusMap[this.CHECK_REFUSE] = '不通过';
  }

}
