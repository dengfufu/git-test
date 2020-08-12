import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {WareOutcomeListComponent} from './ware-outcome-list/ware-outcome-list.component';
import {WareOutcomeAddComponent} from './ware-outcome-add/ware-outcome-add.component';
import {WareOutcomeDetailComponent} from './ware-outcome-detail/ware-outcome-detail.component';
import {WareOutcomeConsignComponent} from './ware-outcome-consign/ware-outcome-consign.component';
import {WareOutcomeBatchAuditComponent} from './ware-outcome-audit/ware-outcome-batch-audit/ware-outcome-batch-audit.component';

const routes: Routes = [
  {path: '', redirectTo: 'ware-outcome-list', pathMatch: 'full'},
  {path: 'ware-outcome-list', component: WareOutcomeListComponent, data: {name: '备件出库管理'}},
  {path: 'ware-outcome-add', component: WareOutcomeAddComponent, data: {name: '新增备件出库记录'}},
  {path: 'ware-outcome-batch-audit', component: WareOutcomeBatchAuditComponent, data: {name: '批量审批出库记录'}},
  {path: 'ware-outcome-detail', component: WareOutcomeDetailComponent, data: {name: '出库详情'}},
  {path: 'ware-outcome-consign', component: WareOutcomeConsignComponent, data: {name: '新建发货单'}},

]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class WmsOutcomeRoutingModule{

}
