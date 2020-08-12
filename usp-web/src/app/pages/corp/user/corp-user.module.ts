import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {CorpUserRoutingModule} from './corp-user-routing.module';
import {CorpUserAddComponent} from './corp-user/corp-user-add.component';
import {CorpRoleListComponent} from './corp-role/corp-role-list.component';
import {CorpRoleAddComponent} from './corp-role/corp-role-add.component';
import {CorpRoleEditComponent} from './corp-role/corp-role-edit.component';
import {CorpUserListComponent} from './corp-user/corp-user-list.component';
import {UserSkillListComponent} from './user-skill/user-skill-list.component';
import {UserSkillEditComponent} from './user-skill/user-skill-edit.component';
import {UserSkillAddComponent} from './user-skill/user-skill-add.component';
import {JoinCorpListComponent} from './join-corp/join-corp-list.component';
import {JoinCorpCheckComponent} from './join-corp/join-corp-check.component';
import {RoleRightScopeComponent} from './corp-role/right-scope/role-right-scope.component';
import {UserRightScopeComponent} from './corp-user/right-scope/user-right-scope.component';
import {CorpUserRightScopeComponent} from './corp-user/corp-user-right-scope.component';

@NgModule({
  imports: [SharedModule, CorpUserRoutingModule],
  exports: [],
  declarations: [
    CorpRoleListComponent,
    CorpRoleAddComponent,
    CorpRoleEditComponent,
    CorpUserListComponent,
    CorpUserAddComponent,
    CorpUserRightScopeComponent,
    UserSkillListComponent,
    UserSkillEditComponent,
    UserSkillAddComponent,
    JoinCorpListComponent,
    JoinCorpCheckComponent,
    RoleRightScopeComponent,
    UserRightScopeComponent
  ],
  entryComponents: [
    CorpRoleAddComponent,
    CorpRoleEditComponent,
    CorpUserAddComponent,
    CorpUserRightScopeComponent,
    UserSkillAddComponent,
    UserSkillEditComponent,
    JoinCorpCheckComponent,
    RoleRightScopeComponent,
    UserRightScopeComponent
  ]
})
export class CorpUserModule {
}
