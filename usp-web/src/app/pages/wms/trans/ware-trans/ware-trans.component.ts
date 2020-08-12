import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';

import {finalize} from 'rxjs/operators';
import {UserService} from '@core/user/user.service';
import {Page} from '@core/interceptor/result';
import {ActivatedRoute, Router} from '@angular/router';
import {WareTransService} from '../ware-trans.service';

@Component({
  selector: 'app-branch-settle',
  templateUrl: './ware-trans.component.html',
  styleUrls: ['./ware-trans.component.less']
})
export class WareTransComponent implements OnInit {
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
  statusCountMap: {[key: number]: number} = {
    10:0,
    20:0,
    30:0,
    40:0,
    50:0,
    100:0,
    200:0
  };
  NOT_SUBMITTED = 0;
  WAITING_EXAMINE = 1;
  WAITING_DISPATCH = 2;
  DISPATCHED = 3;
  RECEIVED = 4;
  checkOptionsOne = [
    {label: '未审批', value: 10, checked: true},
    {label: '已审批', value: 20, checked: true},
    {label: '待调拨', value: 30, checked: true},
    {label: '已调拨', value: 40, checked: true},
    {label: '待发货', value: 50, checked: true},
    {label: '已发货', value: 60, checked: true},
    {label: '待发货', value: 70, checked: true},
    {label: '已发货', value: 80, checked: true},
  ];
  indeterminate = false;


  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private router: Router,
              public userService: UserService,
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
    this.queryList();
    this.queryStatusCount();
  }

  loadList(): void {

  }

  initWorkStatusCheckBox() {

  }


  changeTag(workStatus) {
    this.workStatusNow = workStatus;
    this.queryList();
  }

  queryStatusCount(){
    const params = this.getParams();
    console.log('params',params);

    this.wareTransService.queryStatusCount(params,this.statusCountMap).subscribe((res: any) => {

    });
  }

  getPageParam() {

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
    this.router.navigate(['../ware-trans-detail'],
      {queryParams: {id}, relativeTo: this.activatedRoute});
  }

  toDispatch(id) {
    this.router.navigate(['../ware-trans-dispatch'],
      {queryParams: {id}, relativeTo: this.activatedRoute});
  }

  // 获取各个状态的工单数量
  getWorkStatusCount() {

  }

  /**
   * 客服建单
   */
  applyTrans() {
    this.router.navigate(['../ware-trans-apply'],
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

  changeColor(workStatus){
    return workStatus === this.workStatusNow ? {color: '#20BF8E'} : {color: '#999999'};
  }


  batchReceive() {
    this.router.navigate(['../to-be-received-list'],
      {queryParams: {}, relativeTo: this.activatedRoute});
  }

  confirmReceive() {
    this.router.navigate(['../ware-trans-receive'],
      {queryParams: {}, relativeTo: this.activatedRoute});
  }


  queryList( reset: boolean = false) {
    const params = this.getParams();
    if (reset) {
      this.page.pageNum = 1;
      params.pageNum = 1;
    }
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


  getParams() {
    const params = Object.assign({}, this.searchForm.value);
    switch (this.workStatusNow) {
      case this.wareTransService.COMMON_APPROVAL:
        params.flowNodeTypeList = [this.wareTransService.COMMON_APPROVAL];
        break;
      case this.wareTransService.DELIVERY:
        params.flowNodeTypeList = [this.wareTransService.DELIVERY];
        break;
      case this.wareTransService.RECEIVE:
        params.flowNodeTypeList = [this.wareTransService.RECEIVE];
        break;
      case this.wareTransService.END:
        params.transStatusList = [this.wareTransService.COMPLETE_ALLOCATION];
        break;
    }
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    params.createBy = this.userService.userInfo.userId;
    params.largeClassId = this.wareTransService.TRANS;
    params.smallClassId = this.wareTransService.TRANS_WARE_TRANSFER;
    return params;
  }
}
