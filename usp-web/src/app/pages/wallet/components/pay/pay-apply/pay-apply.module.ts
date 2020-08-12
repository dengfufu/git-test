import {NgModule} from '@angular/core';

import {PayApplyService} from './pay-apply.service';
import {SharedModule} from '../../../../../shared/shared.module';
import {PayApplyAddComponent} from './add/pay-apply-add.component';
import {PayApplyViewComponent} from './view/pay-apply-view.component';
import {PayApplyDetailComponent} from './detail/pay-apply-detail.component';

@NgModule({
  imports: [SharedModule],
  exports: [
    PayApplyViewComponent
  ],
  declarations: [
    PayApplyAddComponent,
    PayApplyViewComponent,
    PayApplyDetailComponent],
  entryComponents: [
    PayApplyAddComponent,
    PayApplyViewComponent,
    PayApplyDetailComponent
  ],
  providers: [PayApplyService],
})
export class PayApplyModule {
}
