import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';

@Component({
  selector: 'app-tenant-add',
  templateUrl: 'tenant-add.component.html'
})
export class TenantAddComponent implements OnInit {

  validateForm: FormGroup;
  spinning = false;
  corpLoading = false;

  corpList = [];
  levelOptions = [];
  showDemanderLevel = false;
  showServiceLevel = false;
  nzFilterOption = () => true;


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
      cloudManager: [null, [Validators.required]],
      needAccount: [null, [Validators.required]],
      applyCheckApi: [null, [Validators.pattern(reg)]],
      demanderLevel: [null],
      serviceLevel: [null]
    });
  }

  ngOnInit(): void {
    this.listCorp();
    this.setLevelOptions();
  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    this.spinning = true;
    this.httpClient
      .post('/api/uas/sys-tenant/add',
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

  listCorp(corpName?: any) {
    const params = {matchFilter: corpName};
    this.corpLoading = true;
    this.httpClient
      .post('/api/uas/corp-registry/match', params)
      .pipe(
        finalize(() => {
          this.corpLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const list = [];
        res.data.forEach(item => {
          list.push({
            value: item.corpId,
            text: item.corpName
          });
        });
        this.corpList = list;
      });
  }

  matchCorp(corpName) {
    this.listCorp(corpName);
  }

  destroyModal(): void {
    this.modal.destroy();
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
}
