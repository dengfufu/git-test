import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';

import {NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {BaiduMapComponent} from '../../../../common/baidu-map/baidu-map.component';
import {Page} from '@core/interceptor/result';

@Component({
  selector: 'app-application-config-add',
  templateUrl: 'trans-part-detail-list.component.html',
  styleUrls: [ 'trans-part-detail-list.component.less']
})
export class TransPartDetailListComponent implements OnInit {

  @Input() object;

  validateForm: FormGroup;
  values: any[] | null = null;
  nzOptions: any;
  spinning = false;


  statusSelectedValue: any;
  list =  [];
  isAllDisplayDataChecked = false;
  isIndeterminate = false;
  numberOfChecked = 0;
  mapOfDeliveryCheckedId: { [key: string]: boolean } = {};
  selectedDeliveryList = [];
  page = new Page();

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
    if(this.object !== undefined){
      this.list = this.object;
      console.log('this.list', this.list);
    }
  }

  destroyModal(): void {
    this.modal.destroy();
  }

  addDelivery() {
    this.modal.destroy(this.list);
  }



  cancel() {
    this.modal.destroy();
  }


  deleteDetail(index: number) {
    this.list.splice(index,1)
  }

  submitForm(value: any) {

  }
}
