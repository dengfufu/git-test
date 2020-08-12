import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {ZonValidators} from '@util/zon-validators';
import {Result} from '@core/interceptor/result';
import {WorkCheckService} from '../../work-check.service';

@Component({
  selector: 'app-anyfix-work-check-service',
  templateUrl: './service-check.component.html'
})
export class ServiceCheckComponent implements OnInit {

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
      finishCheckNote: [null, [ZonValidators.maxLength(400, '审核备注')]],
      finishCheckStatus: [this.workCheckService.CHECK_PASS]
    });
  }

  ngOnInit() {
  }

  /**
   * 服务审核结果更改
   */
  finishCheckChange(event) {
    if (event === 3) {
      this.checkForm.controls.finishCheckNote.setValidators([ZonValidators.required('审核备注'),
        ZonValidators.maxLength(400, '审核备注')]);
    } else {
      this.checkForm.controls.finishCheckNote.setValidators([ZonValidators.maxLength(400, '审核备注')]);
    }
    this.checkForm.controls.finishCheckNote.updateValueAndValidity();
    this.checkForm.controls.finishCheckNote.markAsDirty();
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
    this.httpClient.post('/api/anyfix/work-check/service/check', params)
      .pipe(finalize(() => {
        this.loading = false;
      })).subscribe((res: any) => {
      this.messageService.success('审核成功！');
      this.modalRef.destroy('submit');
    });
  }

}
