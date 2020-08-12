import {ChangeDetectorRef, Input, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {BaiduMapComponent} from '../../common/baidu-map/baidu-map.component';
import {NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {ZonValidators} from '@util/zon-validators';
import {ZorroUtils} from '@util/zorro-utils';
import {Result} from '@core/interceptor/result';

@Component({
  selector: 'app-device-branch-edit',
  templateUrl: 'device-branch-edit.component.html',
  styleUrls: ['device-branch.component.less']
})
export class DeviceBranchEditComponent implements OnInit {

  ZorroUtils = ZorroUtils;
  @Input() deviceBranch;

  validateForm: FormGroup;
  currentCorpId = this.userService.currentCorp.corpId;
  customCorpList: any[] = [];
  serviceCorpList: any[] = [];
  contactUserList: any[] = [];
  upperBranchList = [];
  areaList: any;
  demanderCorpOptions: any = [];
  isServiceProvider = this.userService.currentCorp.serviceProvider  === 'Y';
  spinning = false;
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private modalService: NzModalService,
              private httpClient: HttpClient,
              private userService: UserService) {
    this.validateForm = this.formBuilder.group({
      demanderCustom: [null, [ZonValidators.required('客户名称')]],
      branchName: [null, [ZonValidators.required('网点名称'), ZonValidators.maxLength(50), ZonValidators.notEmptyString()]],
      branchId: [null, [Validators.required]],
      area: [null, [Validators.required]],
      address: [null, [Validators.required]],
      upperBranchId: [],
      branchPhone: [],
      description: [],
      contactId: [],
      contactName: [null, [ZonValidators.required('联系人姓名'), ZonValidators.maxLength(20, '联系人姓名')]],
      contactOption: [null, [Validators.required]],
      contactPhone: [null, [Validators.required]],
      province: [null, [Validators.required]],
      city: [null, [Validators.required]],
      district: [null, [Validators.required]],
      lon: [],
      lat: [],
      enabled: [],
      demanderCorp: []
    });
  }

  ngOnInit(): void {
    const demanderCustom: any = {};
    demanderCustom.customId = this.deviceBranch.customId;
    demanderCustom.customCorp = this.deviceBranch.customCorp;
    this.validateForm.controls.demanderCustom.setValue(demanderCustom);
    this.validateForm.controls.branchId.setValue(this.deviceBranch.branchId);
    this.validateForm.controls.branchName.setValue(this.deviceBranch.branchName);
    this.validateForm.controls.contactId.setValue(this.deviceBranch.contactId);
    this.validateForm.controls.contactName.setValue(this.deviceBranch.contactName);
    this.validateForm.controls.contactPhone.setValue(this.deviceBranch.contactPhone);
    this.validateForm.controls.contactOption.setValue(this.deviceBranch.contactId + ',' + this.deviceBranch.contactPhone);
    this.validateForm.controls.address.setValue(this.deviceBranch.address);
    this.validateForm.controls.description.setValue(this.deviceBranch.description);
    this.validateForm.controls.province.setValue(this.deviceBranch.province);
    this.validateForm.controls.city.setValue(this.deviceBranch.city);
    this.validateForm.controls.district.setValue(this.deviceBranch.district);
    this.validateForm.controls.branchPhone.setValue(this.deviceBranch.branchPhone);
    this.validateForm.controls.lon.setValue(this.deviceBranch.lon);
    this.validateForm.controls.lat.setValue(this.deviceBranch.lat);
    this.validateForm.controls.enabled.setValue(this.deviceBranch.enabled === 'Y');
    this.validateForm.controls.upperBranchId.setValue(this.deviceBranch.upperBranchId);
    this.validateForm.controls.demanderCorp.setValue(this.deviceBranch.demanderCorp);

    const area = [this.deviceBranch.province, this.deviceBranch.city, this.deviceBranch.district];
    this.validateForm.controls.area.setValue(area);

    if (this.deviceBranch.contactId && this.deviceBranch.contactId !== '0') {
      this.matchCorpUser();
    } else {
      this.validateForm.controls.contactOption.setValue(0);
      this.matchCorpUser(this.deviceBranch.contactName);
    }
    if (this.isServiceProvider) {
      this.validateForm.controls.demanderCorp.setValidators(Validators.required);
      this.listDemanderCorp();
      if(this.deviceBranch.demanderCorp) {
        this.listCustomCorp(this.deviceBranch.demanderCorp)
      }
    } else {
      this.listCustomCorp(this.currentCorpId);
    }
    this.listArea();
    this.matchDeviceBranch();
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
      .post('/api/anyfix/device-branch/update',
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

  /**
   * 客户列表
   */
  listCustomCorp(demanderCorp) {
    this.httpClient
      .post('/api/anyfix/demander-custom/custom/list', {
        demanderCorp
      })
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.customCorpList = res.data || [];
        const demanderCustom = this.validateForm.controls.demanderCustom.value;
        this.customCorpList.forEach(item => {
          if( demanderCustom) {
            if (demanderCustom.customId !== '0') {
              if (item.customId === demanderCustom.customId) {
                this.validateForm.controls.demanderCustom.setValue(item,
                  {onlySelf: false, emitViewToModelChange: false});
              }
            } else {
              if (item.customCorp === demanderCustom.customCorp) {
                this.validateForm.controls.demanderCustom.setValue(item,
                  {onlySelf: false, emitViewToModelChange: false});
              }
            }
          }
        });
      });
  }

  /**
   * 客户名称改变
   */
  customCorpChange() {
    this.validateForm.patchValue({
      upperBranchId: null
    });
    this.matchDeviceBranch();
  }

  matchDeviceBranch(name?: string): void {
    const demanderCustom = this.validateForm.controls.demanderCustom.value;
    if (!demanderCustom) {
      return;
    }
    const params = {
      matchFilter: name,
      customId: demanderCustom.customId,
      customCorp: demanderCustom.customCorp
    };
    this.httpClient
      .post('/api/anyfix/device-branch/match', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.upperBranchList = res.data;
      });
  }

  /**
   * 模糊查询企业人员
   * 若输入不存在的，则新增
   * @param name 人员姓名
   */
  matchCorpUser(name?: string) {
    const demanderCustom = this.validateForm.controls.demanderCustom.value;
    if (!demanderCustom) {
      return;
    }
    const payload = {
      corpId: demanderCustom.customCorp,
      matchFilter: name
    };
    this.httpClient
      .post('/api/uas/corp-user/match', payload)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const listOfOption: Array<{value: string; text: string}> = [];
        res.data.forEach(item => {
          listOfOption.push({
            value: item.userId + ',' + item.mobile,
            text: item.userName + '(' + item.mobile + ')'
          });
        });
        this.contactUserList = listOfOption || [];
        if (this.deviceBranch.contactId && this.deviceBranch.contactId !== '0') {
          let match = false;
          this.contactUserList.forEach(option => {
            const temp = option.value.split(',');
            if (temp[0] === this.deviceBranch.contactId) {
              match = true;
            }
          });
          if (!match) {
            this.contactUserList.unshift({
              value: this.deviceBranch.contactId + ',' + this.deviceBranch.contactPhone,
              text: this.deviceBranch.contactName + '(' + this.deviceBranch.contactPhone + ')'
            });
          }
        }
        // 不存在的人员，则新增
        if (name && name.trim() !== '' && this.contactUserList.length === 0) {
          this.contactUserList.push({value: 0, text: name});
          this.validateForm.controls.contactName.setValue(name);
        }
      });
  }

  listArea() {
    this.httpClient.get('/api/uas/area/list')
      .subscribe((res: any) => {
        this.areaList = res.data;
      });
  }

  changeContact(event) {
    if (!event) {
      return;
    }
    const temp = event.split(',');
    this.validateForm.controls.contactId.setValue(temp[0]);
    this.validateForm.controls.contactPhone.setValue(temp[1]);
    this.contactUserList.forEach(item => {
      const itemTemp = item.value.split(',');
      if (itemTemp[0] === temp[0]) {
        this.validateForm.controls.contactName.setValue(item.text.substring(0, item.text.lastIndexOf('(')));
      }
    });
  }

  onChanges(values: any): void {
    if (values === null) {
      return;
    }
    if (values.length > 0) {
      this.validateForm.controls.province.setValue(values[0]);
    }
    if (values.length > 1) {
      this.validateForm.controls.city.setValue(values[1]);
    }
    if (values.length > 2) {
      this.validateForm.controls.district.setValue(values[2]);
    }
  }

  openMap() {
    const modal = this.modalService.create({
      nzTitle: '选择经纬度',
      nzContent: BaiduMapComponent,
      nzFooter: null,
      nzComponentParams: {
        point: {
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

  demanderCorpChange(demanderCorp: any) {
    this.validateForm.controls.demanderCustom.setValue(null);
    if(demanderCorp) {
      this.listCustomCorp(demanderCorp);
    }
  }

  listDemanderCorp() {
    this.httpClient.get('/api/anyfix/demander/list').subscribe((result: Result) => {
      this.demanderCorpOptions = result.data || [];
    });
  }

}
