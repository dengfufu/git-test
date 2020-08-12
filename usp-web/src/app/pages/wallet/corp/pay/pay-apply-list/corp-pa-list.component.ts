import {Component, Input, OnInit} from '@angular/core';
import {Page, Result} from '../../../../../core/interceptor/result';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {UserService} from '../../../../../core/user/user.service';
import {Router} from '@angular/router';
import {PAY_RIGHT} from '../../../../../core/right/right';
import {PayApplyStatusCode, PayService} from '../../../service/pay.service';
import {format} from 'date-fns';
import {NzMessageService} from 'ng-zorro-antd';
import {PayApplyService} from '../../../components/pay/pay-apply/pay-apply.service';

@Component({
  selector: 'app-corp-pa-list',
  templateUrl: 'corp-pa-list.component.html'
})
export class CorpPaListComponent implements OnInit {

  payRight = PAY_RIGHT;
  payApplyStatusCode = PayApplyStatusCode;
  loading = false;
  page = new Page();
  list: any[] = [];

  drawerVisible = false;
  status: string;
  orderName: string;
  startApplyTime: string;
  endApplyTime: string;

  payApplyStatusDef = [];

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
      status: this.status,
      corpId: this.userService.currentCorp.corpId,
      orderName: this.orderName,
      startApplyTime: this.startApplyTime ? format(this.startApplyTime, 'YYYY-MM-DD') : '',
      endApplyTime: this.endApplyTime ? format(this.endApplyTime, 'YYYY-MM-DD') : ''
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

  showDetail(settleId: string) {
    this.router.navigate(['/anyfix/settle/settle-demander-detail'], {queryParams: {settleId}});
  }

  closeDrawer() {
    this.drawerVisible = false;
  }

  openDrawer() {
    this.drawerVisible = true;
  }

  resetParam() {
    this.status = undefined;
    this.orderName = undefined;
    this.startApplyTime = undefined;
    this.endApplyTime = undefined;
  }

  pay(data) {
    this.payApplyService.createDemanderPayApply({
      payerCorpId: data.payerCorpId,
      payeeCorpId: data.payeeCorpId,
      orderId: data.orderId
    }).subscribe(() => {
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
