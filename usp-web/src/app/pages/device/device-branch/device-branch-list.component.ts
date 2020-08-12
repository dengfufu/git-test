import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {DeviceBranchAddComponent} from './device-branch-add.component';
import {DeviceBranchEditComponent} from './device-branch-edit.component';
import {UserService} from '@core/user/user.service';
import {Page, Result} from '@core/interceptor/result';
import {ActivatedRoute, Router} from '@angular/router';
import {ANYFIX_RIGHT} from '@core/right/right';
import {DeviceBranchService} from './service/device-branch.service';
import { ZorroUtils } from '@util/zorro-utils';

@Component({
  selector: 'app-device-branch-list',
  templateUrl: 'device-branch-list.component.html',
  styleUrls: ['device-branch-list.component.less']
})
export class DeviceBranchListComponent implements OnInit {

  ZorroUtils = ZorroUtils;
  aclRight = ANYFIX_RIGHT;

  demanderCorp = null;
  searchForm: FormGroup;
  page = new Page();

  list: any;
  loading = false;
  serviceCorpList: any;
  customCorpList: any;
  branchOptions = [];
  upperBranchOptions = [];
  nzOptions: any;
  savedPramsValue: any;
  drawerVisible: boolean;
  customCorpModal: any;
  demanderCorpOptions: any = [];
  isServiceProvider = this.userService.currentCorp.serviceProvider  === 'Y';

  nzFilterOption = () => true;


  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              public userService: UserService,
              private modalService: NzModalService,
              private deviceBranchService: DeviceBranchService,
              private router: Router,
              private messageService: NzMessageService,
              private activatedRoute: ActivatedRoute) {
    this.searchForm = this.formBuilder.group({
      branchId: [],
      district: [],
      area: [],
      enabled: ['Y', []],
      customId:[],
      upperBranchId:[],
      customCorp: [],
      corpId: [],
      demanderCorp: [null, [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.getParamsValue();
    this.listAreaDto();
    if( this.isServiceProvider) {
      this.listDemanderCorp();
    } else {
      this.demanderCorp = this.userService.currentCorp.corpId;
      this.initSelect();
    }
    this.queryBranch();
  }

  initSelect() {
    this.matchBranch();
    this.matchCustomCorp();
    this.matchUpperBranch();
  }

  getParamsValue() {
    const params = this.deviceBranchService.paramsValue;
    if (params) {
      this.searchForm.setValue(params.formValue);
      this.page = params.page;
      this.deviceBranchService.paramsValue = null;
    }
  }

  listDemanderCorp() {
    this.httpClient.get('/api/anyfix/demander/list').subscribe((result: Result) => {
      this.demanderCorpOptions = result.data || [];

    });
  }

  saveParamsValue() {
    const params = {
      formValue: this.searchForm.value,
      page: this.page
    };
    this.savedPramsValue = params;
  }

  queryBranch(reset?: boolean) {
    // tslint:disable-next-line:forin
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.saveParamsValue();
    this.httpClient
      .post('/api/anyfix/device-branch/query', this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const data = res.data;
        this.list = data.list || [];
        this.page.total = data.total;
      });
  }

  getParams() {
    const params = Object.assign({}, this.searchForm.value);
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    params.serviceProvider = this.isServiceProvider;
    return params;
  }


  // 进入添加页面
  addModal(): void {
    const modal = this.modalService.create({
      nzTitle: '添加设备网点',
      nzContent: DeviceBranchAddComponent,
      nzFooter: null,
      nzStyle:{'margin-top': '-40px'},
      nzWidth: 800
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.queryBranch();
      }
    });
  }

  // 进入编辑页面
  editModal(data): void {
    const modal = this.modalService.create({
      nzTitle: '编辑设备网点',
      nzContent: DeviceBranchEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        deviceBranch: data
      }
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.queryBranch();
      }
    });
  }

  // 删除确认
  showDeleteConfirm(branchId): void {
    this.modalService.confirm({
      nzTitle: '确定删除该网点吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteBranch(branchId),
      nzCancelText: '取消'
    });
  }

  // 删除
  deleteBranch(branchId) {
    this.httpClient
      .delete('/api/anyfix/device-branch/' + branchId, {})
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        // 如果是为第n页第一条，需要调整为n-1页才有数据显示
        if (this.list.length === 1 && this.page.pageNum > 1) {
          this.page.pageNum = this.page.pageNum - 1;
        }
        this.queryBranch();
      });
  }

  matchBranch(branchName?: string): void {
    const payload = {
      matchFilter: branchName,
      corpId: this.demanderCorp,
      upperBranchId: this.searchForm.controls.upperBranchId.value
    };
    this.httpClient
      .post('/api/anyfix/device-branch/relate/match', payload)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.branchOptions = res.data;
      });
  }

  matchUpperBranch(branchName?: string): void {
    const payload = {
      matchFilter: branchName,
      corpId: this.demanderCorp
    };
    this.httpClient
      .post('/api/anyfix/device-branch/upper-branch', payload)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.upperBranchOptions = res.data;
      });
  }

  listAreaDto(): void {
    this.httpClient
      .get('/api/uas/area/list')
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.nzOptions = res.data;
      });
  }

  /**
   * 网点详情
   * @param branchId 网点编号
   */
  branchDetail(branchId) {
    this.deviceBranchService.paramsValue = this.savedPramsValue;
    this.router.navigate(['../device-branch-list/device-branch-detail'],
      {queryParams: {branchId}, relativeTo: this.activatedRoute});
  }

  openDrawer() {
    this.drawerVisible = true;
  }

  closeDrawer() {
    this.drawerVisible = false;
  }

  clearDrawer() {
    this.customCorpModal = null;
    this.searchForm.reset();
  }

  areaChange(event) {
    if (event === null) {
      this.searchForm.controls.district.setValue(null);
      return;
    }
    if (event !== undefined){
      this.searchForm.controls.district.setValue(event[event.length - 1]);
    }
  }

  // 客户列表
  matchCustomCorp(customCorpName?: string) {
    this.httpClient.post('/api/anyfix/demander-custom/custom/list', {
      customCorpName,
      demanderCorp: this.demanderCorp
    })
    .subscribe((res: any) => {
      this.customCorpList = res.data;
    });
  }



  upperBranchChange() {
    this.searchForm.controls.branchId.setValue(null);
    this.matchBranch();
  }

  customChange(data) {
    this.searchForm.controls.customCorp.setValue(null);
    this.searchForm.controls.customId.setValue(null);
    if (!data) {
      return;
    }
    if( data.demander === true) {
      this.searchForm.controls.customCorp.setValue(data.customCorp);
    } else {
      this.searchForm.controls.customId.setValue(data.customId);
    }
  }

  clickSelect() {
    if(!this.demanderCorp) {
      this.messageService.error('请先选择委托商');
    }
  }

  demanderChange(demanderCorp) {
    this.resetData();
    this.demanderCorp = demanderCorp;
    if(demanderCorp) {
      this.initSelect();
    }
  }

  resetData() {
    this.upperBranchOptions = [];
    this.branchOptions = [];
    this.customCorpList = [];
    this.searchForm.controls.upperBranchId.setValue(null);
    this.searchForm.controls.branchId.setValue(null);
    this.searchForm.controls.customId.setValue(null);
    this.customCorpModal = null;
  }
}
