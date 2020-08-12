import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class WorkFeeVerifyService {

  // 对账单状态
  TO_SUBMIT = 1; // 待提交
  TO_VERIFY = 2; // 待对账
  TO_CONFIRM = 3; // 待确认
  VERIFY_REFUSE = 4; // 确认不通过
  VERIFY_PASS = 5; // 确认通过

  // 结算状态
  TO_SETTLE = 1; // 待结算
  IN_SETTLE = 2; // 结算中
  SETTLED = 3; // 已结算

  verifyStatusList = [
    {value: this.TO_SUBMIT, label: '待提交'},
    {value: this.TO_VERIFY, label: '待对账'},
    {value: this.TO_CONFIRM, label: '待确认'},
    {value: this.VERIFY_REFUSE, label: '不通过'},
    {value: this.VERIFY_PASS, label: '已通过'}
  ];
  settleStatusList = [
    {value: this.TO_SETTLE, label: '待结算'},
    {value: this.IN_SETTLE, label: '结算中'},
    {value: this.SETTLED, label: '已结算'}
  ];

  constructor() {
  }

}
