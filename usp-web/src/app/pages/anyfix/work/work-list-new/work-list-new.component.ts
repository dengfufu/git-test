import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {delay, finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {NzModalService} from 'ng-zorro-antd';
import {AnyfixService} from '@core/service/anyfix.service';
import {AnyfixModuleService} from '@core/service/anyfix-module.service';
import {WorkAddComponent} from '../../add/work-add.component';
import {Page} from '@core/interceptor/result';
import {ANYFIX_RIGHT} from '@core/right/right';
import {UserService} from '@core/user/user.service';
import {ImportWorkComponent} from '../../import/import-work.component';
import {StProComponent} from '@shared/components/st-pro/st-pro.component';
import {
  STProOverflowMode,
  STProColumn,
  STProData,
  STProReq,
  STProRes,
  STProSortIndicator,
  STProColumnBadge, STProColumnTag, STProRequestOptions
} from '@shared/components/st-pro/st.pro.interfaces';

export const WORK_STATUS_TAG: STProColumnTag = {
  10: { text: '待提单', color: '#5E86C8' },
  20: { text: '待分配', color: '#FFD617' },
  30: { text: '待派单', color: '#5EC894' },
  40: { text: '待接单', color: '#7ED321' },
  50: { text: '待签到', color: '#48B8FF' },
  60: { text: '待服务', color: '#FF7717' },
  70: { text: '服务中', color: '#FF9F00' },
  80: { text: '待评价', color: '#6A77D3' },
  90: { text: '已完成', color: '#B5D46F' },
  100: { text: '已退单', color: '#65CEDC' },
  110: { text: '已撤单', color: '#D3D3D3' },
};

@Component({
  selector: 'app-work-list-new',
  templateUrl: 'work-list-new.component.html',
  styleUrls: ['work-list-new.component.less']
})
export class WorkListNewComponent implements OnInit {
  aclRight = ANYFIX_RIGHT;

  searchForm: FormGroup;
  workList: any = [];
  loading = true;
  visible = false;
  // 默认全部选中
  allChecked = true;
  // 全选的中间状态
  indeterminate = false;
  workStatusOptions: any = [];
  // 用于总数统计的 option
  allWorkStatus: any = [];
  optionList: any = {};
  // 当前查询的工单状态 0=全部工单
  workStatusNow: any = 0;
  workStatusCountList: any = {};
  params: any = {};
  branchOption : any = [];
  customTileList : any = [];
  showRowData : any = [];

  defaultCols = 13;
  totalWidth = 450;
  currentRow = 0;
  // 需要导出的工单数量
  workNum: number;
  isDemanderCorp: boolean = this.userService.currentCorp.serviceDemander === 'Y' ? true : false;
  
  @ViewChild(StProComponent,{static:true}) stPro;

  page: Page = new Page(1,10, [10,20,50,100]);
  
  columns: STProColumn[] = [
    {
      index: 'id',
      type: 'checkbox',
      fixed: 'left',
      width: 50
    },
    {
      title: '序号',
      index: '_no',
      type: 'no',
      fixed: 'left',
      width: 50,
    },
    {
      title: '工单状态',
      type: 'tag',
      fixed: 'left',
      width: 100,
      index: 'workStatus',
      tag: WORK_STATUS_TAG,
      sort: 'workStatusOrder'
    },
    {
      title: '工单编号',
      index: 'workCode',
      fixed: 'left',
      width: 150,
      type: 'link',
      click: (record: STProData)=>{
        this.toWorkDetail(record.workId);
      }
    },{
      title: '费用',
      index: 'amt',
      type: 'currency',
      width: 100,
      numberDigits: '1.0-2',
    }, {
      title: '委托单号',
      index: 'checkWorkCode',
      // format: (item: STProData, col: STProColumn, index: number) => deepGet(item, col.index,'') || '--',
      width: 150
    },
    {
      title: '省',
      index: 'provinceName',
      width: 80,
      sort: 'provinceNameOrder'
    },{
      title: '市',
      index: 'cityName',
      width: 80
    },{
      title: '区',
      index: 'districtName',
      width: 100
    },{
      title: '客户名称',
      index: 'customCorpName',
      width: 200
    },{
      title: '设备类型',
      index: 'smallClassName',
      width: 150
    },{
      title: '出厂序列号',
      index: 'serial',
      width: 100
    },{
      title: '品牌',
      index: 'brandName',
      width: 100
    },{
      title: '型号',
      index: 'modelName',
      width: 150
    },{
      title: '设备规格',
      index: 'specificationName',
      width: 100
    },{
      title: '工单来源',
      index: 'sourceName',
      width: 80
    },{
      title: '工单类型',
      index: 'workTypeName',
      width: 80
    },{
      title: '创建人',
      index: 'creatorName',
      width: 80
    },{
      title: '创建时间',
      index: 'createTime',
      type: 'date',
      dateFormat: 'YYYY-MM-DD HH:mm',
      width: 150
    },{
      title: '委托商',
      index: 'demanderCorpName',
      width: 100
    },{
      title: '设备网点',
      index: 'deviceBranchName',
      width: 150
    },{
      title: '服务商',
      index: 'serviceCorpName',
      width: 100
    },{
      title: '服务网点',
      index: 'serviceBranchName',
      width: 150
    },{
      title: '工程师',
      index: 'engineerName',
      width: 100
    },{
      title: '协同工程师',
      index: 'togetherEngineers',
      width: 100
    },{
      title: '外部协同工程师',
      index: 'helpNames',
      width: 150
    },{
      title: '预约时间',
      index: 'bookTimeEnd',
      type: 'date',
      dateFormat: 'YYYY-MM-DD HH:mm',
      width: 150
    },{
      title: '提单时间',
      index: 'dispatchTime',
      type: 'date',
      dateFormat: 'YYYY-MM-DD HH:mm',
      width: 150
    },{
      title: '完单日期',
      index: 'endTime',
      type: 'date',
      dateFormat: 'YYYY-MM-DD HH:mm',
      width: 150
    },{
      title: '服务方式',
      index: 'serviceModeName',
      width: 150
    },{
      title: '服务请求',
      index: 'serviceRequest',
      width: 250
    },{
      title: '联系人',
      index: 'contactName',
      width: 100
    },{
      title: '联系电话',
      index: 'contactPhone',
      width: 150
    },{
      title: '详细地址',
      index: 'address',
      width: 250
    },{
      title: '维保方式',
      index: 'warrantyModeName',
      width: 100
    },{
      title: '故障现象',
      index: 'faultTypeName',
      width: 150
    },{
      title: '接单时间',
      index: 'acceptTime',
      type: 'date',
      dateFormat: 'YYYY-MM-DD HH:mm',
      width: 150
    },{
      title: '出发时间',
      index: 'goTime',
      type: 'date',
      dateFormat: 'YYYY-MM-DD HH:mm',
      width: 150
    },{
      title: '到达(签到)时间',
      index: 'signTime',
      type: 'date',
      dateFormat: 'YYYY-MM-DD HH:mm',
      width: 150
    },
    {
      title: '操作',
      index: '_op',
      width: 100,
      fixed: 'right',
      buttons: [
        {
          text: '修改',
          click: (record)=>{console.log(record)}
        },
        {
          text: '删除',
          pop: true,
          popTitle: '确认删除?',
          click: (record)=>{console.log('删除记录',record)}
        },
      ]
    },
  ];
  
  sortInd:STProSortIndicator = {ascend:'asc',descend:'desc'};
  
  overflowMode: STProOverflowMode = 'truncate';
  
  scrollX:string;
  
  url = '/api/anyfix/work-request/query';
  
  req: STProReq = {
    process: (option: STProRequestOptions) => {
      option.body = {...option.body, ...this.getParams()};
      return option;
    }
  };
  res: STProRes = {
    process: (data: STProData[], rawData?: any) => {
      data.forEach(e=>{
        e.amt = Math.random() * 1000
      });
      return data;
    }
  };
  
  nzFilterOption = () => true;
  
  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private router: Router,
              public userService: UserService,
              private nzModalService: NzModalService,
              private activatedRoute: ActivatedRoute,
              public anyfixService: AnyfixService,
              private anyfixModuleService: AnyfixModuleService,
              ) {
    this.initWorkStatusOption();
  
    this.searchForm = this.formBuilder.group({
      workCode: [],
      checkWorkCode: [],
      workTypes: [],
      workStatuses: [this.workStatusOptions,[]],
      demanderCorp: [],
      customId: [],
      smallClassId: [],
      deviceModel: [],
      deviceBrand: [],
      contactName: [],
      contactPhone: [],
      createTime: [],
      dispatchTime: [],
      serial: [],
      district: [],
      area: [],
      deviceBranch: [],
      serviceModes: [],
      provinceCode:[],
      serviceBranches:[]
    });
  }
  
  ngOnInit(): void {
    this.initOptionList();
    this.getBranchOption('');
    let params = this.anyfixService.getParams();
    if (params && params._type === 'work-list') {
      this.params = params;
      this.workStatusOptions = params.workStatusOptions;
      this.workStatusNow = params.workStatusNow;
      this.allChecked = params.allChecked;
      this.page.pageNum = params.pageNum;
      this.page.pageSize = params.pageSize;
      // this.loadWorkList(params);
      params.workStatuses = this.allWorkStatus;
      this.getWorkStatusCount(params);
      this.anyfixService.setParams(null);
    } else {
      // this.loadWorkList(this.getPageParam());
      params = this.getParams();
      params.workStatuses = this.allWorkStatus;
      this.getWorkStatusCount(params);
    }
  }
  
  /**
   * 构造多选框选项
   */
  initWorkStatusOption() {
    this.workStatusOptions = [];
    const statusMap = this.anyfixService.getWorkStatusMap();
    this.anyfixService.workStatusList.forEach(item => {
      // 也包含已退单
      if (item < this.anyfixService.CLOSED || item === this.anyfixService.RETURNED) {
        this.workStatusOptions.push({label: statusMap[item], value: item, checked: true});
      }
    });
    this.allWorkStatus = this.workStatusOptions.map(i=>i.value).join(',');
  }
  
  /**
   * 初始化各个下拉列表
   */
  initOptionList() {
    this.anyfixModuleService.getWorkType().subscribe((res: any) => {
      this.optionList.workTypeOption = res.data;
    });
    this.anyfixModuleService.getWorkSourceOption().subscribe((res: any) => {
      this.optionList.workSourceOption = res.data;
    });
    this.anyfixModuleService.getDemanderOption().subscribe((res:any)=>{
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
  
  /**
   * 机构下拉选项
   * @param event
   */
  getBranchOption(event) {
    this.httpClient
      .post('/api/anyfix/service-branch/match', {
        serviceCorp: this.userService.currentCorp.corpId,
        branchName : event
      })
      .subscribe((res:any) => {
        this.branchOption = res.data;
      });
  }
  
  /**
   * 手工触发列表刷新
   */
  queryWork() {
    const params = this.getParams();
    this.stPro.reset();
    params.workStatuses = this.allWorkStatus;
    this.getWorkStatusCount(params);
  }

  getAllWork() {
    this.workStatusOptions.forEach(item => {
      item.checked = true;
    });
    this.searchForm.controls.workStatuses.patchValue(this.workStatusOptions, {mitEvent:true});

    this.stPro.reset();
  
    this.workStatusNow = 0;
  }

  changeTag(workStatus) {
    this.workStatusOptions.forEach(item => {
      if (item.value === workStatus) {
        item.checked = true;
        this.workStatusNow = workStatus;
      } else {
        item.checked = false;
      }
    });
    this.searchForm.controls.workStatuses.patchValue(this.workStatusOptions, {mitEvent:true});
    this.stPro.reset();
  }
  
  // 查询受理中工单
  getHandledWork(reset: boolean = false) {
    this.workStatusNow = 1;
    // this.allChecked = false;
    this.workStatusOptions.forEach(item => {
      this.anyfixService.handledWorkStatusList.indexOf(item.value) > -1 ? item.checked =true : item.checked =false;
    });
    this.searchForm.controls.workStatuses.patchValue(this.workStatusOptions, {mitEvent:true});
    this.stPro.reset();
    //
    const params = this.getParams();
    // this.loadWorkList(params, reset);
    params.workStatuses = this.allWorkStatus;
    this.getWorkStatusCount(params);
  }
  
  
  getParams() {
    const params = Object.assign({}, this.searchForm.value);
    // params.pageNum = this.page.pageNum;
    // params.pageSize = this.page.pageSize;
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

    const workStatuses  = this.searchForm.value.workStatuses.filter(i=>i.checked).map(i=>i.value);
    params.workStatuses = workStatuses.join(',');
    return params;
  }
  
  openDrawer() {
    this.visible = true;
  }

  closeDrawer() {
    this.visible = false;
  }

  clearDrawer() {
    // 清空筛选条件
    this.searchForm.reset();
    const params = this.getParams();
  
    this.stPro.reset();
    params.workStatuses = this.allWorkStatus;
    this.getWorkStatusCount(params);
  }

  updateSingleChecked() {
    if (typeof this.workStatusOptions === 'object' && this.workStatusOptions !== null) {
      if (this.workStatusOptions.every(item => !item.checked)) {
        this.allChecked = false;
        this.indeterminate = false;
      } else if (this.workStatusOptions.every(item => item.checked)) {
        this.allChecked = true;
        this.indeterminate = false;
      } else {
        this.allChecked = false;
        this.indeterminate = true;
      }
    }
  }

  updateAllChecked() {
    this.indeterminate = false;
    
    if (this.allChecked) {
      this.workStatusOptions = this.workStatusOptions.map(item => {
        return {
          ...item,
          checked: true
        };
      });
    } else {
      this.workStatusOptions = this.workStatusOptions.map(item => {
        return {
          ...item,
          checked: false
        };
      });
    }
  
    this.searchForm.controls.workStatuses.patchValue(this.workStatusOptions, {mitEvent:true});
  
  }

  toWorkDetail(workId) {
    const params = this.getParams();
    this.params.workStatuses = params.workStatuses;
    this.params._type = 'work-list';
    this.params.workStatusOptions = this.workStatusOptions;
    this.params.workStatusNow = this.workStatusNow;
    this.params.allChecked = this.allChecked;
    this.params.pageNum = this.page.pageNum;
    this.params.pageSize = this.page.pageSize;
    this.anyfixService.setParams(this.params);
    this.router.navigate(['/anyfix/work-detail'], {queryParams: {workId}, relativeTo: this.activatedRoute});
  }

  // 获取各个状态的工单数量
  getWorkStatusCount(params) {
    this.httpClient.post('/api/anyfix/work-stat/status/count', params).pipe(
      finalize(() => {
        this.cdf.markForCheck();
      })
    )
      .subscribe((res: any) => {
        if (res.data !== null) {
          this.workStatusCountList = res.data;
          if (typeof this.workStatusCountList === 'object') {
            this.workStatusCountList.ALL_WORK = 0;
            this.workStatusCountList.HANDLED_WORK = 0;
            this.workStatusCountList.forEach(item => {
              switch (item.workStatus) {
                case this.anyfixService.TO_DISTRIBUTE:
                  this.workStatusCountList.TO_DISTRIBUTE = item.workNumber;
                  break;
                case this.anyfixService.TO_HANDLE:
                  this.workStatusCountList.TO_HANDLE = item.workNumber;
                  break;
                case this.anyfixService.TO_ASSIGN:
                  this.workStatusCountList.TO_ASSIGN = item.workNumber;
                  break;
                case this.anyfixService.TO_CLAIM:
                  this.workStatusCountList.TO_CLAIM = item.workNumber;
                  break;
                case this.anyfixService.TO_SIGN:
                  this.workStatusCountList.TO_SIGN = item.workNumber;
                  break;
                case this.anyfixService.TO_SERVICE:
                  this.workStatusCountList.TO_SERVICE = item.workNumber;
                  break;
                case this.anyfixService.IN_SERVICE:
                  this.workStatusCountList.IN_SERVICE = item.workNumber;
                  break;
                case this.anyfixService.TO_EVALUATE:
                  this.workStatusCountList.TO_EVALUATE = item.workNumber;
                  break;
                case this.anyfixService.RETURNED:
                  this.workStatusCountList.RETURNED = item.workNumber;
                  break;
              }
              this.workStatusCountList.ALL_WORK += Number.parseInt(item.workNumber, 10);
              if (this.anyfixService.handledWorkStatusList.indexOf(item.workStatus) > -1) {
                this.workStatusCountList.HANDLED_WORK += Number.parseInt(item.workNumber, 10);
              }
            });
          }
        }
      });
  }

  areaChange(event) {
    if (event === null) {
      this.searchForm.controls.district.setValue(null);
      return;
    }
    if (event !== undefined){
      if(event.length > 2) {
        this.searchForm.controls.district.setValue(event[2]);
      }else if(event.length > 1) {
        this.searchForm.controls.district.setValue(event[1]);
      } else if(event.length === 1) {
        this.searchForm.controls.district.setValue(event[0]);
      }
    }
  }

  showWorkStatusCount(workStatusCount) {
    return workStatusCount > 0 ? workStatusCount : 0;
  }

  /**
   * 客服建单
   */
  addWork() {
    const modal = this.nzModalService.create({
      nzTitle: '新建工单',
      nzContent: WorkAddComponent,
      nzFooter: null,
      nzMaskClosable: false,
      nzWidth: '1000px',
      nzStyle:{'margin-top': '-40px'}
    });
    modal.afterClose.subscribe(result => {
      if (result === 'update') {
        this.queryWork();
      }
    });
  }

  changeColorBig(workStatus){
    if(workStatus === 0) {
      return this.allChecked ? {color: '#20BF8E'} : {color: '#444444'};
    } else {
      return workStatus === this.workStatusNow ? {color: '#20BF8E'} : {color: '#444444'};
    }
  }

  changeColorSmall(workStatus) {
    return workStatus === this.workStatusNow ? {color: '#20BF8E'} : {color: '#999999'};
  }
  
  importWork(){
    const modal = this.nzModalService.create({
      nzTitle: '导入工单',
      nzContent: ImportWorkComponent,
      nzFooter: null,
      nzMaskClosable: false,
      nzWidth: '800px',
      nzStyle:{'margin-top': '-40px'}
    });
    modal.afterClose.subscribe(result => {
      if (result === 'update') {
        this.queryWork();
      }
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
  
  exportWork() {
    const param = this.getParams();
    const page = new Page(1, 99999);
    this.loading = true;
    this.httpClient
    .post('/api/anyfix/work-request/query', {...page, ...param})
    .pipe(
      finalize(() => {
        this.loading = false;
        this.cdf.markForCheck();
      })
    )
    .subscribe((res: any) => {
      if (res.data) {
        this.stPro.export(res.data.list, {filename:'工单查询'});
      }
    });
  }
  
  dbClick($event){
    console.log($event);
  }
  
  selectRow($event){
    console.log($event);
  }
}
