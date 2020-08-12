import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {NgZorroAntdModule, NzAddOnModule} from 'ng-zorro-antd';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';
import {LayoutComponents} from '@shared/layout';
import {ProLayoutModule} from 'pro-layout';
import {TranslateModule} from '@ngx-translate/core';
import {Components} from '@shared/components';
import {InnerHtmlPipe} from './pipe/inner-html.pipe';
import {DelonACLModule} from '@delon/acl';
import {UserDetailComponent} from '@shared/components/user-detail/user-detail.component';
import {NoticeListComponent} from '../pages/uas/notice/notice-list.component';
import {DragDropModule} from '@angular/cdk/drag-drop';
import {Directives} from '@shared/directive';
import {CustomListComponent} from '@shared/components/custom-list/custom-list.component';
import {DeleteConfirmComponent} from '@shared/components/delete-confirm/delete-confirm.component';
import {ImgViewerModule} from '@shared/components/image-viewer/image-viewer.module';
import {LazyLoadImageModule} from 'ng-lazyload-image';
import {NgxEchartsModule} from 'ngx-echarts';
import {ScrollingModule} from '@angular/cdk/scrolling';
import {DelonUtilModule} from '@delon/util';
import {AlainThemeModule} from '@delon/theme';
import {StProModule} from '@shared/components/st-pro/st-pro.module';
import {CorpUserEditComponent} from '@shared/components/corp-user-edit/corp-user-edit.component';


const Modules = [
  CommonModule,
  FormsModule,
  ReactiveFormsModule,
  RouterModule,

  NgZorroAntdModule,
  NzAddOnModule,
  DelonACLModule,
  DelonUtilModule,
  AlainThemeModule,
  ProLayoutModule,
  TranslateModule,
  DragDropModule,
  ScrollingModule,
  LazyLoadImageModule,
  ImgViewerModule,
  NgxEchartsModule,
  ImgViewerModule,
  StProModule
];

const Pipes = [
  InnerHtmlPipe,
];

@NgModule({
  imports: [
    ...Modules,
  ],
  exports: [
    ...Modules,
    ...LayoutComponents,
    ...Components,
    ...Pipes,
    ...Directives
  ],
  declarations: [
    ...LayoutComponents,
    ...Components,
    ...Pipes,
    ...Directives
  ],
  entryComponents: [
    UserDetailComponent,
    NoticeListComponent,
    CustomListComponent,
    DeleteConfirmComponent,
    CorpUserEditComponent
  ]
})
export class SharedModule {
}
