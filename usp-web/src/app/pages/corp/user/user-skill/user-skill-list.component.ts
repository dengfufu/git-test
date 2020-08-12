import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalService} from 'ng-zorro-antd';
import {UserSkillAddComponent} from './user-skill-add.component';
import {UserSkillEditComponent} from './user-skill-edit.component';
import {UserService} from '@core/user/user.service';
import {Page} from '@core/interceptor/result';
import {ANYFIX_RIGHT} from '@core/right/right';

@Component({
  selector: 'app-corp-user-skill-list',
  templateUrl: 'user-skill-list.component.html',
  styleUrls: ['user-skill-list.component.less']
})
export class UserSkillListComponent implements OnInit {

  aclRight = ANYFIX_RIGHT;

  nzOptions: any;
  values: any;

  searchForm: FormGroup;
  page = new Page();
  loading = false;
  userOptionLoading = false;
  drawerVisible = false;

  deviceLargeClassOptionLoading = false;
  deviceLargeClassOptions = [];

  deviceSmallClassOptionLoading = false;
  deviceSmallClassOptions = [];

  demanderCorpOptionLoading = false;
  demanderCorpOptions = [];

  deviceBrandOptionLoading = false;
  deviceBrandOptions = [];

  deviceModelOptionLoading = false;
  deviceModelOptions = [];

  workTypeOptionLoading = false;
  workTypeOptions = [];

