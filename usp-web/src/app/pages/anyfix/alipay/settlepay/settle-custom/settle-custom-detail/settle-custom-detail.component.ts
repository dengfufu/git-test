import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'app-settle-custom-detail',
  templateUrl: './settle-custom-detail.component.html',
  styleUrls: ['./settle-custom-detail.component.less']
})
export class SettleCustomDetailComponent implements OnInit {

  @Input()
  settleCustom: any;
  settleCustomDetailList: any[];
  pageNum = 1;
  pageSize = 10;
  total = 0;
  loading = false;

  constructor(private httpClient: HttpClient) { }

  ngOnInit() {
    this.querySettleCustomDetail();
  }

  querySettleCustomDetail() {
    this.loading = true;
    this.httpClient.post('/api/anyfix/settle-custom-detail/query', this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      )
      .subscribe((res: any) => {
        this.settleCustomDetailList = res.data.list;
        this.total = res.data.total;
      })
  }

  getParams() {
    const params = {
      pageNum: this.pageNum,
      pageSize: this.pageSize,
      settleId: this.settleCustom.settleId
    }
    return params;
  }

}
