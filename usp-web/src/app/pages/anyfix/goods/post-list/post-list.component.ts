import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {NzModalService} from 'ng-zorro-antd';
import {AnyfixService} from '@core/service/anyfix.service';
import {Page, Result} from '@core/interceptor/result';
import {UserService} from '@core/user/user.service';
import {CustomListComponent} from '@shared/components/custom-list/custom-list.component';
import {ANYFIX_RIGHT} from '@core/right/right';
import {isNull} from '@util/helpers';

@Component({
  selector: 'app-anyfix-goods-postlist',
  templateUrl: 'post-list.component.html',
  styleUrls: ['post-list.component.less']
})
export class PostListComponent implements OnInit {

  anyfixRight = ANYFIX_RIGHT;

  searchForm: FormGroup;
  page = new Page();
  loading = false;
  visible = false;

  goodsPostList: any[] = [];

  consignBranchList: any[] = []; // 发货网点列表
  receiveBranchList: any[] = []; // 收货网点列表
  consignBranchVisible = false; // 发货网点是否可见
  receiveBranchVisible = false; // 收货网点是否可见
  consignTypeVisible = false; // 托运方式是否可见
  expressTypeVisible = false; // 快递类型是否可见
  expressCompanyVisible = false; // 快递公司是否可见
  expressNoVisible = false; // 快递单号是否可见
  corpList: any[] = []; // 企业列表
  consignUserList: any[] = []; // 发货人列表
  receiveUserList: any[] = [];// 收货人列表
  areaList: any[] = []; // 行政区划列表
  transportTypeList: any[] = [];// 运输方式列表
  consignTypeList: any[] = []; // 托运方式列表
  expressTypeList: any[] = []; // 快递类型列表
  expressCompanyList: any[] = []; // 快递公司列表
  nzFilterOption = () => true;

  signStatusList = [
    {id: 1, text: '未签收'},
    {id: 2, text: '部分签收'},
    {id: 3, text: '全部签收'}
  ];

