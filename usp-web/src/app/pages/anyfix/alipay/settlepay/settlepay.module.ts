import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {SettlepayRoutingModule} from './settlepay-routing.module';
import { SettleCustomComponent } from './settle-custom/settle-custom.component';
import { SettleBranchComponent } from './settle-branch/settle-branch.component';
import { SettleStaffComponent } from './settle-staff/settle-staff.component';
import {NzDrawerModule} from 'ng-zorro-antd';
import { SettleCustomAddComponent } from './settle-custom/settle-custom-add/settle-custom-add.component';
import { SettleCustomDetailComponent } from './settle-custom/settle-custom-detail/settle-custom-detail.component';
import { SettleBranchDetailComponent } from './settle-branch/settle-branch-detail/settle-branch-detail.component';
import { SettleBranchAddComponent } from './settle-branch/settle-branch-add/settle-branch-add.component';
import { SettleStaffRecordComponent } from './settle-staff-record/settle-staff-record.component';
import { SettleStaffRecordAddComponent } from './settle-staff-record/settle-staff-record-add/settle-staff-record-add.component';
import { SettleStaffRecordDetailComponent } from './settle-staff-record/settle-staff-record-detail/settle-staff-record-detail.component';
import { ChooseStaffComponent } from './settle-staff-record/choose-staff/choose-staff.component';
import {SettlepayComponent} from './settlepay.component';
import { SettleStaffDetailComponent } from './settle-staff/settle-staff-detail/settle-staff-detail.component';
import { SettleDemanderComponent } from './settle-demander/settle-demander.component';
import { SettleDemanderAddComponent } from './settle-demander/settle-demander-add/settle-demander-add.component';
import { SettleWorkListComponent } from './settle-work-list/settle-work-list.component';
import { SettleDemanderDetailComponent } from './settle-demander/settle-demander-detail/settle-demander-detail.component';
import { SettleDemanderCheckComponent } from './settle-demander/settle-demander-check/settle-demander-check.component';
import { SettleDemanderReceiptComponent } from './settle-demander/settle-demander-receipt/settle-demander-receipt.component';
import { SettleDemanderInvoiceComponent } from './settle-demander/settle-demander-invoice/settle-demander-invoice.component';
import { SettleDemanderPayComponent } from './settle-demander/settle-demander-pay/settle-demander-pay.component';
import { SelectBankAccountComponent } from './settle-demander/select-bank-account/select-bank-account.component';
import { SettleDemanderModComponent } from './settle-demander/settle-demander-mod/settle-demander-mod.component';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { SelectWorkFeeVerifyComponent } from './settle-demander/select-work-fee-verify/select-work-fee-verify.component';
import {ServiceOfferComponent} from '../service-offer/service-offer.component';
import {WorkOrderComponent} from '../work-order/work-order.component';


@NgModule({
  imports: [SharedModule, SettlepayRoutingModule, NzDrawerModule, ScrollingModule],
  exports: [],
  declarations: [
    SettlepayComponent,
    SettleCustomComponent,
    SettleBranchComponent,
    SettleStaffComponent,
    SettleCustomAddComponent,
    SettleCustomDetailComponent,
    SettleBranchDetailComponent,
    SettleBranchAddComponent,
    SettleStaffRecordComponent,
    SettleStaffRecordAddComponent,
    SettleStaffRecordDetailComponent,
    ChooseStaffComponent,
    SettleStaffDetailComponent,
    SettleDemanderComponent,
    SettleDemanderAddComponent,
    SettleWorkListComponent,
    SettleDemanderDetailComponent,
    SettleDemanderCheckComponent,
    SettleDemanderReceiptComponent,
    SettleDemanderInvoiceComponent,
    SettleDemanderPayComponent,
    SelectBankAccountComponent,
    SettleDemanderModComponent,
    SelectWorkFeeVerifyComponent,
    ServiceOfferComponent,
    WorkOrderComponent
  ],
  entryComponents: [SettleCustomAddComponent,
    SettleCustomDetailComponent,
    SettleBranchAddComponent,
    SettleBranchDetailComponent,
    ChooseStaffComponent,
    SettleStaffRecordDetailComponent,
    SettleStaffRecordAddComponent,
    SettleStaffDetailComponent,
    SettleDemanderAddComponent,
    SettleWorkListComponent,
    SettleDemanderCheckComponent,
    SettleDemanderReceiptComponent,
    SettleDemanderInvoiceComponent,
    SettleDemanderPayComponent,
    SelectBankAccountComponent,
    SettleDemanderModComponent,
    SelectWorkFeeVerifyComponent
  ]
})
export class SettlepayModule {
}
