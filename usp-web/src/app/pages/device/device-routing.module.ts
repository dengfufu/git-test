import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {DeviceBranchListComponent} from './device-branch/device-branch-list.component';
import {DeviceInfoListComponent} from './device-info/device-info-list.component';
import {DeviceDetailComponent} from './device-info/device-detail/device-detail.component';
import {DeviceBranchDetailComponent} from './device-branch/device-branch-detail.component';

const routes: Routes = [
  {path: '', redirectTo: 'device-branch-list', pathMatch: 'full'},
  {path: 'device-branch-list', component: DeviceBranchListComponent, data: {name: '设备网点'}},
  {path: 'device-branch-list/device-branch-detail', component: DeviceBranchDetailComponent, data: {name: '设备网点详情'}},
  {path: 'device-info', component: DeviceInfoListComponent, data: {name: '设备档案'}},
  {path: 'device-info/device-detail', component: DeviceDetailComponent, data: {name: '设备详情'}},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [],
  declarations: [],
})
export class DeviceRoutingModule {
}