  totalWidth = 300; // 总宽度
  checkedColumnList: any[] = []; // 选中的自定义列
  customColumnList = [
    {title: '发货公司', field: 'consignCorpName', width: 100, checked: true},
    {title: '发货网点', field: 'consignBranchName', width: 100, checked: true},
    {title: '发货人', field: 'consignUserName', width: 100, checked: true},
    {title: '发货人电话', field: 'consignUserPhone', width: 120, checked: true},
    {title: '发货时间', field: 'consignTime', width: 100, type: 'date', checked: true},
    {title: '省', field: 'consignProvinceName', width: 60, checked: true},
    {title: '市', field: 'consignCityName', width: 80, checked: true},
    {title: '区', field: 'consignDistrictName', width: 100, checked: true},
    {title: '发货地址', field: 'consignAddress', width: 250, checked: true},
    {title: '收货公司', field: 'receiveCorpName', width: 100, checked: true},
    {title: '收货网点', field: 'receiveBranchName', width: 100, checked: true},
    {title: '收货人', field: 'receiverName', width: 100, checked: true},
    {title: '收货人电话', field: 'receiverPhone', width: 120, checked: true},
    {title: '省', field: 'receiveProvinceName', width: 60, checked: true},
    {title: '市', field: 'receiveCityName', width: 80, checked: true},
    {title: '区', field: 'receiveDistrictName', width: 100, checked: true},
    {title: '收货地址', field: 'receiveAddress', width: 250, checked: true},
    {title: '运输方式', field: 'transportTypeName', width: 80, checked: true},
    {title: '快递公司', field: 'expressCorpName', width: 150, checked: true},
    {title: '快递单号', field: 'expressNo', width: 150, checked: true},
    {title: '快递类型', field: 'expressTypeName', width: 80, checked: true},
    {title: '总箱数', field: 'boxNum', width: 80, checked: true},
    {title: '付费方式', field: 'payWayName', width: 80, checked: true},
    {title: '邮寄费', field: 'postFee', width: 100, checked: true},
    {title: '签收人', field: 'signerName', width: 100, checked: false},
    {title: '签收时间', field: 'signTime', width: 100, type: 'date', checked: false}
  ];

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private router: Router,
              public userService: UserService,
              private nzModalService: NzModalService,
              private activatedRoute: ActivatedRoute,
              public anyfixService: AnyfixService) {

    this.searchForm = this.formBuilder.group({
      postNo: [],
      signStatusList: [],
      consignCorp: [],
      consignBranch: [],
      consignUserName: [],
      consignArea: [],
      consignAreaList: [],
      transportTypeList: [],
      consignTypeList: [],
      expressTypeList: [],
      expressCorpName: [],
      expressNo: [],
      receiveCorp: [],
      receiveBranch: [],
      receiveArea: [],
      receiveAreaList: [],
      receiverName: [],
      consignTime: [],
      payWayList: [],
      createTime: []
    });
  }

  ngOnInit(): void {
    this.initShowColumnList();
    this.queryGoodsPostList();
    this.listArea();
    this.listCorp();
    this.listTransportType();
  }

  /**
   * 分页查询物品寄送单
   */
  queryGoodsPostList(reset?: boolean) {
    this.listGoodsPost(this.getParams(), reset);
  }

  /**
   * 获得查询参数
   */
  getParams() {
    let params: any = {};
    if (this.searchForm) {
      params = this.searchForm.value;
    }
    params.corpId = this.userService.currentCorp.corpId;
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    const consignAreaList: any[] = this.searchForm.controls.consignAreaList.value || [];
    if (consignAreaList.length > 0) {
      params.consignArea = consignAreaList[consignAreaList.length - 1];
    }
    const receiveAreaList: any[] = this.searchForm.controls.receiveAreaList.value || [];
    if (receiveAreaList.length > 0) {
      params.receiveArea = receiveAreaList[receiveAreaList.length - 1];
    }
    const consignTime = this.searchForm.controls.consignTime.value;
    if (consignTime) {
      params.consignStartDate = consignTime[0];
      params.consignEndDate = consignTime[1];
      params.consignTime = null;
    }
    const createTime = this.searchForm.controls.createTime.value;
    if (createTime) {
      params.createStartDate = createTime[0];
      params.createEndDate = createTime[1];
      params.createTime = null;
    }
    return params;
  }

  /**
   * 物品寄送单列表
   */
  listGoodsPost(params: any, reset: boolean = false): void {
    if (reset) {
      this.page.pageNum = 1;
      params.pageNum = 1;
    }
    this.loading = true;
    this.httpClient
      .post('/api/anyfix/goods-post/query', params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const data = res.data;
        this.page.total = data.total;
        this.goodsPostList = data.list || [];
        this.goodsPostList.forEach(item => {
          switch (item.signStatus) {
            case 1:
              item.buttonColor = {'background-color': '#48B8FF'};
              break;
            case 2:
              item.buttonColor = {'background-color': '#FF9F00'};
              break;
            case 3:
              item.buttonColor = {'background-color': '#B5D46F'};
          }
        });
      });
  }

  openDrawer() {
    this.visible = true;
  }

  closeDrawer() {
    this.visible = false;
  }

  clearDrawer() {
    // 清空筛选条件
    this.searchForm.reset();
  }

  /**
   * 初始化显示自定义列
   */
  initShowColumnList() {
    const goodsPostFieldList = localStorage.getItem('goodsPostFieldList');
    if (goodsPostFieldList && goodsPostFieldList != null) {
      const fieldList: any[] = JSON.parse(goodsPostFieldList) || [];
      const list: any[] = [];
      fieldList.forEach((item: any) => {
        if (item.checked) {
          list.push(item.field);
        }
      });
      this.checkedColumnList = [];
      this.customColumnList.forEach((item: any) => {
        if (list.includes(item.field)) {
          item.checked = true;
          this.totalWidth += item.width;
          this.checkedColumnList.push(item);
        } else {
          item.checked = false;
        }
      });
    } else {
      this.checkedColumnList = this.customColumnList;
      this.checkedColumnList.forEach((item: any) => {
        if (item.checked) {
          this.totalWidth += item.width;
        }
      });
    }
  }

  /**
   * 自定义列
   */
  openCustomColumnList() {
    const listJson = JSON.stringify(this.customColumnList);
    const list = JSON.parse(listJson);
    const modal = this.nzModalService.create({
      nzTitle: '自定义列',
      nzContent: CustomListComponent,
      nzFooter: null,
      nzWidth: 900,
      nzComponentParams: {
        titleList: list,
        type: 'goodsPostFieldList'
      }
    });
    modal.afterClose.subscribe((res: any) => {
      if (res) {
        this.resetShowTable(res);
      }
    });
  }

  resetShowTable(fieldList: any[]) {
    this.totalWidth = 300;
    const list: any[] = [];
    fieldList.forEach((item: any) => {
      if (item.checked) {
        list.push(item.field);
      }
    });
    this.checkedColumnList = [];
    this.customColumnList.forEach((item: any) => {
      if (list.includes(item.field)) {
        item.checked = true;
        this.totalWidth += item.width;
        this.checkedColumnList.push(item);
      } else {
        item.checked = false;
      }
    });
  }

  /**
   * 添加物品寄送单
   */
  addGoodsPost() {
    this.router.navigate(['../post-add'],
      {relativeTo: this.activatedRoute});
  }

  /**
   * 物品寄送单详情
   */
  viewGoodsPostDetail(postId) {
    this.router.navigate(['../post-detail'],
      {
        queryParams: {postId},
        relativeTo: this.activatedRoute
      });
  }


  /**
   * 企业列表
   */
  listCorp() {
    this.httpClient
      .get('/api/anyfix/demander-service/relates/' + this.userService.currentCorp.corpId)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        this.corpList = result.data || [];
        this.corpList.unshift({
          corpId: this.userService.currentCorp.corpId,
          corpName: this.userService.currentCorp.shortName
        });
      });
  }

  /**
   * 发货企业改变
   */
  consignCorpChange() {
    const consignCorp = this.searchForm.get('consignCorp').value;
    if (isNull(consignCorp)) {
      return;
    }
    this.searchForm.controls.consignUserName.setValue(null,
      {onlySelf: true, emitViewToModelChange: false});
    this.matchConsignUser();
    this.httpClient
      .get('/api/uas/sys-tenant/' + consignCorp)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        const consignCorpTenant = result.data;
        if (consignCorpTenant != null && consignCorpTenant.serviceProvider === 'Y') {
          this.consignBranchVisible = true;
          this.matchConsignBranch();
        } else {
          this.consignBranchVisible = false;
        }
      });
  }

  /**
   * 模糊查询发货网点
   */
  matchConsignBranch(branchName?: string) {
    const consignCorp = this.searchForm.get('consignCorp').value;
    // 服务网点
    this.listServiceBranch(consignCorp, branchName).then((branchList: any[]) => {
      this.consignBranchList = branchList;
    });
  }

  /**
   * 发货网点改变
   */
  consignBranchChange() {
    this.searchForm.controls.consignUserName.setValue(null,
      {onlySelf: true, emitViewToModelChange: false});
    this.matchConsignUser();
  }

  /**
   * 收货企业改变
   */
  receiveCorpChange() {
    const receiveCorp = this.searchForm.get('receiveCorp').value;
    if (isNull(receiveCorp)) {
      return;
    }
    this.searchForm.controls.receiverName.setValue(null,
      {onlySelf: true, emitViewToModelChange: false});
    this.matchReceiveUser();
    this.httpClient
      .get('/api/uas/sys-tenant/' + receiveCorp)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        const receiveCorpTenant = result.data;
        if (receiveCorpTenant != null && receiveCorpTenant.serviceProvider === 'Y') {
          this.receiveBranchVisible = true;
          this.matchReceiveBranch();
        } else {
          this.receiveBranchVisible = false;
        }
      });
  }

  /**
   * 模糊查询收货网点
   */
  matchReceiveBranch(branchName?: string) {
    const receiveCorp = this.searchForm.get('receiveCorp').value;
    // 服务网点
    this.listServiceBranch(receiveCorp, branchName).then((branchList: any[]) => {
      this.receiveBranchList = branchList;
    });
  }

  /**
   * 收货网点改变
   */
  receiveBranchChange() {
    this.searchForm.controls.receiverName.setValue(null,
      {onlySelf: true, emitViewToModelChange: false});
    this.matchReceiveUser();
  }

  /**
   * 模糊查询发货人
   */
  matchConsignUser(userName?: string) {
    const consignCorp = this.searchForm.get('consignCorp').value;
    const consignBranch = this.searchForm.get('consignBranch').value;
    if (isNull(consignBranch)) {
      this.listCorpUser(consignCorp, userName).then((userList: any[]) => {
        this.consignUserList = userList;
      });
    } else {
      this.listServiceBranchUser(consignCorp, consignBranch, userName).then((userList: any[]) => {
        this.consignUserList = userList;
      });
    }
  }

  /**
   * 模糊查询收货人
   */
  matchReceiveUser(userName?: string) {
    const receiveCorp = this.searchForm.get('receiveCorp').value;
    const receiveBranch = this.searchForm.get('receiveBranch').value;
    if (isNull(receiveBranch)) {
      this.listCorpUser(receiveCorp, userName).then((userList: any[]) => {
        this.receiveUserList = userList;
      });
    } else {
      this.listServiceBranchUser(receiveCorp, receiveBranch, userName).then((userList: any[]) => {
        this.receiveUserList = userList;
      });
    }
  }

  /**
   * 运输方式列表
   */
  listTransportType() {
    this.httpClient
      .get('/api/anyfix/goods-post/transport-type/list')
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        this.transportTypeList = result.data;
      });
  }

  /**
   * 运输方式改变
   */
  transportTypeChange() {
    this.consignTypeVisible = false;
    this.expressTypeVisible = false;
    this.expressCompanyVisible = false;
    this.expressNoVisible = false;
    const transportTypeList: number[] = this.searchForm.get('transportTypeList').value || [];
    if (transportTypeList.includes(2)) {
      // 托运
      this.consignTypeVisible = true;
      this.listConsignType();
    }
    if (transportTypeList.includes(3)) {
      // 快递
      this.expressTypeVisible = true;
      this.expressCompanyVisible = true;
      this.expressNoVisible = true;
      this.listExpressType();
      this.matchExpressCompany();
    }
  }

  /**
   * 托运方式列表
   */
  listConsignType() {
    this.httpClient
      .get('/api/anyfix/goods-post/consign-type/list')
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        this.consignTypeList = result.data;
      });
  }

  /**
   * 快递方式列表
   */
  listExpressType() {
    this.httpClient
      .get('/api/anyfix/goods-post/express-type/list')
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        this.expressTypeList = result.data;
      });
  }

  /**
   * 模糊查询快递公司
   */
  matchExpressCompany(corpName?: string) {
    const params = {
      corpId: this.userService.currentCorp.corpId,
      name: corpName
    };
    this.httpClient
      .post('/api/anyfix/express-company/match', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        this.expressCompanyList = result.data || [];
        if (!isNull(corpName)) {
          let match = false;
          this.expressCompanyList.forEach(option => {
            if (option.name === corpName) {
              match = true;
            }
          });
          if (!match) {
            this.expressCompanyList.unshift({id: '0', name: corpName + '[新增]'});
          }
        }
      });
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
      .subscribe((result: Result) => {
        this.areaList = result.data;
      });
  }

  /**
   * 企业人员列表
   */
  listCorpUser(corpId: string, userName?: string): Promise<any> {
    if (isNull(corpId)) {
      return new Promise<any>((resolve) => {
        resolve([]);
      });
    }
    return new Promise((resolve, reject) => {
      const params = {
        corpId,
        matchFilter: userName
      };
      this.httpClient
        .post('/api/uas/corp-user/match', params)
        .pipe(
          finalize(() => {
            this.cdf.markForCheck();
          })
        )
        .subscribe((result: Result) => {
          const list: any[] = result.data || [];
          if (!isNull(userName)) {
            let match = false;
            list.forEach(option => {
              if (option.userName === userName) {
                match = true;
              }
            });
            if (!match) {
              list.unshift({userId: '0', userName: '[' + userName + ']'});
            }
          }
          resolve(list);
        }, e => {
          throw e;
        });
    });
  }

  /**
   * 查询服务网点
   * @param corpId
   * @param branchName
   */
  private listServiceBranch(corpId: string, branchName?: string): Promise<any> {
    return new Promise((resolve, reject) => {
      const payload = {
        serviceCorp: corpId,
        branchName
      };
      this.httpClient.post('/api/anyfix/service-branch/match', payload)
        .subscribe((res: Result) => {
          const list = res.data;
          resolve(list);
        }, e => {
          throw e;
        });
    });
  }

  /**
   * 服务网点人员列表
   */
  listServiceBranchUser(corpId: string, branchId: string, userName?: string): Promise<any> {
    if (isNull(branchId) || isNull(corpId)) {
      return new Promise<any>((resolve) => {
        resolve([]);
      });
    }
    return new Promise((resolve, reject) => {
      const params = {
        corpId,
        branchId,
        matchFilter: userName
      };
      this.httpClient
        .post('/api/anyfix/service-branch-user/match', params)
        .pipe(
          finalize(() => {
            this.cdf.markForCheck();
          })
        )
        .subscribe((result: Result) => {
          const list: any[] = result.data || [];
          if (!isNull(userName)) {
            let match = false;
            list.forEach(option => {
              if (option.userName === userName) {
                match = true;
              }
            });
            if (!match) {
              list.unshift({userId: '0', userName: '[' + userName + ']'});
            }
          }
          resolve(list);
        }, e => {
          throw e;
        });
    });
  }

}
