import {Component, OnDestroy, OnInit} from '@angular/core';
import {WorkplaceService, DefaultRangeDate, RangeDates} from '../../workplace.service';
import {Subscription} from 'rxjs';
import {NavigationEnd, Router} from '@angular/router';
import {filter} from 'rxjs/operators';

/**
 * 设备按类型统计
 */
@Component({
  selector: 'app-device-type-pie',
  templateUrl: 'device-type-pie.component.html'
})
export class DeviceTypePieComponent implements OnInit, OnDestroy {

  isLoading = false;
  isEmptyData = true;
  chartOption: any;
  rangeDate = DefaultRangeDate;
  rangeDates: any = RangeDates;
  reloadSubscription: Subscription;

  constructor(public workplaceService: WorkplaceService,
              public router: Router,) {
    this.reloadSubscription = this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(_ => {
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
    this.workplaceService.initDeviceTypeData(this.rangeDate).subscribe((res: any) => {
      this.isLoading = false;
      if (res) {
        this.isEmptyData = false;
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
          name: '设备类型',
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
          },
        }
      ]
    };
  }

  ngOnDestroy() {
    this.reloadSubscription.unsubscribe();
  }
}
