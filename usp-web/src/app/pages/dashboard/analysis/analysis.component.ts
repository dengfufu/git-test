import {Component, Inject, OnInit} from '@angular/core';
import {DomSanitizer, SafeResourceUrl} from '@angular/platform-browser';
import {environment} from '@env/environment';
import {UserService} from '@core/user/user.service';
import {DA_SERVICE_TOKEN, ITokenService} from '@delon/auth';

@Component({
  selector: 'app-analysis',
  templateUrl: 'analysis.component.html',
  styleUrls: ['analysis.component.less']
})
export class AnalysisComponent implements OnInit {

  safeUrl: SafeResourceUrl;

  constructor(public sanitizer: DomSanitizer,
              @Inject(DA_SERVICE_TOKEN) private tokenService: ITokenService,
              public userService: UserService) {
  }

  ngOnInit() {
    // tslint:disable-next-line:max-line-length
    const url = `${environment.dip_url}/login/uspSSO?token=${this.tokenService.get().token}&tenantid=${this.userService.currentCorp.corpId}`;
    this.safeUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }
}
