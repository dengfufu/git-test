import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {DeviceBranchListComponent} from './device-branch/device-branch-list.component';
import {DeviceBranchAddComponent} from './device-branch/device-branch-add.component';
import {DeviceBranchEditComponent} from './device-branch/device-branch-edit.component';
import {DeviceRoutingModule} from './device-routing.module';
import {DeviceInfoListComponent} from './device-info/device-info-list.component';
import {DeviceInfoAddComponent} from './device-info/device-info-add.component';
import {DeviceInfoEditComponent} from './device-info/device-info-edit.component';
import {DeviceLocateEditComponent} from './device-info/locate/device-locate-eidt.component';
import {DeviceServiceEditComponent} from './device-info/service/device-service-edit.component';
import {DeviceDetailComponent} from './device-info/device-detail/device-detail.component';
import {CorpModule} from '../corp/corp.module';
import {DeviceBranchDetailComponent} from './device-branch/device-branch-detail.component';
import { DeviceInfoImportComponent } from './device-info/device-info-import/device-info-import.component';

export const Components = [
  DeviceBranchListComponent,
  DeviceBranchAddComponent,
  DeviceBranchEditComponent,
  DeviceInfoListComponent,
  DeviceInfoAddComponent,
  DeviceInfoEditComponent,
  DeviceLocateEditComponent,
  DeviceServiceEditComponent,
  DeviceDetailComponent,
  DeviceBranchDetailComponent,
  DeviceInfoImportComponent
];

@NgModule({
  imports: [SharedModule, DeviceRoutingModule, CorpModule],
  exports: [],
  declarations: [...Components],
  providers: [],
  entryComponents: [
    ...Components
  ]
})
export class DeviceModule {
}
