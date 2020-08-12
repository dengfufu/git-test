import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {ZonValidators} from '@util/zon-validators';

@Component({
  selector: 'app-corp-user-add',
  templateUrl: 'corp-user-add.component.html'
})
export class CorpUserAddComponent implements OnInit {

  spinning = false;
  validateForm: FormGroup;

  roleOptionLoading = false;
  roleOptions = [];

  corpId = this.userService.currentCorp.corpId;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient,
              private userService: UserService) {
    this.validateForm = this.formBuilder.group({
      corpId: [this.corpId, [Validators.required]],
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
  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    this.spinning = true;
    this.httpClient
      .post('/api/uas/corp-user/add',
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
    const params = {
      matchFilter: roleName,
      corpId: this.corpId,
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
