import {ChangeDetectorRef, Input, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzModalRef, NzModalService} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {UserService} from '@core/user/user.service';
import {ZonValidators} from '@util/zon-validators';

@Component({
  selector: 'app-device-info-edit',
  templateUrl: 'device-info-edit.component.html'
})
export class DeviceInfoEditComponent implements OnInit {

  @Input() deviceInfo: any = {};
  validateForm: FormGroup;
  serviceCorpList: any;
  customCorpList: Array<any>;
  deviceClassList: any[];
  brandModelList: any[];
  specificationList: any[] = [];

  demanderCorp = this.deviceInfo.demanderCorp;
  spinning = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private modalService: NzModalService,
              private httpClient: HttpClient,
              private userService: UserService) {
    this.validateForm = this.formBuilder.group({
      deviceClass: [null, Validators.required],
      largeClassId: [null, [Validators.required]],
      smallClassId: [null, [Validators.required]],
      specificationId: [],
      brandModel: [null, Validators.required],
      brandId: [null, [Validators.required]],
      modelId: [null, [Validators.required]],
      serial: [null, [Validators.required]],
      customId: [null, [Validators.required]],
      contactName:[],
      contactPhone:[null,ZonValidators.phoneOrMobile()],
      serviceCorp: [],
      factoryDate: [],
      purchaseDate: [],
      warrantyMode: [null, [ZonValidators.required('维保方式')]],
      contNo: [null, [ZonValidators.maxLength(30, '维保合同号')]],
      warrantyDate: [],
      warrantyStartDate: [],
      warrantyEndDate: [],
      warrantyStatus: [],
      warrantyNote: []
    });
  }

  initValue() {
    this.validateForm.patchValue(
      {
        deviceClass: [this.deviceInfo.largeClassId, this.deviceInfo.smallClassId],
        largeClassId: this.deviceInfo.largeClassId,
        smallClassId: this.deviceInfo.smallClassId,
        specificationId: this.deviceInfo.specificationId,
        brandModel: [this.deviceInfo.brandId, this.deviceInfo.modelId],
        brandId: this.deviceInfo.brandId,
        contactPhone: this.deviceInfo.contactPhone,
        contactName: this.deviceInfo.contactName,
        modelId: this.deviceInfo.modelId,
        serial: this.deviceInfo.serial,
        serviceCorp: this.deviceInfo.serviceCorp,
        factoryDate: this.deviceInfo.factoryDate,
        purchaseDate: this.deviceInfo.purchaseDate,
        warrantyMode: this.deviceInfo.warrantyMode.toString(),
        contNo: this.deviceInfo.contNo,
        warrantyStatus: this.deviceInfo.warrantyStatus.toString(),
        warrantyNote: this.deviceInfo.warrantyNote,
        customId: this.deviceInfo.customId
      }
    );
    if (this.deviceInfo.warrantyStartDate != null) {
      this.validateForm.patchValue({
        warrantyDate: [this.deviceInfo.warrantyStartDate, this.deviceInfo.warrantyEndDate]
      });
    }
  }

  ngOnInit(): void {
    this.demanderCorp = this.deviceInfo.demanderCorp;
    this.initValue();
    this.listServiceCorp();
    this.listCustomCorp();
    this.listDeviceClass();
    this.listDeviceSpecification(this.deviceInfo.smallClassId);
    this.listBrandModel(this.deviceInfo.smallClassId);
  }

  // 服务商列表
  listServiceCorp() {
    this.httpClient.post('/api/anyfix/demander-service/service/list', {demanderCorp: this.demanderCorp,
      enabled: 'Y'})
      .subscribe((res: any) => {
        this.serviceCorpList = res.data;
      });
  }

  // 客户列表
  listCustomCorp() {
    this.httpClient.post('/api/anyfix/demander-custom/custom/list', {demanderCorp: this.demanderCorp,
      enabled: 'Y'})
      .subscribe((res: any) => {
        this.customCorpList = res.data;
      });
  }

  // 设备分类列表
  listDeviceClass() {
    this.httpClient.post('/api/device/device-class/list', {corpId: this.demanderCorp})
      .subscribe((res: any) => {
        const tempList: any[] = [];
        res.data.forEach(largeClass => {
          const largeObject =
            {
              value: largeClass.id,
              label: largeClass.name,
              children: []
            };
          if (largeClass.deviceSmallClassDtoList && largeClass.deviceSmallClassDtoList.length > 0) {
            largeClass.deviceSmallClassDtoList.forEach(smallClass => {
              const smallObject =
                {
                  value: smallClass.id,
                  label: smallClass.name,
                  isLeaf: true
                };
              largeObject.children.push(smallObject);
            });
          }
          tempList.push(largeObject);
        });
        this.deviceClassList = tempList;
      });
  }

  deviceClassChange(event) {
    this.validateForm.patchValue({
      brandModel: null,
      brandId: null,
      modelId: null,
      specificationId: null
    });
    if (event && event.length > 0) {
      this.listBrandModel(event[1]);
      this.listDeviceSpecification(event[1]);
      this.validateForm.patchValue({
        largeClassId: event[0],
        smallClassId: event[1]
      });
    } else {
      this.brandModelList = [];
      this.validateForm.patchValue({
        largeClassId: null,
        smallClassId: null
      });
    }
  }

  // 设备规格列表
  listDeviceSpecification(smallClassId) {
    this.httpClient.post(`/api/device/device-specification/list`, {smallClassId, enabled: 'Y'})
      .subscribe((res: any) => {
        this.specificationList = res.data;
      });
  }

  listBrandModel(smallClassId) {
    this.httpClient.post(`/api/device/device-brand/brand/model/list`, {corp: this.demanderCorp, smallClassId})
      .subscribe((res: any) => {
        const tmpList: any[] = [];
        res.data.forEach(brand => {
          const brandObj = {
            value: brand.id,
            label: brand.name,
            children: []
          };
          if (brand.deviceModelDtoList && brand.deviceModelDtoList.length > 0) {
            brand.deviceModelDtoList.forEach(model => {
              const modelObject = {
                value: model.id,
                label: model.name,
                isLeaf: true
              };
              brandObj.children.push(modelObject);
            });
          }
          tmpList.push(brandObj);
        });
        this.brandModelList = tmpList;
      });
  }

  brandModelChange(event) {
    if (event && event.length > 0) {
      this.validateForm.patchValue({
        brandId: event[0],
        modelId: event[1]
      });
    } else {
      this.validateForm.controls.brandId.reset();
      this.validateForm.controls.modelId.reset();
    }
  }

  warrantyDateChange(event: Array<any>) {
    if (event.length > 0) {
      this.validateForm.patchValue({
        warrantyStartDate: event[0],
        warrantyEndDate: event[1]
      });
      const date = new Date();
      if( date > this.validateForm.controls.warrantyEndDate.value ||
        date < this.validateForm.controls.warrantyStartDate.value ) {
        this.validateForm.controls.warrantyStatus.setValue('2');
      } else {
        this.validateForm.controls.warrantyStatus.setValue('1');
      }
    } else {
      this.validateForm.patchValue({
        warrantyStartDate: null,
        warrantyEndDate: null
      });
      this.validateForm.controls.warrantyStatus.setValue(null);
    }
  }

  serialChange() {
    if (this.validateForm.value.serial) {
      let serial = this.validateForm.value.serial || '';
      serial = serial.replace(/，/ig, '').replace(/,/ig, '').trim().toLocaleUpperCase();
      if (serial === this.validateForm.value.serial) {
      } else {
        setTimeout(() => {
          this.validateForm.patchValue({
            serial
          });
        }, 1);
      }
    }
  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    value.deviceId = this.deviceInfo.deviceId;
    this.spinning = true;
    this.httpClient
      .post('/api/device/device-info/update',
        value)
      .pipe(
        finalize(() => {
          this.spinning = false;
        })
      )
      .subscribe(() => {
        this.modal.destroy('submit');
      });
  }

  destroyModal(): void {
    this.modal.destroy();
  }

}
