import {BrowserModule} from '@angular/platform-browser';
import {APP_INITIALIZER, ErrorHandler, LOCALE_ID, NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {registerLocaleData} from '@angular/common';
import zh from '@angular/common/locales/zh';
import {SharedModule} from '@shared/shared.module';
import {DelonAuthConfig, DelonAuthModule, SimpleInterceptor} from '@delon/auth';
import {TranslateModule} from '@ngx-translate/core';
import {UserModule} from './pages/user/user.module';
import {PRO_LAYOUT} from 'pro-layout';
import {StartupService} from '@core/startup/startup.service';
import {DefaultInterceptor} from '@core/interceptor/default.interceptor';
import {NZ_CONFIG} from 'ng-zorro-antd';
import {ParamInterceptor} from '@core/interceptor/param.interceptor';
import {ErrorHandlerService} from '@core/service/error-handler.service';
import {DelonACLConfig, DelonACLModule} from '@delon/acl';
import {NgxEchartsModule} from 'ngx-echarts';
import {ST_PRO_CONFIG} from '@shared/components/st-pro';

registerLocaleData(zh);

export function fnDelonAuthConfig(): DelonAuthConfig {
  return {
    ...new DelonAuthConfig(),
    ...({
      login_url: '/user/login',
      token_send_place: 'header',
      token_send_key: 'Authorization',
      token_send_template: 'Bearer ${token}',
      // 登录与注册
      ignores: [new RegExp('/ba/plugin/cda/api/doQuery')]
    } as DelonAuthConfig),
  };
}

export function fnDelonACLConfig(): DelonACLConfig {
  return {
    ...new DelonACLConfig(),
    ...({
      guard_url: 'exception/403'
    } as DelonACLConfig),
  };
}

export function StartupServiceFactory(startupService: StartupService) {
  return () => startupService.init();
}

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,

    SharedModule,
    DelonAuthModule,
    DelonACLModule.forRoot(),
    TranslateModule.forRoot(),
    AppRoutingModule,
    NgxEchartsModule,

    // pages
    UserModule
  ],
  providers: [
    StartupService,
    { provide: LOCALE_ID, useValue: 'zh-cn' },
    {provide: APP_INITIALIZER, useFactory: StartupServiceFactory, deps: [StartupService], multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: SimpleInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ParamInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: DefaultInterceptor, multi: true},
    {provide: DelonAuthConfig, useFactory: fnDelonAuthConfig},
    {provide: DelonACLConfig, useFactory: fnDelonACLConfig},
    {
      provide: PRO_LAYOUT,
      useValue: {
        title: '畅修',
        logo: 'assets/logo.svg',
        navTheme: 'light',
        layout: 'sidemenu',
        contentWidth: 'Fluid',
        fixedHeader: true,
        autoHideHeader: false,
        fixSiderbar: true,
        mode: 'inline',
        // mode: 'vertical',
        siderWidth: '160'
      }
    },
    {
      provide: NZ_CONFIG, useValue: {
        table: {
          nzSize: 'small'
        },
        card: {
          nzSize: 'small'
        }
      }
    },
    // {
    //   provide: ST_PRO_CONFIG, useValue: {
    //     columnSetting: {
    //       persistUrl: '/api/uas/user-defined-setting/merge'
    //     }
    //   }
    // },
    [{provide: ErrorHandler, useClass: ErrorHandlerService}],
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
