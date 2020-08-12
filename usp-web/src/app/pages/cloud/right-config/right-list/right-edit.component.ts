import {ChangeDetectorRef, Input, Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {ZonValidators} from '@util/zon-validators';


@Component({
  selector: 'app-right-edit',
  templateUrl: 'right-edit.component.html'
})
export class RightEditComponent implements OnInit {

  @Input() sysRight;

  validateForm: FormGroup;
  spinning = false;

  rightScopeList: any[] = [];
  corpList: any[] = [];

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient,
              private nzMessageService: NzMessageService) {
    this.validateForm = this.formBuilder.group({
      rightId: [null, [ZonValidators.required()]],
      rightName: [null, [ZonValidators.required(), ZonValidators.maxLength(50), ZonValidators.notEmptyString()]],
      rightCode: [null, [ZonValidators.maxLength(20)]],
      hasScope: [null, [ZonValidators.required()]],
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
    this.validateForm.patchValue({
      rightId: this.sysRight.key,
      rightName: this.sysRight.title,
      rightCode: this.sysRight.rightCode,
      scopeTypeList: this.sysRight.scopeTypeList,
      hasScope: this.sysRight.hasScope === 'Y',
      serviceDemander: this.sysRight.serviceDemander === 'Y',
      serviceDemanderCommon: this.sysRight.serviceDemanderCommon === 'Y',
      serviceProvider: this.sysRight.serviceProvider === 'Y',
      serviceProviderCommon: this.sysRight.serviceProviderCommon === 'Y',
      deviceUser: this.sysRight.deviceUser === 'Y',
      deviceUserCommon: this.sysRight.deviceUserCommon === 'Y',
      cloudManager: this.sysRight.cloudManager === 'Y',
      cloudManagerCommon: this.sysRight.cloudManagerCommon === 'Y',
      hasExtraCorp: this.sysRight.hasExtraCorp === 'Y',
    });
    if (this.sysRight.extraCorpList && this.sysRight.extraCorpList.length > 0) {
      this.sysRight.extraCorpList.forEach((extraCorp: any) => {
        this.extraCorpListForm.push(
          this.formBuilder.group({
            corpId: [extraCorp.corpId, [ZonValidators.required()]],
            common: [extraCorp.common === 'Y' ? true : false, [ZonValidators.required()]]
          })
        );
      });
    }
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
      this.httpClient.post('/api/uas/sys-right/update', param)
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
