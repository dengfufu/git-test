import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {CorpUserListComponent} from './corp-user/corp-user-list.component';
import {CorpRoleListComponent} from './corp-role/corp-role-list.component';
import {CorpRoleAddComponent} from './corp-role/corp-role-add.component';
import {CorpRoleEditComponent} from './corp-role/corp-role-edit.component';
import {UserSkillListComponent} from './user-skill/user-skill-list.component';
import {JoinCorpListComponent} from './join-corp/join-corp-list.component';
import {CorpUserRightScopeComponent} from './corp-user/corp-user-right-scope.component';


const routes: Routes = [
  {path: '', redirectTo: 'user-manage', pathMatch: 'full'},
  {path: 'user-skill', component: UserSkillListComponent, data: {name: '技能管理'}},
  {path: 'role-list', component: CorpRoleListComponent, data: {name: '角色管理'}},
  {path: 'role-add', component: CorpRoleAddComponent, data: {name: '添加角色'}},
  {path: 'role-edit', component: CorpRoleEditComponent, data: {name: '编辑角色'}},
  {path: 'user-list', component: CorpUserListComponent, data: {name: '员工管理'}},
  {path: 'join-corp', component: JoinCorpListComponent, data: {name: '加入申请'}},
  {path: 'user-right-scope', component: CorpUserRightScopeComponent, data: {name: '范围权限'}},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CorpUserRoutingModule {
}
