import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {finalize} from 'rxjs/operators';
import {Page} from '@core/interceptor/result';
import {NzModalService} from 'ng-zorro-antd';
import {CorpUserSelectorComponent} from '../../corp/user/selector/corp-user-selector.component';
import {UserService} from '@core/user/user.service';
import {ANYFIX_RIGHT} from '@core/right/right';

@Component({
  selector: 'app-device-branch-detail',
  templateUrl: 'device-branch-detail.component.html',
  styleUrls: ['device-branch.component.less']
})
export class DeviceBranchDetailComponent implements OnInit {

  aclRight = ANYFIX_RIGHT;

  branchId: any;
  deviceBranch: any = {};
  deviceBranchList: any = [];
  deviceInfoList: any = [];
  loading = false;
  workList: any= [];
  page = new Page();
  deviceInfoPage = new Page();
  workListPage = new Page();
  deviceInfoLoading = false;
  workListLoading = false;
  isServiceProvider = this.userService.currentCorp.serviceProvider  === 'Y';
  constructor(private httpClient: HttpClient,
              private modalService: NzModalService,
              private router: Router,
              private userService: UserService,
              private activatedRoute: ActivatedRoute,
              private cdf: ChangeDetectorRef) {
    this.branchId = this.activatedRoute.snapshot.queryParams.branchId;
  }

  ngOnInit() {
    this.initBranchInfo();
    this.queryBranchUser();
    this.queryDeviceInfoList();
    this.queryWorkList();
  }

  /**
   * 初始化网点数据
   */
  initBranchInfo() {
    this.httpClient
      .get('/api/anyfix/device-branch/' + this.branchId)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.deviceBranch = res.data;
      });
  }

  /**
   * 加载网点人员
   */
  queryBranchUser(reset?: boolean) {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient
      .post('/api/anyfix/device-branch-user/query', this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.deviceBranchList = res.data.list;
        this.page.total = res.data.total;
      });
  }

  queryDeviceInfoList() {
    this.deviceInfoLoading = true;
    const params = {
      branchId: this.branchId
    };
    this.httpClient
      .post('/api/device/device-info/query', params)
      .pipe(
        finalize(() => {
          this.deviceInfoLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.deviceInfoList = res.data.list;
        this.deviceInfoPage.total = res.data.total;
      });
  }

  queryWorkList() {
    this.workListLoading = true;
    const params = {
      deviceBranch: this.branchId
    };
    this.httpClient
      .post('/api/anyfix/work-request/query', params)
      .pipe(
        finalize(() => {
          this.workListLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.workList = res.data.list;
        this.workListPage.total = res.data.total;
      });
  }

  getParams() {
    const params: any = {};
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    params.branchId = this.branchId;
    return params;
  }

  // 删除确认
  showDeleteConfirm(userId): void {
    this.modalService.confirm({
      nzTitle: '确定删除该人员吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteBranchUser(userId),
      nzCancelText: '取消'
    });
  }

  // 删除
  deleteBranchUser(userId) {
    this.httpClient
      .delete('/api/anyfix/device-branch-user/' + this.branchId + '/' + userId, {})
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        // 如果是为第n页第一条，需要调整为n-1页才有数据显示
        if (this.deviceBranchList.length === 1 && this.page.pageNum > 1) {
          this.page.pageNum = this.page.pageNum - 1;
        }
        this.queryBranchUser();
      });
  }

  // 进入选择页面
  selectUserModal(): void {
    const modal = this.modalService.create({
      nzTitle: '选择人员',
      nzContent: CorpUserSelectorComponent,
      nzFooter: null,
      nzWidth: 800,
      nzStyle:{'margin-top': '-40px'},
      nzComponentParams: {
        params: {
          urlParams: {branchId: this.branchId},
          url: '/api/anyfix/device-branch-user/add',
          fromUri: 'device-branch-user',
        }
      }
    });
    modal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.queryBranchUser();
      }
    });
  }

  goBack() {
    history.go(-1);
  }
}
