import {
  AfterViewInit,
  ChangeDetectorRef,
  Component, Inject, OnDestroy,
  OnInit,
  TemplateRef,
  ViewChild,
  ViewEncapsulation
} from '@angular/core';
import {MenuDataItem, Settings, SettingsService} from 'pro-layout';
import {MenuData} from '../../../app-menu';
import {NavigationEnd, Router} from '@angular/router';
import {filter, finalize} from 'rxjs/operators';
import {Subscription} from 'rxjs';
import {Result} from '@core/interceptor/result';
import {HttpClient, HttpParams} from '@angular/common/http';
import {UserService} from '@core/user/user.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {environment} from '@env/environment';
import {DA_SERVICE_TOKEN, ITokenService} from '@delon/auth';
import {FormBuilder, FormControl, Validators} from '@angular/forms';


@Component({
  selector: 'app-basic-layout',
  templateUrl: 'basic-layout.component.html',
  styleUrls: ['basic-layout.component.less'],
  encapsulation: ViewEncapsulation.None,
})
export class BasicLayoutComponent implements OnInit, AfterViewInit, OnDestroy{

  @ViewChild('linkIconTemplate', {static: true})
  linkIconTemplate: TemplateRef<void>;

  url = environment.server_url;
  menuData = MenuData;
  footer: any;

  list: Array<any>;
  settings: Settings;
  shouldShowModal = false;
  isOkLoading = false;
  isSmsLoading: any;
  changePasswordForm:any;
  changPasswordSpinning = false;
  mobile = '';
  count = 0;
  isCaptchaLoading: boolean;
  interval$: any;

  // 刷新事件处理
  reloadSubscription: Subscription;

