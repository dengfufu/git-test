import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
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
import {CustomListComponent} from '@shared/components/custom-list/custom-list.component';
import {WorkRemindTimeEditComponent} from './work-remind-time-edit/work-remind-time-edit.component';

@Component({
  selector: 'app-work-remind-list',
  templateUrl: 'work-remind-list.component.html',
  styleUrls: ['work-remind-list.component.less']
})
export class WorkRemindListComponent implements OnInit {
  aclRight = ANYFIX_RIGHT;

  searchForm: FormGroup;
  workList: any = [];
  page = new Page();
  loading = true;
  visible = false;
  allChecked = false;
  checkOptionsOne: any = [];
  optionList: any = {};
  // 当前查询的工单状态 0=全部工单
  workStatusNow: any = 0;
  workStatusCountList: any = {};
  params: any = {};
  branchOption : any = [];
  customTileList : any = [];
  showRowData : any = [];
  titleList : any = ['委托商','省','市','区','设备类型','出厂序列号','品牌','型号','设备规格','工单来源',
    '工单类型','创建人','创建时间','设备网点','服务商','协同工程师','外部协同人员',
    '预约时间','提单日期','完单日期','服务方式','服务请求','联系人','联系电话','详细地址','维保方式','故障现象',
    '接单时间','出发时间','到达时间'];
  widthList : any = [100,50,80,100,150,100,100,150,100,80,
    100,80,150,120,100,100,100,
    150,150,150,80,250,100,150,250,80,150,
    150,150,150];
  fieldList: any = ['demanderCorpName','provinceName','cityName','districtName','smallClassName',
    'serial','brandName','modelName','specificationName','sourceName',
    'workTypeName','creatorName','createTime','deviceBranchName',
    'serviceCorpName','togetherEngineers', 'helpNames',
    'bookTimeEnd','dispatchTime','endTime','serviceModeName','serviceRequest',
    'contactName','contactPhone','address','warrantyModeName','faultTypeName',
    'acceptTime','goTime','signTime'];
  customListKey = 'workCustomList';
  defaultCols = 13;
  totalWidth = 1300;
  currentRow = 0;
  // 需要导出的工单数量
  workNum: number;

  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private router: Router,
              public userService: UserService,
              private modalService: NzModalService,
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
      serviceModes: [],
      provinceCode:[],
      serviceBranches:[],
      remindTypes:[]
    });
  }

  ngOnInit(): void {
    this.initShowTitleAndColumn();
    this.initOptionList();
    this.initWorkStatusCheckBox();
    this.getBranchOption('');
    let params = this.anyfixService.getParams();
    if (params && params._type === 'work-list') {
      this.params = params;
      this.checkOptionsOne = params.checkOptionsOne;
      this.workStatusNow = params.workStatusNow;
      this.allChecked = params.allChecked;
      this.page.pageNum = params.pageNum;
      this.page.pageSize = params.pageSize;
      this.loadWorkList(params);
      params.workStatuses = this.getHandledWork();
      this.getWorkStatusCount(params);
      this.anyfixService.setParams(null);
    } else {
      this.loadWorkList(this.getPageParam());
      params = this.getParams();
      params.workStatuses = this.getAllStatus();
      this.getWorkStatusCount(params);
    }
  }

  getAllStatus(): string {
    return this.anyfixService.TO_DISTRIBUTE + ',' + this.anyfixService.TO_HANDLE + ',' +
    this.anyfixService.TO_ASSIGN + ',' + this.anyfixService.TO_CLAIM + ',' +
    this.anyfixService.TO_SIGN + ',' + this.anyfixService.TO_SERVICE + ',' +
    this.anyfixService.IN_SERVICE + ',' + this.anyfixService.RETURNED;
  }

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
    this.anyfixModuleService.getWorkRemindType().subscribe((res: any) => {
      this.optionList.workRemindTypeOption = res.data;
    });
  }

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
      .post('/api/anyfix/work-request/queryRemindWork', params)
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

  getParams() {
    const params = Object.assign({}, this.searchForm.value);
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
          } else {
            workStatuses = workStatuses + ',' + workStatus.value;
          }
        }
      });
    } else {
      this.workStatusNow = 0;
    }
    if (this.allChecked) {
      this.workStatusNow = 0;
    }
    params.workStatuses = workStatuses;
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

  updateSingleChecked() {
    if (typeof this.checkOptionsOne === 'object' && this.checkOptionsOne !== null) {
      // 异步更新
      if (this.checkOptionsOne.every(item => item.checked)) {
        Promise.resolve(null).then( ()=> {this.allChecked = true;});
      } else {
        Promise.resolve(null).then( ()=> {this.allChecked = false;});
      }
    }
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

  toWorkDetail(workId) {
    const params = this.getParams();
    this.params.workStatuses = params.workStatuses;
    this.params._type = 'work-list';
    this.params.checkOptionsOne = this.checkOptionsOne;
    this.params.workStatusNow = this.workStatusNow;
    this.params.allChecked = this.allChecked;
    this.params.pageNum = this.page.pageNum;
    this.params.pageSize = this.page.pageSize;
    this.params.remindType = params.remindType;
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

  clickRow(workId) {
    this.currentRow = workId;
  }

  rowColor(workId) {
    return workId === this.currentRow ? {'background-color': '#FFEFC4'} : null;
  }

  openCustomList() {
    let titleList =  JSON.stringify(this.customTileList)
    titleList = JSON.parse(titleList);
    const modal = this.modalService.create({
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
    this.totalWidth = 1300;
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

  // 编辑工单预警时间
  editWorkRemindTime(id, workRemindType, workRemindTypeName, remindTime) {
    const modal = this.modalService.create({
      nzTitle: '修改预警时间',
      nzContent: WorkRemindTimeEditComponent,
      nzComponentParams: {
        workId: id,
        remindType: workRemindType,
        remindTypeName: workRemindTypeName,
        workRemindTime: remindTime
      },
      nzFooter: null,
      nzWidth: 500
    });
    modal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.queryWork();
      }
    });
  }

}
