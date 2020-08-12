import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {finalize} from 'rxjs/operators';
import {Result} from '@core/interceptor/result';

@Component({
  selector: 'app-work-fee-rule-view',
  templateUrl: './work-fee-rule-view.component.html',
  styleUrls: ['./work-fee-rule-view.component.less']
})
export class WorkFeeRuleViewComponent implements OnInit {

  @Input() assortId;
  @Input() modEnabled;
  // 工单收费定义
  assortFee: any = {};
  // 是否可用
  enabled: boolean;
  // 加载中
  spinning = false;
  // 提交加载中
  submitLoading = false;

  constructor(
    public httpClient: HttpClient,
    public modalRef: NzModalRef,
    public messageService: NzMessageService
  ) { }

  ngOnInit() {
    this.getAssortFeeDetail();
  }

  // 获取工单收费规则
  getAssortFeeDetail() {
    this.spinning = true;
    this.httpClient.get(`/api/anyfix/work-fee-assort-define/${this.assortId}`)
      .pipe(
        finalize(() => {
          this.spinning = false;
        })
      )
      .subscribe((res: any) => {
        this.assortFee = res.data;
        this.enabled = (this.assortFee.enabled === 'Y');
      })
  }

  // 关闭页面
  destroyModal() {
    this.modalRef.destroy('close');
  }

  // 更新是否可用
  submit() {
    const params = Object.assign({}, this.assortFee);
    params.enabled = this.enabled ? 'Y' : 'N';
    params.updateEnabled = 'Y';
    this.submitLoading = true;
    this.httpClient.post('/api/anyfix/work-fee-assort-define/update', params)
      .pipe(
        finalize(() => {
          this.submitLoading = false;
        })
      ).subscribe((res: Result) => {
      if (res && res.code === 0) {
        this.messageService.success('修改成功');
        this.modalRef.destroy('submit');
      }
    });
  }

}
