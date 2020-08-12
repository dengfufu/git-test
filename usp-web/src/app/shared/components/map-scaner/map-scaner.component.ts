import {Component, Input, AfterViewInit} from '@angular/core';

declare let BMap: any;
declare let BMap_Symbol_SHAPE_POINT: any;

@Component({
  selector: 'app-map-scaner',
  templateUrl: './map-scaner.component.html',
  styleUrls: ['./map-scaner.component.less']
})
export class MapScanerComponent implements AfterViewInit {

  shouldShowMap = false;
  text = '查看地图';
  @Input() point: any;
  constructor() { }

  ngAfterViewInit() {

  }

  clickButton() {
    this.shouldShowMap = !this.shouldShowMap;
    this.text = this.shouldShowMap? '隐藏地图':'查看地图';
    setTimeout(() => {
      if (this.shouldShowMap) {
        this.showMap(this.point.lon, this.point.lat, 'map');
      }
    }, 100);
  }

  /**
   * 显示地图
   */
  showMap(bdLon: any, bdLat: any, divName: any) {
    const mapZoom = 18;
    const map = this.initMap(bdLon, bdLat, mapZoom, divName);
    const labelTitle = '签到位置';
    const marker = this.createMarker(bdLon, bdLat, labelTitle);
    map.addOverlay(marker);
    const point = new BMap.Point(bdLon, bdLat);
    map.panTo(point);
    // this.setCenter(bdLon, bdLat, map);
  }

  /**
   * baidu Map画面初始化
   */
  initMap(lon, lat, zoom, divName) {
    const map = new BMap.Map(divName);
    const point = new BMap.Point(lon, lat);
    map.centerAndZoom(point, zoom);
    // map.addControl(new BMap.NavigationControl());
    // map.addControl(new BMap.ScaleControl());
    // map.addControl(new BMap.OverviewMapControl());
    // map.addControl(new BMap.MapTypeControl());
    map.enableScrollWheelZoom(); // 启动滚轮缩放
    return map;
  }

  /**
   * 创建标注及label
   */
  createMarker(bdLon, bdLat, title) {
    const bdPoint = new BMap.Point(bdLon, bdLat);
    const marker = new BMap.Marker(bdPoint, {
      icon: new BMap.Symbol(BMap_Symbol_SHAPE_POINT, {
        scale: 1, // 图标缩放大小
        fillColor: 'red', // 填充颜色
        fillOpacity: 0.8 // 填充透明度
      })
    });
    const label = new BMap.Label(title, {offset: new BMap.Size(20, -10)});
    marker.setLabel(label); // 添加label
    return marker;
  }
}
