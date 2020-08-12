import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';


import {Page} from '@core/interceptor/result';
import {ActivatedRoute, Router} from '@angular/router';


import {WareTransService} from '../ware-trans.service';
import {finalize} from 'rxjs/operators';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'app-branch-settle',
  templateUrl: './quick-transfer.component.html',
  styleUrls: ['./quick-transfer.component.less']
})
export class QuickTransferComponent implements OnInit {
  searchForm: FormGroup;
  showMore = false;
  list: any;
  page = new Page();
  loading = true;
  visible = false;
  allChecked = true;
  optionList: any = {};
  // 当前查询的工单状态 0=全部工单
  workStatusNow: any = 0;
  workStatusCountList: any = {};

  NOT_SUBMITTED = 0;
  WAITING_EXAMINE = 1;
  WAITING_DISPATCH = 2;
  DISPATCHED = 3;
  RECEIVED = 4;
  checkOptionsOne = [
    {label: '待审批', value: 10, checked: true},
    {label: '已入库', value: 20, checked: true}
  ];
  indeterminate = false;
  confirmStatus = false;
  statusCountMap: {[key: number]: number} = {
    60:0,
    100:0
  };
  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private router: Router,
              private userService: UserService,
              private nzModalService: NzModalService,
              private activatedRoute: ActivatedRoute,
              public wareTransService: WareTransService,
  ) {
    this.searchForm = this.formBuilder.group({
      workCode: ['', []],
      workTypes: ['', []],
      workStatuses: ['', []],
      customCorp: ['', []],
      smallClassId: ['', []],
      deviceModel: ['', []],
      deviceBrand: ['', []],
      contactName: ['', []],
      contactPhone: ['', []],
      createTime: ['', []],
      serial: ['', []],
      district: ['', []],
      area: ['', []],
      serviceModes: ['', []]
    });
  }

  ngOnInit(): void {
    this.loading = false;
    // this.initOptionList();
    // this.initWorkStatusCheckBox();
    // this.loadWorkList(this.getPageParam());
    this.initData();
    this.getWorkStatusCount();
    this.queryList(false);
    // this.getStatusCount();
    this.queryStatusCount();
  }

  loadList(): void {
    this.loading = true;
    this.httpClient
      .post('/api/wms/trans-ware-common/queryTrans',{})
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
          console.log('res,res,res,',res);
          this.list = res.data.list;
          this.page.total = res.data.total;
      });
  }


  queryList( reset: boolean = false) {
    const params = this.getParams();
    if (reset) {
      this.page.pageNum = 1;
      params.pageNum = 1;
    }
    console.log('params',params);
    this.loading = true;
    this.wareTransService.queryList(params).subscribe((res: any) => {
      this.loading = false;
      console.log('res,res',res);
      if(res.data != null){
        this.list = res.data.list;
        this.page.total = res.data.total;
      }
    });
  }

  queryStatusCount(){
    const params = this.getParams();
    console.log('params',params);

    this.wareTransService.queryStatusCount(params,this.statusCountMap).subscribe((res: any) => {

    });
  }

  initWorkStatusCheckBox() {

  }

  getAllWork() {

  }

  changeTag(workStatus) {
    this.workStatusNow = workStatus;
    if(workStatus === this.wareTransService.COMPLETE_ALLOCATION){
      // this.confirmStatus = true;
    } else {
      this.confirmStatus = false;
    }
    this.queryList();
  }


  getParams() {
    const params = Object.assign({}, this.searchForm.value);
    switch (this.workStatusNow) {
      case this.wareTransService.FOR_ALLOCATION:
        params.transStatusList = [this.wareTransService.FOR_ALLOCATION];
        break;
      case this.wareTransService.COMPLETE_ALLOCATION:
        params.transStatusList = [this.wareTransService.COMPLETE_ALLOCATION];
        break;
    }
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    params.createBy = this.userService.userInfo.userId;
    params.largeClassId = this.wareTransService.TRANS;
    params.smallClassId = this.wareTransService.TRANS_WARE_SHIFT;
    return params;
  }



  openDrawer() {
    this.visible = true;
  }

  closeDrawer() {
    this.visible = false;
  }

  clearDrawer() {
    this.searchForm.reset();
    this.initWorkStatusCheckBox();

  }

  updateSingleChecked() {
    if (this.checkOptionsOne.every(item => !item.checked)) {
      this.allChecked = false;
      this.indeterminate = false;
    } else if (this.checkOptionsOne.every(item => item.checked)) {
      this.allChecked = true;
      this.indeterminate = false;
    } else {
      this.indeterminate = true;
    }
  }

  updateAllChecked() {
    this.indeterminate = false;
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

  toWorkDetail(id) {
    this.router.navigate(['../quick-transfer-detail'],
      {queryParams: {id}, relativeTo: this.activatedRoute});
  }

  // 获取各个状态的工单数量
  getWorkStatusCount() {

  }

  showWorkStatusCount(workStatusCount) {
    return workStatusCount > 0 ? workStatusCount : 0;
  }

  /**
   * 申请快速转库
   */
  apply() {
    this.router.navigate(['../quick-transfer-apply'],
      {queryParams: {}, relativeTo: this.activatedRoute});
  }

  addBatchWork() {
    this.router.navigate(['../ware-trans-dispatch'],
      {queryParams: {}, relativeTo: this.activatedRoute});
  }

  audit() {
    this.router.navigate(['../ware-trans-audit'],
      {queryParams: {}, relativeTo: this.activatedRoute});
  }

  editWork(id) {
    this.router.navigate(['../ware-trans-edit'],
      {queryParams: {id}, relativeTo: this.activatedRoute});
  }

  changeColor(workStatus){
    return workStatus === this.workStatusNow ? {color: '#20BF8E'} : {color: '#999999'};
  }

  initData() {

  }

  getStatusCount(){
    const params = this.getParams();

    this.loading = true;
    this.httpClient
      .post('/api/wms/trans-ware-common/countByWareStatus',params)
      .pipe(
        finalize(() => {
        })
      )
      .subscribe((res: any) => {
        if(typeof res.data === 'object' && res.data !== null){
          console.log('length',res.data.length);
          if(Object.keys(res.data).length  ===0){
            return;
          }
          Object.keys(res.data).map(key => {
            if (res.data[key] && res.data[key].length > 0) {
              this.statusCountMap[key] = parseFloat(res.data[key]);
            }
          });
        }
        console.log('countMap' , this.statusCountMap);
      });
  }


  confirm() {
    this.router.navigate(['../quick-transfer-confirm'],
      {queryParams: {}, relativeTo: this.activatedRoute});
  }
}
