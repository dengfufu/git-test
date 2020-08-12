import {Component, OnInit} from '@angular/core';
import {UserService} from '@core/user/user.service';

/**
 * 个人中心
 */
@Component({
  selector: 'app-account-center',
  templateUrl: 'account-center.component.html'
})
export class AccountCenterComponent implements OnInit {

  selectedKey = '个人资料';

  constructor(public userService: UserService) {
  }

  ngOnInit() {
  }

  changeSelected(key: string) {
    this.selectedKey = key;
  }

}
