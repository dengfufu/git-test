import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {WareIncomeListComponent} from './ware-income-list/ware-income-list.component';
import {WmsIncomeRoutingModule} from './wms-income-routing.module';
import { IncomeBaseAddComponent } from './ware-income-batch-add/income-base-add/income-base-add.component';
import { WareIncomeViewComponent } from './ware-income-view/ware-income-view.component';
import {WareIncomeBatchAddComponent} from './ware-income-batch-add/ware-income-batch-add.component';
import { WareIncomeBatchAuditComponent } from './ware-income-batch-audit/ware-income-batch-audit.component';
import {DatePipe} from '@angular/common';
import { ChooseReverseDetailComponent } from './ware-income-add/choose-reverse-detail/choose-reverse-detail.component';
import { WareIncomeAddComponent } from './ware-income-add/ware-income-add.component';
import { WareIncomeAuditComponent } from './ware-income-audit/ware-income-audit.component';

const Components = [
  WareIncomeListComponent,
  WareIncomeBatchAddComponent,
  WareIncomeViewComponent,
  WareIncomeBatchAuditComponent,
  WareIncomeAddComponent,
  WareIncomeAuditComponent
]

const EntryComponents = [
  IncomeBaseAddComponent,
  ChooseReverseDetailComponent
]

@NgModule({
  imports: [SharedModule, WmsIncomeRoutingModule],
  exports: [],
  declarations: [...Components, ...EntryComponents],
  entryComponents: [...EntryComponents],
  providers: [DatePipe]
})
export class WmsIncomeModule{}
