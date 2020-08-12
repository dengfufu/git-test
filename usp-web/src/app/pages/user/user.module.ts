import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {ForgetComponent} from './forget/forget.component';


const COMPONENTS = [
  LoginComponent,
  RegisterComponent,
  ForgetComponent
];

@NgModule({
  imports: [SharedModule],
  exports: [...COMPONENTS],
  declarations: [...COMPONENTS],
})
export class UserModule {
}
