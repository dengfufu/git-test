import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {NzModalService} from 'ng-zorro-antd';
import {AnyfixService} from '@core/service/anyfix.service';
import {AnyfixModuleService} from '@core/service/anyfix-module.service';
import {Page} from '@core/interceptor/result';
import {ANYFIX_RIGHT} from '@core/right/right';
import {UserService} from '@core/user/user.service';
import {ImportWorkComponent} from '../../import/import-work.component';
import {CustomListComponent} from '@shared/components/custom-list/custom-list.component';
import {WorkListFilterComponent} from './filter/work-list-filter.component';


@Component({
  selector: 'app-work-list',
  templateUrl: 'work-list.component.html',
  styleUrls: ['work-list.component.less']
})
export class WorkListComponent implements OnInit {
  aclRight = ANYFIX_RIGHT;

  searchForm: FormGroup;
  workList = [];
  page = new Page();
  loading = true;
  visible = false;
  // 当前查询的工单状态 0=全部工单
  workStatusNow: any = 0;
  workStatusCountList: any = {};
  attentionWorkCount = 0;
  supportWorkCount = 0;
  params: any = {};
  allChecked = false;
  checkOptionsOne: any = [];
  customTileList : any = [];
  showRowData : any = [];
  titleList : any = ['省','市','区','客户名称','设备类型','出厂序列号','品牌','型号','设备规格','工单来源',
    '工单类型','创建人','创建时间','委托商','设备网点','服务商','服务网点','工程师','协同工程师','外部协同人员',
    '预约时间','提单日期','完单日期','服务方式','服务请求','联系人','联系电话','详细地址','维保方式','故障现象',
    '接单时间','出发时间','到达时间'];
  widthList : any = [50,80,100,200,150,150,100,150,100,80,
    100,80,150,100,120,100,120,80,100,100,
    150,150,150,80,250,100,150,250,80,150,
    150,150,150];
  fieldList: any = ['provinceName','cityName','districtName','customCorpName','smallClassName',
    'serial','brandName','modelName','specificationName','sourceName',
    'workTypeName','creatorName','createTime','demanderCorpName','deviceBranchName',
    'serviceCorpName','serviceBranchName','engineerName','togetherEngineers', 'helpNames',
    'bookTimeEnd','dispatchTime','endTime','serviceModeName','serviceRequest',
    'contactName','contactPhone','address','warrantyModeName','faultTypeName',
    'acceptTime','goTime','signTime'];
  customListKey = 'workCustomList';
  defaultCols = 13;
  totalWidth = 500;
  currentRow = 0;
  // 需要导出的工单数量
  workNum: number;
  // 判断当前企业是否是委托商
  isDemanderCorp: boolean = this.userService.currentCorp.serviceDemander === 'Y' ? true : false;
  // 判断当前企业是否是服务商
  isServiceCorp: boolean = this.userService.currentCorp.serviceProvider === 'Y' ? true : false;

