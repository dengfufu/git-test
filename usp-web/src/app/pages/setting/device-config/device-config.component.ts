import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, ActivationEnd, Router} from '@angular/router';
import {filter} from 'rxjs/operators';
import {ACLService} from '@delon/acl';
import {DEVICE_RIGHT} from '../../../core/right/right';

@Component({
  selector: 'device-config-list',
  templateUrl: 'device-config.component.html'
})
export class DeviceConfigComponent implements OnInit {

  tabList = [];

  tabActiveKey: string;
  tabActiveTitle: string;

  firstActiveUrl = '';

  constructor(private router: Router,
              private activateRoute: ActivatedRoute,
              private aclService: ACLService) {
    const rightIds = this.aclService.data.abilities;
    if (rightIds.includes(DEVICE_RIGHT.DEVICE_BRAND)) {
      const deviceBrand = {key: 'device-brand', tab: '设备品牌'};
      this.tabList.push(deviceBrand);
      this.firstActiveUrl = this.firstActiveUrl === '' ? 'device-brand' : this.firstActiveUrl;
    }
    if (rightIds.includes(DEVICE_RIGHT.DEVICE_LARGE_CLASS)) {
      const deviceLargeClass = {key: 'device-large-class', tab: '设备大类'};
      this.tabList.push(deviceLargeClass);
      this.firstActiveUrl = this.firstActiveUrl === '' ? 'device-large-class' : this.firstActiveUrl;
    }
    if (rightIds.includes(DEVICE_RIGHT.DEVICE_SMALL_CLASS)) {
      const deviceSmallClass = {key: 'device-small-class', tab: '设备类型'};
      this.tabList.push(deviceSmallClass);
      this.firstActiveUrl = this.firstActiveUrl === '' ? 'device-small-class' : this.firstActiveUrl;
    }
    if (rightIds.includes(DEVICE_RIGHT.DEVICE_MODEL)) {
      const deviceModel = {key: 'device-model', tab: '设备型号'};
      this.tabList.push(deviceModel);
      this.firstActiveUrl = this.firstActiveUrl === '' ? 'device-model' : this.firstActiveUrl;
    }
  }

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
