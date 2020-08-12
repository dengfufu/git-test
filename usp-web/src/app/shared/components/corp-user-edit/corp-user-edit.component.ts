import {ChangeDetectorRef, Input, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {ZonValidators} from '@util/zon-validators';


@Component({
  selector: 'app-corp-user-edit',
  templateUrl: 'corp-user-edit.component.html'
})
export class CorpUserEditComponent implements OnInit {

  @Input() corpUser;

  validateForm: FormGroup;

  roleOptionLoading = false;
  roleOptions = [];

  spinning = false;
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private userService: UserService,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      corpId: [null, [Validators.required]],
      userId: [null, [Validators.required]],
      userName: [null, [ZonValidators.required('员工姓名'), ZonValidators.maxLength(20), ZonValidators.notEmptyString()]],
      mobile: [null, [Validators.required, Validators.pattern('^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|' +
        '([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$')]],
      deviceBranchIdList: [],
      serviceBranchIdList: [],
      roleIdList: [],
      account: [null, [ZonValidators.maxLength(20)]]
    });
  }

  ngOnInit(): void {
    this.matchRole();
    this.validateForm.controls.userName.setValue(this.corpUser.userName);
    this.validateForm.controls.mobile.setValue(this.corpUser.mobile);
    this.validateForm.controls.corpId.setValue(this.corpUser.corpId);
    this.validateForm.controls.userId.setValue(this.corpUser.userId);
    this.validateForm.controls.roleIdList.setValue(this.corpUser.roleIdList);
    this.validateForm.controls.account.setValue(this.corpUser.account);
  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    this.spinning = true;
    this.httpClient
      .post('/api/uas/corp-user/update',
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
   * 模糊查询角色
   * @param roleName 角色名称
   */
  matchRole(roleName?) {
    const params: any = {
      matchFilter: roleName,
      corpId: this.corpUser.corpId,
      enabled: 'Y'
    };
    this.httpClient
      .post('/api/uas/sys-role/match', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.roleOptions = res.data || [];
      });
  }
}
