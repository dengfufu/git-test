import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {finalize} from 'rxjs/operators';
import {SettleStaffDetailComponent} from './settle-staff-detail/settle-staff-detail.component';

@Component({
  selector: 'app-staff-settle',
  templateUrl: './settle-staff.component.html',
  styleUrls: ['./settle-staff.component.less']
})
export class SettleStaffComponent implements OnInit {

  settleStaffList: any[];
  pageSize = 10;
  pageNum = 1;
  total = 0;
  searchForm: FormGroup;
  serviceCorp = this.userService.currentCorp.corpId;
  loading = false;
  visible = false;

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private nzModalService: NzModalService,
              private userService: UserService,
              private cdf: ChangeDetectorRef,
              private messageService: NzMessageService) {
  }

  ngOnInit() {
    this.querySettleStaff(this.getPageParam(), true);
    this.searchForm = this.formBuilder.group({});
    this.searchForm.addControl('userName', new FormControl());
  }

  querySettleStaff(params, reset: boolean) {
    if (reset) {
      this.pageNum = 1;
    }
    this.loading = true;
    this.httpClient.post('/api/anyfix/settle-staff/query', params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.settleStaffList = res.data.list;
        this.total = res.data.total;
      });
  }

  getParams() {
    const params = this.searchForm.value;
    params.serviceCorp = this.serviceCorp;
    params.pageNum = this.pageNum;
    params.pageSize = this.pageSize;
    return params;
  }

  getPageParam() {
    const params: any = {};
    params.serviceCorp = this.serviceCorp;
    params.pageNum = this.pageNum;
    params.pageSize = this.pageSize;
    return params;
  }

  showDetail(settleStaff) {
    const modal = this.nzModalService.create({
      nzTitle: '网点结算明细',
      nzWidth: 1000,
      nzContent: SettleStaffDetailComponent,
      nzComponentParams: {settleStaff},
      nzFooter: null
    });
  }

  delete(settleId) {
    this.loading = true;
    this.httpClient.delete(`/api/anyfix/settle-staff/${settleId}`)
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      ).subscribe((res: any) => {
      this.messageService.success('删除成功');
      this.querySettleStaff(this.getParams(), false);
    });
  }

  openDrawer() {
    this.visible = true;
  }

  closeDrawer() {
    this.visible = false;
  }

  clearDrawer() {
    this.searchForm.reset();
  }

  addSettleStaffRecord() {

  }

}
