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
  templateUrl: 'ware-delivery-edit.component.html'
})
export class WareDeliveryEditComponent implements OnInit {

  @Input() object;

  validateForm: FormGroup;
  contactOptionList: any;
  contactSelectedValue: any;
  values: any[] | null = null;
  nzOptions: any;
  typeSelectValue: any;
  serviceCorp = this.userService.currentCorp.corpId;
  spinning = false;
  normsOptions: any;
  normsSelectedValue: any;
  propertySelectedValue: any;
  propertySelectOptions: any;
  modelSelectOptions: any;
  modelSelectedValue: any;
  statusOptions: any;
  propertyMap: Map<any,string>;
  statusSelectedValue: any;
  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private userService: UserService,
              private httpClient: HttpClient,
              private modalService: NzModalService) {
    this.validateForm = this.formBuilder.group({
      propertyNameCode: [''],
      norms: ['', [Validators.required]],
      model: ['', [Validators.required]],
      propertyId:['', [Validators.required]],
      barcode: ['', [Validators.required]],
      status: ['', [Validators.required]],
      count: ['', [Validators.required]],
      // subBoxNum: ['', [Validators.required]],
      expressNo: [''],
      companyName: [''],
      propertyName: [''],
      normsName: [''],
      statusName:[''],
      modelName:['']
    });
  }

  ngOnInit(): void {
    this.initSelectData();
    console.log('haha',this.object);
    this.propertySelectedValue  = this.object.propertyNameCode;
    this.normsSelectedValue = this.object.norms;
    this.modelSelectedValue = this.object.model;
    this.statusSelectedValue = this.object.status;

    this.validateForm.patchValue({
      propertyNameCode: this.object.propertyNameCode,
      norms: this.object.norms,
      model: this.object.model,
      propertyId: this.object.propertyId,
      barcode: this.object.barcode,
      status: this.object.status,
      count: this.object.count,
      subBoxNum: this.object.subBoxNum
    });

  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }

    this.validateForm.value.propertyName = this.getText(this.propertySelectedValue,this.propertySelectOptions);
    this.validateForm.value.statusName = this.getText(this.statusSelectedValue,this.statusOptions);
    this.validateForm.value.normsName = this.getText(this.normsSelectedValue,this.normsOptions);
    this.validateForm.value.modelName = this.getText(this.modelSelectedValue,this.modelSelectOptions);


    this.modal.destroy(this.validateForm.value);
  }

  destroyModal(): void {
    this.modal.destroy();
  }

  onChanges(values: any): void {
    if(values === null){
      return;
    }
    this.validateForm.controls.province.setValue(values[0]);
    this.validateForm.controls.city.setValue(values[1]);
    this.validateForm.controls.district.setValue(values[2]);
  }

  search(value: string): void {
    console.log(value);
  }

  getContactSelectValue(event) {
    if(event === undefined){
      return;
    }
    const payload = {
      corpId: event
    }
    this.httpClient
      .post('/api/uas/corp-user/listCorpUser',payload)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        console.log('res,res',res);
        const listOfOption: Array<{ value: string; text: string }> = [];
        res.data.list.forEach(item => {
          listOfOption.push({
            value: item.userId + ',' + item.mobile + ',' + item.userName,
            text: item.userName + '(' + item.mobile + ')'
          });
        });
        this.contactOptionList = listOfOption;
        this.contactSelectedValue = listOfOption[0].value;
      });
  }

  changeContact(event){
    if(event === undefined){
      return;
    }
    const temp = event.split(',');
    console.log(temp);
    this.validateForm.controls.contactId.setValue(temp[0]);
    this.validateForm.controls.contactPhone.setValue(temp[1]);
    this.validateForm.controls.contactName.setValue(temp[2]);
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


  listArea(){
    this.httpClient
      .get('/api/uas/area/list')
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        console.log('res,res',res);
        this.nzOptions = res.data;
      });
  }

  openMap() {
    const modal = this.modalService.create({
      nzTitle: '选择经纬度',
      nzContent: BaiduMapComponent,
      nzFooter: null,
      nzComponentParams: {
        point:
          {
            lon: this.validateForm.controls.lon.value,
            lat: this.validateForm.controls.lat.value
          }},
      nzWidth: 800,
    });
    modal.afterOpen.subscribe(() => console.log('[afterOpen] emitted!'));
    modal.afterClose.subscribe(result => {
      console.log(result);
      if(result){
        this.validateForm.controls.lon.setValue(result.mark.point.lng);
        this.validateForm.controls.lat.setValue(result.mark.point.lat);
        this.validateForm.controls.address.setValue(result.address);
      }
    });
  }

  changePropertyName($event: any) {
    console.log($event);
  }

  getText(value, obj:  Array<{value: any; text: string}>){
    let text = '';
    for(let i=0; i<obj.length; i++){
      const item = obj[i];
      console.log(item);
      if(item.value === value){
        text = item.text;
        break;
      }
    }
    return text;
  }
}
