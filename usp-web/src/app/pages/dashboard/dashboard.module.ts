import {NgModule} from '@angular/core';
import {DashboardRoutingModule} from './dashboard-routing.module';
import {WorkplaceComponent} from './workplace/workplace.component';
import {SharedModule} from '@shared/shared.module';
import {AnalysisComponent} from './analysis/analysis.component';
import {WorkplaceModule} from './workplace/workplace.module';


@NgModule({
  imports: [
    SharedModule,
    DashboardRoutingModule,
    WorkplaceModule
  ],
  exports: [],
  declarations: [
    WorkplaceComponent,
    AnalysisComponent,
  ]
})
export class DashboardModule {
}
