import {Component, OnInit} from '@angular/core';
import {Result} from '@core/interceptor/result';
import {HttpClient} from '@angular/common/http';
import {UserService} from '@core/user/user.service';
import {finalize} from 'rxjs/operators';
import {NzMessageService} from 'ng-zorro-antd';

/**
 * 企业钱包账户
 */
@Component({
  selector: 'app-corp-account-info',
  templateUrl: 'corp-account-info.component.html',
  styleUrls: ['corp-account-info.component.less']
})
export class CorpAccountInfoComponent implements OnInit {

  loading = false;
  accountInfo: any;

  constructor(private httpClient: HttpClient,
              private nzMessageService: NzMessageService,
              private userService: UserService) {
  }

  ngOnInit() {
    this.viewCorpAccountInfo();
  }

  viewCorpAccountInfo() {
    this.loading = true;
    const param = {
      corpId: this.userService.currentCorp.corpId
    };
    this.httpClient.post('/api/pay/account-info/corp-view', param)
      .pipe(finalize(() => this.loading = false))
      .subscribe((result: Result) => {
        if (result && result.code === 0) {
          this.accountInfo = result.data;
        }
      });
  }

}
