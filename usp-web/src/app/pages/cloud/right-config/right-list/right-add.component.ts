import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {AppList, AppMap} from '../../../app';
import {ZonValidators} from '@util/zon-validators';

@Component({
  selector: 'app-right-add',
  templateUrl: 'right-add.component.html'
})
export class RightAddComponent implements OnInit {

  @Input() sysRight;

  validateForm: FormGroup;
  appName: any;
  appList = AppList;
  rightScopeList: any[] = [];
  corpList: any[] = [];

  spinning = false;

  constructor(private formBuilder: FormBuilder,
              private modal: NzModalRef,
              private httpClient: HttpClient,
              private nzMessageService: NzMessageService) {
    this.validateForm = this.formBuilder.group({
      parentId: [null, [ZonValidators.required()]],
      rightId: [null, [ZonValidators.required()]],
      rightName: [null, [ZonValidators.required(), ZonValidators.maxLength(50), ZonValidators.notEmptyString()]],
      rightCode: [null, [ZonValidators.maxLength(20)]],
      appId: [null, [ZonValidators.required()]],
      hasScope: [false, [ZonValidators.required()]],
      scopeTypeList: [],
      serviceDemander: [false, [ZonValidators.required()]],
      serviceDemanderCommon: [false, [ZonValidators.required()]],
      serviceProvider: [false, [ZonValidators.required()]],
      serviceProviderCommon: [false, [ZonValidators.required()]],
      deviceUser: [false, [ZonValidators.required()]],
      deviceUserCommon: [false, [ZonValidators.required()]],
      cloudManager: [false, [ZonValidators.required()]],
      cloudManagerCommon: [false, [ZonValidators.required()]],
      hasExtraCorp: [false, [ZonValidators.required()]],
      extraCorpList: this.formBuilder.array([])
    });
  }

  get extraCorpListForm(): FormArray {
    return this.validateForm.get('extraCorpList') as FormArray;
  }

  addExtraCorp() {
    this.extraCorpListForm.push(
      this.formBuilder.group({
        corpId: ['', [ZonValidators.required()]],
        common: [false, [ZonValidators.required()]]
      })
    );
  }

  removeExtraCorp(index: number) {
    this.extraCorpListForm.removeAt(index);
  }

  ngOnInit(): void {
    // 加载范围权限以及租户企业数据
    this.listRightScopeType();
    this.listCorpList();

    // 设置父节点相关内容
    if (this.sysRight) {
      this.validateForm.controls.appId.setValue(this.sysRight.appId);
      this.validateForm.controls.parentId.setValue(this.sysRight.key);
      this.appName = AppMap[this.sysRight.appId];
    } else {
      this.validateForm.controls.parentId.setValue(0);
    }

    // 查询最大id
    this.httpClient.get('/api/uas/sys-right/id/max').subscribe((res: any) => {
      this.validateForm.patchValue({
        rightId: res.data
      });
    });
  }

  /**
   * 权限范围类型列表
   */
  listRightScopeType() {
    this.httpClient.get('/api/uas/sys-right-scope-type/type/list').subscribe((res: any) => {
      this.rightScopeList = res.data;
    });
  }

  /**
   * 所有租户企业
   */
  listCorpList() {
    this.httpClient.post('/api/uas/sys-tenant/list', {}).subscribe((res: any) => {
      this.corpList = res.data;
    });
  }

  submitForm(value: any): void {
    if (this.validateForm.valid) {
      if (this.validateForm.value.hasScope === true) {
        if (!this.validateForm.value.scopeTypeList || this.validateForm.value.scopeTypeList.length === 0) {
          this.nzMessageService.error('请选择范围类型！');
          return;
        }
      }
      if (this.validateForm.value.hasExtraCorp === true) {
        if (this.extraCorpListForm.controls.length === 0) {
          this.nzMessageService.error('请添加额外企业相关信息！');
          return;
        }
      }
      const param = {...this.validateForm.value};
      param.scopeTypeList = param.hasScope ? param.scopeTypeList : [];
      param.hasScope = param.hasScope ? 'Y' : 'N';
      param.serviceDemanderCommon = param.serviceDemander && param.serviceDemanderCommon ? 'Y' : 'N';
      param.serviceDemander = param.serviceDemander ? 'Y' : 'N';
      param.serviceProviderCommon = param.serviceProvider && param.serviceProviderCommon ? 'Y' : 'N';
      param.serviceProvider = param.serviceProvider ? 'Y' : 'N';
      param.deviceUserCommon = param.deviceUser && param.deviceUserCommon ? 'Y' : 'N';
      param.deviceUser = param.deviceUser ? 'Y' : 'N';
      param.cloudManagerCommon = param.cloudManager && param.cloudManagerCommon ? 'Y' : 'N';
      param.cloudManager = param.cloudManager ? 'Y' : 'N';
      param.hasExtraCorp = param.hasExtraCorp ? 'Y' : 'N';
      param.extraCorpList.forEach((extraCorp: any) => {
        extraCorp.common = extraCorp.common ? 'Y' : 'N';
      });
      this.spinning = true;
      this.httpClient.post('/api/uas/sys-right/add', param)
        .pipe(finalize(() => this.spinning = false))
        .subscribe(() => {
          this.modal.destroy('submit');
        });
    } else {
      this.validateForm.markAsDirty();
      this.validateForm.updateValueAndValidity();
    }
  }

  destroyModal(): void {
    this.modal.destroy();
  }
}
