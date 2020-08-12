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
import {WareTransToDispatchListComponent} from '../ware-trans-to-dispatch-list/ware-trans-to-dispatch-list.component';

@Component({
  selector: 'ware-trans-dispatch-edit',
  templateUrl: './ware-trans-dispatch-edit.component.html',
  styleUrls: [ './ware-trans-dispatch-edit.component.less']
})
export class WareTransDispatchEditComponent implements OnInit {

  showList: any[] = [];

  submitForm: FormGroup;
  page = new Page();
  detailListForm: FormGroup;
  isExpressType = false;
  nzOptions: any;

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


  constructor(
    private modalService: NzModalService,
    private formBuilder: FormBuilder,
    private cdf: ChangeDetectorRef,
    private activatedRoute: ActivatedRoute,
    private httpClient: HttpClient,
    private messageService: NzMessageService,
    private datePipe: DatePipe
  ) {
    this.submitForm = new FormGroup({});
    this.submitForm = this.formBuilder.group({
      consignorId: ['', Validators.required],
      consignorContact: ['', Validators.required],
      receiverId: ['', Validators.required],
      receiverContact: ['', Validators.required],
      receiveArea: ['', Validators.required],
      receiveAddress: ['', Validators.required],
      transType: ['', Validators.required],
      totalBoxNum: ['', Validators.required],
      note: ['', Validators.required],
      detailList:[[]]
    });

    this.detailListForm = new FormGroup({});
    this.detailListForm = this.formBuilder.group({
      applyDepotId: ['', Validators.required],
      applicant: ['', Validators.required],
      applyDate: ['', Validators.required],
      applyNote: ['', Validators.required],
      priorityLevel: ['', Validators.required],
      partInfoList:[[]],
    });
  }

  ngOnInit() {
    // console.log(this.incomeMainService.incomeMain);
    this.listAreaDto();
    this.initTransType();
  }

  deleteDetail(index: number) {
    this.showList.splice(index,1);
  }

  addSubmit() {

  }

  saveDetail() {

  }

  initTransType() {

    this.submitForm.addControl('expressType', new FormControl('', Validators.required));
    this.submitForm.addControl('expressCompany', new FormControl('', Validators.required));
    this.submitForm.addControl('expressNo', new FormControl('', Validators.required));

    this.submitForm.value.transType = 1;
    this.isExpressType = true;
  }

  goBack() {
    // history.back();
    console.log('goBack',this.submitForm.value);
  }


  addTransPartDetail(data) {
    const modal = this.modalService.create({
      nzTitle: '添加调度备件详情',
      nzContent: TransPartDetailAddComponent,
      nzFooter: null,
      nzWidth: 800,
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
      data.partInfoList = result.partInfoList;
      console.log('data.partInfoList',result.partInfoList);
    });
  }

  addWareTrans() {
    const modal = this.modalService.create({
      nzTitle: '新添调度',
      nzContent: WareTransToDispatchListComponent,
      nzFooter: null,
      nzWidth: 1200,
      nzComponentParams: {
        object: this.showList
      }
    });
    modal.afterOpen.subscribe(() => console.log('[afterOpen] emitted!'));
    modal.afterClose.subscribe(result => {
      if(result === undefined){
        return;
      }
      console.log('result,result',result);
      for(const item of result){
        item.partInfoList = [];
        this.showList = [...this.showList,item];
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

  transTypeChange() {
    if(this.submitForm.value.transType === 1) {
      this.submitForm.addControl('expressType', new FormControl('', Validators.required));
      this.submitForm.addControl('expressCompany', new FormControl('', Validators.required));
      this.submitForm.addControl('expressNo', new FormControl('', Validators.required));
      this.isExpressType = true;
    } else {
      this.isExpressType = false;
      this.submitForm.removeControl('expressType');
      this.submitForm.removeControl('expressCompany');
      this.submitForm.removeControl('expressNo');
    }
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

}
