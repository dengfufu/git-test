import {NgModule} from '@angular/core';
import {SharedModule} from '@shared/shared.module';
import {NzDrawerModule} from 'ng-zorro-antd';
import {GoodsRoutingModule} from './goods-routing.module';
import {PostListComponent} from './post-list/post-list.component';
import {PostAddComponent} from './post-add/post-add.component';
import {PostDetailComponent} from './post-detail/post-detail.component';
import {PostEditComponent} from './post-edit/post-edit.component';
import {GoodsDetailAddComponent} from './goods-detail/goods-detail-add.component';
import {GoodsDetailEditComponent} from './goods-detail/goods-detail-edit.component';
import {WorkSelectorComponent} from './work-selector/work-selector.component';

@NgModule({
  imports: [SharedModule, GoodsRoutingModule, NzDrawerModule],
  exports: [],
  declarations: [
    PostListComponent,
    PostAddComponent,
    PostEditComponent,
    PostDetailComponent,
    GoodsDetailAddComponent,
    GoodsDetailEditComponent,
    WorkSelectorComponent,
  ],
  entryComponents: [
    GoodsDetailAddComponent,
    GoodsDetailEditComponent,
    WorkSelectorComponent
  ]
})
export class GoodsModule {
}
