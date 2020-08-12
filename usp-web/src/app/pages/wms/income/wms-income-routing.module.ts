import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {WareIncomeListComponent} from './ware-income-list/ware-income-list.component';
import {WareIncomeViewComponent} from './ware-income-view/ware-income-view.component';
import {WareIncomeBatchAddComponent} from './ware-income-batch-add/ware-income-batch-add.component';
import {WareIncomeBatchAuditComponent} from './ware-income-batch-audit/ware-income-batch-audit.component';
import {WareIncomeAddComponent} from './ware-income-add/ware-income-add.component';
import {WareIncomeAuditComponent} from './ware-income-audit/ware-income-audit.component';

const routes: Routes = [
  {path: '', redirectTo: 'ware-income-list', pathMatch: 'full'},
  {path: 'ware-income-list', component: WareIncomeListComponent, data: {name: '入库管理', keep: true, key: 'ware-income-list'}},
  {path: 'ware-income-add', component: WareIncomeAddComponent, data: {name: '添加入库单'}},
  {path: 'ware-income-batch-add', component: WareIncomeBatchAddComponent, data: {name: '批量添加入库单'}},
  {path: 'ware-income-view', component: WareIncomeViewComponent, data: {name: '查看入库详情'}},
  {path: 'ware-income-batch-audit', component: WareIncomeBatchAuditComponent, data: {name: '批量审批入库单'}},
  {path: 'ware-income-audit', component: WareIncomeAuditComponent, data: {name: '审批入库单'}},
]

@NgModule({
  imports: [RouterModule.forChild(routes)]
})
export class WmsIncomeRoutingModule{

}
