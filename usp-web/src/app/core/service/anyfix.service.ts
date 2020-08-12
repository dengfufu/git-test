import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class AnyfixService {

  TO_DISTRIBUTE = 10;
  TO_HANDLE = 20;
  TO_ASSIGN = 30;
  TO_CLAIM = 40;
  TO_SIGN = 50;
  TO_SERVICE = 60;
  IN_SERVICE = 70;
  TO_EVALUATE = 80;
  CLOSED = 90;
  RETURNED = 100;
  CANCELED = 110;

  // 去除待评价
  workStatusList = [this.TO_DISTRIBUTE, this.TO_HANDLE, this.TO_ASSIGN,
    this.TO_CLAIM, this.TO_SIGN, this.TO_SERVICE,
    this.IN_SERVICE, this.CLOSED,
    this.RETURNED, this.CANCELED];
  // 可结算的工单状态列表，包含待评价、已完成
  settleWorkStatusList = [this.TO_EVALUATE, this.CLOSED];
  // 委托商可确认的工单列表，包含待评价、已完成、已退单
  demanderCheckStatusList = [this.TO_EVALUATE, this.CLOSED, this.RETURNED];
  // 处理中工单列表，包含待分配、待派单、待接单、待签到、待服务、服务中
  handledWorkStatusList = [this.TO_HANDLE, this.TO_ASSIGN, this.TO_CLAIM, this.TO_SIGN, this.TO_SERVICE, this.IN_SERVICE];
  // 技术支持
  workSupportStatusList = [this.TO_DISTRIBUTE, this.TO_HANDLE, this.TO_ASSIGN, this.TO_CLAIM, this.TO_SIGN, this.TO_SERVICE,
    this.IN_SERVICE, this.TO_EVALUATE];
  workStatusMap: {[key: string]: string} = {};
  trafficMap: {[key: number]: string} = {};
  params: any;
  // 一次性参数
  paramsOnce: any;

  constructor() {
    this.workStatusMap[this.TO_DISTRIBUTE] = '待提单';
    this.workStatusMap[this.TO_HANDLE] = '待分配';
    this.workStatusMap[this.TO_ASSIGN] = '待派单';
    this.workStatusMap[this.TO_CLAIM] = '待接单';
    this.workStatusMap[this.TO_SIGN] = '待签到';
    this.workStatusMap[this.TO_SERVICE] = '待服务';
    this.workStatusMap[this.IN_SERVICE] = '服务中';
    this.workStatusMap[this.TO_EVALUATE] = '待评价';
    this.workStatusMap[this.CLOSED] = '已完成';
    this.workStatusMap[this.RETURNED] = '已退单';
    this.workStatusMap[this.CANCELED] = '已撤单';

    this.trafficMap[1] = '公交';
    this.trafficMap[2] = '摩的';
    this.trafficMap[3] = '步行';
    this.trafficMap[4] = '长途汽车';
    this.trafficMap[5] = '现场';
    this.trafficMap[6] = '出租车';
    this.trafficMap[7] = '火车';
    this.trafficMap[8] = '飞机';
    this.trafficMap[9] = '公司车';
    this.trafficMap[10] = '银行车';
    this.trafficMap[11] = '郊县汽车';
    this.trafficMap[12] = '轮渡';
  }

  // query(param: any): Observable<any> {
  //   return this.httpClient.post('/api/anyfix/work-request/query', param)
  //     .pipe(
  //       finalize(() => {
  //       }));
  // }
  getWorkStatusMap() {
    return this.workStatusMap;
  }

  getParams() {
    return this.params;
  }

  setParams(value: any) {
    this.params = value;
  }

  getParamsOnce() {
    const p = this.paramsOnce;
    this.paramsOnce = null;
    return p;
  }

  setParamsOnce(value: any) {
    this.paramsOnce = value;
  }

  getHandledWorkStatus() {
    let handleStatus = '';
    this.handledWorkStatusList.forEach(item => {
      handleStatus += item + ',';
    });
    handleStatus = handleStatus.substr(0, handleStatus.length - 1);
    return handleStatus;
  }

  getWorkStatusColor(workStatus: number) {
    let buttonColor;
    switch (workStatus) {
      case this.TO_DISTRIBUTE:
        buttonColor = {'background-color': '#5E86C8'};
        break;
      case this.TO_HANDLE:
        buttonColor = {'background-color': '#FFD617'};
        break;
      case this.TO_ASSIGN:
        buttonColor = {'background-color': '#5EC894'};
        break;
      case this.TO_CLAIM:
        buttonColor = {'background-color': '#7ED321'};
        break;
      case this.TO_SIGN:
        buttonColor = {'background-color': '#48B8FF'};
        break;
      case this.TO_SERVICE:
        buttonColor = {'background-color': '#FF7717'};
        break;
      case this.IN_SERVICE:
        buttonColor = {'background-color': '#FF9F00'};
        break;
      case this.TO_EVALUATE:
        buttonColor = {'background-color': '#6A77D3'};
        break;
      case this.RETURNED:
        buttonColor = {'background-color': '#65CEDC'};
    }
    return buttonColor;
  }
}
