import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {ToAssignComponent} from '../../assign/to-assign/to-assign.component';
import {RecallAssignComponent} from '../../assign/recall-asssign/recall-assign.component';
import {DispatchComponent} from '../../dispatch/dispatch.component';
import {finalize} from 'rxjs/operators';
import {ServiceReturnComponent} from '../../handle/service-return/service-return.component';
import {environment} from '@env/environment';
import {AnyfixService} from '@core/service/anyfix.service';
import {CustomRecallComponent} from '../../handle/custom-recall/custom-recall.component';
import {SelectBranchComponent} from '../../handle/select-branch/select-branch.component';
import {ANYFIX_RIGHT} from '@core/right/right';
import {UsedPartEditComponent} from './used-part-edit/used-part-edit.component';
import {WorkPostEditComponent} from './work-post-edit/work-post-edit.component';
import {UserService} from '@core/user/user.service';
import {RecyclePartEditComponent} from './recycle-part-edit/recycle-part-edit.component';
import {WorkCheckService} from '../work-check.service';
import {ACLService} from '@delon/acl';
import {WorkSysTypeEnum} from '@core/service/enums.service';
import {ServiceCheckComponent} from './work-check/service-check.component';
import {FileEditComponent} from './file-edit/file-edit.component';
import {ServiceContentEditComponent} from './service-content-edit/service-content-edit.component';
import {ChatWindowComponent} from '@shared/components/chat-window/chat-window.component';
import {WorkSupportComponent} from '../../support/work-support.component';
import {WorkReviewListComponent} from '../../review/list/work-review-list.component';
import {WorkReviewAddComponent} from '../../review/add/work-review-add.component';
import {WorkSupportRecordAddComponent} from '../../support/add/work-support-record-add.component';
import {WorkFollowAddComponent} from '../../follow/work-follow-add.component';
import {Result} from '@core/interceptor/result';
import {WorkFeeDetailComponent} from './work-fee-detail/work-fee-detail.component';
import {ServiceConfirmComponent} from './work-confirm/service-confirm.component';
import {WorkFeeAddComponent} from './work-fee-add/work-fee-add.component';
import {ReturnAssignServiceComponent} from '../../assign/return-assign-service/return-assign-service.component';

@Component({
  selector: 'app-work-detail',
  templateUrl: './work-detail.component.html',
  styleUrls: ['./work-detail.component.less']
})
export class WorkDetailComponent implements OnInit {

  aclRight = ANYFIX_RIGHT;

  workId: any;
  demanderCorp: any;
  serviceCorp: any;
  work: any = {};
  label: string | undefined = '';
  showFileUrl = environment.server_url + '/api/file/showFile?fileId=';
  downFileUrl = environment.server_url + '/api/file/downloadFile?fileId=';
  // 工单评价信息
  workEvaluate: any;
  loading = true;

  // 自定义字段数据
  customFieldDataList: any = [];
  // 当前企业编号
  curCorpId = this.userService.currentCorp.corpId;
  // 工单操作按钮是否显示
  operateBtnVisible = false;
  // 编辑工单按钮是否显示
  editBtnVisible = false;

  canServiceModWork = false;
  canServiceModFee = false;
  canServiceModFinish = false;
  canModFinishFiles = false;
  aclRightIdList: any[] = [];
  serviceImageList: any = [];
  // 图片附件
  imgList: any = [];
  // 非图片附件
  fileList: any = [];
  // 维护类工单
  maintenance = WorkSysTypeEnum.MAINTENANCE;
  showFinishFile: boolean;
  isServiceCorp = false; // 判断当前企业是否是服务商
  isDemanderCorp = false; // 判断当前企业是否是委托商
  workIds = [];
  index = 0;
  canModGroupFiles = false;
  @ViewChild('chatwindow', {read: ChatWindowComponent, static: false}) chatwindow: ChatWindowComponent;

  // 工单是否关注
  canAttention = false;
  isAttention = false;
  isSupport = false;
  supportLength = 0;
  cols = 1;

  firstOpen = true;
  fileGroupList: any = [];
  @ViewChild(WorkReviewListComponent, {static: false}) workReview: WorkReviewListComponent;
  @ViewChild(WorkFeeDetailComponent, {static: false}) workFeeDetail: WorkFeeDetailComponent;
  // 分类费用明细列表
  assortFeeList: any[] = [];
  // 实施发生费用明细列表
  implementFeeList: any[] = [];
  serviceStandardLoading = false;
  finishCheckStatusLabel: string; // 工单服务审核状态表单名称
  finishCheckStatusName: string; // 工单服务审核状态名称
  finishCheckNote = ''; // 工单服务审核备注
  finishCheckBtnName = '审核'; // 工单服务审核按钮名称
  serviceStandardData: any = {};
  rightIds = this.aclService.data.abilities;

  selectedTabName: any; // 选中的tab页名称
  selectedTabIndex = 0; // 选中的tab页索引

