import {ChangeDetectorRef, Component, OnInit} from '@angular/core'
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {DeviceModelAddComponent} from './device-model-add.component';
import {DeviceModelEditComponent} from './device-model-edit.component';
import {Page, Result} from '@core/interceptor/result';
import {DEVICE_RIGHT} from '@core/right/right';
import {UserService} from '@core/user/user.service';


@Component({
  selector: 'app-device-model-list',
  templateUrl: 'device-model-list.component.html',
  styleUrls: ['device-model-list.component.less']
})
export class DeviceModelListComponent implements OnInit {

  aclRight = DEVICE_RIGHT;

  validateForm: FormGroup;
  deviceModelList: any;
  page = new Page();
  loading = true;
  brandList: any;
  serviceCorpList: any[] = [];
  largeClassList: any[] = [];
  smallClassList: any[] = [];
  drawerVisible: boolean;
  useFormValue = true;

  demanderCorpList: any[] = [];

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private userService : UserService,
              private msgService : NzMessageService,
              private modalService: NzModalService) {
    this.validateForm = this.formBuilder.group({
      name: [],
      brandId: [],
      largeClassId: [],
      smallClassId: [],
      enabled: ['Y'],
      corp: []
    });
  }

  ngOnInit(): void {
    this.listDemanderCorp();
  }

  getBrand() {
    const corp = this.validateForm.value.corp || 0;
    this.httpClient.post('/api/device/device-brand/list',{corp})
      .subscribe((res: any) => {
        this.brandList = res.data;
      })
  }

  getDeviceClass() {
    const corp = this.validateForm.value.corp || 0;
    this.httpClient.post('/api/device/device-class/list',{corpId : corp})
      .subscribe((res: any) => {
        this.largeClassList = res.data;
      })
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
        this.getBrand();
        this.getDeviceClass();
        // 默认委托商后再查询
        this.queryDeviceModel();
      }
    });
  }

  /**
   * 委托商变化重新获取编号值
   */
  demanderCorpChange(e){
    this.validateForm.controls.brandId.reset();
    this.validateForm.controls.largeClassId.reset();
    this.getBrand();
    this.getDeviceClass();
  }


  largeClassChange(event) {
    if(event === '' || event === null){
      this.smallClassList = [];
    }
    this.validateForm.controls.smallClassId.reset();
    if(this.largeClassList && this.largeClassList.length > 0){
      this.largeClassList.forEach(largeClass => {
        if(event === largeClass.id){
          this.smallClassList = largeClass.deviceSmallClassDtoList;
        }
      })
    }
  }

  queryDeviceModel(reset: boolean = false) {
    if(reset) {
      this.page.pageNum = 1;
    }
    let params: any = {};
    if (this.validateForm) {
      params = Object.assign(this.validateForm.value);
    }
    params.corp = this.validateForm.value.corp;
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    this.loadDeviceModelList(params);
  }

  // 初始化数据
  loadDeviceModelList(params: string): void {
    this.loading = true;
    this.httpClient
      .post('/api/device/device-model/query',
        params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.deviceModelList = res.data.list;
        this.page.total = res.data.total;
      });
  }


  // 进入添加型号页面
  addDeviceModel(): void {
    const modal = this.modalService.create({
      nzTitle: '添加型号',
      nzContent: DeviceModelAddComponent,
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        this.useFormValue = false;
        this.queryDeviceModel(true);
      }
    });
  }

  // 进入编辑型号页面
  modDeviceModel(data: any): void {
    const modal = this.modalService.create({
      nzTitle: '编辑型号',
      nzContent: DeviceModelEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        deviceModel: data
      }
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        this.queryDeviceModel();
      }
    });
  }

  // 删除确认
  showDeleteConfirm(deviceModelId): void {
    this.modalService.confirm({
      nzTitle: '确定删除该型号吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteDeviceModel(deviceModelId),
      nzCancelText: '取消'
    });
  }

  // 删除型号
  deleteDeviceModel(deviceModelId){
    this.httpClient
      .delete('/api/device/device-model/'+deviceModelId)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        // 如果是为第n页第一条，需要调整为n-1页才有数据显示
        if (this.deviceModelList.length === 1 && this.page.pageNum > 1) {
          this.page.pageNum = this.page.pageNum - 1;
        }
        this.queryDeviceModel();
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
