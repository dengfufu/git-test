import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Page} from '@core/interceptor/result';

import {ActivatedRoute, Router} from '@angular/router';

import {HttpClient} from '@angular/common/http';
import {DatePipe} from '@angular/common';


@Component({
  selector: 'to-be-received-list.component',
  templateUrl: './return-to-be-received-list.component.html',
  styleUrls: [ './return-to-be-received-list.component.less']
})
export class ReturnToBeReceivedListComponent implements OnInit {

  showList: any[] = [];

  addDetailForm: FormGroup;
  page = new Page();
  addForm: FormGroup;
  radioValue: any;

  isAllDisplayDataChecked = false;
  isIndeterminate = false;
  numberOfChecked = 0;
  mapOfDeliveryCheckedId: { [key: string]: boolean } = {};
  isBatchSelectVisible = false;

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
      {id: '453456356', name: '天耀2330'},
      {id: '453456356', name: '34'}
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


  storeOptions = [
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
    private router: Router,
    private activateRoute: ActivatedRoute,
  ) {
    this.addDetailForm = new FormGroup({});
    this.addDetailForm = this.formBuilder.group({
      ware: ['', Validators.required],
      model: ['', Validators.required],
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
  }

  deleteDetail(index: number) {
    this.showList.splice(index,1);
  }

  addSubmit() {

  }

  saveDetail() {

  }

  goBack() {
    history.back();
  }

  initDetailList() {
    const tempList = [
      {
        id:'1',
        applyNo:'12434',
        depotName:'库房122',
        applicantName:'林小小',
        partName:'备件122',
        brandName:'劳力士',
        modelName:'型号12',
        emergencyLevel:'十分紧急',
        applyDate:'2019-12-11 12:00:00',
        dispatchNum:12,
        dispatchQuantity:12,
        currentStock:12,
        examinedCount:12,
        applyCount:12,
        waitingExaminedCount:12,
        dispatchStoreName:''
      },
      {
        id:'2',
        applyNo:'12435',
        depotName:'库房122',
        applicantName:'林小小',
        partName:'备件122',
        brandName:'劳力士',
        modelName:'型号12',
        emergencyLevel:'十分紧急',
        applyDate:'2019-12-11 12:00:00',
        dispatchNum:12,
        dispatchQuantity:12,
        currentStock:12,
        examinedCount:12,
        applyCount:12,
        waitingExaminedCount:12,
        dispatchStoreName:''
      }
    ];
    for( const item of tempList){
      this.showList = [ ...this.showList, item];
    }
  }

  addTransPartDetail(data) {

  }

  addDispatchStoreHouse(data) {

  }

  checkStock() {

  }


  showTransPartDetailList(data) {

  }

  removeDispatchStoreHouse(data: any) {
    data.dispatchStoreName = '';
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
            item.dispatchStoreName = this.storeSelectedValue.label;
            break;
          }
        }
      }
    });
    this.isBatchSelectVisible = true;
  }

  showBatchSelect() {
    this.isBatchSelectVisible = true;
  }

  confirmReceive() {
    this.router.navigate(['../ware-return-confirm'], {queryParams: {}, relativeTo: this.activatedRoute});
  }
}
