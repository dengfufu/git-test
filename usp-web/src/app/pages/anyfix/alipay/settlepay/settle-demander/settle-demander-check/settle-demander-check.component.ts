import {Component, Input, OnInit} from '@angular/core';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {UserService} from '@core/user/user.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'app-settle-demander-check',
  templateUrl: './settle-demander-check.component.html',
  styleUrls: ['./settle-demander-check.component.less']
})
export class SettleDemanderCheckComponent implements OnInit {

  // 结算单
  @Input() settleDemander: any;
  // 表单
  form: FormGroup;
  // 提交加载
  isLoading = false;

  constructor(
    private modalRef: NzModalRef,
    private httpClient: HttpClient,
    private userService: UserService,
    private formBuilder: FormBuilder,
    private messageService: NzMessageService
  ) {
    this.form = this.formBuilder.group({
      checkResult: [null, [Validators.required]],
      checkNote: []
    })
  }

  ngOnInit() {
    // this.queryDetail();
    this.form.patchValue({
      checkResult: this.settleDemander.checkStatus === 3 ? 'N' : 'Y',
      checkNote: this.settleDemander.checkNote
    })
  }

  // 核对结果改变
  checkResultChange(event) {
    console.log(event);
    if (event === 'N') {
      this.form.controls.checkNote.setValidators([Validators.required]);
    } else if (event === 'Y') {
      this.form.controls.checkNote.clearValidators();
    }
    this.form.controls.checkNote.markAsDirty();
    this.form.controls.checkNote.updateValueAndValidity();
  }

  // 取消
  cancel() {
    this.modalRef.destroy('cancel');
  }

  // 提交
  submit() {
    if (!this.form.valid) {
      this.form.markAsDirty();
      return;
    }
    this.settleDemander.checkResult = this.form.value.checkResult;
    this.settleDemander.checkNote = this.form.value.checkNote;
    this.isLoading = true;
    this.httpClient.post('/api/anyfix/settle-demander/check', this.settleDemander)
      .pipe(
        finalize(() => {
          this.isLoading = false;
        })
      ).subscribe((res: any) => {
        this.messageService.success('确认成功！');
        this.modalRef.destroy('submit');
    })
  }

}
