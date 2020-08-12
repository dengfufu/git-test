import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';

import {NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {BaiduMapComponent} from '../../../../common/baidu-map/baidu-map.component';

@Component({
  selector: 'app-application-config-add',
  templateUrl: 'ware-delivery-info.component.html'
})
export class WareDeliveryInfoComponent implements OnInit {

  validateForm: FormGroup;
  spinning = false;

  expressCorpSelectedValue: any;
  expressCorpOptions: any;
  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private userService: UserService,
              private httpClient: HttpClient,
              private modalService: NzModalService) {
    this.validateForm = this.formBuilder.group({
      expressNo: ['', [Validators.required]],
      subBoxNum: ['', [Validators.required]],
      expressCorp: ['', [Validators.required]],
      expressCorpName:['']
    });
  }

  ngOnInit(): void {
    this.initSelectData();
  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }

    this.validateForm.value.expressCorpName = this.getText(this.expressCorpSelectedValue,this.expressCorpOptions);
    this.modal.destroy(this.validateForm.value);
  }

  destroyModal(): void {
    this.modal.destroy();
  }

  initSelectData(){
    this.expressCorpOptions = [
      {
        text:'圆通公司',
        value:'1'
      },
      {
        text:'高通公司',
        value:'2'
      }
    ];
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
