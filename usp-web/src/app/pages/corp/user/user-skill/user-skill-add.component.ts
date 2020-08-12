import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'app-corp-user-skill-add',
  templateUrl: 'user-skill-add.component.html'
})
export class UserSkillAddComponent implements OnInit {

  validateForm: FormGroup;

  deviceLargeClassOptionLoading = false;
  deviceLargeClassOptions = [];

  deviceSmallClassOptionLoading = false;
  deviceSmallClassOptions = [];

  userOptionLoading = false;
  userOptions = [];

  demanderCorpOptionLoading = false;
  demanderCorpOptions = [];

  deviceBrandOptionLoading = false;
  deviceBrandOptions = [];

  deviceModelOptionLoading = false;
  deviceModelOptions = [];

  workTypeOptionLoading = false;
  workTypeOptions = [];

  spinning = false;
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private userService: UserService,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      userId: [null, [Validators.required]],
      demanderCorp: [null, [Validators.required]],
      workTypeList: [],
      largeClassId: [],
      smallClassIdList: [],
      brandIdList: [],
      deviceTypeIdList: [],
      modelIdList: []
    });
  }

  ngOnInit(): void {
    this.matchUser();
  }

  submitForm(value: any): void {
    this.spinning = true;
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    this.httpClient
      .post('/api/anyfix/staff-skill/add',
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

  /**
   * 模糊查询设备大类
   * @param largeClassName 大类名称
   */
  matchDeviceLargeClass(largeClassName?) {
    const payload = {
      corp: this.validateForm.controls.demanderCorp.value,
      enabled: 'Y',
      matchFilter: largeClassName
    };
    this.deviceLargeClassOptionLoading = true;
    this.httpClient
      .post('/api/device/device-large-class/match', payload)
      .pipe(
        finalize(() => {
          this.deviceLargeClassOptionLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.deviceLargeClassOptions = res.data;
      });
  }

  /**
   * 设备大类改变
   */
  changeDeviceLargeClass() {
    this.validateForm.controls.smallClassIdList.setValue(null);
    this.matchDeviceSmallClass();
  }

  /**
   * 模糊查询设备类型
   * @param smallClassName 设备类型名称
   */
  matchDeviceSmallClass(smallClassName?) {
    const payload = {
      largeClassId: this.validateForm.controls.largeClassId.value,
      enabled: 'Y',
      matchFilter: smallClassName
    };
    this.deviceSmallClassOptionLoading = true;
    this.httpClient
      .post('/api/device/device-small-class/match', payload)
      .pipe(
        finalize(() => {
          this.deviceSmallClassOptionLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.deviceSmallClassOptions = res.data;
      });
  }

  /**
   * 改变设备类型
   */
  changeDeviceSmallClass() {
    this.validateForm.controls.modelIdList.setValue(null);
    this.matchDeviceModel();
  }

  /**
   * 模糊查询设备型号
   * @param modelName 型号名称
   */
  matchDeviceModel(modelName?): void {
    const payload = {
      corp: this.validateForm.controls.demanderCorp.value,
      brandIdList: this.validateForm.controls.brandIdList.value,
      smallClassIdList: this.validateForm.controls.smallClassIdList.value,
      enabled: 'Y',
      matchFilter: modelName
    };
    this.deviceModelOptionLoading = true;
    this.httpClient
      .post('/api/device/device-model/match', payload)
      .pipe(
        finalize(() => {
          this.deviceModelOptionLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.deviceModelOptions = res.data;
      });
  }

  /**
   * 改变设备品牌
   */
  changeDeviceBrand() {
    this.validateForm.controls.modelIdList.setValue(null);
    this.matchDeviceModel();
  }

  /**
   * 模糊查询设备品牌
   * @param brandName 品牌名称
   */
  matchDeviceBrand(brandName?): void {
    const payload = {
      corp: this.validateForm.controls.demanderCorp.value,
      enabled: 'Y',
      matchFilter: brandName
    };
    this.deviceBrandOptionLoading = true;
    this.httpClient
      .post('/api/device/device-brand/match', payload)
      .pipe(
        finalize(() => {
          this.deviceBrandOptionLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.deviceBrandOptions = res.data;
      });
  }

  /**
   * 工单类型列表
   */
  listWorkType() {
    const corpId = this.validateForm.controls.demanderCorp.value;
    this.workTypeOptionLoading = true;
    this.httpClient
      .post('/api/anyfix/work-type/list', {demanderCorp: corpId, enabled: 'Y'})
      .pipe(
        finalize(() => {
          this.workTypeOptionLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.workTypeOptions = res.data;
      });
  }

  /**
   * 改变委托商
   */
  changeCorp() {
    this.validateForm.controls.workTypeList.setValue(null);
    this.validateForm.controls.largeClassId.setValue(null);
    this.validateForm.controls.smallClassIdList.setValue(null);
    this.validateForm.controls.brandIdList.setValue(null);
    this.validateForm.controls.modelIdList.setValue(null);
    this.matchDeviceLargeClass();
    this.matchDeviceModel();
    this.matchDeviceBrand();
  }

  /**
   * 委托商列表
   */
  listDemanderCorp() {
    this.demanderCorpOptionLoading = true;
    this.httpClient
      .post('/api/anyfix/demander-service/demander/list', {enabled: 'Y'})
      .pipe(
        finalize(() => {
          this.demanderCorpOptionLoading = false;
        })
      )
      .subscribe((res: any) => {
        this.demanderCorpOptions = res.data;
      });
  }

  /**
   * 模糊查询人员
   * @param userName 人员姓名
   */
  matchUser(userName?) {
    const payload = {
      corpId: this.userService.currentCorp.corpId,
      matchFilter: userName
    };
    this.userOptionLoading = true;
    this.httpClient
      .post('/api/uas/corp-user/match', payload)
      .pipe(
        finalize(() => {
          this.userOptionLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.userOptions = res.data;
      });
  }

}
