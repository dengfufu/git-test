import { Component, OnInit } from '@angular/core';
import {ANYFIX_RIGHT} from '@core/right/right';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Page} from '@core/interceptor/result';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {UserService} from '@core/user/user.service';
import {finalize} from 'rxjs/operators';
import {ServiceItemFeeRuleAddComponent} from './service-item-fee-rule-add.component';

@Component({
  selector: 'app-service-item-fee-rule',
  templateUrl: './service-item-fee-rule-list.component.html',
  styleUrls: ['./service-item-fee-rule-list.component.less']
})
export class ServiceItemFeeRuleListComponent implements OnInit {

  aclRight = ANYFIX_RIGHT;
  // 打开筛选
  drawerVisible = false;
  // 查询条件表单
  searchForm: FormGroup;
  // 服务项目下拉数据
  serviceItemOptions = [];
  // 列表当前页数据
  workFeeRuleList: any[] = [];
  // 分页
  page = new Page();
  // 加载中
  loading = false;

  constructor(
    private formBuilder: FormBuilder,
    private modalService: NzModalService,
    private httpClient: HttpClient,
    private userService: UserService,
    private messageService: NzMessageService
  ) {
    this.searchForm = this.formBuilder.group({
      ruleName: [],
      serviceItemId: []
    })
  }

  ngOnInit() {
    this.searchServiceItem();
    this.query(false);
  }

  searchServiceItem() {
    this.httpClient.post('/api/anyfix/service-item/list', {serviceCorp: this.userService.currentCorp.corpId})
      .subscribe((res: any) => {
        this.serviceItemOptions = res.data;
      })
  }

  // 查询
  query(reset?: boolean) {
    this.loading = true;
    this.httpClient.post('/api/anyfix/service-item-fee-rule/query', this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      ).subscribe((res: any) => {
      this.workFeeRuleList = res.data.list;
      this.page.total = res.data.total;
    })
  }

  // 查询参数
  getParams(): any {
    const params = this.searchForm.value;
    params.serviceCorp = this.userService.currentCorp.corpId;
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    return params;
  }

  // 打开添加页面
  add() {
    const addModal = this.modalService.create({
      nzTitle: '添加规则',
      nzContent: ServiceItemFeeRuleAddComponent,
      nzComponentParams: {
        type: 'add',
        serviceItemFeeRule: {}
      },
      nzFooter: null,
      nzWidth: 800,
      nzStyle:{'margin-top': '-40px'},
    })
    // 添加提交后刷新列表
    addModal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.query(false);
      }
    })
  }

  // 打开编辑页面
  mod(rule) {
    const modModal = this.modalService.create({
      nzTitle: '编辑规则',
      nzContent: ServiceItemFeeRuleAddComponent,
      nzComponentParams: {
        type: 'mod',
        serviceItemFeeRule: rule
      },
      nzFooter: null,
      nzWidth: 800
    })
    // 修改提交后刷新列表
    modModal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {

        this.query(false);
      }
    })
  }

  // 弹出删除确认框
  showDeleteConfirm(ruleId) {
    this.modalService.confirm({
      nzTitle: '确定删除该规则吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.delete(ruleId),
      nzCancelText: '取消'
    });
  }

  // 删除
  delete(ruleId: any) {
    this.httpClient
      .delete(`/api/anyfix/service-item-fee-rule/${ruleId}`)
      .subscribe(() => {
        this.messageService.success('删除成功！');
        this.query(false);
      });
  }

  // 打开删选条件
  openDrawer() {
    this.drawerVisible = true;
  }

  // 关闭删选条件
  closeDrawer() {
    this.drawerVisible = false;
  }

  // 清除删选条件
  clearDrawer() {
    this.searchForm.reset();
  }

}
