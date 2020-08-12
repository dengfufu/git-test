import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {UserService} from '@core/user/user.service';
import {finalize} from 'rxjs/operators';
import {ZonValidators} from '@util/zon-validators';
import {SelectBankAccountComponent} from '../select-bank-account/select-bank-account.component';
import {SelectWorkFeeVerifyComponent} from '../select-work-fee-verify/select-work-fee-verify.component';

@Component({
  selector: 'app-settle-demander-add',
  templateUrl: './settle-demander-add.component.html',
  styleUrls: ['./settle-demander-add.component.less']
})
export class SettleDemanderAddComponent implements OnInit {

  // 表单
  form: FormGroup;
  // 加载中
  isLoading = false;
  // 委托商下拉数据
  demanderOptions: any[] = [];
  // 行政区划下选项
  districtsOptions: any[] = [];
  // 委托商下拉框加载中
  demanderOptionLoading = false;
  // 结算总金额
  settleFee = 0;
  // 对账单列表
  verifyList: any[] = [];
  // 开始日期
  startDate: any;
  // 截止日期
  endDate: any;

  constructor(
    private httpClient: HttpClient,
    private modalService: NzModalService,
    private modalRef: NzModalRef,
    private fromBuilder: FormBuilder,
    private userService: UserService,
    private messageService: NzMessageService
  ) {
    this.form = fromBuilder.group({
      demanderCorp: [null, [ZonValidators.required('委托商')]], // 委托商
      district: [], // 行政区划
      contNo: [null, [ZonValidators.maxLength(50, '协议合同号')]],
      note: [null, [ZonValidators.maxLength(400, '备注')]], // 备注
      // discount: [0, [ZonValidators.intOrFloat('折扣率'), ZonValidators.min(0, '折扣率'),
      //   ZonValidators.max(100, '折扣率')]], // 折扣率
      // discountAmount: [0, [ZonValidators.intOrFloat('优惠金额'), ZonValidators.min(0, '优惠金额')]],
      // taxRate: [0, [ZonValidators.intOrFloat('税率'), ZonValidators.min(0, '税率'),
      //   ZonValidators.max(100, '税率')]],
      accountNumber: [null, [ZonValidators.maxLength(30, '收款账户'), Validators.pattern(/^\d{6,30}$/i)]],
      accountName: [null, [ZonValidators.maxLength(50, '户名')]],
      accountBank: [null, [ZonValidators.maxLength(50, '开户银行')]]
    });
  }

  ngOnInit() {
    this.queryDemanderOptions();
    this.listDistrict();
  }

  // 查询委托商下拉框数据
  queryDemanderOptions() {
    const params = {
      serviceCorp: this.userService.currentCorp.corpId,
      enabled: 'Y'
    };
    this.demanderOptionLoading = true;
    this.httpClient.post('/api/anyfix/demander-service/demander/list', params)
      .pipe(
        finalize(() => {
          this.demanderOptionLoading = false;
        })
      ).subscribe((res: any) => {
      this.demanderOptions = res.data;
    });
  }

  // 行政区划下拉数据
  listDistrict() {
    this.httpClient.get('/api/uas/area/list')
      .subscribe((res: any) => {
        this.districtsOptions = res.data;
      });
  }

  // 选择对账单
  selectVerifyList() {
    const params = {
      serviceCorp: this.userService.currentCorp.corpId,
      demanderCorp: this.form.value.demanderCorp,
      status: 5, // 确认通过
      settleStatus: 1, // 未结算
      // startDate: this.form.value.settleTimeRange[0],
      // endDate: this.form.value.settleTimeRange[1],
      district: (this.form.value.district || []).length > 0 ?
        this.form.value.district[this.form.value.district.length - 1] : ''
    };
    const modal = this.modalService.create({
      nzTitle: '选择对账单',
      nzContent: SelectWorkFeeVerifyComponent,
      nzComponentParams: {verifyFilter: params, selectedVerifyList: [...this.verifyList]},
      nzFooter: null,
      nzWidth: 900,
      nzStyle: {'margin-top': '-50px'}
    });
    modal.afterClose.subscribe((res: any) => {
      console.log(res);
      if (res && res.role === 'submit') {
        this.verifyList = res.data || [];
        this.settleFee = 0;
        this.verifyList.forEach((item: any) => {
          this.settleFee += item.verifyAmount;
        });
        this.getTimeRange();
      }
    });
  }

