import {Input, Component, OnInit, ChangeDetectorRef, Output, EventEmitter} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {AnyfixModuleService} from '@core/service/anyfix-module.service';
import { ZorroUtils } from '@util/zorro-utils';
import {HttpClient} from '@angular/common/http';
import {UserService} from '@core/user/user.service';
import {WorkCheckService} from '../../work-check.service';


@Component({
  selector: 'work-history-filter',
  templateUrl: 'work-history-filter.component.html',
  styleUrls: ['work-history-filter.component.less']
})
export class WorkHistoryFilterComponent implements OnInit {

  @Input() allChecked;
  @Input() checkOptionsOne;
  @Input() checkStatusList;
  @Input() workStatusNow;
  @Input() yesterdayFinished;
  @Input() todayFinished;
  @Input() params;
  @Input() searchForm;
  // 审核确认状态
  @Input() checkConfirmObject;

  @Output() searchFormData: EventEmitter<{key: string}[]>
    = new EventEmitter<{key: string}[]>();

  optionList: any = {};
  branchOption : any = [];

  ZorroUtils = ZorroUtils;
  // 判断当前企业是否是服务商
  isServiceCorp: boolean = this.userService.currentCorp.serviceProvider === 'Y';
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              public userService: UserService,
              public workCheckService: WorkCheckService,
              private anyfixModuleService: AnyfixModuleService
              ) {
  }


  ngOnInit(): void {
    this.initOptionList();
    this.getBranchOption('');
  }

  submit() {
    this.searchFormData.emit(this.searchForm.value);
  }

  initOptionList() {
    this.anyfixModuleService.getWorkType().subscribe((res: any) => {
      this.optionList.workTypeOption = res.data;
    });
    this.anyfixModuleService.getWorkSourceOption().subscribe((res: any) => {
      this.optionList.workSourceOption = res.data;
    });
    this.anyfixModuleService.getDemanderOption().subscribe((res:any)=>{
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

  getBranchOption(event) {
    this.httpClient
      .post('/api/anyfix/service-branch/match', {
        serviceCorp: this.userService.currentCorp.corpId,
        branchName : event
      })
      .subscribe((res:any) => {
        this.branchOption = res.data;
      });
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

  updateAllChecked() {
    if (this.allChecked) {
      this.checkOptionsOne = this.checkOptionsOne.map(item => {
        return {
          ...item,
          checked: true
        };
      });
    } else {
      this.checkOptionsOne = this.checkOptionsOne.map(item => {
        return {
          ...item,
          checked: false
        };
      });
    }
  }

  updateSingleChecked() {
    if (typeof this.checkOptionsOne === 'object' && this.checkOptionsOne !== null) {
      // 异步更新
      if (this.checkOptionsOne.every(item => item.checked)) {
        Promise.resolve(null).then( ()=> {this.allChecked = true;});
      } else {
        Promise.resolve(null).then( ()=> {this.allChecked = false;});
      }
    }
  }

  areaChange(event) {
    if (event === null) {
      this.searchForm.controls.district.setValue(null);
      return;
    }
    if (event !== undefined){
      if(event.length > 2) {
        this.searchForm.controls.district.setValue(event[2]);
      }else if(event.length > 1) {
        this.searchForm.controls.district.setValue(event[1]);
      } else if(event.length === 1) {
        this.searchForm.controls.district.setValue(event[0]);
      }
    }
  }

}
