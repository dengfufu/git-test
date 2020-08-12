import {Component, Input, OnInit} from '@angular/core';
import {finalize} from 'rxjs/operators';
import {Result} from '../../../../../../core/interceptor/result';
import {HttpClient} from '@angular/common/http';
import {PayApplyStatusCode} from '../../../../service/pay.service';

/**
 * 支付申请单详情
 */
@Component({
  selector: 'app-pay-apply-detail',
  templateUrl: 'pay-apply-detail.component.html'
})
export class PayApplyDetailComponent implements OnInit {

  @Input() paymentApplyId: string;

  payApplyStatusCode = PayApplyStatusCode;

  loading = false;

  paymentApply: any = {};

  constructor(private httpClient: HttpClient) {
  }

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.httpClient.post('/api/pay/payment-apply/view', {paymentApplyId: this.paymentApplyId})
      .pipe(finalize(() => this.loading = false))
      .subscribe((res: Result) => {
        if (res && res.code === 0) {
          this.paymentApply = res.data;
        }
      });
  }
}
