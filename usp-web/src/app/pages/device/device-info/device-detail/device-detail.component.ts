import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {DeviceInfoEditComponent} from '../device-info-edit.component';
import {DeleteConfirmComponent} from '@shared/components/delete-confirm/delete-confirm.component';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {DeviceLocateEditComponent} from '../locate/device-locate-eidt.component';
import {DeviceServiceEditComponent} from '../service/device-service-edit.component';
import {finalize} from 'rxjs/operators';
import {Page} from '@core/interceptor/result';
import {DEVICE_RIGHT} from '@core/right/right';
import {UserService} from '@core/user/user.service';
import {AnyfixService} from '@core/service/anyfix.service';

@Component({
  selector: 'app-device-info-detail',
  templateUrl: 'device-detail.component.html',
  styleUrls: ['device-detail.component.less']
})
export class DeviceDetailComponent implements OnInit {

  aclRight = DEVICE_RIGHT;

  deviceId: any;
  deviceInfo: any = {};
  loading = false;

  selectedIndex = 0;

  workList: Array<any> = [];
  // 自定义字段数据
  customFieldDataList: any = [];
  page = new Page();
  locateSpinning = false;
  serviceSpinning = false;
  curCorpId = this.userService.currentCorp.corpId;

  constructor(private router: Router,
              private activateRoute: ActivatedRoute,
              private httpClient: HttpClient,
              private modalService: NzModalService,
              private cdf: ChangeDetectorRef,
              private messageService: NzMessageService,
              public anyfixService: AnyfixService,
              private userService: UserService) {
    this.deviceId = this.activateRoute.snapshot.queryParams.deviceId;
  }

  ngOnInit() {
    this.loadData();
    this.queryWorkList();
  }

  loadData() {
    this.loading = true;
    this.httpClient.get('/api/device/device-info/' + this.deviceId)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.deviceInfo = res.data;
        this.customFieldDataList = res.data.customFieldDataList;
      });
  }

  // 工单列表
  queryWorkList(reset?: boolean) {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient.post('/api/anyfix/work-request/query', this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.workList = res.data.list;
        this.page.total = res.data.total;
      });
  }

  getParams() {
    const params: any = {
      pageNum: this.page.pageNum,
      pageSize: this.page.pageSize,
      deviceId: this.deviceId
    };
    return params;
  }

  editDeviceInfo() {
    const modal = this.modalService.create({
      nzTitle: '编辑保修档案',
      nzContent: DeviceInfoEditComponent,
      nzFooter: null,
      nzWidth: '1000px',
      nzStyle:{'margin-top': '-40px'},
      nzComponentParams: {
        deviceInfo: this.deviceInfo
      }
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.loadData();
      }
    });
  }

  editDeviceLocate() {
    const modal = this.modalService.create({
      nzTitle: '编辑位置信息',
      nzContent: DeviceLocateEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        deviceInfo: this.deviceInfo
      }
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.loadData();
      }
    });
  }

  editDeviceService() {
    const modal = this.modalService.create({
      nzTitle: '编辑服务信息',
      nzContent: DeviceServiceEditComponent,
      nzFooter: null,
      nzWidth: 600,
      nzStyle:{'margin-top': '-40px'},
      nzComponentParams: {
        deviceInfo: this.deviceInfo
      }
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.loadData();
      }
    });
  }

  // 删除确认
  showDeleteConfirm(deviceId): void {
    const modal = this.modalService.create({
      nzComponentParams:{title1:'确定删除该设备吗?',title2:'删除后不可恢复'},
      nzContent: DeleteConfirmComponent,
      nzFooter: null,
      nzWidth: 472,
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.deleteDeviceInfo(deviceId);
      }
    });
  }

  // 删除设备
  private deleteDeviceInfo(deviceId) {
    this.httpClient
      .delete('/api/device/device-info/' + deviceId)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        this.messageService.success('删除成功！');
        this.goBack();
      });
  }

  deleteLocate() {
    const modal = this.modalService.create({
      nzComponentParams:{title1:'确定清除位置信息吗?'},
      nzContent: DeleteConfirmComponent,
      nzFooter: null,
      nzWidth: 472,
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.deleteLocateSubmit();
      }
    });
  }

  deleteLocateSubmit() {
    this.locateSpinning = true;
    this.httpClient.delete('/api/device/device-locate/' + this.deviceInfo.deviceId)
      .pipe(
        finalize(() => {
          this.locateSpinning = false;
          this.cdf.markForCheck();
        })
      ).subscribe((res: any) => {
      this.messageService.success('清除位置信息成功！');
      this.loadData();
    });
  }

  deleteService() {
    const modal = this.modalService.create({
      nzComponentParams:{title1:'确定清除服务信息吗?'},
      nzContent: DeleteConfirmComponent,
      nzFooter: null,
      nzWidth: 472,
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.deleteServiceSubmit();
      }
    });
  }

  deleteServiceSubmit() {
    this.serviceSpinning = true;
    this.httpClient.delete('/api/device/device-service/' + this.deviceInfo.deviceId)
      .pipe(
        finalize(() => {
          this.serviceSpinning = false;
          this.cdf.markForCheck();
        })
      ).subscribe((res: any) => {
      this.messageService.success('删除服务信息成功！');
      this.loadData();
    });
  }

  viewWorkDetail(workId) {
    const workIds = this.workList.map((item) => {
      return item.workId;
    });
    this.anyfixService.setParamsOnce(workIds);
    this.router.navigate(['/anyfix/work-detail'], {queryParams: {workId}});
  }

  goBack() {
    history.back();
  }


}
