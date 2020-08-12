import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Page, Result} from '@core/interceptor/result';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {WorkFeeRuleViewComponent} from '../../../../setting/work-config/work-fee-rule/work-fee-rule-view/work-fee-rule-view.component';
import {ImplementFeeViewComponent} from '../../../../setting/work-config/implement-fee/implement-fee-view/implement-fee-view.component';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {WorkFeeEditComponent} from '../work-fee-edit/work-fee-edit.component';
import {WorkCheckService} from '../../work-check.service';
import {ANYFIX_RIGHT} from '@core/right/right';
import {ACLService} from '@delon/acl';
import {UserService} from '@core/user/user.service';
import {AnyfixService} from '@core/service/anyfix.service';
import {FeeCheckComponent} from '../work-check/fee-check.component';
import {FeeConfirmComponent} from '../work-confirm/fee-confirm.component';
import {environment} from '@env/environment';
import {WorkConfigService} from '../../../../setting/work-config/work-config.service';

@Component({
  selector: 'work-fee-detail',
  templateUrl: './work-fee-detail.component.html',
  styleUrls: ['./work-fee-detail.component.less']
})
export class WorkFeeDetailComponent implements OnInit {

  @Input() work: any;
  @Output()
  refreshParent: EventEmitter<any> = new EventEmitter();

  // 页面加载中
  spinning = false;
  // 显示工单收费规则定义
  showAssortFee = false;
  // 工单收费规则分页
  assortPage = new Page();
  // 工单收费规则数据
  workFeeAssortList: any[] = [];
  // 收费规则列表加载中
  assortFeeLoading = false;
  assortFeeList: any[] = [];
  implementFeeList: any[] = [];
  aclRight = ANYFIX_RIGHT;
  aclRightIdList: any[] = [];
  // 是否启用报价
  needQuote = false;

  contConfigData : any = {};
  feeCheckStatusLabel: string; // 工单费用审核状态表单名称
  feeCheckStatusName: string; // 工单费用审核状态名称
  feeCheckNote: string; // 工单费用审核备注
  feeCheckBtnName = '审核'; // 工单费用审核按钮名称
  detail: any = {};
  imageList : any[] = [];
  isQueriedContConfig = false;
  feeRuleLoading = false;
  showFileUrl = environment.server_url + '/api/file/showFile?fileId=';
  constructor(
    public httpClient: HttpClient,
    public modalService: NzModalService,
    public workCheckService: WorkCheckService,
    public aclService: ACLService,
    public userService: UserService,
    public anyfixService: AnyfixService,
    public messageService: NzMessageService,
    public workConfigService: WorkConfigService
  ) {

  }

  ngOnInit() {
    this.initData();
  }

  initData() {
    this.aclRightIdList = this.aclService.data.abilities || [];
    this.assortFeeList = (this.work.workFeeDetailDtoList || []).filter((item: any) => item.feeType === 1);
    this.implementFeeList = (this.work.workFeeDetailDtoList || []).filter((item: any) => item.feeType === 2);
    this.initFeeCheckStatusName();
    this.initItemConfig();
  }

  /**
   * 初始化工单费用审核状态名称
   */
  initFeeCheckStatusName() {
    // 先清除费用审核状态名称，以避免刷新的时候，状态不刷新
    this.feeCheckStatusLabel = null;
    this.feeCheckStatusName = null;
    const workStatus = this.work.workStatus;
    const workFeeStatus = this.work.workFeeStatus;
    const feeCheckStatus = this.work.feeCheckStatus;
    const feeConfirmStatus = this.work.feeConfirmStatus;
    // 服务商查看已完成的工单
    if (this.anyfixService.demanderCheckStatusList.includes(workStatus) && this.isServiceCorp()) {
      this.feeCheckStatusLabel = '费用审核状态';
      if (workFeeStatus === 1) {
        this.feeCheckStatusName = '未录入完成';
      }
      if (feeCheckStatus === this.workCheckService.TO_CHECK) {
        this.feeCheckStatusName = '待审核';
      }
      if (feeCheckStatus === this.workCheckService.CHECK_REFUSE) {
        this.feeCheckBtnName = '重新审核';
        this.feeCheckStatusName = '审核不通过';
        this.feeCheckNote = this.work.feeCheckNote || '';
      }
      if (feeCheckStatus === this.workCheckService.CHECK_PASS) {
        this.feeCheckBtnName = '重新审核';
        this.feeCheckStatusName = '审核通过';
        this.feeCheckNote = this.work.feeCheckNote || '';
        if (feeConfirmStatus === this.workCheckService.TO_CONFIRM) {
          this.feeCheckStatusName += '待确认';
        }
        if (feeConfirmStatus === this.workCheckService.CONFIRM_REFUSE) {
          this.feeCheckStatusName = '确认不通过';
          this.feeCheckNote = this.work.feeConfirmNote || '';
        }
      }
      if (feeConfirmStatus === this.workCheckService.CONFIRM_PASS) {
        this.feeCheckStatusName = '确认通过';
        this.feeCheckNote = this.work.feeConfirmNote || '';
      }
    }
    // 委托商查看已完成的工单
    if (this.anyfixService.demanderCheckStatusList.includes(workStatus) && this.isDemanderCorp()) {
      this.feeCheckStatusLabel = '费用确认状态';
      if (feeConfirmStatus === this.workCheckService.TO_CONFIRM) {
        this.feeCheckStatusName = '待确认';
      }
      if (feeConfirmStatus === this.workCheckService.CONFIRM_REFUSE) {
        this.feeCheckStatusName = '确认不通过';
        this.feeCheckNote = this.work.feeConfirmNote || '';
      }
      if (feeConfirmStatus === this.workCheckService.CONFIRM_PASS) {
        this.feeCheckStatusName = '确认通过';
        this.feeCheckNote = this.work.feeConfirmNote || '';
      }
    }
  }

