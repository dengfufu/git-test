import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {FlowNodeEditComponent} from './flow-template-add/flow-node-edit/flow-node-edit.component';
import {FlowNodeHandlerListComponent} from './flow-template-add/flow-node-handler-list/flow-node-handler-list.component';
import {FlowNodeHandlerEditComponent} from './flow-template-add/flow-node-handler-edit/flow-node-handler-edit.component';
import {FlowTemplateListComponent} from './flow-template-list/flow-template-list.component';
import {FlowTemplateAddComponent} from './flow-template-add/flow-template-add.component';
import {FlowTemplateEditComponent} from './flow-template-edit/flow-template-edit.component';
import {FlowTemplateCopyComponent} from './flow-template-copy/flow-template-copy.component';
import {FlowTypeComponent} from './flow-template-add/flow-type/flow-type.component';
import {FlowTemplateRoutingModule} from './flow-template-routing.module';

const Components = [
  FlowTemplateListComponent,
  FlowTemplateAddComponent,
  FlowTemplateEditComponent,
  FlowTemplateCopyComponent,
  FlowTypeComponent,
  FlowNodeEditComponent,
  FlowNodeHandlerListComponent,
  FlowNodeHandlerEditComponent,
]

const EntryComponents = [
  FlowTemplateListComponent,
  FlowTemplateAddComponent,
  FlowTemplateEditComponent,
  FlowTemplateCopyComponent,
  FlowTypeComponent,
  FlowNodeEditComponent,
  FlowNodeHandlerListComponent,
  FlowNodeHandlerEditComponent,
]

@NgModule({
  imports: [SharedModule, FlowTemplateRoutingModule],
  exports: [],
  declarations: [...Components, ...EntryComponents],
  entryComponents: [...EntryComponents],
  providers: []
})
export class FlowTemplateModule{}
