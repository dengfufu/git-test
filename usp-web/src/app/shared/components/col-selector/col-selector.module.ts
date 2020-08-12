import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ColSelectorComponent } from './col-selector.component';
import {NgZorroAntdModule} from 'ng-zorro-antd';
import {DragDropModule} from '@angular/cdk/drag-drop';
import {FormsModule} from '@angular/forms';


@NgModule({
  declarations: [ColSelectorComponent],
  imports: [
    CommonModule,
    FormsModule,
    // ReactiveFormsModule,
    NgZorroAntdModule,
    DragDropModule
  ],
  exports: [
    ColSelectorComponent
  ]
})
export class ColSelectorModule { }
