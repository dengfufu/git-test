import {Router, RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {WareBrandListComponent} from './ware-brand/list/ware-brand-list.component';
import {WareConfigComponent} from './ware-config/ware-config.component';
import {WarePropertyRightListComponent} from './ware-property-right/list/ware-property-right-list.component';
import {WareModelListComponent} from './ware-model/list/ware-model-list.component';
import {WareStatusListComponent} from './ware-status/list/ware-status-list.component';
import {WareSupplierListComponent} from './ware-supplier/list/ware-supplier-list.component';
import {WareExpressListComponent} from './ware-express/list/ware-express-list.component';
import {WareCatalogListComponent} from './ware-catalog/list/ware-catalog-list.component';
import {WareAreaListComponent} from './ware-area/list/ware-area-list.component';
import {WareHouseListComponent} from './ware-house/list/ware-house-list.component';
import {WareParamListComponent} from './ware-param/list/ware-param-list.component';
import {CustomListListComponent} from './custom-list/main-list/custom-list-list.component';

const routes: Routes = [
  {path: '', redirectTo: 'ware-config', pathMatch: 'full'},
  {
    path: 'ware-config',
    component: WareConfigComponent,
    data: {name: '基础数据'},
    children:
      [
        {path: '', redirectTo: 'ware-brand', pathMatch: 'full'},
        {path: 'ware-brand', component: WareBrandListComponent, data: {name: '品牌'}},
        {path: 'ware-supplier', component: WareSupplierListComponent, data: {name: '供应商'}},
        {path: 'ware-express', component: WareExpressListComponent, data: {name: '快递公司'}},
        {path: 'ware-status', component: WareStatusListComponent, data: {name: '物料状态'}},
        {path: 'ware-property-right', component: WarePropertyRightListComponent, data: {name: '物料产权'}},
        {path: 'ware-catalog', component: WareCatalogListComponent, data: {name: '分类'}},
        {path: 'ware-model', component: WareModelListComponent, data: {name: '型号'}},
        {path: 'ware-area', component: WareAreaListComponent, data: {name: '区域'}},
        {path: 'ware-house', component: WareHouseListComponent, data: {name: '库房'}},
        {path: 'custom-list', component: CustomListListComponent, data: {name: '常用列表'}},
        {path: 'ware-param', component: WareParamListComponent, data: {name: '参数'}},
      ]
  }
]

@NgModule({
  imports: [RouterModule.forChild(routes)]
})
export class WmsSettingRoutingModule{

}