  // 获取数据项配置
  initItemConfig() {
    const itemIdList = [this.workConfigService.WORK_ADD_NEED_QUOTE];
    this.httpClient.post('/api/anyfix/service-config/corp/getConfig', {corpId: this.work.demanderCorp, itemIdList})
      .subscribe((res: any) => {
        const iemtConfigList = res.data || [];
        this.needQuote = iemtConfigList.find((itemConfig: any) =>
          itemConfig.itemId === this.workConfigService.WORK_ADD_NEED_QUOTE && itemConfig.itemValue === '2') !== undefined
      });
  }

  // 展开查看工单收费规则
  appendAssortFee() {
    this.showAssortFee = !this.showAssortFee;
    if (this.workFeeAssortList.length <= 0) {
      this.queryAssortFeeList(true);
    }
    if(!this.isQueriedContConfig && this.aclRightIdList.includes(ANYFIX_RIGHT.WORK_ASSORT_FEE)) {
      this.getContConfigData();
    }
  }

  // 当前企业是否为服务商
  isServiceCorp() {
    return this.userService.currentCorp.corpId === this.work.serviceCorp;
  }

  // 当前企业是否为委托商
  isDemanderCorp() {
    return this.userService.currentCorp.corpId === this.work.demanderCorp;
  }

  getContConfigData() {
    if (this.work.serviceCorp === '0' ||
      this.work.demanderCorp === '0' ||
      this.work.dispatchTime == null) {
      return;
    }
    this.feeRuleLoading = true;
    const params = {
      serviceCorp: this.work.serviceCorp,
      demanderCorp: this.work.demanderCorp,
      dispatchTime: this.work.dispatchTime
    };
    this.httpClient.post('/api/anyfix/demander-cont/fee', params)
      .pipe(
        finalize(() => {
          this.feeRuleLoading = false;
        })
      ).subscribe((res: any) => {
      if( res.data) {
        this.contConfigData = res.data;
        this.isQueriedContConfig = true;
        this.imageList = res.data.feeRuleFileList || [];
      }
    });
  }
  // 查询工单收费规则列表
  queryAssortFeeList(reset?: boolean) {
    if (reset) {
      this.assortPage.pageNum = 1;
    }
    const params = {
      pageNum: this.assortPage.pageNum,
      pageSize: this.assortPage.pageSize,
      serviceCorp: this.work.serviceCorp,
      demanderCorp: this.work.demanderCorp
    };
    this.httpClient.post('/api/anyfix/work-fee-assort-define/query', params)
      .pipe(
        finalize(() => {
          this.assortFeeLoading = false;
        })
      ).subscribe((res: any) => {
      this.workFeeAssortList = res.data.list;
      this.assortPage.total = res.data.total;
    });
  }

