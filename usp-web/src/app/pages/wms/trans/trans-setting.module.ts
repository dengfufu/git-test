import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {TransSettingRoutingModule} from './trans-setting-routing.module';
import {NzDrawerModule} from 'ng-zorro-antd';
import {TransComponent} from './trans.component';
import {WareTransComponent} from './ware-trans/ware-trans.component';
import {WareTransDetailComponent} from './ware-trans/ware-trans-detail/ware-trans-detail.component';
import {WareDeliveryAddComponent} from './ware-trans/ware-delivery-add/ware-delivery-add.component';
import {WareDeliveryEditComponent} from './ware-trans/ware-delivery-edit/ware-delivery-edit.component';
import {WareDeliveryInfoComponent} from './ware-trans/ware-delivery-info/ware-delivery-info.component';
import {QuickTransferComponent} from './quick-transfer/quick-transfer.component';
import {QuickTransferDetailComponent} from './quick-transfer/quick-transfer-detail/quick-transfer-detail.component';
import {WareReceiveAddComponent} from './ware-trans/ware-receive-add/ware-receive-add.component';
import {WareReturnComponent} from './ware-return/ware-return.component';
import {WareReturnDetailComponent} from './ware-return/ware-return-detail/ware-return-detail.component';
import {WareTransEditComponent} from './ware-trans/ware-trans-edit/ware-trans-edit.component';
import {TransPartDetailAddComponent} from './ware-trans/trans-part-detail-add/trans-part-detail-add.component';
import {TransPartDetailListComponent} from './ware-trans/trans-part-detail-list/trans-part-detail-list.component';
import {WareTransApplyComponent} from './ware-trans/ware-trans-apply/ware-trans-apply.component';
import {WareTransAuditComponent} from './ware-trans/ware-trans-audit/ware-trans-audit.component';
import {StockPartListComponent} from './ware-trans/stock-part-list/stock-part-list.component';
import {WareTransDispatchComponent} from './ware-trans/ware-trans-dispatch/ware-trans-dispatch.component';
import {ToBeReceivedListComponent} from './ware-trans/to-be-received-list/to-be-received-list.component';
import {WareTransReceiveComponent} from './ware-trans/ware-trans-receive/ware-trans-receive.component';
import {QuickTransferApplyComponent} from './quick-transfer/quick-transfer-apply/quick-transfer-apply.component';
import {QuickTransferConfirmComponent} from './quick-transfer/quick-transfer-confirm/quick-transfer-confirm.component';
import {WareReturnApplyComponent} from './ware-return/ware-return-apply/ware-return-apply.component';
import {ReturnToBeReceivedListComponent} from './ware-return/to-be-received-list/return-to-be-received-list.component';
import {WareReturnConfirmComponent} from './ware-return/ware-return-confirm/ware-return-confirm.component';
import {WareTransDispatchEditComponent} from './ware-trans/ware-trans-dispatch-edit/ware-trans-dispatch-edit.component';
import {WareReturnReceiveComponent} from './ware-return/ware-return-receive/ware-return-receive.component';
import {WareReturnPartAddComponent} from './ware-return/ware-return-part-add/ware-return-part-add.component';
import {QuickTransferPartAddComponent} from './quick-transfer/quick-transfer-part-add/quick-transfer-part-add.component';
import {WareTransToDispatchListComponent} from './ware-trans/ware-trans-to-dispatch-list/ware-trans-to-dispatch-list.component';




@NgModule({
  imports: [SharedModule, TransSettingRoutingModule, NzDrawerModule],
  exports: [],
  declarations: [
    TransComponent,
    QuickTransferComponent,
    WareTransComponent,
    QuickTransferDetailComponent,
    WareTransDetailComponent,
    WareDeliveryEditComponent,
    WareDeliveryAddComponent,
    WareDeliveryInfoComponent,
    WareReceiveAddComponent,
    WareReturnComponent,
    WareReturnDetailComponent,
    WareTransApplyComponent,
    WareTransEditComponent,
    WareTransDispatchComponent,
    TransPartDetailAddComponent,
    WareTransToDispatchListComponent,
    TransPartDetailListComponent,
    WareTransAuditComponent,
    StockPartListComponent,
    ToBeReceivedListComponent,
    WareTransReceiveComponent,
    QuickTransferApplyComponent,
    QuickTransferConfirmComponent,
    WareReturnApplyComponent,
    ReturnToBeReceivedListComponent,
    WareReturnConfirmComponent,
    WareTransDispatchEditComponent,
    WareReturnReceiveComponent,
    WareReturnPartAddComponent,
    QuickTransferPartAddComponent

  ],
  entryComponents: [QuickTransferDetailComponent,
    WareDeliveryAddComponent,
    WareTransDetailComponent,
    WareDeliveryEditComponent,
    WareDeliveryInfoComponent,
    WareReceiveAddComponent,
    WareTransEditComponent,
    TransPartDetailAddComponent,
    WareTransToDispatchListComponent,
    TransPartDetailListComponent,
    StockPartListComponent,
    WareReturnPartAddComponent,
    QuickTransferPartAddComponent
  ]
})
export class TransSettingModule {
}
