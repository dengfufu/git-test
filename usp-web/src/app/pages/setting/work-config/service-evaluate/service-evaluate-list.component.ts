import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {ServiceEvaluateAddComponent} from './service-evaluate-add.component';
import {ServiceEvaluateEditComponent} from './service-evaluate-edit.component';
import {Page, Result} from '@core/interceptor/result';
import {ANYFIX_RIGHT} from '@core/right/right';


@Component({
  selector: 'app-service-evaluate-list',
  templateUrl: 'service-evaluate-list.component.html',
  styleUrls: ['service-evaluate-list.component.less']
})
export class ServiceEvaluateListComponent implements OnInit {

  aclRight = ANYFIX_RIGHT;

  searchForm: FormGroup;
  page = new Page();
  list: any;
  loading = false;
  useFormValue = true;
  drawerVisible: boolean;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private msgService : NzMessageService,
              private httpClient: HttpClient,
              private modalService: NzModalService) {
    this.searchForm = this.formBuilder.group({
      name: [],
      enabled: []
    });
  }

  ngOnInit(): void {
    this.queryServiceEvaluate();
  }

  loadList(params): void {
    this.loading = true;
    this.httpClient
      .post('/api/anyfix/service-evaluate/query',
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

  queryServiceEvaluate(reset?: boolean) {
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
      nzTitle: '添加服务评价指标',
      nzContent: ServiceEvaluateAddComponent,
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        this.queryServiceEvaluate();
      }
    });
  }

  // 进入编辑页面
  editModal(data): void {
    const modal = this.modalService.create({
      nzTitle: '编辑服务评价指标',
      nzContent: ServiceEvaluateEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        serviceEvaluate: data
      }
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        this.queryServiceEvaluate();
      }
    });
  }

  // 删除确认
  showDeleteConfirm(configId): void {
    this.modalService.confirm({
      nzTitle: '确定删除该配置吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteServiceEvaluate(configId),
      nzCancelText: '取消'
    });
  }

  // 删除
  deleteServiceEvaluate(id) {
    this.httpClient
      .delete('/api/anyfix/service-evaluate/' + id)
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
        this.queryServiceEvaluate();
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
