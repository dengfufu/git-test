import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'app-settle-branch-detail',
  templateUrl: './settle-branch-detail.component.html',
  styleUrls: ['./settle-branch-detail.component.less']
})
export class SettleBranchDetailComponent implements OnInit {

  @Input()
  settleBranch: any;
  settleBranchDetailList: any[];
  pageNum = 1;
  pageSize = 10;
  total = 0;
  loading = false;

  constructor(private httpClient: HttpClient) { }

  ngOnInit() {
    this.querySettleBranchDetail();
  }

  querySettleBranchDetail() {
    this.loading = true;
    this.httpClient.post('/api/anyfix/settle-branch-detail/query', this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      )
      .subscribe((res: any) => {
        this.settleBranchDetailList = res.data.list;
        this.total = res.data.total;
      })
  }

  getParams() {
    const params = {
      pageNum: this.pageNum,
      pageSize: this.pageSize,
      settleId: this.settleBranch.settleId
    }
    return params;
  }

}
