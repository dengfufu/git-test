import {Component, OnInit} from '@angular/core';
import {ANYFIX_RIGHT} from '@core/right/right';
import {Page} from '@core/interceptor/result';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {FormBuilder, FormGroup} from '@angular/forms';
import {UserService} from '@core/user/user.service';
import {finalize} from 'rxjs/operators';
import {ImplementFeeAddComponent} from './implement-fee-add/implement-fee-add.component';
import {ACLService} from '@delon/acl';

@Component({
  selector: 'app-work-implement-fee',
  templateUrl: './implement-fee.component.html',
  styleUrls: ['./implement-fee.component.less']
})
export class ImplementFeeComponent implements OnInit {

  // 分页
  page: Page = new Page();
  // 数据列表
  implementFeeList: any[] = [];
  // 加载中
  loading = false;
  // 权限
  aclRight = ANYFIX_RIGHT;
  // 查询表单
  searchForm: FormGroup;
  // 打开筛选
  drawerVisible = false;
  // 当前企业
  curCorpId = this.userService.currentCorp.corpId;
  // 委托商列表
  demanderCorpList: any[] = [];
  // 权限列表
  aclRightIdList: any[] = [];

  constructor(
    public httpClient: HttpClient,
    public messageService: NzMessageService,
    public formBuilder: FormBuilder,
    public userService: UserService,
    public modalService: NzModalService,
    public aclService: ACLService
  ) {
    this.searchForm = this.formBuilder.group({
      serviceCorp: [this.curCorpId],
      demanderCorp: [],
      implementName: [],
      enabled: ['Y']
    });
  }

  ngOnInit() {
    // 获取权限列表
    this.aclRightIdList = this.aclService.data.abilities || [];
    // 加载列表
    this.query();
    // 加载委托商
    this.listDemanderCorpList();
  }

  // 查询
  query(reset?: boolean) {
    // 重置页码
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient.post('/api/anyfix/work-fee-implement-define/query', this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      ).subscribe((res: any) => {
      this.implementFeeList = res.data.list;
      this.page.total = res.data.total;
    });
  }

  // 查询委托商列表
  listDemanderCorpList() {
    this.httpClient.post('/api/anyfix/demander-service/demander/list', {serviceCorp: this.curCorpId})
      .subscribe((res: any) => {
        this.demanderCorpList = res.data;
      });
  }

  // 查询参数
  getParams() {
    const params: any = Object.assign({}, this.searchForm.value);
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    return params;
  }

  // 添加
  add() {
    const addModal = this.modalService.create({
      nzTitle: '添加工单支出费用',
      nzContent: ImplementFeeAddComponent,
      nzComponentParams: {
        type: 'add',
        implementId: ''
      },
      nzFooter: null,
      nzWidth: 800,
      nzStyle: {'margin-top': '-40px'}
    });
    // 添加提交后刷新列表
    addModal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.query(false);
      }
    });
  }

  // 修改是否可用
  modEnabled(event, data: any) {
    console.log(event);
    this.loading = true;
    const params: any = Object.assign({}, data);
    params.enabled = event === true ? 'Y' : 'N';
    this.httpClient.post('/api/anyfix/work-fee-implement-define/update', params)
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      ).subscribe((res: any) => {
      this.query();
    });
  }

  // 修改
  mod(data) {
    const modModal = this.modalService.create({
      nzTitle: '编辑工单支出费用',
      nzContent: ImplementFeeAddComponent,
      nzComponentParams: {
        type: 'mod',
        implementId: data.implementId
      },
      nzFooter: null,
      nzWidth: 800,
      nzStyle: {'margin-top': '-40px'}
    });
    // 添加提交后刷新列表
    modModal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.query(false);
      }
    });
  }

  // 打开筛选条件
  openDrawer() {
    this.drawerVisible = true;
  }

  // 关闭筛选条件
  closeDrawer() {
    this.drawerVisible = false;
  }

  // 清除筛选条件
  clearDrawer() {
    this.searchForm.reset();
    // 是否可用默认为是
    this.searchForm.controls.enabled.setValue('Y');
  }

}
