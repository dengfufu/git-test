import {Component} from '@angular/core';
import {filter} from 'rxjs/operators';
import {ActivatedRoute, ActivationEnd, Router} from '@angular/router';
import {ACLService} from '@delon/acl';
import {ANYFIX_RIGHT} from '../../../core/right/right';


@Component({
  selector: 'app-service-reason',
  templateUrl: 'service-reason.component.html'
})
export class ServiceReasonComponent {

  tabList = [];

  tabActiveKey: string;
  tabActiveTitle: string;

  firstActiveUrl = '';

  constructor(private router: Router,
              private activateRoute: ActivatedRoute,
              private aclService: ACLService) {
    const rightIds = this.aclService.data.abilities;
    if (rightIds.includes(ANYFIX_RIGHT.SERVICE_REASON)) {
      const serviceReturnReason = {key: 'service-return-reason', tab: '客服退单原因'};
      this.tabList.push(serviceReturnReason);
      const serviceRecallReason = {key: 'service-recall-reason', tab: '客服撤回派单原因'};
      this.tabList.push(serviceRecallReason);
      const engineerRefuseReason = {key: 'engineer-refuse-reason', tab: '工程师拒绝派单原因'};
      this.tabList.push(engineerRefuseReason);
      const engineerReturnReason = {key: 'engineer-return-reason', tab: '工程师退回派单原因'};
      this.tabList.push(engineerReturnReason);
      this.firstActiveUrl = 'service-return-reason';
    }
    if (rightIds.includes(ANYFIX_RIGHT.CUSTOM_REASON)) {
      const customReason = {key: 'custom-reason', tab: '客户撤单原因'};
      this.tabList.push(customReason);
      this.firstActiveUrl = this.firstActiveUrl === '' ? 'custom-reason' : this.firstActiveUrl;
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
    this.router.navigate([`${item.key}`], {
      relativeTo: this.activateRoute
    });
  }
}
