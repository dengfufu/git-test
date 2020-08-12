import {Component, ElementRef, OnInit, Renderer2} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {SettingsService} from 'pro-layout';
import {Title} from '@angular/platform-browser';
import {RouterDataService} from '@core/service/router-data.service';
import {WebsocketService} from '@core/service/websocket.service';
import {environment} from '@env/environment';
import {AutoLoginService} from '@core/user/auto-login.service';

@Component({
  selector: 'app-root',
  template: '<router-outlet></router-outlet>',
})
export class AppComponent implements OnInit {
  constructor(private router: Router,
              private activatedRoute: ActivatedRoute,
              private renderer: Renderer2,
              private httpClient: HttpClient,
              private settingsService: SettingsService,
              private websocketService: WebsocketService,
              private routerDataService: RouterDataService,
              private elementRef: ElementRef,
              private autoLoginService: AutoLoginService,
              private title: Title) {
  }

  ngOnInit() {
    this.routerDataService.data().subscribe((data: any) => {
      if (data) {
        this.title.setTitle(data.name + ' - ' + this.settingsService.settings.title);
      }
    });
    this.websocketService.init();
    const version = `${environment.application_name}-V${environment.version}-${environment.h5_version}-${environment.env}`;
    this.elementRef.nativeElement.setAttribute('usp-version', version);
    this.autoLoginService.initIntervalJob();

  }
}
