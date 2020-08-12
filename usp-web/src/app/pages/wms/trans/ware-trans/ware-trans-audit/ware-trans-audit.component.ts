import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Page} from '@core/interceptor/result';

import {ActivatedRoute} from '@angular/router';

import {HttpClient} from '@angular/common/http';
import {DatePipe} from '@angular/common';
import {TransPartDetailAddComponent} from '../trans-part-detail-add/trans-part-detail-add.component';
import {TransPartDetailListComponent} from '../trans-part-detail-list/trans-part-detail-list.component';
import {StockPartListComponent} from '../stock-part-list/stock-part-list.component';
import {WmsService} from '../../../wms.service';
import {UserService} from '@core/user/user.service';
import {WareTransService} from '../../ware-trans.service';

@Component({
  selector: 'app-add-ware-income',
  templateUrl: './ware-trans-audit.component.html',
  styleUrls: [ './ware-trans-audit.component.less']
})
export class WareTransAuditComponent implements OnInit {

  showList: any[] = [];

  searchForm: FormGroup;
  page = new Page();
  addForm: FormGroup;
  radioValue: any;
  dtoList = [];
  isAllDisplayDataChecked = false;
  isIndeterminate = false;
  numberOfChecked = 0;
  mapOfDeliveryCheckedId: { [key: string]: boolean } = {};
  isBatchSelectVisible = false;
  checkedList = [];
  fromDepotSelectionOptions = [];
  selectOptions: any = {
    applicantOptions: [
      {value: '76765754757', name: '内存条'},
      {value: '4678578576868', name: '显卡'},
      {value: '25656456456', name: '散热器'}
    ],
    depotOptions: [
      {value: '12341453452345', name: '库房1'},
      {value: '354645634564', name: '库房2'}
    ],
    priorityOptions: [
      {value: '325345245235', name: '紧急'},
      {value: '453456356', name: '十分紧急'}
    ],
    wareOptions: [
      {value: '325345245235', name: 'GTX 1080Ti'},
      {value: '453456356', name: '天耀2330'}
    ],
    modelOptions: [
      {value: '325345245235', name: 'BBMMP'},
      {value: '453456356', name: '天耀2330'}
    ],
    statusList: [
      {value: 10, name: '全新'},
      {value: 20, name: '待修'},
      {value: 30, name: '修复'},
      {value: 40, name: '报废'}
    ],
    normsList: [
      {attribute: '内存', value: ['2G', '4G','8G','16G','32G']},
      {attribute: '插口', value: []},
    ]
  };
  repoOptions: [
    { label: '库房1', value: 1 },
    { label: '库房2', value: 2 },
    { label: '库房3', value: 3 }
  ];

  auditStatusOptions = [
    { label: '通过', value: 1 },
    { label: '不通过', value: 2 }
  ];

  repoStatusOptions = [
    { label: '库房1', value: 1 },
    { label: '库房2', value: 2 },
    { label: '库房3', value: 3 }
  ];

  storeOptions: [
    { label: '库房1', value: '1' },
    { label: '库房2', value: '2' },
    { label: '库房3', value: '3' }
  ];


  storeSelectedValue: any;

  constructor(
    private modalService: NzModalService,
    private formBuilder: FormBuilder,
    private cdf: ChangeDetectorRef,
    private activatedRoute: ActivatedRoute,
    private httpClient: HttpClient,
    private messageService: NzMessageService,
    private datePipe: DatePipe,
    public wmsService : WmsService,
    private userService: UserService,
    public wareTransService : WareTransService
  ) {
    this.searchForm = new FormGroup({});
    this.searchForm = this.formBuilder.group({
      toDepotId: ['', Validators.required],
      catalogId: [''],
      status: ['', Validators.required],
      doDescried: [''],
      dispatchList: ['', Validators.required],
    });
  }

  ngOnInit() {

    this.queryDepotList('');
    this.wmsService.queryCatalogList('');
  }

  deleteDetail(index: number) {
    this.showList.splice(index,1);
    this.dtoList.splice(index,1);
  }

  addSubmit() {

  }

  saveDetail() {

  }

  goBack() {
    // history.back();
    console.log('this.dtoList',this.dtoList);
  }

