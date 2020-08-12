import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';

import {NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {BaiduMapComponent} from '../../../../common/baidu-map/baidu-map.component';
import {WareTransService} from '../../ware-trans.service';
import {Page} from '@core/interceptor/result';

@Component({
  selector: 'app-application-config-add',
  templateUrl: 'trans-part-detail-add.component.html'
})
export class TransPartDetailAddComponent implements OnInit {

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
  list =  [];
  isAllDisplayDataChecked = false;
  isIndeterminate = false;
  numberOfChecked = 0;
  mapOfDeliveryCheckedId: { [key: string]: boolean } = {};
  selectedDeliveryList = [];
  loading = false;
  page = new Page();
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

    this.queryStock();

  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin

  }

  destroyModal(): void {
    this.modal.destroy();
  }


  queryStock() {
    this.loading = true;
    this.httpClient.post('/api/wms/stock-common/pageBy',this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        console.log('呀呀呀呀呀啊呀呀,',res);
        this.list = res.data.list;
        this.page.total = res.data.total;
        if(this.object.stockCommonDtoList != null){
          this.checkData(this.object.stockCommonDtoList);
        }

      });
  }

  checkReceiveAll(value: boolean) {
    this.list.filter(item => !item.disabled).forEach(item => (this.mapOfDeliveryCheckedId[item.id] = value));
    this.refreshStatus();
  }


  initSelectData(){
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
    console.log('mapOfDeliveryCheckedId', this.mapOfDeliveryCheckedId);
    this.isAllDisplayDataChecked = this.list
      .filter(item => !item.disabled)
      .every(item => this.mapOfDeliveryCheckedId[item.id]);
    this.isIndeterminate =
      this.list.filter(item => !item.disabled).some(item => this.mapOfDeliveryCheckedId[item.id]) &&
      !this.isAllDisplayDataChecked;
    this.numberOfChecked = this.list.filter(item => this.mapOfDeliveryCheckedId[item.id]).length;
  }

  addDelivery() {
    this.selectedDeliveryList  = [];
    this.getSelectedDeliveryList();
    console.log('selectedList',this.selectedDeliveryList);
    this.object.stockCommonDtoList = this.selectedDeliveryList;
    this.modal.destroy(this.object);
  }

  getSelectedDeliveryList() {
    Object.keys(this.mapOfDeliveryCheckedId).forEach(key => {
      console.log('key',key, 'value', this.mapOfDeliveryCheckedId[key]);
      if(this.mapOfDeliveryCheckedId[key]){
        for(const item of this.list ){
          if(item.id === key && !item.disabled){
            this.selectedDeliveryList.push(item);
          }
        }
      }
    });
  }

  cancel() {
    console.log(this.list);
    this.modal.destroy();
  }

  checkData(stockCommonDtoList) {
    console.log('stockCommonDtoList 我是个哈啊', stockCommonDtoList);
    for(const item of stockCommonDtoList ){
      this.mapOfDeliveryCheckedId[item.id] = true;
      for(const receiveDelivery of this.list){
        if(receiveDelivery.id === item.id) {
          receiveDelivery.subBoxNum = item.subBoxNum;
          receiveDelivery.quantity = item.quantity;
          break;
        }
      }
    }
    this.refreshStatus();
    this.cdf.markForCheck();
  }

  getParams() {
    const params = Object.assign({}, this.validateForm.value);
    params.largeClassId = this.wareTransService.TRANS;
    params.smallClassId = this.wareTransService.TRANS_WARE_TRANSFER;
    params.depotId = this.object.fromDepotId;
    params.modelId = this.object.modelId;
    params.brandId = this.object.brandId;
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;

    return params;
  }
}
