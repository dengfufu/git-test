import {Component, OnDestroy, Inject, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {ITokenService, DA_SERVICE_TOKEN} from '@delon/auth';
import {HttpClient, HttpParams} from '@angular/common/http';
import {StartupService} from '@core/startup/startup.service';
import {environment} from '@env/environment';
import {UserService} from '@core/user/user.service';
import {Result} from '@core/interceptor/result';
import {DomSanitizer} from '@angular/platform-browser';

@Component({
  selector: 'app-login',
  templateUrl: 'login.component.html',
  styleUrls: ['login.component.less']
})
export class LoginComponent implements OnInit, OnDestroy {

  form: FormGroup;
  type = 0;
  isLoading = false;
  mobiles = [];
  lastMobile: string;

  // #region get captcha
  count = 0;
  interval$: any;
  // #endregion

  captchaBase64: any;

  constructor(formBuilder: FormBuilder,
              modalSrv: NzModalService,
              private router: Router,
              private startupService: StartupService,
              private sanitizer: DomSanitizer,
              @Inject(DA_SERVICE_TOKEN) private tokenService: ITokenService,
              public httpClient: HttpClient,
              private userService: UserService,
              public messageService: NzMessageService) {

    this.form = formBuilder.group({
      mobile: [null, [Validators.required, Validators.pattern(/^1\d{10}$/)]],
      password: [null, Validators.required],
      captchaId: [null, [Validators.required]],
      captchaCode: [null, [Validators.required]],
      captcha: [null, [Validators.required]]
    });

    if (this.userService.userInfo.mobile) {
      this.form.controls.mobile.setValue(userService.userInfo.mobile);
      this.userService.userInfo.mobile = null;
    }
    this.lastMobile = localStorage.getItem('mobile');
    if (this.lastMobile) {
      this.form.patchValue({
        mobile: this.lastMobile
      });
    }
    const mobiles = localStorage.getItem('mobiles');
    if (mobiles && mobiles !== undefined && mobiles !== null) {
      this.mobiles = JSON.parse(mobiles);
    }
  }

  ngOnInit(): void {
    this.getImageCaptcha();
  }

  switch(ret: any) {
    this.type = ret.index;
  }

  submit() {
    if (this.type === 0) {
      this.mobile.markAsDirty();
      this.mobile.updateValueAndValidity();
      this.password.markAsDirty();
      this.password.updateValueAndValidity();
      this.captchaCode.markAsDirty();
      this.captchaCode.updateValueAndValidity();
      if (this.mobile.invalid || this.password.invalid || this.captchaCode.invalid) {
        return;
      }
      this.loginByPassWord();
    } else {
      this.mobile.markAsDirty();
      this.mobile.updateValueAndValidity();
      this.captcha.markAsDirty();
      this.captcha.updateValueAndValidity();
      if (this.mobile.invalid || this.captcha.invalid) {
        return;
      }
      this.loginBySmsCode(this.mobile.value, this.captcha.value);
    }
  }


  loginByPassWord() {
    // 默认配置中对所有HTTP请求都会强制 [校验](https://ng-alain.com/auth/getting-started) 用户 Token
    // 然一般来说登录请求不需要校验，因此可以在请求URL加上：`/login?_allow_anonymous=true` 表示不触发用户 Token 校验
    this.isLoading = true;
    const payload = new HttpParams()
      .set('mobile', this.mobile.value)
      .set('password', this.password.value)
      .set('captchaId', this.captchaId.value)
      .set('captchaCode', this.captchaCode.value);
    this.httpClient.post('/api/auth/oauth/mobile/token?_allow_anonymous=true', payload, {
      headers: {
        Authorization: 'Basic ' + window.btoa(`${environment.client_id}:${environment.client_secret}`)
      }
    }).subscribe((res: Result) => {
      if (res && res.code === 0) {
        this.afterLogin(res);
      } else {
        this.isLoading = false;
        this.messageService.error(res.msg);
      }
    });
  }

  loginBySmsCode(mobile: string, smsCode: string) {
    this.isLoading = true;
    const payload = new HttpParams()
      .set('mobile', mobile)
      .set('smsCode', smsCode);
    this.httpClient.post('/api/auth/oauth/sms/token?_allow_anonymous=true', payload, {
      headers: {
        Authorization: 'Basic ' + window.btoa(`${environment.client_id}:${environment.client_secret}`)
      }
    }).subscribe((res: Result) => {
      if (res && res.code === 0) {
        this.afterLogin(res);
      } else {
        this.isLoading = false;
        this.messageService.error(res.msg);
      }
    });
  }

  afterLogin(res) {
    // 保存手机号
    localStorage.setItem('mobile', this.mobile.value);
    if (this.mobiles.length > 5) {
      this.mobiles.pop();
    }
    const index = this.mobiles.indexOf(this.mobile.value);
    if (index >= 0) {
    } else {
      this.mobiles.unshift(this.mobile.value);
    }
    localStorage.setItem('mobiles', JSON.stringify(this.mobiles));

    // 设置用户Token信息
    const token = res.data;
    this.tokenService.set({
      token: token.access_token,
      refresh_token: token.refresh_token,
      expires_in: token.expires_in,
      expires_date: new Date().getTime() + token.expires_in * 1000,
      scope: token.scope
    });
    this.startupService.initData().then(() => {
      if(this.userService.isPayAuditUser()) {
        this.router.navigateByUrl(this.userService.getPayAuditMenu()[0].path)
      } else {
        this.router.navigateByUrl('/');
      }
    }).then(() => {
      this.isLoading = false;
    });
  }

  /**
   * 图形验证码
   */
  getImageCaptcha() {
    this.httpClient.get('/api/auth/validate/imageCaptcha?_allow_anonymous=true').subscribe((res: Result) => {
      if (res && res.code === 0) {
        this.form.controls.captchaId.setValue(res.data.captchaId);
        this.captchaBase64 = this.sanitizer.bypassSecurityTrustResourceUrl(res.data.captchaBase64);
      }
    });
  }

  /**
   * 短信验证码
   */
  getCaptcha() {
    if (this.mobile.invalid) {
      this.mobile.markAsDirty({onlySelf: true});
      this.mobile.updateValueAndValidity({onlySelf: true});
      return;
    }
    const payload = new HttpParams().set('mobile', this.mobile.value);
    this.httpClient.post('/api/auth/validate/sms-code?_allow_anonymous=true', payload).subscribe((res: any) => {
      this.count = 59;
      this.interval$ = setInterval(() => {
        this.count -= 1;
        if (this.count <= 0) {
          clearInterval(this.interval$);
        }
      }, 1000);
    });
  }

  // #region fields
  get password() {
    return this.form.controls.password;
  }

  get mobile() {
    return this.form.controls.mobile;
  }

  get captchaId() {
    return this.form.controls.captchaId;
  }

  get captchaCode() {
    return this.form.controls.captchaCode;
  }

  get captcha() {
    return this.form.controls.captcha;
  }

  // #endregion

  ngOnDestroy(): void {
    if (this.interval$) {
      clearInterval(this.interval$);
    }
  }
}
