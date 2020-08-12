import {ChangeDetectorRef, Component, Inject, Input, OnInit, ViewChild} from '@angular/core';
import {MenuTheme} from 'pro-layout';
import {Router} from '@angular/router';
import {DA_SERVICE_TOKEN, ITokenService} from '@delon/auth';
import {Corp, UserService} from '@core/user/user.service';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Result} from '@core/interceptor/result';
import {WebsocketService} from '@core/service/websocket.service';
import {ACLService} from '@delon/acl';
import {NzModalService, NzOptionComponent} from 'ng-zorro-antd';
import {NoticeListComponent} from '../../../../pages/uas/notice/notice-list.component';
import {environment} from '@env/environment';

@Component({
  selector: 'pro-right-content',
  templateUrl: 'right-content.component.html',
  styleUrls: ['right-content.component.less']
})
export class RightContentComponent implements OnInit {

  @Input() theme: MenuTheme;
  @Input() layout: string;

  visible = false;
  @ViewChild(NoticeListComponent, {static: false}) appNoticeList: NoticeListComponent;
  
  // 已选择的企业
  selectedCorp:Corp;
  
  // 选项比较方法，按corpId
  compareWithFun = (c1: Corp, c2: Corp) => {
    return c1 && c2 && c1.corpId === c2.corpId
  };
  
  // 关键字过滤方法，支持公司简称的中文和拼音首字母
  filterOptionFun = (input?: string, option?: NzOptionComponent) => {
    const corp:Corp = (option.nzValue as Corp);
    return (corp.shortName && corp.shortName.indexOf(input) >= 0)
      || (corp.pinyin && corp.pinyin.indexOf(input.trim().toLowerCase()) >= 0);
  };
  
  constructor(public cdf: ChangeDetectorRef,
              @Inject(DA_SERVICE_TOKEN) public tokenService: ITokenService,
              public router: Router,
              public httpClient: HttpClient,
              public websocketService: WebsocketService,
              public aclService: ACLService,
              public userService: UserService,
              private nzModalService: NzModalService) {
  }

  ngOnInit() {
    this.selectedCorp = this.userService.currentCorp;
  }

  logout() {
    const payload = new HttpParams()
      .set('token', this.tokenService.get().token);
    this.httpClient.post('/api/auth/oauth/remove/token?_allow_anonymous=true', payload, {
      headers: {
        Authorization: 'Basic ' + window.btoa(`${environment.client_id}:${environment.client_secret}`)
      }
    }).subscribe((result: Result) => {
      console.log(result);
    });
    this.tokenService.clear();
    this.userService.clear();
    this.router.navigateByUrl(this.tokenService.login_url);
  }

  changeCorp(corp: any) {
    this.userService.changeCorp(corp);
    this.userService.initRightList().then(() => {
      // 切换企业后跳转到首页
      this.router.navigate(['/']);
      this.cdf.markForCheck();
    });
  }
  
  toNoticePage() {
    this.websocketService.hasNewNotice = false;
    // const modal = this.nzModalService.create({
    //   nzTitle: '消息中心',
    //   nzContent: NoticeListComponent,
    //   nzFooter: null,
    //   nzMask: false,
    //   nzWidth: '600px',
    //   nzStyle: { right:0 ,top:'64px',position:'fixed',overflow_y:'scroll',overflow_x:'hidden',z_index: 1,
    //     height: 'calc(100% - 55px)', padding_bottom: '53px'}
    //   });
    // modal.afterClose.subscribe(result => {
    //
    // });
    // // this.router.navigate(['/uas/notice-list']);
    this.appNoticeList.loadData(true);
    this.visible = true;

  }

  closeNoticeDrawer() {
    this.visible = false;
  }
}
