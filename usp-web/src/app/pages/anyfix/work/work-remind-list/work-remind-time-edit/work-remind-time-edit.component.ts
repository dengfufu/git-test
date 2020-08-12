import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {format} from 'date-fns';
import {ZonValidators} from '@util/zon-validators';

@Component({
  selector: 'app-work-remind-time-edit',
  templateUrl: './work-remind-time-edit.component.html'
})
export class WorkRemindTimeEditComponent implements OnInit {

  @Input() workId: any;
  @Input() remindType: any;
  @Input() remindTypeName: any;
  @Input() workRemindTime: any;
  // 表单
  form: FormGroup;
  // 加载中
  isLoading = false;
  remindDate: any;
  remindTime: any;

  constructor(
    private modalRef: NzModalRef,
    private httpClient: HttpClient,
    private formBuilder: FormBuilder,
    private messageService:NzMessageService
  ) {
    this.form = this.formBuilder.group({
      workId: [],
      remindType: [],
      remindTime: [],
      note: ['', [ZonValidators.required()]]
    })
  }

  ngOnInit() {
    if (this.workRemindTime) {
      this.remindDate = new Date(format(this.workRemindTime, 'YYYY-MM-DD'));
      this.remindTime = new Date(format(this.workRemindTime, 'YYYY-MM-DD HH:mm'));
    }
  }

  // 取消
  cancel() {
    this.modalRef.destroy('cancel');
  }

  submit() {
    this.formDataBuild();
    if (!this.remindDate && !this.remindTime) {
      this.messageService.error('请选择预警时间');
      return;
    }
    if (this.remindDate && !this.remindTime) {
      this.messageService.error('请选择预警时间');
      return;
    }
    if (!this.remindDate && this.remindTime) {
      this.messageService.error('请选择预警日期');
      return;
    }
    const params = this.form.value;
    params.workId = this.workId;
    params.remindType = this.remindType;
    if (this.form.valid) {
      this.httpClient.post('/api/anyfix/work-remind/modRemindTime', params)
        .subscribe((res: any) => {
          this.messageService.success('修改成功！');
          this.modalRef.destroy('submit');
        })
    }
  }

  formDataBuild(){
    // 组装预警时间
    if (this.remindDate && this.remindTime) {
      const hour = this.remindTime.getHours();
      const minute = this.remindTime.getMinutes();
      this.remindDate.setHours(hour, minute, 0, 0);
      this.form.patchValue({
        remindTime: this.remindDate
      });
    } else {
      this.form.patchValue({
        remindTime: null
      });
    }
  }

}
