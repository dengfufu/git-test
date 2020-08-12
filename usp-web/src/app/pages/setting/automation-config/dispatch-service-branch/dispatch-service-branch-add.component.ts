import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {AutomationConfigService} from '../automation-config.service';
import {Result} from '@core/interceptor/result';
import {AreaService} from '@core/area/area.service';
import { ZorroUtils } from '@util/zorro-utils';

@Component({
  selector: 'app-dispatch-service-branch-add',
  templateUrl: 'dispatch-service-branch-add.component.html'
})

export class DispatchServiceBranchAddComponent implements OnInit {

  ZorroUtils = ZorroUtils;
  @Input() dispatchServiceBranch;
  @Input() pageType;
  @Input() serviceCorp: string;
  @Input() serviceName: string;

  validateForm: FormGroup;

  demanderCorpOptionLoading = false;
  serviceBranchOptionLoading = false;

  largeClassOptionLoading = false;
  smallClassOptionLoading = false;
  brandOptionLoading = false;
  modelOptionLoading = false;
  deviceBranchOptionLoading = false;


  demanderCorpOption = [];
  largeClassOption = [];
  smallClassOption = [];
  brandOption = [];
  modelOption = [];
  deviceBranchOption = [];
  serviceBranchOption = [];
  districts = [];
  workTypeOptions: any[] = [];

  isSubmit = false;
  spinning = false;

