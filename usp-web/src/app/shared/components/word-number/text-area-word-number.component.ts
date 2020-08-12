import {Input, Component, OnInit} from '@angular/core';

@Component({
  selector: 'text-area-word-number',
  templateUrl: 'text-area-word-number.component.html'
})
export class TextAreaWordNumberComponent implements OnInit {

  @Input() currentNumber = 0;
  @Input() totalCount = 200 ;


  constructor() {

  }

  ngOnInit(): void {

  }

}
