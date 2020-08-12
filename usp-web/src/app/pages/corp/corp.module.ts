import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {CorpRoutingModule} from './corp-routing.module';
import {PickCorpComponent} from '../common/pick-corp.component';
import {BaiduMapComponent} from '../common/baidu-map/baidu-map.component';
import {DemanderServiceListComponent} from './demander-service/demander-service-list.component';
import {DemanderServiceAddComponent} from './demander-service/demander-service-add.component';
import {DemanderServiceEditComponent} from './demander-service/demander-service-edit.component';
import {ServiceDemanderListComponent} from './service-demander/service-demander-list.component';
import {ServiceDemanderAddComponent} from './service-demander/service-demander-add.component';
import {DemanderCustomListComponent} from './demander-custom/demander-custom-list.component';
import {DemanderCustomAddComponent} from './demander-custom/demander-custom-add.component';
import {DemanderCustomEditComponent} from './demander-custom/demander-custom-edit.component';
import {CorpUserSelectorComponent} from './user/selector/corp-user-selector.component';
import {DemanderCustomDetailComponent} from './demander-custom/demander-custom-detail.component';
import {BranchListComponent} from './service-branch/list/branch-list.component';
import {BranchDetailComponent} from './service-branch/detail/branch-detail.component';
import {BranchEditComponent} from './service-branch/edit/branch-edit.component';
import {BranchAddComponent} from './service-branch/add/branch-add.component';
import {ServiceDemanderCreateComponent} from './service-demander/service-demander-create.component';
import {ServiceDemanderConfigComponent} from './service-demander/service-demander-config.component';
import {DemanderDetailComponent} from './service-demander/detail/demander-detail.component';
import {WorkConfigModule} from '../setting/work-config/work-config.module';
import {FileConfigAddComponent} from './service-demander/detail/fileconfig/file-config-add.component';
import {ContConfigComponent} from './service-demander/detail/contconfig/cont-config.component';
import {DatePipe} from '@angular/common';
import {CloudModule} from '../cloud/cloud.module';
import {ServiceDemanderEditComponent} from './service-demander/detail/edit/service-demander-edit.component';
import { AutoConfigComponent } from './service-demander/detail/auto-config/auto-config.component';


@NgModule({
  imports: [SharedModule, CorpRoutingModule,WorkConfigModule],
  exports: [],
  providers: [DatePipe],
  declarations: [
    DemanderServiceListComponent,
    DemanderServiceAddComponent,
    DemanderServiceEditComponent,
    ServiceDemanderListComponent,
    ServiceDemanderAddComponent,
    ServiceDemanderCreateComponent,
    PickCorpComponent,
    BaiduMapComponent,
    BranchListComponent,
    BranchDetailComponent,
    BranchAddComponent,
    BranchEditComponent,
    DemanderCustomListComponent,
    DemanderCustomAddComponent,
    DemanderCustomEditComponent,
    CorpUserSelectorComponent,
    DemanderCustomDetailComponent,
    DemanderDetailComponent,
    ServiceDemanderConfigComponent,
    DemanderCustomDetailComponent,
    FileConfigAddComponent,
    ContConfigComponent,
    ServiceDemanderEditComponent,
    ContConfigComponent,
    AutoConfigComponent
  ],
  entryComponents: [
    DemanderServiceEditComponent,
    DemanderServiceAddComponent,
    ServiceDemanderAddComponent,
    ServiceDemanderConfigComponent,
    DemanderCustomEditComponent,
    DemanderCustomAddComponent,
    BaiduMapComponent,
    CorpUserSelectorComponent,
    BranchAddComponent,
    BranchEditComponent,
    ServiceDemanderCreateComponent,
    FileConfigAddComponent,
    ContConfigComponent,
    ServiceDemanderEditComponent,
    ContConfigComponent,
    AutoConfigComponent
  ]
})
export class CorpModule {
}
