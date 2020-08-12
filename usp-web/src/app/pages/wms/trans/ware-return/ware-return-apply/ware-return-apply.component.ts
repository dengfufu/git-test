import {ChangeDetectorRef, Component, ElementRef, HostListener, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {environment} from '@env/environment';
import {ActivatedRoute, Router} from '@angular/router';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import { NzInputDirective } from 'ng-zorro-antd/input';
import {AnyfixService} from '@core/service/anyfix.service';

import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Page} from '@core/interceptor/result';
import {WareReturnPartAddComponent} from '../ware-return-part-add/ware-return-part-add.component';

@Component({
  selector: 'app-settle-branch-detail',
  templateUrl: './ware-return-apply.component.html',
  styleUrls: ['./ware-return-apply.component.less']
})
export class WareReturnApplyComponent implements OnInit {

  @ViewChild(NzInputDirective, { static: false, read: ElementRef }) inputElement: ElementRef;

  workId: any;

  loading = true;
  normsValue: any = {};

  isAllDisplayDataChecked = false;
  isIndeterminate = false;
  deliveryList = [];
  mapOfCheckedId: { [key: string]: boolean } = {};
  numberOfChecked = 0;
  examinedStatus = 1;
  submitForm: FormGroup;
  toBeRepairedList =  [];
  i = 0;

  addDetailForm: FormGroup;
  addForm : FormGroup;
  isExpressType = false;
  flowInstanceTraceList = [
    {id: '12141342', templateNoteName: '申请', completeHandlerName: '赵明星',
      completeTime: '2019-09-09 12:09:09', completeDescription: '完成', completed: 'Y'},
    {id: '254625646', templateNoteName: '审核', completeHandlerName: '胡志鹏',
      completeTime: '2019-09-10 13:12:00', completeDescription: '核对无误，准许入库', completed: 'Y'},
    {id: '54635456', templateNoteName: '发货', completeHandlerName: '大傻',
      completeTime: null, completeDescription: '', completed: 'N'},
    {id: '54635456', templateNoteName: '收货', completeHandlerName: '大傻',
      completeTime: null, completeDescription: '', completed: 'N'}
  ];

