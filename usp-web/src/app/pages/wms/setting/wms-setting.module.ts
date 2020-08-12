import {NgModule} from '@angular/core';
import {WareBrandListComponent} from './ware-brand/list/ware-brand-list.component';
import {WareBrandAddComponent} from './ware-brand/add/ware-brand-add.component';
import {WareBrandEditComponent} from './ware-brand/edit/ware-brand-edit.component';
import {SharedModule} from '@shared/shared.module';
import {WmsSettingRoutingModule} from './wms-setting-routing.module';
import {WareSupplierListComponent} from './ware-supplier/list/ware-supplier-list.component';
import {WareSupplierAddComponent} from './ware-supplier/add/ware-supplier-add.component';
import {WareSupplierEditComponent} from './ware-supplier/edit/ware-supplier-edit.component';
import {WareExpressListComponent} from './ware-express/list/ware-express-list.component';
import {WareStatusListComponent} from './ware-status/list/ware-status-list.component';
import {WarePropertyRightListComponent} from './ware-property-right/list/ware-property-right-list.component';
import {WareCatalogListComponent} from './ware-catalog/list/ware-catalog-list.component';
import {WareModelListComponent} from './ware-model/list/ware-model-list.component';
import {WareAreaListComponent} from './ware-area/list/ware-area-list.component';
import {WareHouseListComponent} from './ware-house/list/ware-house-list.component';
import {WareParamListComponent} from './ware-param/list/ware-param-list.component';
import {WareExpressAddComponent} from './ware-express/add/ware-express-add.component';
import {WareExpressEditComponent} from './ware-express/edit/ware-express-edit.component';
import {WareStatusAddComponent} from './ware-status/add/ware-status-add.component';
import {WareStatusEditComponent} from './ware-status/edit/ware-status-edit.component';
import {WarePropertyRightAddComponent} from './ware-property-right/add/ware-property-right-add.component';
import {WarePropertyRightEditComponent} from './ware-property-right/edit/ware-property-right-edit.component';
import {WareCatalogAddComponent} from './ware-catalog/add/ware-catalog-add.component';
import {WareCatalogEditComponent} from './ware-catalog/edit/ware-catalog-edit.component';
import {WareModelAddComponent} from './ware-model/add/ware-model-add.component';
import {WareModelEditComponent} from './ware-model/edit/ware-model-edit.component';
import {WareAreaAddComponent} from './ware-area/add/ware-area-add.component';
import {WareAreaEditComponent} from './ware-area/edit/ware-area-edit.component';
import {WareHouseAddComponent} from './ware-house/add/ware-house-add.component';
import {WareHouseEditComponent} from './ware-house/edit/ware-house-edit.component';
import {WareParamAddComponent} from './ware-param/add/ware-param-add.component';
import {WareParamEditComponent} from './ware-param/edit/ware-param-edit.component';
import {WareConfigComponent} from './ware-config/ware-config.component';
import {CustomListListComponent} from './custom-list/main-list/custom-list-list.component';
import {CustomListAddComponent} from './custom-list/main-add/custom-list-add.component';
import {CustomListDetailComponent} from './custom-list/detail-add/custom-list-detail.component';
import {CustomListNameEditComponent} from './custom-list/main-name-edit/custom-list-name-edit.component';

const Components = [
  WareConfigComponent,
  WareBrandListComponent,
  WareSupplierListComponent,
  WareExpressListComponent,
  WareStatusListComponent,
  WarePropertyRightListComponent,
  WareCatalogListComponent,
  WareModelListComponent,
  WareAreaListComponent,
  WareHouseListComponent,
  CustomListListComponent,// 常用列表
  WareParamListComponent
]

const EntryComponents = [
  WareBrandAddComponent,
  WareBrandEditComponent,
  WareSupplierAddComponent,
  WareSupplierEditComponent,
  WareExpressAddComponent,
  WareExpressEditComponent,
  WareStatusAddComponent,
  WareStatusEditComponent,
  WarePropertyRightAddComponent,
  WarePropertyRightEditComponent,
  WareCatalogAddComponent,
  WareCatalogEditComponent,
  WareModelAddComponent,
  WareModelEditComponent,
  WareAreaAddComponent,
  WareAreaEditComponent,
  WareHouseAddComponent,
  WareHouseEditComponent,
  CustomListAddComponent,
  CustomListDetailComponent,// 常用列表
  CustomListNameEditComponent,
  WareParamAddComponent,
  WareParamEditComponent
]

@NgModule({
  imports: [SharedModule, WmsSettingRoutingModule],
  exports: [],
  declarations: [...Components, ...EntryComponents],
  entryComponents: [...EntryComponents],
  providers: []
})
export class WmsSettingModule{}
