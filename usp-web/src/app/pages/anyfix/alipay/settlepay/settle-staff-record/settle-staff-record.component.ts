import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {SettleStaffRecordAddComponent} from './settle-staff-record-add/settle-staff-record-add.component';
import {SettleStaffRecordDetailComponent} from './settle-staff-record-detail/settle-staff-record-detail.component';
import {ActivatedRoute, Router} from '@angular/router';
import {finalize} from 'rxjs/operators';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'app-settle-staff-record',
  templateUrl: './settle-staff-record.component.html',
  styleUrls: ['./settle-staff-record.component.less']
})
export class SettleStaffRecordComponent implements OnInit {

  settleStaffRecordList: any[];
  pageSize = 10;
  pageNum = 1;
  total = 0;
  searchForm: FormGroup;
  loading = false;
  visible = false;

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private nzModalService: NzModalService,
              private router: Router,
              private activateRoute: ActivatedRoute,
              private userService: UserService,
              private cdf: ChangeDetectorRef,
              private messageService: NzMessageService) {}

  ngOnInit() {
    this.querySettleStaffRecord(this.getPageParam(), false);
    this.searchForm = this.formBuilder.group({});
    this.searchForm.addControl('recordName', new FormControl());
  }

  querySettleStaffRecord(params: any, reset: boolean): void {
    if (reset) {
      this.pageNum = 1;
    }
    this.loading = true;
    this.httpClient.post('/api/anyfix/settle-staff-record/query', params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.settleStaffRecordList = res.data.list;
        this.total = res.data.total;
        console.log(this.total);
      })
  }

  getParams() {
    const params = this.searchForm.value;
    params.serviceCorp = this.userService.currentCorp.corpId;
    params.pageNum = this.pageNum;
    params.pageSize = this.pageSize;
    return params;
  }

  getPageParam() {
    const params: any = {};
    params.serviceCorp = this.userService.currentCorp.corpId;
    params.pageNum = this.pageNum;
    params.pageSize = this.pageSize;
    return params;
  }

  addSettleStaffRecord() {
    const modal = this.nzModalService.create({
      nzTitle: '添加员工结算记录',
      nzWidth: 1000,
      nzContent: SettleStaffRecordAddComponent,
      nzFooter: null
    })
    modal.afterClose.subscribe((result: any) => {
      if(result === 'submit') {
        this.querySettleStaffRecord(this.getParams(), true);
      }
    });
  }

  showDetail(settleStaffRecord) {
    const modal = this.nzModalService.create({
      nzTitle: '员工结算记录明细',
      nzWidth: 1000,
      nzContent: SettleStaffRecordDetailComponent,
      nzComponentParams: {settleStaffRecord},
      nzFooter: null
    })
  }

  delete(recordId) {
    this.loading = true;
    this.httpClient.delete(`/api/anyfix/settle-staff-record/${recordId}`)
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      ).subscribe((res: any) => {
      this.messageService.success('删除成功');
      this.querySettleStaffRecord(this.getParams(), false);
    })
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

}
