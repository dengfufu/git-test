import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {WmsRoutingModule} from './wms-routing.module';
import {WmsSettingModule} from './setting/wms-setting.module';
import {WmsOutcomeModule} from './outcome/wms-outcome.module';
import {TransSettingModule} from './trans/trans-setting.module';
import {WmsIncomeModule} from './income/wms-income.module';
import {FormTemplateModule} from './template/form-template/form-template.module';
import {FlowTemplateModule} from './template/flow-template/flow-template.module';

const Components = [
];

const EntryComponents = [
];

@NgModule({
  imports: [SharedModule, WmsRoutingModule, WmsSettingModule,WmsOutcomeModule, WmsIncomeModule,TransSettingModule,FormTemplateModule,
    FlowTemplateModule],
  exports: [],
  declarations: [...Components, ...EntryComponents],
  providers: [],
  entryComponents: [...EntryComponents]
})
export class WmsModule {
}
