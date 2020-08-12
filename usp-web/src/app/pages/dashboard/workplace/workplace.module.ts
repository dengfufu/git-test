import {NgModule} from '@angular/core';
import {WorkNewWidgetComponent} from './components/work-new-widget/work-new-widget.component';
import {WorkStatusPieComponent} from './components/work-status-pie/work-status-pie.component';
import {WorkCityMapComponent} from './components/work-city-map/work-city-map.component';
import {DeviceTypePieComponent} from './components/device-type-pie/device-type-pie.component';
import {CloudInfoWidgetComponent} from './components/cloud-info-widget/cloud-info-widget.component';
import {WorkTypePieComponent} from './components/work-type-pie/work-type-pie.component';
import {WorkDemanderBarComponent} from './components/work-demander-bar/work-demander-bar.component';
import {WorkProviderBarComponent} from './components/work-provider-bar/work-provider-bar.component';
import {WorkTodoComponent} from './components/work-todo/work-todo.component';
import {DemanderOrderComponent} from './components/demander-order/demander-order.component';
import {SharedModule} from '@shared/shared.module';
import {WorkplaceService} from './workplace.service';

const ChartComponents = [
  WorkNewWidgetComponent,
  WorkStatusPieComponent,
  WorkCityMapComponent,
  DeviceTypePieComponent,
  CloudInfoWidgetComponent,
  WorkTypePieComponent,
  WorkDemanderBarComponent,
  WorkProviderBarComponent,
  WorkTodoComponent,
  WorkNewWidgetComponent,
  DemanderOrderComponent
];

@NgModule({
  imports: [SharedModule],
  exports: [...ChartComponents],
  declarations: [...ChartComponents],
  providers: [
    WorkplaceService
  ],
})
export class WorkplaceModule {
}
