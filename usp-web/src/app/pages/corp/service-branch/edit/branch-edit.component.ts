import {ChangeDetectorRef, Input, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef, NzModalService} from 'ng-zorro-antd';
import {BaiduMapComponent} from '../../../common/baidu-map/baidu-map.component';
import {ZonValidators} from '@util/zon-validators';
import {Result} from '@core/interceptor/result';
import {AreaService} from '@core/area/area.service';
import {UserService} from '@core/user/user.service';
import { ZorroUtils } from '@util/zorro-utils';


@Component({
  selector: 'app-corp-service-branch-edit',
  templateUrl: 'branch-edit.component.html'
})
export class BranchEditComponent implements OnInit {

  ZorroUtils = ZorroUtils;
  @Input() branchId;

  validateForm: FormGroup;
  contactOptionList: any;
  values: any[] = null;
  nzOptions: any;
  spinning = false;

  upperBranchList: any[] = [];

  serviceBranch: any;
  serviceCorp: any;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private modalService: NzModalService,
              private httpClient: HttpClient,
              private userService: UserService,
              private areaService: AreaService) {
    this.validateForm = this.formBuilder.group({
      branchId: [null, [Validators.required]],
      branchName: [null, [ZonValidators.required('网点名称'), ZonValidators.notEmptyString(), ZonValidators.maxLength(50)]],
      upperBranchId: [],
      area: [null, [Validators.required]],
      address: [null, [Validators.required]],
      serviceCorp: [null, [Validators.required]],
      description: [],
      branchPhone: [],
      contactId: [],
      contactPhone: [],
      contactOption: [],
      province: [null, [Validators.required]],
      city: [null, [Validators.required]],
      district: [],
      type: [null, [Validators.required]],
      contactName: [],
      enabled: [],
      lon: [],
      lat: []
    });
  }

  ngOnInit(): void {
    this.initServiceBranch();
    this.listArea();
  }

  /**
   * 初始化网点数据
   */
  initServiceBranch() {
    this.spinning = true;
    this.httpClient
      .get('/api/anyfix/service-branch/' + this.branchId)
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        if (result.code === 0) {
          this.serviceBranch = result.data;
          this.serviceCorp = this.serviceBranch.serviceCorp;
          let contactOptionStr: any;
          if (this.serviceBranch.contactId && this.serviceBranch.contactId !== '0') {
            contactOptionStr = this.serviceBranch.contactId + ','
              + this.serviceBranch.contactPhone + ',' + this.serviceBranch.contactName;
          }
          let area = this.serviceBranch.province ? this.serviceBranch.province : '';
          area = this.serviceBranch.city ? this.serviceBranch.city : area;
          area = this.serviceBranch.district ? this.serviceBranch.district : area;
          const areaArray: string[] = this.areaService.getAreaListByDistrict(area);
          this.validateForm.patchValue({
            branchId: this.serviceBranch.branchId,
            branchName: this.serviceBranch.branchName,
            // tslint:disable-next-line:triple-equals
            upperBranchId: this.serviceBranch.upperBranchId == '0' ? null : this.serviceBranch.upperBranchId,
            contactName: this.serviceBranch.contactName,
            contactPhone: this.serviceBranch.contactPhone,
            contactOption: contactOptionStr,
            address: this.serviceBranch.address,
            serviceCorp: this.serviceBranch.serviceCorp,
            description: this.serviceBranch.description,
            branchPhone: this.serviceBranch.branchPhone,
            province: this.serviceBranch.province,
            city: this.serviceBranch.city,
            district: this.serviceBranch.district,
            type: this.serviceBranch.type.toString(),
            lon: this.serviceBranch.lon,
            lat: this.serviceBranch.lat,
            enabled: this.serviceBranch.enabled === 'Y',
            area: areaArray
          });
          this.matchServiceBranch();
          this.matchContactUser();
        }
      });
  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    value.enabled = this.validateForm.value.enabled ? 'Y' : 'N';
    this.spinning = true;
    this.httpClient
      .post('/api/anyfix/service-branch/update',
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

  changeContact(event) {
    if (!event) {
      return;
    }
    const temp = event.split(',');
    this.validateForm.controls.contactId.setValue(temp[0]);
    this.validateForm.controls.contactPhone.setValue(temp[1]);
    this.validateForm.controls.contactName.setValue(temp[2]);
  }

  onChanges(values: any): void {
    if (!values) {
      return;
    }
    this.validateForm.controls.province.setValue(values[0]);
    this.validateForm.controls.city.setValue(values[1]);
    this.validateForm.controls.district.setValue(values[2]);
  }

  /**
   * 网点名称模糊下拉
   * @param value 网点编号
   */
  matchServiceBranch(value?: string): void {
    const params = {
      serviceCorp: this.userService.currentCorp.corpId,
      branchName: value
    };
    this.httpClient
      .post('/api/anyfix/service-branch/match', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.upperBranchList = res.data || [];
        let match = false;
        this.upperBranchList.forEach(option => {
          if (option.branchId === this.serviceBranch.upperBranchId) {
            match = true;
          }
        });
        if (!match && this.serviceBranch.upperBranchId !== '0') {
          this.upperBranchList.unshift({
            branchId: this.serviceBranch.upperBranchId,
            branchName: this.serviceBranch.upperBranchName
          });
        }
      });
  }

  matchContactUser(filter?) {
    const params = {corpId: this.serviceCorp, matchFilter: filter};
    this.httpClient
      .post('/api/uas/corp-user/match', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const list = [];
        res.data.forEach(item => {
          list.push({
            value: item.userId + ',' + item.mobile + ',' + item.userName,
            text: item.userName + '(' + item.mobile + ')'
          });
        });
        this.contactOptionList = list || [];
        let match = false;
        const userList = res.data || [];
        userList.forEach(option => {
          if (option.userId === this.serviceBranch.contactId) {
            match = true;
          }
        });
        if (!match && this.serviceBranch.contactId && this.serviceBranch.contactId !== '0') {
          this.contactOptionList.unshift({
            value: this.serviceBranch.contactId + ',' + this.serviceBranch.contactPhone + ',' + this.serviceBranch.contactName,
            text: this.serviceBranch.contactName + '(' + this.serviceBranch.contactPhone + ')'
          });
        }
      });
  }

  listArea() {
    this.httpClient
      .get('/api/uas/area/list')
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.nzOptions = res.data;
      });
  }

  openMap() {
    const modal = this.modalService.create({
      nzTitle: '选择经纬度',
      nzContent: BaiduMapComponent,
      nzFooter: null,
      nzComponentParams: {point: {lon: this.validateForm.controls.lon.value, lat: this.validateForm.controls.lon.value}},
      nzWidth: 800
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        this.validateForm.controls.lon.setValue(result.mark.point.lng);
        this.validateForm.controls.lat.setValue(result.mark.point.lat);
        this.validateForm.controls.address.setValue(result.address);
      }
    });
  }
}
