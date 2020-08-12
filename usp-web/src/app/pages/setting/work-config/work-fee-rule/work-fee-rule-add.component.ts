import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {finalize} from 'rxjs/operators';
import {AutomationConfigService} from '../../automation-config/automation-config.service';
import {Result} from '@core/interceptor/result';
import {ZonValidators} from '@util/zon-validators';
import {AreaService} from '@core/area/area.service';
import { ZorroUtils } from '@util/zorro-utils';

@Component({
  selector: 'app-work-fee-rule-add',
  templateUrl: './work-fee-rule-add.component.html',
  styleUrls: ['./work-fee-rule-add.component.less']
})
export class WorkFeeRuleAddComponent implements OnInit {

  // add=添加，mod=编辑，copy=复制
  @Input() type: any;
  @Input() assortFee: any;
  ZorroUtils = ZorroUtils;
  // 表单
  form: FormGroup;
  // 当前企业编号
  serviceCorp = this.userService.currentCorp.corpId;

  // 下拉框加载中
  demanderCorpOptionLoading = false;
  customCorpOptionLoading = false;
  largeClassOptionLoading = false;
  smallClassOptionLoading = false;
  specificationOptionLoading = false;
  brandOptionLoading = false;
  modelOptionLoading = false;

  // 下拉框选项
  demanderCorpOptions = [];
  customCorpOptions = [];
  largeClassOptions = [];
  smallClassOptions = [];
  specificationOptions = [];
  brandOptions = [];
  modelOptions = [];
  districtOptions = [];
  workTypeOptions = [];
  serviceItemOptions = [];

  // 已选择的行政区划
  selectedDistrictList: any[] = [];
  // 当前选择的行政区划
  selectDistrict: any = {};
  // 行政区划当前选中的option，包含label
  selectDistrictOption: any[] = [];

