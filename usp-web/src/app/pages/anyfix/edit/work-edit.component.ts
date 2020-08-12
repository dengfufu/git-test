import {ChangeDetectorRef, Component, Input, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, FormControl} from '@angular/forms';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {ZonValidators} from '@util/zon-validators';
import {Result} from '@core/interceptor/result';
import {FileService} from '@core/service/file.service';
import {finalize} from 'rxjs/operators';
import {NzMessageService, NzTreeNodeOptions} from 'ng-zorro-antd';
import {compareDesc, format} from 'date-fns';
import {WorkSysTypeEnum} from '@core/service/enums.service';
import {environment} from '@env/environment';
import {UploadFile} from 'ng-zorro-antd/upload';
import {AnyfixService} from '@core/service/anyfix.service';
import {AreaService} from '@core/area/area.service';
import {CustomFieldComponent} from '@shared/components/custom-field/custom-field.component';
import {saveAs} from 'file-saver';
import {ZorroUtils} from '@util/zorro-utils';
import {DataConfigService} from '../../data-config/data-config.service';
import {AllCreateEnum, ConfigRequireEnum, WorkConfigService} from '../../setting/work-config/work-config.service';
import {isNull} from '@util/helpers';

export class FaultType {
  id: number;
  name: string;

  [key: string]: any;
}

/**
 * 建单
 */
@Component({
  selector: 'app-work-edit',
  templateUrl: 'work-edit.component.html',
  styleUrls: ['work-edit.component.less']
})
export class WorkEditComponent implements OnInit {

  ZorroUtils = ZorroUtils;
  @ViewChild(CustomFieldComponent, {static: false}) customFieldComponent: CustomFieldComponent;

  @Input() workId: any;

  form: FormGroup;
  isLoading = false;
  work: any = {};

  /*文件上传路径*/
  uploadAction = '/api/file/uploadFile';
  baseFileUrl = environment.server_url + '/api/file/showFile?fileId=';
  previewImage: any;
  previewVisible = false;

  fileList: any[] = [];
  imgFileList: any[] = [];
  notImgFileList: any[] = [];

  demanderCorpId = '';
  customCorpList: any[] = [];
  customCorpFilters: any[] = [];
  workTypeOptions: any[] = [];
  deviceBranchList: any[] = [];
  brandList: any[] = [];
  brandFilters: any[] = [];
  modelList: any[] = [];
  modelFilters: any[] = [];
  faultTypeList: any[] = [];
  deviceClassNodes: NzTreeNodeOptions[] = [];
  deviceSpecificationList: any[] = [];
  deviceSpecificationFilters: any[] = [];
  districtsList: any[];
  customFieldDataList: any[] = [];
  otherFaultType: FaultType = new FaultType();
  workTypeSelect: any;

  faultDate: Date;
  faultTime: Date;
  bookDate: Date;
  bookEndTime: Date;
  custom: any;
  deviceBranch: string;
  model: string;
  specification: string;
  brand: string;

  isInstallWorkSysType = false;
  // 是否显示故障现象
  ifShowFaultType = false;
  // 是否显示故障代码
  ifShowFaultCode = false;
  // 是否显示故障时间
  ifShowFaultDate = false;
  deleteFileIdList: any = [];
  newFileIdList: any = [];

