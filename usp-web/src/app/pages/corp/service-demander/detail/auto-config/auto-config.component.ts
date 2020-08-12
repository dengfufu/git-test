import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {ZonValidators} from '@util/zon-validators';
import {DemanderService} from '../../demander.service';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'app-auto-config',
  templateUrl: './auto-config.component.html',
  styleUrls: ['./auto-config.component.less']
})
export class AutoConfigComponent implements OnInit {

  @Input() demanderAutoConfig: any;
  @Input() demanderServiceId: string;
  // 表单
  form: FormGroup;

  submitLoading = false;

  constructor(
    public formBuilder: FormBuilder,
    public modalRef: NzModalRef,
    public demanderService: DemanderService,
    public httpClient: HttpClient,
    public messageService: NzMessageService
  ) {
    this.form = this.formBuilder.group({
      settleType: [null, [ZonValidators.required('结算方式')]],
      settleDay: [null, [ZonValidators.intOrZero('结算日期'),
        ZonValidators.min(1, '结算日期'), ZonValidators.max(31, '结算日期')]],
      autoVerify: [false, [ZonValidators.required('自动对账')]],
      autoConfirmService: [false, [ZonValidators.required('自动确认服务')]],
      autoConfirmServiceHours: [null, [ZonValidators.intOrFloat('自动确认服务时间'),
        ZonValidators.max(240, '自动确认服务时间'), ZonValidators.min(1, '自动确认服务时间')]],
      autoConfirmFee: [false, [ZonValidators.required('自动确认费用')]],
      autoConfirmFeeHours: [null, [ZonValidators.intOrFloat('自动确认费用时间'),
        ZonValidators.max(240, '自动确认费用时间'), ZonValidators.min(1, '自动确认费用时间')]]
    })
  }

  ngOnInit() {
    // 初始化表单
    if (this.demanderAutoConfig) {
      this.form.patchValue({
        settleType: this.demanderAutoConfig.settleType,
        settleDay: this.demanderAutoConfig.settleDay === 0 ? null : this.demanderAutoConfig.settleDay,
        autoVerify: this.demanderAutoConfig.autoVerify === 'Y' ? true : false,
        autoConfirmService: this.demanderAutoConfig.autoConfirmService === 'Y' ? true : false,
        autoConfirmServiceHours: this.demanderAutoConfig.autoConfirmServiceHours === 0 ?
          null : this.demanderAutoConfig.autoConfirmServiceHours,
        autoConfirmFee: this.demanderAutoConfig.autoConfirmFee === 'Y' ? true : false,
        autoConfirmFeeHours: this.demanderAutoConfig.autoConfirmFeeHours === 0 ?
          null : this.demanderAutoConfig.autoConfirmFeeHours
      })
    }
  }

  // 取消
  destroyModal() {
    this.modalRef.destroy('cancel');
  }

  // 提交表单
  submitForm() {
    if (!this.form.valid) {
      return;
    }
    if ((this.form.value.settleType === 2 || this.form.value.settleType === 3) && !this.form.value.settleDay) {
      this.messageService.warning('结算方式为按月结算或按季度结算时，结算日期必填');
      return;
    }
    if (this.form.value.autoConfirmService && !this.form.value.autoConfirmServiceHours) {
      this.messageService.warning('自动确认服务开启时，自动确认服务时间必填');
      return;
    }
    if (this.form.value.autoConfirmFee && !this.form.value.autoConfirmFeeHours) {
      this.messageService.warning('自动确认费用开启时，自动确认费用时间必填');
      return;
    }
    const params = {
      id: this.demanderServiceId,
      settleType: this.form.value.settleType,
      settleDay: this.form.value.settleDay,
      autoVerify: this.form.value.autoVerify ? 'Y' : 'N',
      autoConfirmService: this.form.value.autoConfirmService ? 'Y' : 'N',
      autoConfirmServiceHours: this.form.value.autoConfirmServiceHours,
      autoConfirmFee: this.form.value.autoConfirmFee ? 'Y' : 'N',
      autoConfirmFeeHours: this.form.value.autoConfirmFeeHours,
    }
    this.submitLoading = true;
    this.httpClient.post('/api/anyfix/demander-auto-config/config', params)
      .pipe(
        finalize(() => {
          this.submitLoading = false;
        })
      ).subscribe((res: any) => {
        this.messageService.success('配置成功！');
        this.modalRef.destroy('submit');
    })
  }

}
