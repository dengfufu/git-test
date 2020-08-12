import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {PostListComponent} from './post-list/post-list.component';
import {PostAddComponent} from './post-add/post-add.component';
import {PostDetailComponent} from './post-detail/post-detail.component';
import {PostEditComponent} from './post-edit/post-edit.component';

const routes: Routes = [
  {path: '', component: PostListComponent, pathMatch: 'full'},
  {path: 'post-list', component: PostListComponent, data: {name: '物品寄送'}},
  {path: 'post-add', component: PostAddComponent, data: {name: '新建寄送单'}},
  {path: 'post-edit', component: PostEditComponent, data: {name: '编辑寄送单'}},
  {path: 'post-detail', component: PostDetailComponent, data: {name: '寄送单详情'}},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class GoodsRoutingModule {
}
