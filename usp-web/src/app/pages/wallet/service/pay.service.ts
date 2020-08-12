import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {forkJoin, Observable, Observer} from 'rxjs';
import {UserService} from '@core/user/user.service';
import {Result} from '@core/interceptor/result';
import {NzMessageService} from 'ng-zorro-antd';

export type ModulePayStatus = 'passed'| 'create-account' | 're-sign';

@Injectable({
  providedIn: 'root'
})
export class PayService {
  
  constructor(public httpClient: HttpClient,
              public nzMessageService: NzMessageService,
              public userService: UserService ) {
  
  }
  
  /**
   * 获取支付订单状态定义
   * @returns {Observable<Option[]>}
   */
  getPayApplyStatusDef(): Observable<Option[]> {
    return new Observable<Option[]>((subscriber) => {
      const data:Option[] = [];
      for (const key in PayApplyStatusCode) {
        data.push({
          code: PayApplyStatusCode[key],
          name: PayApplyStatusName[key]
        })
      }
      subscriber.next(data);
    })
  }
  
  /**
   * 获取钱包流水类型定义
   * @returns {Observable<Option[]>}
   */
  getPayTraceTypeDef(): Observable<Option[]> {
    return new Observable<Option[]>((subscriber) => {
      const data = [];
      for (const key in PayTraceTypeCode) {
        data.push({
          code: PayTraceTypeCode[key],
          name: PayTraceTypeName[key]
        })
      }
      subscriber.next(data);
    })
  }
  
  /**
   * 获取钱包紫金方向定义
   * @returns {Observable<Option[]>}
   */
  getPayDirectionDef(): Observable<Option[]> {
    return new Observable<Option[]>((subscriber) => {
      const data = [];
      for (const key in PayDirectionCode) {
        data.push({
          code: PayDirectionCode[key],
          name: PayDirectionName[key]
        })
      }
      subscriber.next(data);
    })
  }
  
  /**
   * 返回支付协议、模块的开通情况
   * 0 - 账户已开通，且已签订最新协议
   * 99 - 账户未开通
   * 98 - 协议更新，需要重签/补签
   */
  checkAccountAndProtocol(): Observable<{role:ModulePayStatus}> {
    
    return new Observable(subscriber => {
      forkJoin([this.checkSignToB(), this.checkAccount()]).subscribe((results:any[])=>{
        if (results[1].code === 0 && !results[1].data){
          // 没有账户，需要创建账户
          subscriber.next({role: 'create-account'})
        } else {
          if(results[0].code === 0 ) {
            if(!results[0].data || results[0].data.length===0) {
              // 无待签协议
              subscriber.next({role: 'passed'});
            } else {
              subscriber.next({role: 're-sign'});
            }
          }
        }
      })
    });
  }
  
  checkAccount(): Observable<any>{
    const param = {
      corpId: this.userService.currentCorp.corpId,
    };
    return this.httpClient.post('/api/pay/account-info/corp-view', param);
  }
  
  checkSignToB(): Observable<any>{
    const param = {
      corpId: this.userService.currentCorp.corpId,
      module: 'payment'
    };
    return this.httpClient.post('/api/uas/protocol/check-sign', param);
  }
  
  checkSignToC(): Observable<any>{
    const param = {
      userId: this.userService.userInfo.userId,
      module: 'payment'
    };
    return this.httpClient.post('/api/uas/protocol/check-sign', param);
  }
  
  getPaymentProtocol(): Observable<any>{
    const param = {
      module: 'payment'
    };
    return this.httpClient.get('/api/uas/protocol/list', {params: param});
  }
  
  createCorpAccount(param:any): Observable<any> {
    return this.httpClient.post('/api/pay/account-info/corp-init', param);
  }
  
  signToB(param:any): Observable<any>{
    return this.httpClient.post('/api/uas/protocol/sign-tob', param);
  }
  
  
}

export interface Option {
  code:string;
  name:string;
}

export const PayDirectionCode = {
  IN: 10,
  OUT: 20
};
export const PayDirectionName = {
  IN:'收入',
  OUT:'支出'
};

export const PayApplyStatusCode = {
  CREATE:100,
  CANCEL:101,
  IN_PAY:200,
  PAY_SUCCESS:300,
  PAY_FAIL:301
};
export const PayApplyStatusName = {
  CREATE:'订单创建',
  CANCEL:'订单取消',
  IN_PAY:'支付中',
  PAY_SUCCESS:'支付成功',
  PAY_FAIL:'支付失败'
};

export const PayTraceTypeCode = {
  CONSUME:100000,
  WALLET_CONSUME:100001,
  WITHDRAW:200000,
  REFUND:300000,
  REFUND_PAY_PLATFORM_FEE:900000,
  PAY_PLATFORM_FEE:900001,
  PLATFORM_FEE:900010,
  REFUND_PLATFORM_FEE:900011,
  PLATFORM_RENT:900020,
  REFUND_PLATFORM_RENT:900021,
  PLATFORM_MARKETING:900030,
};

export const PayTraceTypeName = {
  CONSUME:'支付消费',
  WALLET_CONSUME:'余额消费',
  WITHDRAW:'提现',
  REFUND:'退款',
  REFUND_PAY_PLATFORM_FEE:'退支付手续费',
  PAY_PLATFORM_FEE:'支付平台手续费',
  PLATFORM_FEE:'平台费用',
  REFUND_PLATFORM_FEE:'退平台费用',
  PLATFORM_RENT:'入驻费用',
  REFUND_PLATFORM_RENT:'退入驻费用',
  PLATFORM_MARKETING:'营销活动',
};

export const WalletAccountType = {
  ENTERPRISE: 'e',
  CUSTOMER: 'c'
};
