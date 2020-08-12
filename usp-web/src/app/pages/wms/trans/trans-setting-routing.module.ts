import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';


import {TransComponent} from './trans.component';
import {WareTransComponent} from './ware-trans/ware-trans.component';
import {WareTransDetailComponent} from './ware-trans/ware-trans-detail/ware-trans-detail.component';
import {QuickTransferComponent} from './quick-transfer/quick-transfer.component';
import {QuickTransferDetailComponent} from './quick-transfer/quick-transfer-detail/quick-transfer-detail.component';
import {WareReturnComponent} from './ware-return/ware-return.component';

import {WareReturnDetailComponent} from './ware-return/ware-return-detail/ware-return-detail.component';
import {WareTransEditComponent} from './ware-trans/ware-trans-edit/ware-trans-edit.component';
import {WareTransApplyComponent} from './ware-trans/ware-trans-apply/ware-trans-apply.component';
import {WareTransAuditComponent} from './ware-trans/ware-trans-audit/ware-trans-audit.component';
import {WareTransDispatchComponent} from './ware-trans/ware-trans-dispatch/ware-trans-dispatch.component';
import {ToBeReceivedListComponent} from './ware-trans/to-be-received-list/to-be-received-list.component';
import {WareTransReceiveComponent} from './ware-trans/ware-trans-receive/ware-trans-receive.component';
import {QuickTransferApplyComponent} from './quick-transfer/quick-transfer-apply/quick-transfer-apply.component';
import {QuickTransferConfirmComponent} from './quick-transfer/quick-transfer-confirm/quick-transfer-confirm.component';
import {WareReturnApplyComponent} from './ware-return/ware-return-apply/ware-return-apply.component';
import {ReturnToBeReceivedListComponent} from './ware-return/to-be-received-list/return-to-be-received-list.component';
import {WareReturnConfirmComponent} from './ware-return/ware-return-confirm/ware-return-confirm.component';
import {WareTransDispatchEditComponent} from './ware-trans/ware-trans-dispatch-edit/ware-trans-dispatch-edit.component';
import {WareReturnReceiveComponent} from './ware-return/ware-return-receive/ware-return-receive.component';


const routes: Routes = [
  {
    path: 'ware-trans-config',
    component: TransComponent,
    data:{ name:'转库管理'},
    children: [
      {path: '', redirectTo: 'ware-trans', pathMatch: 'full'},
      {path: 'ware-trans', component: WareTransComponent, data: {name: '物料库存调度'}},
      {path: 'ware-trans-detail', component: WareTransDetailComponent, data: {name: '物料库存调度详情'}},
      {path: 'quick-transfer', component: QuickTransferComponent, data: {name: '物料快速转库'}},
      {path: 'quick-transfer-detail', component: QuickTransferDetailComponent, data: {name: '物料快速转库详情'}},
      {path: 'ware-return', component: WareReturnComponent, data: {name: '待修物料返还'}},
      {path: 'ware-return-detail', component: WareReturnDetailComponent, data: {name: '待修物料返还详情'}},
      {path: 'ware-return-receive', component: WareReturnReceiveComponent, data: {name: '待修物料返还收货'}},
      {path: 'ware-trans-apply', component: WareTransApplyComponent, data: {name: '库存调度申请'}},
      {path: 'ware-trans-edit', component: WareTransEditComponent, data: {name: '库存调度编辑'}},
      {path: 'ware-trans-dispatch', component: WareTransDispatchComponent, data: {name: '新建发货单'}},
      {path: 'ware-trans-audit', component: WareTransAuditComponent, data: {name: '批量审批'}},
      {path: 'to-be-received-list', component: ToBeReceivedListComponent, data: {name: '待收货列表'}},
      {path: 'ware-trans-receive', component: WareTransReceiveComponent, data: {name: '物流信息'}},
      {path: 'quick-transfer-apply', component: QuickTransferApplyComponent, data: {name: '快速转库申请'}},
      {path: 'quick-transfer-confirm', component: QuickTransferConfirmComponent, data: {name: '入库确认'}},
      {path: 'ware-return-apply', component: WareReturnApplyComponent, data: {name: '待修物料返还申请'}},
      {path: 'ware-return-confirm', component: WareReturnConfirmComponent, data: {name: '待修物料返还申请'}},
      {path: 'return-to-be-received-list', component: ReturnToBeReceivedListComponent, data: {name: '待收货返回列表'}},
      {path: 'ware-trans-dispatch-edit', component: WareTransDispatchEditComponent, data: {name: '编辑物流信息'}},
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TransSettingRoutingModule {
}