  configItem: any = {};
  serviceConfigList: any;
  corpItemIdList = this.workConfigService.dataItemIdList;

  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private fileService: FileService,
              private cdf: ChangeDetectorRef,
              private nzMessageService: NzMessageService,
              public anyfixService: AnyfixService,
              public dataConfigService: DataConfigService,
              private areaService: AreaService,
              public workConfigService: WorkConfigService) {
    this.workId = this.activatedRoute.snapshot.queryParams.workId;
    this.form = formBuilder.group({
      workId: [null, [ZonValidators.required()]], // 工单ID
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
      zone: [], // 分布
      workType: [null, [ZonValidators.required()]], // 工单类型
      serials: [null, [ZonValidators.maxLength(50)]], // 厂商序列号
      warrantyMode: [], // 维保方式
      deviceId: [], // 设备号
      deviceCodes: [null, [ZonValidators.maxLength(50)]], // 设备编号
      smallClass: [], // 设备分类
      specification: [], // 设备规格ID
      specificationName: [], // 设备规格名称
      brand: [], // 设备品牌
      model: [], // 设备型号
      modelName: [], // 新设备型号
      serviceRequest: [null, [ZonValidators.maxLength(500)]], // 服务请求
      // faultTypes: [], // 故障现象
      faultTypeList: [], // 故障现象
      bookTimeEnd: [], // 预约时间结束
      faultTime: [], // 故障时间
      faultCode: [null, [ZonValidators.maxLength(50)]], // 故障代码
      source: [], // 工单来源
      files: [], // 附件,
      deleteFileIdList: [],
      newFileIdList: [],
      districts: [],
      deviceNum: [1], // 设备数量
      checkWorkCode: [null, [ZonValidators.maxLength(20)]], // 委托单号
      deviceDescription: ['', [ZonValidators.maxLength(100)]], // 设备描述
      basicServiceFee: [0, [ZonValidators.intOrFloat('费用报价'), ZonValidators.min(0, '费用报价'),
        ZonValidators.max(99999999.99, '费用报价')]] // 费用报价
    });
  }

  ngOnInit(): void {
    this.getWorkDetail();
    // 行政区划
    this.httpClient.get('/api/uas/area/list')
      .subscribe((res: any) => {
        this.districtsList = res.data;
      });
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
      itemValue: '',
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
      description: '客户名称',
      itemValue: 0
    };
    this.configItem[this.workConfigService.WORK_ADD_ALLOW_CREATE_DEVICE_BRANCH] = {
      isShow: false,
      description: '设备网点',
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
  };

  getConfigData() {
    this.dataConfigService.resetConfigShow(this.configItem, this.form);
    this.dataConfigService.getCorpConfigData(this.demanderCorpId, this.corpItemIdList, this.configItem).then((res: any) => {
      if (res.data && res.data.length > 0) {
        this.serviceConfigList = res.data;
        this.dataConfigService.validateConfigItem(this.configItem, this.form);
      }
    });
  }

  goBack() {
    history.go(-1);
  }

  /**
   * 获得工单详情
   */
  getWorkDetail() {
    this.isLoading = true;
    this.httpClient
      .get('/api/anyfix/work-request/detail/' + this.workId)
      .pipe(
        finalize(() => {
          this.isLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        if (res && res.data) {
          this.work = res.data;
          this.customFieldDataList = res.data.customFieldDataList;
          this.form.controls.serials.setValue(this.work.serial,
            {onlySelf: true, emitViewToModelChange: false});
          this.form.patchValue({
            workId: this.work.workId,
            demanderCorp: this.work.demanderCorp,
            contactName: this.work.contactName,
            contactPhone: this.work.contactPhone,
            address: this.work.address,
            district: this.work.district,
            workType: this.work.workType,
            deviceId: this.work.deviceId,
            serviceRequest: this.work.serviceRequest,
            faultCode: this.work.faultCode,
            checkWorkCode: this.work.checkWorkCode,
            deviceDescription: this.work.deviceDescription
          });
          this.form.patchValue({
            // tslint:disable-next-line:triple-equals
            smallClass: this.work.smallClass != 0 ? this.work.smallClass : null,
            // tslint:disable-next-line:triple-equals
            specification: this.work.specification != 0 ? this.work.specification : null,
            specificationName: this.work.specificationName,
            zone: this.work.zone ? this.work.zone : null,
            warrantyMode: this.work.warrantyMode ? this.work.warrantyMode : null,
            source: this.work.source ? this.work.source : null,
            basicServiceFee: this.work.workFeeDto ? this.work.workFeeDto.basicServiceFee : 0
          });

          this.demanderCorpId = this.work.demanderCorp;
          this.listCustomCorp(this.work.demanderCorp);
          this.loadWorkType(this.work.demanderCorp);
          this.initWorkTypeChange();
          this.listDeviceClass(this.work.demanderCorp);
          this.listDeviceBrand();

          const districtList = this.areaService.getAreaListByDistrict(this.work.district);
          this.form.patchValue({
            districts: districtList
          });

          if (this.work.bookTimeEnd) {
            this.bookDate = new Date(format(this.work.bookTimeEnd, 'YYYY-MM-DD'));
            this.bookEndTime = new Date(format(this.work.bookTimeEnd, 'YYYY-MM-DD HH:mm'));
          }
          if (this.work.faultTime) {
            this.faultDate = new Date(format(this.work.faultTime, 'YYYY-MM-DD'));
            this.faultTime = new Date(format(this.work.faultTime, 'YYYY-MM-DD HH:mm'));
          }
          this.initFileList();
          this.formDataBuild();
          this.setServiceRequestValue();
          this.getConfigData();
        }
      });
  }

  initWorkTypeChange() {
    if (this.work.workSysType === WorkSysTypeEnum.INSTALL) {
      this.isInstallWorkSysType = true;
      this.form.controls.smallClass.setValidators([ZonValidators.required()]);
      this.form.controls.specification.setValidators([ZonValidators.required()]);
    } else {
      this.isInstallWorkSysType = false;
      this.form.controls.smallClass.setValidators(null);
      this.form.controls.specification.setValidators(null);
    }
    if (this.work.workSysType === WorkSysTypeEnum.MAINTENANCE) {
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
  }

  /**
   * 客户列表
   * @param demanderCorp 委托商列表
   */
  listCustomCorp(demanderCorp) {
    this.httpClient.get(`/api/anyfix/demander-custom/custom/list/${demanderCorp}`).subscribe((result: Result) => {
      if (result.code === 0) {
        this.customCorpList = result.data || [];
        this.customCorpFilters = this.customCorpList;

        // 初始化选中的值
        this.customCorpList.forEach(item => {
          if (item.customId === this.work.customId) {
            this.custom = item;
          }
        });
        this.matchDeviceBranch(this.work.customId).then((res: any[]) => {
          if (res && res.length > 0) {
            res.forEach(option => {
              if (option.branchId === this.work.deviceBranch) {
                this.deviceBranch = option.branchId;
                this.deviceBranchChange(this.deviceBranch);
              }
            });
          }
        });
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
      this.customCorpFilters.push({customCorp: value + '[新增]', customCorpName: value + '[新增]'});
    }
  }


  /**
   * 客户企业改变
   * @param customCorp: 客户企业
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
   */
  matchDeviceBranch(corpId, branchName?: string) {
    if (!corpId) {
      this.deviceBranchList = [{branchId: branchName + '[新增]', branchName: branchName + '[新增]'}];
      return;
    }
    const params = {customId: corpId, matchFilter: branchName};
    return this.httpClient.post(`/api/anyfix/device-branch/match`, params).toPromise().then((result: Result) => {
      if (result.code === 0) {
        this.deviceBranchList = result.data || [];
        if (!branchName) {
          return this.deviceBranchList;
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
        return this.deviceBranchList;
      }
    });
    return null;
  }

  /**
   * 默认填充联系人，电话，地址信息
   * @param branchId: 设备网点id
   */
  deviceBranchChange(branchId: string) {
    // if (!this.needLoadChangeData) {
    //   return;
    // }
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
      if (this.configItem[this.workConfigService.WORK_ADD_REQUIRE_DEVICE_BRANCH].itemValue === ConfigRequireEnum.REQUIRED) {
        this.form.patchValue({
          deviceBranch: null,
          deviceBranchName: null
        });
      } else {
        this.form.patchValue({
          deviceBranch: 0,
          deviceBranchName: null
        });
      }
    }
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
   * 联系地址改变
   */
  addressChange() {
    this.form.controls.address.markAsDirty();
    this.form.controls.address.updateValueAndValidity();
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
        this.workTypeOptions = result.data || [];
        this.workTypeOptions.forEach(item => {
          if (item.id === this.work.workType) {
            this.workTypeSelect = item;
          }
        });
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
          const selectedList = [];
          if (this.work.faultTypeList && this.work.faultTypeList.length > 0) {
            this.work.faultTypeList.forEach((item: any) => {
              this.faultTypeList.forEach((faultType: any) => {
                if (faultType.id === item.id) {
                  selectedList.push(faultType);
                }
              });
            });
            this.form.patchValue({faultTypeList: selectedList});
          }
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

        let match = false;
        this.deviceSpecificationFilters.forEach(item => {
          if (item.id === this.work.specification) {
            this.specification = item.id;
            match = true;
          }
        });
        // 匹配不到ID，用名称匹配（存在型号已经生成到数据库，但是这单的id还是为0的情况）
        if (!match) {
          this.deviceSpecificationFilters.forEach(item => {
            if (item.name.toLowerCase() === this.work.specificationName.toLowerCase()) {
              this.specification = item.id;
              match = true;
            }
          });
        }
        if (!match && this.work.specificationName) {
          this.deviceSpecificationFilters.push({
            id: this.work.specificationName + '[新增]',
            name: this.work.specificationName + '[新增]'
          });
          this.specification = this.work.specificationName + '[新增]';
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
  listDeviceBrand() {
    const url = `/api/device/device-brand/list`;
    let params;
    params = {
      corp: this.demanderCorpId
    };
    this.httpClient.post(url, params).subscribe((result: Result) => {
      if (result.code === 0) {
        this.brandList = result.data || [];
        this.brandFilters = this.brandList;
        this.brandList.forEach(item => {
          if (item.id === this.work.brand) {
            this.brand = item.id;
          }
        });
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
    const smallClassValue = value;
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
        this.modelList = result.data || [];
        this.modelFilters = this.modelList;
        if (model && model.indexOf('[新增]') <= 0) {
          let matchValue = false;
          this.modelFilters.forEach(option => {
            if (option.id === model) {
              matchValue = true;
            }
          });
          if (!matchValue) {
            this.model = null;
          }
        }

        let match = false;
        this.modelFilters.forEach(item => {
          if (item.id === this.work.model) {
            this.model = item.id;
            match = true;
          }
        });
        // 匹配不到ID，用名称匹配（存在型号已经生成到数据库，但是这单的id还是为0的情况）
        if (!match) {
          this.modelFilters.forEach(item => {
            if (item.name === this.work.modelName) {
              this.model = item.id;
              match = true;
            }
          });
        }
        if (!match && this.work.modelName) {
          this.modelFilters.push({id: this.work.modelName + '[新增]', name: this.work.modelName + '[新增]'});
          this.model = this.work.modelName + '[新增]';
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
    let serials: string = this.form.value.serials || '';
    serials = serials.replace(/，/ig, ',').trim().toLocaleUpperCase();
    if (serials.includes(',')) {
      this.nzMessageService.error('每次只能修改一台设备！');
      serials = serials.replace(/,/ig, '');
      this.form.controls.serials.setValue(serials,
        {onlySelf: true, emitViewToModelChange: false});
      return;
    }
    this.form.controls.serials.setValue(serials,
      {onlySelf: true, emitViewToModelChange: false});
    this.findDeviceInfo();
  }

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

  initFileList() {
    const list = this.work.fileList || [];
    const idList: any[] = [];
    const imgList: any[] = [];
    const notImgList: any[] = [];
    list.forEach(file => {
      const nzFile: any = {};
      nzFile.uid = file.fileId;
      nzFile.fileId = file.fileId;
      nzFile.status = 'done';
      nzFile.ifDb = 'Y';
      nzFile.url = this.baseFileUrl + file.fileId;
      if (file.format === 1) {
        nzFile.name = '';
        nzFile.thumbUrl = this.baseFileUrl + file.fileId;
        imgList.push(nzFile);
      } else {
        nzFile.name = file.fileName;
        notImgList.push(nzFile);
      }
      idList.push(file.fileId);
    });
    this.imgFileList = [...imgList];
    this.notImgFileList = [...notImgList];
    this.fileList = [...idList];
  }

  handleChange(info: {file: UploadFile}) {
    switch (info.file.status) {
      case 'uploading':
        break;
      case 'done':
        if (info.file && info.file.response && info.file.response.data) {
          if (info.file.response.data.format !== 1) {
            this.notImgFileList.push(this.changeFile(info.file.response.data));
            this.imgFileList.splice(this.imgFileList.length - 1, 1);
          }
        }
        this.getFileIdList(info, 1);
        break;
      case 'removed':
        this.getFileIdList(info, 2);
        break;
      case 'error':
        break;
    }
    this.notImgFileList = [...this.notImgFileList];
  }

  changeFile(file) {
    const nzFile: any = {};
    nzFile.uid = file.fileId;
    nzFile.fileId = file.fileId;
    nzFile.status = 'done';
    nzFile.url = this.baseFileUrl + file.fileId;
    nzFile.name = file.fileName;
    // nzFile.file.response.data.fileId = file.fileId;
    return nzFile;
  }

  /**
   * 获得最终的文件编号列表
   * @param file 文件
   * @param type 类型，1=添加 2=删除
   */
  getFileIdList(info: any, type: number) {
    if (info) {
      let fileId;
      // 新增的文件信息
      if (info.file.response && info.file.response.data) {
        fileId = info.file.response.data.fileId;
      } else {
        fileId = info.file.fileId;
      }
      if (type === 1) {
        this.newFileIdList.push(fileId);
        // 新增
        this.fileList.push(fileId);
      } else if (type === 2) {
        // 删除
        const index = this.fileList.indexOf(fileId);
        if (index > -1) {
          this.fileList.splice(index, 1);
        }
        // 如果是新建但又没有用到的文件,不清除缓存表
        const indexNew = this.newFileIdList.indexOf(fileId);
        if (indexNew > -1) {
          this.newFileIdList.splice(indexNew, 1);
        }
        this.deleteFileIdList.push(fileId);
      }
    }
  }

  handlePreview = (file: UploadFile) => {
    this.previewImage = file.url || file.thumbUrl;
    this.previewVisible = true;
  };

  // 下载非图片格式文件
  downloadFile = (file: UploadFile) => {
    let isFileSaverSupported = false;
    try {
      isFileSaverSupported = !!new Blob();
    } catch {
    }
    if (!isFileSaverSupported) {
      console.log('浏览器版本过低');
      return;
    }
    this.httpClient.request('get', file.url, {
      params: {},
      responseType: 'blob',
      observe: 'response'
    }).subscribe(
      (res: HttpResponse<Blob>) => {
        if (res.status !== 200 || (res.body && res.body.size <= 0)) {
          return;
        }
        const disposition = this.getDisposition(res.headers.get('content-disposition'));
        let fileName = file.name;
        fileName =
          fileName || disposition[`filename*`] || disposition[`filename`] || res.headers.get('filename') || res.headers.get('x-filename');
        saveAs(res.body, decodeURI(fileName as string));
      },
      err => this.nzMessageService.error(err)
    );
  };

  private getDisposition(data: string | null) {
    const arr: Array<{}> = (data || '').split(';').filter(i => i.includes('=')).map(v => {
      const strArr = v.split('=');
      const utfId = `UTF-8''`;
      let value = strArr[1];
      if (value.startsWith(utfId)) {
        value = value.substr(utfId.length);
      }
      return {[strArr[0].trim()]: value};
    });
    // tslint:disable-next-line:variable-name
    return arr.reduce((_o, item) => item, {});
  }

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
      files: filesString,
      deleteFileIdList: this.deleteFileIdList,
      newFileIdList: this.newFileIdList
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
          customCorpName: this.custom.customCorpName
        });
      }
    } else {
      this.form.patchValue({
        customCorp: 0,
        customId: 0,
        customCorpName: null
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
          if (item.id === this.model) {
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
          if (item.id === this.model) {
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
        this.nzMessageService.error(error + '不能为空');
        return;
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
      this.setServiceRequestValue();
      // 提交表单
      this.isLoading = true;
      this.httpClient.post('/api/anyfix/work-request/update', this.form.value)
        .pipe(
          finalize(() => {
            this.isLoading = false;
          })
        ).subscribe((result: any) => {
        if (result.code === 0) {
          this.nzMessageService.success('修改成功');
          this.goBack();
        }
        // this.needLoadChangeData = true;
      });
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
    if (event && event.sysType === WorkSysTypeEnum.INSTALL) {
      this.isInstallWorkSysType = true;
      this.form.controls.smallClass.setValidators([ZonValidators.required()]);
      this.form.controls.specification.setValidators([ZonValidators.required()]);
    } else {
      this.isInstallWorkSysType = false;
      this.form.controls.smallClass.setValidators(null);
      this.form.controls.specification.setValidators(null);
    }
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
      const serviceRequest = this.form.value.serviceRequest || '';

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
        const requestList: any[] = serviceRequest.toString().split(',');
        const typeNameList: any[] = faultTypeNames.split(',');
        let names = '';
        if (faultTypeNames !== '') {
          typeNameList.forEach(str => {
            if (!requestList.includes(str)) {
              names = str + ',';
            }
          });
          this.form.patchValue({serviceRequest: names + serviceRequest.toString()});
        } else {
          this.form.patchValue({serviceRequest: serviceRequest.toString()});
        }
      } else {
        this.form.patchValue({serviceRequest: faultTypeNames});
      }
    }
  }
}
