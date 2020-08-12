import {Component, Input, OnInit} from '@angular/core';
import {Page, Result} from '../../../../../../core/interceptor/result';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {UserService} from '../../../../../../core/user/user.service';
import {Router} from '@angular/router';
import {PayApplyStatusCode, PayService} from '../../../../service/pay.service';
import {NzMessageService} from 'ng-zorro-antd';
import {PayApplyService} from '../pay-apply.service';
import {PAY_RIGHT} from '../../../../../../core/right/right';


@Component({
  selector: 'app-pay-apply-view',
  templateUrl: 'pay-apply-view.component.html',
  styleUrls: [`pay-apply-view.component.less`]
})
export class PayApplyViewComponent implements OnInit {

  @Input() orderId: string;

  loading = false;
  page = new Page();
  list: any[] = [];

  payApplyStatusDef = [];
  payRight = PAY_RIGHT;
  payApplyStatusCode = PayApplyStatusCode;

  constructor(private httpClient: HttpClient,
              private router: Router,
              private payService: PayService,
              private nzMessageService: NzMessageService,
              private payApplyService: PayApplyService,
              private userService: UserService) {
  }

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.queryList();
    this.payService.getPayApplyStatusDef().subscribe(item => {
      this.payApplyStatusDef = item || [];
      console.log(this.payApplyStatusDef);
    });
  }

  queryList(reset: boolean = false) {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    const param = {
      pageNum: this.page.pageNum,
      pageSize: this.page.pageSize,
      orderId: this.orderId,
      corpId: this.userService.currentCorp.corpId,
      status: this.payApplyStatusCode.PAY_SUCCESS
    };
    this.httpClient.post('/api/pay/payment-apply/query', param)
      .pipe(finalize(() => this.loading = false))
      .subscribe((res: Result) => {
        if (res && res.code === 0) {
          this.list = res.data.list;
          this.page.total = res.data.total;
        }
      });
  }

  pay(data) {
    this.payApplyService.createDemanderPayApply({
      payerCorpId: data.payerCorpId,
      payeeCorpId: data.payeeCorpId,
      orderId: data.orderId,
      orderName: data.orderName,
      orderAmount: data.orderAmount,
    }).subscribe(()=>{
      this.loadData();
    });
  }

  cancelPay(data) {
    this.loading = true;
    this.httpClient.post('/api/pay/payment-apply/cancel', {paymentApplyId: data.id})
      .pipe(finalize(() => this.loading = false))
      .subscribe((result: Result) => {
        if (result && result.code === 0) {
          this.nzMessageService.success('取消支付订单成功');
          this.loadData();
        }
      });
  }

  view(data) {
    this.payApplyService.viewPayApply(data.id);
  }

}
