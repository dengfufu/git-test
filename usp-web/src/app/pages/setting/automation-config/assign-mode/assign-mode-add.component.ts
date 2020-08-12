import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {AutomationConfigService} from '../automation-config.service';
import {UserService} from '@core/user/user.service';
import {AreaService} from '@core/area/area.service';
import { ZorroUtils } from '@util/zorro-utils';

@Component({
  selector: 'app-assign-mode-add',
  templateUrl: 'assign-mode-add.component.html'
})
export class AssignModeAddComponent implements OnInit {

  ZorroUtils = ZorroUtils;
  @Input() assignMode;
  @Input() pageType;
  @Input() serviceCorp: string;
  @Input() serviceName: string;

  validateForm: FormGroup;

  demanderCorpOption = [];
  largeClassOption = [];
  smallClassOption = [];
  brandOption = [];
  modelOption = [];
  deviceBranchOption = [];
  workTypeOption = [];

  engineerOption = [];
  districts = [];

  demanderCorpOptionLoading = false;
  largeClassOptionLoading = false;
  smallClassOptionLoading = false;
  brandOptionLoading = false;
  modelOptionLoading = false;
  deviceBranchOptionLoading = false;
  workTypeOptionLoading = false;
  engineerOptionLoading = false;

  isSubmit = false;
  spinning = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient,
              private automationConfigService: AutomationConfigService,
              private userService: UserService,
              private nzMessageService: NzMessageService,
              private areaService: AreaService) {
    this.validateForm = this.formBuilder.group({
      demanderCorp: [null, [Validators.required]],
      autoMode: [null, [Validators.required]],
      manualMode: [null, [Validators.required]],
      workType: [null, []],

      largeClassId: [null, []],
      smallClassId: [null, []],
      brandId: [null, []],
      modelId: [null, []],
      district: [null, []],
      deviceBranch: [null, []],

      userList: [null, []],
      distance: [null, []],
      skilled: [null, []]
    });
  }

  ngOnInit() {
    this.httpClient
      .get('/api/uas/area/list')
      .subscribe((res: any) => {
        this.districts = res.data;

        if (this.pageType === 'mod') {
          this.serviceCorp = this.assignMode.serviceCorp;
          this.serviceName = this.assignMode.serviceCorpName;
          this.validateForm.controls.demanderCorp.setValue(this.assignMode.demanderCorp);
          this.validateForm.controls.autoMode.setValue(this.assignMode.autoMode);
          this.validateForm.controls.manualMode.setValue(this.assignMode.manualMode);
          this.validateForm.controls.userList.setValue(this.assignMode.userList.split(','));
          this.validateForm.controls.distance.setValue(this.assignMode.distance);
          this.validateForm.controls.skilled.setValue(this.assignMode.skilled);
          this.validateForm.controls.workType.setValue(this.assignMode.workType);
          this.validateForm.controls.largeClassId.setValue(this.assignMode.largeClassId);
          this.validateForm.controls.smallClassId.setValue(this.assignMode.smallClassId.split(','));
          this.validateForm.controls.brandId.setValue(this.assignMode.brandId.split(','));
          this.validateForm.controls.modelId.setValue(this.assignMode.modelId.split(','));

          this.validateForm.controls.district.setValue(this.areaService.getAreaListByDistrict(this.assignMode.district));

          this.validateForm.controls.deviceBranch.setValue(this.assignMode.deviceBranch.split(','));
          this.searchDemanderCorp();
          this.searchDeviceBranch();
          this.searchLargeClass();
          this.searchSmallClass();
          this.searchModel();
          this.searchBrand();
          this.searchWorkType();
          this.searchEngineer();
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

      let district = '';
      if (this.validateForm.value.district && this.validateForm.value.district.length > 0){
        district = this.validateForm.value.district[this.validateForm.value.district.length - 1];
      }


      const deviceBranch = this.automationConfigService.getStrNameFromArray(this.validateForm.value.deviceBranch);


      const userList = this.automationConfigService.getStrNameFromArray(this.validateForm.value.userList);

      let params: {};
      let url = '/api/anyfix/work-assign-mode/add';
      params = {
        brandId,
        deviceBranch,
        district,
        largeClassId: this.validateForm.value.largeClassId,
        modelId,
        serviceCorp: this.serviceCorp,
        smallClassId,
        demanderCorp: this.validateForm.value.demanderCorp,
        workType: this.validateForm.value.workType,
        autoMode: this.validateForm.value.autoMode,
        manualMode: this.validateForm.value.manualMode,
        userList,
        distance: this.validateForm.value.distance,
        skilled: this.validateForm.value.skilled === 'Y' ? 1 : 0

      };

      if (this.pageType === 'mod') {
        url = '/api/anyfix/work-assign-mode/update';
        params = {
          brandId,
          deviceBranch,
          district,
          largeClassId: this.validateForm.value.largeClassId,
          modelId,
          serviceCorp: this.serviceCorp,
          smallClassId,
          demanderCorp: this.validateForm.value.demanderCorp,
          workType: this.validateForm.value.workType,
          autoMode: this.validateForm.value.autoMode,
          manualMode: this.validateForm.value.manualMode,
          userList,
          distance: this.validateForm.value.distance,
          skilled: this.validateForm.value.skilled === 'Y' ? 1 : 0,
          id: this.assignMode.id,
          conditionId: this.assignMode.conditionId,
          assignRule: this.assignMode.assignRule

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
          this.modal.destroy('submit');
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
        this.demanderCorpOption = res.data;
      });
  }


  searchLargeClass() {
    if (!this.validateForm.value.demanderCorp) {
      this.nzMessageService.warning('请先选择委托商');
      return;
    }
    this.largeClassOptionLoading = true;
    this.httpClient
      .post('/api/device/device-large-class/list', {corpId: this.validateForm.value.demanderCorp})
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

  searchSmallClass(largeClass?) {
    if (!this.validateForm.value.demanderCorp) {
      this.nzMessageService.warning('请先选择委托商');
      return;
    }
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
  }

  searchBrand() {
    if (!this.validateForm.value.demanderCorp) {
      this.nzMessageService.warning('请先选择委托商');
      return;
    }
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

  }

  searchModel() {
    if (!this.validateForm.value.demanderCorp) {
      this.nzMessageService.warning('请先选择委托商');
      return;
    }
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

  }

  searchDeviceBranch() {
    if (!this.validateForm.value.demanderCorp) {
      this.nzMessageService.warning('请先选择委托商');
      return;
    }
    this.deviceBranchOptionLoading = true;
    this.httpClient
      .post('/api/anyfix/device-branch/list' , {customCorp : this.validateForm.value.demanderCorp })
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

  searchWorkType() {
    if (!this.validateForm.value.demanderCorp) {
      this.nzMessageService.warning('请先选择委托商');
      return;
    }
    this.workTypeOptionLoading = true;
    this.httpClient
      .post('/api/anyfix/work-type/list', {demanderCorp: this.validateForm.value.demanderCorp,enabled:'Y'})
      .pipe(
        finalize(() => {
          this.workTypeOptionLoading = false;
        })
      )
      .subscribe((res: any) => {
        this.workTypeOption = res.data;
      });
  }

  searchEngineer() {
    this.engineerOptionLoading = true;
    this.httpClient
      .post('/api/uas/corp-user/user/list',{})
      .pipe(
        finalize(() => {
          this.engineerOptionLoading = false;
        })
      )
      .subscribe((res: any) => {
        this.engineerOption = res.data;
      });
  }

  demanderCorpChange() {
    // 委托商改变，选择成功的设备型号置为空
    this.validateForm.controls.workType.setValue(null);
    this.validateForm.controls.modelId.setValue(null);
    this.validateForm.controls.brandId.setValue(null);
    this.validateForm.controls.deviceBranch.setValue(null);
    this.validateForm.controls.largeClassId.setValue(null);
    this.validateForm.controls.smallClassId.setValue(null);
  }

  largeClassChange() {
    this.validateForm.controls.smallClassId.setValue(null);
  }

  areaChange(event) {
    if (event === null) {
      return;
    }
  }
}
