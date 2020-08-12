import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalService} from 'ng-zorro-antd';
import {Page} from '@core/interceptor/result';
import {UserService} from '@core/user/user.service';
import {ANYFIX_RIGHT} from '@core/right/right';
import {ServiceBranchService} from '../sevice-branch.service';
import {BranchAddComponent} from '../add/branch-add.component';
import { ZorroUtils } from '@util/zorro-utils';

@Component({
  selector: 'app-corp-service-branch-list',
  templateUrl: 'branch-list.component.html',
  styleUrls: ['branch-list.component.less']
})
export class BranchListComponent implements OnInit {

  ZorroUtils = ZorroUtils;
  aclRight = ANYFIX_RIGHT;

  searchForm: FormGroup;
  page = new Page();
  spinning = false;
  branchDetailSpinning = false;
  loading = false;

  // 行政区划列表
  areaList: any[] = [];

  // 网点详情是否可见
  branchDetailVisible = false;

  // 服务网点列表
  branchList: any[] = [];
  // 服务网点树节点
  branchTreeNodes: any[] = [];
  // 模糊下拉网点列表
  branchOptionList: any[] = [];
  // 选中的网点编号
  branchId: any;
  // 选中的网点名称
  branchName: any;
  // 默认展开的节点
  expandedKeys = [];

  serviceBranch: any;

  headerTitle = '全部网点';
  // 网点列表类型，1=全部网点 2=下级网点 3=同级网点
  branchListType = 1;
  // 默认只查询第一级网点
  ifFirstLevel = 'Y';
  // 是否显示展开按钮
  expandBtnVisible = true;
  expandBtnName = '展开下级';

