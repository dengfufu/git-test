import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalService} from 'ng-zorro-antd';
import {CorpUserAddComponent} from './corp-user-add.component';
import {UserService} from '@core/user/user.service';
import {Page} from '@core/interceptor/result';
import {ActivatedRoute, Router} from '@angular/router';
import {UAS_RIGHT} from '@core/right/right';
import {CorpUserEditComponent} from '@shared/components/corp-user-edit/corp-user-edit.component';

@Component({
  selector: 'app-corp-user-list',
  templateUrl: 'corp-user-list.component.html',
  styleUrls: ['corp-user-list.component.less']
})
export class CorpUserListComponent implements OnInit {

  aclRight = UAS_RIGHT;

  searchForm: FormGroup;
  page = new Page();
  list = [];
  loading = false;

  serviceBranchOptions = [];

  nzOptions: any;
  userOptions = [];
  roleOptions = [];
  provinceOptions = [];
  currentCorp = this.userService.currentCorp;

  drawerVisible: boolean;
  useFormValue = true;
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              public userService: UserService,
              private modalService: NzModalService,
              private router: Router,
              private activatedRoute: ActivatedRoute) {
    this.searchForm = this.formBuilder.group({
      userId: [],
      roleId: [],
      province: [],
      serviceBranch: [],
      type: [1]
    });
  }

  ngOnInit(): void {
    this.queryUser();
    this.matchUser();
    this.matchRole();
    this.matchServiceBranch();
    this.listProvince();
  }

  listProvince() {
    this.httpClient
      .get('/api/uas/area/province/list').subscribe((res: any) => {
      this.provinceOptions = res.data;
    });
  }

  queryUser(reset?: boolean) {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient
      .post('/api/uas/corp-user/query',
        this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.page.total = res.data.total;
        this.list = res.data.list;
      });
  }

  getParams() {
    let params: any = {};
    if (this.useFormValue) {
      params = Object.assign({}, this.searchForm.value);
    }
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    params.corpId = this.currentCorp.corpId;
    return params;
  }

  // 进入添加页面
  addModal(): void {
    const modal = this.modalService.create({
      nzTitle: '添加员工',
      nzContent: CorpUserAddComponent,
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.useFormValue = false;
        this.queryUser();
      }
    });
  }

  // 进入编辑页面
  editModal(data): void {
    const modal = this.modalService.create({
      nzTitle: '编辑员工',
      nzContent: CorpUserEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        corpUser: data
      }
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.queryUser();
      }
    });
  }

  // 删除确认
  showDeleteConfirm(userId): void {
    this.modalService.confirm({
      nzTitle: '确定删除该员工吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteUser(userId),
      nzCancelText: '取消'
    });
  }

  /**
   * 设置人员范围权限
   */
  userRightScope(userId) {
    this.router.navigate(['../user-right-scope'],
      {
        queryParams: {userId},
        relativeTo: this.activatedRoute
      });
  }

  // 删除企业人员
  deleteUser(userId) {
    this.httpClient
      .delete('/api/uas/corp-user/' + this.currentCorp.corpId + '/' + userId)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        // 如果是为第n页第一条，需要调整为n-1页才有数据显示
        if (this.list.length === 1 && this.page.pageNum > 1) {
          this.page.pageNum = this.page.pageNum - 1;
        }
        this.queryUser();
      });
  }

  matchUser(userName?: string) {
    const payload = {
      corpId: this.currentCorp.corpId,
      matchFilter: userName
    };
    this.httpClient
      .post('/api/uas/corp-user/match', payload)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.userOptions = res.data;
      });
  }

  matchServiceBranch(name?: string) {
    const payload = {
      serviceCorp: this.currentCorp.corpId,
      branchName: name
    };
    this.httpClient
      .post('/api/anyfix/service-branch/match', payload)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.serviceBranchOptions = res.data;
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

  matchRole(roleName?: string) {
    const params = {
      matchFilter: roleName,
      corpId: this.currentCorp.corpId
    };
    this.httpClient
      .post('/api/uas/sys-role/match', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.roleOptions = res.data || [];
      });
  }

}
