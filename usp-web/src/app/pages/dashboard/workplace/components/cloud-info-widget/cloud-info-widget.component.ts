import {Component, OnDestroy, OnInit} from '@angular/core';
import {WorkplaceService} from '../../workplace.service';
import {Subscription} from 'rxjs';
import {NavigationEnd, Router} from '@angular/router';
import {filter} from 'rxjs/operators';

/**
 * 云平台相关信息
 *
 */
@Component({
  selector: 'app-cloud-info-widget',
  templateUrl: 'cloud-info-widget.component.html',
  styleUrls: ['cloud-info-widget.component.less']
})
export class CloudInfoWidgetComponent implements OnInit, OnDestroy {

  cloudInfo: any = {};
  reloadSubscription: Subscription;

  constructor(public workplaceService: WorkplaceService,
              public router: Router,) {
    this.reloadSubscription = this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(_ => {
      this.cloudInfo = {};
      this.loadData();
    });
  }

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.workplaceService.initCloudInfoData().subscribe((res: any) => {
      this.cloudInfo = res;
    });
  }

  ngOnDestroy() {
    this.reloadSubscription.unsubscribe();
  }
}