  nzFilterOption = () => true;


  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient,
              public automationConfigService: AutomationConfigService,
              private nzMessageService: NzMessageService,
              private areaService: AreaService) {
    this.validateForm = this.formBuilder.group({
      /*demanderCorp: [null, [Validators.required]],*/
      demanderCorp: [null, []],
      serviceBranch: [null, [Validators.required]],
      handleType: [null, [Validators.required]],
      largeClassId: [null, []],
      smallClassId: [null, []],
      brandId: [null, []],
      modelId: [null, []],
      district: [null, [Validators.required]],
      deviceBranch: [null, []],
      workType: [null,[]]
    });
  }

  ngOnInit() {
    console.log(this.dispatchServiceBranch);
    this.searchServiceBranch();
    this.httpClient
      .get('/api/uas/area/list')
      .subscribe((res: any) => {
        this.districts = res.data;
        if (this.pageType === 'mod') {
          this.serviceCorp = this.dispatchServiceBranch.serviceCorp;
          this.serviceName = this.dispatchServiceBranch.serviceCorpName;
          this.validateForm.controls.demanderCorp.setValue(this.dispatchServiceBranch.demanderCorp);
          this.validateForm.controls.serviceBranch.setValue(this.dispatchServiceBranch.serviceBranch);
          this.validateForm.controls.handleType.setValue(this.dispatchServiceBranch.handleType);
          this.validateForm.controls.largeClassId.setValue(this.dispatchServiceBranch.largeClassId);
          this.validateForm.controls.smallClassId.setValue(this.dispatchServiceBranch.smallClassId.split(','));
          this.validateForm.controls.brandId.setValue(this.dispatchServiceBranch.brandId.split(','));
          this.validateForm.controls.modelId.setValue(this.dispatchServiceBranch.modelId.split(','));
          this.validateForm.controls.district.setValue(this.areaService.getAreaListByDistrict(this.dispatchServiceBranch.district));
          this.validateForm.controls.deviceBranch.setValue(this.dispatchServiceBranch.deviceBranch.split(','));
          this.validateForm.controls.workType.setValue(this.dispatchServiceBranch.workType);

          this.searchDemanderCorp();
          this.searchServiceBranch(this.dispatchServiceBranch.serviceBranchName);
          this.loadWorkType();
          /* this.searchBrand();
          this.searchDeviceBranch();
          this.searchLargeClass();
          this.searchSmallClass();
          this.searchModel();*/
        }
      });

  }


  submitForm(value: any) {
    this.isSubmit = true;
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    if (this.validateForm.valid) {
      const smallClassId = this.automationConfigService.getStrNameFromArray(this.validateForm.value.smallClassId);

      const brandId = this.automationConfigService.getStrNameFromArray(this.validateForm.value.brandId);

      const modelId = this.automationConfigService.getStrNameFromArray(this.validateForm.value.modelId);

      const deviceBranch = this.automationConfigService.getStrNameFromArray(this.validateForm.value.deviceBranch);

      let district = '';
      if (this.validateForm.value.district && this.validateForm.value.district.length > 0){
        district = this.validateForm.value.district[this.validateForm.value.district.length - 1];
      }

      let params: {};
      let url = '/api/anyfix/work-dispatch-service-branch/add';
      params = {
        brandId,
        deviceBranch,
        district,
        largeClassId: this.validateForm.value.largeClassId,
        modelId,
        serviceCorp: this.serviceCorp,
        serviceBranch: this.validateForm.value.serviceBranch,
        handleType: this.validateForm.value.handleType,
        smallClassId,
        demanderCorp: this.validateForm.value.demanderCorp,
        workType: this.validateForm.value.workType


      };

      if (this.pageType === 'mod') {
        url = '/api/anyfix/work-dispatch-service-branch/update';
        params = {
          brandId,
          deviceBranch,
          district,
          largeClassId: this.validateForm.value.largeClassId,
          modelId,
          serviceCorp: this.serviceCorp,
          serviceBranch: this.validateForm.value.serviceBranch,
          handleType: this.validateForm.value.handleType,
          smallClassId,
          demanderCorp: this.validateForm.value.demanderCorp,
          id: this.dispatchServiceBranch.id,
          conditionId: this.dispatchServiceBranch.conditionId,
          workType: this.validateForm.value.workType

        };
      }
      this.spinning = true;
      this.httpClient
        .post(url, params)
        .pipe(
          finalize(() => {
            this.spinning = false;
            this.cdf.markForCheck();
          })
        )
        .subscribe(() => {
          this.modal.destroy('update');
        });
    }
  }

  destroyModal() {
    this.modal.destroy();
  }

  searchDemanderCorp() {
    this.demanderCorpOptionLoading = true;
    this.httpClient
      .post('/api/anyfix/demander-service/demander/list', {enabled: 'Y'})
      .pipe(
        finalize(() => {
          this.demanderCorpOptionLoading = false;
        })
      )
      .subscribe((res: any) => {
        if (res.code === 0) {
          this.demanderCorpOption = res.data;
        }
      });

  }


  searchLargeClass() {
    if (this.validateForm.value.demanderCorp > 0 ) {
      this.largeClassOptionLoading = true;
      this.httpClient
        .post('/api/device/device-large-class/list', {corp: this.validateForm.value.demanderCorp})
        .pipe(
          finalize(() => {
            this.largeClassOptionLoading = false;
          })
        )
        .subscribe((res: any) => {
          if (res.code === 0) {
            this.largeClassOption = res.data;
          }
        });
    }else {
      this.nzMessageService.warning('请先选择委托商');
    }
  }


  searchSmallClass(largeClass?) {
    /*if (!this.validateForm.value.demanderCorp) {
      this.nzMessageService.warning('请先选择委托商');
      return;
    }*/
    if (this.validateForm.value.demanderCorp > 0 ) {
      this.smallClassOptionLoading = true;
      this.httpClient
        .post('/api/device/device-small-class/list',
          {largeClassId: largeClass, corp: this.validateForm.value.demanderCorp})
        .pipe(
          finalize(() => {
            this.smallClassOptionLoading = false;
          })
        )
        .subscribe((res: any) => {
          if (res.code === 0) {
            this.smallClassOption = res.data;
          }
        });
    }else {
      this.nzMessageService.warning('请先选择委托商');
    }
  }

  searchBrand() {
    if (this.validateForm.value.demanderCorp > 0 ) {
      this.brandOptionLoading = true;
      this.httpClient
        .post('/api/device/device-brand/list', {corpId: this.validateForm.value.demanderCorp})
        .pipe(
          finalize(() => {
            this.brandOptionLoading = false;
          })
        )
        .subscribe((res: any) => {
          if (res.code === 0) {
            this.brandOption = res.data;
          }
        });
    }else {
      this.nzMessageService.warning('请先选择委托商');
    }
  }

  searchModel() {
    if (this.validateForm.value.demanderCorp > 0 ) {
      this.modelOptionLoading = true;
      this.httpClient
        .post('/api/device/device-model/list', {corp: this.validateForm.value.demanderCorp})
        .pipe(
          finalize(() => {
            this.modelOptionLoading = false;
          })
        )
        .subscribe((res: any) => {
          if (res.code === 0) {
            this.modelOption = res.data;
          }
        });
    }else {
      this.nzMessageService.warning('请先选择委托商');
    }
  }

  searchDeviceBranch() {
    if (this.validateForm.value.demanderCorp > 0 ) {
      this.deviceBranchOptionLoading = true;
      this.httpClient
        .post('/api/anyfix/device-branch/list' , {customCorp : this.validateForm.value.demanderCorp})
        .pipe(
          finalize(() => {
            this.deviceBranchOptionLoading = false;
          })
        )
        .subscribe((res: any) => {
          if (res.code === 0) {
            this.deviceBranchOption = res.data;
          }
        });
    }else {
      this.nzMessageService.warning('请先选择委托商');
    }
  }

  changeServiceBranch() {
    if (!this.validateForm.value.serviceBranch) {
      this.searchServiceBranch();
    }
  }
  searchServiceBranch(name?) {
    const params = {branchName: name};
    this.serviceBranchOptionLoading = true;
    this.httpClient
      .post('/api/anyfix/service-branch/match', params)
      .pipe(
        finalize(() => {
          this.serviceBranchOptionLoading = false;
        })
      )
      .subscribe((res: any) => {
        if (res.code === 0) {
          this.serviceBranchOption = res.data;
        }
      });
  }


  demanderCorpChange() {
    // 委托商改变，选择成功的设备型号置为空
    this.validateForm.controls.modelId.setValue(null);
    this.validateForm.controls.brandId.setValue(null);
    this.validateForm.controls.deviceBranch.setValue(null);
    this.validateForm.controls.largeClassId.setValue(null);
    this.validateForm.controls.smallClassId.setValue(null);
    this.validateForm.controls.workType.setValue(null);

  }

  largeClassChange() {
    this.validateForm.controls.smallClassId.setValue(null);
  }

  /**
   * 列出工单类型
   */
  loadWorkType() {
    this.httpClient.post(`/api/anyfix/work-type/list`,
      {demanderCorp:this.validateForm.value.demanderCorp,enabled: 'Y'}).subscribe((result: Result) => {
      if (result.code === 0) {
        this.workTypeOptions = result.data;
      }
    });
  }

  areaChange(event) {
    if (event === null) {
      return;
    }
  }
}
