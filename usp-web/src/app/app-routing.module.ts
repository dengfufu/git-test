import {NgModule} from '@angular/core';
import {Routes, RouterModule, PreloadAllModules} from '@angular/router';
import {LoginComponent} from './pages/user/login/login.component';
import {RegisterComponent} from './pages/user/register/register.component';
import {UserLayoutComponent} from '@shared/layout/user-layout/user-layout.component';
import {BasicLayoutComponent} from '@shared/layout/basic-layout/basic-layout.component';
import {SimpleGuard} from '@delon/auth';
import {ForgetComponent} from './pages/user/forget/forget.component';


const routes: Routes = [
  {
    path: '',
    component: BasicLayoutComponent,
    canActivate: [SimpleGuard],
    children: [
      {path: '', redirectTo: 'dashboard', pathMatch: 'full'},
      {
        path: 'dashboard',
        data: {name: 'Dashboard'},
        loadChildren: () => import('./pages/dashboard/dashboard.module').then(m => m.DashboardModule)
      },
      {
        path: 'anyfix',
        data: {name: '工单管理'},
        loadChildren: () => import('./pages/anyfix/anyfix.module').then(m => m.AnyfixModule)
      },
      {
        path: 'device',
        data: {name: '设备管理'},
        loadChildren: () => import('./pages/device/device.module').then(m => m.DeviceModule)
      },
      {
        path: 'wms',
        data: {name: '备件管理'},
        loadChildren: () => import('./pages/wms/wms.module').then(m => m.WmsModule)
      },
      {
        path: 'corp',
        data: {name: '客户管理'},
        loadChildren: () => import('./pages/corp/corp.module').then(m => m.CorpModule)
      },
      {
        path: 'corp-user',
        data: {name: '人员管理'},
        loadChildren: () => import('./pages/corp/user/corp-user.module').then(m => m.CorpUserModule)
      },
      {
        path: 'setting',
        data: {name: '系统设置'},
        loadChildren: () => import('./pages/setting/setting.module').then(m => m.SettingModule)
      },
      {
        path: 'cloud',
        data: {name: '平台管理'},
        loadChildren: () => import('./pages/cloud/cloud.module').then(m => m.CloudModule)
      },
      {
        path: 'wallet',
        data: {name: '支付管理'},
        loadChildren: () => import('./pages/wallet/wallet.module').then(m => m.WalletModule)
      },
      {
        path: 'uas',
        data: {name: '个人中心'},
        loadChildren: () => import('./pages/uas/uas.module').then(m => m.UasModule)
      }
    ]
  },
  {
    path: 'user',
    component: UserLayoutComponent,
    children: [
      {path: 'login', component: LoginComponent, data: {name: '登录'}},
      {path: 'register', component: RegisterComponent, data: {name: '注册'}},
      {path: 'forget', component: ForgetComponent, data: {name: '忘记密码'}}
    ]
  },
  {path: '**', redirectTo: 'exception/404'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true, onSameUrlNavigation: 'reload',preloadingStrategy: PreloadAllModules})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
