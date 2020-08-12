import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalService, toNumber} from 'ng-zorro-antd';
import {CustomReasonEditComponent} from './custom-reason-edit.component';
import {CustomReasonAddComponent} from './custom-reason-add.component';
import {Page, Result} from '@core/interceptor/result';
import {ActivatedRoute} from '@angular/router';
import {ANYFIX_RIGHT} from '@core/right/right';
import {ZonValidators} from '@util/zon-validators';


/**
 * 客户原因
 */
@Component({
  selector: 'app-custom-reason-list',
  templateUrl: 'custom-reason-list.component.html',
  styleUrls: ['custom-reason-list.component.less']
})
export class CustomReasonListComponent implements OnInit {

  aclRight = ANYFIX_RIGHT;

  searchForm: FormGroup;
  page = new Page();
  list: any;
  reasonType = 0; // 原因类型
  addTitle = '';
  editTitle = '';

  loading = false;
  drawerVisible: boolean;
  // 委托商列表
  demanderCorpList = [];

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private modalService: NzModalService,
              private msgService: NzMessageService,
              private activatedRoute: ActivatedRoute) {
    this.searchForm = this.formBuilder.group({
      customCorp: [null, [ZonValidators.required('委托商')]],
      name: [],
      enabled: [],
      reasonType: [this.reasonType, []]
    });
    this.activatedRoute.data.subscribe((item) => {
      this.reasonType = toNumber(item.reasonType);
      if (this.reasonType === 1) {
        this.addTitle = '添加客户撤单原因';
        this.editTitle = '编辑客户撤单原因';
      }
    });
  }

  ngOnInit(): void {
    this.loadDemanderCorp();
  }

  // 加载委托商列表
  loadDemanderCorp() {
    this.httpClient.get(`/api/anyfix/demander/list`).subscribe((result: Result) => {
      if (result.code === 0) {
        this.demanderCorpList = result.data || [];
        if (this.demanderCorpList.length >= 1) {
          this.searchForm.patchValue({
            customCorp: this.demanderCorpList[0].demanderCorp
          });
        }
      }
      this.queryReason();
    });
  }

  loadList(reset?: boolean): void {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient
      .post('/api/anyfix/custom-reason/query',
        this.getParams())
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
    this.loadList(reset);
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
      nzContent: CustomReasonAddComponent,
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
      nzContent: CustomReasonEditComponent,
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
    this.httpClient
      .delete('/api/anyfix/custom-reason/' + id)
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
    // 不清空客户的查询条件
    Object.keys(this.searchForm.controls).forEach((key: any) => {
      if (key !== 'customCorp') {
        this.searchForm.controls[key].reset();
      }
    });
  }
}