  drawerVisible = false;
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private modalService: NzModalService,
              private userService: UserService,
              private serviceBranchService: ServiceBranchService) {
    this.searchForm = this.formBuilder.group({
      branchId: [],
      area: [],
      enabled: ['Y']
    });
  }

  ngOnInit(): void {
    this.listArea();
    this.queryServiceBranch();
    this.matchBranch();
    this.loadBranchTree().then(data => {
      this.branchTreeNodes = data;
    });
  }

  /**
   * 展开下级
   */
  expandLowerBranchList() {
    if (this.expandBtnName === '展开下级') {
      this.expandBtnName = '收起下级';
      this.ifFirstLevel = '';
    } else {
      this.expandBtnName = '展开下级';
      this.ifFirstLevel = 'Y';
    }
    this.queryServiceBranch(true);
  }

  /**
   * 分页查询服务网点列表
   * @param reset 是否重置
   */
  queryServiceBranch(reset?: boolean) {
    if (reset) {
      this.page.pageNum = 1;
    }
    if (this.branchListType === 1) {
      const params: any = {};
      if (this.searchForm) {
        Object.assign(params, this.searchForm.value);
      }
      params.ifFirstLevel = this.ifFirstLevel;
      params.pageNum = this.page.pageNum;
      params.pageSize = this.page.pageSize;

      if (this.searchForm.value.area !== null) {
        if (this.searchForm.value.area[0]) {
          params.areaCode = this.searchForm.value.area[0];
        }
        if (this.searchForm.value.area[1]) {
          params.areaCode = this.searchForm.value.area[1];
        }
        if (this.searchForm.value.area[2]) {
          params.areaCode = this.searchForm.value.area[2];
        }
      }
      this.loadList(params);
      return;
    }
    if (this.branchListType === 2) {
      this.queryLowerServiceBranch(this.branchId).then((lowerRes: any[]) => {
        this.branchList = lowerRes;
      });
      return;
    }
    if (this.branchListType === 3) {
      this.querySameServiceBranch(this.branchId).then((sameRes: any[]) => {
        this.branchList = sameRes;
      });
    }
  }

  loadList(params: any): void {
    this.loading = true;
    this.httpClient
      .post('/api/anyfix/service-branch/query',
        params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.branchList = res.data.list;
        this.page.total = res.data.total;
      });
  }

  /**
   * 加载树
   */
  loadBranchTree(branchId?) {
    let url = '/api/anyfix/service-branch/tree';
    const params: any = {
      serviceCorp: this.userService.currentCorp.corpId
    };
    if (branchId) {
      params.branchId = branchId;
      url = '/api/anyfix/service-branch/upper/tree';
    }
    this.spinning = true;
    return this.httpClient
      .post(url, params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
          this.spinning = false;
        })
      )
      .toPromise().then((res: any) => {
        return res.data || [];
      });
  }

  clickTreeNode(event) {
    this.expandBtnVisible = false;
    this.branchName = event.node._title;
    this.showBranchDetail(event.node.key, true);
  }

  getBranchParams(branchId, reset?: boolean) {
    if (reset) {
      this.page.pageNum = 1;
    }
    const params: any = {};
    if (this.searchForm) {
      Object.assign(params, this.searchForm.value);
    }
    params.branchId = branchId;
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    if (this.searchForm.value.area !== null) {
      if (this.searchForm.value.area[0]) {
        params.areaCode = this.searchForm.value.area[0];
      }
      if (this.searchForm.value.area[1]) {
        params.areaCode = this.searchForm.value.area[1];
      }
      if (this.searchForm.value.area[2]) {
        params.areaCode = this.searchForm.value.area[2];
      }
    }
    return params;
  }

  /**
   * 分页查询下级服务网点列表
   * @param branchId 网点编号
   * @param reset 是否重置
   */
  queryLowerServiceBranch(branchId, reset?: boolean): Promise<any> {
    const params: any = this.getBranchParams(branchId, reset);
    this.loading = true;
    return new Promise((resolve, reject) => {
      this.listLowerBranch(params).then((res: any[]) => {
        resolve(res);
      }).finally(
        () => {
          this.loading = false;
        });
    });
  }

  listLowerBranch(params: any): Promise<any> {
    return this.httpClient
      .post('/api/anyfix/service-branch/lower/query',
        params)
      .toPromise().then((res: any) => {
        const list = res.data.list;
        this.page.total = res.data.total;
        return list;
      });
  }

  /**
   * 分页查询同级服务网点列表
   * @param branchId 网点编号
   * @param reset 是否重置
   */
  querySameServiceBranch(branchId, reset?: boolean): Promise<any> {
    const params: any = this.getBranchParams(branchId, reset);
    this.loading = true;
    return new Promise((resolve, reject) => {
      this.listSameBranch(params).then((res: any[]) => {
        resolve(res);
      }).finally(
        () => {
          this.loading = false;
        });
    });
  }

  listSameBranch(params: any): Promise<any> {
    return this.httpClient
      .post('/api/anyfix/service-branch/same/query',
        params)
      .toPromise().then((res: any) => {
        const list = res.data.list;
        this.page.total = res.data.total;
        return list;
      });
  }

  /**
   * 网点名称模糊下拉
   * @param value 网点编号
   */
  matchBranch(value?: string): void {
    const params = {
      serviceCorp: this.userService.currentCorp.corpId,
      branchName: value
    };
    this.httpClient
      .post('/api/anyfix/service-branch/match', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.branchOptionList = res.data || [];
      });
  }

  /**
   * 网点改变
   * @param branch 网点
   */
  branchChange(branch) {
    this.branchId = '';
    this.branchName = '';
    if (branch) {
      this.expandBtnVisible = false;
      this.branchName = branch.branchName;
      this.showBranchDetail(branch.branchId, true);
      this.loadBranchTree(branch.branchId).then(res => {
        this.branchTreeNodes = res;
        this.findAllUpperIdList(branch.branchId).then(data => {
          this.expandedKeys = data || [];
          this.expandedKeys = this.expandedKeys.filter(item => item !== branch.branchId);
        });
      });
    } else {
      this.expandBtnVisible = true;
      this.branchListType = 1;
      this.headerTitle = '全部网点';
      this.queryServiceBranch(true);
      this.loadBranchTree().then(res => {
        this.branchTreeNodes = res;
      });
    }
  }

  /**
   * 获得所有上级网点编号列表
   * @param id 网点编号
   */
  findAllUpperIdList(id) {
    const params = {
      branchId: id
    };
    return this.httpClient
      .post('/api/anyfix/service-branch/upper/list', params)
      .toPromise().then((res: any) => {
        const list: any[] = res.data || [];
        const idList = [];
        list.forEach(branch => {
          idList.push(branch.branchId);
        });
        return idList;
      });
  }

  /**
   * 网点详情
   */
  showBranchDetail(branchId, reset: boolean) {
    this.expandBtnVisible = false;
    this.branchId = branchId;
    this.branchDetailSpinning = true;
    this.serviceBranchService.loadBranchDetail(branchId).then(() => {
      this.branchDetailVisible = true;
      this.branchDetailSpinning = false;
    });
    const params = {branchId};
    this.serviceBranchService.queryBranchUser(params, true).then(() => {
      this.branchDetailVisible = true;
      this.branchDetailSpinning = false;
    });
    this.queryLowerServiceBranch(branchId, reset).then((lowerRes: any[]) => {
      this.headerTitle = '下级网点';
      this.branchListType = 2;
      if (lowerRes && lowerRes.length > 0) {
        this.branchList = lowerRes;
      } else {
        this.headerTitle = '同级网点';
        this.branchListType = 3;
        this.querySameServiceBranch(branchId, reset).then((sameRes: any[]) => {
          this.branchList = sameRes;
        });
      }
    });
  }

  /**
   * 添加服务网点
   */
  addBranch() {
    const modal = this.modalService.create({
      nzTitle: '添加服务网点',
      nzContent: BranchAddComponent,
      nzFooter: null,
      nzMaskClosable: false,
      nzWidth: 800
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        if (this.branchListType === 1) {
          this.queryServiceBranch();
        }
        if (this.branchListType === 2) {
          this.queryLowerServiceBranch(this.branchId).then((lowerRes: any[]) => {
            this.branchList = lowerRes;
          });
        }
        if (this.branchListType === 3) {
          this.querySameServiceBranch(this.branchId).then((sameRes: any[]) => {
            this.branchList = sameRes;
          });
        }
      }
    });
  }

  changeBranchList(event) {
    if (event === 'submit') {
      this.expandBtnVisible = true;
      this.headerTitle = '全部网点';
      this.serviceBranch = null;
      this.branchDetailVisible = false;
      this.queryServiceBranch(true);
      this.matchBranch();
      this.loadBranchTree().then(data => {
        this.branchTreeNodes = data;
      });
    }
  }

  /**
   * 行政区划列表
   */
  listArea(): void {
    this.httpClient
      .get('/api/uas/area/list')
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.areaList = res.data;
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
