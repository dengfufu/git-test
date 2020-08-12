import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Result} from '@core/interceptor/result';
import {NzDrawerRef, NzMessageService, NzModalService} from 'ng-zorro-antd';
import {DemanderPaymentApply} from '../pay-apply.service';
import {finalize} from 'rxjs/operators';
import {PayApplyStatusCode} from '../../../../service/pay.service';

@Component({
  selector: 'app-pay-apply-add',
  templateUrl: 'pay-apply-add.component.html',
  styleUrls: [`pay-apply-add.component.less`]
})
export class PayApplyAddComponent implements OnInit, OnDestroy {

  @Input() demanderPaymentApply: DemanderPaymentApply;

  // @Output() nzPaySuccess = new EventEmitter<void>();

  disabledButton = false;  // 按钮禁用
  loading = false; // 加载效果
  payCreateSuccess = false; // 支付创建成功
  errorMsg; // 错误信息
  paySuccess = false; // 支付成功
  payProgress: string; // 支付过程
  payApplyInfo: any = {}; // 支付申请信息
  payInterval: any; // 支付结果查询定时任务

  constructor(private httpClient: HttpClient,
              private nzDrawerRef: NzDrawerRef,
              private nzModalService: NzModalService,
              private nzMessageService: NzMessageService) {
  }

  ngOnInit() {
    this.createPayApplySubmit();
  }

  ngOnDestroy(): void {
    clearInterval(this.payInterval);
  }

  /**
   * 创建订单支付申请
   */
  createPayApplySubmit() {
    this.loading = true;
    this.payProgress = '生成支付申请中...';
    this.httpClient.post('/api/pay/payment-apply/demander-create', this.demanderPaymentApply)
      .pipe(finalize(() => this.loading = false))
      .subscribe((result: Result) => {
        if (result && result.code === 0) {
          this.payCreateSuccess = true;
          this.payApplyInfo = result.data;
        }
      },err=>{
        this.errorMsg = err;
        console.log('支付创建失败',err)
      });
  }

  /**
   * 取消订单支付申请
   */
  cancel() {
    this.loading = true;
    this.payProgress = '订单取消中...';
    this.httpClient.post('/api/pay/payment-apply/cancel', {paymentApplyId: this.payApplyInfo.id})
      .pipe(finalize(() => this.loading = false))
      .subscribe((result: Result) => {
        if (result && result.code === 0) {
          this.nzMessageService.success('取消支付订单成功');
          this.disabledButton = true;
        }
      });
  }

  /**
   * 选择支付方式(支付宝)付款
   */
  submit() {
    const currentDate = new Date();
    const expireTime = this.payApplyInfo.expireTime;
    if (expireTime < currentDate) {
      this.nzModalService.confirm({
        nzTitle: '<i>支付申请已经超时!</i>',
        nzContent: '<b>是否重新生成支付订单</b>',
        nzOnOk: () => {
          this.createPayApplySubmit();
        },
        nzOnCancel: () => {
          this.nzDrawerRef.close();
        }
      });
    } else {
      this.loading = true;
      this.payProgress = '订单支付中...';
      this.httpClient.post('/api/pay/alipay/pc-pay', {paymentApplyId: this.payApplyInfo.id})
        .subscribe((result: Result) => {
          if (result && result.code === 0) {
            const payWindow = window.open('#', '_blank');
            payWindow.document.write(result.data);
            this.disabledButton = true;
            this.payInterval = setInterval(() => {
              this.payResult();
            }, 1000);
          }
        });
    }
  }

  /**
   * 查询在线支付结果
   */
  private payResult() {
    this.httpClient.post('/api/pay/payment-apply/direct-check-result', {paymentApplyId: this.payApplyInfo.id})
      .subscribe((result: Result) => {
        if (result && result.code === 0) {
          if (result.data) {
            console.log('支付成功');
            this.paySuccess = true;
            this.loadPayApplyResult(this.payApplyInfo.id);
            clearInterval(this.payInterval);
            // this.nzPaySuccess.emit();
          }
        }
      });
  }
  
  private loadPayApplyResult(paymentApplyId:string) {
    this.httpClient.post('/api/pay/payment-apply/view',{paymentApplyId}).subscribe((result:Result)=>{
      if(result.code === 0) {
        this.payApplyInfo = result.data;
        if(this.payApplyInfo.status === PayApplyStatusCode.PAY_SUCCESS) {
          this.paySuccess = true;
        }
      }
    })
  }
  public close() {
    this.nzDrawerRef.close()
  }
}
