import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {ANYFIX_RIGHT} from '@core/right/right';
import {WorkFeeVerifyConfirmComponent} from '../confirm/work-fee-verify-confirm.component';
import {finalize} from 'rxjs/operators';
import {Page} from '@core/interceptor/result';
import {WorkFeeVerifyModComponent} from '../mod/work-fee-verify-mod.component';
import {ImportVerifyComponent} from '../import-verify/import-verify.component';
import {DemanderContNoComponent} from '../../../settle/settle-demander/demander-cont-no/demander-cont-no.component';

@Component({
  selector: 'app-work-fee-verify-detail',
  templateUrl: './work-fee-verify-detail.component.html',
  styleUrls: ['./work-fee-verify-detail.component.less']
})
export class WorkFeeVerifyDetailComponent implements OnInit {

  // 对账单编号
  verifyId: string;
  // 对账单
  workFeeVerify: any = {};
  // 对账单明细
  detailList: any[] = [];
  // 当前企业编号
  corpId = this.userService.currentCorp.corpId;
  // 分页
  page = new Page();
  // 工单管理权限
  aclRight = ANYFIX_RIGHT;
  // 加载中
  loading = false;
  // 明细表格加载中
  tableLoading = false;
  // 左边是否收起
  isCollapsed = false;

  constructor(
    public httpClient: HttpClient,
    public activatedRoute: ActivatedRoute,
    public router: Router,
    public modalService: NzModalService,
    public messageService: NzMessageService,
    public userService: UserService
  ) {
    this.verifyId = this.activatedRoute.snapshot.queryParams.verifyId;
  }

  ngOnInit() {
    this.findDetail();
    this.queryDetailList(true);
  }

  // 获取对账单详情
  findDetail() {
    this.loading = true;
    this.httpClient.get(`/api/anyfix/work-fee-verify/${this.verifyId}`)
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      )
      .subscribe((res: any) => {
        this.workFeeVerify = res.data;
      });
  }

  // 分页查询对账单明细
  queryDetailList(reset?: boolean) {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.tableLoading = true;
    this.httpClient.post('/api/anyfix/work-fee-verify-detail/query', this.getPageParams())
      .pipe(
        finalize(() => {
          this.tableLoading = false;
        })
      ).subscribe((res: any) => {
        this.detailList = res.data.list || [];
        this.page.total = res.data.total || 0;
    })
  }

  getPageParams() {
    const params = {
      pageNum: this.page.pageNum,
      pageSize: this.page.pageSize,
      verifyId: this.verifyId
    };
    return params;
  }

  // 查看委托协议
  showContNo() {
    const modal = this.modalService.create({
      nzTitle: '查看委托协议',
      nzContent: DemanderContNoComponent,
      nzWidth: 800,
      nzComponentParams: {
        contId: this.workFeeVerify.contId
        // serviceCorp: this.workFeeVerify.serviceCorp,
        // demanderCorp: this.workFeeVerify.demanderCorp
      },
      nzFooter: null
    });
  }

  // 返回
  goBack() {
    history.back();
  }

  // 查看工单详情
  toWorkDetail(workId) {
    this.router.navigate(['../../work-detail'], {relativeTo: this.activatedRoute, queryParams: {workId}});
  }

  // 提交对账
  submit() {
    const uncheckNum = this.workFeeVerify.workQuantity - this.workFeeVerify.checkNum;
    if (uncheckNum > 0) {
      this.messageService.error('还有 ' + uncheckNum + ' 工单费用未审核通过，不能提交对账');
      return;
    }
    this.modalService.confirm({
      nzTitle: '确认提交委托商对账',
      nzOkText: '确定',
      nzCancelText: '取消',
      nzOnOk: instance => {
        this.loading = true;
        this.httpClient.post('/api/anyfix/work-fee-verify/submit', this.workFeeVerify)
          .pipe(
            finalize(() => {
              this.loading = true;
            })
          )
          .subscribe((res: any) => {
            this.messageService.success('提交对账成功！');
            this.findDetail();
            this.queryDetailList(true);
          })
      }
    })
  }

  // 打开对账页面
  verify() {
    this.router.navigate(['../verify'], {queryParams: {verifyId: this.verifyId}, relativeTo: this.activatedRoute});
  }

  // 打开导入对账页面
  importVerify() {
    const modal = this.modalService.create({
      nzTitle: '导入对账',
      nzWidth: 800,
      nzContent: ImportVerifyComponent,
      nzComponentParams: {verifyId: this.verifyId},
      nzFooter: null
    });
    modal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        // 刷新页面
        this.findDetail();
        this.queryDetailList(true);
      }
    });
  }

  // 打开修改页面
  mod() {
    const modal = this.modalService.create({
      nzTitle: '修改',
      nzWidth: 800,
      nzContent: WorkFeeVerifyModComponent,
      nzComponentParams: {verifyId: this.verifyId},
      nzFooter: null
    });
    modal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        // 刷新页面
        this.findDetail();
        this.queryDetailList(true);
      }
    });
  }

  // 打开确认页面
  confirm() {
    const modal = this.modalService.create({
      nzTitle: '确认',
      nzWidth: 800,
      nzContent: WorkFeeVerifyConfirmComponent,
      nzComponentParams: {workFeeVerify: this.workFeeVerify},
      nzFooter: null
    });
    modal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        // 刷新页面
        this.findDetail();
        this.queryDetailList(true);
      }
    });
  }

  // 弹出删除确认框
  deleteConfirm() {
    this.modalService.confirm({
      nzTitle: '确定删除该对账单吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.delete(this.verifyId),
      nzCancelText: '取消'
    });
  }

  // 删除
  delete(verifyId) {
    this.httpClient.delete(`/api/anyfix/work-fee-verify/${verifyId}`)
      .subscribe((res: any) => {
        this.messageService.success('删除成功！');
        history.back();
      });
  }

}
