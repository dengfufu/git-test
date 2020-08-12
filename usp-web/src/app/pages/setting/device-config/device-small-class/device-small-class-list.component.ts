import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {DeviceSmallClassAddComponent} from './device-small-class-add.component';
import {DeviceSmallClassEditComponent} from './device-small-class-edit.component';
import {Page, Result} from '@core/interceptor/result';
import {DEVICE_RIGHT} from '@core/right/right';
import {DeviceSmallClassDetailComponent} from './device-small-class-detail.component';
import {UserService} from '@core/user/user.service';


@Component({
  selector: 'app-device-small-class-list',
  templateUrl: 'device-small-class-list.component.html',
  styleUrls: ['device-small-class-list.component.less']
})
export class DeviceSmallClassListComponent implements OnInit {

  aclRight = DEVICE_RIGHT;

  validateForm: FormGroup;
  smallClassList: any;
  page = new Page();
  loading = true;
  largeClassList: any;
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
      largeClassId: [],
      enabled: ['Y'],
      corp: []
    });
  }

  ngOnInit(): void {
    this.listDemanderCorp();
  }

  getLargeClass() {
    const corp = this.validateForm.value.corp || 0;
    this.httpClient.post('/api/device/device-large-class/list', {corp})
      .subscribe((res: any) => {
        this.largeClassList = res.data;
      });
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
        this.getLargeClass();
        // 默认委托商后再查询
        this.querySmallClass();
      }
    });
  }
  /**
   * 委托商变化重新获取大类
   */
  demanderCorpChange(e){
    this.validateForm.controls.largeClassId.reset();
    this.getLargeClass();
  }

  // 初始化数据
  loadSmallClassList(params: string, reset?: boolean): void {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient
      .post('/api/device/device-small-class/query',
        params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.smallClassList = res.data.list;
        this.page.total = res.data.total;
      });
  }

  querySmallClass(reset: boolean = false) {
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
    this.loadSmallClassList(params);
  }



  // 进入添加配置页面
  addSmallClass(): void {
    const modal = this.modalService.create({
      nzTitle: '添加设备类型',
      nzContent: DeviceSmallClassAddComponent,
      nzFooter: null,
      nzWidth: 800,
      nzMaskClosable: false
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        this.useFormValue = false;
        this.querySmallClass(true);
      }
    });
  }

  // 进入编辑配置页面
  editSmallClass(id): void {
    const modal = this.modalService.create({
      nzTitle: '编辑设备类型',
      nzContent: DeviceSmallClassEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzMaskClosable: false,
      nzComponentParams: {
        smallClassId: id
      }
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        this.querySmallClass();
      }
    });
  }

  // 删除确认
  showDeleteConfirm(smallClassId): void {
    this.modalService.confirm({
      nzTitle: '确定删除该配置吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteSmallClass(smallClassId),
      nzCancelText: '取消'
    });
  }

  // 删除配置
  deleteSmallClass(smallClassId) {
    this.httpClient
      .delete('/api/device/device-small-class/' + smallClassId)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        // 如果是为第n页第一条，需要调整为n-1页才有数据显示
        if (this.smallClassList.length === 1 && this.page.pageNum > 1) {
          this.page.pageNum = this.page.pageNum - 1;
        }
        this.querySmallClass();
      });
  }

  /**
   * 查看设备类型
   * @param id 设备类型id
   */
  viewSmallClass(id){
    this.modalService.create({
      nzTitle: '查看设备类型',
      nzContent: DeviceSmallClassDetailComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        smallClassId: id
      }
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
