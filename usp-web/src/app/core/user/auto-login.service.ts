import {Inject, Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '@env/environment';
import {Result} from '@core/interceptor/result';
import {DA_SERVICE_TOKEN, ITokenService} from '@delon/auth';

/**
 * 自动登录
 *
 * 1. token存在每次初始化都会刷新token
 * 2. 每隔10分钟检查token，如果快过期，则刷新token
 */
@Injectable({
  providedIn: 'root'
})
export class AutoLoginService {

  constructor(private httpClient: HttpClient,
              @Inject(DA_SERVICE_TOKEN) private tokenService: ITokenService) {
  }

  /**
   * 每隔10分钟检查token，如果快过期，则自动更新
   */
  initIntervalJob() {
    setInterval(() => {
      console.log('每隔10分钟检查token，如果快过期，则自动更新');
      const tokenModel: any = this.tokenService.get();
      if (tokenModel) {
        const expires_date = tokenModel.expires_date;
        const refreshTime = new Date().getTime() + 10 * 60 * 1000;
        if (refreshTime >= expires_date) {
          this.refreshToken(tokenModel.refresh_token).subscribe((result: Result) => {
            if (result.code === 0) {
              const token = result.data;
              this.tokenService.set({
                token: token.access_token,
                refresh_token: token.refresh_token,
                expires_in: token.expires_in,
                expires_date: new Date().getTime() + token.expires_in * 1000,
                scope: token.scope
              });
            }
          });
        }
      }
    }, 10 * 60 * 1000 - 10 * 1000);
  }

  refreshToken(refreshToken: string) {
    const payload = new HttpParams()
      .set('grant_type', 'refresh_token')
      .set('client_id', String(environment.client_id))
      .set('client_secret', environment.client_secret)
      .set('refresh_token', refreshToken);
    return this.httpClient.post('/api/auth/oauth/token?_allow_anonymous=true', payload);
  }
}
