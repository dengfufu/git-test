import {Component, OnDestroy, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import * as echart from 'echarts';
import {DefaultRangeDate, RangeDates, WorkplaceService} from '../../workplace.service';
import {Subscription} from 'rxjs';
import {NavigationEnd, Router} from '@angular/router';
import {filter} from 'rxjs/operators';

/**
 * 工单城市分布地图
 */
@Component({
  selector: 'app-work-city-map',
  templateUrl: 'work-city-map.component.html'
})
export class WorkCityMapComponent implements OnInit, OnDestroy {

  isLoading = false;
  isEmptyData = true;
  cityData: any[] = [];
  workTotalCount = 0; // 工单总数
  chartOption: any;
  rangeDate = DefaultRangeDate;
  rangeDates: any = RangeDates;
  geoCoordMap: any = {};
  reloadSubscription: Subscription;

  constructor(public workplaceService: WorkplaceService,
              public httpClient: HttpClient,
              public router: Router,) {
    this.reloadSubscription = this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(_ => {
      this.cityData = [];
      this.workTotalCount = 0;
      this.loadData();
    });
  }


  ngOnInit() {
    this.httpClient.get('assets/echart/china.json?v=1.0').subscribe((chinaData: any) => {
      // 注册地图
      echart.registerMap('china', chinaData);
      // 省份和地理位置map
      const mapFeatures = chinaData.features;
      mapFeatures.forEach((v) => {
        const name = v.properties.name; // 地区名称
        this.geoCoordMap[name] = v.properties.cp;// 地区经纬度
      });
      // 加载数据
      this.loadData();
    });
  }

  dateRangeChange(event) {
    this.loadData();
  }

  loadData() {
    this.isLoading = true;
    this.workplaceService.initWorkCityData(this.rangeDate).subscribe((res: any) => {
      this.isLoading = false;
      if (res) {
        this.isEmptyData = false;
        const mapData = res.cityDate;
        this.cityData = res.cityDate.sort((a, b) => {
          return b.value - a.value;
        });
        const countRange = res.countRange;
        this.workTotalCount = res.totalCount;
        this.renderChart(mapData, this.convertData(mapData), [], countRange);
      } else {
        this.isEmptyData = true;
      }
    });
  }

  renderChart(mapData: any[], scatterData: any[], effectScatterData: any[], visualMapData: number[]) {
    this.chartOption = {
      tooltip: {
        padding: 0,
        formatter: (params) => {
          let detailHtml = '';
          const province = params.name;
          const provinceMapData = mapData.find((data) => {
            return data.name === province;
          });
          if (provinceMapData && provinceMapData.status) {
            provinceMapData.status.forEach((status: any) => {
              const tmp = `
                  <p style="color:#fff;font-size:12px;">
                    <i style="display:inline-block;width:10px;height:10px;background:#0abdea;border-radius:40px;margin:0 8px"></i>
                    ${status.name}：
                    <span style="color:#0abdea;margin:0 6px;">${status.value}</span>
                  </p>
                `;
              detailHtml = detailHtml + tmp;
            });
          }
          const tipHtml = `
            <div style="width:280px; min-height:180px;border:1px solid rgba(197,197,197,0.7)">
              <div style="width:100%;height:40px;line-height:40px;border-bottom:2px solid #d0d0d0;padding:0 20px">
                  <i style="display:inline-block;width:8px;height:8px;background:#0abdea;border-radius:40px;"></i>
                  <span style="margin-left:10px;color:#fff;font-size:16px;">
                    ${params.name}：${provinceMapData && provinceMapData.value || 0}
                  </span>
              </div>
              <div style="padding:20px">
                ${detailHtml}
              </div>
            </div>
          `;
          return tipHtml;
        },
      },
      visualMap: {
        show: true,
        min: 0,
        max: visualMapData[1],
        left: '10%',
        top: 'bottom',
        calculable: true,
        seriesIndex: [1],
        inRange: {
          color: ['#b8e2ff', '#15a8fe']
        }
      },
      geo: {
        show: true,
        map: 'china',
        label: {
          normal: {
            show: false
          },
          emphasis: {
            show: false,
          }
        },
        roam: false,
        itemStyle: {
          normal: {
            areaColor: '#fbfbfb',
            borderColor: '#878787',
          },
          emphasis: {
            areaColor: '#1389fe',
          }
        }
      },
      series: [
        {
          name: '散点',
          type: 'scatter',
          coordinateSystem: 'geo',
          data: scatterData,
          symbolSize: (val) => {
            return 0;
          },
          // label: {
          //   normal: {
          //     formatter: '{b}',
          //     position: 'right',
          //     show: true
          //   },
          //   emphasis: {
          //     show: true
          //   }
          // },
          // itemStyle: {
          //   normal: {
          //     color: '#FFFFFF'
          //   }
          // }
        },
        {
          type: 'map',
          map: 'china',
          geoIndex: 0,
          aspectScale: 0.75, // 长宽比
          showLegendSymbol: false, // 存在legend时显示
          label: { // 图形上的文本标签
            normal: {
              show: true
            },
            emphasis: {
              show: false,
              textStyle: {
                color: '#fff'
              }
            }
          },
          roam: false, // 开启鼠标缩放和平移漫游
          animation: false,
          data: mapData
        },
        // {
        //   name: '点',
        //   type: 'scatter',
        //   coordinateSystem: 'geo',
        //   zlevel: 6,
        // },
        // {
        //   name: 'Top 10',
        //   type: 'effectScatter',
        //   coordinateSystem: 'geo',
        //   data: effectScatterData,
        //   symbolSize: (val) => {
        //     return val[2] / 10;
        //   },
        //   showEffectOn: 'render',
        //   rippleEffect: {
        //     brushType: 'stroke'
        //   },
        //   hoverAnimation: true,
        //   label: {
        //     normal: {
        //       formatter: '{b}',
        //       position: 'left',
        //       show: false
        //     }
        //   },
        //   itemStyle: {
        //     normal: {
        //       color: 'yellow',
        //       shadowBlur: 10,
        //       shadowColor: 'yellow'
        //     }
        //   },
        //   zlevel: 1
        // },
      ]
    };
  }

  convertData(mapData: any[]) {
    const res = [];
    console.log(this.geoCoordMap);
    mapData.forEach((v) => {
      const geoCoord = this.geoCoordMap[v.name];
      if (geoCoord) {
        res.push({
          name: v.name,
          value: geoCoord.concat(v.value),
        });
      }
    });
    return res;
  }

  getTop10Data(mapData: any[]) {
    return this.convertData(mapData.sort((a, b) => {
      return b.value - a.value;
    }).slice(0, 10));
  }

  ngOnDestroy() {
    this.reloadSubscription.unsubscribe();
  }
}