  constructor(private httpClient: HttpClient,
              public router: Router,
              private activatedRoute: ActivatedRoute,
              private modalService: NzModalService,
              private cdf: ChangeDetectorRef,
              private msg: NzMessageService,
              public anyfixService: AnyfixService,
              private userService: UserService,
              public workCheckService: WorkCheckService,
              private activateRoute: ActivatedRoute,
              private messageService: NzMessageService,
              private aclService: ACLService) {
    this.workId = this.activatedRoute.snapshot.queryParams.workId;
    this.workIds = this.anyfixService.getParamsOnce() || [];
    this.index = this.workIds.indexOf(this.workId);
    this.selectedTabName = this.activatedRoute.snapshot.queryParams.selectedTabName;
  }

  ngOnInit() {
    this.aclRightIdList = this.aclService.data.abilities || [];
    this.query();
  }

  goBack() {
    history.go(-1);
  }

  prev() {
    if (this.index > 0) {
      this.index--;
      this.workId = this.workIds[this.index];
      this.query();
      this.chatwindow.nav(this.workId);
    }
  }

  next() {
    if (this.index < (this.workIds.length - 1)) {
      this.index++;
      this.workId = this.workIds[this.index];
      this.query();
      this.chatwindow.nav(this.workId);
    }
  }

