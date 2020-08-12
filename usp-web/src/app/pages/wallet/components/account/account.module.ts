import { NgModule } from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {AccountActiveComponent} from './account-active/account-active.component';
import {AccountTraceComponent} from './account-trace/account-trace.component';

const components = [
  AccountActiveComponent,
  AccountTraceComponent
]

@NgModule({
  imports: [
    SharedModule
  ],
  declarations: [
    ...components
  ],
  exports: [
    ...components
  ]
})
export class AccountModule { }
