import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {AppConfigListComponent} from './app-config/app-config-list.component';
import {CorpVerifyListComponent} from './corp-verify/corp-verify-list.component';
import {TokenListComponent} from './token/token-list.component';
import {RightListComponent} from './right-config/right-list/right-list.component';
import {TenantListComponent} from './tenant/tenant-list.component';
import {RightUrlListComponent} from './right-config/right-url/right-url-list.component';
import {CorpListComponent} from './corp-list/corp-list.component';
import {CorpDetailComponent} from './corp-list/corp-detail.component';
import {OauthClientListComponent} from './oauth-client/oauth-client-list.component';
import {RedisComponent} from './redis/redis.component';
import {OperationLogListComponent} from './operation-log/operation-log-list.component';
import {LoginLogListComponent} from './login-log/login-log-list.component';
import {UserInfoListComponent} from './user-info/user-info-list.component';
import {UserInfoDetailComponent} from './user-info/user-info-detail.component';

const routes: Routes = [
  {path: '', redirectTo: 'app-config', pathMatch: 'full'},
  {path: 'corp-verify', component: CorpVerifyListComponent, data: {name: '企业认证'}},
  {path: 'tenant', component: TenantListComponent, data: {name: '租户管理'}},
  {path: 'right-list', component: RightListComponent, data: {name: '权限管理'}},
  {path: 'right-url', component: RightUrlListComponent, data: {name: '鉴权管理'}},
  {path: 'token', component: TokenListComponent, data: {name: '凭证管理'}},
  {path: 'redis', component: RedisComponent, data: {name: 'Redis管理'}},
  {path: 'app-config', component: AppConfigListComponent, data: {name: '应用服务'}},
  {path: 'corp-list', component: CorpListComponent, data: {name: '企业列表'}},
  {path: 'corp-list/detail', component: CorpDetailComponent, data: {name: '企业详情'}},
  {path: 'oauth-client', component: OauthClientListComponent, data: {name: '第三方应用'}},
  {path: 'login-log', component: LoginLogListComponent, data: {name: '登录日志'}},
  {path: 'operation-log', component: OperationLogListComponent, data: {name: '操作日志'}},
  {path: 'user-info', component: UserInfoListComponent, data: {name: '用户管理'}},
  {path: 'user-info/detail', component: UserInfoDetailComponent, data: {name: '用户详情'}},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [],
  declarations: []
})
export class CloudRoutingModule {
}