  // 提交中
  submitLoading = false;

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
      assortName: [null, [ZonValidators.required('费用名称'),
        ZonValidators.maxLength(100, '费用名称'), ZonValidators.notEmptyString('费用名称')]],
      amount: [null, [ZonValidators.required('费用金额'), ZonValidators.intOrFloat('费用金额')]],
      note: [null, [ZonValidators.maxLength(200, '费用说明')]],
      startDate: [], // 适用起始日期
      endDate: [],  // 适用截止日期
      demanderCorp: [null, [ZonValidators.required('委托商')]], // 委托商
      customId: [], // 客户关系编号
      workType: [], // 工单类型
      serviceMode: [], // 服务方式
      serviceItem: [], // 服务项目
      together: [false],
      largeClassId: [], // 设备大类
      smallClassId: [], // 设备小类
      specification: [], // 设备规格
      brandId: [], // 设备品牌
      modelId: [], // 设备型号
      zone: [], // 分布
      districtNegate: ['N'], // 行政区划取反
      enabled: [true, [ZonValidators.required('是否可用')]] // 是否可用
    });
  }

  ngOnInit() {
    // 如果为修改或复制页面，则初始化表单
    if (this.type === 'mod' || this.type === 'copy') {
      this.form.controls.startDate.setValue(this.assortFee.startDate);
      this.form.controls.endDate.setValue(this.assortFee.endDate);
      this.form.controls.smallClassId.setValue((this.assortFee.smallClassId || '').length > 0 ?
        this.assortFee.smallClassId.split(',') : []);
      this.form.controls.specification.setValue((this.assortFee.specification || '').length > 0 ?
        this.assortFee.specification.split(',') : []);
      this.form.controls.brandId.setValue(this.assortFee.brandId ? this.assortFee.brandId.split(',') : null);
      this.form.controls.modelId.setValue(this.assortFee.modelId ? this.assortFee.modelId.split(',') : null);
      this.form.controls.workType.setValue(this.assortFee.workType ? this.assortFee.workType.split(',') : null);
      this.form.patchValue({
        assortName: this.assortFee.assortName,
        amount: this.assortFee.amount,
        enabled: this.assortFee.enabled === 'Y' ? true : false,
        together: this.assortFee.together === 'Y' ? true : false,
        serviceMode: this.assortFee.serviceMode === 0 ? null : this.assortFee.serviceMode,
        serviceItem: this.assortFee.serviceItem,
        demanderCorp: this.assortFee.demanderCorp,
        customId: this.assortFee.customId === '0' ? null : this.assortFee.customId,
        largeClassId: this.assortFee.largeClassId === '0' ? null : this.assortFee.largeClassId,
        districtNegate: this.assortFee.districtNegate,
        note: this.assortFee.note,
        zone: this.assortFee.zone === 0 ? null : this.assortFee.zone
      });
      const districtList: any[] = this.assortFee.district ? this.assortFee.district.split(',') : [];
      const districtNameList: any[] = this.assortFee.districtName ?
        this.assortFee.districtName.split(',') : [];
      this.selectedDistrictList = districtList.map((item: any, index) => {
        return {
          value: item,
          label: districtNameList[index]
        };
      });
      if (this.assortFee.demanderCorp && this.assortFee.demanderCorp !== '0') {
        this.searchLargeClass(this.assortFee.demanderCorp);
        this.loadWorkType(this.assortFee.demanderCorp);
        this.loadServiceItem(this.assortFee.demanderCorp);
      }
    }
    this.loadDistrict();
    this.searchDemanderCorp();
    this.searchCustomCorp();
    this.searchSmallClass();
    this.searchSpecification();
    this.searchModel();
    this.searchBrand();
  }

  // 数字框格式
  parserRmb = (value: string) => value.replace('￥ ', '');

  loadDistrict() {
    this.httpClient
      .get('/api/uas/area/list')
      .subscribe((res: any) => {
        this.districtOptions = res.data;
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

  // 更改委托商
  demanderChange(event) {
    this.form.controls.customId.reset();
    this.form.controls.workType.reset();
    this.form.controls.serviceItem.reset();
    this.form.controls.largeClassId.reset();
    this.form.controls.smallClassId.reset();
    this.form.controls.specification.reset();
    this.form.controls.brandId.reset();
    this.form.controls.modelId.reset();
    this.searchCustomCorp();
    this.loadWorkType(event);
    this.searchLargeClass(event);
    this.searchSpecification();
    this.loadServiceItem(event);
  }

  // 查询客户下拉框
  searchCustomCorp() {
    if (!this.form.value.demanderCorp || this.form.value.demanderCorp.length <= 0) {
      this.customCorpOptions = [];
    } else {
      this.customCorpOptionLoading = true;
      this.httpClient.post('/api/anyfix/demander-custom/custom/list', {demanderCorp: this.form.value.demanderCorp})
        .pipe(
          finalize(() => {
            this.customCorpOptionLoading = false;
          })
        ).subscribe((res: any) => {
        this.customCorpOptions = res.data;
      });
    }
  }

  // 查询工单类型
  loadWorkType(corpId: any) {
    this.httpClient.post(`/api/anyfix/work-type/list`, {
      demanderCorp: corpId,
      enabled: 'Y'
    }).subscribe((result: Result) => {
      if (result.code === 0) {
        this.workTypeOptions = result.data;
      }
    });
  }

  // 查询服务项目
  loadServiceItem(corpId: any) {
    if (!corpId || corpId.length <= 0) {
      this.serviceItemOptions = [];
      return;
    }
    const params = {
      serviceCorp: this.serviceCorp,
      demanderCorp: corpId
    };
    this.httpClient.post('/api/anyfix/service-item/list', params)
      .subscribe((res: any) => {
        this.serviceItemOptions = res.data;
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

  // 加载设备规格
  searchSpecification() {
    if (this.form.value.demanderCorp && this.form.value.demanderCorp !== '0') {
      this.specificationOptionLoading = true;
      const params = {
        corp: this.form.value.demanderCorp
      };
      this.httpClient.post('/api/device/device-specification/list', params)
        .pipe(
          finalize(() => {
            this.specificationOptionLoading = false;
          })
        )
        .subscribe((res: any) => {
          if (res.data && res.data.length > 0) {
            this.specificationOptions = res.data;
            this.specificationOptions.forEach((item: any) => {
              item.name = '[' + item.smallClassName + ']' + item.name;
            });
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
      this.modelOptionLoading = true;
      this.httpClient
        .post('/api/device/device-model/list', {corp: this.form.value.demanderCorp})
        .pipe(
          finalize(() => {
            this.modelOptionLoading = false;
          })
        )
        .subscribe((res: any) => {
          if (res.code === 0) {
            this.modelOptions = res.data;
          }
        });
    }
  }

  // 选中行政区划
  addDistrict(event) {
    if (!event && this.selectDistrict && this.selectDistrict.length > 0) {
      const params: any = {};
      // 只获取区
      params.value = this.selectDistrict[this.selectDistrict.length - 1];
      params.label = '';
      // 名称取全称
      this.selectDistrictOption.forEach((item: any) => {
        params.label += item.label;
      });
      // 如果后来选中的为之前选中的父级，则去除原来选中的
      let alreadyExist = false;
      this.selectedDistrictList = this.selectedDistrictList.filter((item: any) =>
        !this.areaService.isChildren(item.value, params.value));
      this.selectedDistrictList.forEach((item: any) => {
        if (this.areaService.isChildren(params.value, item.value)) {
          alreadyExist = true;
        }
      });
      // 不存在则添加
      if (!alreadyExist) {
        this.selectedDistrictList = [...this.selectedDistrictList, params];
      }
      // 重置输入框
      this.selectDistrict = [];
      this.selectDistrictOption = [];
    }
  }

  // 删除选中的行政区划
  deleteDistrict(index) {
    this.selectedDistrictList.splice(index, 1);
  }

  // 获取下拉选项
  districtChange(event) {
    this.selectDistrictOption = event;
  }


  // 取消
  cancel() {
    this.modalRef.destroy('cancel');
  }

  // 添加或修改提交
  submit() {
    if (!this.form.valid) {
      this.form.markAsDirty();
      return;
    }
    // 校验数据
    if (!this.checkData()) {
      return;
    }
    // 组装参数
    const params: any = Object.assign({}, this.form.value);
    const smallClassId = this.automationConfigService.getStrNameFromArray(this.form.value.smallClassId);
    const specification = this.automationConfigService.getStrNameFromArray(this.form.value.specification);
    const brandId = this.automationConfigService.getStrNameFromArray(this.form.value.brandId);
    const modelId = this.automationConfigService.getStrNameFromArray(this.form.value.modelId);
    const workType = this.automationConfigService.getStrNameFromArray(this.form.value.workType);
    let url = '/api/anyfix/work-fee-assort-define/add';
    // 组装参数
    params.serviceCorp = this.serviceCorp;
    params.smallClassId = smallClassId;
    params.specification = specification;
    params.brandId = brandId;
    params.modelId = modelId;
    params.together = this.form.value.together ? 'Y' : 'N';
    params.enabled = this.form.value.enabled ? 'Y' : 'N';
    params.workType = workType;
    params.district = this.automationConfigService.getStrNameFromArray(this.selectedDistrictList.map((item: any) => item.value));
    // 如果是修改页面，则url与添加不同，且有主键
    if (this.type === 'mod') {
      params.assortId = this.assortFee.assortId;
      params.conditionId = this.assortFee.conditionId;
      url = '/api/anyfix/work-fee-assort-define/update';
    }
    console.log(params);
    this.submitLoading = true;
    this.httpClient.post(url, params)
      .pipe(
        finalize(() => {
          this.submitLoading = false;
        })
      )
      .subscribe((res: any) => {
        const msg = this.type === 'mod' ? '修改成功！' : '添加成功！';
        this.messageService.success(msg);
        this.modalRef.destroy('submit');
      });
  }

  // 校验数据
  checkData(): boolean {
    const startDate = this.form.value.startDate;
    const endDate = this.form.value.endDate;
    if (startDate > endDate) {
      this.messageService.error('适用开始时间不能早于适用截止时间');
      return false;
    }
    const amount = this.form.value.amount || 0;
    if (amount <= 0) {
      this.messageService.error('费用金额必须大于0');
      return false;
    }
    return true;
  }

}
