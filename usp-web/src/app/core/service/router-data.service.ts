import {Injectable, OnDestroy} from '@angular/core';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {filter, map, mergeMap, share} from 'rxjs/operators';
import {BehaviorSubject, Observable} from 'rxjs';

/**
 * 获取路由配置data信息
 */
@Injectable({
  providedIn: 'root'
})
export class RouterDataService implements OnDestroy {

  private data$: BehaviorSubject<any> = new BehaviorSubject<any>(null);


  constructor(private router: Router,
              private activatedRoute: ActivatedRoute) {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd),
      map(() => this.activatedRoute),
      map((route) => {
        while (route.firstChild) {
          route = route.firstChild;
        }
        return route;
      }),
      filter((route) => route.outlet === 'primary'),
      mergeMap((route) => route.data)
    ).subscribe((event) => {
      this.data$.next(event);
    });
  }

  data(): Observable<any> {
    return this.data$.pipe(share());
  }

  ngOnDestroy(): void {
    this.data$.unsubscribe();
  }

}
