import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {NzModalService} from 'ng-zorro-antd';
import {DispatchServiceCorpAddComponent} from './dispatch-service-corp-add.component';
import {UserService} from '@core/user/user.service';
import {Page} from '@core/interceptor/result';
import {ANYFIX_RIGHT} from '@core/right/right';
import {ZorroUtils} from '@util/zorro-utils';


@Component({
  selector: 'app-dispatch-service-corp-list',
  templateUrl: 'dispatch-service-corp-list.component.html',
  styleUrls: ['dispatch-service-corp-list.component.less']
})
export class DispatchServiceCorpListComponent implements OnInit {

  ZorroUtils = ZorroUtils;
  aclRight = ANYFIX_RIGHT;

  searchForm: FormGroup;
  page = new Page();

  list: any;
  loading = false;

  serviceCorpList: any;

  serviceCorpOptionLoading = false;

  serviceCorpOption = [];

  areaOption = [];
  district: ''; // 表单提交的行政划分参数

  currentCorpId = '';
  useFormValue = true;
  drawerVisible: boolean;
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private modalService: NzModalService,
              private userService: UserService) {
    this.searchForm = this.formBuilder.group({
      serviceCorp: [],
      district: []
    });
  }

  ngOnInit(): void {
    this.query();
    this.currentCorpId = this.userService.currentCorp.corpId;
    this.getAreaOption();
  }


  // 初始化数据
  loadList(params): void {
    this.loading = true;
    this.httpClient
      .post('/api/anyfix/work-dispatch-service-corp/query',
        params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.serviceCorpList = res.data.list;
        this.page.total = res.data.total;
      });
  }

  query(reset?: boolean) {
    if(this.searchForm.value.district){
      this.district = this.searchForm.value.district[this.searchForm.value.district.length-1];
    }
    if(reset) {
      this.page.pageNum = 1;
    }
    let params: any = {};
    if (this.useFormValue) {
      params = Object.assign({},this.searchForm.value);
    }
    params.district = this.district;
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    this.loadList(params);
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

  add() {
    const modal = this.modalService.create({
      nzTitle: '添加工单提交服务商',
      nzContent: DispatchServiceCorpAddComponent,
      nzComponentParams: {
        pageType: 'add',
        demanderCorp: this.userService.currentCorp.corpId,
        demanderCorpName: this.userService.currentCorp.corpName
      },
      nzFooter: null,
      nzWidth: 900
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.useFormValue = false;
        this.query();
      }
    });
  }

  // 进入修改配置页面
  mod(data): void {
    const modal = this.modalService.create({
      nzTitle: '修改工单提交服务商',
      nzContent: DispatchServiceCorpAddComponent,
      nzComponentParams: {
        dispatchServiceCorp: data,
        pageType: 'mod'
      },
      nzFooter: null,
      nzWidth: 900
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.query();
      }
    });
  }


  // 删除确认
  showDeleteConfirm(id): void {
    this.modalService.confirm({
      nzTitle: '确定删除该规则吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.delete(id),
      nzCancelText: '取消'
    });
  }

  // 删除配置
  delete(id) {

    this.httpClient
      .delete('/api/anyfix/work-dispatch-service-corp/' + id)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        // 如果是为第n页第一条，需要调整为n-1页才有数据显示
        if (this.serviceCorpList.length === 1 && this.page.pageNum > 1) {
          this.page.pageNum = this.page.pageNum - 1;
        }
        this.query();
      });
  }

  /**
   * 列出服务委托方所有可用的服务商
   */
  searchServiceCorp(): void {
    if (this.serviceCorpOption != null && this.serviceCorpOption.length > 0) {
      this.serviceCorpOptionLoading = false;
    } else {
      this.serviceCorpOptionLoading = true;
      this.httpClient
        .post('/api/anyfix/demander-service/service/list', {})
        .pipe(
          finalize(() => {
            this.serviceCorpOptionLoading = false;
          })
        )
        .subscribe((res: any) => {
          this.serviceCorpOption = res.data;
        });
    }
  }

  // 获取行政区划数据
  getAreaOption(): void {
    this.httpClient.get('/api/uas/area/list')
      .subscribe((res: any) => {
        this.areaOption = res.data;
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
    this.district = '';
  }

}
