import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {NzModalService} from 'ng-zorro-antd';
import {AnyfixService} from '@core/service/anyfix.service';
import {AnyfixModuleService} from '@core/service/anyfix-module.service';
import {Page} from '@core/interceptor/result';
import {WorkHistoryService} from './work-hisitory.service';
import {UserService} from '@core/user/user.service';
import {CustomListComponent} from '@shared/components/custom-list/custom-list.component';
import {WorkCheckService} from '../work-check.service';
import {ANYFIX_RIGHT} from '@core/right/right';
import {ZorroUtils} from '@util/zorro-utils';
import {WorkHistoryFilterComponent} from './filter/work-history-filter.component';
import {deepCopy} from '@delon/util';

@Component({
  selector: 'app-work-history',
  templateUrl: 'work-history.component.html',
  styleUrls: ['work-history.component.less']
})
export class WorkHistoryComponent implements OnInit {
  ZorroUtils = ZorroUtils;
  aclRight = ANYFIX_RIGHT;

  searchForm: FormGroup;
  workList: any = [];
  loading = true;
  page = new Page();
  visible = false;
  workStatusList: any = [];
  optionList: any = {};
  // 当前查询的工单状态 0=全部工单
  workStatusNow: any = 0;
  // 记录当前的条件状态
  savedPramsValue: any;
  all: 0;
  handle = 1;
  todayCreated = 2;
  todayFinished = 3;
  todayDispatched = 4;
  yesterdayFinished = 5;
  currentMonthFinished = 6;
  attention = 7;
  support = 8;

  allChecked = false;
  branchOption: any = [];
  checkOptionsOne: any = [];
  params: any = {};
  customTileList: any = [];
  showRowData: any = [];
  titleList: any = ['省', '市', '区', '客户名称', '设备类型', '出厂序列号', '品牌', '型号', '设备规格', '工单来源',
    '工单类型', '创建人', '创建时间', '服务审核', '费用审核', '委托商', '设备网点', '服务商', '服务网点', '工程师', '协同工程师', '外部协同人员',
    '预约时间', '提单日期', '完单日期', '服务方式', '服务请求', '联系人', '联系电话', '详细地址', '维保方式', '故障现象',
    '接单时间', '出发时间', '到达时间'];
  widthList: any = [50, 80, 100, 200, 150, 150, 100, 150, 100, 80,
    100, 80, 150, 100, 100, 100, 120, 100, 120, 80, 100, 100,
    150, 150, 150, 80, 250, 100, 150, 250, 80, 150,
    150, 150, 150];
  fieldList: any = ['provinceName', 'cityName', 'districtName', 'customCorpName', 'smallClassName',
    'serial', 'brandName', 'modelName', 'specificationName', 'sourceName',
    'workTypeName', 'creatorName', 'createTime', 'finishStatusName', 'feeStatusName', 'demanderCorpName', 'deviceBranchName',
    'serviceCorpName', 'serviceBranchName', 'engineerName', 'togetherEngineers', 'helpNames',
    'bookTimeEnd', 'dispatchTime', 'finishTime', 'serviceModeName', 'serviceRequest',
    'contactName', 'contactPhone', 'address', 'warrantyModeName', 'faultTypeName',
    'acceptTime', 'goTime', 'signTime'];
  customListKey = 'historyCustomList';
  defaultCols = 13;
  scrollWidth = 560;
  totalWidth = 560;
  checkStatusList = this.workCheckService.checkStatusList;
  currentRow = 0;
  // 需要导出的工单数量
  workNum: number;
  // 判断当前企业是否是服务商
  isServiceCorp: boolean = this.userService.currentCorp.serviceProvider === 'Y' ? true : false;
  // 审核确认状态
  checkConfirmObject: any = {};

