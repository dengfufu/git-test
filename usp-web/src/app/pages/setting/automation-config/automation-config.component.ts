import {Component} from '@angular/core';
import {filter} from 'rxjs/operators';
import {ActivatedRoute, ActivationEnd, Router} from '@angular/router';
import {ACLService} from '@delon/acl';
import {ANYFIX_RIGHT} from '../../../core/right/right';


@Component({
  selector: 'app-automation-config-list',
  templateUrl: 'automation-config.component.html'
})
export class AutomationConfigComponent {

  tabList = [];

  tabActiveKey: string;
  tabActiveTitle: string;

  firstActiveUrl = '';

  constructor(private router: Router,
              private activateRoute: ActivatedRoute,
              private aclService: ACLService) {
    const rightIds = this.aclService.data.abilities;
    if (rightIds.includes(ANYFIX_RIGHT.DISPATCH_SERVICE_CORP)) {
      const serviceCorp = {key: 'dispatch-service-corp', tab: '自动提交服务商'};
      this.tabList.push(serviceCorp);
      this.firstActiveUrl = this.firstActiveUrl === '' ? 'dispatch-service-corp' : this.firstActiveUrl;
    }
    if (rightIds.includes(ANYFIX_RIGHT.DISPATCH_SERVICE_BRANCH)) {
      const serviceBranch = {key: 'dispatch-service-branch', tab: '自动分配服务网点'};
      this.tabList.push(serviceBranch);
      this.firstActiveUrl = this.firstActiveUrl === '' ? 'dispatch-service-branch' : this.firstActiveUrl;
    }
    if (rightIds.includes(ANYFIX_RIGHT.ASSIGN_MODE)) {
      const assignMode = {key: 'assign-mode', tab: '自动派单模式'};
      this.tabList.push(assignMode);
      this.firstActiveUrl = this.firstActiveUrl === '' ? 'assign-mode' : this.firstActiveUrl;
    }
  }

  // tslint:disable-next-line:use-lifecycle-interface
  ngOnInit() {
    let preUrl = '';
    if (this.router.url.includes(this.firstActiveUrl)) {
      const index = this.router.url.lastIndexOf('/');
      preUrl = this.router.url.substring(0, index);
    } else {
      preUrl = this.router.url;
    }
    const url = preUrl + '/' + this.firstActiveUrl;
    this.router.navigate([url], {
      relativeTo: this.activateRoute
    });
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


  handleTabChange(item: {key: string; tab: string;}) {
    this.router.navigate([`${item.key}`], {relativeTo: this.activateRoute});
  }

}
