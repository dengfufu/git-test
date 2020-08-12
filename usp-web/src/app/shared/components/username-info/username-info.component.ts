import {Input, Component, OnInit} from '@angular/core';

@Component({
  selector: 'username-info',
  templateUrl: 'username-info.component.html',
  styleUrls: ['username-info.less']
})
export class UsernameInfoComponent implements OnInit {

  @Input() userId;
  @Input() username;

  constructor() {
  }

  ngOnInit(): void {
  }

}
