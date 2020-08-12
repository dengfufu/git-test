import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {CloudRoutingModule} from './cloud-routing.module';
import {AppConfigListComponent} from './app-config/app-config-list.component';
import {CorpVerifyListComponent} from './corp-verify/corp-verify-list.component';
import {CorpVerifyCheckComponent} from './corp-verify/corp-verify-check/corp-verify-check.component';
import {TokenListComponent} from './token/token-list.component';
import {AppConfigAddComponent} from './app-config/app-config-add.component';
import {RightListComponent} from './right-config/right-list/right-list.component';
import {RightAddComponent} from './right-config/right-list/right-add.component';
import {RightEditComponent} from './right-config/right-list/right-edit.component';
import {TenantListComponent} from './tenant/tenant-list.component';
import {TenantAddComponent} from './tenant/tenant-add.component';
import {TenantEditComponent} from './tenant/tenant-edit.component';
import {RightUrlListComponent} from './right-config/right-url/right-url-list.component';
import {RightUrlAddComponent} from './right-config/right-url/right-url-add.component';
import {RightUrlEditComponent} from './right-config/right-url/right-url-edit.component';
import {CorpListComponent} from './corp-list/corp-list.component';
import {CorpDetailComponent} from './corp-list/corp-detail.component';
import {SetAdminComponent} from './corp-list/set-admin/set-admin.component';
import {OauthClientListComponent} from './oauth-client/oauth-client-list.component';
import {OauthClientEditComponent} from './oauth-client/edit/oauth-client-edit.component';
import {OauthClientAddComponent} from './oauth-client/add/oauth-client-add.component';
import {NgxJsonViewerModule} from 'ngx-json-viewer';
import {RedisComponent} from './redis/redis.component';
import {LoginLogListComponent} from './login-log/login-log-list.component';
import {OperationLogListComponent} from './operation-log/operation-log-list.component';
import {UserInfoListComponent} from './user-info/user-info-list.component';
import {UserInfoDetailComponent} from './user-info/user-info-detail.component';
import {CorpVerifyEditComponent} from './corp-list/corp-verify-edit/corp-verify-edit.component';
import {CorpRegistryEditComponent} from './corp-list/corp-registry-edit/corp-registry-edit.component';
import {CorpRegUserEditComponent} from './corp-list/corp-regUser-edit/corp-regUser-edit.component';

/**
 * 平台管理
 */
@NgModule({
  imports: [SharedModule, NgxJsonViewerModule, CloudRoutingModule],
  exports: [],
  declarations: [
    AppConfigListComponent,
    AppConfigAddComponent,
    CorpVerifyListComponent,
    CorpVerifyCheckComponent,
    TokenListComponent,
    RightListComponent,
    RightAddComponent,
    RightEditComponent,
    RightUrlListComponent,
    RightUrlAddComponent,
    RightUrlEditComponent,
    TenantListComponent,
    TenantAddComponent,
    TenantEditComponent,
    CorpListComponent,
    CorpDetailComponent,
    SetAdminComponent,
    OauthClientListComponent,
    OauthClientEditComponent,
    OauthClientAddComponent,
    RedisComponent,
    OauthClientAddComponent,
    LoginLogListComponent,
    OperationLogListComponent,
    UserInfoListComponent,
    UserInfoDetailComponent,
    CorpVerifyEditComponent,
    CorpRegistryEditComponent,
    CorpRegUserEditComponent
  ],
  providers: [],
  entryComponents: [
    CorpVerifyCheckComponent,
    RightAddComponent,
    RightEditComponent,
    RightUrlAddComponent,
    RightUrlEditComponent,
    TenantAddComponent,
    TenantEditComponent,
    SetAdminComponent,
    OauthClientEditComponent,
    OauthClientAddComponent,
    LoginLogListComponent,
    OperationLogListComponent,
    UserInfoListComponent,
    CorpVerifyEditComponent,
    CorpRegistryEditComponent,
    CorpRegUserEditComponent
  ]
})
export class CloudModule {
}
