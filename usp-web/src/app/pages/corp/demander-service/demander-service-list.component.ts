import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalService} from 'ng-zorro-antd';
import {DemanderServiceAddComponent} from './demander-service-add.component';
import {DemanderServiceEditComponent} from './demander-service-edit.component';
import {UserService} from '@core/user/user.service';
import {Page} from '@core/interceptor/result';
import {ANYFIX_RIGHT} from '../../../core/right/right';

@Component({
  selector: 'app-corp-demander-service-list',
  templateUrl: 'demander-service-list.component.html',
  styleUrls: ['demander-service-list.component.less']
})
export class DemanderServiceListComponent implements OnInit {

  aclRight = ANYFIX_RIGHT;

  nzOptions: any;
  values: any;
  searchForm: FormGroup;
  page = new Page();
  list: any;
  loading = false;
  serviceCorpList: any;
  customCorpList: any;

  visible = false;

  serviceCorpOptions: any = [];

  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private userService: UserService,
              private modalService: NzModalService) {
    this.searchForm = this.formBuilder.group({
      serviceCorp: [],
      enabled: ['Y', []]
    });
  }

  ngOnInit(): void {
    this.queryDemanderService();
    this.listServiceCorp();
  }

  loadList(reset?: boolean): void {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient
      .post('/api/anyfix/demander-service/query', this.getParams())
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

  queryDemanderService(reset?: boolean) {
    this.loadList(reset);
  }

  getParams() {
    let params: any = {};
    if (this.searchForm) {
      params = this.searchForm.value;
    }
    params.demanderCorp = this.userService.currentCorp.corpId;
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    return params;
  }

  // 进入添加页面
  addModal(): void {
    const modal = this.modalService.create({
      nzTitle: '添加服务商',
      nzContent: DemanderServiceAddComponent,
      nzFooter: null,
      nzMaskClosable: false,
      nzWidth: 800
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.queryDemanderService();
      }
    });
  }

  listServiceCorp(): void {
    this.httpClient
      .post('/api/anyfix/demander-service/service/list', {})
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.serviceCorpOptions = res.data;
      });
  }

  // 进入编辑页面
  editModal(data): void {
    const modal = this.modalService.create({
      nzTitle: '编辑服务商',
      nzContent: DemanderServiceEditComponent,
      nzFooter: null,
      nzMaskClosable: false,
      nzWidth: 800,
      nzComponentParams: {
        demanderService: data
      }
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.queryDemanderService();
      }
    });
  }

  // 删除确认
  showDeleteConfirm(id): void {
    this.modalService.confirm({
      nzTitle: '确定删除吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteDemanderService(id),
      nzCancelText: '取消'
    });
  }

  // 删除
  deleteDemanderService(id) {
    this.httpClient
      .delete('/api/anyfix/demander-service/' + id)
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
        this.queryDemanderService();
      });
  }

  openDrawer() {
    this.visible = true;
  }

  closeDrawer() {
    this.visible = false;
  }

  clearDrawer() {
    this.searchForm.reset();
  }

  matchService(event: string) {

  }
}