  @ViewChild(WorkListFilterComponent, {static: false}) workListFilterComponent: WorkListFilterComponent;

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
    this.searchForm = this.formBuilder.group({
      workCode: [],
      checkWorkCode: [],
      workTypes: [],
      workStatuses: [],
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
      serviceMode: [],
      provinceCode:[],
      serviceBranches:[],
      engineer:[],
      hasWorkChat:[],
      isReview:[]
    });
  }

  ngOnInit(): void {
    this.initShowTitleAndColumn();
    this.initWorkStatusCheckBox();
    let params = this.anyfixService.getParams();
    if (params && params._type === 'work-list') {
      this.params = params;
      this.checkOptionsOne = params.checkOptionsOne;
      this.workStatusNow = params.workStatusNow;
      this.allChecked = params.allChecked;
      this.page.pageNum = params.pageNum;
      this.page.pageSize = params.pageSize;
      params.isAttention = 'N';
      params.isSupport = 'N';
      if (params.workStatusNow === 2) {
        params.isAttention = 'Y';
      }
      if (params.workStatusNow === 3) {
        params.isSupport = 'Y';
      }
      this.loadWorkList(params);
      params.workStatuses = this.getAllStatus();
      this.getWorkStatusCount(params);
      this.anyfixService.setParams(null);
    } else {
      params = this.getParams();
      params.workStatuses = this.getAllStatus();
      this.loadWorkList(Object.assign(this.getPageParam(),params));
      this.getWorkStatusCount(params);
    }
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
    this.anyfixService.workStatusList.forEach(item => {
      // 也包含已退单
      if (item < this.anyfixService.CLOSED || item === this.anyfixService.RETURNED) {
        this.checkOptionsOne.push({label: statusMap[item], value: item, checked: true});
      }
    });
  }



  loadWorkList(params: any, reset: boolean = false): void {
    if (reset) {
      this.page.pageNum = 1;
      params.pageNum = 1;
    }
    this.loading = true;
    this.httpClient
      .post('/api/anyfix/work-request/query', params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const data = res.data;
        this.page.total = data.total;
        this.workList = data.list;
        this.workNum = data.total;
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
                item.buttonColor = {'background-color': '#65CEDC'}
            }
          });
        }
      });
  }

  queryWork(reset: boolean = false) {
    // 工单筛选数据收集
    this.workListFilterComponent.submit()

    const params = this.getParams();
    this.loadWorkList(params, reset);
    params.workStatuses = this.getAllStatus();
    this.getWorkStatusCount(params);
  }

  // 查询受理中工单
  getHandledWork(reset: boolean = false) {
    this.workStatusNow = 1;
    this.allChecked = false;
    this.checkOptionsOne.forEach(item => {
      this.anyfixService.handledWorkStatusList.indexOf(item.value) > -1 ? item.checked =true : item.checked =false;
    });
    const params = this.getParams();
    this.loadWorkList(params, reset);
    params.workStatuses = this.getAllStatus();
    this.getWorkStatusCount(params);
  }

  // 查询已关注工单
  getAttentionWork(reset: boolean = false) {
    this.workStatusNow = 2;
    this.allChecked = true;
    this.checkOptionsOne.forEach(item => {
      item.checked = true;
    });
    const params = this.getParams();
    params.isAttention = 'Y';
    this.loadWorkList(params, reset);
    /*params.workStatuses = this.getAllStatus();
    this.getWorkStatusCount(params);*/
  }

  // 查询已技术支持的工单
  getSupportWork(reset: boolean = false) {
    this.workStatusNow = 3;
    this.allChecked = true;
    this.checkOptionsOne.forEach(item => {
      item.checked = true;
    });
    const params = this.getParams();
    params.isSupport = 'Y';
    this.loadWorkList(params, reset);
  }

  getAllWork() {
    this.workStatusNow = 0;
    this.checkOptionsOne.forEach(item => {
      item.checked = true;
    });
    this.allChecked = true;
    const params = this.getParams();
    params.workStatuses = this.getAllStatus();
    this.loadWorkList(params , false);
  }

  changeTag(workStatus) {
    this.checkOptionsOne.forEach(item => {
      if (item.value === workStatus) {
        item.checked = true;
        this.workStatusNow = workStatus;
      } else {
        item.checked = false;
      }
    });
    this.allChecked = false;
    this.loadWorkList(this.getParams(), true);
  }

  // 收集工单筛选数据
  searchFormDataChange(searchFormData: {key: string}[]) {
    this.searchForm.patchValue(searchFormData)
  }

  getParams() {
    const params =Object.assign({}, this.searchForm.value);
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
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
    let workStatuses = '';
    let isFirst = true;
    // 拼接workStatuses字符串
    if (typeof params.workStatuses === 'object' && params.workStatuses !== null) {
      params.workStatuses.forEach(workStatus => {
        if (workStatus.checked) {
          if (isFirst) {
            workStatuses = workStatus.value;
            isFirst = false;
            // this.workStatusNow = workStatuses;
          } else {
            workStatuses = workStatuses + ',' + workStatus.value;
            // this.workStatusNow = 0;
          }
        }
      });
      if (workStatuses==='') workStatuses='10,20,30,40,50,60,70,100';
    } else {
      // this.workStatusNow = 0;
    }
    if (this.allChecked) {
      // this.workStatusNow = 0;
    }
    params.workStatuses = workStatuses;

    if (this.workStatusNow === 2) {
      params.isAttention = 'Y';
    }
    if (this.workStatusNow === 3) {
      params.isSupport = 'Y';
    }
    return params;
  }

  getPageParam() {
    const params: any = {};
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    params.workStatuses = this.getAllStatus();
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
    this.initWorkStatusCheckBox();
    this.loadWorkList(this.getPageParam());
    const params = this.getParams();
     params.workStatuses = this.getAllStatus();
    this.getWorkStatusCount(params);
  }


  toWorkDetail(workId) {
    const params = this.getParams();
    this.params.workStatuses = params.workStatuses;
    this.params._type = 'work-list';
    this.params.checkOptionsOne = this.checkOptionsOne;
    this.params.workStatusNow = this.workStatusNow;
    this.params.allChecked = this.allChecked;
    this.params.pageNum = this.page.pageNum;
    this.params.pageSize = this.page.pageSize;
    this.anyfixService.setParams(this.params);
    const workIds = this.workList.map((item) => {
      return item.workId;
    });
    this.anyfixService.setParamsOnce(workIds);
    this.router.navigate(['/anyfix/work-detail'], {queryParams: {workId}, relativeTo: this.activatedRoute});
  }

  // 获取各个状态的工单数量
  getWorkStatusCount(params) {
    params.isAttention = 'N';
    params.isSupport = 'N';
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

    // 查询已关注工单的数量
    params.isAttention = 'N';
    if(params.isAttention !== undefined){
      params.isAttention = 'Y';
      params.isSupport = 'N';
      this.httpClient.post('/api/anyfix/work-stat/status/count', params).pipe(
      ).subscribe((res: any) => {
        if (res.data !== null) {
          const list = res.data;
          this.attentionWorkCount = 0;
          list.forEach(item => {
            this.attentionWorkCount += Number.parseInt(item.workNumber, 10);
          });
        }
      });
    }

    // 查询已技术支持的工单的数量
    params.isSupport = 'N';
    if(params.isSupport !== undefined){
      params.isSupport = 'Y';
      params.isAttention = 'N';
      this.httpClient.post('/api/anyfix/work-stat/status/count', params).pipe(
      ).subscribe((res: any) => {
        if (res.data !== null) {
          const list = res.data;
          this.supportWorkCount = 0;
          list.forEach(item => {
            this.supportWorkCount += Number.parseInt(item.workNumber, 10);
          });
        }
      });
    }
  }

  showWorkStatusCount(workStatusCount) {
    return workStatusCount > 0 ? workStatusCount : 0;
  }

  /**
   * 客服建单
   */
  addWork() {
    this.router.navigate(['../work-list/add'],
      {queryParams:  {}, relativeTo: this.activatedRoute});
  }

  changeColorBig(workStatus){
    return workStatus === this.workStatusNow ? {color: '#20BF8E'} : {color: '#444444'};
  }

  changeColorSmall(workStatus) {
    return workStatus === this.workStatusNow ? {color: '#20BF8E'} : {color: '#999999'};
  }

  clickRow(workId) {
    this.currentRow = workId;
  }

  rowColor(workId) {
    return workId === this.currentRow ? {'background-color': '#FFEFC4'} : null;
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

  openCustomList() {
    let titleList =  JSON.stringify(this.customTileList)
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

  initShowTitleAndColumn(){
    let saved = false;
    const submitList =  localStorage.getItem(this.customListKey);
    if(submitList !== null && submitList !== undefined){
      this.customTileList =JSON.parse(submitList);
      if (this.customTileList.length === this.titleList.length) {
        saved = true;
        for(let i=0;i< this.customTileList.length;i++)  {
          this.customTileList[i].width = this.widthList[i];
          this.customTileList[i].title = this.titleList[i];
          if( this.customTileList[i].checked === false){
            this.showRowData[this.fieldList[i]] = false;
          }else if( this.customTileList[i].checked === true ){
            this.showRowData[this.fieldList[i]] = true;
            this.totalWidth += this.customTileList[i].width;
          }
        }
      }
    }
    if (!saved) {
      const objList = [];
      for(let i=0;i< this.titleList.length;i++)  {
        const obj = {
          title : this.titleList[i],
          checked : true,
          width: this.widthList[i]
        };
        if(i>=this.defaultCols){
          obj.checked = false;
          this.showRowData[this.fieldList[i]] = false;
        }else {
          this.showRowData[this.fieldList[i]] = true;
          this.totalWidth += this.widthList[i];
        }
        objList.push(obj);
        this.customTileList = objList;
      }
    }
  }

  resetShowTable(titleList) {
    this.resetTotalWidth();
    let width = 0;
    this.customTileList = titleList;
    for(let i=0;i<titleList.length;i++)  {
      if(titleList[i].checked) {
        this.showRowData[this.fieldList[i]] = true;
        // 重新调整滑动窗口大小
        width += titleList[i].width
      } else {
        this.showRowData[this.fieldList[i]] = false;
      }
    }
    this.totalWidth+=width;
  }

  resetTotalWidth() {
    this.totalWidth = 450;
  }

  addExpandList() {
    if(this.workStatusNow === this.anyfixService.TO_ASSIGN || this.workStatusNow === 1) {
      this.pushToList('服务网点',120,'serviceBranchName');
    }
    if( this.workStatusNow === 1){
      this.pushToList('工程师',80,'engineerName');
    }
    if(this.workStatusNow === 1 || this.workStatusNow === this.anyfixService.IN_SERVICE){
      this.pushToList('签到时间',150,'signTime');
    }
    if(this.workStatusNow === this.anyfixService.TO_CLAIM) {
      this.pushToList('待接单人员',120,'engineerNames');
    }
  }

  pushToList(title,width,fieldName) {
    this.titleList.push(title);
    this.widthList.push(width);
    this.fieldList.push(fieldName);
  }
}
