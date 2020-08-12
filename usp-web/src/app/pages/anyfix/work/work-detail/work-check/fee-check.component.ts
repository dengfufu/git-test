import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {ZonValidators} from '@util/zon-validators';
import {Result} from '@core/interceptor/result';
import {WorkCheckService} from '../../work-check.service';

@Component({
  selector: 'app-anyfix-work-check-fee',
  templateUrl: './fee-check.component.html'
})
export class FeeCheckComponent implements OnInit {

  // 工单编号
  @Input() workId: any;
  // 审核表单
  checkForm: FormGroup;
  // 加载中
  loading = false;
  spinning = false;

  constructor(
    public formBuilder: FormBuilder,
    public modalRef: NzModalRef,
    public httpClient: HttpClient,
    public messageService: NzMessageService,
    public workCheckService: WorkCheckService) {
    this.checkForm = formBuilder.group({
      feeCheckNote: [null, [ZonValidators.maxLength(400, '审核备注')]],
      feeCheckStatus: [this.workCheckService.CHECK_PASS]
    });
  }

  ngOnInit() {
  }

  /**
   * 费用审核结果更改
   */
  feeCheckChange(event) {
    if (event === this.workCheckService.CHECK_REFUSE) {
      this.checkForm.controls.feeCheckNote.setValidators([ZonValidators.required('审核备注'),
        ZonValidators.maxLength(400, '审核备注')]);
    } else {
      this.checkForm.controls.feeCheckNote.setValidators([ZonValidators.maxLength(400, '审核备注')]);
    }
    this.checkForm.controls.feeCheckNote.updateValueAndValidity();
    this.checkForm.controls.feeCheckNote.markAsDirty();
  }

  // 取消
  cancel() {
    this.modalRef.destroy();
  }

  // 审核提交
  checkSubmit() {
    if (!this.checkForm.valid) {
      this.checkForm.markAsDirty();
      return;
    }
    const params = this.checkForm.value;
    params.workId = this.workId;
    this.loading = true;
    this.httpClient.post('/api/anyfix/work-check/fee/check', params)
      .pipe(finalize(() => {
        this.loading = false;
      })).subscribe((res: any) => {
      this.messageService.success('审核成功！');
      this.modalRef.destroy('submit');
    });
  }

}
