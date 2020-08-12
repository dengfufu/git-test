import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {CorpAccountInfoComponent} from './corp/account/account-info/corp-account-info.component';
import {CorpPaListComponent} from './corp/pay/pay-apply-list/corp-pa-list.component';
import {PaymentGuard} from './guard/payment-guard';
import {AccountCheckComponent} from './corp/account/account-check/account-check.component';

const routes: Routes = [
  {
    path: '', canActivate: [PaymentGuard],canActivateChild: [PaymentGuard], children: [
      {path: 'corp/account-info', component: CorpAccountInfoComponent, data: {name: '企业账户'}},
      {path: 'corp/pay-apply-list', component: CorpPaListComponent, data: {name: '企业支付订单'}}
    ]
  },
  {path: 'account/check', component: AccountCheckComponent, data: {name: '启用在线支付'}},
  ];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  declarations: [],
  providers: [PaymentGuard]
})
export class WalletRoutingModule {
}
