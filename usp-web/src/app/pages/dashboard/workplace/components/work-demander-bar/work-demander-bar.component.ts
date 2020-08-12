import {Component, OnDestroy, OnInit} from '@angular/core';
import {WorkplaceService, DefaultRangeDate, RangeDates} from '../../workplace.service';
import {Subscription} from 'rxjs';
import {NavigationEnd, Router} from '@angular/router';
import {filter} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {Result} from '@core/interceptor/result';

/**
 * 工单按委托商统计
 */
@Component({
  selector: 'app-work-demander-bar',
  templateUrl: 'work-demander-bar.component.html'
})
export class WorkDemanderBarComponent implements OnInit, OnDestroy {

  isLoading = false;
  isEmptyData = true;
  chartOption: any;
  rangeDate = DefaultRangeDate;
  rangeDates: any = RangeDates;
  reloadSubscription: Subscription;
  showChart = true;

  dataset4table: any[];
  corpTotalCount = 0;
  workTotalCount = 0;

  workTypeOption: any[] = []; // 工单类型选项
  workTypeSelectedValue = [1, 2, 3]; // 选中的工单类型: 维护,巡检,安装

  constructor(public workplaceService: WorkplaceService,
              public router: Router,
              public httpClient: HttpClient) {
    this.reloadSubscription = this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(_ => {
      this.dataset4table = [];
      this.corpTotalCount = 0;
      this.workTotalCount = 0;
      this.loadData();
    });
  }

  ngOnInit() {
    this.getWordTypeList(); // 获取工单类型选项
    this.loadData();
  }

  dateRangeChange(event) {
    this.loadData();
  }

  loadData() {
    this.isLoading = true;
    this.workplaceService.initWorkDemanderData(this.rangeDate, this.workTypeSelectedValue).subscribe((result: any) => {
      this.isLoading = false;
      if (result) {
        this.isEmptyData = false;
        this.dataset4table = result.dataset4table;
        this.corpTotalCount = result.corpTotalCount;
        this.workTotalCount = result.workTotalCount;
        this.renderChart(result.dataset, result.typeCount);
      } else {
        this.isEmptyData = true;
      }
    });
  }

  /*获取表格每行总数*/
  getCorpWorkCount(data: any[]) {
    let total = 0;
    data.forEach((value, index) => {
      if (index > 0) {
        total = total + value;
      }
    });
    return total;
  }

  renderChart(dataset: any[], typeCount: number) {
    const seriesData = [];
    for (let i = 0; i < typeCount; i++) {
      const data: any = {
        type: 'bar',
        barMaxWidth: 40,
      };
      if (i == typeCount - 1) {//在每行柱形图的最后一个数据块显示总数数值
        data.label = {
          normal: {
            fontSize: 10,
            show: true,
            position: 'right',
            textStyle: { //数值样式
              color: 'black',
            },
            formatter: params => {
              let total = 0;
              let data = params.data;
              data.forEach((value, index) => {
                if (index > 0) {
                  total = total + value;
                }
              });
              return total;
            }
          }
        };
      }
      if (dataset.length > 3) {
        data.stack = '委托商类型总量';
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
        type: 'value',
      },
      yAxis: {
        type: 'category',
        axisLabel: {
          interval: 0,
          fontSize: 10,
          formatter: (value, index) => {
            const count = dataset.length;
            let num = count - index - 1;
            return `${num}.${value}`;
          }
        },
      },
      series: seriesData
    };
  }

  // 获取工单类型选项
  getWordTypeList() {
    const api = '/api/anyfix/work-type/list';
    this.httpClient.post(api, {}).subscribe((result: Result) => {
      if (result.code === 0) {
        const workTypeOption = result.data.map(i => {
          return {
            code: i.id,
            name: i.name
          };
        });
        this.workTypeOption = workTypeOption;
      }
    });
  }

  ngOnDestroy() {
    this.reloadSubscription.unsubscribe();
  }
}
