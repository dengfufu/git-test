import {Component, EventEmitter, Input, OnInit, Output, TemplateRef, ViewEncapsulation} from '@angular/core';
import {RouterDataService} from '@core/service/router-data.service';

@Component({
  selector: 'my-pro-page-header-wrapper',
  templateUrl: 'my-page-header-wrapper.component.html',
  styleUrls: ['my-pro-page-header-wrapper.less'],
  encapsulation: ViewEncapsulation.None,
})
export class MyPageHeaderWrapperComponent implements OnInit {

  @Input() title: string;
  @Input() tabList: { key: string; tab: string; }[];
  @Input() tabActiveKey: string;
  @Output() onTabChange: EventEmitter<{ key: string; tab: string; }> = new EventEmitter<{ key: string; tab: string; }>();
  @Input() tabBarExtraContent: TemplateRef<void>;

  constructor(private routerDataService: RouterDataService) {
  }

  ngOnInit() {
    this.routerDataService.data().subscribe((data: any) => {
      this.title = data.name;
    });
  }

  selectChange(event: any) {
    const selectedTab = this.tabList[event.index];
    this.onTabChange.emit(selectedTab);
  }

  getSelectedIndex() {
    const idx = this.tabList.findIndex(w => w.key === this.tabActiveKey);
    if (idx !== -1) {
      return idx;
    } else {
      return 0;
    }
  }
}
