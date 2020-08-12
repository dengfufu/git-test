import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, ActivationEnd, Router} from '@angular/router';
import {filter} from 'rxjs/operators';
import {ANYFIX_RIGHT} from '@core/right/right';
import {ACLService} from '@delon/acl';

@Component({
  selector: 'app-application-config-settlepay',
  templateUrl: 'settlepay.component.html',
})
export class SettlepayComponent implements OnInit {

  tabList = [];

  tabActiveKey: string;
  tabActiveTitle: string;

  firstActiveUrl = '';

  constructor(private router: Router,
              private activateRoute: ActivatedRoute,
              private aclService: ACLService
  ) {
  }

  ngOnInit() {
    const url = this.router.url.substr(0, this.router.url.lastIndexOf('/') + 1) + this.firstActiveUrl;
    this.router.navigate([url], {
      relativeTo: this.activateRoute
    });
    this.router.events.pipe(
      filter(e => e instanceof ActivationEnd)
    ).subscribe(() => {
      this.setActive();
    });
  }

  private setActive() {
    this.tabActiveKey = this.router.url.substr(this.router.url.lastIndexOf('/') + 1);
    const activeTab = this.tabList.find((tab: any) => {
      return tab.key === this.tabActiveKey;
    });
    if (activeTab) {
      this.tabActiveTitle = activeTab.tab;
    }
  }


  handleTabChange(item: { key: string; tab: string; }) {
    this.router.navigate([`${item.key}`], {relativeTo: this.activateRoute});
  }

}
