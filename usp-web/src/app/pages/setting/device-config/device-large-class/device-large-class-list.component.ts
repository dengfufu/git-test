import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {DeviceLargeClassAddComponent} from './device-large-class-add.component';
import {DeviceLargeClassEditComponent} from './device-large-class-edit.component';
import {Page, Result} from '@core/interceptor/result';
import {DEVICE_RIGHT} from '@core/right/right';
import {UserService} from '@core/user/user.service';



@Component({
  selector: 'app-device-large-class-list',
  templateUrl: 'device-large-class-list.component.html',
  styleUrls: ['device-large-class-list.component.less']
})
export class DeviceLargeClassListComponent implements OnInit {

  aclRight = DEVICE_RIGHT;

  validateForm: FormGroup;
  largeClassList: any;
  page = new Page();
  loading = true;
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
      enabled: ['Y'],
      corp: []
    });
  }

  ngOnInit(): void {
    this.listDemanderCorp();
  }

  // 初始化数据
  loadLargeClassList(params): void {
    this.loading = true;
    this.httpClient
      .post('/api/device/device-large-class/query',
        params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.largeClassList = res.data.list;
        this.page.total = res.data.total;
      });
  }

  queryLargeClass(reset: boolean = false) {
    if (reset) {
      this.page.pageNum = 1;
    }
    let params: any = {};
    if (this.useFormValue) {
      params = Object.assign({},this.validateForm.value);
    }
    params.corp = this.validateForm.value.corp;
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    this.loadLargeClassList(params);
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
        // 默认委托商后再查询
        this.queryLargeClass();
      }
    });
  }

  // 进入添加大类页面
  addLargeClass(): void {
    const modal = this.modalService.create({
      nzTitle: '添加设备大类',
      nzContent: DeviceLargeClassAddComponent,
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        this.useFormValue = false;
        this.queryLargeClass();
      }
    });
  }

  // 进入编辑大类页面
  editLargeClass(data): void {
    const modal = this.modalService.create({
      nzTitle: '编辑设备大类',
      nzContent: DeviceLargeClassEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        deviceLargeClass: data
      }
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        this.queryLargeClass();
      }
    });
  }

  // 删除确认
  showDeleteConfirm(largeClassId): void {
    this.modalService.confirm({
      nzTitle: '确定删除该大类吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteLargeClass(largeClassId),
      nzCancelText: '取消'
    });
  }

  // 删除大类
  deleteLargeClass(largeClassId) {
    this.httpClient
      .delete('/api/device/device-large-class/' + largeClassId)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        // 如果是为第n页第一条，需要调整为n-1页才有数据显示
        if (this.largeClassList.length === 1 && this.page.pageNum > 1) {
          this.page.pageNum = this.page.pageNum - 1;
        }
        this.queryLargeClass();
      });
  }

  openDrawer() {
    this.drawerVisible = true;
  }

  closeDrawer() {
    this.drawerVisible = false;
  }

  clearDrawer() {
    // 不清空委托商查询条件
    this.validateForm.reset();
  }

}
