import {Input, Component, OnInit, Output, EventEmitter, ChangeDetectorRef} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {AnyfixModuleService} from '@core/service/anyfix-module.service';
import {ZorroUtils} from '@util/zorro-utils';
import {HttpClient} from '@angular/common/http';
import {UserService} from '@core/user/user.service';
import {AnyfixService} from '@core/service/anyfix.service';
import {finalize} from 'rxjs/operators';
import {Result} from '@core/interceptor/result';
import {WorkCheckService} from '../../work-check.service';
import {toNumber} from '@util/helpers';

@Component({
  selector: 'work-fee-check-list-filter',
  templateUrl: 'fee-check-list-filter.component.html',
  styleUrls: ['fee-check-list-filter.component.less']
})
export class FeeCheckListFilterComponent implements OnInit {

  @Input() params;
  @Input() searchForm;

  @Output() searchFormData: EventEmitter<{key: string}[]> = new EventEmitter<{key: string}[]>();

  optionList: any = {};
  branchOption: any = [];

  // 费用审核状态查询条件
  feeCheckStatusFilter: any = {
    checkedAll: false,
    indeterminate: true
  };
  // 费用确认状态查询条件
  feeConfirmStatusFilter: any = {
    checkedAll: false,
    indeterminate: true
  };
  feeCheckStatusList: any[] = []; // 费用审核状态
  feeConfirmStatusList: any[] = []; // 费用确认状态

  feeCheckUserList: any[] = []; // 人员列表

  ZorroUtils = ZorroUtils;
  nzFilterOption = () => true;

  constructor(public formBuilder: FormBuilder,
              public httpClient: HttpClient,
              public userService: UserService,
              public anyfixModuleService: AnyfixModuleService,
              public anyfixService: AnyfixService,
              public workCheckService: WorkCheckService,
              public cdf: ChangeDetectorRef) {
  }

  ngOnInit(): void {
    this.initStatusCheckbox();
    this.initOptionList();
    this.matchFeeCheckUser();
    this.getBranchOption('');
  }

  submit() {
    // 如果直接赋值会影响表单值
    const params: any = Object.assign({}, this.searchForm.value);
    if (params.area && params.area.length > 0) {
      params.district = params.area[params.area.length - 1];
    } else {
      params.district = null;
    }
    if (params.createTime && params.createTime.length === 2) {
      params.startDate = params.createTime[0];
      params.endDate = params.createTime[1];
    } else {
      params.startDate = null;
      params.endDate = null;
    }
    if (params.finishTime && params.finishTime.length === 2) {
      params.finishStartDate = params.finishTime[0];
      params.finishEndDate = params.finishTime[1];
    } else {
      params.finishStartDate = null;
      params.finishEndDate = null;
    }
    if (params.dispatchTime && params.dispatchTime.length === 2) {
      params.dispatchTimeStart = params.dispatchTime[0];
      params.dispatchTimeEnd = params.dispatchTime[1];
    } else {
      params.dispatchTimeStart = null;
      params.dispatchTimeEnd = null;
    }
    if (this.feeCheckStatusList && this.feeCheckStatusList.length > 0) {
      params.feeCheckStatuses = this.feeCheckStatusList
        .filter((item: any) => item.checked)
        .map((item: any) => item.value)
        .join(',');
    }
    if (this.feeConfirmStatusList && this.feeConfirmStatusList.length > 0) {
      params.feeConfirmStatuses = this.feeConfirmStatusList
        .filter((item: any) => item.checked)
        .map((item: any) => item.value)
        .join(',');
    }
    if (params.feeCheckTime && params.feeCheckTime.length === 2) {
      params.feeCheckStartDate = params.feeCheckTime[0];
      params.feeCheckEndDate = params.feeCheckTime[1];
    } else {
      params.feeCheckStartDate = null;
      params.feeCheckEndDate = null;
    }
    this.searchFormData.emit(params);
  }

  initOptionList() {
    this.anyfixModuleService.getWorkType().subscribe((res: any) => {
      this.optionList.workTypeOption = res.data;
    });
    this.anyfixModuleService.getWorkSourceOption().subscribe((res: any) => {
      this.optionList.workSourceOption = res.data;
    });
    this.anyfixModuleService.getDemanderOption().subscribe((res: any) => {
      this.optionList.demanderOption = res.data;
    });
    this.anyfixModuleService.getCustomOption().subscribe((res: any) => {
      this.optionList.customOption = res.data;
    });
    this.anyfixModuleService.getAreaInfoOption().subscribe((res: any) => {
      this.optionList.areaInfoOption = res.data;
    });
    this.anyfixModuleService.getProvinceOption().subscribe((res: any) => {
      this.optionList.provinceOption = res.data;
    });
  }

