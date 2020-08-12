import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {WorkRemindAddComponent} from '../add/work-remind-add.component';
import {WorkRemindEditComponent} from '../edit/work-remind-edit.component';
import {Page, Result} from '@core/interceptor/result';
import {ANYFIX_RIGHT} from '@core/right/right';
import {UserService} from '@core/user/user.service';
import {AnyfixModuleService} from '@core/service/anyfix-module.service';

@Component({
  selector: 'app-work-remind-config-list',
  templateUrl: 'work-remind-config-list.component.html',
  styleUrls: ['work-remind-config-list.component.less']
})
export class WorkRemindConfigListComponent implements OnInit {

  aclRight = ANYFIX_RIGHT;

  searchForm: FormGroup;
  page = new Page();
  list: any[];
  workSysTypeList = [];
  loading = false;
  drawerVisible: boolean;
  // 委托商列表
  demanderCorpList = [];
  useFormValue = true;
  totalWidth = 450;
  optionList: any = {};
  params: any = {};
  branchOption : any = [];
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              public userService : UserService,
              private msgService: NzMessageService,
              private anyfixModuleService: AnyfixModuleService,
              private modalService: NzModalService) {
    this.searchForm = this.formBuilder.group({
      demanderCorp: [],
      workType: [],
      enabled: [],
      remindTypes: [],
      customId: [],
      serviceBranches: [],
      smallClassId:[],
      deviceModel:[],
      deviceBrand:[],
      district: [],
      area:[]
    });
  }

  ngOnInit(): void {
    this.loadDemanderCorp();
    this.loadWorkSysType();
    this.getBranchOption('');
    this.initOptionList();
    this.queryWorkRemind();
  }

  // 加载委托商列表
  loadDemanderCorp() {
    this.httpClient.post(`/api/anyfix/demander-service/demander/list`,
    {
        serviceCorp: this.userService.currentCorp.corpId,
        demanderCorp: this.userService.currentCorp.corpId
       }).subscribe((result: Result) => {
      if (result.code === 0) {
        this.demanderCorpList = result.data || [];
      }
    });
  }

  loadWorkSysType() {
    this.httpClient
      .get('/api/anyfix/work-sys-type/list')
      .subscribe((res: any) => {
        this.workSysTypeList = res.data;
      });
  }

  loadList(params): void {
    this.loading = true;
    this.httpClient
      .post('/api/anyfix/work-remind/query',
        params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const data = res.data;
        this.list = data.list;
        this.page.total = data.total;
      });
  }

  queryWorkRemind(reset?: boolean) {
    if (reset) {
      this.page.pageNum = 1;
    }
    let params: any = {};
    params = Object.assign({}, this.searchForm.value);
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    this.loadList(params);
  }

  // 进入添加页面
  addWorkRemindModal(): void {
    const modal = this.modalService.create({
      nzTitle: '添加工单预警设置',
      nzContent: WorkRemindAddComponent,
      nzComponentParams: {
        pageType: 'add',
        serviceCorp: this.userService.currentCorp.corpId,
        serviceName: this.userService.currentCorp.corpName
      },
      nzFooter: null,
      nzWidth: 1000,
      nzStyle: {'margin-top': '-60px'}
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.useFormValue = false;
        this.queryWorkRemind();
      }
    });
  }

  // 进入编辑页面
  editWorkRemindModal(data): void {
    const modal = this.modalService.create({
      nzTitle: '编辑工单预警设置',
      nzContent: WorkRemindEditComponent,
      nzFooter: null,
      nzWidth: 1000,
      nzStyle: {'margin-top': '-60px'},
      nzComponentParams: {
        workRemind: data
      }
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.queryWorkRemind();
      }
    });
  }

  // 删除确认
  showDeleteConfirm(remindId): void {
    this.modalService.confirm({
      nzTitle: '确定删除吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteWorkRemind(remindId),
      nzCancelText: '取消'
    });
  }

  // 删除配置
  deleteWorkRemind(id) {
    this.httpClient
      .delete('/api/anyfix/work-remind/' + id)
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
        this.queryWorkRemind();
      });
  }

  initOptionList() {
    this.anyfixModuleService.getWorkType().subscribe((res: any) => {
      this.optionList.workTypeOption = res.data;
    });
    this.anyfixModuleService.getWorkSourceOption().subscribe((res: any) => {
      this.optionList.workSourceOption = res.data;
    });
    this.anyfixModuleService.getDemanderOption().subscribe((res:any)=>{
      this.optionList.demanderOption = res.data;
    });
    this.anyfixModuleService.getCustomOption().subscribe((res: any) => {
      this.optionList.customOption = res.data;
    });
    this.anyfixModuleService.getAreaInfoOption().subscribe((res: any) => {
      this.optionList.areaInfoOption = res.data;
    });
    this.anyfixModuleService.getProvinceOption().subscribe((res: any) => {
      this.optionList.provinceOption = res.data;
    });
    this.anyfixModuleService.getWorkRemindType().subscribe((res: any) => {
      this.optionList.workRemindTypeOption = res.data;
    });
  }

  areaChange(event) {
    if (event === null) {
      this.searchForm.controls.district.setValue(null);
      return;
    }
    if (event !== undefined){
      if(event.length > 2) {
        this.searchForm.controls.district.setValue(event[2]);
      }else if(event.length > 1) {
        this.searchForm.controls.district.setValue(event[1]);
      } else if(event.length === 1) {
        this.searchForm.controls.district.setValue(event[0]);
      }
    }
  }

  getBranchOption(event) {
    this.httpClient
      .post('/api/anyfix/service-branch/match', {
        serviceCorp: this.userService.currentCorp.corpId,
        branchName : event
      })
      .subscribe((res:any) => {
        this.branchOption = res.data;
      });
  }

  /**
   * 委托商改变
   */
  changeDemanderCorp(event) {
    if (!event) {
      return;
    }
    this.anyfixModuleService.getDeviceSmallClassOptionByDemanderCorp(event).subscribe((res: any) => {
      this.optionList.deviceSmallClassOption = res.data;
    });
    this.anyfixModuleService.getDeviceModelOptionByDemanderCorp(event).subscribe((res: any) => {
      this.optionList.deviceModelOption = res.data;
    });
    this.anyfixModuleService.getDeviceBrandOptionByDemanderCorp(event).subscribe((res: any) => {
      this.optionList.deviceBrandOption = res.data;
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
