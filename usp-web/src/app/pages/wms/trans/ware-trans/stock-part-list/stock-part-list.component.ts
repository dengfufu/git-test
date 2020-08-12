import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';

import {NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {BaiduMapComponent} from '../../../../common/baidu-map/baidu-map.component';
import {Page} from '@core/interceptor/result';
import {WareTransService} from '../../ware-trans.service';

@Component({
  selector: 'app-application-config-add',
  templateUrl: 'stock-part-list.component.html'
})
export class StockPartListComponent implements OnInit {

  @Input() object;
  @Input() action;

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
              public wareTransService : WareTransService,
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
      // subBoxNum: ['', [Validators.required]]

    });
  }

  ngOnInit(): void {
    this.queryList();
  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin

  }

  destroyModal(): void {
    this.modal.destroy();
  }

  checkReceiveAll(value: boolean) {
    this.list.filter(item => !item.disabled).forEach(item => (this.mapOfDeliveryCheckedId[item.id] = value));
    this.refreshStatus();
  }

  queryList() {
    this.loading = true;
    this.httpClient.post('/api/wms/stock-common/pageBy',this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        console.log(res);
        this.list = res.data.list;
        console.log('this.list',this.list);
      });
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


  cancel() {
    this.modal.destroy();
  }


  chooseStore(data) {
    if(this.action === 'add') {
      this.modal.destroy(data);
    }
  }

  getParams() {
    const params = {
      largeClassId : this.wareTransService.TRANS,
      smallClassId : this.wareTransService.TRANS_WARE_TRANSFER,
      modelId: this.object.modelId,
      situation: 10
    };

    return params;
  }
}
