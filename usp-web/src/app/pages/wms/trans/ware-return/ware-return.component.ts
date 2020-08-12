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
  templateUrl: './ware-return.component.html',
  styleUrls: ['./ware-return.component.less']
})
export class WareReturnComponent implements OnInit {
  searchForm: FormGroup;
  showMore = false;
  workList: any;
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

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private router: Router,
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
  }

  loadList(): void {

  }

  initWorkStatusCheckBox() {

  }


  queryWork(reset: boolean = false) {

  }

  getAllWork() {

  }

  changeTag(workStatus) {
    this.workStatusNow = workStatus;
    // if( workStatus === this.wareTransService.WAITING_RECEIVE){
    //   this.router.navigate(['../ware-return-confirm'],
    //     {queryParams: {}, relativeTo: this.activatedRoute});
    // }

  }

  getParams() {

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

  toBeReceivedList() {
    this.router.navigate(['../to-be-received-list'],
      {queryParams: {}, relativeTo: this.activatedRoute});
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

  toWorkDetail(id,examinedStatus) {
    if(this.workStatusNow === 30) {
      this.router.navigate(['../ware-return-confirm'],
        {queryParams: {id,examinedStatus}, relativeTo: this.activatedRoute});
      return;
    }
    this.router.navigate(['../ware-return-detail'],
      {queryParams: {id,examinedStatus}, relativeTo: this.activatedRoute});
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
    this.router.navigate(['../ware-return-apply'],
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
    const tempList =  [
      {
        id:'123',
        examineStatus: 0,
        applyNo: 123123123,
        statusName:'未提交',
        depotName: '深圳库房',
        applyUser:'雄杰',
        sparePart:'齿轮联动器',
        model:'NM1',
        count: 89,
        brandName:'莱卡',
        emergencyDegree:'非常紧急',
        applyDate: '2019-12-12 08:31:21',
        buttonColor:{}, dispatchNo:123
      },
      {
        id:'124',
        examineStatus: 1,
        applyNo: 123123123,
        statusName:'待审批',
        depotName: '深圳库房',
        applyUser:'雄杰',
        sparePart:'齿轮联动器',
        model:'NM1',
        count: 89,
        brandName:'莱卡',
        emergencyDegree:'非常紧急',
        applyDate: '2019-12-12 08:31:21',
        buttonColor:{}, dispatchNo:123
      },
      {
        id:'125',
        examineStatus: 2,
        applyNo: 123123123,
        depotName: '深圳库房',
        statusName:'待发货',
        applyUser:'雄杰',
        sparePart:'齿轮联动器',
        model:'NM1',
        count: 89,
        brandName:'莱卡',
        emergencyDegree:'非常紧急',
        applyDate: '2019-12-12 08:31:21',
        buttonColor:{}, dispatchNo:123
      },
      {
        examineStatus: 3,
        applyNo: 123123123,
        depotName: '深圳库房',
        statusName:'已发货',
        applyUser:'雄杰',
        sparePart:'齿轮联动器',
        model:'NM1',
        count: 89,
        emergencyDegree:'非常紧急',
        applyDate: '2019-12-12 08:31:21',
        buttonColor:{}, dispatchNo:123
      },
      {
        examineStatus: 4,
        applyNo: 123123123,
        depotName: '深圳库房',
        statusName:'已收货',
        applyUser:'雄杰',
        sparePart:'齿轮联动器',
        model:'NM1',
        count: 89,
        emergencyDegree:'非常紧急',
        applyDate: '2019-12-12 08:31:21',
        buttonColor:{}, dispatchNo:123
      },
    ];

    tempList.forEach(item => {
      switch (item.examineStatus) {
        case this.NOT_SUBMITTED:
          item.buttonColor = {'background-color': '#5E86C8'};
          break;
        case this.WAITING_EXAMINE:
          item.buttonColor = {'background-color': '#FFD617'};
          break;
        case this.WAITING_DISPATCH:
          item.buttonColor = {'background-color': '#5EC894'};
          break;
        case this.DISPATCHED:
          item.buttonColor = {'background-color': '#7ED321'};
          break;
        case this.RECEIVED:
          item.buttonColor = {'background-color': '#48B8FF'};
          break;
      }
    });
    this.workList = tempList;
    console.log('tempList', tempList);
  }


  confirm() {
    this.router.navigate(['../return-to-be-received-list'],
      {queryParams: {}, relativeTo: this.activatedRoute});
  }

  clickDispatchItem() {
  }

  dispatch() {
    this.router.navigate(['../ware-return-apply'],
      {queryParams: {}, relativeTo: this.activatedRoute});
  }
}
