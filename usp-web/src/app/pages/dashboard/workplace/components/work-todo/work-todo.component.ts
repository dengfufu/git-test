import {Component, OnDestroy, OnInit} from '@angular/core';
import {WorkplaceService} from '../../workplace.service';
import {Result} from '@core/interceptor/result';
import {NavigationEnd, Router} from '@angular/router';
import {AnyfixService} from '@core/service/anyfix.service';
import {Subscription} from 'rxjs';
import {filter} from 'rxjs/operators';

/**
 * 代办工单
 */
@Component({
  selector: 'app-work-todo',
  templateUrl: 'work-todo.component.html'
})
export class WorkTodoComponent implements OnInit, OnDestroy {

  workTodoList: any[] = [];
  reloadSubscription: Subscription;

  constructor(private chartService: WorkplaceService,
              public anyfixService: AnyfixService,
              public router: Router,) {
    this.reloadSubscription = this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(_ => {
      this.workTodoList = [];
      this.loadData();
    });
  }

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.chartService.initWorkTodoData().subscribe((result: Result) => {
      if (result.code === 0) {
        this.workTodoList = result.data.list || [];
      }
    });
  }

  viewWorkDetail(workId: any) {
    this.router.navigate(['/anyfix/work-detail'], {queryParams: {workId}});
  }


  toWorkList() {
    this.router.navigate(['/anyfix/work-list'], {queryParams: {}});
  }

  getWorkStatusColor(workStatus: any) {
    return this.anyfixService.getWorkStatusColor(workStatus);
  }

  ngOnDestroy() {
    this.reloadSubscription.unsubscribe();
  }
}
