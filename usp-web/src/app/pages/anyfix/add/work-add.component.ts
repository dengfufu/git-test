import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {ZonValidators} from '@util/zon-validators';
import {AddressParser} from '@util/address-parser';
import {Result} from '@core/interceptor/result';
import {FileService} from '@core/service/file.service';
import {finalize} from 'rxjs/operators';
import {NzMessageService, NzTreeNodeOptions} from 'ng-zorro-antd';
import {compareDesc} from 'date-fns';
import {WorkSysTypeEnum} from '@core/service/enums.service';
import {CustomFieldComponent} from '@shared/components/custom-field/custom-field.component';
import {ZorroUtils} from '@util/zorro-utils';
import {DataConfigService} from '../../data-config/data-config.service';
import {DemanderService} from '../../corp/service-demander/demander.service';
import {AllCreateEnum, WorkConfigService} from '../../setting/work-config/work-config.service';
import {DatePipe} from '@angular/common';

export class FaultType {
  id: number;
  name: string;

  [key: string]: any;
}

@Component({
  selector: 'app-detail-add',
  templateUrl: './work-add.component.html',
  styleUrls: ['./work-add.component.less']
})
export class WorkAddComponent implements OnInit {

  ZorroUtils = ZorroUtils;
  form: FormGroup;
  isLoading = false;
  demanderCorpId = '';
  demanderCorpList: any[] = [];
  customCorpList: any[] = [];
  customCorpFilters: any[] = [];
  workTypeOptions: any[] = [];
  deviceBranchList: any[] = [];
  // deviceBranchFilters: any[] = [];
  brandList: any[] = [];
  brandFilters: any[] = [];
  workTypeSelect: any;
  modelList: any[] = [];
  modelFilters: any[] = [];
  faultTypeList: any[] = [];
  fileList: any[] = [];
  deviceClassNodes: NzTreeNodeOptions[] = [];
  deviceSpecificationList: any[] = [];
  deviceSpecificationFilters: any[] = [];
  districtsList: any[];
  customFieldDataList: any[] = [];
  otherFaultType: FaultType = new FaultType();

  faultDate: Date;
  faultTime: Date;
  bookDate: Date;
  bookEndTime: Date;
  custom: any;
  deviceBranch: string;
  model: string;
  brand: string;
  specification: string;
  serialFlag: string;

  isInstallWorkSysType = false;
  // corpId: any;
  // 是否需要触发改变事件
  // needLoadChangeData = true;
  // 是否显示委托商
  ifShowDemander = false;
  // 是否显示故障现象
  ifShowFaultType = false;
  // 是否显示故障代码
  ifShowFaultCode = false;
  // 是否显示故障时间
  ifShowFaultDate = false;
  corpItemIdList = this.workConfigService.dataItemIdList;
  address: any = {};
  @ViewChild(CustomFieldComponent, {static: false}) customFieldComponent: CustomFieldComponent;

  configItem: any = {};

  nzFilterOption = () => true;

  constructor(public formBuilder: FormBuilder,
              public httpClient: HttpClient,
              public router: Router,
              public activatedRoute: ActivatedRoute,
              public fileService: FileService,
              public nzMessageService: NzMessageService,
              public cdf: ChangeDetectorRef,
              private datePipe: DatePipe,
              public dataConfigService: DataConfigService,
              public demanderService: DemanderService,
              public workConfigService: WorkConfigService) {
    this.form = formBuilder.group({
      demanderCorp: [null, [ZonValidators.required()]], // 委托商
      customCorp: [null, ZonValidators.required()], // 客户企业
      customId: [], // 客户企业ID
      customCorpName: [], // 新客户企业名称
      deviceBranch: [], // 设备网点
      deviceBranchName: [], // 新设备网点名称
      contactName: [null, [ZonValidators.required(), ZonValidators.maxLength(20)]], // 联系人姓名
      contactPhone: [null, [ZonValidators.required(), ZonValidators.phoneOrMobile(), ZonValidators.maxLength(20)]], // 联系人电话
      address: [null, [ZonValidators.required(), ZonValidators.maxLength(200)]], // 详细地址
      district: [null, [ZonValidators.required(), ZonValidators.minLength(4)]], // 行政区划
      zone: [1], // 分布
      workType: [null, [ZonValidators.required()]], // 工单类型
      serials: [null, [ZonValidators.maxLength(50)]], // 厂商序列号
      warrantyMode: [20], // 维保方式
      deviceId: [], // 设备号
      deviceCodes: [null, [ZonValidators.maxLength(50)]], // 设备编号
      smallClass: [], // 设备分类
      specification: [], // 设备规格
      specificationName: [], // 设备规格名称
      brand: [], // 设备品牌
      // brandName: [], // 新设备品牌
      model: [], // 设备型号
      modelName: [], // 新设备型号
      serviceRequest: [null, [ZonValidators.maxLength(500)]], // 服务请求
      // faultTypes: [], // 故障现象
      faultTypeList: [], // 故障现象
      bookTimeEnd: [], // 预约时间结束
      faultTime: [], // 故障时间
      faultCode: [null, [ZonValidators.maxLength(50)]], // 故障代码
      source: [4], // 工单来源
      files: [], // 附件,
      districts: [],
      deviceNum: ['1'], // 设备数量
      checkWorkCode: [null, [ZonValidators.maxLength(20)]], // 委托单号
      deviceDescription: [null, [ZonValidators.maxLength(100)]], // 设备描述
      basicServiceFee: [0, [ZonValidators.intOrFloat('费用报价'), ZonValidators.min(0, '费用报价'),
        ZonValidators.max(99999999.99, '费用报价')]] // 费用报价
    });

    // 设备数量不能小于填写的序列号数量
    this.form.controls.serials.valueChanges.subscribe((serials) => {

      serials = serials || '';
      serials = serials.replace(/，/ig, ',').trim().toLocaleUpperCase();
      const serialList: string[] = serials.trim().split(',');
      let num: number = serialList.length;
      if (num === 0) {
        num = 1;
      }

      const deviceNum = this.form.value.deviceNum || 1;
      if (deviceNum < num) {
        this.form.controls.deviceNum.setValidators([ZonValidators.required(), ZonValidators.isInt(), ZonValidators.min(num),
          ZonValidators.maxLength(2)]);
      } else {
        this.form.controls.deviceNum.setValidators([ZonValidators.required(), ZonValidators.isInt(), ZonValidators.maxLength(2)]);
      }

      this.form.controls.deviceNum.markAsDirty();
      this.form.controls.deviceNum.updateValueAndValidity();
    });
  }

