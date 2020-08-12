import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {AutomationConfigService} from '../../automation-config/automation-config.service';
import {finalize} from 'rxjs/operators';
import {Result} from '@core/interceptor/result';
import {ZonValidators} from '@util/zon-validators';
import {AreaService} from '@core/area/area.service';
import { ZorroUtils } from '@util/zorro-utils';

@Component({
  selector: 'app-service-item-fee-rule-add',
  templateUrl: './service-item-fee-rule-add.component.html',
  styleUrls: ['./service-item-fee-rule-add.component.less']
})
export class ServiceItemFeeRuleAddComponent implements OnInit {

  ZorroUtils = ZorroUtils;
  // 添加或者修改
  @Input() type: any;
  @Input() serviceItemFeeRule: any;
  // 表单
  form: FormGroup;
  // 当前企业编号
  serviceCorp = this.userService.currentCorp.corpId;

  // 下拉框加载中
  isLoading = false;
  demanderCorpOptionLoading = false;
  customCorpOptionLoading = false;
  largeClassOptionLoading = false;
  smallClassOptionLoading = false;
  brandOptionLoading = false;
  modelOptionLoading = false;
  deviceBranchOptionLoading = false;
  serviceOtemOptionLoading = false;

  // 下拉框选项
  demanderCorpOptions = [];
  customCorpOptions = [];
  largeClassOptions = [];
  smallClassOptions = [];
  brandOptions = [];
  modelOptions = [];
  deviceBranchOptions = [];
  districtOptions = [];
  workTypeOptions = [];
  serviceItemOptions = [];

  constructor(
    private httpClient: HttpClient,
    private modalRef: NzModalRef,
    private formBuilder: FormBuilder,
    private userService: UserService,
    private automationConfigService: AutomationConfigService,
    private messageService: NzMessageService,
    private areaService: AreaService
  ) {
    this.form = this.formBuilder.group({
      ruleName: [null, [ZonValidators.required(), ZonValidators.maxLength(100), ZonValidators.notEmptyString()]],
      serviceItemId: [null, [Validators.required]],
      unitPrice: [0, [Validators.required, Validators.pattern(/^\d{1,8}\.?\d{0,2}$/)]],
      dateRange: [null, Validators.required],
      demanderCorp: [null, [Validators.required]],
      customId: [null],
      workType: [null, Validators.required],
      serviceMode: [],
      largeClassId: [],
      smallClassId: [],
      brandId: [],
      modelId: [],
      district: [],
      deviceBranch: []
    });
  }

  ngOnInit() {
    // 如果为修改页面，则初始化表单
    if (this.type === 'mod') {
      this.form.controls.dateRange.setValue([this.serviceItemFeeRule.startDate, this.serviceItemFeeRule.endDate]);
      this.form.controls.smallClassId.setValue(this.serviceItemFeeRule.smallClassId.split(','));
      this.form.controls.brandId.setValue(this.serviceItemFeeRule.brandId.split(','));
      this.form.controls.modelId.setValue(this.serviceItemFeeRule.modelId.split(','));
      this.form.controls.deviceBranch.setValue(this.serviceItemFeeRule.deviceBranch.split(','));
      console.log(this.areaService.getAreaListByDistrict(this.serviceItemFeeRule.district));
      this.form.controls.district.setValue(this.areaService.getAreaListByDistrict(this.serviceItemFeeRule.district));
      this.form.patchValue({
        ruleName: this.serviceItemFeeRule.ruleName,
        serviceItemId: this.serviceItemFeeRule.serviceItemId,
        unitPrice: this.serviceItemFeeRule.unitPrice,
        demanderCorp: this.serviceItemFeeRule.demanderCorp,
        customId: this.serviceItemFeeRule.customId,
        workType: this.serviceItemFeeRule.workType,
        largeClassId: this.serviceItemFeeRule.largeClassId
      });
    }
    this.loadDistrict();
    this.searchServiceItem();
    this.searchDemanderCorp();
    this.searchCustomCorp();
    this.searchSmallClass();
    this.searchModel();
    this.searchBrand();
    if (this.type === 'mod') {
      if (this.serviceItemFeeRule.demanderCorp && this.serviceItemFeeRule.demanderCorp !== '0') {
        this.searchLargeClass(this.serviceItemFeeRule.demanderCorp);
        this.loadWorkType(this.serviceItemFeeRule.demanderCorp);
      }
      if (this.serviceItemFeeRule.customId && this.serviceItemFeeRule.customId !== '0') {
        this.searchDeviceBranch(this.serviceItemFeeRule.customId);
      }
    }
  }

