import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {WorkTypeListComponent} from './work-type/work-type-list.component';
import {WorkConfigComponent} from './work-config.component';
import {FaultTypeListComponent} from './fault-type/fault-type-list.component';
import {RemoteWayListComponent} from './remote-way/remote-way-list.component';
import {ServiceItemListComponent} from './service-item/service-item-list.component';
import {ServiceEvaluateListComponent} from './service-evaluate/service-evaluate-list.component';
import {FormsModule} from '@angular/forms';
import {WorkFeeRuleListComponent} from './work-fee-rule/work-fee-rule-list.component';
import {ImplementFeeComponent} from './implement-fee/implement-fee.component';
import {ServiceItemFeeRuleListComponent} from './service-item-fee-rule/service-item-fee-rule-list.component';
import {ConfigItemListComponent} from './config-item/config-item-list.component';

const routes: Routes = [
  {
    path: '', component: WorkConfigComponent, data: {name: '工单设置'},
    children: [
      {
        path: 'work-type', component: WorkTypeListComponent,
        data: {name: '工单类型'}
      },
      {
        path: 'remote-way', component: RemoteWayListComponent,
        data: {name: '远程处理方式'}
      },
      {
        path: 'service-item', component: ServiceItemListComponent,
        data: {name: '服务项目'}
      },
      {
        path: 'fault-type', component: FaultTypeListComponent,
        data: {name: '故障现象'}
      },
      {
        path: 'service-evaluate', component: ServiceEvaluateListComponent,
        data: {name: '服务评价指标'}
      },
      {
        path: 'work-assort-fee', component: WorkFeeRuleListComponent,
        data: {name: '工单收费'}
      },
      // {
      //   path: 'service-item-fee-rule', component: ServiceItemFeeRuleListComponent,
      //   data: {name: '服务项目费用规则'}
      // }
      {
        path: 'work-implement-fee', component: ImplementFeeComponent,
        data: {name: '工单支出费用'}
      },
      {
        path: 'config-item', component: ConfigItemListComponent,
        data: {name: '数据项配置'}
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes),
    FormsModule],
  exports: [RouterModule]
})
export class WorkConfigRoutingModule {
}
