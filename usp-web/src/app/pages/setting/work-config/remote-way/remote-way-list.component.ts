import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {RemoteWayAddComponent} from './remote-way-add.component';
import {RemoteWayEditComponent} from './remote-way-edit.component';
import {Page} from '@core/interceptor/result';
import {ANYFIX_RIGHT} from '@core/right/right';


@Component({
  selector: 'app-remote-way-list',
  templateUrl: 'remote-way-list.component.html',
  styleUrls: ['remote-way-list.component.less']
})
export class RemoteWayListComponent implements OnInit {

  aclRight = ANYFIX_RIGHT;

  searchForm: FormGroup;
  page = new Page();
  remoteWayList: any;
  loading = false;

  drawerVisible: boolean;
  useFormValue = true;
  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private msgService : NzMessageService,
              private modalService: NzModalService) {
    this.searchForm = this.formBuilder.group({
      name: [],
      enabled: []
    });
  }

  ngOnInit(): void {
    this.queryRemoteWayList();
  }

  loadList(params): void {
    this.loading = true;
    this.httpClient
      .post('/api/anyfix/remote-way/query',
        params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.remoteWayList = res.data.list;
        this.page.total = res.data.total;
      });
  }

  queryRemoteWayList(reset?: boolean) {
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
      nzTitle: '添加远程处理方式',
      nzContent: RemoteWayAddComponent,
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        this.useFormValue = false;
        this.queryRemoteWayList();
      }
    });
  }

  // 进入编辑页面
  editModal(data): void {
    const modal = this.modalService.create({
      nzTitle: '编辑远程处理方式',
      nzContent: RemoteWayEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        remoteWay: data
      }
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        this.queryRemoteWayList();
      }
    });
  }

  // 删除确认
  showDeleteConfirm(configId): void {
    this.modalService.confirm({
      nzTitle: '确定删除吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteRemoteWay(configId),
      nzCancelText: '取消'
    });
  }

  // 删除
  deleteRemoteWay(id) {
    this.httpClient
      .delete('/api/anyfix/remote-way/' + id)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        // 如果是为第n页第一条，需要调整为n-1页才有数据显示
        if (this.remoteWayList.length === 1 && this.page.pageNum > 1) {
          this.page.pageNum = this.page.pageNum - 1;
        }
        this.queryRemoteWayList();
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