  ngOnInit(): void {
    this.listDemanderCorp();
    // 行政区划
    this.httpClient.get('/api/uas/area/list')
      .subscribe((res: any) => {
        this.districtsList = res.data;
      });
    // this.bookDate = new Date();
    this.setConfigData();
  }

  setConfigData() {
    this.configItem[this.workConfigService.WORK_ADD_REQUIRE_CHECK_WORK_CODE] = {
      isShow: false,
      description: '委托单号',
      itemValue: '',
      formName: 'checkWorkCode'
    };
    this.configItem[this.workConfigService.WORK_ADD_NEED_QUOTE] = {
      isShow: false,
      description: '费用报价',
      itemValue: 0,
      formName: 'basicServiceFee'
    };
    this.configItem[this.workConfigService.WORK_ADD_REQUIRE_DEVICE_BRANCH] = {
      isShow: false,
      description: '设备网点',
      itemValue: 0,
      formName: 'deviceBranch'
    };
    this.configItem[this.workConfigService.WORK_ADD_ALLOW_CREATE_CUSTOM] = {
      isShow: false,
      description: '',
      itemValue: 0
    };
    this.configItem[this.workConfigService.WORK_ADD_ALLOW_CREATE_DEVICE_BRANCH] = {
      isShow: false,
      description: '',
      itemValue: 0
    };
    this.configItem[this.workConfigService.WORK_ADD_ALLOW_CREATE_MODEL] = {
      isShow: false,
      description: '设备型号',
      itemValue: 0
    };
    this.configItem[this.workConfigService.WORK_ADD_ALLOW_CREATE_SPECIFICATION] = {
      isShow: false,
      description: '设备规格',
      itemValue: 0
    };
    this.configItem[this.workConfigService.CHECK_WORK_CODE_TEMPLATE] = {
      isShow: false,
      description: '',
      itemValue: ''
    };
  };

  /**
   * 委托商列表
   */
  listDemanderCorp() {
    this.httpClient.get(`/api/anyfix/demander/list`).subscribe((result: Result) => {
      if (result.code === 0) {
        this.demanderCorpList = result.data || [];
        if (this.demanderCorpList.length > 1) {
          this.ifShowDemander = true;
        } else {
          if (this.demanderCorpList.length === 1) {
            this.demanderCorpId = this.demanderCorpList[0].demanderCorp;
          } else {
            this.demanderCorpId = null;
            // this.demanderCorpId = this.userService.currentCorp.corpId;
            // this.demanderCorpList = [{demanderCorp: this.userService.currentCorp.corpId,
            //   demanderCorpName: this.userService.currentCorp.corpName}];
          }
          this.ifShowDemander = true;
          this.form.patchValue({
            demanderCorp: this.demanderCorpId
          });
          this.demanderCorpChange(this.demanderCorpId);
        }
      }
    });
  }