  constructor(private settingService: SettingsService,
              public router: Router,
              @Inject(DA_SERVICE_TOKEN) public tokenService: ITokenService,
              private modalService: NzModalService,
              private formBuilder: FormBuilder,
              public userService: UserService,
              private httpClient: HttpClient,
              private nzMessageService: NzMessageService,
              private cdr: ChangeDetectorRef) {
    this.reloadSubscription = this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((e:NavigationEnd) => {
      if(e.url === '/') {
        // 登录及切换公司，刷新菜单
        this.loadExternalMenu();
      }
    });

    this.mobile = this.userService.userInfo.mobile;
    this.changePasswordForm = formBuilder.group({
      phone: [this.mobile],
      oldPassword: [''],
      newPassword: ['', [Validators.required,
        Validators.pattern(/^([A-Za-z0-9_\~,=;:!@#\$%\^&\*\-\|\.\(\)\[\]\{\}<>\?\\\/\'\+\"]{6,20})$/)]],
      confirm: ['', [Validators.required, this.confirmEqual]],
      smsCode:['',[Validators.required,Validators.pattern(/^\d{6}$/)]]
    });

    // TODO 对测试用户特殊处理
    if(this.userService.isPayAuditUser()) {
      this.menuData = this.userService.getPayAuditMenu();
    }
  }

  ngOnInit(): void {
    this.settings = this.settingService.settings;

    console.log(this.settings);

    this.footer = {
      links: [
        {
          key: 'Ant Design Pro',
          title: 'Ant Design Pro',
          href: 'https://pro.ant.design',
          blankTarget: true,
        },
        {
          key: 'github',
          title: this.linkIconTemplate,
          href: 'https://github.com/CK110/ant-design-pro-angular',
          blankTarget: true,
        },
        {
          key: 'Ant Design',
          title: 'Ant Design',
          href: 'https://ng.ant.design',
          blankTarget: true,
        },
      ],
      copyright: '2019 深圳市紫金支点技术股份有限公司'
    };

    this.checkProtocolAndPassword();
  }

  ngAfterViewInit(): void {
  }

  // 验证两次输入的密码一致
  confirmEqual(control: FormControl) {
    if (!control || !control.parent) {
      return null;
    }
    if (control.value !== control.parent.get('newPassword').value) {
      return {match: true};
    }
    return null;
  }

  loadExternalMenu(){
    // 深拷贝
    const menus = JSON.parse(JSON.stringify(MenuData));
    this.httpClient.get('/api/uas/corp-app/getExternalApps').pipe(
      finalize(() => {
        this.menuData = menus;
        this.cdr.markForCheck();
      })
    ).subscribe((res:Result)=>{
      if(res.code === 0 && res.data) {
        const externalMenus: MenuDataItem[] = this.buildExternalMenus(res.data.menus);
        if(externalMenus.length) {
          // 增加外部菜单
          menus.splice(1,0,...externalMenus);
        }
      }
    });
  }

  buildExternalMenus(menus:any[]){
    const externalMenus:MenuDataItem[] = [];
    menus.forEach(menu=>{
      const item: MenuDataItem = {
        name: menu.name,
        path: menu.url,
        external: true
      };

      if(menu.children && menu.children.length > 0){
        item.icon = 'global';
        item.children = this.buildExternalMenus(menu.children);
      }
      externalMenus.push(item);
    });
    return externalMenus;
  }

  checkProtocolAndPassword() {
    this.httpClient.post('/api/uas/protocol/check-sign', {
      userId: this.userService.userInfo.userId
    }).subscribe((res: any) => {
      let {data : list} = res;
      list = list.filter(item => item.needSign);
      this.list = list;
      console.log(list);
      // 存在未签署的协议
      if (list.length) {
        this.showProtocols(list);
      } else {
        this.checkPassword();
      }
    }, error => {

    });
  }

  showProtocols(list) {
    const str = list.map(item =>
      `<a href="${this.url+item.url}?updateDate=${item.updateDate}" target="_blank">${item.name}</a>`
    ).join('、');
    this.modalService.confirm({
      nzTitle: '温馨提示',
      nzWidth: 550,
      nzContent: `<p>感谢您选择使用畅修服务！我们依据最新法律法规及监管政策要求，更新了相关协议及隐私政策，特此向你推送本提示。</p>
        <p>请您务必仔细阅读${str}</p>
        <p>我们将按法律法规要求，采取相应安全保护措施，尽力保护您的个人信息安全可控。</p>`,
      nzOkText: '我同意',
      nzOnOk: () => this.agree(),
      nzCancelText: '不了，谢谢',
      nzOnCancel: () => this.showConfirm()
    });
  }

  agree() {
    return new Promise((resolve, reject)=> {
      const protocolIds = [];
      this.list.forEach(item => {
        protocolIds.push(item.id);
      });
      this.httpClient.post('/api/uas/protocol/sign-toc', {
        userId: this.userService.userInfo.userId,
        protocolIds
      }).subscribe(res => {
        resolve(res);
        this.checkPassword();
      });
    })
  }

  checkPassword() {
    if (!this.userService.userInfo.existPassword) {
      this.modalService.confirm({
        nzTitle: '提示',
        nzContent: '检测到您未设置密码',
        nzOkText: '立即设置',
        nzOnOk: () => this.toSetPassword(),
        nzCancelText: '下次提醒',
        nzOnCancel: () => console.log('Cancel')
      });
    }
  }

  get smsCode() {
    return this.changePasswordForm.controls.smsCode;
  }

  /**
   * 获取短信验证码
   */
  getCaptcha(type) {
    this.isCaptchaLoading = true;
    const payload = new HttpParams().set('mobile', this.mobile);
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

  toSetPassword() {
    this.shouldShowModal = true;
    if (this.changePasswordForm.invalid) {
      return;
    }
    this.isOkLoading = true;
    this.httpClient.post('/api/uas/account/password/changeBySms?_allow_anonymous=true', {
      mobile: this.mobile,
      smsCode: this.smsCode.value,
      newPassword: this.changePasswordForm.value.newPassword,
    }).pipe(finalize(() => this.isOkLoading = false))
      .subscribe((res: Result) => {
        this.nzMessageService.success('设置密码成功');
        this.shouldShowModal = false;
        this.isOkLoading = false;
      });
  }

  showConfirm() {
    this.modalService.confirm({
      nzTitle: '提示',
      nzContent: '不同意将退出应用，您确定吗',
      nzOkText: '确定',
      nzOnOk: () => this.logout(),
      nzCancelText: '取消',
      nzOnCancel: () => this.showProtocols(this.list)
    });
  }

  logout() {
    const payload = new HttpParams()
      .set('token', this.tokenService.get().token);
    this.httpClient.post('/api/auth/oauth/remove/token?_allow_anonymous=true', payload, {
      headers: {
        Authorization: 'Basic ' + window.btoa(`${environment.client_id}:${environment.client_secret}`)
      }
    }).subscribe((result: Result) => {
      console.log(result);
    });
    this.tokenService.clear();
    this.userService.clear();
    this.router.navigateByUrl(this.tokenService.login_url);
  }

  /**
   * 务必取消订阅
   */
  ngOnDestroy() {
    this.reloadSubscription.unsubscribe();
  }

}
