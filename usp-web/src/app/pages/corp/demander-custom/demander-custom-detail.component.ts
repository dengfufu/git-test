import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {finalize} from 'rxjs/operators';
import {Page} from '@core/interceptor/result';
import {NzModalService} from 'ng-zorro-antd';
import {ANYFIX_RIGHT} from '@core/right/right';
import {DemanderCustomEditComponent} from './demander-custom-edit.component';

@Component({
  selector: 'app-device-branch-detail',
  templateUrl: 'demander-custom-detail.component.html',
  styleUrls: ['demander-custom.component.less']
})
export class DemanderCustomDetailComponent implements OnInit {

  aclRight = ANYFIX_RIGHT;
  detail: any = {};
  deviceList: any = [];
  workList: any = [];
  loading = false;
  devicePage = new Page();
  page = new Page();
  workPage = new Page();
  customCorp: any;
  customId = '';
  demanderCorp = '';
  workLoading = false;
  deviceLoading = false;
  spinning = false;
  address = '';

  constructor(private httpClient: HttpClient,
              private modalService: NzModalService,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private cdf: ChangeDetectorRef) {
    this.customId = this.activatedRoute.snapshot.queryParams.customId;
    this.queryDetail();
    this.queryDeviceList();
    this.queryWorkList();
  }

  ngOnInit() {
  }

  /**
   * 初始化网点数据
   */
  queryDetail() {
    const params = {
      customId: this.customId
    };
    this.spinning = true;
    this.httpClient
      .post('/api/anyfix/demander-custom/custom/detail', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
          this.spinning = false;
        })
      )
      .subscribe((res: any) => {
        this.detail = res.data;
        this.customCorp = this.detail.customCorp;
        this.demanderCorp = this.detail.demanderCorp;
        this.address = res.data.region;
        if (res.data.address != null) {
          this.address = this.address + res.data.address;
        }
      });
  }

  queryDeviceList() {
    this.deviceLoading = true;
    this.httpClient
      .post('/api/device/device-info/query', this.getDeviceParams())
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
          this.deviceLoading = false;
        })
      )
      .subscribe((res: any) => {
        this.deviceList = res.data.list;
        this.devicePage.total = res.data.total;
      });
  }

  /**
   * 加载工单记录
   */
  queryWorkList() {
    this.workLoading = true;
    this.httpClient
      .post('/api/anyfix/work-request/custom/query', this.getWorkParams())
      .pipe(
        finalize(() => {
          this.workLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        if (res.data == null) {
          return;
        }
        this.workList = res.data.list;
        this.workPage.total = res.data.total;
      });
  }

  getWorkParams() {
    const params: any = {};
    params.pageNum = this.workPage.pageNum;
    params.pageSize = this.workPage.pageSize;
    params.customCorp = this.customCorp;
    params.demanderCorp = this.demanderCorp;
    return params;
  }

  getDeviceParams() {
    const params: any = {};
    params.pageNum = this.devicePage.pageNum;
    params.pageSize = this.devicePage.pageSize;
    params.customId = this.customId;
    return params;
  }

  goBack() {
    history.go(-1);
  }

  editModal() {
    const modal = this.modalService.create({
      nzTitle: '编辑客户',
      nzContent: DemanderCustomEditComponent,
      nzFooter: null,
      nzMaskClosable: false,
      nzWidth: 800,
      nzComponentParams: {
        demanderCustom: this.detail
      }
    });
    modal.afterClose.subscribe(result => {
      console.log('result', result);
      if (result === 'submit') {
        this.queryDetail();
        this.cdf.markForCheck();
      }
    });
  }

  showDeleteConfirm() {
    this.modalService.confirm({
      nzTitle: '确定删除该使用商吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteDemanderCustom(this.customId),
      nzCancelText: '取消'
    });
  }

  // 删除
  deleteDemanderCustom(id) {
    this.spinning = true;
    this.httpClient
      .delete('/api/anyfix/demander-custom/' + id)
      .pipe(
        finalize(() => {
          this.spinning = false;
        })
      )
      .subscribe(() => {
        this.goBack();
      });
  }
}