  /**
   * 委托商改变
   * @param corpId 委托商编号
   */
  demanderCorpChange(corpId: string) {
    // if (!this.needLoadChangeData) {
    //   return;
    // }
    this.form.controls.basicServiceFee.setValue(0);
    if (corpId) {
      this.demanderCorpId = corpId;
      this.listCustomCorp(corpId);
      this.loadWorkType(corpId);
      if (this.form.value.workType && this.form.value.workType === WorkSysTypeEnum.MAINTENANCE) {
        this.loadFaultType(corpId);
      }
      this.listDeviceClass(corpId);
      this.listDeviceBrand(null);
      this.findDeviceInfo();
      this.getConfigData();
      this.custom = null;
      this.onCustomChange(null);
    }
  }

  /**
   * 客户列表
   * @param demanderCorp 委托商列表
   */
  listCustomCorp(demanderCorp) {
    this.httpClient.get(`/api/anyfix/demander-custom/custom/list/${demanderCorp}`).subscribe((result: Result) => {
      if (result.code === 0) {
        this.customCorpList = result.data;
        this.customCorpFilters = this.customCorpList;
      }
    });
  }

  onSearchCustom(value: string): void {
    if (!value) {
      this.customCorpFilters = this.customCorpList;
      return;
    }
    this.customCorpFilters = this.customCorpList.filter(
      option => option.customCorpName.toLowerCase().indexOf(value.toLowerCase()) !== -1);
    let match = false;
    this.customCorpFilters.forEach(option => {
      if (option.customCorpName === value) {
        match = true;
      }
    });
    if (!match && this.configItem[this.workConfigService.WORK_ADD_ALLOW_CREATE_CUSTOM].itemValue === AllCreateEnum.ALLOW) {
      /*// 向数组的开头添加一个或更多元素，并返回新的长度
      this.customCorpFilters.unshift({customCorp: value + '[新增]', customCorpName: value + '[新增]'});*/
      // 向数组的末尾添加一个或更多元素，并返回新的长度
      this.customCorpFilters.push({customCorp: value + '[新增]', customCorpName: value + '[新增]'});
    }
  }

  /**
   * 客户企业改变
   * @param value: 客户企业
   */
  onCustomChange(value: any): void {
    if (value) {
      this.form.controls.customCorp.setValue(value.customCorp);
    } else {
      this.form.controls.customCorp.setValue(null);
    }
    this.deviceBranch = null;
    this.deviceBranchList = null;
    if (!value || !value.customId || value.customId === '0') {
      return;
    }
    // this.corpId = value;
    this.matchDeviceBranch(value.customId);
    this.findDeviceInfo();
  }

  /**
   * 模糊查询客户企业的设备网点
   * @param corpId: 客户企业
   * @param branchName: 网点名称
   */
  matchDeviceBranch(corpId, branchName?: string) {
    if (!corpId) {
      this.deviceBranchList = [{branchId: branchName + '[新增]', branchName: branchName + '[新增]'}];
      return;
    }
    const params = {customId: corpId, matchFilter: branchName};
    this.httpClient.post(`/api/anyfix/device-branch/match`, params).subscribe((result: Result) => {
      if (result.code === 0) {
        this.deviceBranchList = result.data;
        if (!branchName) {
          return;
        }
        let match = false;
        this.deviceBranchList.forEach(option => {
          if (option.branchName === branchName) {
            match = true;
          }
        });
        if (!match && this.configItem[this.workConfigService.WORK_ADD_ALLOW_CREATE_DEVICE_BRANCH].itemValue
          === AllCreateEnum.ALLOW) {
          this.deviceBranchList.push({branchId: branchName + '[新增]', branchName: branchName + '[新增]'});
        }
      }
    });
  }

  /**
   * 默认填充联系人，电话，地址信息
   * @param branchId: 设备网点id
   */
  deviceBranchChange(branchId: string) {
    this.form.controls.deviceBranch.setValue(this.deviceBranch);
    // if (!this.needLoadChangeData) {
    //   return;
    // }
    if (!branchId || branchId.indexOf('[新增]') > 0) {
      return;
    }
    if (branchId !== '0') {
      this.httpClient.get(`/api/anyfix/device-branch/` + branchId).subscribe((result: Result) => {
        if (result.code === 0) {
          const deviceBranch = result.data;
          this.form.patchValue({
            contactName: deviceBranch.contactName,
            contactPhone: deviceBranch.contactPhone,
            address: deviceBranch.address,
            district: deviceBranch.district || deviceBranch.city,
            districts: [deviceBranch.province, deviceBranch.city, deviceBranch.district]
          });
        }
      });
    }
  }

  /**
   * 联系人改变
   */
  contactNameChange() {
    this.form.controls.contactName.markAsDirty();
    this.form.controls.contactName.updateValueAndValidity();
  }

  /**
   * 联系电话改变
   */
  contactPhoneChange() {
    this.form.controls.contactPhone.markAsDirty();
    this.form.controls.contactPhone.updateValueAndValidity();
  }