  selectOptions: any = {
    applicantOptions: [
      {id: '76765754757', name: '内存条'},
      {id: '4678578576868', name: '显卡'},
      {id: '25656456456', name: '散热器'}
    ],
    brandOptions: [
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
    statusOptions: [
      {id: 10, name: '全新'},
      {id: 20, name: '待修'},
      {id: 30, name: '修复'},
      {id: 40, name: '报废'}
    ],
    inStoreOptions: [
      {id: 10, name: '入库库房1'},
      {id: 20, name: '入库库房2'},
      {id: 30, name: '入库库房3'},
      {id: 40, name: '入库库房4'}
    ],
    transportTypeOptions: [
      {id: 10, name: '入库库房1'},
      {id: 20, name: '入库库房2'},
      {id: 30, name: '入库库房3'},
      {id: 40, name: '入库库房4'}
    ],
    outStoreOptions: [
      {id: 10, name: '出库库房1'},
      {id: 20, name: '出库库房2'},
      {id: 30, name: '出库库房3'},
      {id: 40, name: '出库库房4'}
    ],
    normsList: [
      {attribute: '内存', value: ['2G', '4G','8G','16G','32G']},
      {attribute: '插口', value: []},
    ]
  };
  applicationOptions = [
    {
      value:1,
      label:'夏琳'
    },
    {
      value:2,
      label:'小林'
    }
  ];
  returnDetailList = [];
  showList = [];
  page = new Page();
  nzOptions: any;
  transTypeOptions = [
    { label: '快递', value: 1 },
    { label: '自提', value: 2 },
    { label: '托运', value: 3 }
  ];
  constructor(private httpClient: HttpClient,
              public router: Router,
              private activatedRoute: ActivatedRoute,
              private modalService: NzModalService,
              private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private msg: NzMessageService,
              public anyfixService: AnyfixService) {
    this.activatedRoute.queryParams.subscribe(queryParams => {
      this.workId = queryParams.workId;
      this.examinedStatus = queryParams.examinedStatus;
      console.log('examinedStatus',this.examinedStatus);
    });
    this.submitForm = this.formBuilder.group({
      consignBy: ['', Validators.required],
      consignMobile: ['', Validators.required],
      consignDate: ['', Validators.required],
      description: [''],
      receiverId: ['', Validators.required],
      receiverMobile: ['', Validators.required],
      receiveAddress: ['', Validators.required],
      receiveDistrict: ['', Validators.required],
      expressCorpId: [''],
      expressNo: [''],
      transportTypeId: [''],
      totalBoxNum: ['', Validators.required],
      consignTypeId: ['', Validators.required],
      returnDetailList:[[]],

    });

    this.addForm = this.formBuilder.group({
      outStore: ['', Validators.required],
      inStore: ['', Validators.required],
      transDate: ['', Validators.required],
      transList:[[], Validators.required],
    });

    this.addDetailForm = this.formBuilder.group({
      ware: ['', Validators.required],
      model: ['', Validators.required],
      brand: ['', Validators.required],
      status: ['', Validators.required],
      inStore: ['', Validators.required],
      outStore: ['', Validators.required],
      uniqueCode: ['', Validators.required],
      quantity: ['', Validators.required],
    });
  }

  @HostListener('window:click', ['$event'])
  handleClick(e: MouseEvent): void {
  };

  ngOnInit() {
    // this.initBookTime();
    // this.query();
    // 审批
    this.initFormControl();
    console.log('this.examinedStatus Init,', this.examinedStatus);
    this.initReceiveDelivery();
    console.log('ngOnInit listOfData',this.deliveryList)
    this.listAreaDto();
  }


  initFormControl(){

  }



  initReceiveDelivery() {
    const tempList =[
      {
        id:'1',
        propertyName: '手机',
        normsName: '大',
        modelName: 'XXXMSJ',
        propertyId: '123442',
        barcode: '23423',
        statusName: '保内',
        count:'0',
        receiveDate:'2019-12-12 10:10:10'
      },
      {
        id:'2',
        propertyName: '手机',
        normsName: '大',
        modelName: 'XXXMSJ',
        propertyId: '123442',
        barcode: '23423',
        statusName: '保内',
        count:'0',
        receiveDate:'2019-12-12 10:10:10'
      }
    ];
    this.toBeRepairedList = tempList;
    console.log('toBeRepairedList',this.toBeRepairedList);
  }

  handlePreview = (url: any) => {

  };






  goBack(){
    // history.go(-1);
    console.log('this.valid', this.submitForm.value);
  }

  // 查询工单信息
  query(){

  }

  operate(label) {

  }

  addDelivery(data) {

  }

  editDeliveryInfo() {

  }

  clearDetailForm() {
    this.addDetailForm.reset();
    this.normsValue = {};
  }

  addDetail() {
    console.log('this.form.value', this.addDetailForm.value);
    if(this.addDetailForm.valid === false){
      return;
    }
    const paramsObj = {
      wareId:'',
      normsValue:'',
      modelId: '',
      brandId:'',
      quantity:'',
      statusId:'',
      inStoreId:'',
      outStoreId:'',
      uniqueCode:'',
    };
    const showObj = {
      wareName:'',
      brandName:'',
      modelName:'',
      normsName:'',
      inStoreName:'',
      outStoreName:'',
      quantity:'',
      uniqueCode:'',
      statusName:''
    };
    if(this.addDetailForm.value.ware) {
      showObj.wareName= this.addDetailForm.value.ware.name;
      paramsObj.wareId =  this.addDetailForm.value.ware.id;
    }
    if(this.addDetailForm.value.brand) {
      showObj.brandName= this.addDetailForm.value.brand.name;
      paramsObj.brandId =  this.addDetailForm.value.brand.id;
    }
    if(this.addDetailForm.value.model) {
      showObj.modelName = this.addDetailForm.value.model.name;
      paramsObj.modelId =  this.addDetailForm.value.model.id;
    }
    if(this.addDetailForm.value.status) {
      showObj.statusName = this.addDetailForm.value.status.name;
      paramsObj.statusId =  this.addDetailForm.value.status.id;
    }
    if(this.addDetailForm.value.inStore) {
      showObj.inStoreName = this.addDetailForm.value.inStore.name;
      paramsObj.inStoreId =  this.addDetailForm.value.inStore.id;
    }
    if(this.addDetailForm.value.outStore) {
      showObj.outStoreName = this.addDetailForm.value.outStore.name;
      paramsObj.outStoreId =  this.addDetailForm.value.outStore.id;
    }

    const normsName = Object.keys(this.normsValue).map(
      key => {
        const val = this.normsValue[key];
        return key + ':' + val;
      }
    ).join(',');
    paramsObj.normsValue = normsName;
    showObj.normsName = normsName;
    showObj.quantity = this.addDetailForm.value.quantity;
    paramsObj.quantity = this.addDetailForm.value.quantity;
    showObj.uniqueCode = this.addDetailForm.value.uniqueCode;
    paramsObj.uniqueCode = this.addDetailForm.value.uniqueCode;

    this.returnDetailList.push(paramsObj);
    this.showList = [ ...this.showList, showObj];
    this.setReturnDetailList();
    console.log('this.item', this.showList);
  }

  scanUniqueCode() {
    console.log('扫描唯一编码');
  }

  setReturnDetailList() {
    this.submitForm.controls.returnDetailList.setValue(this.returnDetailList);
  }

  deleteDetail(index) {
    this.showList.splice(index,1);
    this.returnDetailList.splice(index,1);
    console.log('this.returnDetailForm', this.submitForm.value);
    this.setReturnDetailList();
  }

  submit() {
    console.log(this.submitForm.value);
  }

  addSubmit() {

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

  transTypeChange() {
    if(this.submitForm.value.consignTypeId === 1) {
      this.submitForm.controls.expressCorpId.setValidators(Validators.required);
      this.submitForm.controls.expressNo.setValidators(Validators.required);
      this.submitForm.controls.consignTypeId.setValidators(Validators.required);
      this.isExpressType = true;
    } else {
      this.isExpressType = false;
      this.submitForm.controls.expressCorpId.setValidators(null);
      this.submitForm.controls.expressNo.setValidators(null);
      this.submitForm.controls.consignTypeId.setValidators(null);
    }

  }



  addPartList() {
    // if(typeof this.addDetailForm.controls.depotId.value === 'undefined' || this.addDetailForm.controls.depotId.value === null){
    //   this.messageService.warning('请选择库房');
    //   return;
    // }
    const modal = this.modalService.create({
      nzTitle: '物料信息',
      nzWidth: 1300,
      nzFooter: null,
      nzContent: WareReturnPartAddComponent,
      nzComponentParams: {

      }
    });
    modal.afterClose.subscribe(res => {

    });
  }
}
