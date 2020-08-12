import { Component, OnInit } from '@angular/core';
import {UserService} from '@core/user/user.service';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Page} from '@core/interceptor/result';
import {finalize} from 'rxjs/operators';
import {WorkFeeVerifyAddComponent} from './add/work-fee-verify-add.component';
import {ANYFIX_RIGHT} from '@core/right/right';
import {ActivatedRoute, Router} from '@angular/router';
import {WorkFeeVerifyService} from './work-fee-verify.service';

@Component({
  selector: 'app-work-fee-verify',
  templateUrl: './work-fee-verify-list.component.html',
  styleUrls: ['./work-fee-verify-list.component.less']
})
export class WorkFeeVerifyListComponent implements OnInit {

  // 筛选表单
  searchForm: FormGroup;
  // 对账单列表
  verifyList: any[] = [];
  // 分页
  page = new Page();
  // 筛选条件可见
  visible = false;
  // 表格加载中
  loading = false;
  // 委托商下拉列表
  demanderCorpList: any[] = [];
  // 服务商下拉列表
  serviceCorpList: any[] = [];
  // 对账单状态
  verifyStatusList: any[] = [];
  // 对账单状态全选、部分选
  verifyStatusCheckedAll = true;
  verifyStatusIndeterminate = false;
  // 结算状态
  settleStatusList: any[] = [];
  // 结算状态全选、部分选
  settleStatusCheckedAll = true;
  settleStatusIndeterminate = false;
  // 权限
  aclRight = ANYFIX_RIGHT;

  constructor(
    public userService: UserService,
    public httpClient: HttpClient,
    public messageService: NzMessageService,
    public modalService: NzModalService,
    public formBuilder: FormBuilder,
    public router: Router,
    public activatedRoute: ActivatedRoute,
    public workFeeVerifyService: WorkFeeVerifyService
  ) {
    this.searchForm = this.formBuilder.group({
      verifyName: [],
      serviceCorp: [],
      demanderCorp: []
    })
  }

  ngOnInit() {
    // 加载列表数据
    this.queryVerifyList(false);
    // 获取委托商下拉列表
    this.listDemander();
    // 获取服务商下拉列表
    this.listService();
    // 初始化对账单状态列表和结算状态列表
    this.initStatusList();
  }

  // 加载对账单列表
  queryVerifyList(reset?: boolean) {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient.post('/api/anyfix/work-fee-verify/query', this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      ).subscribe((res: any) => {
        this.verifyList = res.data.list;
        this.page.total = res.data.total;
    })
  }

  // 获取查询参数
  getParams() {
    const params = Object.assign({}, this.searchForm.value);
    params.statuses = this.verifyStatusList.filter((verifyStatus: any) => verifyStatus.checked)
      .map((verifyStatus: any) => verifyStatus.value).join(',');
    params.settleStatuses = this.settleStatusList.filter((verifyStatus: any) => verifyStatus.checked)
      .map((verifyStatus: any) => verifyStatus.value).join(',');
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    params.currentCorp = this.userService.currentCorp.corpId;
    return params;
  }

  // 获取委托商下拉列表
  listDemander() {
    this.httpClient.post('/api/anyfix/demander-service/demander/list',
      {serviceCorp: this.userService.currentCorp.corpId})
      .subscribe((res: any) => {
        this.demanderCorpList = res.data;
      })
  }

  // 初始化对账单状态列表和结算状态列表
  initStatusList() {
    this.verifyStatusList = this.workFeeVerifyService.verifyStatusList.map((verifyStatus: any) => {
      return {
        value: verifyStatus.value,
        label: verifyStatus.label,
        checked: true
      };
    });
    this.settleStatusList = this.workFeeVerifyService.settleStatusList.map((settleStatus: any) => {
      return {
        value: settleStatus.value,
        label: settleStatus.label,
        checked: true
      }
    });
  }

  // 对账单状态全选或全不选
  updateVerifyStatusAllChecked() {
    this.verifyStatusIndeterminate = false;
    if (this.verifyStatusCheckedAll) {
      this.verifyStatusList = this.verifyStatusList.map(item => {
        return {
          ...item,
          checked: true
        };
      });
    } else {
      this.verifyStatusList = this.verifyStatusList.map(item => {
        return {
          ...item,
          checked: false
        };
      });
    }
  }

  // 更新对账单状态单选
  updateVerifyStatusSingleChecked() {
    this.verifyStatusCheckedAll = this.verifyStatusList.every(verifyStatus => verifyStatus.checked);
    this.verifyStatusIndeterminate = !this.verifyStatusCheckedAll && this.verifyStatusList.some(veifyStatus => veifyStatus.checked);
  }

  // 结算状态全选或全不选
  updateSettleStatusAllChecked() {
    this.settleStatusIndeterminate = false;
    if (this.settleStatusCheckedAll) {
      this.settleStatusList = this.settleStatusList.map(item => {
        return {
          ...item,
          checked: true
        };
      });
    } else {
      this.settleStatusList = this.settleStatusList.map(item => {
        return {
          ...item,
          checked: false
        };
      });
    }
  }

  // 更新对账单状态单选
  updateSettleStatusSingleChecked() {
    this.settleStatusCheckedAll = this.settleStatusList.every(settleStatus => settleStatus.checked);
    this.settleStatusIndeterminate = !this.settleStatusCheckedAll && this.settleStatusList.some(settleStatus => settleStatus.checked);
  }

  // 获取服务商下拉列表
  listService() {
    this.httpClient.post('/api/anyfix/demander-service/service/list',
      {demanderCorp: this.userService.currentCorp.corpId})
      .subscribe((res: any) => {
        this.serviceCorpList = res.data;
      })
  }

  // 打开添加页面
  addVerify() {
    const modal = this.modalService.create({
      nzTitle: '添加对账单',
      nzContent: WorkFeeVerifyAddComponent,
      nzWidth: 800,
      nzFooter: null,
    });
    modal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.queryVerifyList(true);
      }
    });
  }

  // 查看对账单详情
  toVerifyDetail(verifyId) {
    this.router.navigate(['detail'], {queryParams: {verifyId}, relativeTo: this.activatedRoute});
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
