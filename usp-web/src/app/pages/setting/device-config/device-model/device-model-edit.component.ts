import {ChangeDetectorRef, Input, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {ZonValidators} from '@util/zon-validators';
import {Result} from '@core/interceptor/result';

@Component({
  selector: 'app-device-model-edit',
  templateUrl: 'device-model-edit.component.html'
})
export class DeviceModelEditComponent implements OnInit {

  @Input() deviceModel;
  validateForm: FormGroup;
  brandList: any;
  serviceCorpList: any;
  largeClassList: any;
  smallClassList: any;
  nzOptions: any;

  spinning = false;

  demanderCorpList = [];

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      id: [null, [Validators.required]],
      brandId: [null, [Validators.required]],
      largeClassId: [null, [Validators.required]],
      smallClassId: [null, [Validators.required]],
      corp: [null, [Validators.required]],
      name: [null, [ZonValidators.required('型号名称'),ZonValidators.maxLength(100),ZonValidators.notEmptyString()]],
      enabled: [null, []]
    });
  }

  initValue() {
    this.validateForm.controls.id.setValue(this.deviceModel.id);
    this.validateForm.controls.brandId.setValue(this.deviceModel.brandId);
    this.validateForm.controls.largeClassId.setValue(this.deviceModel.largeClassId);
    this.validateForm.controls.smallClassId.setValue(this.deviceModel.smallClassId);
    this.validateForm.controls.corp.setValue(this.deviceModel.corp);
    this.validateForm.controls.name.setValue(this.deviceModel.name);
    this.validateForm.controls.enabled.setValue(this.deviceModel.enabled === 'Y');
  }

  ngOnInit(): void {
    this.listDemanderCorp();
    this.initValue();
    this.getBrand();
    this.getDeviceClass();
  }

  /**
   * 委托商列表
   */
  listDemanderCorp() {
    this.httpClient.get(`/api/anyfix/demander/list`).subscribe((result: Result) => {
      if (result.code === 0) {
        this.demanderCorpList = result.data || [];
      }
    });
  }

  getBrand() {
    const corp = this.validateForm.value.corp || 0;
    this.httpClient.post('/api/device/device-brand/list', {corp})
      .subscribe((res: any) => {
        this.brandList = res.data;
      });
  }

  getDeviceClass() {
    const corp = this.validateForm.value.corp || 0;
    this.httpClient.post('/api/device/device-class/list', {corpId : corp})
      .subscribe((res: any) => {
        this.largeClassList = res.data;
        const largeClassValue = this.validateForm.controls.largeClassId.value;
        this.largeClassList.forEach(largeClass => {
          if (largeClassValue === largeClass.id) {
            this.smallClassList = largeClass.deviceSmallClassDtoList;
          }
        });
      });
  }

  largeClassChange() {
    this.validateForm.controls.smallClassId.reset();
    this.smallClassList = [];
    if (!this.largeClassList) {
      return false;
    }
    const largeClassValue = this.validateForm.controls.largeClassId.value;
    this.largeClassList.forEach(largeClass => {
      if (largeClassValue === largeClass.id) {
        this.smallClassList = largeClass.deviceSmallClassDtoList;
      }
    });
  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    value.enabled = this.validateForm.controls.enabled.value ? 'Y' : 'N';
    this.spinning = true;
    this.httpClient
      .post('/api/device/device-model/update',
        value)
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res) => {
        this.modal.destroy(res);
      });
  }

  destroyModal(): void {
    this.modal.destroy();
  }

}
