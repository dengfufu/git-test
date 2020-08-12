import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {SettingRoutingModule} from './setting-routing.module';
import {DeviceSmallClassEditComponent} from './device-config/device-small-class/device-small-class-edit.component';
import {DeviceLargeClassEditComponent} from './device-config/device-large-class/device-large-class-edit.component';
import {DeviceModelEditComponent} from './device-config/device-model/device-model-edit.component';
import {DeviceBrandEditComponent} from './device-config/device-brand/device-brand-edit.component';
import {DeviceSmallClassAddComponent} from './device-config/device-small-class/device-small-class-add.component';
import {DeviceSmallClassListComponent} from './device-config/device-small-class/device-small-class-list.component';
import {DeviceLargeClassAddComponent} from './device-config/device-large-class/device-large-class-add.component';
import {DeviceLargeClassListComponent} from './device-config/device-large-class/device-large-class-list.component';
import {DeviceModelAddComponent} from './device-config/device-model/device-model-add.component';
import {DeviceModelListComponent} from './device-config/device-model/device-model-list.component';
import {DeviceBrandAddComponent} from './device-config/device-brand/device-brand-add.component';
import {DeviceBrandListComponent} from './device-config/device-brand/device-brand-list.component';
import {DeviceConfigComponent} from './device-config/device-config.component';
import {CustomFieldConfigListComponent} from './custom-field-config/custom-field-config-list.component';
import {CustomFieldConfigAddComponent} from './custom-field-config/custom-field-config-add.component';
import {CustomFieldDataSourceAddComponent} from './custom-field-config/source/custom-field-data-source-add.component';
import {DeviceSpecificationEditComponent} from './device-config/device-small-class/specification/device-specification-edit.component';
import {DeviceSmallClassDetailComponent} from './device-config/device-small-class/device-small-class-detail.component';
import {DatePipe} from '@angular/common';

const Components = [
  DeviceConfigComponent,
  DeviceBrandListComponent,
  DeviceBrandAddComponent,
  DeviceModelListComponent,
  DeviceModelAddComponent,
  DeviceLargeClassListComponent,
  DeviceLargeClassAddComponent,
  DeviceSmallClassListComponent,
  DeviceSmallClassAddComponent,
  DeviceBrandEditComponent,
  DeviceModelEditComponent,
  DeviceLargeClassEditComponent,
  DeviceSmallClassEditComponent,
  DeviceSmallClassDetailComponent,
  DeviceSpecificationEditComponent,
  CustomFieldConfigListComponent,
  CustomFieldConfigAddComponent,
  CustomFieldDataSourceAddComponent
];

const EntryComponents = [
  DeviceBrandAddComponent,
  DeviceBrandEditComponent,
  DeviceLargeClassAddComponent,
  DeviceLargeClassEditComponent,
  DeviceModelAddComponent,
  DeviceModelEditComponent,
  DeviceSmallClassAddComponent,
  DeviceSmallClassEditComponent,
  DeviceSmallClassDetailComponent,
  DeviceSpecificationEditComponent,
  CustomFieldDataSourceAddComponent
];

/**
 * 系统设置
 */
@NgModule({
  imports: [SharedModule, SettingRoutingModule],
  exports: [],
  declarations: [...Components, ...EntryComponents],
  providers: [DatePipe],
  entryComponents: [...EntryComponents]
})
export class SettingModule {
}
