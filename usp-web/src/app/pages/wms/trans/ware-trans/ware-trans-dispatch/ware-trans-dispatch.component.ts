import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Page} from '@core/interceptor/result';

import {ActivatedRoute} from '@angular/router';

import {HttpClient} from '@angular/common/http';
import {DatePipe} from '@angular/common';
import {TransPartDetailAddComponent} from '../trans-part-detail-add/trans-part-detail-add.component';
import {TransPartDetailListComponent} from '../trans-part-detail-list/trans-part-detail-list.component';
import {finalize} from 'rxjs/operators';
import {WareTransService} from '../../ware-trans.service';
import {WmsService} from '../../../wms.service';
import {UserService} from '@core/user/user.service';
import {WareTransToDispatchListComponent} from '../ware-trans-to-dispatch-list/ware-trans-to-dispatch-list.component';

@Component({
  selector: 'app-add-ware-income',
  templateUrl: './ware-trans-dispatch.component.html',
  styleUrls: [ './ware-trans-dispatch.component.less']
})
export class WareTransDispatchComponent implements OnInit {

  showList: any[] = [];

  submitForm: FormGroup;
  page = new Page();
  detailListForm: FormGroup;
  isExpressType = false;
  nzOptions: any;
  userSelectionOptions : any;
  selectOptions: any = {
    expressTypeOptions: [
      {id: '76765754757', name: '内存条'},
      {id: '4678578576868', name: '显卡'},
      {id: '25656456456', name: '散热器'}
    ],
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
    transTypeOptions = [
      { label: '快递', value: 1 },
      { label: '自提', value: 2 },
      { label: '托运', value: 3 }
  ];
  id: any;
  isConsignType = false;


  constructor(
    private modalService: NzModalService,
    private formBuilder: FormBuilder,
    private cdf: ChangeDetectorRef,
    private activatedRoute: ActivatedRoute,
    private httpClient: HttpClient,
    private messageService: NzMessageService,
    public  wareTransService : WareTransService,
    public wmsService : WmsService,
    private userService: UserService,
    private datePipe: DatePipe

  ) {
    this.submitForm = new FormGroup({});
    this.submitForm = this.formBuilder.group({
      consignBy: ['', Validators.required],
      consignMobile: ['', Validators.required],
      receiveName: ['', Validators.required],
      receiverId: ['', Validators.required],
      receiverMobile: ['', Validators.required],
      receiveDistrict: ['', Validators.required],
      receiveAddress: ['', Validators.required],
      transportTypeId: ['', Validators.required],
      totalBoxNum: ['', Validators.required],
      note: ['', Validators.required],
      expressType : ['', Validators.required],
      expressCorpId : ['', Validators.required],
      consignTypeId : [''],
      expressNo:['', Validators.required],
      area:['', Validators.required],
    });

    this.detailListForm = new FormGroup({});
    this.detailListForm = this.formBuilder.group({
      applyDepotId: ['', Validators.required],
      applicant: ['', Validators.required],
      applyDate: ['', Validators.required],
      applyNote: ['', Validators.required],
      priorityLevel: ['', Validators.required],
      stockCommonDtoList:[[]],
    });

    this.activatedRoute.queryParams.subscribe(queryParams => {
      this.id = queryParams.id
    });
  }

  ngOnInit() {
    // console.log(this.incomeMainService.incomeMain);
    this.listAreaDto();
    this.initTransType();
    this.wmsService.queryUserList('');
    this.wmsService.queryExpressCorpList();
  }

  // 查询用户id和nickname下拉列表
  queryUserList(){
    this.httpClient
      .post('/api/uas/corp-user/user/list/' ,{corpId:this.userService.currentCorp.corpId})
      .subscribe((res: any) => {
        this.userSelectionOptions = res.data;
        if(typeof res.data === 'object' && res.data !== null){
          this.userSelectionOptions.userMap =  {};
          res.data.forEach(item => {
            this.userSelectionOptions.userMap[item.userId] = item;
          })
        }
      })
  }

  deleteDetail(index: number) {
    this.showList.splice(index,1);
  }

  addSubmit2() {

    this.httpClient.post('/api/wms/consign/add',this.getParams())
        .subscribe((res: any) => {
          if(res.code === 0){
            this.messageService.success('发货单提交成功');
            this.goBack();
          }
        });
  }

  getParams() {
    const params = Object.assign({}, this.submitForm.value);
    params.largeClassId = this.wareTransService.TRANS;
    params.smallClassId = this.wareTransService.TRANS_WARE_TRANSFER;
    params.transWareCommonDtoList = this.showList;
    return params;
  }

  initTransType() {
    this.submitForm.value.transType = 1;
    this.isExpressType = true;
  }

 goBack() {
    // history.back();
    console.log('goBack',this.getParams());
  }


  addTransPartDetail(data) {
    const modal = this.modalService.create({
      nzTitle: '选择调拨备件',
      nzContent: TransPartDetailAddComponent,
      nzFooter: null,
      nzWidth: 1000,
      nzComponentParams: {
        object: data
      }
    });
    modal.afterOpen.subscribe(() => console.log('[afterOpen] emitted!'));
    modal.afterClose.subscribe(result => {
      console.log('result',result);
      if(result === undefined){
        return;
      }
      data.stockCommonDtoList = result.stockCommonDtoList;
      console.log('data.stockCommonDtoList',result.stockCommonDtoList);
    });
  }

  addWareTrans() {
    // if(this.submitForm.controls.totalBoxNum.value === ''){
    //   this.messageService.warning('请输入总箱数');
    //   return;
    // }
    const modal = this.modalService.create({
      nzTitle: '选择发货列表备件',
      nzContent: WareTransToDispatchListComponent,
      nzFooter: null,
      nzWidth: 1200,
      nzComponentParams: {

      }
    });
    modal.afterOpen.subscribe(() => console.log('[afterOpen] emitted!'));
    modal.afterClose.subscribe(result => {
      if(result === undefined){
        return;
      }
      console.log('result,result',result);
      for(const item of result){
        item.stockCommonDtoList = [];
        this.showList = [...this.showList,item];
      }
    });
  }

  submit() {
    this.httpClient.post('/api/wms/consign/add',this.getParams())
      .subscribe((res: any) => {
        if(res.code === 0){
          this.messageService.success('发货单提交成功');
          this.goBack();
        }
      });
  }

  showTransPartDetailList(data) {
    const modal = this.modalService.create({
      nzTitle: '调拨备件详情',
      nzContent: TransPartDetailListComponent,
      nzFooter: null,
      nzWidth: 1000,
      nzComponentParams: {
        object: data.stockCommonDtoList
      }
    });
    modal.afterOpen.subscribe(() => console.log('[afterOpen] emitted!'));
    modal.afterClose.subscribe(result => {
      if(result === undefined){
        return;
      }
      console.log('result result', result);
      data.stockCommonDtoList = result;
    });
  }

  transTypeChange() {
    console.log('哈哈哈哈哈');
    if(this.submitForm.value.transportTypeId === 30) {
      this.submitForm.controls.expressType.setValidators(Validators.required);
      this.submitForm.controls.expressCorpId.setValidators(Validators.required);
      this.submitForm.controls.expressNo.setValidators(Validators.required);
      this.isExpressType = true;
    } else {
      this.isExpressType = false;
      this.submitForm.controls.expressType.setValidators(null);
      this.submitForm.controls.expressCorpId.setValidators(null);
      this.submitForm.controls.expressNo.setValidators(null);
    }
    if(this.submitForm.value.transportTypeId === 20){
      this.submitForm.controls.consignTypeId.setValidators(Validators.required);
      this.isConsignType = true;
    } else {
      this.isConsignType = false;
      this.submitForm.controls.consignTypeId.setValidators(null);
    }
    this.submitForm.controls.expressType.updateValueAndValidity();
    this.submitForm.controls.expressType.markAsDirty();
    this.submitForm.controls.expressCorpId.updateValueAndValidity();
    this.submitForm.controls.expressCorpId.markAsDirty();
    this.submitForm.controls.expressNo.updateValueAndValidity();
    this.submitForm.controls.expressNo.markAsDirty();
    this.submitForm.controls.consignTypeId.updateValueAndValidity();
    this.submitForm.controls.consignTypeId.markAsDirty();
  }

  listAreaDto(): void {
    this.httpClient
      .get('/api/uas/area/list')
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.nzOptions = res.data;
      });
  }

  // 根据选择的userid获取对应的mobile
  changeMobile(userId){
    const userInfo = this.wmsService.selectOptions.userMap[userId];
    this.submitForm.controls.consignMobile.setValue(userInfo.mobile);
  }


  // 收货人
  changeReceiveInfo(userId){
    this.wmsService.selectOptions.userList.forEach(item => {
      if(item.userId === userId){
        this.submitForm.controls.receiveName.setValue(item.userName);
        this.submitForm.controls.receiverId.setValue(item.userId);
      }
    });
    if(userId > 0){
      const userInfo = this.wmsService.selectOptions.userMap[userId];
      this.submitForm.controls.receiverMobile.setValue(userInfo.mobile);
    }
  }

  areaChange(event) {
    if (event === null) {
      return;
    }
    if (event !== undefined && event.length && event.length > 2) {
      this.submitForm.controls.receiveDistrict.setValue(event[2]);
    }
  }
}
