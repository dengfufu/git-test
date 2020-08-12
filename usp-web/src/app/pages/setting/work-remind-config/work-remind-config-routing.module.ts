import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {WorkRemindConfigListComponent} from './list/work-remind-config-list.component';

const routes: Routes = [
  {
    path: 'work-remind', component: WorkRemindConfigListComponent, data: {name: '预警设置'},
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes),
    FormsModule],
  exports: [RouterModule]
})
export class WorkRemindConfigRoutingModule {
}
