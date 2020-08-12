import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {FlowNodeEditComponent} from './flow-template-add/flow-node-edit/flow-node-edit.component';
import {FlowNodeHandlerListComponent} from './flow-template-add/flow-node-handler-list/flow-node-handler-list.component';
import {FlowNodeHandlerEditComponent} from './flow-template-add/flow-node-handler-edit/flow-node-handler-edit.component';
import {FlowTemplateListComponent} from './flow-template-list/flow-template-list.component';
import {FlowTemplateAddComponent} from './flow-template-add/flow-template-add.component';
import {FlowTemplateEditComponent} from './flow-template-edit/flow-template-edit.component';
import {FlowTemplateCopyComponent} from './flow-template-copy/flow-template-copy.component';
import {FlowTypeComponent} from './flow-template-add/flow-type/flow-type.component';

const routes: Routes = [
  {path: '', redirectTo: 'flow-template-list', pathMatch: 'full'},
  {path: 'flow-template-list', component: FlowTemplateListComponent, data: {name: '流程模板列表'}},
  {path: 'flow-template-add', component: FlowTemplateAddComponent, data: {name: '流程模板添加'}},
  {path: 'flow-template-edit', component: FlowTemplateEditComponent, data: {name: '模板维护'}},
  {path: 'flow-template-copy', component: FlowTemplateCopyComponent, data: {name: '模板复制'}},
  {path: 'flow-type', component: FlowTypeComponent, data: {name: '选择流程类型'}},
  {path: 'flow-node-edit', component: FlowNodeEditComponent, data: {name: '编辑流程节点'}},
  {path: 'flow-node-handler-edit', component: FlowNodeHandlerEditComponent, data: {name: '编辑流程节点'}},
  {path: 'flow-node-handler-list', component: FlowNodeHandlerListComponent, data: {name: '编辑流程节点'}},
]

@NgModule({
  imports: [RouterModule.forChild(routes)]
})
export class FlowTemplateRoutingModule{

}
