import {ChangeDetectorRef, Input, Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {DatePipe} from '@angular/common';
import {ZonValidators} from '@util/zon-validators';



@Component({
  selector: 'app-config-item-edit',
  templateUrl: 'config-item-edit.component.html'
})
export class ConfigItemEditComponent implements OnInit {

  @Input() data;
  @Input() isForDemander = false;
  @Input() id = '';
  validateForm: FormGroup;
  spinning = false;
  CORP = 2;  // 设置当前企业
  DEMANDER = 1;
  corpType = 1;
  corpId = this.userService.currentCorp.corpId;
  variable = '{{YYYYMMDD}}';
  curDate = '';
  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private datePipe: DatePipe,
              private userService: UserService,
              private httpClient: HttpClient) {

    this.validateForm = this.formBuilder.group({

    });
  }

  ngOnInit(): void {
    if(this.data.isForInput) {
      this.validateForm.addControl(this.data.itemName, new FormControl(this.data.itemValue));
      this.validateForm.controls[this.data.itemName]
        .setValidators([ZonValidators.notEmptyString(), ZonValidators.maxLength(50)]);
      this.setCurDay();
      return;
    }
    this.data.itemValue === '1'?
    this.validateForm.addControl(this.data.itemName, new FormControl(false))
      :  this.validateForm.addControl(this.data.itemName, new FormControl(true));
    this.corpType = this.isForDemander ? this.DEMANDER : this.CORP;
  }

  submitForm(): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    const params = this.convertToParams();
    this.spinning = true;
    const url = this.isForDemander ? 'demander' : 'corp';
    this.httpClient
      .post('/api/anyfix/service-config/'+ url +'/add',
        params)
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        this.modal.destroy('submit');
      });
  }

  destroyModal(): void {
    this.modal.destroy();
  }

  convertToParams() {
    let value = this.validateForm.value[this.data.itemName];
    if (!this.data.isForInput) {
      value =  value ? '2' : '1';
    } else {
      value = value.trim();
    }
    const obj = {
      id : this.id,
      itemId : this.data.itemId,
      itemValue : value,
      type: this.corpType
    };
    return obj;
  }

  setCurDay() {
    this.curDate =  this.datePipe.transform(new Date(),'yyyyMMdd')
  }

}
