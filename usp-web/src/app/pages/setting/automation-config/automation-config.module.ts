import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {AutomationConfigRoutingModule} from './automation-config-routing.module';
import {DispatchServiceCorpAddComponent} from './dispatch-service-corp/dispatch-service-corp-add.component';
import {DispatchServiceCorpListComponent} from './dispatch-service-corp/dispatch-service-corp-list.component';
import {AutomationConfigComponent} from './automation-config.component';
import {AutomationConfigService} from './automation-config.service';
import {DispatchServiceBranchListComponent} from './dispatch-service-branch/dispatch-service-branch-list.component';
import {DispatchServiceBranchAddComponent} from './dispatch-service-branch/dispatch-service-branch-add.component';
import {AssignModeAddComponent} from './assign-mode/assign-mode-add.component';
import {AssignModeListComponent} from './assign-mode/assign-mode-list.component';


@NgModule({
  imports: [SharedModule, AutomationConfigRoutingModule],
  exports: [],
  declarations: [
    AutomationConfigComponent,
    DispatchServiceCorpListComponent,
    DispatchServiceCorpAddComponent,
    DispatchServiceBranchListComponent,
    DispatchServiceBranchAddComponent,
    AssignModeListComponent,
    AssignModeAddComponent

  ],
  providers:[
    AutomationConfigService
  ],
  entryComponents: [
    DispatchServiceCorpAddComponent,
    DispatchServiceBranchAddComponent,
    AssignModeAddComponent
  ]
})
export class AutomationConfigModule {
}
