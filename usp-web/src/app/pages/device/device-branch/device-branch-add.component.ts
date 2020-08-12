import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {NzModalService} from 'ng-zorro-antd';
import {BaiduMapComponent} from '../../common/baidu-map/baidu-map.component';
import {UserService} from '@core/user/user.service';
import {ZonValidators} from '@util/zon-validators';
import {ZorroUtils} from '@util/zorro-utils';
import {Result} from '@core/interceptor/result';

@Component({
  selector: 'app-device-branch-add',
  templateUrl: 'device-branch-add.component.html',
  styleUrls: ['device-branch.component.less']
})
export class DeviceBranchAddComponent implements OnInit {

  ZorroUtils = ZorroUtils;
  currentCorpId = this.userService.currentCorp.corpId;
  validateForm: FormGroup;
  customCorpList: any[] = [];
  serviceCorpList: any[] = [];
  contactUserList: any[] = [];
  upperBranchList = [];
  areaList: any;
  isServiceProvider = this.userService.currentCorp.serviceProvider  === 'Y';
  spinning = false;
  demanderCorpOptions: any = [];
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private userService: UserService,
              private httpClient: HttpClient,
              private modalService: NzModalService) {
    this.validateForm = this.formBuilder.group({
      demanderCustom: [null, [ZonValidators.required('客户名称')]],
      branchName: [null, [ZonValidators.required('网点名称'), ZonValidators.maxLength(50), ZonValidators.notEmptyString()]],
      area: [null, [Validators.required]],
      address: [null, [ZonValidators.required('详细地址'), ZonValidators.maxLength(100), ZonValidators.notEmptyString()]],
      upperBranchId: [],
      description: [],
      branchPhone: [],
      contactId: [],
      contactName: [null, [ZonValidators.required('联系人姓名'), ZonValidators.maxLength(20, '联系人姓名')]],
      contactOption: [null, [Validators.required]],
      contactPhone: [null, [Validators.required]],
      province: [],
      city: [],
      district: [],
      lon: [],
      lat: [],
      enabled: [true, [Validators.required]],
      demanderCorp: []
    });
  }

  ngOnInit(): void {
    this.listArea();
    if( this.isServiceProvider) {
      this.listDemanderCorp();
      this.validateForm.controls.demanderCorp.setValidators([ZonValidators.required()]);
    } else {
      this.listCustomCorp(this.currentCorpId);
    }
  }

  listDemanderCorp() {
    this.httpClient.get('/api/anyfix/demander/list').subscribe((result: Result) => {
      this.demanderCorpOptions = result.data || [];
      if (this.demanderCorpOptions.length > 1) {
        this.validateForm.patchValue({
          demanderCorp: this.demanderCorpOptions[0].demanderCorp
        });
      }
    });
  }

  submitForm(value: any): void {
    this.spinning = true;
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    value.enabled = this.validateForm.value.enabled ? 'Y' : 'N';
    this.httpClient
      .post('/api/anyfix/device-branch/add',
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
    if (values === null) {
      return;
    }
    this.validateForm.controls.province.setValue(values[0]);
    this.validateForm.controls.city.setValue(values[1]);
    this.validateForm.controls.district.setValue(values[2]);
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
        if (this.customCorpList.length > 0) {
          this.validateForm.patchValue({
            demanderCustom: this.customCorpList[0]
          });
          this.matchDeviceBranch();
          // 因逻辑修改，此处不查询
          /*this.matchCorpUser();*/
        }
      });
  }



  /**
   * 客户名称改变
   */
  customCorpChange() {
    // 清空联系人及电话
    this.contactUserList = [];
    this.validateForm.controls.contactOption.reset();
    this.validateForm.controls.contactPhone.reset();
    this.validateForm.patchValue({
      upperBranchId: null
    });
    this.matchDeviceBranch();
  }

  /**
   * 模糊查询上级网点
   * @param name 网点名称
   */
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

  listArea() {
    this.httpClient.get('/api/uas/area/list')
      .subscribe((res: any) => {
        this.areaList = res.data;
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

        // 不存在的人员，则新增
        if (name && name.trim() !== '' && this.contactUserList.length === 0) {
          /*this.contactUserList.push({value: '', text: name});
          this.validateForm.controls.contactName.setValue(name);*/
          this.contactUserList.push({value: 0, text: name});
          this.validateForm.controls.contactName.setValue(name);
        }
      });
  }

  // 聚焦回滚
  matchrollback() {
    this.contactUserList = [];
  }

  /**
   * 联系人改变
   * @param event 联系人编号+联系电话
   */
  changeContact(event) {
    if (!event) {
      return;
    }
    if (event !== '' && event !== null && event !== undefined && event.length >= 2) {
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
  }

  openMap() {
    const modal = this.modalService.create({
      nzTitle: '选择经纬度',
      nzContent: BaiduMapComponent,
      nzComponentParams: {
        point:
          {
            lon: this.validateForm.controls.lon.value,
            lat: this.validateForm.controls.lat.value
          }
      },
      nzFooter: null,
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
}
