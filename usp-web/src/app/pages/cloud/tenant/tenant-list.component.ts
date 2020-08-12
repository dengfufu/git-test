import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {Page} from '@core/interceptor/result';
import {TenantEditComponent} from './tenant-edit.component';
import {TenantAddComponent} from './tenant-add.component';
import {SYS_RIGHT} from '../../../core/right/right';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-tenant-list',
  templateUrl: 'tenant-list.component.html',
  styleUrls: ['tenant-list.component.less']
})
export class TenantListComponent implements OnInit {

  aclRight = SYS_RIGHT;

  searchForm: FormGroup;
  page = new Page();
  list: any;
  loading = false;
  corpLoading = false;
  nzOptions: any;
  ADVANCED = 2;
  corpList = [];
  useFormValue = true;
  drawerVisible: boolean;
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private router: Router,
              private activatedRoute : ActivatedRoute,
              private httpClient: HttpClient,
              private userService: UserService,
              private modalService: NzModalService) {
    this.searchForm = this.formBuilder.group({
      corpId: [],
      serviceDemander: [],
      serviceProvider: [],
      deviceUser: [],
      cloudManager: []
    });
  }

  ngOnInit(): void {
    this.queryTenant();
    this.listCorp();
  }

  loadList(params: string): void {
    this.loading = true;
    this.httpClient
      .post('/api/uas/sys-tenant/query',
        params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const data = res.data;
        this.list = data.list || [];
        this.page.total = data.total;
      });
  }

  queryTenant(reset?: boolean) {
    if(reset) {
      this.page.pageNum = 1;
    }
    let params: any = {};
    if (this.useFormValue) {
      params = Object.assign({},this.searchForm.value);
    }
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    this.loadList(params);
  }

  // 进入添加页面
  addModal(): void {
    const modal = this.modalService.create({
      nzTitle: '添加租户',
      nzContent: TenantAddComponent,
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.useFormValue = false;
        this.queryTenant(true);
      }
    });
  }

  // 进入编辑页面
  editModal(data): void {
    const modal = this.modalService.create({
      nzTitle: '编辑租户',
      nzContent: TenantEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        sysTenant: data
      }
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.queryTenant();
      }
    });
  }

  // 删除确认
  showDeleteConfirm(corpId): void {
    this.modalService.confirm({
      nzTitle: '确定删除吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteTenant(corpId),
      nzCancelText: '取消'
    });
  }

  // 删除
  deleteTenant(id) {
    this.httpClient
      .delete('/api/uas/sys-tenant/' + id)
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
        this.queryTenant();
      });
  }

  listCorp(corpName?: any){
    const params = {matchFilter: corpName};
    this.corpLoading = true;
    this.httpClient
      .post('/api/uas/corp-registry/match', params)
      .pipe(
        finalize(() => {
          this.corpLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const list = [];
        res.data.forEach(item => {
          list.push({
            value: item.corpId,
            text: item.corpName
          });
        });
        this.corpList = list;
      });
  }

  matchCorp(corpName) {
    this.listCorp(corpName);
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

  // 跳转到企业详情里面
  corpDetail(corpId) {
    if (corpId !== undefined && corpId !== null && corpId > 0) {
      this.router.navigate(['/cloud/corp-list/detail'],
        {queryParams: {corpId}, relativeTo: this.activatedRoute});
    }
  }
}