  list: any;
  customCorpList: any;
  listOfOption: Array<{value: string; text: string}> = [];
  userOptions: any;
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private userService: UserService,
              private modalService: NzModalService) {
    this.searchForm = this.formBuilder.group({
      userId: [],
      demanderCorp: [],
      largeClassId: [],
      smallClassId: [],
      workType: [],
      brandId: [],
      modelId: []
    });
  }

  ngOnInit(): void {
    this.querySkill();
    this.listUser();
  }

  loadList(params): void {
    this.loading = true;
    this.httpClient
      .post('/api/anyfix/staff-skill/query', params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const data = res.data;
        this.list = res.data.list;
        this.page.total = data.total;
      });
  }

  querySkill(reset: boolean = false,useFormValue: boolean = true) {
    if(reset) {
      this.page.pageNum = 1;
    }
    this.loadList(this.getParams(useFormValue));
  }

  getParams(userFormValue: boolean = true) {
    let params: any = {};
    if (userFormValue) {
      params = Object.assign({},this.searchForm.value);
    }
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    return params;
  }

  // 进入添加页面
  addModal(): void {
    const modal = this.modalService.create({
      nzTitle: '添加技能',
      nzContent: UserSkillAddComponent,
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.querySkill(true,false);
      }
    });
  }

  // 进入添加页面
  editModal(data): void {
    const modal = this.modalService.create({
      nzTitle: '编辑技能',
      nzContent: UserSkillEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        userSkill: data
      }
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.querySkill();
      }
    });
  }

  // 删除确认
  showDeleteConfirm(configId): void {
    this.modalService.confirm({
      nzTitle: '确定删除该人员技能吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteSkill(configId),
      nzCancelText: '取消'
    });
  }

  // 删除
  deleteSkill(id) {
    this.httpClient
      .delete('/api/anyfix/staff-skill/' + id)
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
        this.querySkill();
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

  matchUser(userName: string) {
    this.listUser(userName);
  }

  listUser(userName?): void {
    const payload = {
      corpId: this.userService.currentCorp.corpId,
      matchFilter: userName
    };
    this.userOptionLoading = true;
    this.httpClient
      .post('/api/uas/corp-user/match', payload)
      .pipe(
        finalize(() => {
          this.userOptionLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const listOfOption: Array<{value: string; text: string}> = [];
        res.data.forEach(item => {
          listOfOption.push({
            value: item.userId,
            text: item.userName
          });
        });
        this.userOptions = listOfOption;
      });
  }

  /**
   * 模糊查询设备大类
   * @param largeClassName 大类名称
   */
  matchDeviceLargeClass(largeClassName?) {
    const payload = {
      corp: this.searchForm.controls.demanderCorp.value,
      matchFilter: largeClassName
    };
    this.deviceLargeClassOptionLoading = true;
    this.httpClient
      .post('/api/device/device-large-class/match', payload)
      .pipe(
        finalize(() => {
          this.deviceLargeClassOptionLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.deviceLargeClassOptions = res.data;
      });
  }

  /**
   * 设备大类改变
   */
  changeDeviceLargeClass() {
    this.searchForm.controls.smallClassId.setValue(null);
    this.matchDeviceSmallClass();
  }

  /**
   * 模糊查询设备类型
   * @param smallClassName 设备类型名称
   */
  matchDeviceSmallClass(smallClassName?) {
    const payload = {
      largeClassId: this.searchForm.controls.largeClassId.value,
      matchFilter: smallClassName
    };
    this.deviceSmallClassOptionLoading = true;
    this.httpClient
      .post('/api/device/device-small-class/match', payload)
      .pipe(
        finalize(() => {
          this.deviceSmallClassOptionLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.deviceSmallClassOptions = res.data;
      });
  }

  /**
   * 改变设备类型
   */
  changeDeviceSmallClass() {
    this.searchForm.controls.modelId.setValue(null);
    this.matchDeviceModel();
  }

  /**
   * 模糊查询设备型号
   * @param modelName 型号名称
   */
  matchDeviceModel(modelName?): void {
    const payload = {
      corp: this.searchForm.controls.demanderCorp.value,
      brandId: this.searchForm.controls.brandId.value,
      smallClassId: this.searchForm.controls.smallClassId.value,
      matchFilter: modelName
    };
    this.deviceModelOptionLoading = true;
    this.httpClient
      .post('/api/device/device-model/match', payload)
      .pipe(
        finalize(() => {
          this.deviceModelOptionLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.deviceModelOptions = res.data;
      });
  }

  /**
   * 改变设备品牌
   */
  changeDeviceBrand() {
    this.searchForm.controls.modelId.setValue(null);
    this.matchDeviceModel();
  }

  /**
   * 模糊查询设备品牌
   * @param brandName 品牌名称
   */
  matchDeviceBrand(brandName?): void {
    const payload = {
      corp: this.searchForm.controls.demanderCorp.value,
      matchFilter: brandName
    };
    this.deviceBrandOptionLoading = true;
    this.httpClient
      .post('/api/device/device-brand/match', payload)
      .pipe(
        finalize(() => {
          this.deviceBrandOptionLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.deviceBrandOptions = res.data;
      });
  }

  /**
   * 工单类型列表
   */
  listWorkType() {
    const corpId = this.searchForm.controls.demanderCorp.value;
    this.workTypeOptionLoading = true;
    this.httpClient
      .post('/api/anyfix/work-type/list', {demanderCorp: corpId, enabled: 'Y'})
      .pipe(
        finalize(() => {
          this.workTypeOptionLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.workTypeOptions = res.data;
      });
  }

  /**
   * 改变委托商
   */
  changeCorp() {
    this.searchForm.controls.workType.setValue(null);
    this.searchForm.controls.largeClassId.setValue(null);
    this.searchForm.controls.smallClassId.setValue(null);
    this.searchForm.controls.brandId.setValue(null);
    this.searchForm.controls.modelId.setValue(null);
    this.matchDeviceLargeClass();
    this.matchDeviceModel();
    this.matchDeviceBrand();
  }

  /**
   * 委托商列表
   */
  listDemanderCorp() {
    this.demanderCorpOptionLoading = true;
    this.httpClient
      .post('/api/anyfix/demander-service/demander/list', {})
      .pipe(
        finalize(() => {
          this.demanderCorpOptionLoading = false;
        })
      )
      .subscribe((res: any) => {
        this.demanderCorpOptions = res.data;
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
