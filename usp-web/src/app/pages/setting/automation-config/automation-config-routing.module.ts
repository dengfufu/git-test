import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {AutomationConfigComponent} from './automation-config.component';
import {DispatchServiceCorpListComponent} from './dispatch-service-corp/dispatch-service-corp-list.component';
import {DispatchServiceBranchListComponent} from './dispatch-service-branch/dispatch-service-branch-list.component';
import {AssignModeListComponent} from './assign-mode/assign-mode-list.component';

const routes: Routes = [
  {
    path: '',
    component: AutomationConfigComponent,
    data: {name: '自动化设置'},
    children: [
      {path: 'dispatch-service-corp', component: DispatchServiceCorpListComponent, data: {name: '工单提交服务商'}},
      {path: 'dispatch-service-branch', component: DispatchServiceBranchListComponent, data: {name: '工单分配服务网点'}},
      {path: 'assign-mode', component: AssignModeListComponent, data: {name: '派单模式设置'}}
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AutomationConfigRoutingModule {
}
