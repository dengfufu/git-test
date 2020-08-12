import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {finalize} from 'rxjs/operators';
import {Page, Result} from '@core/interceptor/result';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {ServiceDemanderAddComponent} from '../service-demander-add.component';
import {ConfigItemEditComponent} from '../../../setting/work-config/config-item/config-item-edit.component';
import {FileConfigAddComponent} from './fileconfig/file-config-add.component';
import {ANYFIX_RIGHT} from '@core/right/right';
import {ContConfigComponent} from './contconfig/cont-config.component';
import {environment} from '@env/environment';
import {deepCopy} from '@delon/util';
import {ServiceDemanderEditComponent} from './edit/service-demander-edit.component';
import {DemanderService} from '../demander.service';
import {AutoConfigComponent} from './auto-config/auto-config.component';

@Component({
  selector: 'app-corp-detail',
  templateUrl: 'demander-detail.component.html',
  styleUrls: ['demander-detail.component.less']
})
export class DemanderDetailComponent implements OnInit {

  aclRight = ANYFIX_RIGHT;
  detail: any = {};
  imageList: any[] = [];
  serviceImageList: any[] = [];
  dataConfigList: any = [];
  fileConfigList: any = [];
  demanderContList:any = [];
  loading = false;
  configPage = new Page();
  page = new Page();
  fileConfigPage = new Page();
  demanderContPage = new Page();
  customCorp = '';
  customId = '';
  demanderCorp = '';
  fileConfigLoading = false;
  configLoading = false;
  spinning = false;
  address = '';
  id = '';
  corpId = this.userService.currentCorp.corpId;
  demanderContLoading = false;

  editManagerFlag = false; // 编辑客户经理
  managerCheckedList: any[] = []; // 选中的客户经理列表
  managerList: any[] = []; // 客户经理列表
  manager: any;
  userList: any[] = [];

  // 委托商自动化配置
  autoConfig: any = {};

  showFileUrl = environment.server_url + '/api/file/showFile?fileId=';
  isVisible = false;
  detailDescription = '';
  SHOW_FEE_DESCRIPTION = 1;
  SHOW_SERVICE_DESCRIPTION = 2;
  title = '';
  nzFilterOption = () => true;

  constructor(private httpClient: HttpClient,
              private modalService: NzModalService,
              private router: Router,
              private msg: NzMessageService,
              private activatedRoute: ActivatedRoute,
              private cdf: ChangeDetectorRef,
              private userService: UserService,
              public demanderService: DemanderService) {
    this.id = this.activatedRoute.snapshot.queryParams.id;
    this.queryDetail();
  }

  ngOnInit() {
    this.dataConfigList = this.demanderService.dataConfigList;
    this.getConfigData();
    this.findManagers();
    this.queryDemanderContList();
  }

