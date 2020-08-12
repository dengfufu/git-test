import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {UserListComponent} from './user-list/user-list.component';
import {RoleListComponent} from './role-list/role-list.component';
import {PermissionListComponent} from './permission-list/permission-list.component';
import {ClientListComponent} from './client-list/client-list.component';
import {AccountCenterComponent} from './account-center/account-center.component';

const routes: Routes = [
  {path: '', redirectTo: 'user-list', pathMatch: 'full'},
  {path: 'user-list', component: UserListComponent, data: {name: '用户管理'}},
  {path: 'role-list', component: RoleListComponent, data: {name: '角色管理'}},
  {path: 'permission-list', component: PermissionListComponent, data: {name: '权限管理'}},
  {path: 'client-list', component: ClientListComponent, data: {name: '应用管理'}},
  {path: 'account-center', component: AccountCenterComponent, data: {name: '个人中心'}},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UasRoutingModule {
}
