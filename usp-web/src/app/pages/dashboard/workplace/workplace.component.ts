import {Component, OnDestroy, OnInit, ViewEncapsulation} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {filter} from 'rxjs/operators';
import {UserService} from '@core/user/user.service';
import {NavigationEnd, Router} from '@angular/router';
import {Subscription} from 'rxjs';
import {DIP_RIGHT} from '@core/right/right';
import {WorkplaceService} from './workplace.service';

@Component({
  selector: 'app-workplace',
  templateUrl: 'workplace.component.html',
  styleUrls: ['workplace.component.less']
})
export class WorkplaceComponent implements OnInit, OnDestroy {

  aclRight = DIP_RIGHT;

  reloadSubscription: Subscription;   // 刷新事件处理

  currentCorpRole: string;

  constructor(public httpClient: HttpClient,
              public router: Router,
              public workplaceService: WorkplaceService,
              public userService: UserService) {
    this.reloadSubscription = this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(_ => {
      // 本路由刷新
      this.loadData();
    });

  }

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.workplaceService.initUserInfoData().subscribe((currentCorpRoleList: string[]) => {
      this.currentCorpRole = currentCorpRoleList.toString();
    });
  }

  getDateState() {
    const hours = new Date().getHours();
    let text = ``;
    if (hours >= 0 && hours <= 10) {
      text = `早上好`;
    } else if (hours > 10 && hours <= 14) {
      text = `中午好`;
    } else if (hours > 14 && hours <= 18) {
      text = `下午好`;
    } else if (hours > 18 && hours <= 24) {
      text = `晚上好`;
    }
    return text;
  }

  /**
   * 务必取消订阅
   */
  ngOnDestroy() {
    this.reloadSubscription.unsubscribe();
  }
}