  // 编辑工单费用
  editWorkFee() {
    const modal = this.modalService.create({
      nzTitle: '编辑工单费用',
      nzContent: WorkFeeEditComponent,
      nzComponentParams: {
        workId: this.work.workId,
        demanderCorp: this.work.demanderCorp
      },
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.refreshParent.emit();
        this.queryWorkDetail();
      }
    });
  }

  // 查询工单详情
  queryWorkDetail() {
    this.httpClient
      .get('/api/anyfix/work-request/detail/' + this.work.workId)
      .pipe(
        finalize(() => {
          this.spinning = false;
        })
      )
      .subscribe((res: any) => {
        this.work = res.data;
        this.assortFeeList = (this.work.workFeeDetailDtoList || []).filter((item: any) => item.feeType === 1);
        this.implementFeeList = (this.work.workFeeDetailDtoList || []).filter((item: any) => item.feeType === 2);
      });
  }

  // 查看工单收费明细
  viewAssortFeeDetail(feeId) {
    const modal = this.modalService.create({
      nzTitle: '工单收费规则',
      nzContent: WorkFeeRuleViewComponent,
      nzComponentParams: {assortId: feeId},
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterClose.subscribe((res: any) => {
    });
  }

  canServiceModFee() {
    return this.work.feeConfirmStatus !== this.workCheckService.CHECK_PASS &&
      this.aclRightIdList.includes(this.aclRight.WORK_FEE_MOD);
  }

  // 查看工单支出费用明细
  viewImplementFeeDetail(feeId) {
    const modal = this.modalService.create({
      nzTitle: '工单支出费用定义',
      nzContent: ImplementFeeViewComponent,
      nzComponentParams: {implementId: feeId},
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterClose.subscribe((res: any) => {
    });
  }

  getImageUrls(imageList: any[]) {
    const temp: string[] = [];
    imageList.forEach((fileId: any) => {
      temp.push(this.showFileUrl + fileId);
    });
    return temp;
  }

  change() {
    if(!this.showAssortFee) {
      return;
    }
    this.showAssortFee = false;
    this.resetData();
    this.appendAssortFee();
  }
  resetData() {
    this.isQueriedContConfig = false;
    this.workFeeAssortList = [];
  }
  /**
   * 费用能否审核
   */
  canFeeCheck() {
    return this.anyfixService.settleWorkStatusList.includes(this.work.workStatus)
      && (
        this.aclRightIdList.includes(this.aclRight.FEE_SERVICE_CHECK)
        || this.aclRightIdList.includes(this.aclRight.FEE_MANAGER_CHECK))
      && (this.work.feeConfirmStatus === 0
        || this.work.feeConfirmStatus === this.workCheckService.TO_CONFIRM
        || this.work.feeConfirmStatus === this.workCheckService.CONFIRM_REFUSE)
      && this.work.workFeeStatus !== this.workCheckService.TO_FILL
      && this.work.serviceCorp === this.userService.currentCorp.corpId;
  }

  /**
   * 费用审核
   */
  feeCheck() {
    this.httpClient.get('/api/anyfix/work-check/' + this.work.workId)
      .pipe(finalize(() => {
      })).subscribe((result: Result) => {
      const workCheck = result.data;
      const feeConfirmStatus = workCheck.feeConfirmStatus || this.workCheckService.TO_CONFIRM;
      if (feeConfirmStatus === this.workCheckService.CONFIRM_PASS) {
        this.messageService.error('费用已确认通过，不能再次审核！');
        return;
      }
      const modal = this.modalService.create({
        nzTitle: '费用审核',
        nzContent: FeeCheckComponent,
        nzFooter: null,
        nzWidth: 800,
        nzComponentParams: {
          workId: this.work.workId
        }
      });
      modal.afterClose.subscribe((res: any) => {
        if (res === 'submit') {
          this.refreshParent.emit();
        }
      });
    });
  }

  /**
   * 费用能否确认
   */
  canFeeConfirm(): boolean {
    return this.work.demanderCorp === this.userService.currentCorp.corpId
      && this.aclRightIdList.includes(this.aclRight.WORK_FEE_CONFIRM)
      && (this.work.finishConfirmStatus === this.workCheckService.CONFIRM_REFUSE
        || this.work.feeConfirmStatus === this.workCheckService.TO_CONFIRM)
      && this.anyfixService.settleWorkStatusList.includes(this.work.workStatus);
  }

  /**
   * 费用确认
   */
  feeConfirm() {
    this.httpClient.get('/api/anyfix/work-check/' + this.work.workId)
      .pipe(finalize(() => {
      })).subscribe((result: Result) => {
      const workCheck = result.data;
      const feeCheckStatus = workCheck.feeCheckStatus || this.workCheckService.TO_CHECK;
      const finishConfirmStatus = workCheck.finishConfirmStatus || this.workCheckService.TO_CONFIRM;
      let errorMsg = '';
      if (feeCheckStatus !== this.workCheckService.CHECK_PASS) {
        errorMsg = '费用未审核通过，不能确认！';
      }
      if (finishConfirmStatus !== this.workCheckService.CONFIRM_PASS) {
        errorMsg = '服务未确认通过，不能确认费用！';
      }
      if (errorMsg !== '') {
        this.messageService.error(errorMsg);
        return;
      }
      this.confirmWork();
    });
  }

  /**
   * 跳转到确认工单页面
   */
  confirmWork() {
    const modal = this.modalService.create({
      nzTitle: '费用确认',
      nzContent: FeeConfirmComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        workId: this.work.workId
      }
    });
    modal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.refreshParent.emit();
      }
    });
  }
}