  /**
   * 初始化网点数据
   */
  queryDetail() {
    this.spinning = true;
    this.httpClient
      .get('/api/anyfix/demander-service/demander/detail/' + this.id)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
          this.spinning = false;
        })
      )
      .subscribe((res: any) => {
        this.detail = res.data;
        this.autoConfig = this.detail.demanderAutoConfigDto || {};
        // this.settleType = this.detail.settleType === 0 ? 2 : this.detail.settleType;
        // this.settleDay = this.detail.settleDay === 0 ? 1 : this.detail.settleDay;
        this.getFileConfigData();
        this.listManager(this.detail.managerList, this.detail.managerNameList);
      });
  }

  listManager(managerList: any[], managerNameList: any[]) {
    if (managerList && managerNameList) {
      const list: any[] = [];
      managerList.forEach((item, index) => {
        const user: any = {};
        user.userId = item;
        user.userName = managerNameList[index];
        list.push(user);
      });
      this.managerList = [...list];
    }
  }

  getFileConfigParams() {
    const params: any = {};
    params.pageNum = this.fileConfigPage.pageNum;
    params.pageSize = this.fileConfigPage.pageSize;
    params.refId = this.detail.id;
    return params;
  }

  goBack() {
    history.go(-1);
  }

  // 进入添加页面
  addModal(isForUpdate, data): void {
    const modal = this.modalService.create({
      nzTitle: '编辑委托商',
      nzContent: ServiceDemanderAddComponent,
      nzFooter: null,
      nzMaskClosable: false,
      nzWidth: 800,
      nzComponentParams: {
        isForUpdate,
        data
      }
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.queryDetail();
      }
    });
  }

  // 进入添加页面
  editModal(): void {
    const modal = this.modalService.create({
      nzTitle: '编辑委托商',
      nzContent: ServiceDemanderEditComponent,
      nzFooter: null,
      nzMaskClosable: false,
      nzWidth: 1000,
      nzComponentParams: {
        detail: this.detail
      }
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.queryDetail();
      }
    });
  }

  // 加载配置
  getConfigData() {
    this.configLoading = true;
    this.httpClient
      .post('/api/anyfix/service-config/demander/getConfig', {
        referId: this.id,
        itemIdList: this.demanderService.dataItemIdList
      })
      .pipe(
        finalize(() => {
          this.configLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const data = res.data;
        if (data && data.length > 0) {
          data.forEach(item => {
            for (const configItem of this.dataConfigList) {
              if (item.itemId === configItem.itemId) {
                configItem.itemValue = item.itemValue;
                break;
              }
            }
          });
        }
      });
  }

  configData(data) {
    // 进入编辑页面
    const modal = this.modalService.create({
      nzTitle: '编辑数据项',
      nzContent: ConfigItemEditComponent,
      nzFooter: null,
      nzWidth: 500,
      nzComponentParams: {
        data,
        isForDemander: true,
        id: this.id
      }
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.getConfigData();
      }
    });
  }


  getFileConfigData() {
    this.fileConfigLoading = true;
    this.httpClient
      .post('/api/anyfix/file-config/query', this.getFileConfigParams())
      .pipe(
        finalize(() => {
          this.fileConfigLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        if (res.data == null) {
          return;
        }
        this.fileConfigList = res.data.list;
        this.fileConfigPage.total = res.data.total;
      });
  }

  addFileConfig(data) {
    const params: any = {
      refId: this.detail.id
    };
    let title = '';
    if (data) {
      params.isForUpdate = true;
      params.data = data;
      title = '修改附件类型';
    } else {
      title = '新增附件类型';
    }
    const modal = this.modalService.create({
      nzTitle: title,
      nzContent: FileConfigAddComponent,
      nzFooter: null,
      nzMaskClosable: false,
      nzWidth: 800,
      nzComponentParams: params
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.getFileConfigData();
      }
    });
  }

  // 删除确认
  showDeleteFileConfig(id): void {
    this.modalService.confirm({
      nzTitle: '确定删除该附件配置吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteFileConfig(id),
      nzCancelText: '取消'
    });
  }

  deleteFileConfig(id) {
    this.httpClient
      .delete('/api/anyfix/file-config/' + id)
      .pipe(
        finalize(() => {
        })
      )
      .subscribe((res: any) => {
        // 如果是为第n页第一条，需要调整为n-1页才有数据显示
        if (this.fileConfigList.length === 1 && this.page.pageNum > 1) {
          this.fileConfigPage.pageNum = this.fileConfigPage.pageNum - 1;
        }
        this.getFileConfigData();
      });
  }

  // 删除确认
  showDeleteConfirmDemander(id): void {
    this.modalService.confirm({
      nzTitle: '确定删除吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteDemanderService(id),
      nzCancelText: '取消'
    });
  }


  deleteDemanderService(id) {
    this.httpClient
      .delete('/api/anyfix/demander-service/' + id)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        this.queryDemanderContList();
      });
  }

  showContConfig(data) {
    // 进入编辑页面
    const modal = this.modalService.create({
      nzTitle: '委托协议设置',
      nzContent: ContConfigComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        refId: this.id,
        data,
        isForUpdate: data !== null
      }
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.queryDemanderContList();
      }
    });
  }

  getImageUrls(imageList: any[]) {
    imageList = imageList || [];
    const temp: string[] = [];
    imageList.forEach((fileId: any) => {
      temp.push(this.showFileUrl + fileId);
    });
    return temp;
  }

  /**
   * 编辑客户经理
   */
  editManager() {
    this.managerCheckedList = [...this.managerList];
    this.editManagerFlag = true;
    this.matchCorpUser();
  }

  matchCorpUser(filter?) {
    const params = {corpId: this.detail.serviceCorp, matchFilter: filter};
    this.httpClient
      .post('/api/uas/corp-user/match', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const list = res.data || [];
        const newList: any[] = [];
        list.forEach(item => {
          newList.push({
            userId: item.userId,
            userName: item.userName
          });
        });
        this.userList = newList;
      });
  }

  /**
   * 获得客户经理
   */
  findManagers() {
    const params = {
      referId: this.id
    };
    this.httpClient
      .post('/api/anyfix/demander-service-manager/findManagers', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      ).subscribe((result: Result) => {
      const demanderServiceManager = result.data;
      if (demanderServiceManager) {
        const managerList = demanderServiceManager.managerList || [];
        const managerNameList = demanderServiceManager.managerNameList || [];
        this.managerCheckedList = [];
        managerList.forEach((manager, index) => {
          const obj: any = {};
          obj.userId = manager;
          obj.userName = managerNameList[index];
          this.managerCheckedList.push(obj);
        });
        this.managerList = [...this.managerCheckedList];
      }
    });
  }

  checkManager() {
    const manager = deepCopy(this.manager);
    if (manager) {
      this.managerCheckedList = [...this.managerCheckedList, manager];
      this.managerCheckedList = this.removeRepeat(this.managerCheckedList);
    }
    this.manager = null;
  }

  /**
   * 去除重复项
   */
  removeRepeat(list: any[]) {
    const managerList: any[] = [];
    if (list && list.length > 0) {
      const userIdList: any[] = [];
      list.forEach(item => {
        if (!userIdList.includes(item.userId)) {
          userIdList.push(item.userId);
          managerList.push(item);
        }
      });
    }
    return managerList;
  }

  // 删除选中的人员
  deleteManager(userId: string) {
    this.managerCheckedList = this.managerCheckedList.filter((item: any) => item.userId !== userId);
  }

  /**
   * 取消
   */
  cancelManager() {
    this.editManagerFlag = false;
    this.manager = null;
  }

  submitManager() {
    let managerList: any[] = [];
    if (this.managerCheckedList) {
      const list: any[] = [];
      this.managerCheckedList.forEach(item => {
        const obj: any = {};
        obj.manager = item.userId;
        list.push(obj);
      });
      managerList = [...list];
    }
    const params = {
      referId: this.detail.id,
      demanderServiceManagerList: managerList
    };
    this.spinning = true;
    this.httpClient
      .post('/api/anyfix/demander-service-manager/edit', params)
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.manager = null;
        this.editManagerFlag = false;
        this.findManagers();
      });
  }

  queryDemanderContList() {
    this.demanderContLoading = true;
    this.httpClient
      .post('/api/anyfix/demander-cont/query', {
        refId: this.id,
        pageNum: this.demanderContPage.pageNum,
        pageSize: this.demanderContPage.pageSize
      })
      .pipe(
        finalize(() => {
          this.demanderContLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        if(res.data) {
          this.demanderContList = res.data.list;
          this.demanderContPage.total = res.data.total || 0;
          for( const item of this.demanderContList) {
            item.feeRuleFileList = item.feeRuleFileList || [];
            item.serviceStandardFileList = item.serviceStandardFileList || [];
          }
        } else {
          this.demanderContList = [];
          this.demanderContPage.total = 0;
        }
      });
  }

  deleteCont(id: any) {
    this.httpClient
      .delete('/api/anyfix/demander-cont/' + id)
      .pipe(
        finalize(() => {
        })
      )
      .subscribe((res: any) => {
        // 如果是为第n页第一条，需要调整为n-1页才有数据显示
        if (this.demanderContList.length === 1 && this.demanderContPage.pageNum > 1) {
          this.demanderContPage.pageNum = this.demanderContPage.pageNum - 1;
        }
        this.queryDemanderContList();
      });
  }

  showDeleteTip(id) {
    this.modalService.confirm({
      nzTitle: '确定删除吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteCont(id),
      nzCancelText: '取消'
    });
  }

  showMoreDetail(type, description: any) {
    this.isVisible = true;
    this.detailDescription = description;
    this.title = type === this.SHOW_FEE_DESCRIPTION?  '收费规则描述': '服务标准描述';

  }

  handleCancel() {
    this.isVisible = false;
  }

  handleOk() {

  }

  cutStr(description : string ) {
    if(description && description.length > 20) {
      description = description.substr(0,20) +'...';
    }
    return description;
  }
  // 编辑自动化配置
  editAutoConfig() {
    const modal = this.modalService.create({
      nzTitle: '自动化配置',
      nzContent: AutoConfigComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        demanderAutoConfig: this.detail.demanderAutoConfigDto,
        demanderServiceId: this.detail.id
      }
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.queryDetail();
      }
    });
  }

  // 取消编辑结算周期
  // cancelEditSettlePeriod() {
  //   this.editSettlePeriodFlag = false;
  //   this.settleType = this.detail.settleType;
  //   this.settleDay = this.detail.settleDay;
  // }

  // 提交编辑结算周期
  // submitSettlePeriod() {
  //   const params = {
  //     id: this.detail.id,
  //     settleType: this.settleType,
  //     settleDay: this.settleType === 1 ? 0 : this.settleDay
  //   };
  //   this.loading = true;
  //   this.httpClient.post('/api/anyfix/demander-service/updateSettlePeriod', params)
  //     .pipe(
  //       finalize(() => {
  //         this.loading = false;
  //       })
  //     ).subscribe((res: any) => {
  //       this.editSettlePeriodFlag = false;
  //       this.msg.success('编辑成功！');
  //       // 刷新页面
  //       this.queryDetail();
  //   })
  // }

}
