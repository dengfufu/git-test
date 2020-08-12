import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {ZonValidators} from '@util/zon-validators';
import {Result} from '@core/interceptor/result';
import {WorkCheckService} from '../../work-check.service';

@Component({
  selector: 'app-anyfix-work-fee-confirm',
  templateUrl: './fee-confirm.component.html'
})
export class FeeConfirmComponent implements OnInit {

  // 工单编号
  @Input() workId: any;
  // 确认表单
  checkForm: FormGroup;
  // 加载中
  loading = false;
  spinning = false;

  constructor(public formBuilder: FormBuilder,
              public modalRef: NzModalRef,
              public httpClient: HttpClient,
              public messageService: NzMessageService,
              public modalService: NzModalService,
              public workCheckService: WorkCheckService) {
    this.checkForm = formBuilder.group({
      feeConfirmStatus: [this.workCheckService.CONFIRM_PASS],
      feeConfirmNote: [null, [ZonValidators.maxLength(400, '确认备注')]]
    });
  }

  ngOnInit() {
  }

  /**
   * 费用确认结果更改
   */
  feeResultChange(event) {
    if (event === this.workCheckService.CONFIRM_REFUSE) {
      this.checkForm.controls.feeConfirmNote.setValidators([ZonValidators.required('确认备注'),
        ZonValidators.maxLength(400, '确认备注')]);
    } else {
      this.checkForm.controls.feeConfirmNote.setValidators([ZonValidators.maxLength(400, '确认备注')]);
    }
    this.checkForm.controls.feeConfirmNote.updateValueAndValidity();
    this.checkForm.controls.feeConfirmNote.markAsDirty();
  }

  // 取消
  cancel() {
    this.modalRef.destroy();
  }

  // 确认提交
  checkSubmit() {
    if (!this.checkForm.valid) {
      this.checkForm.markAsDirty();
      return;
    }
    this.modalService.confirm({
      nzOkText: '确定',
      nzCancelText: '取消',
      nzContent: '是否确认费用？',
      nzOnOk: () => {
        const params = this.checkForm.value;
        params.workId = this.workId;
        this.loading = true;
        this.httpClient.post('/api/anyfix/work-check/fee/confirm', params)
          .pipe(finalize(() => {
            this.loading = false;
          })).subscribe((res: any) => {
          this.messageService.success('确认成功！');
          this.modalRef.destroy('submit');
        });
      }
    });
  }

}
