import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {UserService} from '@core/user/user.service';
import {finalize} from 'rxjs/operators';
import {ZonValidators} from '@util/zon-validators';
import {SelectBankAccountComponent} from '../select-bank-account/select-bank-account.component';
import {SelectWorkFeeVerifyComponent} from '../select-work-fee-verify/select-work-fee-verify.component';
import {DemanderContNoComponent} from '../demander-cont-no/demander-cont-no.component';
import {ACLService} from '@delon/acl';
import {ANYFIX_RIGHT} from '@core/right/right';
import {SettleService} from '../settle.service';
import {AnyfixService} from '@core/service/anyfix.service';
import {SelectSettleWorkComponent} from '../select-settle-work/select-settle-work.component';
import {WorkCheckService} from '../../../work/work-check.service';

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
  // 工单列表
  workList: any[] = [];
  // 开始日期
  startDate: any;
  // 截止日期
  endDate: any;
  // 权限集合
  rightIdList = this.aclService.data.abilities || [];
  // 委托协议号
  contNo = '';
  // 委托协议编号
  contId = '';

  constructor(
    private httpClient: HttpClient,
    private modalService: NzModalService,
    private modalRef: NzModalRef,
    private fromBuilder: FormBuilder,
    private userService: UserService,
    private messageService: NzMessageService,
    private aclService: ACLService,
    public settleService: SettleService,
    public anyfixService: AnyfixService,
    public workCheckService: WorkCheckService
  ) {
    this.form = fromBuilder.group({
      demanderCorp: [null, [ZonValidators.required('委托商')]], // 委托商
      settleWay: [null, [ZonValidators.required('结算方式')]], // 结算方式
      district: [], // 行政区划
      // settleCode: [null, [ZonValidators.maxLength(50, '结算单号')]],
      note: [null, [ZonValidators.maxLength(400, '备注')]], // 备注
      // discount: [0, [ZonValidators.intOrFloat('折扣率'), ZonValidators.min(0, '折扣率'),
      //   ZonValidators.max(100, '折扣率')]], // 折扣率
      // discountAmount: [0, [ZonValidators.intOrFloat('优惠金额'), ZonValidators.min(0, '优惠金额')]],
      // taxRate: [0, [ZonValidators.intOrFloat('税率'), ZonValidators.min(0, '税率'),
      //   ZonValidators.max(100, '税率')]],
      accountId: [],
      accountNumber: [],
      accountName: [],
      accountBank: []
    });
  }

  ngOnInit() {
    this.findCorpBankAccount();
    this.queryDemanderOptions();
    this.listDistrict();
  }

  /**
   * 获得服务商银行账户信息
   */
  findCorpBankAccount() {
    this.httpClient.get(`/api/uas/corp-bank-account/corp/` + this.userService.currentCorp.corpId)
      .subscribe((res: any) => {
        const corpBankAccount = res.data;
        if (corpBankAccount) {
          this.form.patchValue({
            accountId: corpBankAccount.accountId,
            accountNumber: corpBankAccount.accountNumber,
            accountName: corpBankAccount.accountName,
            accountBank: corpBankAccount.accountBank
          });
        }
      });
  }

  // 查询委托商下拉框数据
  queryDemanderOptions() {
    this.demanderOptionLoading = true;
    if (this.rightIdList.includes(ANYFIX_RIGHT.SETTLE_DEMANDER_MANAGER)
      && !this.rightIdList.includes(ANYFIX_RIGHT.SETTLE_DEMANDER_ADD)) {
      const params = {
        serviceCorp: this.userService.currentCorp.corpId,
        managerId: this.userService.userInfo.userId
      };
      this.httpClient.post('/api/anyfix/demander-service-manager/listDemander', params)
        .pipe(
          finalize(() => {
            this.demanderOptionLoading = false;
          })
        ).subscribe((res: any) => {
        this.demanderOptions = res.data || [];
      });
    } else {
      const params = {
        serviceCorp: this.userService.currentCorp.corpId,
        enabled: 'Y'
      };
      this.httpClient.post('/api/anyfix/demander-service/demander/list', params)
        .pipe(
          finalize(() => {
            this.demanderOptionLoading = false;
          })
        ).subscribe((res: any) => {
        this.demanderOptions = res.data || [];
      });
    }
  }

  // 行政区划下拉数据
  listDistrict() {
    this.httpClient.get('/api/uas/area/list')
      .subscribe((res: any) => {
        this.districtsOptions = res.data;
      });
  }

  // 更改委托商，清空选择的对账单或工单、结算周期、结算费用、合同号
  demanderChange(event) {
    this.verifyList = [];
    this.workList = [];
    this.startDate = null;
    this.endDate = null;
    this.settleFee = 0;
    this.form.patchValue({
      settleCode: ''
    });
    // 获取结算方式
    if (event) {
      this.httpClient.get(`/api/anyfix/demander-auto-config/${event}/${this.userService.currentCorp.corpId}`)
        .subscribe((res: any) => {
          if (res && res.data) {
            if (res.data.settleType === 1) {
              // 按单结算
              this.form.controls.settleWay.setValue(this.settleService.SETTLE_WORK);
            } else {
              // 按周期结算
              this.form.controls.settleWay.setValue(this.settleService.SETTLE_PERIOD);
            }
          }
        })
    }
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
      if (res && res.role === 'submit') {
        this.verifyList = res.data || [];
        this.settleFee = 0;
        this.verifyList.forEach((item: any) => {
          this.settleFee += item.verifyAmount;
        });
        this.getTimeRange();
        this.findContNo();
      }
    });
  }

  // 获取委托协议
  findContNo() {
    if (this.form.value.demanderCorp && this.startDate && this.endDate) {
      const params = {
        serviceCorp: this.userService.currentCorp.corpId,
        demanderCorp: this.form.value.demanderCorp,
        startDate: this.startDate,
        endDate: this.endDate
      };
      this.httpClient.post('/api/anyfix/demander-cont/fee', params)
        .subscribe((res: any) => {
          if (res && res.data) {
            this.contNo = res.data.contNo;
            this.contId = res.data.id;
          }
        });
    }
  }

  // 选择工单
  selectWorkList() {
    const params = {
      serviceCorp: this.userService.currentCorp.corpId,
      demanderCorp: this.form.value.demanderCorp,
      startDate: this.form.value.startDate,
      endDate: this.form.value.endDate,
      district: (this.form.value.district || []).length > 0 ?
        this.form.value.district[this.form.value.district.length - 1] : '',
      // 待评价、已完成的工单
      workStatuses: this.anyfixService.settleWorkStatusList.join(','),
      // 费用已确认通过
      feeConfirmStatuses: this.workCheckService.CHECK_PASS.toString(),
      // 单次保的工单
      warrantyModes: '20,30',
      feeCheckStatus: this.workCheckService.CHECK_PASS,
      settleDemanderStatuses: '1,2'
    };
    const modal = this.modalService.create({
      nzTitle: '选择结算工单',
      nzContent: SelectSettleWorkComponent,
      nzComponentParams: {workFilter: params, selectedWorkList: [...this.workList]},
      nzFooter: null,
      nzWidth: 900,
      nzStyle: {'margin-top': '-50px'}
    });
    modal.afterClose.subscribe((res: any) => {
      if (res && res.role === 'submit') {
        console.log(res);
        this.workList = res.detailList || [];
        this.settleFee = 0;
        this.workList.forEach((item: any) => {
          this.settleFee += item.totalFee;
        });
        this.getTimeRange();
        this.findContNo();
      }
    });
  }

  // 获取结算周期
  getTimeRange() {
    if (this.form.value.settleWay === this.settleService.SETTLE_PERIOD) {
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
    } else if (this.form.value.settleWay === this.settleService.SETTLE_WORK) {
      if (this.workList.length > 0) {
        let startDate = this.workList[0].endTime;
        let endDate = this.workList[0].endTime;
        this.workList.forEach((work: any) => {
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

  // 查看委托协议
  showCont() {
    const modal = this.modalService.create({
      nzTitle: '查看委托协议',
      nzContent: DemanderContNoComponent,
      nzWidth: 800,
      nzComponentParams: {
        contId: this.contId
      },
      nzFooter: null
    });
  }

  // 生成合同号
  createSettleCode() {
    if ((this.form.value.demanderCorp || '').length <= 0) {
      this.messageService.warning('请先选择委托商！');
      return;
    }
    const params = {
      demanderCorp: this.form.value.demanderCorp
    };
    this.isLoading = true;
    this.httpClient.post('/api/anyfix/settle-demander/generate/contNo', params)
      .pipe(
        finalize(() => {
          this.isLoading = false;
        })
      ).subscribe((res: any) => {
      this.form.patchValue({
        settleCode: res.data
      });
    });
  }

  // 删除对账单
  deleteVerify(verify) {
    this.verifyList.filter((item: any) => item.verifyId !== verify.verifyId);
    this.settleFee = this.settleFee - verify.verifyAmount;
    this.getTimeRange();
    this.findContNo();
  }

  // 删除工单
  deleteWork(work) {
    this.workList.filter((item: any) => item.workId !== work.workId);
    this.settleFee = this.settleFee - work.totalFee;
    this.getTimeRange();
    this.findContNo();
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
      settleWay: this.form.value.settleWay,
      startDate: this.startDate,
      endDate: this.endDate,
      contId: this.contId,
      contNo: this.contNo,
      district: (this.form.value.district || []).length > 0 ?
        this.form.value.district[this.form.value.district.length - 1] : '',
      note: this.form.value.note,
      settleFee: this.settleFee,
      accountId: this.form.value.accountId,
      accountNumber: this.form.value.accountNumber,
      accountName: this.form.value.accountName,
      accountBank: this.form.value.accountBank,
      detailDtoList: this.form.value.settleWay === this.settleService.SETTLE_PERIOD ? this.verifyList : this.workList
    };
    if (!this.form.valid) {
      return;
    }
    if (this.form.value.settleWay === this.settleService.SETTLE_PERIOD) {
      if (!this.verifyList || this.verifyList.length <= 0) {
        this.messageService.error('请选择对账单');
      }
    } else {
      if (!this.workList || this.workList.length <= 0) {
        this.messageService.error('请选择结算工单');
      }
    }
    if (this.settleFee <= 0) {
      this.messageService.error('结算金额应大于0');
      return;
    }
    if ((this.contId || '').length <= 0 || (this.contNo || '').length <= 0) {
      this.messageService.error('未获取到委托协议号， 请检查');
      return;
    }
    this.httpClient.post('/api/anyfix/settle-demander/add', params)
      .subscribe((res: any) => {
        this.messageService.success('添加成功');
        this.modalRef.destroy('submit');
      });
  }

}
