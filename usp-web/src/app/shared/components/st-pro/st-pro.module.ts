import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StProComponent } from './st-pro.component';
import {NgZorroAntdModule} from 'ng-zorro-antd';
import {DelonUtilModule} from '@delon/util';
import {ColSelectorModule} from '@shared/components/col-selector';
import {STModule} from '@delon/abc';

@NgModule({
  declarations: [
    StProComponent
  ],
  imports: [
    CommonModule,
    NgZorroAntdModule,
    DelonUtilModule,
    STModule,
    ColSelectorModule
  ],
  exports: [
    StProComponent,
    STModule,
    ColSelectorModule
  ]
})
export class StProModule { }
