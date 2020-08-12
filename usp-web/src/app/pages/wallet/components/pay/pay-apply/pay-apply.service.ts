import {Injectable} from '@angular/core';
import {NzDrawerService} from 'ng-zorro-antd';
import {PayApplyAddComponent} from './add/pay-apply-add.component';
import {HttpClient} from '@angular/common/http';
import {forkJoin, Observable} from 'rxjs';
import {Result} from '../../../../../core/interceptor/result';
import {PayApplyDetailComponent} from './detail/pay-apply-detail.component';
import {PayService, WalletAccountType} from '../../../service/pay.service';
import {map, tap} from 'rxjs/operators';
import {Router, RouterStateSnapshot} from '@angular/router';


/**
 * 委托商结算支付申请
 */
export class DemanderPaymentApply {
  payerCorpId: string; // 出账企业ID
  payeeCorpId: string; // 入账企业ID
  orderId: string; // 商品订单ID
  orderName?: string; // 商品名称
  orderAmount?: string; // 商品订单金额
}

@Injectable()
export class PayApplyService {
  type = WalletAccountType.ENTERPRISE;
  constructor(public nzDrawerService: NzDrawerService,
              public router: Router,
              public payService: PayService) {
  }
  
  isAvailable() {
    let role;
    const type = this.type;
    const redirectUrl = this.router.url;
    return this.payService.checkAccountAndProtocol().pipe(
      map(v => {
        role = v.role;
        return v.role === 'passed';
      }),
      tap(v => {
        if (!v) {
          const params = {
            role,
            type,
            redirectUrl
          };
          this.router.navigate(['/wallet/account/check'], {
            queryParams: params
          });
        }
      })
    );
  }
  
  /**
   * 委托商结算在线支付
   * @param demanderPaymentApply 参数
   */
  createDemanderPayApply(demanderPaymentApply: DemanderPaymentApply): Observable<any> {
    return new Observable<any>(subscriber => {
      this.isAvailable().subscribe(v=>{{
        if(v) {
          const drawerRef = this.nzDrawerService.create({
            nzTitle: '委托商结算-在线支付',
            nzContent: PayApplyAddComponent,
            nzWidth: '35%',
            nzContentParams: {demanderPaymentApply}
          });
          drawerRef.afterClose.subscribe(value => {
            subscriber.next(value);
          })
        }
      }})
    })
  }
  
  viewPayApply(paymentApplyId: string) {
    this.nzDrawerService.create({
      nzTitle: '在线支付-订单查看',
      nzContent: PayApplyDetailComponent,
      nzWidth: '60%',
      nzContentParams: {paymentApplyId}
    });
  }
}
