import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, ActivationEnd, Router} from '@angular/router';
import {filter} from 'rxjs/operators';

@Component({
  selector: 'app-application-config-settle',
  templateUrl: 'trans.component.html',
})
export class TransComponent implements OnInit {

  tabList = [
    {
      key: 'ware-trans',
      tab: '物料库存调度',
    },
    {
      key: 'quick-transfer',
      tab: '物料快速转库',
    },
    {
      key: 'ware-return',
      tab: '待修物料返还',
    }
  ];

  tabActiveKey: string;
  tabActiveTitle: string;

  constructor(private router: Router,
              private activateRoute: ActivatedRoute) {
  }

  ngOnInit() {
    this.router.events.pipe(
      filter(e => e instanceof ActivationEnd)
    ).subscribe(() => this.setActive());
    this.setActive();
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
