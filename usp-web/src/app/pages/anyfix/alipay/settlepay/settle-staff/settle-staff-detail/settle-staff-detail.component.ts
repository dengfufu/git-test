import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'app-settle-staff-detail',
  templateUrl: './settle-staff-detail.component.html',
  styleUrls: ['./settle-staff-detail.component.less']
})
export class SettleStaffDetailComponent implements OnInit {

  @Input()
  settleStaff: any;
  settleStaffDetailList: any[];
  pageNum = 1;
  pageSize = 10;
  total = 0;
  loading = false;

  constructor(private httpClient: HttpClient) { }

  ngOnInit() {
    this.querySettleStaffDetail();
  }

  querySettleStaffDetail() {
    this.loading = true;
    this.httpClient.post('/api/anyfix/settle-staff-detail/query', this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      )
      .subscribe((res: any) => {
        this.settleStaffDetailList = res.data.list;
        this.total = res.data.total;
      })
  }

  getParams() {
    const params = {
      pageNum: this.pageNum,
      pageSize: this.pageSize,
      settleId: this.settleStaff.settleId
    }
    return params;
  }

}
