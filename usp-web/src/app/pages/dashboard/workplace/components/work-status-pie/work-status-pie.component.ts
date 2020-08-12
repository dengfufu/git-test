import {Component, OnDestroy, OnInit} from '@angular/core';
import {WorkplaceService, DefaultRangeDate, RangeDates} from '../../workplace.service';
import {Subscription} from 'rxjs';
import {NavigationEnd, Router} from '@angular/router';
import {filter} from 'rxjs/operators';

/**
 * 工单按状态统计
 */
@Component({
  selector: 'app-work-status-pie',
  templateUrl: 'work-status-pie.component.html'
})
export class WorkStatusPieComponent implements OnInit, OnDestroy {

  isLoading = false;
  isEmptyData = true;
  chartOption: any;
  rangeDate = DefaultRangeDate;
  rangeDates: any = RangeDates;
  reloadSubscription: Subscription;
  workTotalCount = 0; // 所有状态的总数

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
    this.workplaceService.initWorkStatusData(this.rangeDate).subscribe((res: any) => {
      this.isLoading = false;
      if (res) {
        this.isEmptyData = false;
        let tmpCount = 0;
        res.forEach((data: number) => {
          tmpCount = tmpCount + data[1];
        });
        this.workTotalCount = tmpCount;
        this.renderChart(res);
      } else {
        this.isEmptyData = true;
      }
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
          name: '状态',
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
