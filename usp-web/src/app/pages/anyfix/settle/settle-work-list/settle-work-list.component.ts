import {Component, Input, OnInit} from '@angular/core';
import {Page} from '@core/interceptor/result';
import {NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'app-settle-work-list',
  templateUrl: './settle-work-list.component.html',
  styleUrls: ['./settle-work-list.component.less']
})
export class SettleWorkListComponent implements OnInit {

  // 查询条件
  @Input() workFilter: any;
  // 列表数据
  workFeeList: any[] = [];
  // 列表加载中
  loading = false;
  // 列表分页
  page = new Page();

  constructor(
    private modalRef: NzModalRef,
    private httpClient: HttpClient
  ) { }

  ngOnInit() {
    this.queryWork(false);
  }

  // 查询工单
  queryWork(reset: boolean) {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    Object.assign(this.workFilter, {pageNum: this.page.pageNum, pageSize: this.page.pageSize})
    this.httpClient.post('/api/anyfix/work-fee/query', this.workFilter)
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      ).subscribe((res: any) => {
        this.workFeeList = res.data.list || [];
        this.page.total = res.data.total;
    })
  }

  // 关闭页面
  cancel() {
    this.modalRef.destroy('cancel');
  }

}
