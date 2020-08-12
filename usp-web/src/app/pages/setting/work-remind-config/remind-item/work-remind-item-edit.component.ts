import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {ZonValidators} from '@util/zon-validators';
import {Result} from '@core/interceptor/result';
import {HttpClient} from '@angular/common/http';


export class WorkRemindItem {
  detailId: string;
  remindType: any;
  remindTypeName: string;
  expireTimeMin: any;
  enabled: string;
};

@Component({
  selector: 'app-work-remind-item-edit',
  templateUrl: 'work-remind-item-edit.component.html'
})
export class WorkRemindItemEditComponent implements OnInit {

  @Input() object: WorkRemindItem;

  validateForm: FormGroup;
  remindTypeOptions: any[] = [];

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      detailId: [], // ID
      remindType: [], // 预警类型
      remindTypeName: [null, [ZonValidators.required('预警类型'), ZonValidators.maxLength(50, '预警类型')]], // 预警类型
      expireTimeMin: [], // 超时时间
      enabled: [true] // 是否可用
    });
  }

  ngOnInit(): void {
    this.loadRemindType();
    if(this.object){
      this.validateForm.patchValue({
        detailId: this.object.detailId,
        remindType: this.object.remindType,
        remindTypeName: this.object.remindTypeName,
        expireTimeMin: this.object.expireTimeMin,
        enabled: this.object.enabled === 'Y'
      });
    }
  }

  loadRemindType() {
    this.httpClient.post(`/api/anyfix/work-remind/listWorkRemindType`, null).subscribe((result: Result) => {
      if (result.code === 0) {
        this.remindTypeOptions = result.data;
      }
    });
  }

  setTypeName($event: Event) {
    for (const remindType of this.remindTypeOptions){
        if(remindType.id === $event){
          this.validateForm.patchValue({remindTypeName: remindType.name});
        }
    }
  }
}
