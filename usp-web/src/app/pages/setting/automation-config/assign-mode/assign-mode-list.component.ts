import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {NzModalService} from 'ng-zorro-antd';
import {AssignModeAddComponent} from './assign-mode-add.component';
import {UserService} from '@core/user/user.service';
import {Page} from '@core/interceptor/result';
import {ANYFIX_RIGHT} from '@core/right/right';
import {ZorroUtils} from '@util/zorro-utils';

@Component({
  selector: 'app-assign-mode-list.component',
  templateUrl: 'assign-mode-list.component.html',
  styleUrls: ['assign-mode-list.component.less']
})
export class AssignModeListComponent implements OnInit {

  ZorroUtils = ZorroUtils;
  aclRight = ANYFIX_RIGHT;

  assignModeList: any;
  page = new Page();
  loading = true;
  drawerVisible = false;

  searchForm: FormGroup;
  useFormValue = true;
  demanderCorpOption = [];
  demanderCorpOptionLoading = false;

  areaOption: any[] = [];
  district = ''; // 表单提交的行政划分参数

  autoModeMap = {
    20: '派给设备负责工程师',
    30: '派给小组',
    40: '距离优先',
    50: '派给网点所有人'
  };

  manualModeMap = {
    1: '公司客服派单',
    2: '设备服务主管派单',
    3: '服务网点客服派单'
  };

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private modalService: NzModalService,
              private userService: UserService) {
    this.searchForm = this.formBuilder.group({
      demanderCorp: [],
      district: []
    });
  }

  ngOnInit(): void {
    this.query();
    this.getAreaOption();
  }

  // 初始化数据
  loadList(params): void {
    this.loading = true;
    this.httpClient
      .post('/api/anyfix/work-assign-mode/query',
        params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.assignModeList = res.data.list;
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
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    this.loadList(params);
  }

  add() {
    const modal = this.modalService.create({
      nzTitle: '添加派单模式设置',
      nzContent: AssignModeAddComponent,
      nzComponentParams: {
        pageType: 'add',
        serviceCorp: this.userService.currentCorp.corpId,
        serviceName: this.userService.currentCorp.corpName
      },
      nzFooter: null,
      nzWidth: 1000
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.useFormValue = false;
        this.query();
      }
    });
  }

  mod(data): void {
    const modal = this.modalService.create({
      nzTitle: '修改服务商派单模式设置',
      nzContent: AssignModeAddComponent,
      nzComponentParams: {
        pageType: 'mod',
        assignMode: data
      },
      nzFooter: null,
      nzWidth: 1000
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.query();
      }
    });
  }


  // 删除确认
  showDeleteConfirm(Id): void {
    this.modalService.confirm({
      nzTitle: '确定删除该模式吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.delete(Id),
      nzCancelText: '取消'
    });
  }

  // 删除配置
  delete(Id) {
    this.httpClient
      .delete('/api/anyfix/work-assign-mode/' + Id)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        // 如果是为第n页第一条，需要调整为n-1页才有数据显示
        if (this.assignModeList.length === 1 && this.page.pageNum > 1) {
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

  getAreaOption(): void {
    this.httpClient.get('/api/uas/area/list')
      .subscribe((res: any) => {
        this.areaOption = res.data;
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
    this.district = '';
  }

}