  /**
   * 地址解析行政区划
   */
  addressParse() {
    const rs = AddressParser.parseDistrict(this.form.value.address, this.districtsList);
    if (rs) {
      if (rs.districts) {
        this.form.patchValue({
          districts: rs.districts
        });
        if (rs.districts.length > 0) {
          this.form.patchValue({
            district: rs.districts[rs.districts.length - 1]
          });
        } else {
          this.form.patchValue({
            district: null
          });
        }
      }
      // if (rs.phone) {
      //   this.form.patchValue({
      //     contactPhone: rs.phone
      //   });
      // }
      // if (rs.contact) {
      //   this.form.patchValue({
      //     contactName: rs.contact
      //   });
      // }
      // this.form.patchValue({
      //   address: rs.address
      // });
    }
  }

  /**
   * 列出工单类型
   */
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

  /**
   * 列出故障现象
   */
  loadFaultType(corpId: any) {
    this.httpClient.post(`/api/anyfix/fault-type/list`, {demanderCorp: corpId})
      .subscribe((result: Result) => {
        if (result.code === 0) {
          this.faultTypeList = result.data;
          this.otherFaultType.id = 0;
          this.otherFaultType.name = '其他';
          this.faultTypeList.push(this.otherFaultType);
        }
      });
  }

  /**
   * 列出所有设备分类
   */
  listDeviceClass(corpId: any) {
    if (!corpId) {
      return;
    }
    const url = `/api/device/device-class/list`;
    const params = {
      corpId
    };
    this.httpClient.post(url, params).subscribe((result: Result) => {
      if (result.code === 0) {
        const treeNode: any[] = [];
        result.data.forEach((largeClass: any) => {
          const parentNode = {
            title: largeClass.name,
            key: largeClass.id,
            selectable: false,
            children: []
          };
          const childrenNode: any[] = [];
          if (largeClass.deviceSmallClassDtoList && largeClass.deviceSmallClassDtoList.length > 0) {
            largeClass.deviceSmallClassDtoList.forEach((smallClass: any) => {
              const childNode = {
                title: smallClass.name,
                key: smallClass.id,
                isLeaf: true
              };
              childrenNode.push(childNode);
            });
            parentNode.children = childrenNode;
            treeNode.push(parentNode);
          }
        });
        this.deviceClassNodes = treeNode;
      }
    });
  }

  /**
   * 设备类型改变
   */
  deviceClassChange(value) {
    // if (!this.needLoadChangeData) {
    //   return;
    // }
    // this.listDeviceBrand(value);
    this.listDeviceModel(value);
    this.listDeviceSpecification(value);
    this.findDeviceInfo();
  }

  /**
   * 设备规格列表
   * @param value 设备类型
   */
  listDeviceSpecification(value) {
    const smallClassValue = value;
    if (!smallClassValue) {
      return;
    }
    const url = `/api/device/device-specification/list`;
    const params = {
      smallClassId: smallClassValue,
      enabled: 'Y'
    };
    const specification = this.specification;
    this.httpClient.post(url, params).subscribe((result: Result) => {
      if (result.code === 0) {
        this.deviceSpecificationList = result.data;
        this.deviceSpecificationFilters = this.deviceSpecificationList;
        if (specification && specification.indexOf('[新增]') <= 0) {
          let match = false;
          this.deviceSpecificationFilters.forEach(option => {
            if (option.id === specification) {
              match = true;
            }
          });
          if (!match) {
            this.specification = null;
          }
        }
      }
    });
  }

  /**
   * 搜索设备规格
   * @param value
   */
  onSearchDeviceSpecification(value?: string) {
    if (!value) {
      this.deviceSpecificationFilters = this.deviceSpecificationList;
      return;
    }
    let match = false;
    this.deviceSpecificationFilters.forEach(option => {
      if (option.name.toLowerCase() === value.toLowerCase()) {
        match = true;
      }
    });
    if (!match && this.configItem[this.workConfigService.WORK_ADD_ALLOW_CREATE_SPECIFICATION].itemValue === AllCreateEnum.ALLOW) {
      this.deviceSpecificationFilters = this.deviceSpecificationList.slice();
      this.deviceSpecificationFilters.push({id: value + '[新增]', name: value + '[新增]'});
    }
  }

  /**
   * 列出类型对应的品牌
   */
  listDeviceBrand(value) {
    const smallClassValue = value;  // this.form.value.smallClass;
    const url = `/api/device/device-brand/list`;
    let params;
    if (!smallClassValue) {
      params = {
        corp: this.demanderCorpId
      };
    } else {
      params = {
        smallClassId: smallClassValue
      };
    }
    this.httpClient.post(url, params).subscribe((result: Result) => {
      if (result.code === 0) {
        this.brandList = result.data;
        this.brandFilters = this.brandList;
      }
    });
  }

