import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {FormsModule} from '@angular/forms';
// import {CustomReasonListComponent} from './reason/custom-reason-list.component';

const routes: Routes = [
  {
    // path: '', component: CustomReasonListComponent, data: {name: '客户撤单原因', reasonType: 1}
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes),
    FormsModule],
  exports: [RouterModule]
})
export class CustomReasonRoutingModule {
}
