import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {WorkTypeAddComponent} from './work-type-add.component';
import {WorkTypeEditComponent} from './work-type-edit.component';
import {Page, Result} from '@core/interceptor/result';
import {ANYFIX_RIGHT} from '@core/right/right';
import {ZonValidators} from '@util/zon-validators';
import {UserService} from '@core/user/user.service';


@Component({
  selector: 'app-work-type-list',
  templateUrl: 'work-type-list.component.html',
  styleUrls: ['work-type-list.component.less']
})
export class WorkTypeListComponent implements OnInit {

  aclRight = ANYFIX_RIGHT;

  searchForm: FormGroup;
  page = new Page();
  list: any;
  workSysTypeList = [];
  loading = false;
  selectedValue: any;
  drawerVisible: boolean;
  // 委托商列表
  demanderCorpList = [];

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private userService : UserService,
              private msgService: NzMessageService,
              private modalService: NzModalService) {
    this.searchForm = this.formBuilder.group({
      demanderCorp: [],
      sysType: [],
      name: [],
      enabled: []
    });
  }

  ngOnInit(): void {
    this.loadDemanderCorp();
    this.loadWorkSysType();
    this.queryWorkType();
  }

  // 加载委托商列表
  loadDemanderCorp() {
    this.httpClient.post(`/api/anyfix/demander-service/demander/list`,
    {
        serviceCorp: this.userService.currentCorp.corpId,
        demanderCorp: this.userService.currentCorp.corpId
       }).subscribe((result: Result) => {
      if (result.code === 0) {
        this.demanderCorpList = result.data || [];
      }
    });
  }

  loadWorkSysType() {
    this.httpClient
      .get('/api/anyfix/work-sys-type/list')
      .subscribe((res: any) => {
        this.workSysTypeList = res.data;
      });
  }

  loadList(params): void {
    this.loading = true;
    this.httpClient
      .post('/api/anyfix/work-type/query',
        params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.list = res.data.list;
        this.page.total = res.data.total;
      });
  }

  queryWorkType(reset?: boolean) {
    if (reset) {
      this.page.pageNum = 1;
    }
    let params: any = {};
    params = Object.assign({}, this.searchForm.value);
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    this.loadList(params);
  }

  // 进入添加页面
  addWorkTypeModal(): void {
    const modal = this.modalService.create({
      nzTitle: '添加工单类型',
      nzContent: WorkTypeAddComponent,
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        this.queryWorkType();
      }
    });
  }

  // 进入编辑页面
  editWorkTypeModal(data): void {
    const modal = this.modalService.create({
      nzTitle: '编辑工单类型',
      nzContent: WorkTypeEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        workType: data
      }
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        this.queryWorkType();
      }
    });
  }

  // 删除确认
  showDeleteConfirm(configId): void {
    this.modalService.confirm({
      nzTitle: '确定删除吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteConfig(configId),
      nzCancelText: '取消'
    });
  }

  // 删除配置
  deleteConfig(id) {
    this.httpClient
      .delete('/api/anyfix/work-type/' + id)
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
        this.queryWorkType();
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
