import {Component, OnDestroy, OnInit} from '@angular/core';
import {WorkplaceService, DefaultRangeDate, RangeDates} from '../../workplace.service';
import {filter} from 'rxjs/operators';
import {NavigationEnd, Router} from '@angular/router';
import {Subscription} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Result} from '@core/interceptor/result';

/**
 * 工单按类型统计
 *
 */
@Component({
  selector: 'app-work-type-pie',
  templateUrl: 'work-type-pie.component.html'
})
export class WorkTypePieComponent implements OnInit, OnDestroy {

  isLoading = false;
  isEmptyData = true;
  chartOption: any;
  rangeDate = DefaultRangeDate;
  rangeDates: any = RangeDates;
  reloadSubscription: Subscription;
  workTotalCount = 0; // 维护类型对应具体值的总数

  constructor(public workplaceService: WorkplaceService,
              public router: Router,
              public httpClient: HttpClient) {
    this.reloadSubscription = this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(_ => {
      this.workTotalCount = 0;
      this.loadData();
    });
  }

  ngOnInit() {
    this.loadData();
  }

  dateRangeChange(event) {
    this.loadData();
  }

  loadData() {
    this.isLoading = true;
    const api = '/api/anyfix/work-type/list';
    this.httpClient.post(api, {}).subscribe((result: Result) => {
      const typeList = [];// 工单类型列表
      // tslint:disable-next-line:prefer-for-of
      for (let i = 0; i < result.data.length; i++) {
        typeList.push(result.data[i].name);
      }

      this.workplaceService.initWorkTypeData(this.rangeDate).subscribe((res: any) => {
        this.isLoading = false;
        if (res) {
          // 获取到的工单按类型统计数据 根据工单类型列表顺序排序
          const newRes = [];
          // tslint:disable-next-line:prefer-for-of
          for (let i = 0; i < typeList.length; i++) {
            // tslint:disable-next-line:prefer-for-of
            for (let j = 0; j < res.length; j++) {
              if (typeList[i] === res[j][0]) {
                newRes.push(res[j]);
              }
            }
          }

          this.isEmptyData = false;
          let tmpCount = 0;
          newRes.forEach((data: number) => {
            tmpCount = tmpCount + data[1];
          });
          this.workTotalCount = tmpCount;
          this.renderChart(newRes);
        } else {
          this.isEmptyData = true;
        }
      });
    });
  }

  renderChart(resultSet: any[]) {
    this.chartOption = {
      dataset: {
        source: resultSet
      },
      tooltip: {
        trigger: 'item',
        formatter: (params) => {
          return `${params.seriesName} <br/>${params.value[0]}: ${params.value[1]} (${params.percent}%)'`;
        }
      },
      legend: {
        orient: 'vertical',
        left: 10
      },
      series: [
        {
          name: '类型',
          type: 'pie',
          radius: ['50%', '70%'],
          avoidLabelOverlap: false,
          label: {
            show: false,
            position: 'center'
          },
          emphasis: {
            label: {
              show: true,
              fontSize: '30',
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          }
        }
      ]
    };
  }

  ngOnDestroy() {
    this.reloadSubscription.unsubscribe();
  }
}
