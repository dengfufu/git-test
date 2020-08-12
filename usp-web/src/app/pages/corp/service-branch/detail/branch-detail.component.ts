import {ChangeDetectorRef, Component, EventEmitter, OnInit, Output} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {NzModalService} from 'ng-zorro-antd';
import {ServiceBranchService} from '../sevice-branch.service';
import {Page, Result} from '@core/interceptor/result';
import {ANYFIX_RIGHT} from '@core/right/right';
import {BranchEditComponent} from '../edit/branch-edit.component';
import {finalize} from 'rxjs/operators';
import {CorpUserSelectorComponent} from '../../user/selector/corp-user-selector.component';
import {FormBuilder, FormGroup} from '@angular/forms';

@Component({
  selector: 'app-corp-service-branch-detail',
  templateUrl: 'branch-detail.component.html',
  styleUrls: ['branch-detail.component.less']
})
export class BranchDetailComponent implements OnInit {

  @Output() private deleteBranchChange = new EventEmitter<string>();

  searchForm: FormGroup;

  aclRight = ANYFIX_RIGHT;

  serviceBranch: any = {};
  branchUserList: any[] = [];
  corpUserList: any[] = [];

  page = new Page();
  loading = false;

  drawerVisible = false;
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private modalService: NzModalService,
              private cdf: ChangeDetectorRef,
              private router: Router,
              private serviceBranchService: ServiceBranchService) {
    this.searchForm = this.formBuilder.group({
      userId: []
    });
    this.serviceBranchService.serviceBranch$.subscribe((res: any) => {
      this.serviceBranch = res.data;
    });
    this.serviceBranchService.branchUserList$.subscribe((res: any) => {
      this.branchUserList = res;
    });
    this.serviceBranchService.page$.subscribe((res: any) => {
      this.page = res;
    });
    if (!this.serviceBranch) {
      this.serviceBranch = {};
    }
  }

  ngOnInit() {
    this.matchCorpUser();
  }

  /**
   * 查询网点人员
   * @param reset 是否重置
   */
  queryBranchUser(reset?: boolean) {
    this.serviceBranchService.listBranchUser(this.getParams(), reset).then((res: any) => {
      this.branchUserList = res.data.list;
      this.page.total = res.data.total;
    });
  }

  getParams() {
    const params: any = {};
    if (this.searchForm) {
      Object.assign(params, this.searchForm.value);
    }
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    params.branchId = this.serviceBranch.branchId;
    return params;
  }

  /**
   * 编辑网点
   * @param id 网点编号
   */
  editBranch(id) {
    const modal = this.modalService.create({
      nzTitle: '编辑服务网点',
      nzContent: BranchEditComponent,
      nzFooter: null,
      nzMaskClosable: false,
      nzWidth: 800,
      nzComponentParams: {
        branchId: id
      }
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.serviceBranchService.loadBranchDetail(id).then(() => {
        });
        this.serviceBranchService.queryBranchUser(this.getParams(), true).then(() => {
        });
      }
    });
  }

  // 删除确认
  deleteBranch(branchId): void {
    this.modalService.confirm({
      nzTitle: '确定删除该网点吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.doDeleteBranch(branchId),
      nzCancelText: '取消'
    });
  }

  /**
   * 删除操作
   * @param id 网点编号
   */
  doDeleteBranch(id) {
    this.httpClient
      .delete('/api/anyfix/service-branch/' + id)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        if (result.code === 0) {
          this.deleteBranchChange.emit('submit');
        }
      });
  }

  // 删除确认
  deleteBranchUser(userId): void {
    this.modalService.confirm({
      nzTitle: '确定删除该人员吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.doDeleteBranchUser(userId),
      nzCancelText: '取消'
    });
  }

  /**
   * 删除网点人员操作
   * @param userId 人员编号
   */
  doDeleteBranchUser(userId) {
    this.httpClient
      .delete('/api/anyfix/service-branch-user/' + this.serviceBranch.branchId + '/' + userId)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        // 如果是为第n页第一条，需要调整为n-1页才有数据显示
        if (this.branchUserList.length === 1 && this.page.pageNum > 0) {
          this.page.pageNum = this.page.pageNum - 1;
        }
        this.queryBranchUser();
      });
  }

  /**
   * 添加网点人员
   */
  addBranchUser(): void {
    const modal = this.modalService.create({
      nzTitle: '选择人员',
      nzContent: CorpUserSelectorComponent,
      nzFooter: null,
      nzWidth: 800,
      nzStyle: {'margin-top': '-40px'},
      nzComponentParams: {
        params: {
          urlParams: {branchId: this.serviceBranch.branchId},
          url: '/api/anyfix/service-branch-user/add',
          fromUri: 'service-branch-user'
        }
      }
    });
    modal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.queryBranchUser();
      }
    });
  }


  matchCorpUser(filter?) {
    const params = {corpId: this.serviceBranch.serviceCorp, matchFilter: filter};
    this.httpClient
      .post('/api/uas/corp-user/match', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.corpUserList = res.data || [];
      });
  }

  openDrawer() {
    this.drawerVisible = true;
  }

  closeDrawer() {
    this.drawerVisible = false;
  }

  clearDrawer() {
    this.searchForm.reset();
  }
}
