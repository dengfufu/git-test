import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {AnyfixRoutingModule} from './anyfix-routing.module';
import {WorkListComponent} from './work/work-list/work-list.component';
import {WorkAddComponent} from './add/work-add.component';
import {ToAssignComponent} from './assign/to-assign/to-assign.component';
import {WorkDetailComponent} from './work/work-detail/work-detail.component';
import {SelectEngineerComponent} from './assign/select-engineer/select-engineer.component';
import {RecallAssignComponent} from './assign/recall-asssign/recall-assign.component';
import {DispatchComponent} from './dispatch/dispatch.component';
import {ServiceReturnComponent} from './handle/service-return/service-return.component';
import {SelectBranchComponent} from './handle/select-branch/select-branch.component';
import {CustomRecallComponent} from './handle/custom-recall/custom-recall.component';
import {WorkHistoryComponent} from './work/work-history/work-history.component';
import {WorkListAtmcaseComponent} from './work/work-list-atmcase/work-list-atmcase.component';
import {WorkDetailAtmcaseComponent} from './work/work-detail-atmcase/work-detail-atmcase.component';
import {WorkFeeEditComponent} from './work/work-detail/work-fee-edit/work-fee-edit.component';
import { UsedPartEditComponent } from './work/work-detail/used-part-edit/used-part-edit.component';
import { WorkPostEditComponent } from './work/work-detail/work-post-edit/work-post-edit.component';
import { RecyclePartEditComponent } from './work/work-detail/recycle-part-edit/recycle-part-edit.component';
import {WorkEditComponent} from './edit/work-edit.component';
import {ImportWorkComponent} from './import/import-work.component';
import {DatePipe} from '@angular/common';
import { ServiceCheckListComponent } from './work/service-check-list/service-check-list.component';
import { ServiceCheckComponent } from './work/work-detail/work-check/service-check.component';
import {WorkConfigModule} from '../setting/work-config/work-config.module';
import {SettleModule} from './settle/settle.module';
import { WorkFeeVerifyListComponent } from './work/work-fee-verify/work-fee-verify-list.component';
import { WorkFeeVerifyAddComponent } from './work/work-fee-verify/add/work-fee-verify-add.component';
import { WorkFeeVerifyDetailComponent } from './work/work-fee-verify/detail/work-fee-verify-detail.component';
import { WorkFeeVerifyComponent } from './work/work-fee-verify/verify/work-fee-verify.component';
import { WorkFeeVerifyConfirmComponent } from './work/work-fee-verify/confirm/work-fee-verify-confirm.component';
import {SelectWorkComponent} from './work/work-fee-verify/select-work/select-work.component';
import {ServiceContentEditComponent} from './work/work-detail/service-content-edit/service-content-edit.component';
import {FileEditComponent} from './work/work-detail/file-edit/file-edit.component';
import {WorkSupportComponent} from './support/work-support.component';
import {WorkReviewListComponent} from './review/list/work-review-list.component';
import {WorkReviewAddComponent} from './review/add/work-review-add.component';
import {WorkSupportRecordAddComponent} from './support/add/work-support-record-add.component';
import {WorkFollowAddComponent} from './follow/work-follow-add.component';
import {WorkListNewComponent} from './work/work-list-new/work-list-new.component';
import {DelonABCModule} from '@delon/abc';
import {WorkRemindListComponent} from './work/work-remind-list/work-remind-list.component';
import {WorkRemindTimeEditComponent} from './work/work-remind-list/work-remind-time-edit/work-remind-time-edit.component';
import { WorkFeeVerifyModComponent } from './work/work-fee-verify/mod/work-fee-verify-mod.component';
import { WorkFeeDetailComponent } from './work/work-detail/work-fee-detail/work-fee-detail.component';
import {WorkListFilterComponent} from './work/work-list/filter/work-list-filter.component';
import {WorkHistoryFilterComponent} from './work/work-history/filter/work-history-filter.component';
import { FeeCheckListComponent } from './work/fee-check-list/fee-check-list.component';
import {FeeCheckComponent} from './work/work-detail/work-check/fee-check.component';
import {ServiceConfirmComponent} from './work/work-detail/work-confirm/service-confirm.component';
import {FeeConfirmComponent} from './work/work-detail/work-confirm/fee-confirm.component';
import { WorkFeeAddComponent } from './work/work-detail/work-fee-add/work-fee-add.component';
import {ServiceConfirmListComponent} from './work/service-confirm-list/service-confirm-list.component';
import {FeeConfirmListComponent} from './work/fee-confirm-list/fee-confirm-list.component';
import { ImportVerifyComponent } from './work/work-fee-verify/import-verify/import-verify.component';
import {ReturnAssignServiceComponent} from './assign/return-assign-service/return-assign-service.component';
import { ServiceCheckListFilterComponent } from './work/service-check-list/filter/service-check-list-filter.component';
import { FeeCheckListFilterComponent } from './work/fee-check-list/filter/fee-check-list-filter.component';
import {ServiceConfirmListFilterComponent} from './work/service-confirm-list/filter/service-confirm-list-filter.component';
import { FeeConfirmListFilterComponent } from './work/fee-confirm-list/filter/fee-confirm-list-filter.component';

