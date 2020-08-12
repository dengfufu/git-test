import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';

declare var BMap;

/**
 * 行走轨迹
 */
@Component({
  selector: 'app-pos-track',
  templateUrl: 'pos-track.component.html'
})
export class PosTrackComponent implements AfterViewInit {

  @ViewChild('container', {static: false}) containerElement: ElementRef;

  map: any;
  tarPoints = new Array();
  contentArray = new Array();

  posStartTime: string;
  posEndTime: string;

  constructor(public httpClient: HttpClient) {
  }

  ngAfterViewInit(): void {
    this.initMap();
  }

  rangePickerChange(event: any) {
    this.posStartTime = event[0];
    this.posEndTime = event[1];
    this.loadData();
  }

  loadData() {
    this.httpClient.post('/api/pos/position/listPosTrack', {posStartTime: this.posStartTime, posEndTime: this.posEndTime})
      .subscribe((res: any) => {
        this.contentArray = res.data;
        this.showPosTrack(this.contentArray);
        this.showMap();
      });
  }

  initMap() {
    this.map = new BMap.Map(this.containerElement.nativeElement);             // 创建地图实例
    this.map.centerAndZoom('中国', 5);                      // 初始化地图，设置中心点坐标和地图级别(默认显示中国)
    this.map.addControl(new BMap.NavigationControl());     // 添加平移缩放控件
    this.map.addControl(new BMap.ScaleControl());          // 添加比例尺控件
    this.map.addControl(new BMap.OverviewMapControl());    // 添加缩略地图控件
    this.map.addControl(new BMap.MapTypeControl());        // 添加地图类型控件
    this.map.enableScrollWheelZoom();
  }

  showPosTrack(data) {
    this.tarPoints = new Array();  // 重新清空坐标数组
    for (const item of data) {
      const targetPoint = new BMap.Point(item.bdLon, item.bdLat); // 转换后的目标坐标
      this.tarPoints.push(targetPoint);
    }
  }

  showMap() {
    let index = 0;
    for (const tarPoint of this.tarPoints) {
      const targetPoint = new BMap.Point(tarPoint.lng, tarPoint.lat); // 转换后的目标坐标
      const content = '<h4>定位信息</h4>'
        + '时间：' + this.contentArray[index].posTime
        + '<br>经度：' + this.contentArray[index].lon
        + '<br>纬度：' + this.contentArray[index].lat
        + '<br>地址：' + this.contentArray[index].addr;

      const marker = this.createMark(tarPoint.lng, tarPoint.lat, content, index);
      this.map.addOverlay(marker);
      index++;
    }
    // 将坐标连线
    const polyline = new BMap.Polyline(this.tarPoints, {strokeColor: 'blue', strokeWeight: 8, strokeOpacity: 1.5});
    this.map.addOverlay(polyline);   // 在地图上画拆线
    this.map.setViewport(this.tarPoints);　// 自动展现坐标坐落范围内的区域
  }

  createMark(lng, lat, content, index) {
    const posMarker = new BMap.Marker(new BMap.Point(lng, lat));

    posMarker.enableDragging();
    const label = new BMap.Label('定位' + index);
    const offset = new BMap.Size(0, 25);
    label.setOffset(offset);
    posMarker.setLabel(label);

    posMarker.addEventListener('click', function(e) {
      this.openInfoWindow(new BMap.InfoWindow(content));
    });

    return posMarker;
  }
}
