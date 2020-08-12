import {Component, Input, OnInit} from '@angular/core';
import {Page, Result} from '../../../../../core/interceptor/result';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {PayService} from '../../../service/pay.service';
import {PayApplyService} from '../../pay/pay-apply/pay-apply.service';
import {format} from 'date-fns';

@Component({
  selector: 'app-account-trace',
  templateUrl: 'account-trace.component.html'
})
export class AccountTraceComponent implements OnInit {

  @Input() accountId: string;

  loading = false;
  page = new Page();
  accountTraceList: any[] = [];

  drawerVisible = false;
  direction: any;
  traceType: any;
  starTime: any;
  endTime: any;

  payTraceTypeDef = [];
  payDirectionDef = [];

  constructor(private httpClient: HttpClient,
              private payApplyService: PayApplyService,
              public payService: PayService) {
  }

  ngOnInit() {
    this.queryCorpAccountTrace();
    this.payService.getPayTraceTypeDef().subscribe(item => {
      this.payTraceTypeDef = item || [];
    });
    this.payService.getPayDirectionDef().subscribe(item => {
      this.payDirectionDef = item || [];
    });
  }

  queryCorpAccountTrace(reset: boolean = false) {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    const param = {
      pageNum: this.page.pageNum,
      pageSize: this.page.pageSize,
      accountId: this.accountId,
      direction: this.direction,
      traceType: this.traceType,
      starTime: this.starTime ? format(this.starTime, 'YYYY-MM-DD') : '',
      endTime: this.endTime ? format(this.endTime, 'YYYY-MM-DD') : ''
    };
    this.httpClient.post('/api/pay/account-trace/query', param)
      .pipe(finalize(() => this.loading = false))
      .subscribe((res: Result) => {
        if (res && res.code === 0) {
          this.accountTraceList = res.data.list;
          this.page.total = res.data.total;
        }
      });
  }

  closeDrawer() {
    this.drawerVisible = false;
  }

  openDrawer() {
    this.drawerVisible = true;
  }

  resetParam() {
    this.direction = undefined;
    this.traceType = undefined;
    this.starTime = undefined;
    this.endTime = undefined;
  }

  viewApply(data) {
    if (data.applySource === 100) { // 支付申请
      this.payApplyService.viewPayApply(data.applyId);
    }
  }
}
