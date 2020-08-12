import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {UserService} from '@core/user/user.service';
import {Result} from '@core/interceptor/result';
import {finalize} from 'rxjs/operators';
import {ZonValidators} from '@util/zon-validators';

@Component({
  selector: 'app-device-info-add',
  templateUrl: 'device-info-add.component.html'
})
export class DeviceInfoAddComponent implements OnInit {

  validateForm: FormGroup;
  deviceClassList: Array<any> = [];
  brandModelList: Array<any> = [];
  branchList: Array<any>;
  serviceCorpList: Array<any>;
  customCorpList: Array<any>;
  specificationList: Array<any> = [];
  demanderCorpList: Array<any> = [];
  customFieldDataList: any[] = [];
  demanderCorp = '';
  currentCorpId = this.userService.currentCorp.corpId;
  spinning = false;
  isDemanderCurrentCorp = false;
  defaultServiceCorpList = [{
    serviceCorp: this.currentCorpId,
    serviceCorpName: this.userService.currentCorp.corpName
  }];
  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private modalService: NzModalService,
              private nzMessageService: NzMessageService,
              private httpClient: HttpClient,
              private userService: UserService) {
    this.validateForm = this.formBuilder.group({
      demanderCorp: [null, Validators.required],
      brandModel: [null, Validators.required],
      deviceClass: [null, Validators.required],
      largeClassId: [null, [Validators.required]],
      smallClassId: [null, [Validators.required]],
      specificationId: [],
      brandId: [null, [Validators.required]],
      modelId: [null, [Validators.required]],
      serial: [null, [Validators.required]],
      customId: [null, [Validators.required]],
      contactName:[],
      contactPhone:[null,ZonValidators.phoneOrMobile()],
      serviceCorp: [],
      factoryDate: [],
      purchaseDate: [],
      warrantyMode: ['20', [ZonValidators.required('维保方式')]],
      contNo: [null, [ZonValidators.maxLength(30, '维保合同号')]],
      warrantyDate: [],
      warrantyStartDate: [],
      warrantyEndDate: [],
      warrantyStatus: [],
      warrantyNote: []
    });
  }

  ngOnInit(): void {
    this.listDemander();
  }

  reGetData() {
    this.listDeviceClass();
    this.listServiceCorp();
    this.listCustomCorp();
  }

  // 服务商列表
  listServiceCorp() {
    if(!this.isDemanderCurrentCorp) {
      this.serviceCorpList = this.defaultServiceCorpList;
      this.validateForm.controls.serviceCorp.setValue(this.currentCorpId);
    } else {
      this.httpClient.post('/api/anyfix/demander-service/service/list', {
        demanderCorp: this.demanderCorp,
        enabled: 'Y'
      })
        .subscribe((res: any) => {
          this.serviceCorpList = res.data || [];
        });
    }
  }

  // 客户列表
  listCustomCorp() {
    this.httpClient.post('/api/anyfix/demander-custom/custom/list', {demanderCorp: this.demanderCorp,
      enabled: 'Y'})
      .subscribe((res: any) => {
        this.customCorpList = res.data;
      });
  }

  /**
   * 获取委托商
   */
  listDemander() {
    const params = {enabled: 'Y'};
    this.httpClient.get('/api/anyfix/demander/list').subscribe((result: Result) => {
      const demanderCorpList = result.data || [];
      if (demanderCorpList.length > 0) {
        this.demanderCorpList = demanderCorpList;
        this.demanderCorp = this.demanderCorpList[0].demanderCorp;
        this.validateForm.controls.demanderCorp.setValue(this.demanderCorp);
      }
    });
  }

  // 设备分类列表
  listDeviceClass() {
    this.httpClient.post('/api/device/device-class/list', {
      corpId: this.demanderCorp
    })
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
      specificationId: null,
      brandId: null,
      modelId: null
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

  // 设备型号列表
  listBrandModel(smallClassId) {
    this.httpClient.post(`/api/device/device-brand/brand/model/list`, {
      smallClassId,
      corp: this.demanderCorp
    })
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

  onChange(event: Array<any>) {
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

  submitForm(value: any): void {
    // 存入自定义字段数据
    if(this.customFieldDataList !== undefined && this.customFieldDataList.length > 0 ) {
      value.customFieldDataList = this.customFieldDataList;
    }

    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    this.spinning = true;
    this.httpClient.post('/api/device/device-info/add', value)
      .pipe(
        finalize(() => {
          this.spinning = false;
        })
      )
      .subscribe((result: Result) => {
        if (result && result.code === 0) {
          this.nzMessageService.success('添加成功');
          this.modal.destroy('submit');
        }
      });
  }

  destroyModal(): void {
    this.modal.destroy();
  }

  // 收集自定义字段数据
  customFormChange(customFieldDataList: { fieldId: any; fieldValue: any; }[]) {
    this.customFieldDataList = customFieldDataList;
  }

  demanderCorpChange(event: any) {
    this.demanderCorp = event? event : '';
    if (this.demanderCorp !== this.currentCorpId) {
      this.isDemanderCurrentCorp = false;
      this.validateForm.controls.serviceCorp.setValidators([Validators.required]);
    } else {
      this.isDemanderCurrentCorp = true;
      this.validateForm.controls.serviceCorp.clearValidators();
    }
    this.validateForm.controls.serviceCorp.updateValueAndValidity();
    this.reGetData();
    this.resetData();
  }

  resetData() {
    this.validateForm.controls.customId.reset();
    if(this.demanderCorp === '' || this.isDemanderCurrentCorp) {
      this.validateForm.controls.serviceCorp.reset();
    }
    this.validateForm.controls.specificationId.reset();
    this.validateForm.controls.deviceClass.reset();
    this.validateForm.controls.brandModel.reset();
  }

}
