import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, ActivationEnd, Router} from '@angular/router';
import {filter} from 'rxjs/operators';

@Component({
  selector: 'ware-config',
  templateUrl: 'ware-config.component.html',
})
export class WareConfigComponent implements OnInit {

  tabList = [
    {
      key: 'ware-brand',
      tab: '品牌',
    },
    {
      key: 'ware-supplier',
      tab: '供应商'
    },
    {
      key: 'ware-express',
      tab: '快递公司'
    },
    {
      key: 'ware-status',
      tab: '物料状态',
    },
    {
      key: 'ware-property-right',
      tab: '物料产权',
    },
    {
      key: 'ware-catalog',
      tab: '分类'
    },
    {
      key: 'ware-model',
      tab: '型号',
    },
    {
      key: 'ware-area',
      tab: '区域',
    },
    {
      key: 'ware-house',
      tab: '库房',
    },
    {
      key: 'custom-list',
      tab: '常用列表',
    },
    {
      key: 'ware-param',
      tab: '参数',
    },
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