  /**
   * 模糊查询设备品牌
   */
  matchBrand(brandName?: string) {
    if (!this.demanderCorpId) {
      return;
    }
    if (!brandName) {
      this.brandFilters = this.brandList;
      return;
    }
    let match = false;
    this.brandList.forEach(option => {
      if (option.name === brandName) {
        match = true;
      }
    });
    if (match) {
      this.brandFilters = this.brandList;
    }
    const url = `/api/device/device-brand/list`;
    const params = {
      corp: this.demanderCorpId
    };
    this.httpClient.post(url, params).subscribe((result: Result) => {
      if (result.code === 0) {
        const allBrandList = result.data;
        allBrandList.forEach(option => {
          if (option.name === brandName) {
            match = true;
          }
        });
        if (match) {
          this.brandFilters = allBrandList;
        } else {
          this.brandFilters = allBrandList.filter(
            option => option.name.toLowerCase().indexOf(brandName.toLowerCase()) !== -1);
        }
      }
    });
  }

  /**
   * 列出类型对应的型号
   */
  listDeviceModel(value) {
    const smallClassValue = value;  // this.form.value.smallClass;
    const brand = this.brand;
    if (!smallClassValue) {
      return;
    }
    const url = `/api/device/device-model/list`;
    const params = {
      smallClassId: smallClassValue,
      brandId: brand
    };
    const model = this.model;
    this.httpClient.post(url, params).subscribe((result: Result) => {
      if (result.code === 0) {
        this.modelList = result.data;
        this.modelFilters = this.modelList;
        if (model && model.indexOf('[新增]') <= 0) {
          let match = false;
          this.modelFilters.forEach(option => {
            if (option.id === model) {
              match = true;
            }
          });
          if (!match) {
            this.model = null;
          }
        }
      }
    });
  }

  /**
   * 品牌改变
   */
  onBrandChange() {
    // if (!this.needLoadChangeData) {
    //   return;
    // }
    this.listDeviceModel(this.form.value.smallClass);
    this.findDeviceInfo();
  }

  /**
   * 型号改变
   */
  onModelChange() {
    // if (!this.needLoadChangeData) {
    //   return;
    // }
    const model = this.model;
    if (!model || model.indexOf('[新增]') > 0) {
      this.findDeviceInfo();
      return;
    }
    // 填充品牌
    this.modelList.forEach(option => {
      if (option.id === model) {
        this.brand = option.brandId;
      }
    });
    this.findDeviceInfo();
  }

  /**
   * 模糊查询型号
   */
  matchModel(value: string) {
    if (!value) {
      return;
    }
    let match = false;
    this.modelFilters.forEach(option => {
      if (option.name.toLowerCase() === value.toLowerCase()) {
        match = true;
      }
    });
    if (!match) {
      this.modelFilters = this.modelList.slice();
      this.modelFilters.push({id: value + '[新增]', name: value + '[新增]'});
    }
  }

  /**
   * 出厂序列号改变
   */
  serialsChange() {
    // if (!this.needLoadChangeData) {
    //   return;
    // }
    let serials = this.form.value.serials || '';
    serials = serials.replace(/，/ig, ',').trim().toLocaleUpperCase();
    const serialList: string[] = serials.trim().split(',');
    let num: number = serialList.length;
    if (num === 0) {
      num = 1;
    }

    const numStr = '' + num;
    this.form.patchValue({
      deviceNum: numStr
    });
    if (serials) {
      if (serials === this.form.value.serials) {
        this.findDeviceInfo();
      } else {
        this.form.patchValue({
          serials
        });
      }
    }
  }

  // /**
  //  * 设备编号改变
  //  */
  // deviceCodesChange() {
  //   if (!this.needLoadChangeData) {
  //     return;
  //   }
  // }

