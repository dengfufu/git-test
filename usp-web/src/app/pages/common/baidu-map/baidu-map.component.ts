import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {NzModalRef} from 'ng-zorro-antd';
declare var BMap;

@Component({
    selector: 'zon-baidu-map',
    templateUrl: 'baidu-map.component.html',
    styleUrls: ['baidu-map.component.less'],
})
export class BaiduMapComponent implements OnInit {

    @Input() point: any;
    inputValue = '';
    map: any;
    mark: any;
    address: any;
    constructor(private modal: NzModalRef) {

    }

    ngOnInit() {
      if(this.point.lon == null || this.point.lon === undefined || this.point.lon.length <= 0
        || this.point.lat == null || this.point.lat === undefined || this.point.lon.length <= 0){
        this.point = {lon: 116.404, lat: 39.915}
      }
      setTimeout(() => {
        this.map = this.initMap(this.point.lon, this.point.lat, 18, '');
      }, 500);
    }

  initMap(lon, lat, zoom, divName) {
    const map = new BMap.Map('map');// 创建地图实例
    const point = new BMap.Point(lon, lat);// 创建点坐标
    map.centerAndZoom(point, 15);// 初始化地图，设置中心点坐标和地图级别
    this.mark = new BMap.Marker(point);
    this.mark.enableDragging();
    map.addOverlay(this.mark);
    map.panTo(point);

    // this.mark.addEventListener('dragend',  (e) => {
    //   alert('当前位置：' + e.point.lng + ', ' + e.point.lat);
    //   console.log(this.mark);
    // });
    map.addEventListener('click', (e) =>{ // 给地图添加点击事件
      new BMap.Geocoder().getLocation(e.point, (rs)=> {
        this.mark.point = rs.point;
        this.address = rs.address;
        // 点击后标记变为点击点
        this.map.clearOverlays();
        this.map.addOverlay(new BMap.Marker(rs.point));
      });
    });
    // const geolocation = new BMap.Geolocation();
    // geolocation.getCurrentPosition(function(r){
    //   if(this.getStatus() === 0){
    //     alert('您的位置：'+r.point.lng+','+r.point.lat);
    //   }
    //   else {
    //     alert('failed'+this.getStatus());
    //   }
    // },null);
    map.enableScrollWheelZoom(true);// 开启鼠标滚轮缩放
    function myFun(result){//
      const cityName = result.name;
      map.setCenter(cityName);
    }

    const myCity = new BMap.LocalCity();
    myCity.get(myFun);
    this.map = map;
    this.autoSearch();
    return map;

  }

  getLocation(map){

  }

  G(id) {
    return document.getElementById(id);
  }


  confirm() {
      // 返回坐标和详细地址
    this.modal.destroy({mark: this.mark, address: this.address});
  }

  autoSearch(){
    const ac = new BMap.Autocomplete(    // 建立一个自动完成的对象
      {
        input : 'suggestId',
        location : this.map
      });
    // 去除无用事件
    // ac.addEventListener('onhighlight', (e) => {  // 鼠标放在下拉列表上的事件
    //   // e = 'xixixi';
    //   console.log(e);
    //   console.log('鼠标放在下拉列表上的事件');
    //   let str = '';
    //   let itemValue = e.fromitem.value;
    //   let value = '';
    //   if (e.fromitem.index > -1) {
    //     value = itemValue.province +  itemValue.city +  itemValue.district +  itemValue.street +  itemValue.business;
    //   }
    //   str = 'FromItem<br />index = ' + e.fromitem.index + '<br />value = ' + value;
    //
    //   value = '';
    //   if (e.toitem.index > -1) {
    //     itemValue = e.toitem.value;
    //     value = itemValue.province +  itemValue.city +  itemValue.district +  itemValue.street +  itemValue.business;
    //   }
    //   str += '<br />ToItem<br />index = ' + e.toitem.index + '<br />value = ' + value;
    //   console.log(str);
    //   this.G('searchResultPanel').innerHTML = str;
    // });
    let myValue;
    ac.addEventListener('onconfirm',(e) => {    // 鼠标点击下拉列表后的事件
      const itemValue = e.item.value;
      myValue = itemValue.province +  itemValue.city +  itemValue.district +  itemValue.street +  itemValue.business;
      this.G('searchResultPanel').innerHTML ='onconfirm<br />index = ' + e.item.index + '<br />myValue = ' + myValue;
      this.setPlace(myValue);
    });
  }

  setPlace(myValue){
    this.address = myValue;
    this.map.clearOverlays();    // 清除地图上所有覆盖物
    // function myFun(){
    //   console.log(local);
    //   const pp = local.getResults().getPoi(0).point;    // 获取第一个智能搜索的结果
    //   this.map.centerAndZoom(pp, 18);
    //   this.map.addOverlay(new BMap.Marker(pp));    // 添加标注
    // }
    const local = new BMap.LocalSearch(this.map, { // 智能搜索
      onSearchComplete: () => {
        const pp = local.getResults().getPoi(0).point;    // 获取第一个智能搜索的结果
        this.map.centerAndZoom(pp, 18);
        this.mark = new BMap.Marker(pp);
        this.map.addOverlay(this.mark);
      }
    });
    local.search(myValue);
  }

}
