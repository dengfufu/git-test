import {ChangeDetectorRef, Input, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzModalRef, NzModalService} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {BaiduMapComponent} from '../../../common/baidu-map/baidu-map.component';
import {AreaService} from '@core/area/area.service';
import {ZonValidators} from '@util/zon-validators';
import { ZorroUtils } from '@util/zorro-utils';

@Component({
  selector: 'app-device-locate-edit',
  templateUrl: 'device-locate-edit.component.html',
  styleUrls: ['device-locate.component.less']
})
export class DeviceLocateEditComponent implements OnInit {

  ZorroUtils = ZorroUtils;
  @Input() deviceInfo: any = {};
  validateForm: FormGroup;
  branchList: Array<any> = [];
  areaList: any;

  spinning = false;
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private modalService: NzModalService,
              private httpClient: HttpClient,
              private areaService: AreaService) {
    this.validateForm = this.formBuilder.group({
      deviceCode: [],
      area: [null, [Validators.required]],
      district: [],
      branchId: [],
      address: [],
      zone: [null, [ZonValidators.required('市区/郊县')]],
      status: [null, [Validators.required]],
      installDate: [],
      lon: [],
      lat: [],
      description: []
    });
  }

  initValue() {
    this.validateForm.patchValue({
      deviceCode: this.deviceInfo.deviceCode,
      district: this.deviceInfo.district,
      branchId: !this.deviceInfo.branchId || this.deviceInfo.branchId === '0' ? null : this.deviceInfo.branchId,
      address: this.deviceInfo.address,
      zone: this.deviceInfo.zone.toString(),
      status: !this.deviceInfo.status || this.deviceInfo.status === 0 ? null : this.deviceInfo.status.toString(),
      installDate: this.deviceInfo.installDate,
      lon: this.deviceInfo.lon,
      lat: this.deviceInfo.lat,
      description: this.deviceInfo.description
    });
    if (this.deviceInfo.district && this.deviceInfo.district.length > 0) {
      this.validateForm.patchValue({
        area: this.areaService.getAreaListByDistrict(this.deviceInfo.district)
      });
    }
  }

  ngOnInit(): void {
    this.initValue();
    this.matchDeviceBranch();
    this.getAreaList();
  }

  // 模糊查询设备网点
  matchDeviceBranch(branchName?: string) {
    // if (this.deviceInfo.customCorp === '0' || this.deviceInfo.customCorp === null) {
    //   this.deviceInfo.customCorp = this.deviceInfo.demanderCorp;
    // }
    // if (this.deviceInfo.customCorp === '0' || this.deviceInfo.customCorp === null) {
    //   return;
    // }
    const params = {
      matchFilter: branchName,
      customId: this.deviceInfo.customId
    };
    this.httpClient.post('/api/anyfix/device-branch/match', params)
      .subscribe((res: any) => {
        this.branchList = res.data;
      });
  }

  getAreaList() {
    this.httpClient
      .get('/api/uas/area/list')
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.areaList = res.data;
      });
  }

  areaChange(event) {
    if (event === null) {
      return;
    }
    if (event && event.length > 0) {
      this.validateForm.controls.district.setValue(event[event.length - 1]);
    }
  }

  choosePosition() {
    const modal = this.modalService.create({
      nzTitle: '选择地点',
      nzContent: BaiduMapComponent,
      nzFooter: null,
      nzComponentParams: {point: {lon: this.validateForm.controls.lon.value, lat: this.validateForm.controls.lat.value}},
      nzWidth: 800
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        this.validateForm.patchValue({
          lon: result.mark.point.lng,
          lat: result.mark.point.lat,
          address: result.address
        });
      }
    });
  }

  branchChange(event) {
    if (event && event.length > 0) {
      this.branchList.forEach(branch => {
        if (event === branch.branchId) {
          this.validateForm.patchValue({
            area: [branch.province, branch.city, branch.district],
            lon: branch.lon,
            lat: branch.lat,
            address: branch.address
          });
        }
      });
    }
  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    value.deviceId = this.deviceInfo.deviceId;
    this.spinning = true;
    this.httpClient
      .post('/api/device/device-locate/update',
        value)
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        this.modal.destroy('submit');
      });
  }

  destroyModal(): void {
    this.modal.destroy();
  }

}
