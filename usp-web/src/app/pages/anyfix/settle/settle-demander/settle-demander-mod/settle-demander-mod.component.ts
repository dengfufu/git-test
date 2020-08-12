import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {finalize} from 'rxjs/operators';
import {SelectBankAccountComponent} from '../select-bank-account/select-bank-account.component';
import {ZonValidators} from '@util/zon-validators';
import {WorkCheckService} from '../../../work/work-check.service';
import {UserService} from '@core/user/user.service';
import {AnyfixService} from '@core/service/anyfix.service';
import {SelectWorkFeeVerifyComponent} from '../select-work-fee-verify/select-work-fee-verify.component';
import {DemanderContNoComponent} from '../demander-cont-no/demander-cont-no.component';
import {SettleService} from '../settle.service';
import {SelectSettleWorkComponent} from '../select-settle-work/select-settle-work.component';

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
    public anyfixService: AnyfixService,
    public settleService: SettleService
  ) {
    this.form = this.formBuilder.group({
      note: [null, [ZonValidators.maxLength(400, '备注')]],
      // settleCode: [null, [ZonValidators.maxLength(50, '结算单号')]],
      accountId: [],
      accountNumber: [],
      accountName: [],
      accountBank: []
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
        this.detailList = this.settleDemander.settleWay === this.settleService.SETTLE_WORK ?
          this.settleDemander.workFeeDtoList : this.settleDemander.workFeeVerifyList;
        // 填充表单
        this.form.patchValue({
          note: this.settleDemander.note,
          contNo: this.settleDemander.contNo,
          accountId: this.settleDemander.accountId,
          accountNumber: this.settleDemander.accountNumber,
          accountName: this.settleDemander.accountName,
          accountBank: this.settleDemander.accountBank
        });
        this.settleFee = this.settleDemander.settleFee;
        this.startDate = this.settleDemander.startDate;
        this.endDate = this.settleDemander.endDate;
      });
  }

  // 查看委托协议
  showCont() {
    const modal = this.modalService.create({
      nzTitle: '查看委托协议',
      nzContent: DemanderContNoComponent,
      nzWidth: 600,
      nzComponentParams: {
        contId: this.settleDemander.contId
        // serviceCorp: this.userService.currentCorp.corpId,
        // demanderCorp: this.settleDemander.demanderCorp
      },
      nzFooter: null
    });
  }

  // 删除对账单
  deleteVerify(verify) {
    this.detailList = this.detailList.filter((item: any) => item.verifyId !== verify.verifyId);
    this.settleFee = this.settleFee - verify.verifyAmount;
    this.getTimeRange();
    // this.createContNo();
  }

  // 删除工单
  deleteWork(work) {
    this.detailList.filter((item: any) => item.workId !== work.workId);
    this.settleFee = this.settleFee - work.totalFee;
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
        // this.createContNo();
      }
    });
  }

  // 选择工单
  selectWorkList() {
    const params = {
      serviceCorp: this.settleDemander.serviceCorp,
      demanderCorp: this.settleDemander.demanderCorp,
      // startDate: this.form.value.startDate,
      // endDate: this.form.value.endDate,
      district: this.settleDemander.district,
      // 对账待评价、已完成的工单
      workStatuses: this.anyfixService.settleWorkStatusList.join(','),
      // 对账单次保的工单
      warrantyModes: '20,30',
      feeCheckStatus: this.workCheckService.CHECK_PASS,
      feeConfirmStatuses: this.workCheckService.CHECK_PASS.toString(),
      // settleDemanderStatus: 1,
      settleId: this.settleDemander.settleId
    };
    const modal = this.modalService.create({
      nzTitle: '选择结算工单',
      nzContent: SelectSettleWorkComponent,
      nzComponentParams: {workFilter: params, selectedWorkList: [...this.detailList]},
      nzFooter: null,
      nzWidth: 900,
      nzStyle: {'margin-top': '-50px'}
    });
    modal.afterClose.subscribe((res: any) => {
      if (res && res.role === 'submit') {
        console.log(res);
        this.detailList = res.detailList || [];
        this.settleFee = 0;
        this.detailList.forEach((item: any) => {
          this.settleFee += item.totalFee;
        });
        this.getTimeRange();
      }
    });
  }

  // 获取结算周期
  getTimeRange() {
    if (this.settleDemander.settleWay === this.settleService.SETTLE_PERIOD) {
      if (this.detailList && this.detailList.length > 0) {
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
    } else if (this.settleDemander.settleWay === this.settleService.SETTLE_WORK) {
      if (this.detailList.length > 0) {
        let startDate = this.detailList[0].endTime;
        let endDate = this.detailList[0].endTime;
        this.detailList.forEach((work: any) => {
          if (work.endTime <= startDate) {
            startDate = work.endTime;
          }
          if (work.endTime >= endDate) {
            endDate = work.endTime;
          }
        });
        this.startDate = startDate;
        this.endDate = endDate;
      } else {
        this.startDate = null;
        this.endDate = null;
      }
    } else {
      this.startDate = null;
      this.endDate = null;
    }
  }

  // 生成合同号
  createContNo() {
    if ((this.endDate || '').length <= 0) {
      this.form.patchValue({
        contNo: ''
      })
      return;
    }
    const params = {
      demanderCorp: this.settleDemander.demanderCorp,
      settleId: this.settleDemander.settleId
    };
    this.isLoading = true;
    this.httpClient.post('/api/anyfix/settle-demander/generate/contNo', params)
      .pipe(
        finalize(() => {
          this.isLoading = false;
        })
      ).subscribe((res: any) => {
      this.form.patchValue({
        contNo: res.data
      });
    });
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
    const params = Object.assign({}, this.form.value);
    params.settleId = this.settleDemander.settleId;
    params.serviceCorp = this.settleDemander.serviceCorp;
    params.demanderCorp = this.settleDemander.demanderCorp;
    params.settleWay = this.settleDemander.settleWay;
    params.district = this.settleDemander.district;
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
