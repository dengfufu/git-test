import {ChangeDetectorRef, Input, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';


@Component({
  selector: 'app-tenant-edit',
  templateUrl: 'tenant-edit.component.html'
})
export class TenantEditComponent implements OnInit {

  @Input() sysTenant;

  validateForm: FormGroup;
  spinning = false;
  showDemanderLevel = false;
  showServiceLevel = false;
  levelOptions = [];
  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient) {
    const reg = '^(https?|http):\\/\\/[^\\s$.?#].[^\\s]*$';
    this.validateForm = this.formBuilder.group({
      corpId: [null, [Validators.required]],
      serviceDemander: [null, [Validators.required]],
      serviceProvider: [null, [Validators.required]],
      deviceUser: [null, [Validators.required]],
      needAccount: [null, [Validators.required]],
      applyCheckApi: [null,[Validators.pattern(reg)]],
      cloudManager: [null, [Validators.required]],
      demanderLevel: [null],
      serviceLevel: [null]
    });
  }

  ngOnInit(): void {
    this.validateForm.controls.corpId.setValue(this.sysTenant.corpId);
    this.validateForm.controls.serviceDemander.setValue(this.sysTenant.serviceDemander);
    this.validateForm.controls.serviceProvider.setValue(this.sysTenant.serviceProvider);
    this.validateForm.controls.deviceUser.setValue(this.sysTenant.deviceUser);
    this.validateForm.controls.cloudManager.setValue(this.sysTenant.cloudManager);
    this.validateForm.controls.needAccount.setValue(this.sysTenant.needAccount);
    this.validateForm.controls.applyCheckApi.setValue(this.sysTenant.applyCheckApi);
    this.showDemanderLevel = this.sysTenant.serviceDemander === 'Y';
    this.showServiceLevel = this.sysTenant.serviceProvider === 'Y';
    this.validateForm.controls.demanderLevel.setValue(this.sysTenant.demanderLevel);
    this.validateForm.controls.serviceLevel.setValue(this.sysTenant.serviceLevel);
    this.setLevelOptions();
  }

  setLevelOptions() {
    this.levelOptions =   [
      {
        value: 1,
        text: '普通'
      },
      {
        value: 2,
        text: '高级'
      }
    ];
  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    this.spinning = true;
    this.httpClient
      .post('/api/uas/sys-tenant/update',
        value)
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        this.modal.destroy('submit');
      });
  }

  destroyModal(): void {
    this.modal.destroy();
  }

  serviceChange(value: any) {
    this.validateForm.controls.serviceLevel.setValue(null);
    if(value && value === 'Y') {
      this.showServiceLevel = true;
      this.validateForm.controls.serviceLevel.setValidators([Validators.required]);
    } else {
      this.showServiceLevel = false;
      this.validateForm.controls.serviceLevel.setValidators(null);
    }
    this.validateForm.controls.serviceLevel.updateValueAndValidity();
  }

  demanderChange(value: any) {
    this.validateForm.controls.demanderLevel.setValue(null);
    if(value && value === 'Y') {
      this.showDemanderLevel = true;
      this.validateForm.controls.demanderLevel.setValidators([Validators.required]);
    } else {
      this.showDemanderLevel = false;
      this.validateForm.controls.demanderLevel.setValidators(null);
    }
    this.validateForm.controls.demanderLevel.updateValueAndValidity();
  }
}
