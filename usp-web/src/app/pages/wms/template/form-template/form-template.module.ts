import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {FormTemplateListComponent} from './form-template-list/form-template-list.component';
import {FormTemplateAddComponent} from './form-template-add/form-template-add.component';
import {FormTemplateEditComponent} from './form-template-edit/form-template-edit.component';
import {FormTemplateFieldEditComponent} from './form-template-field-edit/form-template-field-edit.component';
import {FormTemplateFieldComponent} from './form-template-field/form-template-field.component';
import {FormTemplateRoutingModule} from './form-template-routing.module';

const Components = [
  FormTemplateListComponent,
  FormTemplateAddComponent,
  FormTemplateEditComponent,
  FormTemplateFieldComponent,
  FormTemplateFieldEditComponent
]

const EntryComponents = [
  FormTemplateListComponent,
  FormTemplateAddComponent,
  FormTemplateEditComponent,
  FormTemplateFieldComponent,
  FormTemplateFieldEditComponent
]

@NgModule({
  imports: [SharedModule, FormTemplateRoutingModule],
  exports: [],
  declarations: [...Components, ...EntryComponents],
  entryComponents: [...EntryComponents],
  providers: []
})
export class FormTemplateModule{}
