import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Page} from '@core/interceptor/result';

import {ActivatedRoute} from '@angular/router';

import {HttpClient} from '@angular/common/http';
import {WmsService} from '../../../wms.service';
import {filter, finalize} from 'rxjs/operators';
import {DatePipe} from '@angular/common';
import {ConsignPartAddComponent} from '../../../outcome/ware-outcome-consign/consign-part-add/consign-part-add.component';
import {QuickTransferPartAddComponent} from '../quick-transfer-part-add/quick-transfer-part-add.component';
import {WareTransService} from '../../ware-trans.service';

@Component({
  selector: 'app-add-ware-income',
  templateUrl: './quick-transfer-apply.component.html',
  styleUrls: ['./quick-transfer-apply.component.less']
})
export class QuickTransferApplyComponent implements OnInit {

  type = '';

  showList: any[] = [];
  partList: any[] = [];
  normsValue: any = {};

  addDetailForm: FormGroup;
  page = new Page();
  submitForm: FormGroup;
  priorityOptions: any;
  wareOptions: any;
  fromDepotSelectionOptions = [];
  toDepotSelectionOptions = [];
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
    normsList: [
      {attribute: '内存', value: ['2G', '4G','8G','16G','32G']},
      {attribute: '插口', value: []},
    ]
  };
  fromDepotId: any;
  toDepotId: any;
  flag:any = {
    edit: 0,
  };
  total: any;

  constructor(
    private modalService: NzModalService,
    private formBuilder: FormBuilder,
    private cdf: ChangeDetectorRef,
    private activatedRoute: ActivatedRoute,
    private httpClient: HttpClient,
    public wmsService: WmsService,
    private msg: NzMessageService,
    public wareTransService:WareTransService,
    private messageService: NzMessageService,
    private datePipe: DatePipe
  ) {
    this.addDetailForm = new FormGroup({});


    this.submitForm = new FormGroup({});
    this.submitForm = this.formBuilder.group({
      fromDepotId: [null, Validators.required],
      toDepotId: [null, Validators.required],
      applyDate: ['', Validators.required],
      transDetailCommonSaveDtoList:[[], Validators.required],
      largeClassId: [this.wareTransService.TRANS,Validators.required],
      transStatus:  [this.wareTransService.FOR_ALLOCATION, Validators.required],
      smallClassId: [this.wareTransService.TRANS_WARE_SHIFT,Validators.required],
    });
  }

  ngOnInit() {
    // console.log(this.incomeMainService.incomeMain);
    this.initSelectData();
  }

  goBack() {
    history.back();
  }

  initSelectData(){
    this.queryDepotList('','from');
    this.queryDepotList('','to');
  }

  // 查询库房下拉列表，加上分页仅为限制条数，下同
  queryDepotList(event, depot) {
    this.httpClient.post('/api/wms/ware-depot/query', {name: event, pageNum: 1, pageSize: 100})
      .subscribe((res: any) => {
        if(depot === 'from'){
          this.fromDepotSelectionOptions = res.data.list;
        } else {
          this.toDepotSelectionOptions = res.data.list;
        }
      })
  }

  // 查询备件列表
  submit() {
    if (this.submitForm.value.applyDate) {
      this.submitForm.value.applyDate = this.datePipe.transform(this.submitForm.value.applyDate, 'yyyy-MM-dd');
    }
    this.httpClient.post('/api/wms/trans-ware-common/add',this.submitForm.value)
      .pipe(
        finalize(() => {

        })
      )
      .subscribe((res: any) => {
        if(res.code === 0){
          this.messageService.success('申请转库成功');
          this.goBack();
        }
      });
  }

  clearDetailForm() {
    this.addDetailForm.reset();
    this.normsValue = {};
  }

  deleteDetail(index) {
    // this.tmpList.splice(index, 1);
    this.showList.splice(index, 1);
    this.partList.splice(index, 1);
    this.setPartInfoList();
  }

  saveDetail() {
    console.log('this.addFrom.value',this.submitForm.value);
  }

  setPartInfoList (){
    this.submitForm.controls.transDetailCommonSaveDtoList.setValue(this.partList);
  }

  addSubmit() {
    console.log('this.submitForm',this.submitForm.value);
  }

  addDetail() {
    console.log(this.addDetailForm.value);
    if(this.addDetailForm.valid === false){
      return;
    }
    const obj = {
      wareId:'',
      normsValue:'',
      modelId: '',
      num:'',
      brandId:'',
      statusId:''
    };
    if(this.addDetailForm.value.ware) {
      this.addDetailForm.value.wareId = this.addDetailForm.value.ware.id;
      this.addDetailForm.value.wareName = this.addDetailForm.value.ware.name;
      obj.wareId =  this.addDetailForm.value.ware.id;
    }
    if(this.addDetailForm.value.model) {
      this.addDetailForm.value.modelId = this.addDetailForm.value.model.id;
      this.addDetailForm.value.modelName = this.addDetailForm.value.model.name;
      obj.modelId =  this.addDetailForm.value.model.id;
    }
    if(this.addDetailForm.value.brand) {
      this.addDetailForm.value.brandId = this.addDetailForm.value.brand.id;
      this.addDetailForm.value.brandName = this.addDetailForm.value.brand.name;
      obj.brandId =  this.addDetailForm.value.model.id;
    }
    if(this.addDetailForm.value.status) {
      this.addDetailForm.value.statusId = this.addDetailForm.value.status.id;
      this.addDetailForm.value.statusName = this.addDetailForm.value.status.name;
      obj.statusId =  this.addDetailForm.value.status.id;
    }

    const normsName = Object.keys(this.normsValue).map(
      key => {
        const val = this.normsValue[key];
        return key + ':' + val;
      }
    ).join(',');

    this.addDetailForm.value.normsName = normsName;
    obj.normsValue = normsName;
    obj.num = this.addDetailForm.value.num;

    this.partList.push(obj);
    this.showList = [ ...this.showList, this.addDetailForm.value];
    console.log('this.item', this.showList);
    this.setPartInfoList();
  }

  addPartList() {
    if(typeof this.submitForm.controls.fromDepotId.value === 'undefined' || this.submitForm.controls.fromDepotId.value === null){
      this.messageService.warning('请选择库房');
      return;
    }
    const modal = this.modalService.create({
      nzTitle: '物料信息',
      nzWidth: 1300,
      nzFooter: null,
      nzContent: QuickTransferPartAddComponent,
      nzComponentParams: {
        partCheckedList: this.partList,
        depotId: this.submitForm.controls.fromDepotId.value
      }
    });
    modal.afterClose.subscribe(res => {
      if(typeof res === 'object' && res !== null){
        if(res.code === 0){
          const tmpList = [];
          // 添加原来的记录
          if(typeof this.partList === 'object' && this.partList !== null){
            this.partList.forEach(part => {
              if(res.data.every(item => item.id !== part.id)){
                tmpList.push(part);
              }
            });
          }
          // 添加新的记录
          res.data.forEach(item => {
            this.partList.forEach(part => {
              if(item.id === part.id){
                item.applyQuantity = item.applyQuantity + part.applyQuantity;
                item.actualQty = item.actualQty + part.applyQuantity;
              }
            });
            tmpList.push(item);
          });
          this.partList = tmpList;
          this.sortPartList();
          this.reduceNum();
          this.mergeRow();
          this.cdf.markForCheck();
          this.setPartInfoList();
        }
      }
    });
  }


  // 排序
  sortPartList(){
    // 排序排序规则为wareName > status > modelName > normsValue(排序执行时间越晚，优先级越高)
    this.partList.sort((a, b) =>
      a.normsValue < b.normsValue ? 1 : -1
    );
    this.partList.sort((a, b) =>
      a.modelName > b.modelName ? 1 : -1
    );
    this.partList.sort((a, b) =>
      a.statusName < b.statusName ? 1 : -1
    );
    this.partList.sort((a, b) =>
      a.catalogName > b.catalogName ? 1 : -1
    );
  }

  validFromDepotAndToDepot(depotChange) {
    if(this.submitForm.controls.fromDepotId.value === this.submitForm.controls.toDepotId.value
       && this.submitForm.controls.fromDepotId.value === null){
      return;
    }
    console.log('toDepotId',this.submitForm.value.toDepotId);
    if(this.submitForm.controls.fromDepotId.value ===this.submitForm.controls.toDepotId.value){
      if(depotChange === 'from'){
          console.log('我来置灰色');
          this.submitForm.controls.fromDepotId.setValue(null);
        } else {
        this.submitForm.controls.toDepotId.setValue(null);
      }
        this.msg.error('转出库房与转入库房不能相同');
        return;
    }
  }

  // 修改物料记录数量
  editPart(no) {
    this.flag.edit = no;
    if(no === 0){
      this.reduceNum();
    }
  }

  reduceNum(){
    if(typeof this.partList === 'object' && this.partList.length > 0){
      const totalList = [];
      this.partList.forEach(item => totalList.push(Number.parseInt(item.applyQuantity, 10)));
      this.total = totalList.reduce((pre,cur) =>{
          return pre + cur;
        }
      );
    }else {
      this.total = 0;
    }

  }


  mergeRow() {
    let first = true;
    let firstItem = this.partList[0];
    this.partList.forEach(item  => {
      if(first){
        firstItem.all = item.applyQuantity;
        firstItem.rowNum = 1;
        first = false;
      }else {
        if(firstItem.catalogId === item.catalogId && firstItem.status === item.status && firstItem.modelId === item.modelId &&
          firstItem.normsValue === item.normsValue){
          firstItem.all = Number.parseInt(item.applyQuantity, 10) + Number.parseInt(firstItem.all, 10);
          firstItem.rowNum++;
        }else {
          firstItem = item;
          firstItem.all = item.applyQuantity;
          firstItem.rowNum = 1;
        }
      }
    });
  }

  deletePart(id: any) {
    this.partList = this.partList.filter(item => item.id !== id);
    this.reduceNum();
    this.setPartInfoList();
  }

  dateChange() {

  }
}
