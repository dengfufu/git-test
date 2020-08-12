import {Inject, Injectable} from '@angular/core';
import {UserService} from '@core/user/user.service';
import {AutoLoginService} from '@core/user/auto-login.service';
import {DA_SERVICE_TOKEN, ITokenService} from '@delon/auth';
import {HttpClient} from '@angular/common/http';
import {Result} from '@core/interceptor/result';

@Injectable()
export class StartupService {

  constructor(private httpClient: HttpClient,
              @Inject(DA_SERVICE_TOKEN) private tokenService: ITokenService,
              private autoLoginService: AutoLoginService,
              private userService: UserService) {
  }

  init(): Promise<any> {
    return new Promise(resolve => {
      const tokenModel: any = this.tokenService.get();
      if (tokenModel.token) {
        // 自动登录
        this.autoLoginService.refreshToken(tokenModel.refresh_token).subscribe((result: Result) => {
          if (result.code === 0) {
            const token = result.data;
            this.tokenService.set({
              token: token.access_token,
              refresh_token: token.refresh_token,
              expires_in: token.expires_in,
              expires_date: new Date().getTime() + token.expires_in * 1000,
              scope: token.scope
            });
            this.initData().then(() => {
              resolve('当前用户信息初始化完成');
            });
          }
        }, (error: any) => {
          console.log(error);
          this.initData().then(() => {
            resolve('当前用户信息初始化完成');
          });
        });
      } else {
        resolve('未登陆过或者已退出登录');
      }
    });
  }

  /**
   * 初始化数据
   */
  initData(): Promise<any> {
    return Promise.all([
      this.userService.initUserInfo(),
      this.userService.initCustomCorps(),
      this.userService.loadUserDefinedSetting(), // 加载用户的系统配置
    ]).then(res => {
      this.userService.saveUserDevice();
      console.log(res);
    }).catch(e => {
      console.log(e);
    });
  }

}
