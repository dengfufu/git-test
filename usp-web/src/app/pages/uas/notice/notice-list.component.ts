import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Page, Result} from '@core/interceptor/result';
import {HttpClient} from '@angular/common/http';
import {UserService} from '@core/user/user.service';
import {finalize} from 'rxjs/operators';
import {addMonths, format} from 'date-fns';
import {Pipe, PipeTransform} from '@angular/core';
import {DomSanitizer} from '@angular/platform-browser';

@Component({
  selector: 'app-notice-list',
  templateUrl: 'notice-list.component.html'
})
export class NoticeListComponent implements OnInit {

  noticeList: any[] = [];
  loading: boolean;
  page = new Page();

  currentYearMonth: string;
  yearMonthList: any[];

  constructor(private httpClient: HttpClient,
              private cdf: ChangeDetectorRef,
              private userService: UserService) {
  }

  ngOnInit() {
    const currentDate = new Date(); // 当月
    const lastDate = addMonths(currentDate, -1); // 上一个月
    const prevDate = addMonths(currentDate, -2); // 前一个月
    this.yearMonthList = [
      {
        name: '当前月',
        value: format(currentDate, 'YYYYMM').substring(2),
      },
      {
        name: '上个月',
        value: format(lastDate, 'YYYYMM').substring(2),
      },
      {
        name: '上上一个月',
        value: format(prevDate, 'YYYYMM').substring(2),
      }
    ];
    this.currentYearMonth = this.yearMonthList[0].value;

    this.loadData();
  }

  loadData(reset: boolean = false) {
    if (reset) {
      this.page.pageNum = 1;
    }
    const param = {
      yearMonth: this.currentYearMonth,
      touser: this.userService.userInfo.userId,
      pageNum: this.page.pageNum,
      pageSize: this.page.pageSize
    };
    this.loading = true;
    this.httpClient.post('/api/msg/notice/query', param)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        this.noticeList = result.data.list;
        if (this.noticeList) {
          this.noticeList.forEach(data => {
            data.content = data.content.replace(/line-height: 30px;/g, '')
              .replace(/margin-top: 10px;/g, '')
              .replace(/font-size:15px;/g, '')
              .replace(/font-size:14px;/g, '')
              .replace(/font-weight:500;/g, '');
          });
          this.page.total = result.data.total;
        }
      });
  }

  changeMonth(yearMonth: string) {
    this.currentYearMonth = yearMonth;
    this.loadData(true);
  }
}
