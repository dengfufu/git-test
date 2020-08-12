import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'app-settle-staff-record-detail',
  templateUrl: './settle-staff-record-detail.component.html',
  styleUrls: ['./settle-staff-record-detail.component.less']
})
export class SettleStaffRecordDetailComponent implements OnInit {

  @Input()
  settleStaffRecord: any;
  settleStaffList: any[];
  pageNum = 1;
  pageSize = 10;
  total = 0;
  loading = false;

  constructor(private httpClient: HttpClient) { }

  ngOnInit() {
    this.querySettleStaffRecordDetail();
  }

  querySettleStaffRecordDetail() {
    this.loading = true;
    this.httpClient.post('/api/anyfix/settle-staff/query', this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      )
      .subscribe((res: any) => {
        this.settleStaffList = res.data.list;
        this.total = res.data.total;
      })
  }

  getParams() {
    const params = {
      pageNum: this.pageNum,
      pageSize: this.pageSize,
      recordId: this.settleStaffRecord.recordId
    }
    return params;
  }

}
