import {ChangeDetectorRef, Component, OnInit} from '@angular/core'
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {ZonValidators} from '@util/zon-validators';
import {Result} from '@core/interceptor/result';

@Component({
  selector: 'app-device-model-add',
  templateUrl: 'device-model-add.component.html'
})
export class DeviceModelAddComponent implements OnInit {

  validateForm: FormGroup;
  brandList: any;
  largeClassList: any;
  smallClassList: any;

  spinning = false;

  demanderCorpList: any[] = [];

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      name: [null, [ZonValidators.required('型号名称'),ZonValidators.maxLength(100),ZonValidators.notEmptyString()]],
      brandId: [null, [Validators.required]],
      largeClassId: [null, [Validators.required]],
      smallClassId: [null, [Validators.required]],
      enabled: [true, []],
      corp:[null,ZonValidators.required('委托商')]

    });
  }

  ngOnInit(): void {
    this.getBrand();
    this.getDeviceClass();
    this.listDemanderCorp();

  }

  getBrand() {
    const corp = this.validateForm.value.corp || 0;
    this.httpClient.post('/api/device/device-brand/list',{corp})
      .subscribe((res: any) => {
        this.brandList = res.data;
      })
  }

  getDeviceClass() {
    const corp = this.validateForm.value.corp || 0;
    this.httpClient.post('/api/device/device-class/list',{corpId : corp})
      .subscribe((res: any) => {
        this.largeClassList = res.data;
      })
  }

  /**
   * 委托商列表
   */
  listDemanderCorp() {
    this.httpClient.get(`/api/anyfix/demander/list`).subscribe((result: Result) => {
      if (result.code === 0) {
        this.demanderCorpList = result.data || [];
        if (this.demanderCorpList.length === 1) {
          this.validateForm.patchValue({
            corp: this.demanderCorpList[0].demanderCorp
          });
        }
      }
    });
  }

  /**
   * 委托商变化重新获取编号值
   */
  demanderCorpChange(e){
    this.validateForm.controls.brandId.reset();
    this.validateForm.controls.largeClassId.reset();
    this.getBrand();
    this.getDeviceClass();
  }

  largeClassChange() {
    this.validateForm.controls.smallClassId.reset();
    if(!this.largeClassList){
      return false;
    }
    this.smallClassList = [];
    const largeClassValue = this.validateForm.controls.largeClassId.value;
    this.largeClassList.forEach(largeClass => {
      if(largeClassValue === largeClass.id){
        this.smallClassList = largeClass.deviceSmallClassDtoList;
      }
    })
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
      .post('/api/device/device-model/add',
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
