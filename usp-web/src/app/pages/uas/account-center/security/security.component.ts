import {Component, OnInit} from '@angular/core';
import {UserService} from '@core/user/user.service';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {HttpClient, HttpParams} from '@angular/common/http';
import {NzMessageService} from 'ng-zorro-antd';
import {finalize} from 'rxjs/operators';
import {Result} from '@core/interceptor/result';

/**
 * 安全设置
 */
@Component({
  selector: 'app-security',
  templateUrl: 'security.component.html'
})
export class SecurityComponent implements OnInit {
  isOkLoading = false;

  isChangePasswordVisible = false;
  changePasswordForm: FormGroup;

  isChangeMobileVisible = false;
  changeMobileForm: FormGroup;
  count = 0;
  isCaptchaLoading: boolean;
  interval$: any;

  match = false;
  corpId = this.userService.currentCorp.corpId;
  isExistPassword = false;
  isSmsLoading: any;
  mobile = '';
  changPasswordSpinning = false;
  title = '';
  constructor(public userService: UserService,
              private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private nzMessageService: NzMessageService) {
    this.changePasswordForm = formBuilder.group({
      oldPassword: [''],
      newPassword: ['', [Validators.required,
        Validators.pattern(/^([A-Za-z0-9_\~,=;:!@#\$%\^&\*\-\|\.\(\)\[\]\{\}<>\?\\\/\'\+\"]{6,20})$/)]],
      confirm: ['', [Validators.required, this.confirmEqual]],
      smsCode:['']
    });

    this.changeMobileForm = formBuilder.group({
      newMobile: ['', Validators.required],
      smsCode: ['', [Validators.required]]
    });
  }

  confirmEqual(control: FormControl) {
    if (!control || !control.parent) {
      return null;
    }
    if (control.value !== control.parent.get('newPassword').value) {
      return {match: true};
    }
    return null;
  }

  ngOnInit() {
    this.userService.initUserInfo().then(() => {
      this.isExistPassword = this.userService.userInfo.existPassword;
    });
  }

  modalVisible(type: 'mobile' | 'password') {
    if (type === 'mobile') {
      this.isChangeMobileVisible = true;
    } else if (type === 'password') {
      this.changePasswordForm.reset();
      this.openChangePassword();
      this.isChangePasswordVisible = true;
    }
  }

  get smsCode() {
    return this.changePasswordForm.controls.smsCode;
  }

  handleCancel(type: 'mobile' | 'password') {
    if (type === 'mobile') {
      this.isChangeMobileVisible = false;
    } else if (type === 'password') {
      this.isChangePasswordVisible = false;
    }
  }

  openChangePassword() {
    // 未设置密码
    if(!this.isExistPassword) {
      this.title = '设置密码';
      this.changePasswordForm.controls.smsCode.setValidators([Validators.required, Validators.pattern(/^\d{6}$/)]);
      this.changePasswordForm.controls.oldPassword.setValidators(null);
      this.changePasswordForm.controls.smsCode.updateValueAndValidity();
      this.changePasswordForm.controls.oldPassword.updateValueAndValidity();
    } else {
      this.changePasswordForm.controls.oldPassword.setValidators(Validators.required);
      this.changePasswordForm.controls.smsCode.setValidators(null);
      this.title = '修改密码';
      this.changePasswordForm.controls.oldPassword.updateValueAndValidity();
      this.changePasswordForm.controls.smsCode.updateValueAndValidity();
    }

  }

  // 根据当前企业id查询所有员工
  queryUser(newMobile) {
    this.httpClient
      .post('/api/uas/corp-user/match', {corpId: this.corpId, mobile: newMobile})
      .pipe(
      )
      .subscribe((res: any) => {
        if (res.code === 0) {
          this.match = true;
        }
      });
  }

  handleOk(type: 'mobile' | 'password') {
    this.isOkLoading = true;
    if (type === 'mobile') {
      // 判断是否是企业其他员工手机号
      this.queryUser(this.changeMobileForm.value.newMobile);
      if (this.match) {
        this.nzMessageService.error('新手机号不能是本企业其他员工的手机号，请重新填写！');
      }
      /*// 修改绑定手机
      this.isChangeMobileVisible = true;
      this.httpClient.post('/api/uas/account/updateMobile', {
        mobile: this.changeMobileForm.value.newMobile,
        smsCode: this.changeMobileForm.value.smsCode
      }).pipe(finalize(() => this.isOkLoading = false)).subscribe(() => {
        this.nzMessageService.success('修改成功');
        this.isChangePasswordVisible = false;
        this.userService.initUserInfo().then();
      });*/
    } else if (type === 'password') {
      // 修改密码
      this.isChangePasswordVisible = true;
      this.isExistPassword ? this.submitWithPassword() : this.submitWithoutPassword();

    }
  }

  submitWithPassword() {
    this.httpClient.post('/api/uas/account/password/change', {
      newPassword: this.changePasswordForm.value.newPassword,
      oldPassword: this.changePasswordForm.value.oldPassword
    }).pipe(finalize(() => this.isOkLoading = false)).subscribe(() => {
      this.nzMessageService.success('修改成功');
      this.isChangePasswordVisible = false;
      this.userService.initUserInfo().then();
      this.isExistPassword = true;
    });
  }

  /**
   * 获取短信验证码
   */
  getCaptcha(type) {
    this.isCaptchaLoading = true;
    this.mobile = type === 'password'? this.userService.userInfo.mobile : this.changeMobileForm.value.newMobile;
    const payload = new HttpParams()
      .set('mobile', this.mobile);
    this.httpClient.post('/api/auth/validate/sms-code?_allow_anonymous=true', payload)
      .pipe(finalize(() => this.isCaptchaLoading = false))
      .subscribe(() => {
        this.count = 59;
        this.interval$ = setInterval(() => {
          this.count -= 1;
          if (this.count <= 0) {
            clearInterval(this.interval$);
          }
        }, 1000);
      });
  }

  submitWithoutPassword() {

    Object.keys(this.changePasswordForm.controls).forEach(key => {
      this.changePasswordForm.controls[key].markAsDirty();
      this.changePasswordForm.controls[key].updateValueAndValidity();
    });
    if (this.changePasswordForm.invalid) {
      return;
    }
    this.httpClient.post('/api/uas/account/password/changeBySms?_allow_anonymous=true', {
      mobile: this.mobile,
      smsCode: this.smsCode.value,
      newPassword: this.changePasswordForm.value.newPassword,
    }).pipe(finalize(() => this.isOkLoading = false))
      .subscribe((res: Result) => {
        this.nzMessageService.success('设置密码成功');
        this.isChangePasswordVisible = false;
        this.isExistPassword = true;
    });
  }
}
