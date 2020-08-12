import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {CustomReasonRoutingModule} from './custom-reason-routing.module';
// import {CustomReasonListComponent} from './reason/custom-reason-list.component';
// import {CustomReasonAddComponent} from './reason/custom-reason-add.component';
import {CustomReasonEditComponent} from './reason/custom-reason-edit.component';


@NgModule({
  imports: [SharedModule, CustomReasonRoutingModule],
  exports: [],
  declarations: [
    // CustomReasonListComponent,
    // CustomReasonAddComponent,
    // CustomReasonEditComponent,
  ],
  // entryComponents: [
  //   CustomReasonAddComponent, CustomReasonEditComponent]
})
export class CustomReasonModule {
}
