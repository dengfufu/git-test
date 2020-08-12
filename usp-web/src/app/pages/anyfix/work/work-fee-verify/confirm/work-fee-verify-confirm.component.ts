import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {ZonValidators} from '@util/zon-validators';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'app-work-fee-verify-confirm',
  templateUrl: './work-fee-verify-confirm.component.html',
  styleUrls: ['./work-fee-verify-confirm.component.less']
})
export class WorkFeeVerifyConfirmComponent implements OnInit {

  // 对账单详情
  @Input() workFeeVerify: any;
  // 表单
  form: FormGroup;
  // 加载中
  loading = false;

  constructor(
    public httpClient: HttpClient,
    public modalRef: NzModalRef,
    public formBuilder: FormBuilder,
    public messageService: NzMessageService
  ) {
    this.form = this.formBuilder.group({
      confirmResult: [null, [ZonValidators.required('确认结果')]],
      confirmNote: [null, [ZonValidators.maxLength(200, '确认备注')]]
    });
  }

  ngOnInit() {
    // 初始化表单
    if (this.workFeeVerify) {
      this.form.patchValue({
        confirmResult: this.workFeeVerify.status === 4 ? 'N' : 'Y',
        confirmNote: this.workFeeVerify.confirmNote
      });
    }
  }

  // 确认结果改变
  resultChange(event) {
    if (event === 'N') {
      this.form.controls.confirmNote.setValidators([ZonValidators.maxLength(200, '确认备注'),
        ZonValidators.required('确认备注')]);
      this.form.controls.confirmNote.markAsDirty();
    } else if (event === 'Y') {
      this.form.controls.confirmNote.setValidators([ZonValidators.maxLength(200, '确认备注')]);
    }
    this.form.controls.confirmNote.updateValueAndValidity();
  }

  // 取消
  cancel() {
    this.modalRef.destroy('cancel');
  }

  // 提交
  submit() {
    const params = {
      verifyId: this.workFeeVerify.verifyId,
      confirmResult: this.form.value.confirmResult,
      confirmNote: this.form.value.confirmNote
    };
    this.loading = true;
    this.httpClient.post('/api/anyfix/work-fee-verify/confirm', params)
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      ).subscribe((res: any) => {
      this.messageService.success('确认成功');
      this.modalRef.destroy('submit');
    });
  }

}