  // 数字框格式
  formatterRmb = (value: number) => `￥ ${value}`;
  parserRmb = (value: string) => value.replace('￥ ', '');

  loadDistrict() {
    this.httpClient
      .get('/api/uas/area/list')
      .subscribe((res: any) => {
        this.districtOptions = res.data;
      });
  }

  searchServiceItem() {
    this.serviceOtemOptionLoading = true;
    this.httpClient.post('/api/anyfix/service-item/list', {serviceCorp: this.serviceCorp})
      .pipe(
        finalize(() => {
          this.serviceOtemOptionLoading = false;
        })
      )
      .subscribe((res: any) => {
        this.serviceItemOptions = res.data;
      });
  }

  // 查询委托商下拉框
  searchDemanderCorp() {
    const params = {
      serviceCorp: this.serviceCorp,
      enabled: 'Y'
    };
    this.demanderCorpOptionLoading = true;
    this.httpClient.post('/api/anyfix/demander-service/demander/list', params)
      .pipe(
        finalize(() => {
          this.demanderCorpOptionLoading = false;
        })
      ).subscribe((res: any) => {
      this.demanderCorpOptions = res.data;
    });
  }

  demanderChange(event) {
    this.form.controls.customId.reset();
    this.form.controls.workType.reset();
    this.form.controls.largeClassId.reset();
    this.form.controls.smallClassId.reset();
    this.form.controls.brandId.reset();
    this.form.controls.modelId.reset();
    this.form.controls.deviceBranch.reset();
    this.searchCustomCorp();
    this.loadWorkType(event);
    this.searchLargeClass(event);
  }

  // 查询客户下拉框
  searchCustomCorp() {
    if (this.form.value.demanderCorp && this.form.value.demanderCorp !== '0') {
      this.customCorpOptionLoading = true;
      this.httpClient.post('/api/anyfix/demander-custom/custom/list', {demanderCorp: this.form.value.demanderCorp})
        .pipe(
          finalize(() => {
            this.customCorpOptionLoading = false;
          })
        ).subscribe((res: any) => {
        this.customCorpOptions = res.data;
      });
    } else {
      this.customCorpOptions = [];
    }
  }

  // 查询工单类型
  loadWorkType(corpId: any) {
    this.httpClient.post(`/api/anyfix/work-type/list`, {demanderCorp: corpId, enabled: 'Y'}).subscribe((result: Result) => {
      if (result.code === 0) {
        this.workTypeOptions = result.data;
      }
    });
  }

  // 设备大类
  searchLargeClass(corpId: string) {
    if (this.form.value.demanderCorp && this.form.value.demanderCorp !== '0') {
      this.largeClassOptionLoading = true;
      this.httpClient
        .post('/api/device/device-large-class/list', {corp: this.form.value.demanderCorp})
        .pipe(
          finalize(() => {
            this.largeClassOptionLoading = false;
          })
        )
        .subscribe((res: any) => {
          if (res.code === 0) {
            this.largeClassOptions = res.data;
          }
        });
    }
  }

  // 设备类型
  searchSmallClass(largeClass?: string) {
    if (this.form.value.demanderCorp && this.form.value.demanderCorp !== '0') {
      this.smallClassOptionLoading = true;
      this.httpClient
        .post('/api/device/device-small-class/list',
          {largeClassId: largeClass, corp: this.form.value.demanderCorp})
        .pipe(
          finalize(() => {
            this.smallClassOptionLoading = false;
          })
        )
        .subscribe((res: any) => {
          if (res.code === 0) {
            this.smallClassOptions = res.data;
          }
        });
    }
  }

