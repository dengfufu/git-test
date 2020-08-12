import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalService, toNumber} from 'ng-zorro-antd';
import {ServiceReasonEditComponent} from './service-reason-edit.component';
import {ServiceReasonAddComponent} from './service-reason-add.component';
import {Page, Result} from '@core/interceptor/result';
import {ActivatedRoute} from '@angular/router';
import {ANYFIX_RIGHT} from '@core/right/right';


/**
 * 客服原因
 */
@Component({
  selector: 'app-service-reason-list',
  templateUrl: 'service-reason-list.component.html',
  styleUrls: ['service-reason-list.component.less']
})
export class ServiceReasonListComponent implements OnInit {

  aclRight = ANYFIX_RIGHT;

  searchForm: FormGroup;
  page = new Page();
  list: any;
  loading = false;
  reasonType = 0; // 原因类型
  addTitle = '';
  editTitle = '';

  drawerVisible: boolean;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private modalService: NzModalService,
              private msgService : NzMessageService,
              private activatedRoute: ActivatedRoute) {
    this.searchForm = this.formBuilder.group({
      name: [],
      enabled:[],
      reasonType: [this.reasonType, []]
    });
    this.activatedRoute.data.subscribe((item) => {
      this.reasonType = toNumber(item.reasonType);
      switch (this.reasonType) {
        case 1:
          this.addTitle = '添加客服退单原因';
          this.editTitle = '编辑客服退单原因';
          break;
        case 2:
          this.addTitle = '添加客服撤回派单原因';
          this.editTitle = '编辑客服撤回派单原因';
          break;
        case 3:
          this.addTitle = '添加工程师拒绝派单原因';
          this.editTitle = '编辑工程师拒绝派单原因';
          break;
        case 4:
          this.addTitle = '添加工程师退回派单原因';
          this.editTitle = '编辑工程师退回派单原因';
          break;
        default:
          break;
      }
    });
  }

  ngOnInit(): void {
    this.queryReason();
  }

  loadList(params: any, reset?: boolean): void {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient
      .post('/api/anyfix/service-reason/query',
        params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: Result) => {
        this.list = res.data.list;
        this.page.total = res.data.total;
      });
  }

  queryReason(reset?: boolean) {
    this.loadList(this.getParams(), reset);
  }

  getParams() {
    let params: any = {};
    if (this.searchForm) {
      params = this.searchForm.value;
    }
    params.reasonType = this.reasonType;
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    return params;
  }

  // 进入添加页面
  addModal(): void {
    const modal = this.modalService.create({
      nzTitle: this.addTitle,
      nzContent: ServiceReasonAddComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        reasonType: this.reasonType
      }
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        this.queryReason();
      }
    });
  }

  // 进入编辑页面
  editModal(data): void {
    const modal = this.modalService.create({
      nzTitle: this.editTitle,
      nzContent: ServiceReasonEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        customReason: data
      }
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        this.queryReason();
      }
    });
  }

  // 删除确认
  showDeleteConfirm(configId): void {
    this.modalService.confirm({
      nzTitle: '确定删除吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteReason(configId),
      nzCancelText: '取消'
    });
  }

  // 删除
  deleteReason(id) {
    this.loading = true;
    this.httpClient
      .delete('/api/anyfix/service-reason/' + id)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        // 如果是为第n页第一条，需要调整为n-1页才有数据显示
        if (this.list.length === 1 && this.page.pageNum > 1) {
          this.page.pageNum = this.page.pageNum - 1;
        }
        this.queryReason();
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
