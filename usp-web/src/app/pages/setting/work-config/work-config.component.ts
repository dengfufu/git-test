import {Component} from '@angular/core';
import {filter} from 'rxjs/operators';
import {ActivatedRoute, ActivationEnd, Router} from '@angular/router';
import {ACLService} from '@delon/acl';
import {ANYFIX_RIGHT} from '@core/right/right';


@Component({
  selector: 'app-work-config',
  templateUrl: 'work-config.component.html'
})
export class WorkConfigComponent {

  tabList = [];

  tabActiveKey: string;
  tabActiveTitle: string;

  firstActiveUrl = '';

  constructor(private router: Router,
              private activateRoute: ActivatedRoute,
              private aclService: ACLService) {
    const rightIds = this.aclService.data.abilities;
    if (rightIds.includes(ANYFIX_RIGHT.WORK_TYPE)) {
      const workType = {key: 'work-type', tab: '工单类型'};
      this.tabList.push(workType);
      this.firstActiveUrl = this.firstActiveUrl === '' ? 'work-type' : this.firstActiveUrl;
    }
    if (rightIds.includes(ANYFIX_RIGHT.REMOTE_WAY)) {
      const remoteWay = {key: 'remote-way', tab: '远程处理方式'};
      this.tabList.push(remoteWay);
      this.firstActiveUrl = this.firstActiveUrl === '' ? 'remote-way' : this.firstActiveUrl;
    }
    if (rightIds.includes(ANYFIX_RIGHT.FAULT_TYPE)) {
      const faultType = {key: 'fault-type', tab: '故障现象'};
      this.tabList.push(faultType);
      this.firstActiveUrl = this.firstActiveUrl === '' ? 'fault-type' : this.firstActiveUrl;
    }
    if (rightIds.includes(ANYFIX_RIGHT.SERVICE_EVALUATE)) {
      const serviceEvaluate = {key: 'service-evaluate', tab: '服务评价指标'};
      this.tabList.push(serviceEvaluate);
      this.firstActiveUrl = this.firstActiveUrl === '' ? 'service-evaluate' : this.firstActiveUrl;
    }
    if (rightIds.includes(ANYFIX_RIGHT.SERVICE_ITEM)) {
      const serviceItem = {key: 'service-item', tab: '服务项目'};
      this.tabList.push(serviceItem);
      this.firstActiveUrl = this.firstActiveUrl === '' ? 'service-item' : this.firstActiveUrl;
    }
    // 分类费用
    if (rightIds.includes(ANYFIX_RIGHT.WORK_ASSORT_FEE)) {
      const workAssortFee = {key: 'work-assort-fee', tab: '工单收费规则'};
      this.tabList.push(workAssortFee);
      this.firstActiveUrl = this.firstActiveUrl === '' ? 'work-assort-fee' : this.firstActiveUrl;
    }
    // 先关闭服务项目费用
    // if (rightIds.includes(ANYFIX_RIGHT.SERVICE_ITEM_FEE_RULE)) {
    //   const serviceItemFeeRule = {key: 'service-item-fee-rule', tab: '服务项目费用规则'};
    //   this.tabList.push(serviceItemFeeRule);
    //   this.firstActiveUrl = this.firstActiveUrl === '' ? 'service-item-fee-rule' : this.firstActiveUrl;
    // }
    if (rightIds.includes(ANYFIX_RIGHT.WORK_IMPLEMENT_FEE)) {
      const implementFee = {key: 'work-implement-fee', tab: '工单支出费用'};
      this.tabList.push(implementFee);
      this.firstActiveUrl = this.firstActiveUrl === '' ? 'work-implement-fee' : this.firstActiveUrl;
    }
    if (rightIds.includes(ANYFIX_RIGHT.DATA_CONFIG)) {
      const configItem = {key: 'config-item', tab: '数据项配置'};
      this.tabList.push(configItem);
      this.firstActiveUrl = this.firstActiveUrl === '' ? 'config-item' : this.firstActiveUrl;
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

  handleTabChange(item: {key: string; tab: string;}) {
    this.router.navigate([`${item.key}`], {
      relativeTo: this.activateRoute
    });
  }
}
