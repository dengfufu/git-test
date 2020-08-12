import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {environment} from '@env/environment';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';

@Component({
  selector: 'app-check-modal',
  templateUrl: './corp-verify-check.component.html',
  styleUrls: ['./corp-verify-check.component.less']
})
export class CorpVerifyCheckComponent implements OnInit {

  @Input()
  corpVerify: any;
  checkForm: FormGroup;
  checkResultList = [{label: '通过', value: 'Y'},
    {label: '不通过', value: 'N'}];
  ifPass = false;

  constructor(
    private formBuilder: FormBuilder,
    private http: HttpClient,
    private modal: NzModalRef,
    private messageService: NzMessageService
  ) {
    this.checkForm = this.formBuilder.group({
      checkResult: [null, Validators.required],
      checkNote: []
    });
  }

  ngOnInit() {
    this.corpVerify.licenseImgUrl = environment.server_url + '/api/file/showFile?fileId=' + this.corpVerify.licenseFileId;
  }

  checkResultChange(event) {
    const result = event;
    if (result === 'N') {
      this.checkNote.setValidators(Validators.required);
      this.checkNote.markAsDirty();
      this.checkNote.updateValueAndValidity();
      this.ifPass = false;
    } else if (result === 'Y') {
      this.checkNote.clearValidators();
      this.checkNote.markAsPristine();
      this.checkNote.updateValueAndValidity();
      this.ifPass = true;
    }
  }

  destroyModal(): void {
    this.modal.close('cancel');
  }

  checkSubmit() {
    if (!this.checkResult.valid) {
      this.checkResult.markAsDirty();
      return;
    }
    if (!this.ifPass && !this.checkNote.valid) {
      return;
    }
    if (this.ifPass && !this.checkForm.valid) {
      return;
    }
    const params = this.checkForm.value;
    params.corpId = this.corpVerify.corpId;
    params.txId = this.corpVerify.txId;
    console.log(params);
    this.http.post('/api/uas/corp-verify/audit', params)
      .subscribe((res: any) => {
        this.messageService.success('审核成功');
        this.modal.destroy('submit');
      });
  }

  get checkResult() {
    return this.checkForm.controls.checkResult;
  }

  get checkNote() {
    return this.checkForm.controls.checkNote;
  }

}
