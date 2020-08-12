import {Directive, EventEmitter, HostListener, Output} from '@angular/core';
@Directive({
  selector: '[exeHighlight]'
})
export class AddressDirective {

  @Output() flag = new EventEmitter<any>();

  constructor() {

  }

  @HostListener('paste', ['$event'])
  preventPaste(e: Event): void {
    this.flag.emit(true);
  }
}