  addDispatchStoreHouse(data) {
    const modal = this.modalService.create({
      nzTitle: '库存备件列表',
      nzContent: StockPartListComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        object: data,
        action:'add'
      }
    });
    modal.afterOpen.subscribe(() => console.log('[afterOpen] emitted!'));
    modal.afterClose.subscribe(result => {
      if(result !== undefined){
        console.log(result);
        data.fromDepotName = result.depotName;
        console.log('data',data);
        data.fromDepotId = result.depotId;
      }
    });
  }

  checkStock() {
    const modal = this.modalService.create({
      nzTitle: '库存备件列表',
      nzContent: StockPartListComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
      }
    });
    modal.afterOpen.subscribe(() => console.log('[afterOpen] emitted!'));
    modal.afterClose.subscribe(result => {

    });
  }


  showTransPartDetailList(data) {
    const modal = this.modalService.create({
      nzTitle: '调拨备件详情',
      nzContent: TransPartDetailListComponent,
      nzFooter: null,
      nzWidth: 1000,
      nzComponentParams: {
        object: data.partInfoList
      }
    });
    modal.afterOpen.subscribe(() => console.log('[afterOpen] emitted!'));
    modal.afterClose.subscribe(result => {
      if(result === undefined){
        return;
      }
      console.log('result result', result);
      data.partInfoList = result;
    });
  }

  removeDispatchStoreHouse(data: any) {
    data.fromDepotId = 0;
  }

  checkReceiveAll(value: boolean) {
    this.showList.filter(item => !item.disabled).forEach(item => (this.mapOfDeliveryCheckedId[item.id] = value));
    this.refreshStatus();
  }

  refreshStatus() {
    console.log('mapOfDeliveryCheckedId', this.mapOfDeliveryCheckedId);
    this.isAllDisplayDataChecked = this.showList
      .filter(item => !item.disabled)
      .every(item => this.mapOfDeliveryCheckedId[item.id]);
    this.isIndeterminate =
      this.showList.filter(item => !item.disabled).some(item => this.mapOfDeliveryCheckedId[item.id]) &&
      !this.isAllDisplayDataChecked;
    this.numberOfChecked = this.showList.filter(item => this.mapOfDeliveryCheckedId[item.id]).length;
    this.setFormList();
  }

  setFormList() {
    this.checkedList = [];
    this.dtoList = [];
    Object.keys(this.mapOfDeliveryCheckedId).forEach(key => {
      console.log('mapOfCheckValue',this.mapOfDeliveryCheckedId[key]);
      if(this.mapOfDeliveryCheckedId[key]){
        for(let i=0; i<this.showList.length;i++){
          if(this.showList[i].id === key){
            this.checkedList.push(this.showList[i].id);
            this.dtoList.push(this.showList[i]);
            break;
          }
        }
      }
    });
    this.searchForm.controls.dispatchList.setValue(this.checkedList);
  }

  handleCancel() {
    this.isBatchSelectVisible = false;
  }

  handleOk() {
    Object.keys(this.mapOfDeliveryCheckedId).forEach(key => {
      console.log('key',key, 'value', this.mapOfDeliveryCheckedId[key]);
      if(this.mapOfDeliveryCheckedId[key]){
        for(const item of this.showList ){
          if(item.id === key ){
            item.fromDepotName = this.storeSelectedValue.label;
            break;
          }
        }
      }
    });
    this.isBatchSelectVisible = false;
  }

  showBatchSelect() {
    this.isBatchSelectVisible = true;
  }

  auditTypeChange() {
    console.log(this.searchForm.value);
    console.log('this.searchForm',this.searchForm);
    if(this.searchForm.value.status === 2){
      this.searchForm.controls.doDescried.setValidators(Validators.required);
    } else {
      this.searchForm.controls.doDescried.setValidators(null);
    }
    this.searchForm.controls.doDescried.updateValueAndValidity();
    this.searchForm.controls.doDescried.markAsDirty();
  }

  isListChecked() {
    for (const key in this.mapOfDeliveryCheckedId) {
      if(this.mapOfDeliveryCheckedId[key]){
        return false;
      }
    }
    return true;
  }

  submit() {
    console.log(this.searchForm.value);
    const params = {
      smallClassId: this.wareTransService.TRANS_WARE_TRANSFER,
      transConsignAuditDtoList: this.dtoList
    };

    this.httpClient.post('/api/wms/trans-ware-common/batchAudit', params)
      .subscribe((res: any) => {
        this.messageService.success('审批成功');
        this.goBack();
      })
  }

  queryDepotList(event) {
    this.httpClient.post('/api/wms/ware-depot/query', {name: event, pageNum: 1, pageSize: 100})
      .subscribe((res: any) => {
        this.fromDepotSelectionOptions = res.data.list;
        if(this.fromDepotSelectionOptions.length >0 ){
          this.searchForm.controls.toDepotId.setValue(this.fromDepotSelectionOptions[0].id);
          this.queryList();
        }
      })
  }

  queryList() {
    this.wareTransService.queryList(this.getParams()).subscribe((res: any) => {
      this.showList = res.data.list;
      for(const item of this.showList){
        const leftQuantity = item.applyQuantity - item.consignedQuantity;
        if(leftQuantity > item.depotActualQty){
          item.consignQuantity = item.depotActualQty
        } else {
          item.consignQuantity = leftQuantity;
        }
      }
      this.page.total = res.data.total;
      console.log('this.showList',this.showList);
    });
  }

  getParams() {
    console.log('this.searchForm.value', this.searchForm.value);
    const params = Object.assign({}, this.searchForm.value);
    console.log('params params',params);
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    params.createBy = this.userService.userInfo.userId;
    params.largeClassId = this.wareTransService.TRANS;
    params.smallClassId = this.wareTransService.TRANS_WARE_TRANSFER;
    params.flowNodeTypeList = [20];
    return params;
  }

  toDepotChange($event) {
    this.queryList();
  }


}
