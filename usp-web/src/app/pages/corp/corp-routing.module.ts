import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {DemanderServiceListComponent} from './demander-service/demander-service-list.component';
import {ServiceDemanderListComponent} from './service-demander/service-demander-list.component';
import {DemanderCustomListComponent} from './demander-custom/demander-custom-list.component';
import {DemanderCustomDetailComponent} from './demander-custom/demander-custom-detail.component';
import {BranchListComponent} from './service-branch/list/branch-list.component';
import {DemanderDetailComponent} from './service-demander/detail/demander-detail.component';


const routes: Routes = [
  {path: '', redirectTo: 'corp-manage', pathMatch: 'full'},
  {path: 'demander-custom', component: DemanderCustomListComponent, data: {name: '客户档案'}},
  {path: 'demander-custom/detail', component: DemanderCustomDetailComponent, data: {name: '客户档案详情'}},
  {path: 'demander-service', component: DemanderServiceListComponent, data: {name: '服务商档案'}},
  {path: 'service-demander', component: ServiceDemanderListComponent, data: {name: '委托商档案'}},
  {path: 'service-demander/detail', component: DemanderDetailComponent, data: {name: '委托商档案详情'}},
  {path: 'service-branch', component: BranchListComponent, data: {name: '服务网点'}}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CorpRoutingModule {
}
