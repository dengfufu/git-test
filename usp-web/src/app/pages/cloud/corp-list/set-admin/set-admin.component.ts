import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'app-set-admin',
  templateUrl: 'set-admin.component.html'
})
export class SetAdminComponent implements OnInit {

  @Input() corpId;
  @Input() toCorpId;
  @Input() key;

  spinning = false;
  validateForm: FormGroup;
  userOptions = [];
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      userId: [null, [Validators.required]],
      corpId: [this.corpId, [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.matchUser();
    this.validateForm.controls.corpId.setValue(this.corpId);
  }

  matchUser(userName?: string) {
    const payload = {
      corpId: this.corpId,
      matchFilter: userName
    };
    this.httpClient
      .post('/api/uas/corp-user/match', payload)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.userOptions = res.data;
      });
  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    if (this.toCorpId) {
      value.corpId = this.toCorpId;
    }
    if (this.key) {
      value.key = this.key;
    }
    this.spinning = true;
    this.httpClient
      .post('/api/uas/sys-role/set/admin',
        value)
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res:any) => {
        this.modal.destroy(res);
      });
  }

  destroyModal(): void {
    this.modal.destroy();
  }
}
