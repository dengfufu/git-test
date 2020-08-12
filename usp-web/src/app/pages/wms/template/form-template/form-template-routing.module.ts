import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {FormTemplateListComponent} from './form-template-list/form-template-list.component';
import {FormTemplateAddComponent} from './form-template-add/form-template-add.component';
import {FormTemplateEditComponent} from './form-template-edit/form-template-edit.component';
import {FormTemplateFieldComponent} from './form-template-field/form-template-field.component';
import {FormTemplateFieldEditComponent} from './form-template-field-edit/form-template-field-edit.component';


const routes: Routes = [
  {path: '', redirectTo: 'form-template-list', pathMatch: 'full'},
  {path: 'form-template-list', component: FormTemplateListComponent, data: {name: '表单模板管理'}},
  {path: 'form-template-add', component: FormTemplateAddComponent, data: {name: '表单模板添加'}},
  {path: 'form-template-edit', component: FormTemplateEditComponent, data: {name: '表单模板编辑'}},
  {path: 'flow-node-handler-list', component: FormTemplateFieldComponent, data: {name: '表单模板字段维护'}},
  {path: 'flow-node-handler-list-edit', component: FormTemplateFieldEditComponent, data: {name: '表单模板字段编辑'}},
]

@NgModule({
  imports: [RouterModule.forChild(routes)]
})
export class FormTemplateRoutingModule{

}
