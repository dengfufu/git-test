import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {ServiceReasonListComponent} from './reason/service-reason-list.component';
import {ServiceReasonComponent} from './service-reason.component';
import {CustomReasonListComponent} from '../custom-reason/reason/custom-reason-list.component';

const routes: Routes = [
  {
    path: '', component: ServiceReasonComponent, data: {name: '服务选项'},
    children: [
      {
        path: 'custom-reason', component: CustomReasonListComponent,
        data: {name: '客户撤单原因', reasonType: 1}
      },
      {path: 'service-return-reason', component: ServiceReasonListComponent, data: {name: '客服退单原因', reasonType: 1}},
      {path: 'service-recall-reason', component: ServiceReasonListComponent, data: {name: '客服撤回派单原因', reasonType: 2}},
      {path: 'engineer-refuse-reason', component: ServiceReasonListComponent, data: {name: '工程师拒绝派单原因', reasonType: 3}},
      {path: 'engineer-return-reason', component: ServiceReasonListComponent, data: {name: '工程师退回派单原因', reasonType: 4}}
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes),
    FormsModule],
  exports: [RouterModule]
})
export class ServiceReasonRoutingModule {
}
