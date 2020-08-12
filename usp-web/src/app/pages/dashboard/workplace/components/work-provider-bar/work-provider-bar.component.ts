import {Component, OnDestroy, OnInit} from '@angular/core';
import {WorkplaceService, DefaultRangeDate, RangeDates} from '../../workplace.service';
import {Subscription} from 'rxjs';
import {NavigationEnd, Router} from '@angular/router';
import {filter} from 'rxjs/operators';

/**
 * 工单按服务商统计
 */
@Component({
  selector: 'app-work-provider-bar',
  templateUrl: 'work-provider-bar.component.html'
})
export class WorkProviderBarComponent implements OnInit, OnDestroy {

  isLoading = false;
  isEmptyData = true;
  chartOption: any;
  rangeDate = DefaultRangeDate;
  rangeDates: any = RangeDates;
  reloadSubscription: Subscription;
  workTotalCount = 0;

  constructor(public workplaceService: WorkplaceService,
              public router: Router,) {
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
    this.workplaceService.initWorkProviderData(this.rangeDate).subscribe((result: any) => {
      this.isLoading = false;
      if (result) {
        this.isEmptyData = false;
        this.workTotalCount = result.workTotalCount;
        this.renderChart(result.dataset, result.typeCount);
      } else {
        this.isEmptyData = true;
      }
    });
  }

  renderChart(dataset: any[], typeCount: number) {
    const seriesData = [];
    for (let i = 0; i < typeCount; i++) {
      const data: any = {
        type: 'bar',
        barWidth: 40
      };
      if (dataset.length > 3) {
        data.stack = '总量';
      }
      seriesData.push(data);
    }
    this.chartOption = {
      dataset: {
        source: dataset
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {  // 坐标轴指示器，坐标轴触发有效
          type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
        }
      },
      legend: {
        show: true
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'value'
      },
      yAxis: {
        type: 'category',
      },
      series: seriesData
    };
  }

  ngOnDestroy() {
    this.reloadSubscription.unsubscribe();
  }
}