  /**
   * 查找设备档案
   */
  findDeviceInfo() {
    const demanderCorpId = this.form.value.demanderCorp;
    let serialsValue = this.form.value.serials;
    const smallClassValue = this.form.value.smallClass;
    const modelValue = this.model;
    const brandValue = this.brand;
    const deviceBranch = this.deviceBranch;
    const customCorp = this.custom ? this.custom.customCorp : null;
    if (!serialsValue || !demanderCorpId) {
      return;
    }
    if (serialsValue.indexOf(',') > 0) {
      const a = serialsValue.split(',');
      serialsValue = a[0];
      if (!serialsValue) {
        return;
      }
    }
    if (modelValue && modelValue.indexOf('[新增]') > 0) {
      return;
    }
    if (brandValue && brandValue.indexOf('[新增]') > 0) {
      return;
    }
    const params = {
      serial: serialsValue,
      smallClassId: smallClassValue,
      modelId: modelValue,
      brandId: brandValue,
      demanderCorp: demanderCorpId
    };
    this.httpClient.post('/api/device/device-info/findByWhere', params).subscribe((result: Result) => {
      const deviceInfoTemp = result.data;
      if (deviceInfoTemp && deviceInfoTemp.deviceId) {
        // console.log(deviceInfoTemp);
        // this.needLoadChangeData = false;
        // this.corpId = deviceInfoTemp.customCorp;
        if (customCorp && customCorp.indexOf('[新增]') > 0) {

        } else {
          let match = false;
          this.customCorpList.forEach(option => {
            if (option.customId === deviceInfoTemp.customId) {
              match = true;
              this.custom = option;
              this.form.controls.customId.setValue(this.custom.customId, {
                onlySelf: true,
                emitViewToModelChange: false
              });
              this.form.controls.customCorp.setValue(this.custom.customCorp, {
                onlySelf: true,
                emitViewToModelChange: false
              });
              this.form.controls.customCorpName.setValue(this.custom.customCorpName, {
                onlySelf: true,
                emitViewToModelChange: false
              });
            }
          });
          this.matchDeviceBranch(deviceInfoTemp.customId);
          if (!deviceBranch || deviceBranch.indexOf('[新增]') <= 0) {
            this.deviceBranch = deviceInfoTemp.branchId;
          }
          if (!this.form.value.address) {
            this.form.patchValue({address: deviceInfoTemp.address});
          }
          if (!this.form.value.contactName) {
            this.form.patchValue({contactName: deviceInfoTemp.contactName});
          }
          if (!this.form.value.contactPhone) {
            this.form.patchValue({contactPhone: deviceInfoTemp.contactPhone});
          }
        }
        if (!serialsValue) {
          this.form.patchValue({serials: deviceInfoTemp.serial});
        }
        if (!smallClassValue || smallClassValue === '0') {
          this.form.patchValue({smallClass: deviceInfoTemp.smallClassId});
          this.specification = deviceInfoTemp.specificationId;
        }
        if (!brandValue) {
          this.brand = deviceInfoTemp.brandId;
          this.listDeviceModel(deviceInfoTemp.smallClassId);
        }
        if (!modelValue) {
          this.model = deviceInfoTemp.modelId;
        }
        // if (deviceInfoTemp.deviceCode) {
        //   this.form.patchValue({deviceCode: deviceInfoTemp.deviceCode});
        // }
        this.form.patchValue({deviceId: deviceInfoTemp.deviceId});
        if (deviceInfoTemp.district && deviceInfoTemp.district.length > 0) {
          this.form.patchValue({
            district: deviceInfoTemp.district
          });
          if (deviceInfoTemp.district.length === 6) {
            this.form.patchValue({
              districts: [deviceInfoTemp.district.substr(0, 2), deviceInfoTemp.district.substr(0, 4), deviceInfoTemp.district]
            });
          } else if (deviceInfoTemp.district.length === 4) {
            this.form.patchValue({
              districts: [deviceInfoTemp.district.substr(0, 2), deviceInfoTemp.district]
            });
          } else if (deviceInfoTemp.district.length === 2) {
            this.form.patchValue({
              districts: [deviceInfoTemp.district]
            });
          }

        }
        // this.needLoadChangeData = true;
      } else {
        this.form.patchValue({
          deviceId: 0
        });
      }
    });
  }

  /**
   * 自定义文件上传
   * @param item: 文件信息
   */
  fileCustomRequest = (item: any) => {
    this.fileService.upload(item.file).subscribe((fileId: string) => {
      this.fileList.push(fileId);
      item.onSuccess({}, {status: 'done'});
    });
  };

  formDataBuild(): void {
    // 组装文件
    let filesString = '';
    this.fileList.forEach((fileId: any) => {
      if (filesString) {
        filesString = filesString + ',' + fileId;
      } else {
        filesString = fileId;
      }
    });
    this.form.patchValue({
      files: filesString
    });
    // 组装预约时间
    if (this.bookDate && this.bookEndTime) {
      const hour = this.bookEndTime.getHours();
      const minute = this.bookEndTime.getMinutes();
      this.bookDate.setHours(hour, minute, 0, 0);
      this.form.patchValue({
        bookTimeEnd: this.bookDate
      });
    } else {
      this.form.patchValue({
        bookTimeEnd: null
      });
    }
    // 组装故障时间
    if (this.faultDate && this.faultTime) {
      const hour = this.faultTime.getHours();
      const minute = this.faultTime.getMinutes();
      this.faultDate.setHours(hour, minute, 0, 0);
      this.form.patchValue({
        faultTime: this.faultDate
      });
    } else {
      this.form.patchValue({
        faultTime: null
      });
    }

    const cust = this.custom ? this.custom.customCorpName : null;
    if (cust) {
      if (cust.indexOf('[新增]') > 0) {
        this.form.patchValue({
          customCorp: 0,
          customId: 0,
          customCorpName: cust.substring(0, cust.indexOf('[新增]'))
        });
      } else {
        this.form.patchValue({
          customCorp: this.custom.customCorp,
          customId: this.custom.customId,
          customCorpName: null
        });
      }
    } else {
      this.form.patchValue({
        customCorp: 0,
        customId: 0,
        customCorpName: null
      });
    }

    const branch = this.deviceBranch;
    if (branch) {
      if (branch.indexOf('[新增]') > 0) {
        this.form.patchValue({
          deviceBranch: 0,
          deviceBranchName: branch.substring(0, branch.indexOf('[新增]'))
        });
      } else {
        this.form.patchValue({
          deviceBranch: branch,
          deviceBranchName: null
        });
      }
    } else {
      this.form.patchValue({
        deviceBranch: 0,
        deviceBranchName: null
      });
    }
    this.form.patchValue({
      brand: this.brand
    });
    const specification = this.specification;
    if (specification) {
      if (specification.indexOf('[新增]') > 0) {
        this.form.patchValue({
          specification: 0,
          specificationName: specification.substring(0, specification.indexOf('[新增]'))
        });
      } else {
        let specificationName = '';
        this.deviceSpecificationList.forEach(item => {
          if (item.id === specification) {
            specificationName = item.name;
          }
        });
        this.form.patchValue({
          specification,
          specificationName
        });
      }
    } else {
      this.form.patchValue({
        specification: 0,
        specificationName: null
      });
    }
    const model = this.model;
    if (model) {
      if (model.indexOf('[新增]') > 0) {
        this.form.patchValue({
          model: 0,
          modelName: model.substring(0, model.indexOf('[新增]'))
        });
      } else {
        let modelName = '';
        this.modelList.forEach(item => {
          if (item.id === model) {
            modelName = item.name;
          }
        });
        this.form.patchValue({
          model,
          modelName
        });
      }
    } else {
      this.form.patchValue({
        model: 0,
        modelName: null
      });
    }
    // this.needLoadChangeData  = false;
  }

