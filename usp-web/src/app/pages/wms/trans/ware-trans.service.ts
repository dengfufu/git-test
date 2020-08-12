import {Injectable} from '@angular/core';
import {catchError, finalize, map} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {Result} from '@core/interceptor/result';

@Injectable({
  providedIn: 'root'
})
export class WareTransService{


  FILL_IN = 10; // 填写
  COMMON_APPROVAL = 20; // 普通审批节点
  COUNTERSIGN_APPROVAL = 30; // 会签审批节点
  DELIVERY = 40; // 发货节点
  RECEIVE = 50; // 收货节点
  CONFIRM = 60;  // 确认节点



  TRANS_WARE_TRANSFER = 110; // 物料库存调拨小类编号
  TRANS_WARE_SHIFT = 120; // 物料快速转库小类编号
  TRANS_WARE_BAD_RETURN = 130;// 待修物料返还小类编号

  CONFIRMED = 1;
  TRANS = 30;// 转库大类编号

  FOR_ALLOCATION = 10; // 待调拨
  COMPLETE_ALLOCATION = 30; // 已调拨
  END = 100; // 完成
  ALL_COUNT = 200;
  constructor(public httpClient : HttpClient) {

  }

  queryStatusCount(params,statusCountMap) : Observable<any> {
    return this.httpClient.post('/api/wms/trans-ware-common/countByWareStatus', params)
      .pipe(
        map((res : any) => {
          console.log('length',res.data.length);
          if(Object.keys(res.data).length  ===0){
            return;
          }
          Object.keys(res.data).map(key => {
            if (res.data[key] && res.data[key].length > 0) {
              statusCountMap[key] = parseFloat(res.data[key]);
            }
          });
        }),
        catchError((error: Error) => {
          alert(error.message);
          return throwError(`I caught: ${error}`);
        })
      );
  }


  queryList(params) : Observable<any> {
    return this.httpClient.post('/api/wms/trans-ware-common/queryTrans', params)
      .pipe(
        map((res) => {
          console.log('我是哈哈',res);
          return res;
        }),
        catchError((error: Error) => {
          alert(error.message);
          return throwError(`I caught: ${error}`);
        })
      );
  }
}
