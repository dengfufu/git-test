import {Directive, HostListener, Input} from '@angular/core';
import {NzModalService} from 'ng-zorro-antd';
import {ImageViewerComponent} from '@shared/components/image-viewer/image-viewer.component';

@Directive({
  selector: '[pro-image-viewer]'
})
export class ImageViewerDirective {

  @Input() images: string[] ; // 图片地址
  @Input() currentImgIndex = 1; // 当前图片索引

  constructor(private nzModalService: NzModalService) {

  }

  @HostListener('dblclick', ['$event'])
  click(event: MouseEvent) {
    this.nzModalService.create({
      nzTitle: null,
      nzContent: ImageViewerComponent,
      nzFooter: null,
      nzWidth: 900,
      nzStyle:{'margin-top': '-20px'},
      nzComponentParams: {
        images: this.images,
        currentImgIndex: this.currentImgIndex
      }
    });
  }
}
