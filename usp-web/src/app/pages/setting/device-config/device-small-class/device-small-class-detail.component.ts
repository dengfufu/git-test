import {Input, Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {DeviceSpecification} from './specification/device-specification-edit.component';

@Component({
  selector: 'app-device-small-class-detail',
  templateUrl: 'device-small-class-detail.component.html'
})
export class DeviceSmallClassDetailComponent implements OnInit {

  @Input() smallClassId;
  deviceSmallClass: any = {};
  deviceSpecificationList: DeviceSpecification[] = []; // 设备规格列表

  constructor(private httpClient: HttpClient) {
  }

  ngOnInit(): void {
    this.initDeviceSmallClass();
  }

  /**
   * 初始化设备类型数据
   */
  initDeviceSmallClass() {
    this.httpClient.get('/api/device/device-small-class/' + this.smallClassId)
      .subscribe((res: any) => {
        this.deviceSmallClass = res.data;
        this.deviceSpecificationList = this.deviceSmallClass.deviceSpecificationList;
      });
  }

}
