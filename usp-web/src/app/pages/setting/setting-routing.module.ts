import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {DeviceConfigComponent} from './device-config/device-config.component';
import {DeviceBrandListComponent} from './device-config/device-brand/device-brand-list.component';
import {DeviceModelListComponent} from './device-config/device-model/device-model-list.component';
import {DeviceLargeClassListComponent} from './device-config/device-large-class/device-large-class-list.component';
import {DeviceSmallClassListComponent} from './device-config/device-small-class/device-small-class-list.component';
import {CustomFieldConfigListComponent} from './custom-field-config/custom-field-config-list.component';
import {CustomFieldConfigAddComponent} from './custom-field-config/custom-field-config-add.component';
import {CustomFieldDataSourceAddComponent} from './custom-field-config/source/custom-field-data-source-add.component';

const routes: Routes = [
  {path: '', redirectTo: 'device-config', pathMatch: 'full'},
  {
    path: 'device-config', component: DeviceConfigComponent, data: {name: '设备设置'},
    children: [
      {path: 'device-brand', component: DeviceBrandListComponent, data: {name: '设备品牌'}},
      {path: 'device-model', component: DeviceModelListComponent, data: {name: '设备型号'}},
      {path: 'device-large-class', component: DeviceLargeClassListComponent, data: {name: '设备大类'}},
      {path: 'device-small-class', component: DeviceSmallClassListComponent, data: {name: '设备类型'}}
    ]
  },
  {
    path: 'work-config',
    data: {name: '工单设置'},
    loadChildren: () => import('../setting/work-config/work-config.module').then(m => m.WorkConfigModule)
  },
  {
    path: 'work-remind-config',
    data: {name: '工单预警设置'},
    loadChildren: () => import('../setting/work-remind-config/work-remind-config.module').then(m => m.WorkRemindConfigModule)
  },
  // {
  //   path: 'custom-reason',
  //   data: {name: '委托商原因'},
  //   loadChildren: () => import('../setting/custom-reason/custom-reason.module').then(m => m.CustomReasonModule)
  // },
  {
    path: 'service-reason',
    data: {name: '服务选项'},
    loadChildren: () => import('../setting/service-reason/service-reason.module').then(m => m.ServiceReasonModule)
  },
  {
    path: 'automation-config',
    data: {name: '自动化设置'},
    loadChildren: () => import('../setting/automation-config/automation-config.module').then(m => m.AutomationConfigModule)
  },
  {
    path: 'custom-field-config',
    component: CustomFieldConfigListComponent,
    data: {name: '自定义字段设置'}
  },
  {
    path: 'custom-field-config-add',
    component: CustomFieldConfigAddComponent,
    data: {name: '自定义字段添加'}
  },
  {
    path: 'custom-field-data-source-add',
    component: CustomFieldDataSourceAddComponent,
    data: {name: '自定义字段数据源添加'}
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [],
  declarations: []
})
export class SettingRoutingModule {
}
