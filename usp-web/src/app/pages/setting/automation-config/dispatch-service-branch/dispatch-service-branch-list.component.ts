import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {NzModalService} from 'ng-zorro-antd';
import {DispatchServiceBranchAddComponent} from './dispatch-service-branch-add.component';
import {UserService} from '@core/user/user.service';
import {Page} from '@core/interceptor/result';
import {ANYFIX_RIGHT} from '@core/right/right';
import {ZorroUtils} from '@util/zorro-utils';

/**
 * 进入页面时需要服务商客服所在custom_corp数据（必须）,这个存在多个的可能性
 */
@Component({
  selector: 'app-dispatch-service-branch-list.component',
  templateUrl: 'dispatch-service-branch-list.component.html',
  styleUrls: ['dispatch-service-branch-list.component.less']
})
export class DispatchServiceBranchListComponent implements OnInit {

  ZorroUtils = ZorroUtils;
  aclRight = ANYFIX_RIGHT;

  serviceBranchList: any;
  page = new Page();
  loading = true;

  searchForm: FormGroup;
  demanderCorpOption = [];
  serviceCorpOption = [];
  areaOption = [];
  district = ''; // 表单提交的行政划分参数

  serviceBranchOption = [];
  serviceCorpOptionLoading = false;
  demanderCorpOptionLoading = false;
  serviceBranchOptionLoading = false;
  useFormValue = true;
  drawerVisible: boolean;
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private modalService: NzModalService,
              private userService: UserService) {
    this.searchForm = this.formBuilder.group({
      serviceBranch: [],
      demanderCorp: [],
      district: []
    });
  }

  ngOnInit(): void {
    this.query();
    this.searchServiceBranch();
    this.getAreaOption();
  }

  // 初始化数据
  loadList(params): void {
    this.loading = true;
    this.httpClient
      .post('/api/anyfix/work-dispatch-service-branch/query',
        params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.serviceBranchList = res.data.list;
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
    params.serviceCorp = this.userService.currentCorp.corpId;
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    this.loadList(params);
  }


  add() {
    const modal = this.modalService.create({
      nzTitle: '添加自动分配服务网点设置',
      nzContent: DispatchServiceBranchAddComponent,
      nzComponentParams: {
        pageType: 'add',
        serviceCorp: this.userService.currentCorp.corpId,
        serviceName: this.userService.currentCorp.corpName
      },
      nzFooter: null,
      nzWidth: 900
    });
    modal.afterClose.subscribe(result => {
        if (result === 'update') {
          this.useFormValue = false;
          this.query();
        }
      }
    );
  }


  // 进入修改配置页面
  mod(data): void {
    const modal = this.modalService.create({
      nzTitle: '修改自动分配服务网点设置',
      nzContent: DispatchServiceBranchAddComponent,
      nzComponentParams: {
        pageType: 'mod',
        dispatchServiceBranch: data
      },
      nzFooter: null,
      nzWidth: 900
    });
    modal.afterClose.subscribe(result => {
      if (result === 'update') {
        this.query();
      }
    });
  }

  // 删除确认
  showDeleteConfirm(Id): void {
    this.modalService.confirm({
      nzTitle: '确定删除该规则吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.delete(Id),
      nzCancelText: '取消'
    });
  }

  // 删除配置
  delete(Id) {
    this.httpClient
      .delete('/api/anyfix/work-dispatch-service-branch/' + Id)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        // 如果是为第n页第一条，需要调整为n-1页才有数据显示
        if (this.serviceBranchList.length === 1 && this.page.pageNum > 1) {
          this.page.pageNum = this.page.pageNum - 1;
        }
        this.query();
      });
  }

  searchDemanderCorp(): void {
    // 如果已经加载过，就不要再查了
    if (this.demanderCorpOption != null && this.demanderCorpOption.length > 0) {
      this.demanderCorpOptionLoading = false;
    } else {
      this.demanderCorpOptionLoading = true;
      this.httpClient
        .post('/api/anyfix/demander-service/demander/list', {})
        .pipe(
          finalize(() => {
            this.demanderCorpOptionLoading = false;
          })
        )
        .subscribe((res: any) => {
          this.demanderCorpOption = res.data;
        });
    }
  }

  searchServiceBranch(name?): void {
    const params = {branchName: name};
    this.serviceBranchOptionLoading = true;
    this.httpClient
      .post('/api/anyfix/service-branch/match', params)
      .pipe(
        finalize(() => {
          this.serviceBranchOptionLoading = false;
        })
      )
      .subscribe((res: any) => {
        this.serviceBranchOption = res.data;
      });
  }

  /**
   * 这个就是登陆用户所属公司，即服务商客服所属服务商
   */
  searchOwnCustomCorp(): void {
    if (this.serviceCorpOption != null && this.serviceCorpOption.length > 0) {
      this.serviceCorpOptionLoading = false;

    } else {
      this.serviceCorpOptionLoading = true;
      this.httpClient
        .get('/api/uas/corp-user/listCorpInfos/' + this.userService.userInfo.userId)
        .subscribe((res: any) => {
          this.serviceCorpOptionLoading = false;
          this.serviceCorpOption = res.data;
        });
    }

  }

  getAreaOption(): void {
    this.httpClient.get('/api/uas/area/list')
      .subscribe((res: any) => {
        this.areaOption = res.data;
      });
  }

  // areaChange(event){
  //   if (event[event.length-1] === undefined){
  //     this.district = '';
  //   }else {
  //     this.district = event[event.length-1].value;
  //   }
  // }


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
