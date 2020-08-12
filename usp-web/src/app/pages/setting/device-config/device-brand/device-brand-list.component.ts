import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {DeviceBrandAddComponent} from './device-brand-add.component';
import {DeviceBrandEditComponent} from './device-brand-edit.component';
import {environment} from '@env/environment';
import {Page, Result} from '@core/interceptor/result';
import {DEVICE_RIGHT} from '@core/right/right';
import {UserService} from '@core/user/user.service';


@Component({
  selector: 'app-device-brand-list',
  templateUrl: 'device-brand-list.component.html',
  styleUrls: ['device-brand-list.component.less']
})
export class DeviceBrandListComponent implements OnInit {

  aclRight = DEVICE_RIGHT;

  validateForm: FormGroup;
  deviceBrandList: any;
  page = new Page();
  loading = true;
  baseFileUrl = environment.server_url + '/api/file/showFile?fileId=';
  drawerVisible: boolean;
  useFormValue = true;

  demanderCorpList: any[] = [];

  resetForm(): void {
    this.validateForm.reset();
  }

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private userService : UserService,
              private msgService : NzMessageService,
              private modalService: NzModalService) {
    this.validateForm = this.formBuilder.group({
      name: [],
      corp: [],
      enabled: ['Y']
    });
  }

  ngOnInit(): void {
    this.listDemanderCorp();
  }

  // 初始化数据
  loadDeviceBrandList(params): void {
    this.loading = true;
    this.httpClient.post('/api/device/device-brand/query', params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.deviceBrandList = res.data.list;
        this.page.total = res.data.total;
      });
  }

  queryDeviceBrand(reset: boolean = false) {
    if(reset) {
      this.page.pageNum = 1;
    }
    let params: any = {};
    if (this.useFormValue) {
      params = Object.assign({},this.validateForm.value);
    }
    params.corp = this.validateForm.value.corp;
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    this.loadDeviceBrandList(params);
  }

  /**
   * 委托商列表
   */
  listDemanderCorp() {
    this.httpClient.post(`/api/anyfix/demander-service/demander/list`,
      {
        serviceCorp: this.userService.currentCorp.corpId,
        demanderCorp: this.userService.currentCorp.corpId
      }).subscribe((result: Result) => {
      if (result.code === 0) {
        this.demanderCorpList = result.data || [];
        this.queryDeviceBrand();
      }
    });
  }

  // 进入添加品牌页面
  addDeviceBrand(): void {
    const modal = this.modalService.create({
      nzTitle: '添加品牌',
      nzContent: DeviceBrandAddComponent,
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        // 不进行使用筛选条件
        this.useFormValue = false;
        this.queryDeviceBrand(true);
      }
    });
  }

  // 进入编辑品牌页面
  editDeviceBrand(data): void {
    const modal = this.modalService.create({
      nzTitle: '编辑品牌',
      nzContent: DeviceBrandEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        deviceBrand: data
      }
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        this.queryDeviceBrand();
      }
    });
  }

  // 删除确认
  showDeleteConfirm(deviceBrandId): void {
    this.modalService.confirm({
      nzTitle: '确定删除该品牌吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteDeviceBrand(deviceBrandId),
      nzCancelText: '取消'
    });
  }

  // 删除品牌
  deleteDeviceBrand(deviceBrandId) {
    this.httpClient
      .delete('/api/device/device-brand/' + deviceBrandId)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        // 如果是为第n页第一条，需要调整为n-1页才有数据显示
        if (this.deviceBrandList.length === 1 && this.page.pageNum > 1) {
          this.page.pageNum = this.page.pageNum - 1;
        }
        this.queryDeviceBrand();
      });
  }

  openDrawer() {
    this.drawerVisible = true;
  }

  closeDrawer() {
    this.drawerVisible = false;
  }

  clearDrawer() {
    this.validateForm.reset();
  }

}
