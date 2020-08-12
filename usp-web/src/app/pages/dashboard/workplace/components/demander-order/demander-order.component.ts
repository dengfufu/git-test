import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {NzMessageService} from 'ng-zorro-antd/message';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable, Subscription} from 'rxjs';
import {filter, map} from 'rxjs/operators';
import {startOfMonth, endOfMonth, format, subMonths, startOfYear, endOfYear} from 'date-fns';
import {environment} from '@env/environment';
import {DA_SERVICE_TOKEN, ITokenService} from '@delon/auth';
import {UserService} from '@core/user/user.service';
import {NavigationEnd, Router} from '@angular/router';

export class CdaResult {
  metadata: any[];
  queryInfo: any[];
  resultset: any[];
}

export const RangeDates = [
  {
    label: '今年',
    rangeDate: [startOfYear(new Date()), endOfYear(new Date())]
  },
  {
    label: '近6个月',
    rangeDate: [startOfMonth(subMonths(new Date(), 5)), endOfMonth(new Date())]
  },
  {
    label: '近12个月',
    rangeDate: [startOfMonth(subMonths(new Date(), 11)), endOfMonth(new Date())]
  }
];

@Component({
  selector: 'app-demander-order',
  templateUrl: './demander-order.component.html',
  styleUrls: ['./demander-order.component.less']
})

export class DemanderOrderComponent implements OnInit, OnDestroy {

  isLoading = false;
  isEmptyData = true;
  showChart = true;// 是否显示eChart图表
  chartOption: any;// eChart图标配置项
  selectDemanderOption: any[] = [];// 委托商下拉选项
  selectDemanderVal: any[] = [];// 委托商下拉选中值
  dataset: any[];// eChart图表数据
  datasettable: any[];// 表格数据
  scrollValue: any = {x: 0};// 表格滚动条
  rangeDates: any[] = RangeDates;// 快捷日期范围
  rangeDate: any[] = [startOfMonth(subMonths(new Date(), 5)), endOfMonth(new Date())];// 日期
  reloadSubscription: Subscription;

  constructor(private message: NzMessageService,
              @Inject(DA_SERVICE_TOKEN) private tokenService: ITokenService,
              private userService: UserService,
              public router: Router,
              private httpClient: HttpClient) {
    this.reloadSubscription = this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(_ => {
      this.loadData();
    });
  }

  ngOnInit() {
    this.scrollValue.x = (window.innerWidth - 198) + 'px';

    this.loadData();
  }

  ngOnDestroy() {
    this.reloadSubscription.unsubscribe();
  }

  // 加载数据
  loadData() {
    this.isLoading = true;
    this.getData(this.rangeDate).subscribe((result: any) => {
      this.isLoading = false;
      if (result !== undefined) {
        this.isEmptyData = false;
        this.setSelectDemanderOption(result.dataset);// 设置委托商下拉列表
        this.dataset = result.dataset;
        this.datasettable = result.datasettable;

        result.dataset = result.dataset.slice(0, 6);// 截取前5个委托商数据用于渲染eChart图表
        this.renderChart(result.dataset);
      } else {
        this.isEmptyData = true;
      }
    });
  }

  // 获取委托商工单量数据
  private getData(rangeDate: Date[]): Observable<any> {
    const payload = new HttpParams()
      .set('path', '/public/cda/usp_workplace.cda')
      .set('dataAccessId', 'demander_trend_chart')
      .set('paramstartMonth', format(rangeDate[0], 'YYYY-MM'))
      .set('paramendMonth', rangeDate[1] ? format(rangeDate[1], 'YYYY-MM') : format(endOfMonth(new Date()), 'YYYY-MM'))
      .set('paramPRM_SCOPE', '0')
      .set('token', this.tokenService.get().token)
      .set('tenantid', this.userService.currentCorp.corpId);
    return this.httpClient.get(environment.dip_url + '/ba/plugin/cda/api/doQuery?_allow_anonymous=true', {params: payload})
      .pipe(
        map((result: CdaResult) => {
          return result.resultset.length === 0 ? undefined : this.convertResultSet(result.resultset);
        })
      );
  }

