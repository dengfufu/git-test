import {ChangeDetectorRef, Input, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';

@Component({
  selector: 'app-corp-user-skill-edit',
  templateUrl: 'user-skill-edit.component.html'
})
export class UserSkillEditComponent implements OnInit {

  @Input() userSkill;

  validateForm: FormGroup;

  deviceLargeClassOptionLoading = false;
  deviceLargeClassOptions = [];

  deviceSmallClassOptionLoading = false;
  deviceSmallClassOptions = [];

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
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      id: [null, [Validators.required]],
      userId: [null, [Validators.required]],
      demanderCorp: [null, [Validators.required]],
      workTypeList: [],
      largeClassId: [],
      smallClassIdList: [],
      brandIdList: [],
      modelIdList: []
    });
  }

  ngOnInit(): void {
    this.validateForm.controls.id.setValue(this.userSkill.id);
    this.validateForm.controls.userId.setValue(this.userSkill.userId);
    this.validateForm.controls.demanderCorp.setValue(this.userSkill.demanderCorp);
    this.validateForm.controls.workTypeList.setValue(this.userSkill.workTypeList);
    this.validateForm.controls.largeClassId.setValue(this.userSkill.largeClassId);
    this.validateForm.controls.smallClassIdList.setValue(this.userSkill.smallClassIdList);
    this.validateForm.controls.brandIdList.setValue(this.userSkill.brandIdList);
    this.validateForm.controls.modelIdList.setValue(this.userSkill.modelIdList);
    this.listDemanderCorp();
    this.listWorkType();
    this.matchDeviceLargeClass();
    this.matchDeviceSmallClass();
    this.matchDeviceBrand();
    this.matchDeviceModel();
  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    this.spinning = true;
    this.httpClient
      .post('/api/anyfix/staff-skill/update',
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
   * 改变设备品牌
   */
  changeDeviceBrand() {
    this.validateForm.controls.modelIdList.setValue(null);
    this.matchDeviceModel();
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

}
