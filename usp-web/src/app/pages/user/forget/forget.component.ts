import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {HttpClient, HttpParams} from '@angular/common/http';
import {UserService} from '@core/user/user.service';
import {NzMessageService} from 'ng-zorro-antd';
import {Result} from '@core/interceptor/result';


@Component({
  selector: 'app-forget',
  templateUrl: 'forget.component.html',
  styleUrls: ['forget.component.less']
})
export class ForgetComponent implements OnInit, OnDestroy {

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

  constructor(public formBuilder: FormBuilder,
              public router: Router,
              public http: HttpClient,
              public messageService: NzMessageService) {
    this.form = formBuilder.group({
      mobilePrefix: ['+86'],
      mobile: [null, [Validators.required, Validators.pattern(/^1\d{10}$/)]],
      smsCode: [null, [Validators.required, Validators.pattern(/^\d{6}$/)]],
      newPassword: [null, [Validators.required, Validators.minLength(6), this.checkPassword]],
      confirm: [null, [Validators.required, this.confirmationValidator]],
    });
  }

  checkPassword = (control: FormControl) => {
    if (!control) {
      return null;
    }
    this.visible = !!control.value;
    if (control.value && control.value.length > 9) {
      this.status = 'ok';
    } else if (control.value && control.value.length > 5) {
      this.status = 'pass';
    } else {
      this.status = 'pool';
    }
    if (this.visible) {
      this.progress = control.value.length * 10 > 100 ? 100 : control.value.length * 10;
    }
  };

  updateConfirmValidator(): void {
    Promise.resolve().then(() => this.form.controls.confirm.updateValueAndValidity());
  }

  confirmationValidator = (control: FormControl): { [s: string]: boolean } => {
    if (!control.value) {
      return {required: true};
    } else if (control.value !== this.form.controls.newPassword.value) {
      return {equar: true, error: true};
    }
    return {};
  };

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
    if (this.interval$) {
      clearInterval(this.interval$);
    }
  }

  submit() {
    this.error = '';
    Object.keys(this.form.controls).forEach(key => {
      this.form.controls[key].markAsDirty();
      this.form.controls[key].updateValueAndValidity();
    });
    if (this.form.invalid) {
      return;
    }
    this.http.post('/api/uas/account/password/changeBySms?_allow_anonymous=true', {
      mobile: this.mobile.value,
      smsCode: this.smsCode.value,
      newPassword: this.newPassword.value,
    }).subscribe((res: Result) => {
      if (res && res.code === 0) {
        this.router.navigateByUrl('/user/login');
      }
    });
  }

  getCaptcha() {
    if (this.mobile.invalid) {
      this.mobile.markAsDirty({onlySelf: true});
      this.mobile.updateValueAndValidity({onlySelf: true});
      return;
    }
    const payload = new HttpParams().set('mobile', this.mobile.value);
    this.http.post('/api/auth/validate/sms-code?_allow_anonymous=true', payload).subscribe((res: any) => {
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

  get mobile() {
    return this.form.controls.mobile;
  }

  get smsCode() {
    return this.form.controls.smsCode;
  }

  get newPassword() {
    return this.form.controls.newPassword;
  }

  get confirm() {
    return this.form.controls.confirm;
  }

}
