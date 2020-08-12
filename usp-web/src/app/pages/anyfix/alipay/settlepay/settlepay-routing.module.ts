import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {SettleBranchComponent} from './settle-branch/settle-branch.component';
import {SettleCustomComponent} from './settle-custom/settle-custom.component';
import {SettleStaffComponent} from './settle-staff/settle-staff.component';
import {SettleStaffRecordComponent} from './settle-staff-record/settle-staff-record.component';
import {SettlepayComponent} from './settlepay.component';
import {SettleDemanderComponent} from './settle-demander/settle-demander.component';
import {SettleDemanderDetailComponent} from './settle-demander/settle-demander-detail/settle-demander-detail.component';
import {ServiceOfferComponent} from '../service-offer/service-offer.component';
import {WorkOrderComponent} from '../work-order/work-order.component';

const routes: Routes = [
  {
    path: '',
    component: SettlepayComponent,
    children: [
      {path: '', redirectTo: 'service-offer', pathMatch: 'full'},
      {path: 'settle-demander-detail', component: SettleDemanderDetailComponent, data: {name: '结算详情'}},
      {path: 'settle-custom', component: SettleCustomComponent, data: {name: '客户结算'}},
      {path: 'settle-branch', component: SettleBranchComponent, data: {name: '网点结算'}},
      {path: 'settle-staff-record', component: SettleStaffRecordComponent, data: {name: '员工结算记录'}},
      {path: 'settle-staff', component: SettleStaffComponent, data: {name: '员工结算'}},
      {path: 'service-offer', component: ServiceOfferComponent, data: {name: '服务报价'}},
      {path: 'work-order', component: WorkOrderComponent, data: {name: '我的工单'}},
      {path: 'settle-demander', component: SettleDemanderComponent, data: {name: '待支付工单'}},
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SettlepayRoutingModule {
}