  @ViewChild(WorkHistoryFilterComponent, {static: false}) workHistoryFilterComponent: WorkHistoryFilterComponent;

  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private router: Router,
              public userService: UserService,
              private nzModalService: NzModalService,
              private workHistoryService: WorkHistoryService,
              private activatedRoute: ActivatedRoute,
              public anyfixService: AnyfixService,
              public workCheckService: WorkCheckService,
              private anyfixModuleService: AnyfixModuleService) {
    this.searchForm = this.formBuilder.group({
      workCode: [],
      checkWorkCode: [],
      workTypes: [],
      workStatuses: [],
      demanderCorp: [],
      finishCheckStatuses: [],
      feeCheckStatuses: [],
      finishConfirmStatuses: [],
      feeConfirmStatuses: [],
      customId: [],
      smallClassId: [],
      deviceModel: [],
      deviceBrand: [],
      contactName: [],
      contactPhone: [],
      dispatchTime: [],
      createTime: [],
      serial: [],
      district: [],
      area: [],
      serviceMode: [],
      workFeeStatus: [],
      finishTime: [],
      workStatusCheck: [],
      serviceBranches: [],
      engineer: [],
      hasWorkChat: [],
      isReview: []
    });
  }

  ngOnInit(): void {
    //  进来界面初始化，选中全部
    if (!this.isServiceCorp) {
      this.defaultCols = 13;
    }
    this.workStatusNow = this.all;
    this.initShowTitleAndColumn();
    this.initOptionList();
    this.getParamsValue();
    this.initWorkStatusCheckBox();
    // 初始化审核确认状态选项
    this.initCheckConfirmObject();
    this.loadWorkList(this.getParams());
    this.getBranchOption('');
  }


  getParamsValue() {
    const params = this.workHistoryService.paramsValue;
    if (params) {
      // 解决点击返回键报错问题
      if (params.formValue.demanderCorp === undefined) {
        params.formValue.demanderCorp = null;
      }
      if(params.formValue.workCode ===undefined){
        params.formValue.workCode = null;
      }
      if(params.formValue.checkWorkCode ===undefined){
        params.formValue.checkWorkCode = null;
      }
      if(params.formValue.workTypes ===undefined){
        params.formValue.workTypes = null;
      }
      if(params.formValue.customId ===undefined){
        params.formValue.customId = null;
      }
      if(params.formValue.smallClassId ===undefined){
        params.formValue.smallClassId = null;
      }
      if(params.formValue.area ===undefined){
        params.formValue.area = null;
      }
      if(params.formValue.contactName ===undefined){
        params.formValue.contactName = null;
      }
      if(params.formValue.contactPhone ===undefined){
        params.formValue.contactPhone = null;
      }
      if(params.formValue.deviceBrand ===undefined){
        params.formValue.deviceBrand = null;
      }
      if(params.formValue.deviceModel ===undefined){
        params.formValue.deviceModel = null;
      }
      if(params.formValue.hasWorkChat ===undefined){
        params.formValue.hasWorkChat = null;
      }
      if(params.formValue.serial ===undefined){
        params.formValue.serial = null;
      }
      if(params.formValue.serviceMode ===undefined){
        params.formValue.serviceMode = null;
      }
      if (params.formValue.workFeeStatus === undefined) {
        params.formValue.workFeeStatus = null;
      }
      this.searchForm.setValue(params.formValue);
      this.page = params.page;
      this.workStatusNow = params.extra.workStatusNow;
    }
    this.workHistoryService.paramsValue = null;
  }

  getAllStatus(): string {
    return this.anyfixService.TO_DISTRIBUTE + ',' + this.anyfixService.TO_HANDLE + ',' +
      this.anyfixService.TO_ASSIGN + ',' + this.anyfixService.TO_CLAIM + ',' +
      this.anyfixService.TO_SIGN + ',' + this.anyfixService.TO_SERVICE + ',' +
      this.anyfixService.IN_SERVICE + ',' + this.anyfixService.RETURNED;
  }

  initWorkStatusCheckBox() {
    this.checkOptionsOne = [];
    const statusMap = this.anyfixService.getWorkStatusMap();
    Object.keys(statusMap).forEach(key => {
      this.checkOptionsOne.push({label: statusMap[key], value: key, checked: true});
    });
  }

  // 初始化审核确认状态选项
  initCheckConfirmObject() {
    const checkStatusList = Object.keys(this.workCheckService.serviceCheckStatusMap).map(checkStatus => {
      return {
        label: this.workCheckService.serviceCheckStatusMap[checkStatus],
        value: checkStatus,
        checked: true
      };
    });
    const confirmStatusList = Object.keys(this.workCheckService.demnanderCheckStatusMap).map(confirmStatus => {
      return {
        label: this.workCheckService.demnanderCheckStatusMap[confirmStatus],
        value: confirmStatus,
        checked: true
      }
    });
    this.checkConfirmObject.finishCheckStatusList = deepCopy(checkStatusList);
    this.checkConfirmObject.feeCheckStatusList = deepCopy(checkStatusList);
    this.checkConfirmObject.finishConfirmStatusList = deepCopy(confirmStatusList);
    this.checkConfirmObject.feeConfirmStatusList = deepCopy(confirmStatusList);
  }

  saveParamsValue() {
    const params = {
      formValue: this.searchForm.value,
      page: this.page,
      extra: {
        workStatusNow: this.workStatusNow
      }
    };
    this.savedPramsValue = params;
  }


  initOptionList() {
    this.anyfixModuleService.getWorkType().subscribe((res: any) => {
      this.optionList.workTypeOption = res.data;
    });
    this.anyfixModuleService.getWorkSourceOption().subscribe((res: any) => {
      this.optionList.workSourceOption = res.data;
    });
    this.anyfixModuleService.getDemanderOption().subscribe((res: any) => {
      this.optionList.demanderOption = res.data;
    });
    this.anyfixModuleService.getCustomOption().subscribe((res: any) => {
      this.optionList.customOption = res.data;
    });
    this.anyfixModuleService.getAreaInfoOption().subscribe((res: any) => {
      this.optionList.areaInfoOption = res.data;
    });
    this.anyfixModuleService.getProvinceOption().subscribe((res: any) => {
      this.optionList.provinceOption = res.data;
    });
  }

  loadWorkList(params: any, reset?: boolean): void {
    if (reset) {
      this.page.pageNum = 1;
      params.pageNum = 1;
    }
    this.loading = true;
    this.saveParamsValue();
    this.httpClient
      .post('/api/anyfix/work-request/query', params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const page = res.data;
        this.page.total = page.total;
        this.workList = page.list;
        this.workNum = page.total;
        if (typeof this.workList === 'object') {
          this.workList.forEach(item => {
            switch (item.workStatus) {
              case this.anyfixService.TO_DISTRIBUTE:
                item.buttonColor = {'background-color': '#5E86C8'};
                break;
              case this.anyfixService.TO_HANDLE:
                item.buttonColor = {'background-color': '#FFD617'};
                break;
              case this.anyfixService.TO_ASSIGN:
                item.buttonColor = {'background-color': '#5EC894'};
                break;
              case this.anyfixService.TO_CLAIM:
                item.buttonColor = {'background-color': '#7ED321'};
                break;
              case this.anyfixService.TO_SIGN:
                item.buttonColor = {'background-color': '#48B8FF'};
                break;
              case this.anyfixService.TO_SERVICE:
                item.buttonColor = {'background-color': '#FF7717'};
                break;
              case this.anyfixService.IN_SERVICE:
                item.buttonColor = {'background-color': '#FF9F00'};
                break;
              case this.anyfixService.TO_EVALUATE:
                item.buttonColor = {'background-color': '#6A77D3'};
                break;
              case this.anyfixService.RETURNED:
                item.buttonColor = {'background-color': '#65CEDC'};
                break;
              case this.anyfixService.CLOSED:
                item.buttonColor = {'background-color': '#B5D46F'};
                break;
              case this.anyfixService.CANCELED:
                item.buttonColor = {'background-color': '#D3D3D3'};
                break;
            }
            if ((item.workStatus === this.anyfixService.TO_EVALUATE
              || item.workStatus === this.anyfixService.RETURNED
              || item.workStatus === this.anyfixService.CLOSED)
              && item.finishCheckStatus === 0) {
              item.finishStatusName = '未提交';
            }
            if ((item.workStatus === this.anyfixService.TO_EVALUATE
              || item.workStatus === this.anyfixService.RETURNED
              || item.workStatus === this.anyfixService.CLOSED)
              && item.feeCheckStatus === 0) {
              item.feeStatusName = '未提交';
            }
          });
        }
      });
  }

  queryWork(reset?: boolean) {
    // 工单筛选数据收集
    this.workHistoryFilterComponent.submit();

    this.loadWorkList(this.getParams(), reset);
  }

  changeTag(workStatus) {
    this.workStatusNow = workStatus;
    this.loadWorkList(this.getParams(), true);
  }


  // 收集工单筛选数据
  searchFormDataChange(searchFormData: {key: string}[]) {
    this.searchForm.patchValue(searchFormData)
  }

  getParams() {
    const params = Object.assign({}, this.searchForm.value);
    // 完单日期
    const finishTime = this.searchForm.controls.finishTime.value;
    if (finishTime) {
      params.finishStartDate = finishTime[0];
      params.finishEndDate = finishTime[1];
      params.finishTime = null;
    }
    // 创建时间
    const createTime = this.searchForm.controls.createTime.value;
    if (createTime) {
      params.startDate = createTime[0];
      params.endDate = createTime[1];
      params.createTime = null;
    }
    // 提单日期
    const dispatchTime = this.searchForm.controls.dispatchTime.value;
    if (dispatchTime) {
      params.dispatchTimeStart = dispatchTime[0];
      params.dispatchTimeEnd = dispatchTime[1];
      params.dispatchTime = null;
    }
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    switch (this.workStatusNow) {
      case this.handle:
        params.workStatuses = this.anyfixService.getHandledWorkStatus();
        break;
      case this.attention:
        params.isAttention = 'Y';
        break;
      case this.support:
        params.isSupport = 'Y';
        break;
      case this.todayCreated:
        params.startDate = new Date();
        params.endDate = new Date();
        break;
      case this.todayDispatched:
        params.dispatchTimeStart = new Date();
        params.dispatchTimeEnd = new Date();
        break;
      case this.todayFinished:
        params.finishTimeDay = new Date();
        break;
      case this.yesterdayFinished:
        const time = new Date();
        time.setTime(time.getTime() - 24 * 60 * 60 * 1000);
        params.finishTimeDay = time;
        break;
      case this.currentMonthFinished :
        params.finishTimeMonth = new Date();
        break;
      case this.anyfixService.CANCELED:
        params.workStatuses = this.anyfixService.CANCELED;
        break;
      case this.anyfixService.CLOSED:
        params.workStatuses = this.anyfixService.CLOSED;
        break;
      case this.anyfixService.RETURNED:
        params.workStatuses = this.anyfixService.RETURNED;
        break;
      case this.all:
        // 拼接workStatuses字符串
        if (typeof params.workStatusCheck === 'object' && params.workStatusCheck !== null) {
          let workStatuses = '';
          params.workStatusCheck.forEach(workStatus => {
            if (workStatus.checked) {
              workStatuses = workStatuses + workStatus.value + ',';
            }
          });
          if (workStatuses !== '') {
            workStatuses = workStatuses.substr(0, workStatuses.length - 1);
          }
          params.workStatuses = workStatuses;
          params.workStatusCheck = null;
        }
        break;
    }
    // 审核确认状态
    if (params.finishCheckStatuses) {
      params.finishCheckStatuses = params.finishCheckStatuses.filter(item => item.checked).map(finishCheckStatus => {
          return finishCheckStatus.value;
      }).join(',');
    }
    if (params.feeCheckStatuses) {
      params.feeCheckStatuses = params.feeCheckStatuses.filter(item => item.checked).map(feeCheckStatus => {
          return feeCheckStatus.value;
      }).join(',');
    }
    if (params.finishConfirmStatuses) {
      params.finishConfirmStatuses = params.finishConfirmStatuses.filter(item => item.checked).map(finishConfirmStatus => {
          return finishConfirmStatus.value;
      }).join(',');
    }
    if (params.feeConfirmStatuses) {
      params.feeConfirmStatuses = params.feeConfirmStatuses.filter(item => item.checked).map(feeConfirmStatus => {
          return feeConfirmStatus.value;
      }).join(',');
    }
    return params;
  }

  openDrawer() {
    this.visible = true;
  }

  closeDrawer() {
    this.visible = false;
  }

  getBranchOption(event) {
    this.httpClient
      .post('/api/anyfix/service-branch/match', {
        serviceCorp: this.userService.currentCorp.corpId,
        branchName: event
      })
      .subscribe((res: any) => {
        this.branchOption = res.data;
      });
  }

  // 工程师更换
  engineerChange(engineer) {
    if (this.searchForm.value.serviceBranches || engineer) {
      this.branchChange(this.searchForm.value.serviceBranches, engineer);
    } else {
      this.optionList.engineerList = [];
    }
  }

  // 服务网点更换
  branchChange(branch, engineer?) {
    if (branch !== undefined) {
      this.optionList.engineerList = [];
      if (!engineer) {
        engineer = this.searchForm.value.engineer;
      }
      if (!branch && !engineer) {
        return;
      }
      if (!branch) {
        branch = 0;
      }
      this.anyfixModuleService.getEngineerListOption(branch, engineer)
        .subscribe((res: any) => {
          this.optionList.engineerList = res.data;
        });
    }
  }

  clearDrawer() {
    this.searchForm.reset();
    this.initCheckConfirmObject();
    this.loadWorkList(this.getParams());
    this.getBranchOption('');
    this.initWorkStatusCheckBox();
    this.loadWorkList(this.getParams());
  }

  areaChange(event) {
    if (event === null) {
      this.searchForm.controls.district.setValue(null);
      return;
    }
    if (event !== undefined) {
      if (event.length > 2) {
        this.searchForm.controls.district.setValue(event[2]);
      } else if (event.length > 1) {
        this.searchForm.controls.district.setValue(event[1]);
      } else if (event.length === 1) {
        this.searchForm.controls.district.setValue(event[0]);
      }
    }
  }

  toWorkDetail(workId) {
    this.workHistoryService.paramsValue = this.savedPramsValue;
    const workIds = this.workList.map((item) => {
      return item.workId;
    });
    this.anyfixService.setParamsOnce(workIds);
    this.router.navigate(['/anyfix/work-detail'], {queryParams: {workId}, relativeTo: this.activatedRoute});
  }


  changeColor(workStatus) {
    return workStatus === this.workStatusNow ? {color: '#20BF8E'} : {color: '#999999'};
  }

  clickRow(workId) {
    this.currentRow = workId;
  }

  rowColor(workId) {
    return workId === this.currentRow ? {'background-color': '#FFEFC4'} : null;
  }

  updateAllChecked() {
    if (this.allChecked) {
      this.checkOptionsOne = this.checkOptionsOne.map(item => {
        return {
          ...item,
          checked: true
        };
      });
    } else {
      this.checkOptionsOne = this.checkOptionsOne.map(item => {
        return {
          ...item,
          checked: false
        };
      });
    }
  }

  updateSingleChecked() {
    if (typeof this.checkOptionsOne === 'object' && this.checkOptionsOne !== null) {
      // 异步更新
      if (this.checkOptionsOne.every(item => item.checked)) {
        Promise.resolve(null).then(() => {
          this.allChecked = true;
        });
      } else {
        Promise.resolve(null).then(() => {
          this.allChecked = false;
        });
      }
    }
  }


  initShowTitleAndColumn() {
    let saved = false;
    const submitList = localStorage.getItem(this.customListKey);
    if (submitList !== null && submitList !== undefined) {
      this.customTileList = JSON.parse(submitList);
      if (this.customTileList.length === this.titleList.length) {
        saved = true;

        for (let i = 0; i < this.customTileList.length; i++) {
          this.customTileList[i].width = this.widthList[i];
          this.customTileList[i].title = this.titleList[i];
          // if( this.customTileList[i].checked === false){
          //   this.showRowData[this.fieldList[i]] = false;
          // }else if( this.customTileList[i].checked === true ){
          //   this.showRowData[this.fieldList[i]] = true;
          //   this.totalWidth += this.customTileList[i].width;
          // }
        }
        this.resetShowTable(this.customTileList);
      }
    }
    if (!saved) {
      const objList = [];
      for (let i = 0; i < this.titleList.length; i++) {
        const obj = {
          title: this.titleList[i],
          checked: true,
          width: this.widthList[i]
        };
        if (i >= this.defaultCols) {
          obj.checked = false;
          // this.showRowData[this.fieldList[i]] = false;
          // }else {
          // this.showRowData[this.fieldList[i]] = true;
          // this.totalWidth += this.widthList[i];
        }
        objList.push(obj);
      }
      this.customTileList = objList;
      this.resetShowTable(this.customTileList);
    }
  }

  openCustomList() {
    let titleList =  JSON.stringify(this.customTileList);
    titleList = JSON.parse(titleList);
    const modal = this.nzModalService.create({
      nzTitle: '自定义列',
      nzContent: CustomListComponent,
      nzFooter: null,
      nzWidth: 900,
      nzComponentParams: {
        titleList,
        type: this.customListKey
      }
    });
    modal.afterClose.subscribe((res: any) => {
      if (res) {
        this.resetShowTable(res);
      }
    });
  }

  resetShowTable(titleList) {
    this.resetTotalWidth();
    let width = 0;
    this.customTileList = titleList;
    for (let i = 0; i < titleList.length; i++) {
      if (titleList[i].checked) {
        this.showRowData[this.fieldList[i]] = true;
        // 重新调整滑动窗口大小
        width += titleList[i].width;
      } else {
        this.showRowData[this.fieldList[i]] = false;
      }
    }
    this.totalWidth += width;
  }

  resetTotalWidth() {
    this.totalWidth = this.scrollWidth;
  }

  /**
   * 导出工单
   */
  exportWork() {
    const params = this.getParams();
    this.httpClient
      .post('/api/anyfix/work-excel/export', params)
      .subscribe((res:any) => {
        this.branchOption = res.data;
      });
  }

  /**
   * 委托商改变
   */
  changeDemanderCorp(event) {
    if (!event) {
      return;
    }
    this.anyfixModuleService.getDeviceSmallClassOptionByDemanderCorp(event).subscribe((res: any) => {
      this.optionList.deviceSmallClassOption = res.data;
    });
    this.anyfixModuleService.getDeviceModelOptionByDemanderCorp(event).subscribe((res: any) => {
      this.optionList.deviceModelOption = res.data;
    });
    this.anyfixModuleService.getDeviceBrandOptionByDemanderCorp(event).subscribe((res: any) => {
      this.optionList.deviceBrandOption = res.data;
    });
  }
}