  submit() {
    const errorList = this.customFieldComponent.submit() || [];
    if (errorList.length > 0) {
      for (const error of errorList) {
        if (error.required === 'Y') {
          this.nzMessageService.error(error.fieldName + '不能为空');
          return;
        }
      }
    }
    if (this.customFieldDataList && this.customFieldDataList.length > 0) {
      this.form.addControl('customFieldDataList', new FormControl(this.customFieldDataList, []));
    }
    if (this.form.valid) {
      // 组装form 值
      this.formDataBuild();
      // 额外校验
      if (this.form.value.faultTime && this.form.value.bookTimeEnd) {
        if (compareDesc(this.form.value.faultTime, this.form.value.bookTimeEnd) === -1) {
          this.nzMessageService.error('故障时间不能大于预约时间');
          return;
        }
      }
      if (this.faultDate && !this.faultTime) {
        this.nzMessageService.error('请选择故障时间');
        return;
      }
      if (!this.faultDate && this.faultTime) {
        this.nzMessageService.error('请选择故障日期');
        return;
      }
      if (this.bookDate && !this.bookEndTime) {
        this.nzMessageService.error('请选择预约时间');
        return;
      }
      if (!this.bookDate && this.bookEndTime) {
        this.nzMessageService.error('请选择预约日期');
        return;
      }

      // if (this.isInstallWorkSysType) {
      //   if (!this.form.value.smallClass) {
      //     this.nzMessageService.error('设备类型不能为空');
      //     return;
      //   }
      //   if (!this.form.value.specification) {
      //     this.nzMessageService.error('设备规格不能为空');
      //     return;
      //   }
      // }
      // 出厂序列号不允许为*, 必须含有字母或数字
      const p = /[0-9a-z]/i;
      this.serialFlag = '';
      if (this.form.value.serials) {
        let serials = this.form.value.serials || '';
        serials = serials.replace(/，/ig, ',').trim().toLocaleUpperCase();
        const serialList: string[] = serials.trim().split(',');
        serialList.forEach((item: any) => {
          if (!p.test(item)) {
            this.serialFlag = 'A9';
          }
        });
        if (this.serialFlag === 'A9') {
          this.nzMessageService.error('出厂序列号必须含有字母或数字');
          return;
        }
      }
      // 设备型号不允许为*, 必须含有字母或数字
      if (this.model) {
        if (!p.test(this.model)) {
          this.nzMessageService.error('设备型号必须含有字母或数字');
          return;
        }
      }

      if (!this.form.value.faultTypeList || this.form.value.faultTypeList.length === 0) {
        if (!this.form.value.serviceRequest) {
          this.nzMessageService.error('服务请求不能为空');
          return;
        }
      } else {
        let others = false;
        this.form.value.faultTypeList.forEach((item: any) => {
          if (item.id === 0) {
            others = true;
          }
        });
        if (others) {
          if (!this.form.value.serviceRequest) {
            this.nzMessageService.error('选择了其他，详细现象不能为空');
            return;
          }
        }
      }
      const serviceRequest = this.form.value.serviceRequest;
      this.setServiceRequestValue();
      // 提交表单
      this.isLoading = true;
      this.httpClient.post('/api/anyfix/work-request/add', this.form.value)
        .pipe(
          finalize(() => {
            this.isLoading = false;
          })
        ).subscribe((result: any) => {
        if (result.code === 0) {
          this.nzMessageService.success('提交成功');
          this.goBack();
        }
        // this.needLoadChangeData = true;
      }, (error => {
        this.form.patchValue({serviceRequest});
      }));
    } else {
      // tslint:disable-next-line:forin
      for (const i in this.form.controls) {
        this.form.controls[i].markAsDirty();
        this.form.controls[i].updateValueAndValidity();
      }
    }
  }

