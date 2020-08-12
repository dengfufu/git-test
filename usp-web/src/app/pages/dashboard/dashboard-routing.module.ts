import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {WorkplaceComponent} from './workplace/workplace.component';
import {AnalysisComponent} from './analysis/analysis.component';

const routes: Routes = [
  {path: '', redirectTo: 'workplace', pathMatch: 'full'},
  {path: 'workplace', component: WorkplaceComponent, data: {name: '工作台'}},
  {path: 'analysis', component: AnalysisComponent, data: {name: '统计分析'}},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DashboardRoutingModule {
}
