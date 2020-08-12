import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';

import {NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {Page} from '@core/interceptor/result';
import {WmsService} from '../../../wms.service';
import {WareTransService} from '../../ware-trans.service';

@Component({
  selector: 'app-application-config-add',
  templateUrl: 'ware-trans-to-dispatch-list.component.html'
})
export class WareTransToDispatchListComponent implements OnInit {

  @Input() object;

  validateForm: FormGroup;
  values: any[] | null = null;
  nzOptions: any;
  spinning = false;
  normsOptions: any;
  normsSelectedValue: any;
  propertySelectedValue: any;
  propertySelectOptions: any;
  modelSelectOptions: any;
  modelSelectedValue: any;
  statusOptions: any;

  statusSelectedValue: any;
  isAllDisplayDataChecked = false;
  isIndeterminate = false;
  numberOfChecked = 0;
  mapOfCheckedId: { [key: string]: boolean } = {};
  selectedDeliveryList = [];
  loading = false;
  page = new Page();
  list: any[] = [];


  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private userService: UserService,
              private httpClient: HttpClient,
              private wareTransService : WareTransService,
              private modalService: NzModalService) {
    this.validateForm = this.formBuilder.group({
      propertyNameCode: [''],
      id:[''],
      norms: [''],
      modelId: [''],
      propertyId:[''],
      barcode: [''],
      status: [''],
      count: [''],
      receiveDate: [''],
      // subBoxNum: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.queryList();
  }

  queryList(reset: boolean = false  ) {
    const params = this.getParams();
    this.loading = true;
    if (reset) {
      this.page.pageNum = 1;
      params.pageNum = 1;
    }
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
    const params = Object.assign({}, this.validateForm.value);
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    params.largeClassId = this.wareTransService.TRANS;
    params.smallClassId = this.wareTransService.TRANS_WARE_TRANSFER;
    params.flowNodeTypeList = [this.wareTransService.DELIVERY];
    return params;
  }


  submitForm(value: any): void {

  }

  destroyModal(): void {
    this.modal.destroy(this.selectedDeliveryList);
  }

  checkReceiveAll(value: boolean) {
    this.list.filter(item => !item.disabled).forEach(item => (this.mapOfCheckedId[item.id] = value));
    this.refreshStatus();
  }

  initSelectData() {
    this.normsOptions = [
      {
        text:'大',
        value:'1'
      },
      {
        text:'中',
        value:'2'
      }
    ];

    this.propertySelectOptions = [
      {
        text:'电脑',
        value:'1'
      },
      {
        text:'硬件',
        value:'2'
      }
    ];
    this.modelSelectOptions = [
      {
        text:'BBPP',
        value:'1'
      },
      {
        text:'MMXX',
        value:'2'
      }
    ];

    this.statusOptions = [
      {
        text:'完好',
        value:'1'
      },
      {
        text:'受损',
        value:'2'
      }
    ];
  }

  getText(value, obj:  Array<{value: any; text: string}>){
    let text = '';
    for(const item of obj){
      if(item.value === value){
        text = item.text;
        break;
      }
      return text;
    }
  }

  refreshStatus() {
    console.log('mapOfCheckedId', this.mapOfCheckedId);
    this.isAllDisplayDataChecked = this.list
      .filter(item => !item.disabled)
      .every(item => this.mapOfCheckedId[item.id]);
    this.isIndeterminate =
      this.list.filter(item => !item.disabled).some(item => this.mapOfCheckedId[item.id]) &&
      !this.isAllDisplayDataChecked;
    this.numberOfChecked = this.list.filter(item => this.mapOfCheckedId[item.id]).length;
  }

  addDelivery() {
    this.selectedDeliveryList  = [];
    this.getSelectedDeliveryList();
    this.modal.destroy(this.selectedDeliveryList);
  }

  getSelectedDeliveryList() {
    Object.keys(this.mapOfCheckedId).forEach(key => {
      console.log('key',key, 'value', this.mapOfCheckedId[key]);
      if(this.mapOfCheckedId[key]){
        for(const item of this.list ){
          if(item.id === key && !item.disabled){
            this.selectedDeliveryList.push(item);
          }
        }
      }
    });
  }

  cancel() {
    this.modal.destroy();
  }

  resetDisabled() {
    for(const wareTrans of this.list){
      wareTrans.disabled = false;
    }
  }

  queryWork(b: boolean) {
  }
}
