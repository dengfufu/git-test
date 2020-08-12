import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {UasRoutingModule} from './uas-routing.module';
import {UserListComponent} from './user-list/user-list.component';
import {RoleListComponent} from './role-list/role-list.component';
import {PermissionListComponent} from './permission-list/permission-list.component';
import {ClientListComponent} from './client-list/client-list.component';
import {AccountCenterComponent} from './account-center/account-center.component';
import {PosTrackComponent} from './account-center/pos-track/pos-track.component';
import {UserInfoComponent} from './account-center/user-info/user-info.component';
import {SecurityComponent} from './account-center/security/security.component';
import {CorpInfoComponent} from './account-center/corp-info/corp-info.component';
import {WorkingStatusComponent} from './account-center/working-status/working-status.component';

const accountComponents = [
  AccountCenterComponent,
  UserInfoComponent,
  PosTrackComponent,
  SecurityComponent,
  CorpInfoComponent,
  WorkingStatusComponent
];

@NgModule({
  imports: [SharedModule, UasRoutingModule],
  exports: [],
  declarations: [
    UserListComponent,
    RoleListComponent,
    PermissionListComponent,
    ClientListComponent,

    ...accountComponents,

  ],
})
export class UasModule {
}
