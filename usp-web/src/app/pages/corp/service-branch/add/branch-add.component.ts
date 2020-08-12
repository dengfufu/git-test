import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {BaiduMapComponent} from '../../../common/baidu-map/baidu-map.component';
import {ZonValidators} from '@util/zon-validators';
import { ZorroUtils } from '@util/zorro-utils';

@Component({
  selector: 'app-corp-service-branch-add',
  templateUrl: 'branch-add.component.html'
})
export class BranchAddComponent implements OnInit {

  ZorroUtils = ZorroUtils;
  validateForm: FormGroup;
  contactOptionList: any;
  values: any[] = null;
  upperBranchList: any[] = [];

  serviceCorp = this.userService.currentCorp.corpId;

  nzOptions: any;
  spinning = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient,
              private modalService: NzModalService,
              private userService: UserService) {
    this.validateForm = this.formBuilder.group({
      serviceCorp: [this.serviceCorp, [Validators.required]],
      branchName: [null, [ZonValidators.required('网点名称'), ZonValidators.notEmptyString(), ZonValidators.maxLength(50)]],
      upperBranchId: [],
      area: [null, [Validators.required]],
      address: [null, [Validators.required]],
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
      enabled: [true, []],
      lon: [],
      lat: []
    });
  }

  ngOnInit(): void {
    this.matchServiceBranch();
    this.matchCorpUser();
    this.listArea();
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
      .post('/api/anyfix/service-branch/add',
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

  onChanges(values: any): void {
    if (!values) {
      return;
    }
    this.validateForm.controls.province.setValue(values[0]);
    if (values[1]) {
      this.validateForm.controls.city.setValue(values[1]);
    }
    if (values[2]) {
      this.validateForm.controls.district.setValue(values[2]);
    }
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
      });
  }

  matchCorpUser(filter?) {
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
        this.contactOptionList = list;
      });
  }

  changeContact(event) {
    if (event === undefined) {
      return;
    }
    const temp = event.split(',');
    this.validateForm.controls.contactId.setValue(temp[0]);
    this.validateForm.controls.contactPhone.setValue(temp[1]);
    this.validateForm.controls.contactName.setValue(temp[2]);
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

  /**
   * 打开地图
   */
  openMap() {
    const modal = this.modalService.create({
      nzTitle: '选择经纬度',
      nzContent: BaiduMapComponent,
      nzFooter: null,
      nzComponentParams: {
        point:
          {
            lon: this.validateForm.controls.lon.value,
            lat: this.validateForm.controls.lat.value
          }
      },
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