  // 获取结算周期
  getTimeRange() {
    if (this.verifyList.length > 0) {
      let startDate = this.verifyList[0].startDate;
      let endDate = this.verifyList[0].endDate;
      this.verifyList.forEach((verify: any) => {
        if (verify.startDate <= startDate) {
          startDate = verify.startDate;
        }
        if (verify.endDate >= endDate) {
          endDate = verify.endDate;
        }
      });
      this.startDate = startDate;
      this.endDate = endDate;
    } else {
      this.startDate = null;
      this.endDate = null;
    }
  }

  // 删除对账单
  deleteVerify(verify) {
    this.verifyList.filter((item: any) => item.verifyId !== verify.verifyId);
    this.settleFee = this.settleFee - verify.verifyAmount;
  }

  // 选择收款信息
  selectReceiptAccount() {
    const modal = this.modalService.create({
      nzTitle: '选择收款账户',
      nzContent: SelectBankAccountComponent,
      nzWidth: 700,
      nzComponentParams: {
        selectedAccount: {
          accountNumber: this.form.value.accountNumber,
          accountName: this.form.value.accountName,
          accountBank: this.form.value.accountBank
        }
      },
      nzFooter: null
    });
    modal.afterClose.subscribe((res: any) => {
      if (res) {
        this.form.patchValue({
          accountNumber: res.accountNumber,
          accountName: res.accountName,
          accountBank: res.accountBank
        });
      }
    });
  }

  // 取消添加
  cancel() {
    this.modalRef.destroy('cancel');
  }

  // 添加提交
  submit() {
    const params = {
      serviceCorp: this.userService.currentCorp.corpId,
      demanderCorp: this.form.value.demanderCorp,
      startDate: this.startDate,
      endDate: this.endDate,
      district: (this.form.value.district || []).length > 0 ?
        this.form.value.district[this.form.value.district.length - 1] : '',
      note: this.form.value.note,
      // taxRate: this.form.value.taxRate / 100,
      settleFee: this.settleFee,
      // discountAmount: this.form.value.discountAmount,
      // payable: (this.settleFee - this.form.value.discountAmount),
      accountNumber: this.form.value.accountNumber,
      accountName: this.form.value.accountName,
      accountBank: this.form.value.accountBank,
      detailDtoList: this.verifyList
    };
    // if (this.workQuantity <= 0) {
    //   this.messageService.error('无可结算工单，请检查！');
    //   return;
    // }
    if (!this.verifyList || this.verifyList.length <= 0) {
      this.messageService.error('请选择对账工单');
    }
    if (this.settleFee <= 0) {
      this.messageService.error('结算金额应大于0');
      return;
    }
    // if (this.settleFee - this.form.value.discountAmount <= 0) {
    //   this.messageService.error('优惠金额应小于总金额');
    //   return;
    // }
    if ((this.form.value.accountNumber || '').length > 0
      || (this.form.value.accountName || '').length > 0
      || (this.form.value.accountBank || '').length > 0) {
      if ((this.form.value.accountNumber || '').length <= 0
        || (this.form.value.accountName || '').length <= 0
        || (this.form.value.accountBank || '').length <= 0) {
        this.messageService.error('请填写完整收款账户信息：收款账户、户名、开户行');
        return;
      }
    }
    console.log(params);
    this.httpClient.post('/api/anyfix/settle-demander/add', params)
      .subscribe((res: any) => {
        this.messageService.success('添加成功');
        this.modalRef.destroy('submit');
      });
  }

}
