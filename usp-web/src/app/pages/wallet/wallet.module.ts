import {NgModule} from '@angular/core';

import {SharedModule} from '@shared/shared.module';
import {WalletRoutingModule} from './wallet-routing.module';
import {PayApplyModule} from './components/pay/pay-apply/pay-apply.module';
import {CorpAccountInfoComponent} from './corp/account/account-info/corp-account-info.component';
import {CorpPaListComponent} from './corp/pay/pay-apply-list/corp-pa-list.component';
import {AccountCheckComponent} from './corp/account/account-check/account-check.component';
import {AccountModule} from './components/account/account.module';

@NgModule({
  imports: [SharedModule, WalletRoutingModule, AccountModule, PayApplyModule],
  exports: [],
  declarations: [
    CorpAccountInfoComponent,
    CorpPaListComponent,
    AccountCheckComponent
  ],
  providers: [],
})
export class WalletModule {
}
