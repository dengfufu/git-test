import {Input, Component, OnInit} from '@angular/core';

@Component({
  selector: 'input-word-number',
  templateUrl: 'input-word-number.component.html'
})
export class InputWordNumberComponent implements OnInit {

  @Input() currentNumber = 0;
  @Input() totalCount = 20 ;

  constructor() {

  }

  ngOnInit(): void {

  }

}