  initStatusCheckbox() {
    const checkStatusArray: number[] = [];
    if (this.params && this.params.feeCheckStatuses !== undefined) {
      const array: any[] = this.params.feeCheckStatuses.toString().split(',');
      array.forEach(status => {
        checkStatusArray.push(toNumber(status, 0));
      });
    } else {
      checkStatusArray.push(this.workCheckService.TO_CHECK);
      checkStatusArray.push(this.workCheckService.CHECK_REFUSE);
    }
    const confirmStatusArray: number[] = [];
    if (this.params && this.params.feeConfirmStatuses !== undefined) {
      const array: any[] = this.params.feeConfirmStatuses.toString().split(',');
      array.forEach(status => {
        confirmStatusArray.push(toNumber(status, 0));
      });
    } else {
      confirmStatusArray.push(this.workCheckService.CONFIRM_REFUSE);
    }
    this.workCheckService.FEE_CHECK_STATUS_LIST.forEach(item => {
      if (checkStatusArray.includes(item.value)) {
        this.feeCheckStatusList.push({label: item.label, value: item.value, checked: true});
      } else {
        this.feeCheckStatusList.push({label: item.label, value: item.value, checked: false});
      }
    });
    this.workCheckService.FEE_CONFIRM_STATUS_LIST.forEach(item => {
      if (confirmStatusArray.includes(item.value)) {
        this.feeConfirmStatusList.push({label: item.label, value: item.value, checked: true});
      } else {
        this.feeConfirmStatusList.push({label: item.label, value: item.value, checked: false});
      }
    });
  }

  getBranchOption(event) {
    this.httpClient
      .post('/api/anyfix/service-branch/match', {
        serviceCorp: this.userService.currentCorp.corpId,
        branchName: event
      })
      .subscribe((res: any) => {
        this.branchOption = res.data;
      });
  }

  matchFeeCheckUser(userName?: string) {
    this.listUser(userName).then((userList: any[]) => {
      this.feeCheckUserList = userList;
    });
  }

  private listUser(userName?: string): Promise<any> {
    return new Promise((resolve, reject) => {
      const payload = {
        corpId: this.userService.currentCorp.corpId,
        matchFilter: userName
      };
      this.httpClient.post('/api/uas/corp-user/match', payload)
        .pipe(
          finalize(() => {
            this.cdf.markForCheck();
          })
        )
        .subscribe((res: Result) => {
          const userList = res.data;
          resolve(userList);
        }, e => {
          throw e;
        });
    });
  }

  /**
   * 审核状态单选
   */
  updateFinishCheckStatusSingleChecked() {
    if (typeof this.feeCheckStatusList === 'object' && this.feeCheckStatusList !== null) {
      if (this.feeCheckStatusList.every(item => !item.checked)) {
        Promise.resolve(null).then(() => {
          this.feeCheckStatusFilter.checkedAll = false;
          this.feeCheckStatusFilter.indeterminate = false;
        });
      } else if (this.feeCheckStatusList.every(item => item.checked)) {
        Promise.resolve(null).then(() => {
          this.feeCheckStatusFilter.checkedAll = true;
          this.feeCheckStatusFilter.indeterminate = false;
        });
      } else {
        Promise.resolve(null).then(() => {
          this.feeCheckStatusFilter.checkedAll = false;
          this.feeCheckStatusFilter.indeterminate = true;
        });
      }
    }
  }

  /**
   * 确认状态单选
   */
  updateFinishConfirmStatusSingleChecked() {
    if (typeof this.feeConfirmStatusList === 'object' && this.feeConfirmStatusList !== null) {
      if (this.feeConfirmStatusList.every(item => !item.checked)) {
        Promise.resolve(null).then(() => {
          this.feeConfirmStatusFilter.checkedAll = false;
          this.feeConfirmStatusFilter.indeterminate = false;
        });
      } else if (this.feeConfirmStatusList.every(item => item.checked)) {
        Promise.resolve(null).then(() => {
          this.feeConfirmStatusFilter.checkedAll = true;
          this.feeConfirmStatusFilter.indeterminate = false;
        });
      } else {
        Promise.resolve(null).then(() => {
          this.feeConfirmStatusFilter.checkedAll = false;
          this.feeConfirmStatusFilter.indeterminate = true;
        });
      }
    }
  }

  /**
   * 委托商改变
   */
  changeDemanderCorp(event) {
    if (!event) {
      return;
    }
    this.anyfixModuleService.getDeviceSmallClassOptionByDemanderCorp(event).subscribe((res: any) => {
      this.optionList.deviceSmallClassOption = res.data;
    });
    this.anyfixModuleService.getDeviceModelOptionByDemanderCorp(event).subscribe((res: any) => {
      this.optionList.deviceModelOption = res.data;
    });
    this.anyfixModuleService.getDeviceBrandOptionByDemanderCorp(event).subscribe((res: any) => {
      this.optionList.deviceBrandOption = res.data;
    });
  }

  // 工程师更换
  engineerChange(engineer) {
    if (this.searchForm.value.serviceBranches || engineer) {
      this.branchChange(this.searchForm.value.serviceBranches, engineer);
    } else {
      this.optionList.engineerList = [];
    }
  }

  // 服务网点更换
  branchChange(branch, engineer?) {
    if (branch !== undefined) {
      this.optionList.engineerList = [];
      if (!engineer) {
        engineer = this.searchForm.value.engineer;
      }
      if (!branch && !engineer) {
        return;
      }
      if (!branch) {
        branch = 0;
      }
      this.anyfixModuleService.getEngineerListOption(branch, engineer)
        .subscribe((res: any) => {
          this.optionList.engineerList = res.data;
        });
    }
  }

  areaChange(event) {
    if (event === null) {
      this.searchForm.controls.district.setValue(null);
      return;
    }
    if (event !== undefined) {
      if (event.length > 2) {
        this.searchForm.controls.district.setValue(event[2]);
      } else if (event.length > 1) {
        this.searchForm.controls.district.setValue(event[1]);
      } else if (event.length === 1) {
        this.searchForm.controls.district.setValue(event[0]);
      }
    }
  }

}