@NgModule({
  imports: [SharedModule, AnyfixRoutingModule, WorkConfigModule, SettleModule, DelonABCModule],
  exports: [],
  declarations: [
    WorkListComponent,
    WorkListNewComponent,
    WorkHistoryComponent,
    WorkAddComponent,
    WorkEditComponent,
    WorkListFilterComponent,
    WorkHistoryFilterComponent,
    ToAssignComponent,
    WorkDetailComponent,
    SelectEngineerComponent,
    RecallAssignComponent,
    DispatchComponent,
    ServiceReturnComponent,
    ReturnAssignServiceComponent,
    SelectBranchComponent,
    CustomRecallComponent,
    WorkFeeEditComponent,
    UsedPartEditComponent,
    WorkPostEditComponent,
    RecyclePartEditComponent,
    RecyclePartEditComponent,
    WorkListAtmcaseComponent,
    WorkDetailAtmcaseComponent,
    ImportWorkComponent,
    FileEditComponent,
    ServiceContentEditComponent,
    ServiceCheckListComponent,
    ServiceCheckListFilterComponent,
    FeeCheckListComponent,
    FeeCheckListFilterComponent,
    ServiceCheckComponent,
    ServiceConfirmListComponent,
    ServiceConfirmListFilterComponent,
    FeeConfirmListComponent,
    FeeConfirmListFilterComponent,
    WorkFeeVerifyListComponent,
    WorkFeeVerifyComponent,
    WorkFeeVerifyAddComponent,
    WorkFeeVerifyDetailComponent,
    WorkFeeVerifyConfirmComponent,
    SelectWorkComponent,
    WorkFeeVerifyModComponent,
    SelectWorkComponent,
    ServiceCheckComponent,
    FeeCheckComponent,
    ServiceConfirmComponent,
    FeeConfirmComponent,
    WorkSupportComponent,
    WorkSupportRecordAddComponent,
    WorkFollowAddComponent,
    WorkReviewListComponent,
    WorkReviewAddComponent,
    WorkRemindListComponent,
    WorkRemindTimeEditComponent,
    WorkFeeDetailComponent,
    WorkFeeAddComponent,
    ImportVerifyComponent
  ],
  entryComponents: [
    WorkEditComponent,
    WorkFeeEditComponent,
    UsedPartEditComponent,
    WorkPostEditComponent,
    RecyclePartEditComponent,
    ImportWorkComponent,
    ServiceCheckComponent,
    WorkFeeVerifyComponent,
    WorkFeeVerifyAddComponent,
    WorkFeeVerifyModComponent,
    WorkFeeVerifyConfirmComponent,
    SelectWorkComponent,
    FileEditComponent,
    ServiceContentEditComponent,
    ServiceCheckComponent,
    FeeCheckComponent,
    ServiceConfirmComponent,
    FeeConfirmComponent,
    WorkSupportComponent,
    WorkSupportRecordAddComponent,
    WorkFollowAddComponent,
    WorkReviewAddComponent,
    WorkRemindTimeEditComponent,
    WorkFeeAddComponent,
    ImportVerifyComponent
  ],
  providers: [DatePipe]
})
export class AnyfixModule {
}
