import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {WmsOutcomeRoutingModule} from './wms-outcome-routing.module';
import {WareOutcomeListComponent} from './ware-outcome-list/ware-outcome-list.component';
import {WareOutcomeAddComponent} from './ware-outcome-add/ware-outcome-add.component';
import {WareOutcomeAuditComponent} from './ware-outcome-audit/ware-outcome-audit.component';
import {WareOutcomeDetailComponent} from './ware-outcome-detail/ware-outcome-detail.component';
import {OutcomePartAddComponent} from './ware-outcome-add/outcome-part-add/outcome-part-add.component';
import {WareOutcomeConsignComponent} from './ware-outcome-consign/ware-outcome-consign.component';
import {ConsignPartAddComponent} from './ware-outcome-consign/consign-part-add/consign-part-add.component';
import {WareOutcomeBatchAuditComponent} from './ware-outcome-audit/ware-outcome-batch-audit/ware-outcome-batch-audit.component';

const Components = [
  WareOutcomeListComponent,
  WareOutcomeAddComponent,
  WareOutcomeDetailComponent,
  WareOutcomeConsignComponent,
  WareOutcomeBatchAuditComponent
]

const EntryComponents = [
  OutcomePartAddComponent,
  WareOutcomeAuditComponent,
  ConsignPartAddComponent
]

@NgModule({
  imports: [SharedModule, WmsOutcomeRoutingModule],
  exports: [],
  declarations: [...Components, ...EntryComponents],
  entryComponents: [...EntryComponents],
  providers: []
})
export class WmsOutcomeModule{}
