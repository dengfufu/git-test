import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {WorkTypeListComponent} from './work-type/work-type-list.component';
import {WorkTypeAddComponent} from './work-type/work-type-add.component';
import {WorkConfigComponent} from './work-config.component';
import {WorkConfigRoutingModule} from './work-config-routing.module';
import {WorkTypeEditComponent} from './work-type/work-type-edit.component';
import {FaultTypeListComponent} from './fault-type/fault-type-list.component';
import {FaultTypeAddComponent} from './fault-type/fault-type-add.component';
import {FaultTypeEditComponent} from './fault-type/fault-type-edit.component';
import {RemoteWayAddComponent} from './remote-way/remote-way-add.component';
import {RemoteWayListComponent} from './remote-way/remote-way-list.component';
import {RemoteWayEditComponent} from './remote-way/remote-way-edit.component';
import {ServiceItemAddComponent} from './service-item/service-item-add.component';
import {ServiceItemEditComponent} from './service-item/service-item-edit.component';
import {ServiceItemListComponent} from './service-item/service-item-list.component';
import {ServiceEvaluateListComponent} from './service-evaluate/service-evaluate-list.component';
import {ServiceEvaluateAddComponent} from './service-evaluate/service-evaluate-add.component';
import {ServiceEvaluateEditComponent} from './service-evaluate/service-evaluate-edit.component';
import { WorkFeeRuleListComponent } from './work-fee-rule/work-fee-rule-list.component';
import { ServiceItemFeeRuleListComponent } from './service-item-fee-rule/service-item-fee-rule-list.component';
import { ServiceItemFeeRuleAddComponent } from './service-item-fee-rule/service-item-fee-rule-add.component';
import { WorkFeeRuleAddComponent } from './work-fee-rule/work-fee-rule-add.component';
import {AutomationConfigService} from '../automation-config/automation-config.service';
import { ImplementFeeComponent } from './implement-fee/implement-fee.component';
import { ImplementFeeAddComponent } from './implement-fee/implement-fee-add/implement-fee-add.component';
import { ImplementFeeViewComponent } from './implement-fee/implement-fee-view/implement-fee-view.component';
import { WorkFeeRuleViewComponent } from './work-fee-rule/work-fee-rule-view/work-fee-rule-view.component';
import {ConfigItemListComponent} from './config-item/config-item-list.component';
import {ConfigItemEditComponent} from './config-item/config-item-edit.component';


@NgModule({
  imports: [SharedModule, WorkConfigRoutingModule],
  exports: [],
  providers: [AutomationConfigService],
  declarations: [
    WorkTypeListComponent,
    WorkTypeAddComponent,
    WorkTypeEditComponent,
    FaultTypeListComponent,
    FaultTypeAddComponent,
    FaultTypeEditComponent,
    WorkConfigComponent,
    RemoteWayAddComponent,
    RemoteWayListComponent,
    RemoteWayEditComponent,
    ServiceItemAddComponent,
    ServiceItemEditComponent,
    ServiceItemListComponent,
    ServiceEvaluateListComponent,
    ServiceEvaluateAddComponent,
    ServiceEvaluateEditComponent,
    WorkFeeRuleListComponent,
    ServiceItemFeeRuleListComponent,
    ServiceItemFeeRuleAddComponent,
    WorkFeeRuleAddComponent,
    ConfigItemListComponent,
    ConfigItemEditComponent,
    WorkFeeRuleAddComponent,
    ImplementFeeComponent,
    ImplementFeeAddComponent,
    WorkFeeRuleViewComponent,
    ImplementFeeViewComponent
  ],
  entryComponents: [
    WorkTypeAddComponent, WorkTypeEditComponent,
    FaultTypeEditComponent, FaultTypeAddComponent,
    RemoteWayAddComponent, RemoteWayEditComponent,
    ServiceItemAddComponent, ServiceItemEditComponent,
    ServiceEvaluateAddComponent, ServiceEvaluateEditComponent,
    WorkFeeRuleAddComponent, ServiceItemFeeRuleAddComponent,
    ImplementFeeAddComponent,WorkFeeRuleViewComponent,
    ImplementFeeViewComponent,
    ConfigItemEditComponent

  ]
})
export class WorkConfigModule {
}
