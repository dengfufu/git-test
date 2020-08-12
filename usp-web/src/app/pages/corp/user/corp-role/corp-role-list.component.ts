import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {Page} from '@core/interceptor/result';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-corp-role-list',
  templateUrl: 'corp-role-list.component.html',
  styleUrls: ['corp-role-list.component.less']
})
export class CorpRoleListComponent implements OnInit {

  searchForm: FormGroup;
  page = new Page();
  list: any;
  loading = false;
  roleList: Array<{value: string; text: string}> = [];
  nzOptions: any;

  drawerVisible: boolean;
  nzFilterOption = () => true;


  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private userService: UserService,
              private modalService: NzModalService) {
    this.searchForm = this.formBuilder.group({
      roleName: [],
      enabled: ['Y', []]
    });
  }

  ngOnInit(): void {
    this.loadList(this.getParams());
    this.matchRole();
  }

  loadList(params: any, reset?: boolean): void {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient
      .post('/api/uas/sys-role/query',
        params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const data = res.data || [];
        this.list = data.list;
        this.page.total = data.total;
      });
  }

  queryRole(reset?: boolean) {
    this.loadList(this.getParams(), reset);
  }

  getParams() {
    let params: any = {};
    if (this.searchForm) {
      params = this.searchForm.value;
    }
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    return params;
  }

  // 进入添加页面
  addModal(): void {
    this.router.navigate(['../role-add'], {relativeTo: this.activatedRoute});
  }

  // 进入编辑页面
  editModal(roleId): void {
    this.router.navigate(['../role-edit'],
      {
        queryParams: {roleId},
        relativeTo: this.activatedRoute
      });
  }

  // 删除确认
  showDeleteConfirm(roleId): void {
    this.modalService.confirm({
      nzTitle: '确定删除吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteRole(roleId),
      nzCancelText: '取消'
    });
  }

  // 删除
  deleteRole(roleId) {
    this.httpClient
      .delete('/api/uas/sys-role/' + roleId)
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
        this.queryRole();
      });
  }

  matchRole(roleName?) {
    const params = {
      matchFilter: roleName,
      corpId: this.userService.currentCorp.corpId
    };

    this.httpClient
      .post('/api/uas/sys-role/match', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const list: any[] = res.data || [];
        const listOfOption: Array<{value: string; text: string}> = [];
        list.forEach(item => {
          listOfOption.push({
            value: item.roleName,
            text: item.roleName
          });
        });
        this.roleList = listOfOption;
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
