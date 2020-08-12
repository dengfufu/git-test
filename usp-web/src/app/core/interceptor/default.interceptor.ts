import {Injectable, Injector} from '@angular/core';
import {Route, Router} from '@angular/router';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpErrorResponse,
  HttpEvent,
  HttpResponseBase, HttpClient, HttpResponse
} from '@angular/common/http';
import {Observable, of, throwError} from 'rxjs';
import {mergeMap, catchError} from 'rxjs/operators';
import {NzMessageService, NzNotificationService} from 'ng-zorro-antd';
import {environment} from '@env/environment';
import {DA_SERVICE_TOKEN, ITokenService} from '@delon/auth';
import {UserService} from '@core/user/user.service';

const CODEMESSAGE = {
  200: '服务器成功返回请求的数据。',
  201: '新建或修改数据成功。',
  202: '一个请求已经进入后台排队（异步任务）。',
  204: '删除数据成功。',
  400: '发出的请求有错误，服务器没有进行新建或修改数据的操作。',
  401: '用户没有权限（令牌、用户名、密码错误）。',
  403: '访问未经授权，已被禁止。',
  404: '发出的请求针对的是不存在的记录，服务器没有进行操作。',
  406: '请求的格式不可得。',
  410: '请求的资源被永久删除，且不会再得到的。',
  422: '当创建一个对象时，发生一个验证错误。',
  500: '服务器发生错误，请检查服务器。',
  502: '网关错误。',
  503: '服务不可用，服务器暂时过载或维护。',
  504: '网关超时。'
};

// 不需要统一处理业务错误
const WHITE_URLS = [
  '/api/auth/oauth/mobile/token',
  '/api/auth/oauth/sms/token'
];

/**
 * 默认HTTP拦截器，其注册细节见 `app.module.ts`
 */
@Injectable()
export class DefaultInterceptor implements HttpInterceptor {
  constructor(private injector: Injector,
              private userService: UserService,
              private router: Router,
              private messageService: NzMessageService) {
  }

  private get notification(): NzNotificationService {
    return this.injector.get(NzNotificationService);
  }

  private goTo(url: string) {
    this.router.navigateByUrl(url);
  }

  private checkIsWhiteUrl(url: string): boolean {
    const api = url.split(environment.server_url)[1];
    return WHITE_URLS.includes(api);
  }

  private handleData(ev: HttpResponseBase): Observable<any> {
    // 可能会因为 `throw` 导出无法执行 `_HttpClient` 的 `end()` 操作
    if (ev.status > 0) {
      this.injector.get(HttpClient);
    }
    // 业务处理：一些通用操作
    switch (ev.status) {
      case 200:
        // 业务层级错误处理，以下是假定restful有一套统一输出格式（指不管成功与否都有相应的数据格式）情况下进行处理
        // 例如响应内容：
        //  错误内容：{ code: 1, msg: '非法参数' }
        //  正确内容：{ code: 0, data: {  } }
        // 则以下代码片断可直接适用
        if (ev instanceof HttpResponse) {
          const body: any = ev.body;
          if (body instanceof Blob) {
            return of(ev);
          }
          if (body && body.code !== 0 && !this.checkIsWhiteUrl(ev.url)) {
            this.messageService.error(body.msg);
            // 继续抛出错误中断后续所有 Pipe、subscribe 操作，因此：
            // this.http.get('/').subscribe() 并不会触发
            return throwError(body.msg);
          } else {
            // 重新修改 `body` 内容为 `response` 内容，对于绝大多数场景已经无须再关心业务状态码
            // return of(new HttpResponse(Object.assign(event, {body: body.response})));
            // 依然保持完整的格式
            return of(ev);
          }
        }
        break;
      case 401:
        if (this.router.url !== '/user/login' && this.router.url !== '/') {
          this.notification.error(`未登录或登录已过期，请重新登录。`, ``);
        }
        // 清空 token 信息
        (this.injector.get(DA_SERVICE_TOKEN) as ITokenService).clear();
        this.userService.clear();
        this.goTo('/user/login');
        break;
      case 403:
        this.messageService.error(CODEMESSAGE['403']);
        return throwError({});
        break;
      case 404:
      case 500:
        // this.goTo(`/exception/${ev.status}`);
        console.warn(CODEMESSAGE['500'], ev);
        break;
      default:
        if (ev instanceof HttpErrorResponse) {
          console.warn('未可知错误，大部分是由于后端不支持CORS或无效配置引起', ev);
          return throwError(ev);
        }
        break;
    }
    return of(ev);
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // 统一加上服务端前缀
    let url = req.url;
    let newReq;

    if (url.indexOf('_allow_anonymous=true&') > 0) {
      url = url.replace('_allow_anonymous=true&', '');
    } else {
      url = url.replace('?_allow_anonymous=true', '');
    }

    if (!url.startsWith('https://') && !url.startsWith('http://') && !url.startsWith('assets') &&
      !url.startsWith('/ba/plugin/cda/api/doQuery')) {
      url = environment.server_url + url;
      newReq = req.clone({url});
      return next.handle(newReq).pipe(
        catchError((err: HttpErrorResponse) => this.handleData(err)),
        mergeMap((event: any) => {
          // 允许统一对请求错误处理
          if (event instanceof HttpResponseBase) {
            return this.handleData(event);
          }
          // 若一切都正常，则后续操作
          return of(event);
        })
      );
    } else {
      newReq = req.clone({url});
    }
    return next.handle(newReq);
  }
}
