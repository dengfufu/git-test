import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import uuidV1 from 'uuid/v1';
import {UserService} from '@core/user/user.service';
import {environment} from '@env/environment';

/**
 * 增加公共参数
 */
@Injectable()
export class ParamInterceptor implements HttpInterceptor {

  browser = {
    versions: () => {
      const u = navigator.userAgent;
      const app = navigator.appVersion;
      return {
        trident: u.indexOf('Trident') > -1, // IE内核
        presto: u.indexOf('Presto') > -1, // opera内核
        webKit: u.indexOf('AppleWebKit') > -1, // 苹果、谷歌内核
        gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') === -1, // 火狐内核
        mobile: !!u.match(/AppleWebKit.*Mobile.*/), // 是否为移动终端
        ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), // ios终端
        android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, // android终端或uc浏览器
        iPhone: u.indexOf('iPhone') > -1, // 是否为iPhone或者QQHD浏览器
        iPad: u.indexOf('iPad') > -1, // 是否iPad
        webApp: u.indexOf('Safari') === -1 // 是否web应用程序，没有头部与底部
      };
    },
    language: navigator.language.toLowerCase()
  };

  constructor(private userService: UserService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let newReq;
    if (req.url.indexOf('/ba/plugin/cda/api/doQuery') < 0) {
      let lon = localStorage.getItem('current_pos_lon');
      let lat = localStorage.getItem('current_pos_lat');
      let deviceId = '';
      let deviceType = 0;
      const userAgent = navigator.userAgent.toLowerCase();
      if (this.browser.versions().mobile) {
        deviceType = 4; // mobileWeb
      } else {
        deviceType = 5;// PC
      }

      let osVersion = userAgent;
      lon = !lon || lon === 'undefined' || lon === 'NaN' ? '' : lon;
      lat = !lat || lat === 'undefined' || lat === 'NaN' ? '' : lat;
      deviceId = !deviceId || deviceId === 'undefined' || deviceId === 'NaN' || deviceId === null
      || deviceId === 'null' ? '0' : deviceId;
      osVersion = !osVersion || osVersion === 'undefined' || osVersion === 'NaN' || osVersion === null
      || osVersion === 'null' ? '' : osVersion;

      const commonParams = {
        corpId: this.userService.currentCorp.corpId,
        appId: 10001, // TODO remove,暂时兼容后端代码
        retryFlag: '0',
        tranId: uuidV1(),
        version: environment.version,
        h5Version: environment.h5_version,
        deviceId: '' + deviceId,
        deviceType: '' + deviceType,
        osVersion: '' + osVersion,
        lat,
        lon
      };
      newReq = req.clone({
        headers: req.headers.set('Common-Params', JSON.stringify(commonParams))
      });
    } else {
      newReq = req;
    }
    return next.handle(newReq);
  }
}
