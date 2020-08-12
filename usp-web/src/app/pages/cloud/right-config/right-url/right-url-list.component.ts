import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {Page} from '@core/interceptor/result';
import {RightUrlAddComponent} from './right-url-add.component';
import {RightUrlEditComponent} from './right-url-edit.component';
import {SYS_RIGHT} from '@core/right/right';

@Component({
  selector: 'app-right-url-list',
  templateUrl: 'right-url-list.component.html',
  styleUrls: ['right-url-list.component.less']
})
export class RightUrlListComponent implements OnInit {

  aclRight = SYS_RIGHT;

  searchForm: FormGroup;
  page = new Page();
  list: any;
  loading = false;
  rightTree = [];

  drawerVisible: boolean;

  nzOptions: any;
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private userService: UserService,
              private modalService: NzModalService) {
    this.searchForm = this.formBuilder.group({
      rightId: [],
      uri: []
    });
  }

  ngOnInit(): void {
    this.loadRightTree();
    this.queryAuthRight();
  }

  loadList(reset?: boolean): void {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient
      .post('/api/uas/sys-right-url/query',
        this.getParams())
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

  queryAuthRight(reset?: boolean) {
    this.loadList(reset);
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
    const modal = this.modalService.create({
      nzTitle: '添加鉴权',
      nzContent: RightUrlAddComponent,
      nzFooter: null,
      nzMaskClosable: false,
      nzWidth: 800
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.queryAuthRight();
      }
    });
  }

  // 进入编辑页面
  editModal(data): void {
    const modal = this.modalService.create({
      nzTitle: '编辑鉴权',
      nzContent: RightUrlEditComponent,
      nzFooter: null,
      nzMaskClosable: false,
      nzWidth: 800,
      nzComponentParams: {
        sysAuthRight: data
      }
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.queryAuthRight();
      }
    });
  }

  // 删除确认
  showDeleteConfirm(id): void {
    this.modalService.confirm({
      nzTitle: '确定删除吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteRight(id),
      nzCancelText: '取消'
    });
  }

  // 删除
  deleteRight(id) {
    this.httpClient
      .delete('/api/uas/sys-right-url/' + id)
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
        this.queryAuthRight();
      });
  }

  /**
   * 加载权限树
   */
  loadRightTree() {
    this.httpClient
      .post('/api/uas/sys-right/tree', {})
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.rightTree = res.data;
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