  // 渲染eChart图表
  renderChart(dataset: any[]) {
    const xData = [];
    for (let i = 1; i < dataset[0].length; i++) {
      xData.push(dataset[0][i]);
    }
    const sData = [];
    for (let i = 1; i < dataset.length; i++) {
      const tmpSData = [];
      for (let j = 1; j < dataset[i].length; j++) {
        tmpSData.push(dataset[i][j]);
      }
      sData.push(tmpSData);
    }
    const xArr = [];
    const yArr = [];
    const sArr = [];
    const gArr = [];
    const per = 80 / (dataset.length - 1);// 根据委托商个数将图表80%高度平均作为每个委托商图表的高度
    for (let i = 1; i < dataset.length; i++) {
      const xObj = {
        gridIndex: i - 1,
        show: false,
        type: 'category',
        boundaryGap: false,
        data: xData
      };
      if (i === dataset.length - 1) {
        xObj.show = true;
      }
      xArr.push(xObj);

      const yObj = {
        gridIndex: i - 1,
        axisLabel: {
          show: false
        },
        axisTick: {
          show: true
        },
        splitLine: {
          show: false
        },
        splitNumber: 1,
        name: dataset[i][0],
        nameLocation: 'center',
        nameRotate: 360
      };
      yArr.push(yObj);

      const sObj = {
        type: 'line',
        data: sData[i - 1],
        xAxisIndex: i - 1,
        yAxisIndex: i - 1,
        // smooth: true
        // areaStyle: {}
        label: {
          normal: {
            show: true,
            position: 'top'
          }
        }
      };
      sArr.push(sObj);

      const gObj = {
        top: `${10 + per * (i - 1)}%`,
        height: `${per}%`
      };
      gArr.push(gObj);
    }
    this.chartOption = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'cross'
        }
      },
      xAxis: xArr,
      yAxis: yArr,
      grid: gArr,
      series: sArr,
    };
  }

  // 快捷月份
  shortcutRangeFun(rangeDate) {
    const [...tmpRangeDate] = rangeDate;
    this.rangeDate = tmpRangeDate;
    this.loadData();
  }

  // 月份选择器
  monthChange(e) {
    this.loadData();
  }

  // 禁用开始月份
  disabledStartMonth = (startValue: Date): boolean => {
    if (!startValue || !this.rangeDate[1]) {
      return false;
    }
    return startValue.getTime() > this.rangeDate[1].getTime() ||
      startValue.getTime() <= this.rangeDate[1].getTime() - 24 * 60 * 60 * 1000 * 30 * 25;
  };

  // 禁用结束月份
  disabledEndMonth = (endValue: Date): boolean => {
    if (!endValue || !this.rangeDate[0]) {
      return false;
    }
    return endValue.getTime() <= this.rangeDate[0].getTime() ||
      endValue.getTime() > this.rangeDate[0].getTime() + 24 * 60 * 60 * 1000 * 30 * 25;
  };

  // 月份范围
  rangeMonth(start: Date, end: Date) {
    end = end ? end : new Date();
    start = start ? start : startOfMonth(subMonths(end, 11));

    const s = [start.getFullYear(), start.getMonth() + 1];
    const e = [end.getFullYear(), end.getMonth() + 1];
    const result = [];
    while (s[0] < e[0] || (s[0] === e[0] && s[1] <= e[1])) {
      result.push(`${s[0]}-${s[1] > 9 ? s[1] : '0' + s[1]}`);
      s[1]++;
      if (s[1] > 12) {
        s[1] = 1;
        s[0]++;
      }
    }
    return result;
  }

  // 设置委托商下拉列表
  setSelectDemanderOption(arr: any[]) {
    this.selectDemanderOption = [];
    for (let i = 1; i < arr.length; i++) {
      this.selectDemanderOption.push(arr[i][0]);
    }

    // 默认选中前5个委托商
    this.selectDemanderVal = [];
    for (let i = 0; i < this.selectDemanderOption.length; i++) {
      if (i < 5) {
        this.selectDemanderVal.push(this.selectDemanderOption[i]);
      }
    }
  }

  // 委托商下拉选中值改变时
  demanderValChange(options: any[]) {
    const [...tmpDataset] = this.dataset;

    // 根据委托商下拉选项对委托商下拉选中值进行排序
    const newOptions = [];
    for (const item of this.selectDemanderOption) {
      for (const item2 of options) {
        if (item === item2) {
          newOptions.push(item2);
        }
      }
    }

    const dataset = [];
    dataset.push(tmpDataset[0]);
    for (const item of newOptions) {
      for (const item2 of tmpDataset) {
        if (item === item2[0]) {
          dataset.push(item2);
        }
      }
    }

    this.renderChart(dataset);
  }

  // 转换数据格式
  private convertResultSet(resultSet: any[]) {
    const corpList = [];
    resultSet.forEach((result: any[]) => {
      const corp: string = result[0];
      const index1 = corpList.indexOf(corp);
      if (index1 < 0) {
        corpList.push(corp);
      }
    });
    const monthList = this.rangeMonth(this.rangeDate[0], this.rangeDate[1]);

    // 第一行
    const firstRow = ['委托商'];
    monthList.forEach((month: string) => {
      firstRow.push(month);
    });

    // 数据行
    const detailRow = [];
    corpList.forEach((corp) => {
      const row = [];
      row.push(corp);
      monthList.forEach((month) => {
        const tmp = resultSet.find((result) => {
          const rCorp = result[0];
          const rMonth = result[1];
          return rCorp === corp && rMonth === month;
        });
        if (tmp) {
          row.push(tmp[2]);
        } else {
          row.push(0);
        }
      });
      detailRow.push(row);
    });

    // 数据行排序_降序
    detailRow.sort((temA: any[], temB: any[]) => {
      let tmpACount = 0;
      let tmpBCount = 0;
      for (let i = 1; i <= firstRow.length - 1; i++) {
        tmpACount = tmpACount + temA[i];
        tmpBCount = tmpBCount + temB[i];
      }
      return tmpBCount - tmpACount;
    });

    // 计算每个月份所有委托商的工单量总和_用于表格最后一行合计
    const sumOrderArr = [];
    sumOrderArr.push('合计');
    for (let i = 1; i < detailRow[0].length; i++) {
      let sum = 0;
      for (const item of detailRow) {
        sum += item[i];
      }
      sumOrderArr.push(sum);
    }

    // 合并第一行与数据行
    let resultData = [];
    resultData.push(firstRow);
    resultData = resultData.concat(detailRow);

    const [...tmpDataset] = resultData;
    const [...tmpDatasettable] = resultData;
    tmpDatasettable.push(sumOrderArr);

    return {
      dataset: tmpDataset,// 用于eChart图表
      datasettable: tmpDatasettable,// 用于表格
    };
  }

}
