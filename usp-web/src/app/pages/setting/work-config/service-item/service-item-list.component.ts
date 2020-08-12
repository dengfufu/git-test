import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {ServiceItemAddComponent} from './service-item-add.component';
import {ServiceItemEditComponent} from './service-item-edit.component';
import {Page} from '@core/interceptor/result';
import {ANYFIX_RIGHT} from '@core/right/right';
import {UserService} from '@core/user/user.service';


@Component({
  selector: 'app-service-item-list',
  templateUrl: 'service-item-list.component.html',
  styleUrls: ['service-item-list.component.less']
})
export class ServiceItemListComponent implements OnInit {

  aclRight = ANYFIX_RIGHT;

  searchForm: FormGroup;
  page = new Page();
  serviceItemList: any;
  loading = false;
  useFormValue = true;
  drawerVisible: boolean;
  // 委托商下拉选项
  demanderCorpOptions: any = [];

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private msgService : NzMessageService,
              private modalService: NzModalService,
              public userService: UserService) {
    this.searchForm = this.formBuilder.group({
      name: [],
      demanderCorp: [],
      enabled: []
    });
  }

  ngOnInit(): void {
    this.queryServiceItem();
    this.listDemanderCorp();
  }

  // 获取委托商下拉选项
  listDemanderCorp() {
    const params = {
      serviceCorp: this.userService.currentCorp.corpId,
      enabled: 'Y'
    };
    this.httpClient.post('/api/anyfix/demander-service/demander/list', params)
      .subscribe((res: any) => {
      this.demanderCorpOptions = res.data;
    });
  }

  loadList(params): void {
    this.loading = true;
    this.httpClient
      .post('/api/anyfix/service-item/query',
        params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.serviceItemList = res.data.list;
        this.page.total = res.data.total;
      });
  }

  queryServiceItem(reset?: boolean) {
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
      nzTitle: '添加服务项目',
      nzContent: ServiceItemAddComponent,
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        this.queryServiceItem();
      }
    });
  }

  // 进入编辑页面
  editModal(data): void {
    const modal = this.modalService.create({
      nzTitle: '编辑服务项目',
      nzContent: ServiceItemEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        serviceItem: data
      }
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        this.queryServiceItem();
      }
    });
  }

  // 删除确认
  showDeleteConfirm(configId): void {
    this.modalService.confirm({
      nzTitle: '确定删除吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteServiceItem(configId),
      nzCancelText: '取消'
    });
  }

  // 删除
  deleteServiceItem(id) {
    this.httpClient
      .delete('/api/anyfix/service-item/' + id)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        // 如果是为第n页第一条，需要调整为n-1页才有数据显示
        if (this.serviceItemList.length === 1 && this.page.pageNum > 1) {
          this.page.pageNum = this.page.pageNum - 1;
        }
        this.queryServiceItem();
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
