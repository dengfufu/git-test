import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Page} from '@core/interceptor/result';

import {ActivatedRoute} from '@angular/router';

import {HttpClient} from '@angular/common/http';
import {DatePipe} from '@angular/common';
import {WmsService} from '../../../wms.service';
import {finalize} from 'rxjs/operators';
import {UserService} from '@core/user/user.service';
import {WareTransService} from '../../ware-trans.service';


@Component({
  selector: 'app-add-ware-income',
  templateUrl: './quick-transfer-confirm.component.html',
  styleUrls: [ './quick-transfer-confirm.component.less']
})
export class QuickTransferConfirmComponent implements OnInit {

  showList: any[] = [];
  searchForm: FormGroup;
  page = new Page();
  addForm: FormGroup;
  checkedList  = [];
  loading  = false;

  selectOptions: any = {
    applicantOptions: [
      {id: '76765754757', name: '内存条'},
      {id: '4678578576868', name: '显卡'},
      {id: '25656456456', name: '散热器'}
    ],
    depotOptions: [
      {id: '12341453452345', name: '库房1'},
      {id: '354645634564', name: '库房2'}
    ],
    priorityOptions: [
      {id: '325345245235', name: '紧急'},
      {id: '453456356', name: '十分紧急'}
    ],
    wareOptions: [
      {id: '325345245235', name: 'GTX 1080Ti'},
      {id: '453456356', name: '天耀2330'}
    ],
    modelOptions: [
      {id: '325345245235', name: 'BBMMP'},
      {id: '453456356', name: '天耀2330'}
    ],
    statusList: [
      {id: 10, name: '全新'},
      {id: 20, name: '待修'},
      {id: 30, name: '修复'},
      {id: 40, name: '报废'}
    ],
    normsList: [
      {attribute: '内存', value: ['2G', '4G','8G','16G','32G']},
      {attribute: '插口', value: []},
    ]
  };

  isAllDisplayDataChecked = false;
  isIndeterminate = false;
  mapOfCheckedId : { [key: string]: boolean } = {};
  numberOfChecked = 0;
  fromDepotSelectionOptions = [];


  constructor(
    private modalService: NzModalService,
    private formBuilder: FormBuilder,
    private cdf: ChangeDetectorRef,
    private activatedRoute: ActivatedRoute,
    private httpClient: HttpClient,
    private messageService: NzMessageService,
    private datePipe: DatePipe,
    public wmsService: WmsService,
    public wareTransService: WareTransService,
    private userService: UserService
  ) {
    this.searchForm = this.formBuilder.group({
      fromDepotId: ['', Validators.required],
      catalog: ['', Validators.required],
      num: ['', Validators.required],
    });

    this.addForm = new FormGroup({});
    this.addForm = this.formBuilder.group({
      applyDepotId: ['', Validators.required],
      applicant: ['', Validators.required],
      applyDate: ['', Validators.required],
      applyNote: ['', Validators.required],
      priorityLevel: ['', Validators.required],
      partInfoList:[[], Validators.required],
    });

  }

  ngOnInit() {
    // console.log(this.incomeMainService.incomeMain);
    this.initDetailList();
    this.queryDepotList('');
    this.wmsService.queryCatalogList('');
    this.queryList(false);
  }

  deleteDetail(index: number) {
    this.showList.splice(index,1);
  }

  addSubmit() {

  }

  saveDetail() {
    const params = {
      smallClassId: this.wareTransService.TRANS_WARE_SHIFT,
      idList: this.checkedList
    };

    this.httpClient.post('/api/wms/trans-ware-common/batchAudit', params)
      .subscribe((res: any) => {
        this.messageService.success('快速转库确认成功');
        this.goBack();
      })
  }

  goBack() {
    history.back();
  }

  initDetailList() {
  }


  checkReceiveAll(value: boolean) {
    this.showList.filter(item => !item.disabled).forEach(item => (this.mapOfCheckedId[item.id] = value));
    this.refreshStatus();
  }

  // 查询库房下拉列表，加上分页仅为限制条数，下同
  queryDepotList(event) {
    this.httpClient.post('/api/wms/ware-depot/query', {name: event, pageNum: 1, pageSize: 100})
      .subscribe((res: any) => {
        this.fromDepotSelectionOptions = res.data.list;
      })
  }


  queryList( reset: boolean = false) {
    const params = this.getParams();
    if (reset) {
      this.page.pageNum = 1;
      params.pageNum = 1;
    }
    params.createBy = this.userService.userInfo.userId;
    params.largeClassId = this.wareTransService.TRANS;
    params.smallClassId = this.wareTransService.TRANS_WARE_SHIFT;
    params.transStatusList = [
      this.wareTransService.FOR_ALLOCATION
    ];
    this.loading = true;
    this.httpClient
      .post('/api/wms/trans-ware-common/queryTrans',params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        if(res.data != null){
          this.showList = res.data.list;
          console.log('this.showList',this.showList);
          this.page.total = res.data.total;
        }
      });
  }


  refreshStatus() {
    this.isAllDisplayDataChecked = this.showList
      .filter(item => !item.disabled)
      .every(item => this.mapOfCheckedId[item.id]);
    this.isIndeterminate =
      this.showList.filter(item => !item.disabled).some(item => this.mapOfCheckedId[item.id]) &&
      !this.isAllDisplayDataChecked;
    this.numberOfChecked = this.showList.filter(item => this.mapOfCheckedId[item.id]).length;
    this.setCheckedList();
  }

  setCheckedList() {
    this.checkedList = [];
    Object.keys(this.mapOfCheckedId).forEach(key => {
      if(this.mapOfCheckedId[key]){
        for(const item of this.showList ){
          if(item.id === key ){
            this.checkedList.push(item.id);
            break;
          }
        }
      }
    });
  }

  resetChecked(){
    this.mapOfCheckedId = {};
    this.checkedList = [];
    this.isAllDisplayDataChecked = false;
    this.isIndeterminate = false;
  }

  outStoreChange() {
    this.resetChecked();
  }

  catalogChange($event: any) {

  }

  getParams() {
    const params = Object.assign({}, this.searchForm.value);
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    return params;
  }
}
