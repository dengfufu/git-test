import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {WorkListComponent} from './work/work-list/work-list.component';
import {ToAssignComponent} from './assign/to-assign/to-assign.component';
import {WorkDetailComponent} from './work/work-detail/work-detail.component';
import {SelectEngineerComponent} from './assign/select-engineer/select-engineer.component';
import {RecallAssignComponent} from './assign/recall-asssign/recall-assign.component';
import {DispatchComponent} from './dispatch/dispatch.component';
import {ServiceReturnComponent} from './handle/service-return/service-return.component';
import {SelectBranchComponent} from './handle/select-branch/select-branch.component';
import {CustomRecallComponent} from './handle/custom-recall/custom-recall.component';
import {WorkHistoryComponent} from './work/work-history/work-history.component';
import {WorkListAtmcaseComponent} from './work/work-list-atmcase/work-list-atmcase.component';
import {WorkDetailAtmcaseComponent} from './work/work-detail-atmcase/work-detail-atmcase.component';
import {ServiceCheckListComponent} from './work/service-check-list/service-check-list.component';
import {WorkFeeVerifyListComponent} from './work/work-fee-verify/work-fee-verify-list.component';
import {WorkFeeVerifyDetailComponent} from './work/work-fee-verify/detail/work-fee-verify-detail.component';
import {WorkFeeVerifyComponent} from './work/work-fee-verify/verify/work-fee-verify.component';
import {WorkAddComponent} from './add/work-add.component';
import {WorkListNewComponent} from './work/work-list-new/work-list-new.component';
import {WorkRemindListComponent} from './work/work-remind-list/work-remind-list.component';
import {WorkEditComponent} from './edit/work-edit.component';
import {ReturnAssignServiceComponent} from './assign/return-assign-service/return-assign-service.component';
import {FeeCheckListComponent} from './work/fee-check-list/fee-check-list.component';
import {ServiceConfirmListComponent} from './work/service-confirm-list/service-confirm-list.component';
import {FeeConfirmListComponent} from './work/fee-confirm-list/fee-confirm-list.component';

const routes: Routes = [
  {path: '', component: WorkListComponent, pathMatch: 'full'},
  {path: 'work-list', component: WorkListComponent, data: {name: '工单处理'}},
  {path: 'work-list-new', component: WorkListNewComponent, data: {name: '工单处理(新)'}},
  {path: 'work-list-atmcase', component: WorkListAtmcaseComponent, data: {name: '金融设备工单'}},
  {path: 'work-remind-list', component: WorkRemindListComponent, data: {name: '工单预警'}},
  {path: 'work-history', component: WorkHistoryComponent, data: {name: '工单查询'}},
  {path: 'dispatch', component: DispatchComponent, data: {name: '委托商客服提单'}},
  {
    path: 'settle',
    data: {name: '结算管理'},
    loadChildren: () => import('./settle/settle.module').then(m => m.SettleModule)
  },
  {path: 'service-check-list', component: ServiceCheckListComponent, data: {name: '服务审核'}},
  {path: 'fee-check-list', component: FeeCheckListComponent, data: {name: '费用审核'}},
  {path: 'service-confirm-list', component: ServiceConfirmListComponent, data: {name: '服务确认'}},
  {path: 'fee-confirm-list', component: FeeConfirmListComponent, data: {name: '费用确认'}},
  {path: 'to-assign', component: ToAssignComponent, data: {name: '客服派单'}},
  {path: 'work-detail', component: WorkDetailComponent, data: {name: '工单详情'}},
  // {path: 'work-list/work-detail', component: WorkDetailComponent, data: {name: '工单详情'}},
  {path: 'work-list/add', component: WorkAddComponent, data: {name: '添加工单'}},
  {path: 'work-detail/edit', component: WorkEditComponent, data: {name: '编辑工单'}},
  // {path: 'work-history/work-detail', component: WorkDetailComponent, data: {name: '工单详情'}},
  {path: 'work-list-atmcase/work-detail-atmcase', component: WorkDetailAtmcaseComponent, data: {name: '工单详情'}},
  {path: 'select-engineer', component: SelectEngineerComponent, data: {name: '选择工程师'}},
  {path: 'recall-assign', component: RecallAssignComponent, data: {name: '撤回派单原因'}},
  {path: 'service-return', component: ServiceReturnComponent, data: {name: '客服退单原因'}},
  {path: 'custom-recall', component: CustomRecallComponent, data: {name: '客户撤单原因'}},
  {path: 'return-assign-service', component: ReturnAssignServiceComponent, data: {name: '派单主管退单原因'}},
  {path: 'select-branch', component: SelectBranchComponent, data: {name: '选择服务网点'}},
  {path: 'work-fee-verify', component: WorkFeeVerifyListComponent, data: {name: '工单对账'}},
  {path: 'work-fee-verify/verify', component: WorkFeeVerifyComponent, data: {name: '对账'}},
  {path: 'work-fee-verify/detail', component: WorkFeeVerifyDetailComponent, data: {name: '对账单明细'}},
  {
    path: 'goods-post', data: {name: '物品寄送'},
    loadChildren: () => import('./goods/goods.module').then(m => m.GoodsModule)
  },
  {
    path: 'pay',
    data: {name: '工单'},
    loadChildren: () => import('./alipay/settlepay/settlepay.module').then(m => m.SettlepayModule)
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AnyfixRoutingModule {
}