  // 设备品牌
  searchBrand() {
    if (this.form.value.demanderCorp && this.form.value.demanderCorp !== '0') {
      this.brandOptionLoading = true;
      this.httpClient
        .post('/api/device/device-brand/list', {corp: this.form.value.demanderCorp})
        .pipe(
          finalize(() => {
            this.brandOptionLoading = false;
          })
        )
        .subscribe((res: any) => {
          if (res.code === 0) {
            this.brandOptions = res.data;
          }
        });
    }
  }

  // 设备型号
  searchModel() {
    if (this.form.value.demanderCorp && this.form.value.demanderCorp !== '0') {
      const params = {
        largeClassId: this.form.value.largeClassId,
        smallClassIdList: this.form.value.smallClassId,
        brandIdList: this.form.value.brandId,
        pageNum: 1,
        pageSize: 100,
        corp: this.form.value.demanderCorp
      };
      this.modelOptionLoading = true;
      this.httpClient
        .post('/api/device/device-model/query', params)
        .pipe(
          finalize(() => {
            this.modelOptionLoading = false;
          })
        )
        .subscribe((res: any) => {
          if (res.code === 0) {
            this.modelOptions = res.data.list;
          }
        });
    }
  }

  // 选中客户之后清空设备网点
  customChange(event) {
    this.form.controls.deviceBranch.reset();
    if (event && event.length > 0) {
      this.searchDeviceBranch(event);
    } else {
      this.deviceBranchOptions = [];
    }
  }

  // 设备网点
  searchDeviceBranch(customId: string) {
    if (customId && customId !== '0') {
      const params = {
        customId,
        enabled: 'Y'
      };
      this.deviceBranchOptionLoading = true;
      this.httpClient
        .post('/api/anyfix/device-branch/match', params)
        .pipe(
          finalize(() => {
            this.deviceBranchOptionLoading = false;
          })
        )
        .subscribe((res: any) => {
          if (res.code === 0) {
            this.deviceBranchOptions = res.data;
          }
        });
    }
  }

  // 取消
  cancel() {
    this.modalRef.destroy('cancel');
  }

  // 添加或修改提交
  submit() {
    if (!this.form.valid) {
      this.form.markAsDirty();
      this.form.updateValueAndValidity();
      return;
    }
    // 组装参数
    let params: any = {};
    const smallClassId = this.automationConfigService.getStrNameFromArray(this.form.value.smallClassId);
    const brandId = this.automationConfigService.getStrNameFromArray(this.form.value.brandId);
    const modelId = this.automationConfigService.getStrNameFromArray(this.form.value.modelId);
    const deviceBranch = this.automationConfigService.getStrNameFromArray(this.form.value.deviceBranch);
    let district = '';
    let startDate = '';
    let endDate = '';
    let url = '/api/anyfix/service-item-fee-rule/add';
    if (this.form.value.district && this.form.value.district.length > 0) {
      district = this.form.value.district[this.form.value.district.length - 1];
    }
    if (this.form.value.dateRange && this.form.value.dateRange.length === 2) {
      startDate = this.form.value.dateRange[0];
      endDate = this.form.value.dateRange[1];
    }
    // 组装参数
    params = {
      ruleName: this.form.value.ruleName,
      serviceItemId: this.form.value.serviceItemId,
      unitPrice: this.form.value.unitPrice,
      startDate,
      endDate,
      demanderCorp: this.form.value.demanderCorp,
      customId: this.form.value.customId,
      workType: this.form.value.workType,
      largeClassId: this.form.value.largeClassId,
      smallClassId,
      brandId,
      modelId,
      district,
      deviceBranch
    };
    // 如果是修改页面，则url与添加不同，且有主键
    if (this.type === 'mod') {
      params.ruleId = this.serviceItemFeeRule.ruleId;
      params.conditionId = this.serviceItemFeeRule.conditionId;
      url = '/api/anyfix/service-item-fee-rule/update';
    }
    console.log(params);
    this.httpClient.post(url, params)
      .subscribe((res: any) => {
        const msg = this.type === 'add' ? '添加成功！' : '修改成功！';
        this.messageService.success(msg);
        this.modalRef.destroy('submit');
      });
  }

}
