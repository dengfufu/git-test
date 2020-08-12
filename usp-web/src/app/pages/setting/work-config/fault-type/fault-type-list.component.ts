import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {FaultTypeAddComponent} from './fault-type-add.component';
import {FaultTypeEditComponent} from './fault-type-edit.component';
import {Page, Result} from '@core/interceptor/result';
import {ANYFIX_RIGHT} from '@core/right/right';
import {ZonValidators} from '@util/zon-validators';
import {UserService} from '@core/user/user.service';


@Component({
  selector: 'app-fault-type-list',
  templateUrl: 'fault-type-list.component.html',
  styleUrls: ['fault-type-list.component.less']
})
export class FaultTypeListComponent implements OnInit {

  aclRight = ANYFIX_RIGHT;

  searchForm: FormGroup;

  page = new Page();
  faultTypeList: any;
  loading = false;
  useFormValue = true;
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
      name: [],
      enabled: []
    });
  }

  ngOnInit(): void {
    this.loadDemanderCorp();
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
        this.queryFaultType();
      }
    });
  }

  loadList(params): void {
    this.loading = true;
    this.httpClient
      .post('/api/anyfix/fault-type/query',
        params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.faultTypeList = res.data.list;
        this.page.total = res.data.total;
      });
  }

  queryFaultType(reset?: boolean) {
    if (reset) {
      this.page.pageNum = 1;
    }
    let params: any = {};
    if (this.useFormValue) {
      params = Object.assign({}, this.searchForm.value);
    }
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    this.loadList(params);
  }

  // 进入添加页面
  addModal(): void {
    const modal = this.modalService.create({
      nzTitle: '添加故障现象',
      nzContent: FaultTypeAddComponent,
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        this.queryFaultType();
      }
    });
  }

  // 进入编辑页面
  editModal(data): void {
    const modal = this.modalService.create({
      nzTitle: '编辑故障现象',
      nzContent: FaultTypeEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        faultType: data
      }
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        this.queryFaultType();
      }
    });
  }

  // 删除确认
  showDeleteConfirm(configId): void {
    this.modalService.confirm({
      nzTitle: '确定删除吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteFaultType(configId),
      nzCancelText: '取消'
    });
  }

  // 删除
  deleteFaultType(id) {
    this.httpClient
      .delete('/api/anyfix/fault-type/' + id)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        this.queryFaultType();
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