  // 查询工单信息
  query() {
    this.loading = true;
    this.httpClient
      .get('/api/anyfix/work-request/detail/' + this.workId)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        if (res.data !== null) {
          this.work = res.data;
          // 刷新工单费用页面
          if (this.workFeeDetail) {
            this.workFeeDetail.work = this.work;
            this.workFeeDetail.initData();
            this.workFeeDetail.change();
          }
          this.demanderCorp = this.work.demanderCorp;
          this.serviceCorp = this.work.serviceCorp;
          if (this.rightIds.includes(ANYFIX_RIGHT.FINISH_SERVICE_CHECK)
            || this.rightIds.includes(ANYFIX_RIGHT.FINISH_MANAGER_CHECK)) {
            this.getServiceStandardData();
          }
          this.customFieldDataList = this.work.customFieldDataList;
          this.isAttention = this.work.isAttention;
          this.isSupport = this.work.isSupport;
          if (this.userService.currentCorp.corpId === this.serviceCorp) {
            this.isServiceCorp = true;
          }
          if (this.userService.currentCorp.corpId === this.demanderCorp) {
            this.isDemanderCorp = true;
          }
          if (this.work.workSupportList) {
            this.supportLength = this.work.workSupportList.length;
            this.work.workSupportList.reverse();
            this.work.workSupportList.forEach(item => {
              if (item.recordList) {
                item.recordList.reverse();
              }
            });
          } else {
            this.supportLength = 0;
          }
          if (this.work.workFollowList && this.work.workFollowList.length > 0) {
            this.work.workFollowList.reverse();
          }
          if (this.work.workFollowList && this.work.workFollowList.length > 0 && this.supportLength > 0 &&
            this.hasWorkSupportRight() && this.hasFollowRight()) {
            this.cols = 2;
          } else {
            this.cols = 1;
          }
          // 筛选附件种类
          this.imgFiltrate(this.work.fileList);
          if (this.work !== 'undefined') {
            switch (this.work.workStatus) {
              case this.anyfixService.TO_DISTRIBUTE:
                this.label = '提单';
                this.operateBtnVisible = true;
                break;
              case this.anyfixService.TO_HANDLE:
                this.label = '分配';
                this.operateBtnVisible = (this.curCorpId === this.work.serviceCorp);
                break;
              case this.anyfixService.TO_ASSIGN:
                this.label = '派单';
                this.operateBtnVisible = (this.curCorpId === this.work.serviceCorp);
                break;
              case this.anyfixService.TO_CLAIM:
                this.label = '撤回派单';
                this.operateBtnVisible = (this.curCorpId === this.work.serviceCorp);
                break;
              case this.anyfixService.TO_SIGN:
                this.label = '签到';
                break;
              case this.anyfixService.TO_SERVICE:
                this.label = '服务';
                break;
              case this.anyfixService.IN_SERVICE:
                this.label = '服务中';
                break;
              case this.anyfixService.TO_EVALUATE:
                this.label = '评价';
                break;
              case this.anyfixService.CLOSED:
                this.label = '已完成';
                break;
              case this.anyfixService.RETURNED:
                this.label = '已退单';
                break;
              case this.anyfixService.CANCELED:
                this.label = '已撤单';
                break;
            }
            this.work.trafficName = this.anyfixService.trafficMap[this.work.traffic] || '';
            // 修改操作记录
            this.work.workOperateList.forEach(item => {
              item.summary = item.summary.replace(/<br\/>/g, ' ');
              item.summary = item.summary.replace(/<br>/g, ' ');
              if (item.operateType === 150 && item.referId > 0) {
                const point = this.getPoint(item.referId);
                if (point && point.lon && point.lat) {
                  item.point = point;
                }
              }
            });
            this.canAttention = this.aclRightIdList.includes(this.aclRight.WORK_ATTENTION);
            this.canServiceModWork = this.canServiceModWorkCheck();
            this.canServiceModFee = this.canServiceModFeeCheck();
            this.canServiceModFinish = this.canServiceModFinishCheck();
            this.initEditBtnVisible();
            this.assortFeeList = (this.work.workFeeDetailDtoList || []).filter((item: any) => item.feeType === 1);
            this.implementFeeList = (this.work.workFeeDetailDtoList || []).filter((item: any) => item.feeType === 2);
            this.initFinishCheckStatusName();
            this.fileGroupList = this.work.fileGroupList || [];
            this.canModGroupFiles = this.canModeGroupFilesCheck();
            this.canModFinishFiles = this.canModFinishFilesCheck();
            this.showFinishFileCheck();
            this.initSelectedTabIndex();
          }
        }
      });
    // this.httpClient.get('/api/anyfix/work-evaluate/workId/' + this.workId)
    //   .subscribe((res: any) => {
    //     this.workEvaluate = res.data;
    //   });
    // 刷新客户回访记录
    if (this.hasWorkReviewRight()) {
      if (!this.firstOpen) {
        this.workReview.queryWorkReview(this.workId);
      } else {
        this.firstOpen = false;
      }
    }
  }

  /**
   * 初始化工单服务审核状态名称
   */
  initFinishCheckStatusName() {
    const workStatus = this.work.workStatus;
    const finishCheckStatus = this.work.finishCheckStatus;
    const finishConfirmStatus = this.work.finishConfirmStatus;
    // 服务商查看已完成的工单
    if (this.anyfixService.demanderCheckStatusList.includes(workStatus) && this.isServiceCorp) {
      this.finishCheckStatusLabel = '服务审核状态';
      if (finishCheckStatus === this.workCheckService.TO_CHECK) {
        this.finishCheckStatusName = '待审核';
      }
      if (finishCheckStatus === this.workCheckService.CHECK_REFUSE) {
        this.finishCheckBtnName = '重新审核';
        this.finishCheckStatusName = '审核不通过';
        this.finishCheckNote = this.work.finishCheckNote || '';
      }
      if (finishCheckStatus === this.workCheckService.CHECK_PASS) {
        this.finishCheckBtnName = '重新审核';
        this.finishCheckStatusName = '审核通过';
        this.finishCheckNote = this.work.finishCheckNote || '';
        if (finishConfirmStatus === this.workCheckService.TO_CONFIRM) {
          this.finishCheckStatusName += '待确认';
        }
        if (finishConfirmStatus === this.workCheckService.CONFIRM_REFUSE) {
          this.finishCheckStatusName = '确认不通过';
          this.finishCheckNote = this.work.finishConfirmNote || '';
        }
      }
      if (finishConfirmStatus === this.workCheckService.CONFIRM_PASS) {
        this.finishCheckStatusName = '确认通过';
        this.finishCheckNote = this.work.finishConfirmNote || '';
      }
    }
    // 委托商查看已完成的工单
    if (this.anyfixService.demanderCheckStatusList.includes(workStatus) && this.isDemanderCorp) {
      this.finishCheckStatusLabel = '服务确认状态';
      if (finishConfirmStatus === this.workCheckService.TO_CONFIRM) {
        this.finishCheckStatusName = '待确认';
      }
      if (finishConfirmStatus === this.workCheckService.CONFIRM_REFUSE) {
        this.finishCheckStatusName = '确认不通过';
        this.finishCheckNote = this.work.finishConfirmNote || '';
      }
      if (finishConfirmStatus === this.workCheckService.CONFIRM_PASS) {
        this.finishCheckStatusName = '确认通过';
        this.finishCheckNote = this.work.finishConfirmNote || '';
      }
    }
  }

  /**
   * 初始化编辑按钮显示
   */
  initEditBtnVisible() {
    this.work.resubmitWorkId = !this.work.resubmitWorkId ? '0' : this.work.resubmitWorkId;
    if ((this.work.workStatus === this.anyfixService.RETURNED && (this.work.finishConfirmStatus === 0
      || this.work.finishConfirmStatus === this.workCheckService.TO_CONFIRM)
      || this.work.workStatus === this.anyfixService.TO_DISTRIBUTE)
      && (this.userService.userInfo.userId === this.work.creator && this.aclRightIdList.includes(this.aclRight.WORK_ADD) ||
      this.aclRightIdList.includes(this.aclRight.WORK_MOD))
      // tslint:disable-next-line:triple-equals
      && this.work.resubmitWorkId == '0') {
      // 建单人在已退单状态可编辑工单
      this.editBtnVisible = true;
    } else if (this.work.workStatus !== this.anyfixService.RETURNED
      && this.work.workStatus !== this.anyfixService.CANCELED
      && this.work.workStatus !== this.anyfixService.TO_DISTRIBUTE
      && this.curCorpId === this.serviceCorp
      && this.aclRightIdList.includes(this.aclRight.WORK_MOD)) {
      // 服务商客服有权限则可编辑工单
      this.editBtnVisible = true;
    } else {
      this.editBtnVisible = false;
    }
  }

  /**
   * 初始化默认选中tab索引
   */
  initSelectedTabIndex() {
    let index = this.selectedTabIndex || 0;
    // 默认选中"工单费用"
    if (this.selectedTabName === 'workFee' && this.work.workFeeDto) {
      // 有支持跟进权限
      if (this.hasWorkSupportRight() || this.hasFollowRight()) {
        index = 4;
      } else {
        index = 3;
      }
    }
    this.selectedTabIndex = index;
  }

  operate(label) {
    switch (label) {
      case '提单':
        this.toDispatch();
        break;
      case '分配':
        this.toHandle();
        break;
      case '派单':
        this.toAssign();
        break;
      case '撤回派单':
        this.toRecallAssign();
        break;
    }
  }

  // 进入工单提单页面
  toDispatch(): void {
    const modal = this.modalService.create({
      nzTitle: '提单',
      nzContent: DispatchComponent,
      nzFooter: null,
      nzWidth: 850,
      nzComponentParams: {
        workId: this.workId,
        demanderCorp: this.demanderCorp,
        isRadio: true
      }
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        if (result.code === 0) {
          this.modalService.success({
            nzTitle: '工单提单成功',
            nzContent: '等待服务商受理。'
          });
          this.query();
        }
      }
    });
  }

  // 进入工单分配页面
  toHandle(): void {
    const modal = this.modalService.create({
      nzTitle: '分配',
      nzContent: SelectBranchComponent,
      nzStyle: {'margin-top': '40px'},
      nzFooter: null,
      nzWidth: 1100,
      nzComponentParams: {
        url: '/api/anyfix/work-transfer/service/handle',
        message: '分配',
        workId: this.workId,
        serviceCorp: this.serviceCorp,
        branchName: this.work.serviceBranchName,
        isRadio: true
      }
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        if (result.code === 0) {
          this.modalService.success({
            nzTitle: '工单分配成功',
            nzContent: '等待派单.....'
          });
          this.query();
        }
      }
    });
  }

  // 进入工单转处理页面
  turnHandleWork(): void {
    const modal = this.modalService.create({
      nzTitle: '转处理',
      nzContent: SelectBranchComponent,
      nzFooter: null,
      nzWidth: 1000,
      nzComponentParams: {
        url: '/api/anyfix/work-transfer/service/turn/handle',
        message: '转处理',
        workId: this.workId,
        serviceCorp: this.serviceCorp,
        isRadio: true
      }
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        if (result.code === 0) {
          this.modalService.success({
            nzTitle: '工单转处理成功',
            nzContent: '等待派单.....'
          });
          this.query();
        }
      }
    });
  }

  // 进入客户撤单页面
  customRecall(): void {
    const modal = this.modalService.create({
      nzTitle: '请选择撤单原因 :',
      nzContent: CustomRecallComponent,
      nzFooter: null,
      nzWidth: 500,
      nzComponentParams: {
        workId: this.workId
      }
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        if (result.code === 0) {
          this.modalService.success({
            nzTitle: '客户撤单成功',
            nzContent: '如还有需要，请联系客服.....'
          });
          this.query();
        }
      }
    });
  }

  // 进入客服退单页面
  serviceReturn(): void {
    const modal = this.modalService.create({
      nzTitle: '请选择退单原因:',
      nzContent: ServiceReturnComponent,
      nzFooter: null,
      nzWidth: 500,
      nzComponentParams: {
        workId: this.workId
      }
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        if (result.code === 0) {
          this.modalService.success({
            nzTitle: '退回委托商成功',
            nzContent: '请通知客户.....'
          });
          this.query();
        }
      }
    });
  }

  // 进入派单主管退单页面
  returnAssignByService(): void {
    const modal = this.modalService.create({
      nzTitle: '请选择退单原因:',
      nzContent: ReturnAssignServiceComponent,
      nzFooter: null,
      nzWidth: 500,
      nzComponentParams: {
        workId: this.workId
      }
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        if (result.code === 0) {
          this.modalService.success({
            nzTitle: '服务网点退回成功',
            nzContent: '请通知客服.....'
          });
          this.query();
        }
      }
    });
  }

  // 服务商客服派工
  toAssign() {
    const modal = this.modalService.create({
      nzTitle: '工单派工',
      nzContent: ToAssignComponent,
      nzFooter: null,
      nzWidth: 500,
      nzComponentParams: {
        workId: this.workId,
        serviceBranch: {
          branchId: this.work.serviceBranch,
          branchName: this.work.serviceBranchName
        }
      }
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        if (result.code === 0) {
          this.query();
        }
      }
    });
  }

  // 服务商客服撤回派单
  toRecallAssign() {
    const modal = this.modalService.create({
      nzTitle: '撤回派单原因',
      nzContent: RecallAssignComponent,
      nzWidth: 350,
      nzFooter: null,
      nzComponentParams: {
        workId: this.workId
      }
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        if (result.code !== null && result.code !== undefined) {
          this.query();
          this.msg.success('撤回派单成功');
        } else {
          this.msg.error('撤回派单失败，请重试');
        }
      }
    });
  }

  /**
   * 服务能否审核
   */
  canServiceCheck() {
    return this.anyfixService.settleWorkStatusList.includes(this.work.workStatus)
      && (
        this.aclRightIdList.includes(this.aclRight.FINISH_SERVICE_CHECK)
        || this.aclRightIdList.includes(this.aclRight.FINISH_MANAGER_CHECK))
      && (this.work.finishConfirmStatus === 0
        || this.work.finishConfirmStatus === this.workCheckService.TO_CONFIRM
        || this.work.finishConfirmStatus === this.workCheckService.CONFIRM_REFUSE)
      && this.work.serviceCorp === this.curCorpId;
  }

  /**
   * 服务审核
   */
  serviceCheck() {
    this.httpClient.get('/api/anyfix/work-check/' + this.workId)
      .pipe(finalize(() => {
      })).subscribe((result: Result) => {
      const workCheck = result.data;
      const finishConfirmStatus = workCheck.finishConfirmStatus || this.workCheckService.TO_CONFIRM;
      if (finishConfirmStatus === this.workCheckService.CONFIRM_PASS) {
        this.messageService.error('服务已确认通过，不能再次审核！');
        return;
      }
      const modal = this.modalService.create({
        nzTitle: '服务审核',
        nzContent: ServiceCheckComponent,
        nzFooter: null,
        nzWidth: 800,
        nzComponentParams: {
          workId: this.workId
        }
      });
      modal.afterClose.subscribe((res: any) => {
        if (res === 'submit') {
          this.query();
        }
      });
    });
  }

  /**
   * 服务能否确认
   */
  canServiceConfirm() {
    return this.anyfixService.settleWorkStatusList.includes(this.work.workStatus)
      && this.aclRightIdList.includes(this.aclRight.WORK_FINISH_CONFIRM)
      && (this.work.finishConfirmStatus === 0
        || this.work.finishConfirmStatus === this.workCheckService.TO_CONFIRM)
      && this.work.demanderCorp === this.curCorpId;
  }

  /**
   * 服务确认
   */
  serviceConfirm() {
    this.httpClient.get('/api/anyfix/work-check/' + this.workId)
      .pipe(finalize(() => {
      })).subscribe((result: Result) => {
      const workCheck = result.data;
      const finishCheckStatus = workCheck.finishCheckStatus || this.workCheckService.TO_CHECK;
      const finishConfirmStatus = workCheck.finishConfirmStatus || this.workCheckService.TO_CONFIRM;
      if (finishCheckStatus !== this.workCheckService.CHECK_PASS) {
        this.messageService.error('服务未审核通过，不能确认！');
        return;
      }
      if (finishConfirmStatus === this.workCheckService.CONFIRM_PASS) {
        this.messageService.error('服务已确认通过，不能再次确认！');
        return;
      }
      if (finishConfirmStatus === this.workCheckService.CONFIRM_REFUSE) {
        this.messageService.error('服务已确认不通过，不能再次确认！');
        return;
      }
      const modal = this.modalService.create({
        nzTitle: '服务确认',
        nzContent: ServiceConfirmComponent,
        nzFooter: null,
        nzWidth: 800,
        nzComponentParams: {
          workId: this.workId
        }
      });
      modal.afterClose.subscribe((res: any) => {
        if (res === 'submit') {
          this.query();
        }
      });
    });
  }

  /**
   * 退单能否确认
   */
  canWorkReturnConfirm() {
    return this.work.workStatus === this.anyfixService.RETURNED
      && this.aclRightIdList.includes(this.aclRight.WORK_FINISH_CONFIRM)
      && (this.work.finishConfirmStatus === 0
        || this.work.finishConfirmStatus === this.workCheckService.TO_CONFIRM)
      && this.work.demanderCorp === this.curCorpId;
  }

  /**
   * 退单确认
   */
  workReturnConfirm() {
    this.httpClient.get('/api/anyfix/work-check/' + this.workId)
      .pipe(finalize(() => {
      })).subscribe((result: Result) => {
      const workCheck = result.data;
      const finishConfirmStatus = workCheck.finishConfirmStatus || this.workCheckService.TO_CONFIRM;
      if (finishConfirmStatus === this.workCheckService.CONFIRM_PASS) {
        this.messageService.error('工单已确认，不能再次确认！');
        return;
      }
      this.modalService.confirm({
        nzOkText: '确定',
        nzCancelText: '取消',
        nzContent: '是否确认工单？',
        nzOnOk: () => {
          this.loading = true;
          const params = {
            workId: this.workId,
            finishConfirmStatus: this.workCheckService.CONFIRM_PASS
          };
          this.httpClient.post('/api/anyfix/work-check/confirm', params).pipe(finalize(() => {
            this.loading = false;
          })).subscribe((res: any) => {
            this.messageService.success('确认成功！');
            this.query();
          });
        }
      });
    });
  }

  // 添加使用部件
  addUsedPart() {
    const modal = this.modalService.create({
      nzTitle: '添加使用部件',
      nzContent: UsedPartEditComponent,
      nzComponentParams: {
        type: 'add',
        usedPart: {workId: this.work.workId}
      },
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.query();
      }
    });
  }

  // 编辑使用部件
  editUsedPart(usedPart) {
    const modal = this.modalService.create({
      nzTitle: '编辑使用部件',
      nzContent: UsedPartEditComponent,
      nzComponentParams: {
        type: 'mod',
        usedPart
      },
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.query();
      }
    });
  }

  // 删除使用部件
  delUsedPart(usedId) {
    this.modalService.confirm({
      nzTitle: '确认删除该使用部件？',
      nzOkText: '确定',
      nzCancelText: '取消',
      nzOnOk: instance => {
        this.httpClient.delete(`/api/anyfix/work-ware/used/${usedId}`)
          .subscribe((res: any) => {
            this.messageService.success('使用部件删除成功！');
            this.query();
          });
      }
    });
  }

  // 添加回收部件
  addRecyclePart() {
    const modal = this.modalService.create({
      nzTitle: '添加回收部件',
      nzContent: RecyclePartEditComponent,
      nzComponentParams: {
        type: 'add',
        recyclePart: {workId: this.workId}
      },
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.query();
      }
    });
  }

  // 编辑回收部件
  editRecyclePart(recyclePart) {
    const modal = this.modalService.create({
      nzTitle: '编辑回收部件',
      nzContent: RecyclePartEditComponent,
      nzComponentParams: {
        type: 'mod',
        recyclePart
      },
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.query();
      }
    });
  }

  // 删除回收部件
  deleteRecyclePart(recycleId) {
    this.modalService.confirm({
      nzTitle: '确认删除该回收部件？',
      nzOkText: '确定',
      nzCancelText: '取消',
      nzOnOk: instance => {
        this.httpClient.delete(`/api/anyfix/work-ware/recycle/${recycleId}`)
          .subscribe((res: any) => {
            this.messageService.success('回收部件删除成功！');
            this.query();
          });
      }
    });
  }

  // 添加邮寄单
  addWorkPost() {
    const modal = this.modalService.create({
      nzTitle: '添加回收部件邮寄单',
      nzContent: WorkPostEditComponent,
      nzComponentParams: {
        type: 'add',
        workPost: {workId: this.workId}
      },
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.query();
      }
    });
  }

  // 编辑邮寄单
  editWorkPost(workPost) {
    const modal = this.modalService.create({
      nzTitle: '编辑回收部件邮寄单',
      nzContent: WorkPostEditComponent,
      nzComponentParams: {
        type: 'mod',
        workPost
      },
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.query();
      }
    });
  }

  // 删除邮寄单
  delWorkPost(postId) {
    this.modalService.confirm({
      nzTitle: '确认删除该邮寄单？',
      nzOkText: '确定',
      nzCancelText: '取消',
      nzOnOk: instance => {
        this.httpClient.delete(`/api/anyfix/work-post/${postId}`)
          .subscribe((res: any) => {
            this.messageService.success('邮寄单删除成功！');
            this.query();
          });
      }
    });
  }

  // 能否进行服务商的更改工单操作
  canServiceModWorkCheck(): boolean {
    return this.work.serviceCorp === this.curCorpId &&
      this.work.finishCheckStatus !== this.workCheckService.CHECK_PASS &&
       this.work.feeCheckStatus !== this.workCheckService.CHECK_PASS &&
      (this.aclRightIdList.includes(this.aclRight.WORK_MOD)
        || this.work.finishCheckStatus !== this.workCheckService.CHECK_PASS
        && this.work.engineer === this.userService.userInfo.userId);
  }

  // 能否进行服务商的更改费用操作
  canServiceModFeeCheck(): boolean {
    return this.work.serviceCorp === this.curCorpId &&
      this.work.feeConfirmStatus !== this.workCheckService.CHECK_PASS &&
      // (this.work.workStatus === this.anyfixService.TO_EVALUATE || this.work.workStatus === this.anyfixService.CLOSED) &&
      (this.aclRightIdList.includes(this.aclRight.WORK_FEE_MOD)
        || this.work.feeCheckStatus !== this.workCheckService.CHECK_PASS
        && this.work.engineer === this.userService.userInfo.userId);
  }

  // 工程师能否填写费用
  canAddWorkFee(): boolean {
    return this.work.serviceCorp === this.curCorpId &&
      (this.work.workStatus === this.anyfixService.CLOSED || this.work.workStatus === this.anyfixService.TO_EVALUATE) &&
      this.work.engineer === this.userService.userInfo.userId &&
      (this.work.workFeeStatus === 1 || this.work.feeCheckStatus === 3);
  }

  // 工程师填写费用
  addWorkFee() {
    const modal = this.modalService.create({
      nzTitle: '填写工单费用',
      nzContent: WorkFeeAddComponent,
      nzComponentParams: {
        workId: this.work.workId
      },
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.query();
      }
    });
  }

  // 能否编辑完成信息
  canServiceModFinishCheck(): boolean {
    return this.work.serviceCorp === this.curCorpId &&
      this.work.finishCheckStatus !== this.workCheckService.CHECK_PASS &&
      (this.work.workStatus === this.anyfixService.CLOSED || this.work.workStatus === this.anyfixService.IN_SERVICE) &&
      (this.aclRightIdList.includes(this.aclRight.WORK_MOD)
        || this.work.finishCheckStatus !== this.workCheckService.CHECK_PASS
        && this.work.engineer === this.userService.userInfo.userId);
  }

  // 编辑工单
  editWork(workId) {
    this.anyfixService.setParamsOnce(this.workIds);
    this.router.navigate(['../work-detail/edit'],
      {queryParams: {workId}, relativeTo: this.activatedRoute});
  }

  getParams() {
    return {workId: this.workId};
  }

  copy(event) {
    event.stopPropagation();
    event.preventDefault();
  }

  getWidth(length) {
    if (length === 2) {
      return '60px';
    } else if (length === 3) {
      return '70px';
    } else {
      return '80px';
    }
  }

  // 筛选附件种类
  imgFiltrate(List) {
    this.imgList = [];
    this.fileList = [];
    List.forEach((file: any) => {
      // 获取后缀
      const index = file.fileName.lastIndexOf('.');
      const ext = file.fileName.substr(index + 1);
      if (this.isAssetTypeAnImage(ext)) {
        this.imgList.push(file);
      } else {
        this.fileList.push(file);
      }
    });
  }

  getImageUrls(imageList: any[], justId: boolean = false) {
    const temp: string[] = [];
    if (justId) {
      imageList.forEach((file: any) => {
        temp.push(this.showFileUrl + file);
      });
    } else {
      imageList.forEach((file: any) => {
        temp.push(this.showFileUrl + file.fileId);
      });
    }

    return temp;
  }

  getImageUrlsByFileIdList(imageList: any[]) {
    imageList = imageList || [];
    const temp: string[] = [];
    imageList.forEach((file: any) => {
      temp.push(this.showFileUrl + file.fileId);
    });
    return temp;
  }


  // 判断是否是图片
  isAssetTypeAnImage(ext) {
    return [
      'png', 'jpg', 'jpeg', 'bmp', 'gif', 'webp', 'psd', 'svg', 'tiff'].indexOf(ext.toLowerCase()) !== -1;
  }

  // 跳转到设备详情里面
  serialDetail(deviceId) {
    this.anyfixService.setParamsOnce(this.workIds);
    if (deviceId !== undefined && deviceId !== null && deviceId > 0) {
      this.router.navigate(['/device/device-info/device-detail'], {
        relativeTo: this.activateRoute,
        queryParams: {deviceId}
      });
    }
  }

  // 修改服务内容
  editServiceContent() {
    const modal = this.modalService.create({
      nzTitle: '修改服务内容',
      nzContent: ServiceContentEditComponent,
      nzComponentParams: {
        work: {
          workId: this.work.workId,
          workStatus: this.work.workStatus,
          serviceBranch: {
            branchId: this.work.serviceBranch,
            branchName: this.work.serviceBranchName
          },
          traffic: this.work.traffic,
          trafficNote: this.work.trafficNote,
          goTime: this.work.goTime,
          signTime: this.work.signTime,
          startTime: this.work.startTime,
          endTime: this.work.endTime,
          togetherEngineerList: this.work.togetherEngineerList,
          helpNames: this.work.helpNames,
          finishDescription: this.work.finishDescription
        }
      },
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.query();
      }
    });
  }

  // 修改附件
  editFiles(configId = null, groupId = null, groupName: string = '') {
    const title = groupName === '' ? '修改上传附件' : '修改' + groupName;
    const modal = this.modalService.create({
      nzTitle: title,
      nzContent: FileEditComponent,
      nzComponentParams: {
        workId: this.work.workId,
        configId,
        groupId,
        workType: this.work.workSysType,
        demanderCorp: this.work.demanderCorp,
        serviceCorp: this.work.serviceCorp
      },
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.query();
      }
    });
  }

  addAttention() {
    this.loading = true;
    const params = {
      workId: this.work.workId,
      corpId: this.curCorpId,
      operateType: 'Y'
    };
    this.httpClient.post(`/api/anyfix/work-attention/add`, params)
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      ).subscribe((res: any) => {
      this.msg.success('工单关注成功');
      this.isAttention = true;
      this.query();
    });
  }

  delAttention() {
    this.loading = true;
    this.httpClient.post(`/api/anyfix/work-attention/delWorkAttention`, {workId: this.work.workId})
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      ).subscribe((res: any) => {
      this.msg.success('工单取消关注成功');
      this.isAttention = false;
      this.query();
    });
  }

  // 添加客户回访
  addWorkReview() {
    const modal = this.modalService.create({
      nzTitle: '回访客户',
      nzContent: WorkReviewAddComponent,
      nzComponentParams: {
        workId: this.workId
      },
      nzFooter: null,
      nzMaskClosable: false,
      nzWidth: '700',
      nzStyle: {'margin-top': '-40px'}
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.query();
      }
    });
  }


  hasWorkReviewRight(): boolean {
    return this.aclRightIdList.includes(this.aclRight.WORK_REVIEW);
  }

  hasWorkReviewAddRight(): boolean {
    return this.aclRightIdList.includes(this.aclRight.WORK_REVIEW_ADD);
  }

  // 技术支持
  toWorkSupport() {
    const modal = this.modalService.create({
      nzTitle: '技术支持',
      nzContent: WorkSupportComponent,
      nzWidth: 600,
      nzFooter: null,
      nzComponentParams: {
        work: {
          workId: this.work.workId,
          serviceBranch: {
            branchId: this.work.serviceBranch,
            branchName: this.work.serviceBranchName
          }
        }
      }
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        if (result.code === 0) {
          this.query();
          this.msg.success('转技术支持成功');
        } else if (result.code === 1) {
          this.query();
        }
      }
    });
  }

  // 添加技术支持跟踪记录
  addWorkSupportRecord(support) {
    const modal = this.modalService.create({
      nzTitle: '添加支持记录',
      nzContent: WorkSupportRecordAddComponent,
      nzComponentParams: {
        supportId: support.id,
        severityName: support.severityName,
        description: support.description
      },
      nzFooter: null,
      nzMaskClosable: false,
      nzWidth: '700',
      nzStyle: {'margin-top': '-40px'}
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.query();
        this.msg.success('添加支持记录成功');
      } else if (result === 'close') {
        this.query();
        this.msg.success('关闭技术支持成功');
      } else if (result.code === 1) {
        this.query();
      }
    });
  }

  hasWorkSupportRight(): boolean {
    return this.aclRightIdList.includes(this.aclRight.WORK_SUPPORT);
  }

  // 添加跟进记录
  addFollow() {
    const modal = this.modalService.create({
      nzTitle: '添加跟进记录',
      nzContent: WorkFollowAddComponent,
      nzWidth: 600,
      nzFooter: null,
      nzComponentParams: {
        work: {
          workId: this.work.workId
        }
      }
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        if (result.code === 0) {
          this.query();
          this.msg.success('添加跟进记录成功');
        }
      }
    });
  }

  // 获取显示在地图上的经纬度
  getPoint(id) {
    const workSignList = this.work.workSignList.filter(item => item.signId === id);
    if (workSignList.length > 0) {
      return workSignList[0];
    } else {
      // this.messageService.presentAlert('未获取到定位');
      return null;
    }
  }


  canAddSupport(): boolean {
    return this.anyfixService.workSupportStatusList.includes(this.work.workStatus)
      && this.aclRightIdList.includes(this.aclRight.WORK_SUPPORT_ADD);
  }

  canAddSupportRecord(): boolean {
    return this.anyfixService.workSupportStatusList.includes(this.work.workStatus)
      && this.aclRightIdList.includes(this.aclRight.WORK_SUPPORT_RECORD_ADD);
  }

  hasFollowRight(): boolean {
    return this.aclRightIdList.includes(this.aclRight.WORK_FOLLOW);
  }

  canAddFollow(): boolean {
    return this.aclRightIdList.includes(this.aclRight.WORK_FOLLOW_ADD);
  }

  getServiceStandardData() {
    if (this.work.serviceCorp === '0' || this.work.demanderCorp === '0' ||  this.work.dispatchTime == null) {
      return;
    }
    this.serviceStandardLoading = true;
    const params = {
      serviceCorp: this.work.serviceCorp,
      demanderCorp: this.work.demanderCorp,
      dispatchTime: this.work.dispatchTime
    };
    this.httpClient.post('/api/anyfix/demander-cont/service', params)
      .pipe(
        finalize(() => {
          this.serviceStandardLoading = false;
        })
      ).subscribe((res: any) => {
        if( res.data ) {
          this.serviceStandardData = res.data || {};
          this.serviceImageList = res.data.serviceStandardFileList || [];
        }
    });
  }

  canModeGroupFilesCheck() {
    return this.work.workStatus === this.anyfixService.CLOSED
      && ((this.work.filesStatus !== '1' && this.work.engineer === this.userService.userInfo.userId) ||
        this.canServiceModFinish)
      && this.work.serviceMode === 1
      && this.work.serviceCorp === this.curCorpId
      && this.work.finishCheckStatus !== this.workCheckService.CHECK_PASS;
  }

  showFinishFileCheck() {
    const finishFileList = this.work.finishFileList || [];
    // 如果有完成附件，则显示完成附件
    if (finishFileList.length > 0) {
      this.showFinishFile = true;
      // 如果完成附件没数据，无配置，显示完成附件
    } else if (this.fileGroupList.length < 1) {
      this.showFinishFile = true;
    }
  }

  canModFinishFilesCheck() {
    return this.canServiceModFinish && this.work.filesStatus !== '1';
  }
}
