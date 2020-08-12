import {Component, OnDestroy, OnInit} from '@angular/core';
import {DefaultRangeDate, RangeDates, WorkplaceService} from '../../workplace.service';
import {AnyfixService} from '@core/service/anyfix.service';
import {NavigationEnd, Router} from '@angular/router';
import {filter} from 'rxjs/operators';
import {Subscription} from 'rxjs';
import {endOfMonth, startOfMonth} from 'date-fns';

@Component({
  selector: 'app-work-new-widget',
  templateUrl: 'work-new-widget.component.html',
  styleUrls: ['work-new-widget.component.less']
})
export class WorkNewWidgetComponent implements OnInit, OnDestroy {

  workStatus: any = {};
  rangeDate = [startOfMonth(new Date()), endOfMonth(new Date())];
  rangeDates: any = RangeDates;
  reloadSubscription: Subscription;

  constructor(private workplaceService: WorkplaceService,
              public anyfixService: AnyfixService,
              private router: Router) {
    this.reloadSubscription = this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(_ => {
      this.workStatus = {};
      this.loadData();
    });
  }

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.workplaceService.initWorkNewData(this.rangeDate).subscribe((res: any) => {
      this.workStatus = res;
    });
  }

  ngOnDestroy() {
    this.reloadSubscription.unsubscribe();
  }
}
