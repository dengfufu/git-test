import {ModuleWithProviders, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ImgViewerConfig} from './img-viewer.config';
import {NgZorroAntdModule} from 'ng-zorro-antd';
import {ImageViewerDirective} from '@shared/components/image-viewer/image-viewer.directive';
import {ImageViewerComponent} from '@shared/components/image-viewer/image-viewer.component';

@NgModule({
  imports: [
    CommonModule,
    NgZorroAntdModule
  ],
  declarations: [
    ImageViewerComponent,
    ImageViewerDirective
  ],
  entryComponents: [
    ImageViewerComponent
  ],
  exports: [
    ImageViewerComponent,
    ImageViewerDirective
  ]
})
export class ImgViewerModule {
  static forRoot(config?: ImgViewerConfig): ModuleWithProviders {
    return {
      ngModule: ImgViewerModule,
      providers: [{provide: ImgViewerConfig, useValue: config}]
    };
  }
}
