import {Component, OnInit} from '@angular/core';
import {Page} from '@core/interceptor/result';
import {HttpClient} from '@angular/common/http';
import {NzModalService} from 'ng-zorro-antd';
import {FormBuilder, FormGroup} from '@angular/forms';
import {UserService} from '@core/user/user.service';
import {finalize} from 'rxjs/operators';
import {SettleDemanderAddComponent} from './settle-demander-add/settle-demander-add.component';
import {ActivatedRoute, Router} from '@angular/router';
import {ANYFIX_RIGHT} from '@core/right/right';
import {SettleService} from './settle.service';

@Component({
  selector: 'app-settle-demander',
  templateUrl: './settle-demander.component.html',
  styleUrls: ['./settle-demander.component.less']
})
export class SettleDemanderComponent implements OnInit {

  // 列表数据
  settleDemanderList: any[] = [];
  // 查询表单
  searchForm: FormGroup;
  // 分页参数
  page = new Page();
  // 加载中
  loading = false;
  // 筛选条件可见
  visible = false;
  // 委托商下拉列表数据
  demanderOptions: any[] = [];
  // 服务商下拉列表数据
  serviceOptions: any[] = [];
  // 权限
  aclRight = ANYFIX_RIGHT;

  invoiceStatusList = this.settleService.invoiceStatusList; // 开票状态列表
  payStatusList = this.settleService.payStatusList; // 付款状态列表

  constructor(
    public httpClient: HttpClient,
    public modalService: NzModalService,
    public formBuilder: FormBuilder,
    public userService: UserService,
    public router: Router,
    public activatedRoute: ActivatedRoute,
    public settleService: SettleService
  ) {
    this.searchForm = formBuilder.group({
      settleCode: [],
      demanderCorp: [],
      serviceCorp: [],
      settleWay: [],
      invoiceStatusList: [],
      payStatusList: []
    });
  }

  ngOnInit() {
    this.querySettleDemander(this.getParams(), false);
    this.loadDemanderOptions();
    this.loadServiceOptions();
  }

  // 查询委托商结算单
  querySettleDemander(params: any, reset: boolean) {
    if (reset) {
      this.page.pageNum = 1;
      params.pageNum = 1;
    }
    this.loading = true;
    this.httpClient.post('/api/anyfix/settle-demander/query', params)
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      ).subscribe((res: any) => {
      this.settleDemanderList = res.data.list;
      this.page.total = res.data.total;
    });
  }

  // 委托商下拉数据
  loadDemanderOptions() {
    this.httpClient.post('/api/anyfix/demander-service/demander/list', {serviceCorp: this.userService.currentCorp.corpId})
      .subscribe((res: any) => {
        this.demanderOptions = res.data;
      });
  }

  // 服务商下拉数据
  loadServiceOptions() {
    this.httpClient.post('/api/anyfix/demander-service/service/list', {demanderCorp: this.userService.currentCorp.corpId})
      .subscribe((res: any) => {
        this.serviceOptions = res.data;
      });
  }

  // 组装参数
  getParams() {
    const params = this.searchForm.value;
    params.currentCorp = this.userService.currentCorp.corpId;
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    if (params.payStatusList && params.payStatusList.length > 0) {
      const list: any[] = [];
      params.payStatusList.forEach((status: number) => {
        list.push(status);
        if (status === this.settleService.TO_RECEIPT) {
          list.push(this.settleService.RECEIPTED);
        }
      });
      params.payStatusList = [...new Set(list)];
    }
    return params;
  }

  // 打开添加委托商结算单页面
  addSettleDemander() {
    const modal = this.modalService.create({
      nzTitle: '添加委托商结算单',
      nzContent: SettleDemanderAddComponent,
      nzWidth: 1000,
      nzFooter: null
    });
    modal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.querySettleDemander(this.getParams(), false);
      }
    });
  }

  showDetail(settleId) {
    this.router.navigate(['../settle-demander-detail'], {relativeTo: this.activatedRoute, queryParams: {settleId}});
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

}
