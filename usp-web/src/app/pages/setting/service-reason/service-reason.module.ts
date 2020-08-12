import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {ServiceReasonRoutingModule} from './service-reason-routing.module';
import {ServiceReasonListComponent} from './reason/service-reason-list.component';
import {ServiceReasonAddComponent} from './reason/service-reason-add.component';
import {ServiceReasonEditComponent} from './reason/service-reason-edit.component';
import {ServiceReasonComponent} from './service-reason.component';
import {CustomReasonListComponent} from '../custom-reason/reason/custom-reason-list.component';
import {CustomReasonAddComponent} from '../custom-reason/reason/custom-reason-add.component';
import {CustomReasonEditComponent} from '../custom-reason/reason/custom-reason-edit.component';
import {WorkFeeRuleAddComponent} from '../work-config/work-fee-rule/work-fee-rule-add.component';


@NgModule({
  imports: [SharedModule, ServiceReasonRoutingModule],
  exports: [],
  declarations: [
    ServiceReasonComponent,
    ServiceReasonListComponent,
    ServiceReasonAddComponent,
    ServiceReasonEditComponent,
    CustomReasonAddComponent,
    CustomReasonEditComponent,
    CustomReasonListComponent,
  ],
  entryComponents: [
    ServiceReasonAddComponent, ServiceReasonEditComponent, CustomReasonAddComponent, CustomReasonEditComponent]
})
export class ServiceReasonModule {
}
