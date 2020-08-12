import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {finalize} from 'rxjs/operators';
import {SelectBankAccountComponent} from '../select-bank-account/select-bank-account.component';
import {ZonValidators} from '@util/zon-validators';
import {WorkCheckService} from '../../../../work/work-check.service';
import {UserService} from '@core/user/user.service';
import {AnyfixService} from '@core/service/anyfix.service';
import {SelectWorkFeeVerifyComponent} from '../select-work-fee-verify/select-work-fee-verify.component';

@Component({
  selector: 'app-settle-demander-mod',
  templateUrl: './settle-demander-mod.component.html',
  styleUrls: ['./settle-demander-mod.component.less']
})
export class SettleDemanderModComponent implements OnInit {

  // 结算单系统编号
  @Input() settleId: string;
  // 结算单
  settleDemander: any =  {};
  // 表单
  form: FormGroup;
  // 提交中
  isLoading = false;
  // 明细
  detailList: any[] = [];
  // 按单结算时，结算单的委托商确认状态
  demanderCheckStatusName = '';
  // 结算总金额
  settleFee = 0;
  // 结算工单数量
  workQuantity = 0;
  // 开始日期
  startDate: any;
  // 截止日期
  endDate: any;
  // 页面加载中
  spinning = false;

  constructor(
    public httpClient: HttpClient,
    public messageService: NzMessageService,
    public formBuilder: FormBuilder,
    public modalRef: NzModalRef,
    public modalService: NzModalService,
    public workCheckService: WorkCheckService,
    public userService: UserService,
    public anyfixService: AnyfixService
  ) {
    this.form = this.formBuilder.group({
      note: [null, [ZonValidators.maxLength(400, '备注')]],
      contNo: [null, [ZonValidators.maxLength(50, '协议合同号')]],
      discountAmount: [0, [ZonValidators.intOrFloat('优惠金额'), ZonValidators.min(0, '优惠金额')]],
      taxRate: [0, [ZonValidators.intOrFloat('税率'), ZonValidators.min(0, '税率'),
        ZonValidators.max(100, '税率')]],
      accountNumber: [null, [ZonValidators.maxLength(30, '收款账户'), Validators.pattern(/^\d{6,30}$/i)]],
      accountName: [null, [ZonValidators.maxLength(50, '户名')]],
      accountBank: [null, [ZonValidators.maxLength(50, '开户银行')]]
    });
  }

  ngOnInit() {
    this.querySettleDemander();
  }

  // 获取结算单详情
  querySettleDemander() {
    this.spinning = true;
    this.httpClient.get(`/api/anyfix/settle-demander/${this.settleId}`)
      .pipe(
        finalize(() => {
          this.spinning = false;
        })
      )
      .subscribe((res: any) => {
        this.settleDemander = res.data;
        this.detailList = this.settleDemander.detailDtoList;
        // 填充表单
        this.form.patchValue({
          note: this.settleDemander.note,
          contNo: this.settleDemander.contNo,
          accountNumber: this.settleDemander.accountNumber,
          accountName: this.settleDemander.accountName,
          accountBank: this.settleDemander.accountBank
        });
        this.settleFee = this.settleDemander.settleFee;
        this.startDate = this.settleDemander.startDate;
        this.endDate = this.settleDemander.endDate;
      });
  }

  // 删除对账单
  deleteVerify(verify) {
    this.detailList.filter((item: any) => item.verifyId !== verify.verifyId);
    this.settleFee = this.settleFee - verify.verifyAmount;
  }

  // 选择工单
  selectVerifyList() {
    const modal = this.modalService.create({
      nzTitle: '选择对账单',
      nzContent: SelectWorkFeeVerifyComponent,
      nzWidth: 1000,
      nzComponentParams: {
        verifyFilter: this.getVerifyFilter(),
        selectedVerifyList: this.detailList
      },
      nzFooter: null
    });
    modal.afterClose.subscribe((res: any) => {
      if (res) {
        this.detailList = res.data || [];
        this.settleFee = 0;
        this.detailList.forEach((item: any) => {
          this.settleFee += item.verifyAmount;
        });
        this.getTimeRange();
      }
    });
  }

  // 获取结算周期
  getTimeRange() {
    if (this.detailList.length > 0) {
      let startDate = this.detailList[0].startDate;
      let endDate = this.detailList[0].endDate;
      this.detailList.forEach((verify: any) => {
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
        })
      }
    });
  }

  getVerifyFilter() {
    const params = {
      serviceCorp: this.userService.currentCorp.corpId,
      demanderCorp: this.settleDemander.demanderCorp,
      district: this.settleDemander.district,
      status: 5, // 确认通过
      // settleStatus: 1, // 未结算
      settleId: this.settleId
    }
    return params;
  }

  // 取消
  cancel() {
    this.modalRef.destroy();
  }

  // 修改提交
  submit() {
    if ((this.detailList || []).length <= 0) {
      this.messageService.error('未选择对账单，请检查！');
      return;
    }
    if (this.settleFee <= 0) {
      this.messageService.error('结算金额应大于0');
      return;
    }
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
    const params = Object.assign({}, this.form.value);
    params.settleId = this.settleDemander.settleId;
    params.serviceCorp = this.settleDemander.serviceCorp;
    params.settleFee = this.settleFee;
    params.startDate = this.startDate;
    params.endDate = this.endDate;
    this.detailList.forEach((work: any) => {
      work.settleId = this.settleDemander.settleId;
    });
    params.detailDtoList = this.detailList;
    this.isLoading = true;
    this.httpClient.post('/api/anyfix/settle-demander/update', params)
      .pipe(
        finalize(() => {
          this.isLoading = false;
        })
      ).subscribe((res: any) => {
        this.messageService.success('修改成功！');
        this.modalRef.destroy('submit');
    })
  }

}
