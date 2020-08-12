import {ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {AutomationConfigService} from '../automation-config.service';
import {Result} from '@core/interceptor/result';
import {AreaService} from '@core/area/area.service';
import { ZorroUtils } from '@util/zorro-utils';

@Component({
  selector: 'app-dispatch-service-corp-add',
  templateUrl: 'dispatch-service-corp-add.component.html'
})

/**
 * 进入页面时需要委托商数据（必须）
 */
export class DispatchServiceCorpAddComponent implements OnInit {

  ZorroUtils = ZorroUtils;
  @Input() dispatchServiceCorp;
  @Input() pageType: string;
  @Input() demanderCorp: string;
  @Input() demanderCorpName: string;

  validateForm: FormGroup;

  serviceCorpOptionLoading = false;
  largeClassOptionLoading = false;
  smallClassOptionLoading = false;
  brandOptionLoading = false;
  modelOptionLoading = false;
  deviceBranchOptionLoading = false;

  districts = [];

  serviceCorpOption = [];
  largeClassOption = [];
  smallClassOption = [];
  brandOption = [];
  modelOption = [];
  deviceBranchOption = [];
  workTypeOptions: any[] = [];

  isSubmit = false;
  spinning = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient,
              private automationConfigService: AutomationConfigService,
              private areaService: AreaService) {
    this.validateForm = this.formBuilder.group({
      serviceCorp: [null, [Validators.required]],
      largeClassId: [],
      smallClassId: [],
      brandId: [],
      modelId: [],
      district: [],
      deviceBranch: [],
      workType: []
    });
  }


  ngOnInit() {
    this.httpClient
      .get('/api/uas/area/list')
      .subscribe((res: any) => {
        this.districts = res.data;
        if (this.pageType === 'mod') {
          this.demanderCorp = this.dispatchServiceCorp.demanderCorp;
          this.demanderCorpName = this.dispatchServiceCorp.demanderCorpName;

          this.searchDeviceBranch();
          this.searchLargeClass();
          this.searchServiceCorp();
          this.searchSmallClass();
          this.searchModel();
          this.searchBrand();
          this.loadWorkType();

          this.validateForm.controls.serviceCorp.setValue(this.dispatchServiceCorp.serviceCorp);
          this.validateForm.controls.largeClassId.setValue(this.dispatchServiceCorp.largeClassId);
          this.validateForm.controls.smallClassId.setValue(this.dispatchServiceCorp.smallClassId.split(','));
          this.validateForm.controls.brandId.setValue(this.dispatchServiceCorp.brandId.split(','));
          this.validateForm.controls.modelId.setValue(this.dispatchServiceCorp.modelId.split(','));
          this.validateForm.controls.district.setValue(this.areaService.getAreaListByDistrict(this.dispatchServiceCorp.district));
          this.validateForm.controls.deviceBranch.setValue(this.dispatchServiceCorp.deviceBranch.split(','));

          this.validateForm.controls.workType.setValue(this.dispatchServiceCorp.workType);

        }

      });

  }

  submitForm(value: any) {
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
      let url = '/api/anyfix/work-dispatch-service-corp/add';
      params = {
        brandId,
        deviceBranch,
        district,
        largeClassId: this.validateForm.value.largeClassId,
        modelId,
        serviceCorp: this.validateForm.value.serviceCorp,
        smallClassId,
        demanderCorp: this.demanderCorp,
        workType: this.validateForm.value.workType,

      };

      if (this.pageType === 'mod') {
        url = '/api/anyfix/work-dispatch-service-corp/update';
        params = {
          brandId,
          deviceBranch,
          district,
          largeClassId: this.validateForm.value.largeClassId,
          modelId,
          serviceCorp: this.validateForm.value.serviceCorp,
          smallClassId,
          demanderCorp: this.demanderCorp,
          id: this.dispatchServiceCorp.id,
          conditionId: this.dispatchServiceCorp.conditionId,
          workType: this.validateForm.value.workType,

        };
      }
      this.isSubmit = true;
      this.spinning = true;
      this.httpClient
        .post(url, params)
        .pipe(
          finalize(() => {
            this.isSubmit = false;
            this.spinning = false;
            this.cdf.markForCheck();
          })
        )
        .subscribe(() => {
          this.modal.destroy('submit');
        });
    }
  }

  destroyModal() {
    this.modal.destroy();
  }


  searchServiceCorp() {
    const params = {
      demanderCorp :  this.demanderCorp,
      enabled : 'Y'
    };
    this.serviceCorpOptionLoading = true;
    this.httpClient
      .post('/api/anyfix/demander-service/service/list', params)
      .pipe(
        finalize(() => {
          this.serviceCorpOptionLoading = false;
        })
      )
      .subscribe((res: any) => {
        this.serviceCorpOption = res.data;
      });
  }


  searchLargeClass() {
    this.largeClassOptionLoading = true;
    this.httpClient
      .post('/api/device/device-large-class/list', {corpId: this.demanderCorp})
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
  }


  searchSmallClass(largeClass?: string) {
    this.smallClassOptionLoading = true;
    this.httpClient
      .post('/api/device/device-small-class/list',
        {largeClassId: largeClass, corp: this.demanderCorp})
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
  }

  searchBrand() {
    this.brandOptionLoading = true;
    this.httpClient
      .post('/api/device/device-brand/list', {corpId: this.demanderCorp})
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

  }

  searchModel() {
    this.modelOptionLoading = true;
    this.httpClient
      .post('/api/device/device-model/list',{corp: this.demanderCorp})
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

  }

  searchDeviceBranch() {
    const params  = {
      customCorp : this.demanderCorp,
      enabled : 'Y'
    };
    this.deviceBranchOptionLoading = true;
    this.httpClient
      .post('/api/anyfix/device-branch/list',params )
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
  }

  largeClassChange() {
    this.validateForm.controls.smallClassId.setValue(null);
  }

  /**
   * 列出工单类型
   */
  loadWorkType() {
    const params = {
      demanderCorp :  this.demanderCorp,
      enabled: 'Y'
    };
    this.httpClient.post(`/api/anyfix/work-type/list`, params).subscribe((result: Result) => {
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
