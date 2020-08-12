import {Component, OnInit} from '@angular/core';
import {ANYFIX_RIGHT} from '@core/right/right';
import {FormBuilder, FormGroup} from '@angular/forms';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {Page, Result} from '@core/interceptor/result';
import {WorkFeeRuleAddComponent} from './work-fee-rule-add.component';
import {UserService} from '@core/user/user.service';
import {finalize} from 'rxjs/operators';
import {ACLService} from '@delon/acl';
import {WorkFeeRuleViewComponent} from './work-fee-rule-view/work-fee-rule-view.component';

@Component({
  selector: 'app-setting-work-fee-rule',
  templateUrl: './work-fee-rule-list.component.html',
  styleUrls: ['./work-fee-rule-list.component.less']
})
export class WorkFeeRuleListComponent implements OnInit {

  aclRight = ANYFIX_RIGHT;
  // 打开筛选
  drawerVisible = false;
  // 查询条件表单
  searchForm: FormGroup;
  // 列表当前页数据
  workFeeAssortList: any[] = [];
  // 分页
  page = new Page();
  // 加载中
  loading = false;
  // 委托商列表
  demanderCorpList: any[] = [];
  // 当前企业编号
  curCorpId = this.userService.currentCorp.corpId;
  // 权限编号列表
  rightIdList: any[] = [];

  constructor(
    private formBuilder: FormBuilder,
    private modalService: NzModalService,
    private httpClient: HttpClient,
    private userService: UserService,
    private messageService: NzMessageService,
    private aclService: ACLService
  ) {
    this.searchForm = this.formBuilder.group({
      assortName: [],
      demanderCorp: [],
      serviceCorp: [this.userService.currentCorp.corpId],
      enabled: ['Y']
    });
  }

  ngOnInit() {
    this.rightIdList = this.aclService.data.abilities || [];
    this.listDemanderCorp();
    this.query();
  }

  // 查询
  query(reset?: boolean) {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient.post('/api/anyfix/work-fee-assort-define/query', this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      ).subscribe((res: any) => {
      this.workFeeAssortList = res.data.list;
      this.page.total = res.data.total;
    });
  }

  // 查询参数
  getParams(): any {
    const params = this.searchForm.value;
    params.serviceCorp = this.userService.currentCorp.corpId;
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    return params;
  }

  listDemanderCorp() {
    this.httpClient.post('/api/anyfix/demander-service/demander/list', {serviceCorp: this.curCorpId})
      .subscribe((res: any) => {
        this.demanderCorpList = res.data;
      });
  }

  // 打开添加页面
  add() {
    const addModal = this.modalService.create({
      nzTitle: '添加工单收费规则',
      nzContent: WorkFeeRuleAddComponent,
      nzComponentParams: {
        type: 'add',
        assortFee: {}
      },
      nzFooter: null,
      nzWidth: 1200,
      nzStyle: {'margin-top': '-40px'}
    });
    // 添加提交后刷新列表
    addModal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.query(false);
      }
    });
  }

  // 打开编辑页面
  mod(assortFee) {
    this.httpClient.get(`/api/anyfix/work-fee-assort-define/${assortFee.assortId}`)
      .subscribe((res: any) => {
        if (res.data.used) {
          this.messageService.warning('该工单收费规则已被引用，只能修改是否可用');
          this.toView(assortFee.assortId);
        } else {
          this.toMod(assortFee);
        }
      });
  }

  // 修改
  toMod(assortFee) {
    const modModal = this.modalService.create({
      nzTitle: '编辑工单收费规则',
      nzContent: WorkFeeRuleAddComponent,
      nzComponentParams: {
        type: 'mod',
        assortFee
      },
      nzFooter: null,
      nzWidth: 1200
    });
    // 修改提交后刷新列表
    modModal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.query(false);
      }
    });
  }

  // 查看
  toView(assortId) {
    const modModal = this.modalService.create({
      nzTitle: '修改工单收费规则',
      nzContent: WorkFeeRuleViewComponent,
      nzComponentParams: {
        assortId,
        modEnabled: 'Y'
      },
      nzFooter: null,
      nzWidth: 800
    });
    modModal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.query(false);
      }
    })
  }

  // 修改是否可用
  modEnabled(data) {
    this.loading = true;
    const params: any = Object.assign({}, data);
    if (data.enabled === 'Y') {
      params.enabled = 'N';
    } else {
      params.enabled = 'Y';
    }
    params.updateEnabled = 'Y';
    this.httpClient.post('/api/anyfix/work-fee-assort-define/update', params)
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      ).subscribe((res: Result) => {
      if (res && res.code === 0) {
        this.query(false);
      }
    });
  }

  copy(assortFee) {
    const modModal = this.modalService.create({
      nzTitle: '添加工单收费定义',
      nzContent: WorkFeeRuleAddComponent,
      nzComponentParams: {
        type: 'copy',
        assortFee
      },
      nzFooter: null,
      nzWidth: 1200
    });
    // 复制提交后刷新列表
    modModal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.query(false);
      }
    });
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
      .delete(`/api/anyfix/work-basic-fee-rule/${ruleId}`)
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
    // 默认查询可用的记录
    this.searchForm.controls.enabled.setValue('Y');
  }

}