  disabledHoursFunc = () => {
    return [0, 1, 2, 3, 4, 5, 23, 24];
  };

  districtChange(event) {
    if (event && event.length > 0) {
      this.form.patchValue({
        district: event[event.length - 1].value
      });
    } else {
      this.form.patchValue({
        district: null
      });
    }
  }

  // 收集自定义字段数据
  customFormChange(customFieldDataList: {fieldId: any; fieldValue: any;}[]) {
    this.customFieldDataList = customFieldDataList;
  }

  faultTypeChange() {
    if (!this.form.value.demanderCorp) {
      this.nzMessageService.error('请先选择委托商');
    }
    if (!this.form.value.workType) {
      this.nzMessageService.error('请先选择工单类型');
    }
  }

  workTypeChange(event) {
    if (!event) {
      this.form.controls.workType.setValue(null);
    } else {
      this.form.controls.workType.setValue(event.id);
    }
    // if (event && event.sysType === WorkSysTypeEnum.INSTALL) {
    //   this.isInstallWorkSysType = true;
    //   this.form.controls.smallClass.setValidators([ZonValidators.required()]);
    //   this.form.controls.specification.setValidators([ZonValidators.required()]);
    // } else {
    //   this.isInstallWorkSysType = false;
    //   this.form.controls.smallClass.setValidators(null);
    //   this.form.controls.specification.setValidators(null);
    // }
    if (event && event.sysType === WorkSysTypeEnum.MAINTENANCE) {
      this.ifShowFaultType = true;
      this.ifShowFaultCode = true;
      this.ifShowFaultDate = true;
      if (this.form.value.demanderCorp) {
        this.loadFaultType(this.form.value.demanderCorp);
      }
    } else {
      this.ifShowFaultType = false;
      this.ifShowFaultCode = false;
      this.ifShowFaultDate = false;
    }
    this.form.controls.workType.markAsDirty();
    this.form.controls.workType.updateValueAndValidity();
  }

  // 设置服务请求的值
  setServiceRequestValue() {
    if (this.form.value.workType && this.form.value.workType === WorkSysTypeEnum.MAINTENANCE) {
      let faultTypeNames = '';
      let serviceRequest = '';
      if (this.form.value.serviceRequest) {
        serviceRequest = this.form.value.serviceRequest;
      }

      if (this.form.value.faultTypeList && this.form.value.faultTypeList.length > 0) {
        for (const i in this.form.value.faultTypeList) {
          if (this.form.value.faultTypeList[i].name !== '其他') {
            if (faultTypeNames !== '') {
              faultTypeNames = faultTypeNames + ',';
            }
            faultTypeNames = faultTypeNames + this.form.value.faultTypeList[i].name;
          }
        }
      }

      if (serviceRequest !== '') {
        if (faultTypeNames !== '') {
          this.form.patchValue({serviceRequest: faultTypeNames + ',' + serviceRequest.toString()});
        } else {
          this.form.patchValue({serviceRequest: serviceRequest.toString()});
        }
      } else {
        this.form.patchValue({serviceRequest: faultTypeNames});
      }
    }
  }

  goBack() {
    history.go(-1);
  }

  getConfigData() {
    this.dataConfigService.resetConfigShow(this.configItem, this.form);
    this.dataConfigService.getCorpConfigData(this.demanderCorpId, this.corpItemIdList, this.configItem).then((res: any) => {
      if (res.data && res.data.length > 0) {
        this.dataConfigService.validateConfigItem(this.configItem, this.form);
      }
      this.setCheckWorkTemplate();
    });
  }

  setCheckWorkTemplate() {
    const itemValue =  this.configItem[this.workConfigService.CHECK_WORK_CODE_TEMPLATE].itemValue;
    if (itemValue && itemValue !== '' ) {
      const index = itemValue.search(/\{\{(YY|YYYY)?(MM)?(DD)?\}\}/g);

      if (index > -1) {
        const index2 = itemValue.indexOf('}}', index);
        const df = itemValue.substring(index + 2, index2);
        const curDay = this.datePipe.transform(new Date(), df.replace(/Y/g,'y').replace(/D/g,'d'));
        const newValue = itemValue.replace('{{' + df + '}}', curDay);

        this.form.controls.checkWorkCode.setValue(newValue);
      } else {
        this.form.controls.checkWorkCode.setValue(itemValue);
      }
    }
  }

}
