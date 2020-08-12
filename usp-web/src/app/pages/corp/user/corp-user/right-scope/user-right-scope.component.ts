import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {NzModalRef, NzModalService} from 'ng-zorro-antd';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {UserService} from '@core/user/user.service';
import {RightScopeService} from '../../right-scope.service';

@Component({
  selector: 'app-corp-user-right-scope',
  templateUrl: 'user-right-scope.component.html',
  styleUrls: ['user-right-scope.component.less']
})
export class UserRightScopeComponent implements OnInit {

  @Input() rightId: any;
  @Input() roleRightScopeList: any[];

  // 表单
  form: FormGroup;

  orgListMap: {[key: number]: any[]} = {};
  rightScopeList = this.rightScopeService.rightScopeTypeList;

  spinning = false;

  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private modalService: NzModalService,
              private modal: NzModalRef,
              private userService: UserService,
              private rightScopeService: RightScopeService) {
    this.form = this.formBuilder.group({
      rightId: []
    });
  }

  ngOnInit() {
    this.form.patchValue({
      rightId: this.rightId
    });
    if (this.roleRightScopeList && this.roleRightScopeList.length > 0) {
      this.roleRightScopeList.forEach((item: any) => {
        this.form.addControl('hasAllScope-' + item.scopeType,
          new FormControl(item.hasAllScope === 'Y', []));
        this.form.addControl('hasOwnScope-' + item.scopeType,
          new FormControl(item.hasOwnScope === 'Y', []));
        this.form.addControl('hasOwnLowerScope-' + item.scopeType,
          new FormControl(item.hasOwnLowerScope === 'Y', []));
        this.form.addControl('orgIdList-' + item.scopeType,
          new FormControl(item.orgIdList, []));
        this.form.addControl('containLowerOrgId-' + item.scopeType,
          new FormControl(item.containLowerOrgId === 'Y', []));
      });
    }
    this.rightScopeList.forEach((item: any) => {
      if (!this.form.contains('hasAllScope-' + item.code)) {
        this.form.addControl('hasAllScope-' + item.code, new FormControl(null, []));
      }
      if (!this.form.contains('hasOwnScope-' + item.code)) {
        this.form.addControl('hasOwnScope-' + item.code, new FormControl(null, []));
      }
      if (!this.form.contains('hasOwnLowerScope-' + item.code)) {
        this.form.addControl('hasOwnLowerScope-' + item.code, new FormControl(null, []));
      }
      if (!this.form.contains('orgIdList-' + item.code)) {
        this.form.addControl('orgIdList-' + item.code, new FormControl(null, []));
      }
      if (!this.form.contains('containLowerOrgId-' + item.code)) {
        this.form.addControl('containLowerOrgId-' + item.code, new FormControl(null, []));
      }
      if (item.code === this.rightScopeService.SERVICE_BRANCH) {
        this.listServiceBranch().then((branchList: any[]) => {
          this.orgListMap[item.code] = branchList;
        });
      } else if (item.code === this.rightScopeService.PROVINCE) {
        this.listProvince().then((provinceList: any[]) => {
          this.orgListMap[item.code] = provinceList;
        });
      } else if (item.code === this.rightScopeService.DEMANDER) {
        this.listDemander().then((demanderList: any[]) => {
          this.orgListMap[item.code] = demanderList;
        });
      }
    });
  }

  /**
   * 提交表单
   */
  submitForm(): void {
    const list: any[] = [];
    this.rightScopeList.forEach((item: any) => {
      const value: any = {};
      value.rightId = this.rightId;
      value.scopeType = item.code;
      value.hasAllScope = this.form.get('hasAllScope-' + item.code).value ? 'Y' : 'N';
      value.hasOwnScope = this.form.get('hasOwnScope-' + item.code).value ? 'Y' : 'N';
      value.hasOwnLowerScope = this.form.get('hasOwnLowerScope-' + item.code).value ? 'Y' : 'N';
      value.orgIdList = this.form.get('orgIdList-' + item.code).value;
      value.orgNames = this.findOrgNames(item.code, value.orgIdList);
      value.containLowerOrgId = this.form.get('containLowerOrgId-' + item.code).value ? 'Y' : 'N';
      const scopeNames = this.rightScopeService.findScopeNames(value);
      value.scopeNames = scopeNames;
      list.push(value);
    });
    this.modal.destroy(list);
  }

  findOrgNames(code: number, idList: any[]) {
    let orgNames = '';
    if (!idList) {
      return orgNames;
    }
    this.orgListMap[code].forEach(item => {
      if (idList.includes(item.value)) {
        orgNames = orgNames.length > 0 ? orgNames + ',' + item.label : item.label;
      }
    });
    return orgNames;
  }

  /**
   * 模糊查询服务网点
   */
  private listServiceBranch(name?: string): Promise<any> {
    const params = {
      serviceCorp: this.userService.currentCorp.corpId
    };
    return new Promise((resolve, reject) => {
      this.httpClient
        .post('/api/anyfix/service-branch/list', params)
        .pipe(
          finalize(() => {
            this.cdf.markForCheck();
          })
        )
        .subscribe((res: any) => {
          const list = res.data || [];
          const branchList = [];
          list.forEach(item => {
            branchList.push({
              value: item.branchId,
              label: item.branchName
            });
          });
          resolve(branchList);
        }, e => {
          throw e;
        });
    });
  }

  /**
   * 省份列表
   */
  private listProvince(): Promise<any> {
    return new Promise((resolve, reject) => {
      this.httpClient
        .get('/api/uas/area/province/list')
        .pipe(
          finalize(() => {
            this.cdf.markForCheck();
          })
        )
        .subscribe((res: any) => {
          const list = res.data || [];
          const provinceList = [];
          list.forEach(item => {
            provinceList.push({
              value: item.value,
              label: item.label
            });
          });
          resolve(provinceList);
        }, e => {
          throw e;
        });
    });
  }

  /**
   * 委托商列表
   */
  private listDemander(name?: string): Promise<any> {
    const params = {
      serviceCorp: this.userService.currentCorp.corpId,
      demanderCorpName: name
    };
    return new Promise((resolve, reject) => {
      this.httpClient
        .post('/api/anyfix/demander-service/demander/list', params)
        .pipe(
          finalize(() => {
            this.cdf.markForCheck();
          })
        )
        .subscribe((res: any) => {
          const list = res.data || [];
          const demanderList = [];
          list.forEach(item => {
            demanderList.push({
              value: item.demanderCorp,
              label: item.demanderCorpName
            });
          });
          resolve(demanderList);
        }, e => {
          throw e;
        });
    });
  }

  /**
   * 关闭页面
   */
  destroyModal(): void {
    this.modal.destroy();
  }
}
