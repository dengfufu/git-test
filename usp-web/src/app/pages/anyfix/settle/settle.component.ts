import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, ActivationEnd, Router} from '@angular/router';
import {filter} from 'rxjs/operators';
import {ANYFIX_RIGHT} from '@core/right/right';
import {ACLService} from '@delon/acl';

@Component({
  selector: 'app-application-config-settle',
  templateUrl: 'settle.component.html',
})
export class SettleComponent implements OnInit {

  tabList = [];

  tabActiveKey: string;
  tabActiveTitle: string;

  // 默认加载的url
  firstActiveUrl = '';

  needRedirect = false;

  constructor(private router: Router,
              private activateRoute: ActivatedRoute,
              private aclService: ACLService
  ) {
    if(this.router.url === '/anyfix/settle/' || this.router.url === '/anyfix/settle/settle-demander') {
      this.needRedirect = true;
      const rightIds = this.aclService.data.abilities;
      if (rightIds.includes(ANYFIX_RIGHT.SETTLE_DEMANDER)) {
        const settleDemander = {key: 'settle-demander', tab: '委托商结算'};
        this.tabList = [...this.tabList, settleDemander];
        this.firstActiveUrl = this.firstActiveUrl === '' ? 'settle-demander' : this.firstActiveUrl;
      }
      if (rightIds.includes(ANYFIX_RIGHT.SETTLE_CUSTOM)) {
        const settleCustom = {key: 'settle-custom', tab: '客户结算'};
        this.tabList = [...this.tabList, settleCustom];
        this.firstActiveUrl = this.firstActiveUrl === '' ? 'settle-custom' : this.firstActiveUrl;
      }
      if (rightIds.includes(ANYFIX_RIGHT.SETTLE_STAFF_RECORD)) {
        const settleStaffRecord = {key: 'settle-staff-record', tab: '员工结算记录'};
        this.tabList = [...this.tabList, settleStaffRecord];
        this.firstActiveUrl = this.firstActiveUrl === '' ? 'settle-staff-record' : this.firstActiveUrl;
      }
      if (rightIds.includes(ANYFIX_RIGHT.SETTLE_STAFF)) {
        const settleStaff = {key: 'settle-staff', tab: '员工结算'};
        this.tabList = [...this.tabList, settleStaff];
        this.firstActiveUrl = this.firstActiveUrl === '' ? 'settle-staff' : this.firstActiveUrl;
      }
    }
  }

  ngOnInit() {
    if(this.needRedirect) {
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
