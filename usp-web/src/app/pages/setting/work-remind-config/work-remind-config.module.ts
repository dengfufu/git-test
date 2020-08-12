import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {WorkRemindConfigRoutingModule} from './work-remind-config-routing.module';
import {AutomationConfigService} from '../automation-config/automation-config.service';
import {WorkRemindConfigListComponent} from './list/work-remind-config-list.component';
import {WorkRemindAddComponent} from './add/work-remind-add.component';
import {WorkRemindEditComponent} from './edit/work-remind-edit.component';
import {WorkRemindItemEditComponent} from './remind-item/work-remind-item-edit.component';


@NgModule({
  imports: [SharedModule, WorkRemindConfigRoutingModule],
  exports: [],
  providers: [AutomationConfigService],
  declarations: [
    WorkRemindConfigListComponent,
    WorkRemindAddComponent,
    WorkRemindEditComponent,
    WorkRemindItemEditComponent
  ],
  entryComponents: [
    WorkRemindAddComponent,
    WorkRemindEditComponent,
    WorkRemindItemEditComponent
  ]
})
export class WorkRemindConfigModule {
}
