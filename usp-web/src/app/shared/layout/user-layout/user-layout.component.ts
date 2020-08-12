import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {SettingsService} from 'pro-layout';
import {environment} from '@env/environment';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-user-layout',
  templateUrl: 'user-layout.component.html',
  styleUrls: ['user-layout.component.less'],
})
export class UserLayoutComponent implements OnInit {

  url = environment.server_url;

  @ViewChild('linkIconTemplate', {static: true})
  linkIconTemplate: TemplateRef<void>;
  protocolList: any;
  footer: any;

  constructor(
    public settingService: SettingsService,
    public http: HttpClient,
  ) {
  }

  ngOnInit() {
    this.footer = {
      copyright: '2019 深圳市紫金支点技术股份有限公司'
    };
    this.getProtocol();
  }

  getProtocol() {
    this.http.get('/api/uas/protocol/list?_allow_anonymous=true').subscribe((res: any) => {
      this.protocolList = res.data;
    });
  }
}
