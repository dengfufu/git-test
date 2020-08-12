import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';

import {NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {BaiduMapComponent} from '../../../../common/baidu-map/baidu-map.component';

@Component({
  selector: 'app-application-config-add',
  templateUrl: 'ware-receive-add.component.html'
})
export class WareReceiveAddComponent implements OnInit {

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
  receiveDeliveryList =  [];
  isAllDisplayDataChecked = false;
  isIndeterminate = false;
  numberOfChecked = 0;
  mapOfDeliveryCheckedId: { [key: string]: boolean } = {};
  selectedDeliveryList = [];

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private userService: UserService,
              private httpClient: HttpClient,
              private modalService: NzModalService) {
    this.validateForm = this.formBuilder.group({
      propertyNameCode: [''],
      id:[''],
      norms: [''],
      model: [''],
      propertyId:[''],
      barcode: [''],
      status: [''],
      count: [''],
      receiveDate: [''],
      // subBoxNum: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.initSelectData();
    this.initReceiveDelivery();
  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin

  }

  destroyModal(): void {
    this.modal.destroy();
  }



  checkReceiveAll(value: boolean) {
    this.receiveDeliveryList.filter(item => !item.disabled).forEach(item => (this.mapOfDeliveryCheckedId[item.id] = value));
    this.refreshStatus();
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
      }
      ,{
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
      ,{
        id:'3',
        propertyName: '手机',
        normsName: '大',
        modelName: 'XXXMSJ',
        propertyId: '123442',
        barcode: '23423',
        statusName: '保内',
        count:'0',
        receiveDate:'2019-12-12 10:10:10'
      }
      ,{
        id:'4',
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
    this.receiveDeliveryList = tempList;
    console.log('receiveDeliveryList',this.receiveDeliveryList);
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
    this.isAllDisplayDataChecked = this.receiveDeliveryList
      .filter(item => !item.disabled)
      .every(item => this.mapOfDeliveryCheckedId[item.id]);
    this.isIndeterminate =
      this.receiveDeliveryList.filter(item => !item.disabled).some(item => this.mapOfDeliveryCheckedId[item.id]) &&
      !this.isAllDisplayDataChecked;
    this.numberOfChecked = this.receiveDeliveryList.filter(item => this.mapOfDeliveryCheckedId[item.id]).length;
  }

  addDelivery() {
    this.selectedDeliveryList  = [];
    this.getSelectedDeliveryList();
    this.modal.destroy(this.selectedDeliveryList);
  }

  getSelectedDeliveryList() {
    Object.keys(this.mapOfDeliveryCheckedId).forEach(key => {
      console.log('key',key, 'value', this.mapOfDeliveryCheckedId[key]);
      if(this.mapOfDeliveryCheckedId[key]){
        for(const item of this.receiveDeliveryList ){
          if(item.id === key){
            this.selectedDeliveryList.push(item);
          }
        }
      }
    });
  }

  cancel() {
    this.modal.destroy();
  }
}
