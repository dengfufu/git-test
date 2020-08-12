import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {HttpClient, HttpParams} from '@angular/common/http';
import {NzMessageService} from 'ng-zorro-antd';
import {finalize} from 'rxjs/operators';
import {environment} from '@env/environment';
import {UserService} from '@core/user/user.service';
import {Result} from '@core/interceptor/result';


@Component({
  selector: 'app-register',
  templateUrl: 'register.component.html',
  styleUrls: ['register.component.less']
})
export class RegisterComponent implements OnInit, OnDestroy {
  
  constructor(public formBuilder: FormBuilder,
              public router: Router,
              public http: HttpClient,
              private userService: UserService,
              public messageService: NzMessageService) {
    this.form = formBuilder.group({
      // email: [null, [Validators.required, Validators.email]],
      password: [null, [Validators.required, Validators.minLength(6), RegisterComponent.checkPassword.bind(this)]],
      confirm: [null, [Validators.required, Validators.minLength(6), RegisterComponent.passwordEquar]],
      mobilePrefix: ['+86'],
      mobile: [null, [Validators.required, Validators.pattern(/^1\d{10}$/)],[this.checkDuplicateRegister]],
      smsCode: [null, [Validators.required, Validators.pattern(/^\d{6}$/)]],
      useragreement:[false,[Validators.requiredTrue]],
    });
  }


  get password() {
    return this.form.controls.password;
  }

  get confirm() {
    return this.form.controls.confirm;
  }

  get mobile() {
    return this.form.controls.mobile;
  }

  // get email() {
  //   //   return this.form.controls.email;
  //   // }

  get smsCode() {
    return this.form.controls.smsCode;
  }

  get useragreement() {
    return this.form.controls.useragreement;
  }

  url = environment.server_url;

  form: FormGroup;
  error = '';
  type = 0;
  visible = false;
  status = 'pool';
  progress = 0;
  passwordProgressMap = {
    ok: 'success',
    pass: 'normal',
    pool: 'exception',
  };
  count = 0;
  interval$: any;
  isLoading: boolean;
  userProtocol: any;

  static checkPassword(control: FormControl) {
    if (!control) {
      return null;
    }
    const self: any = this;
    self.visible = !!control.value;
    if (control.value && control.value.length > 9) {
      self.status = 'ok';
    } else if (control.value && control.value.length > 5) {
      self.status = 'pass';
    } else {
      self.status = 'pool';
    }

    if (self.visible) {
      self.progress = control.value.length * 10 > 100 ? 100 : control.value.length * 10;
    }
  }

  static passwordEquar(control: FormControl) {
    if (!control || !control.parent) {
      return null;
    }
    // tslint:disable-next-line:no-non-null-assertion
    if (control && control.value !== control.parent.get('password')!.value) {
      return {equar: true};
    }
    return null;
  }

  // 验证该手机号是否已经被注册
  checkDuplicateRegister = (control: FormControl) => {
    return new Promise((resolve) => {
      return this.http.post('/api/uas/register/checkIsRegister?_allow_anonymous=true', {mobile: this.mobile.value})
        .subscribe((res: any) => {
          if (res.data) {
            resolve({duplicate: true});
          } else {
            resolve(null);
          }
        });
    });
  }

  ngOnInit() {
    this.getUserProtocol();
  }

  getUserProtocol() {
    this.http.get('/api/uas/protocol/list?_allow_anonymous=true').subscribe((res: any) => {
      this.userProtocol = res.data.filter(item => item.id===1)[0];
    });
  }

  getCaptcha() {
    if (this.mobile.invalid) {
      this.mobile.markAsDirty({onlySelf: true});
      this.mobile.updateValueAndValidity({onlySelf: true});
      return;
    }
    const payload = new HttpParams().set('mobile', this.mobile.value);
    this.http.post('/api/auth/validate/sms-code?_allow_anonymous=true', payload)
      .pipe(
        finalize(() => {
        })
      )
      .subscribe((res: any) => {
        this.countTime();
      });
  }

  countTime() {
    this.count = 59;
    this.interval$ = setInterval(() => {
      this.count -= 1;
      if (this.count <= 0) {
        clearInterval(this.interval$);
      }
    }, 1000);
  }

  // #endregion
  submit() {
    this.registerByMobile();

  }

  registerByMobile() {
    this.error = '';
    Object.keys(this.form.controls).forEach(key => {
      this.form.controls[key].markAsDirty();
      this.form.controls[key].updateValueAndValidity();
    });
    if (this.form.invalid) {
      return;
    }
    this.http
      .post('/api/uas/register/mobile?_allow_anonymous=true', {
        mobile: this.mobile.value,
        smsCode: this.smsCode.value,
        password: this.password.value,
        // email: this.email.value,
        publicKey: 100,
        clientId: environment.client_id
      })
      .pipe(
        finalize(() => {
        })
      )
      .subscribe((res: Result) => {
        // 登录
        if(res.code === 0) {
          this.messageService.info('注册成功，请重新登录');
          this.userService.userInfo.mobile = this.mobile.value;
          this.router.navigateByUrl('/user/login');
        } else {
          this.messageService.error('注册失败: ' + res.msg)
        }
      });
  }

  ngOnDestroy(): void {
    if (this.interval$) {
      clearInterval(this.interval$);
    }
  }
}
