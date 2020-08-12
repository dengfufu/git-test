import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {Page} from '@core/interceptor/result';
import {SYS_RIGHT} from '../../../core/right/right';
import {isNull} from '@util/helpers';

@Component({
  selector: 'app-login-log-list',
  templateUrl: 'login-log-list.component.html',
  styleUrls: ['login-log-list.component.less']
})
export class LoginLogListComponent implements OnInit {

  aclRight = SYS_RIGHT;

  searchForm: FormGroup;
  page = new Page();
  params: any = {};
  userIdList = [];
  list: any;
  loading = false;
  corpLoading = false;
  optionList: any = {
    operateTypeList: [
      {value: 1, text: '登录'},
      {value: 2, text: '自动登录'},
      {value: 3, text: '登出'}],
    logonTypeList: [
      {value: 1, text: '手机号+密码'},
      {value: 2, text: '手机号+短信验证码'},
      {value: 3, text: '微信'}],
    logonResultList: [
      {value: 1, text: '成功'},
      {value: 2, text: '密码错误'}],
    deviceTypeList: [
      {value: 1, text: 'IPhone'},
      {value: 2, text: 'IPad'},
      {value: 3, text: 'Android'},
      {value: 4, text: 'Mobile Web'},
      {value: 5, text: 'PC'}]
  };
  corpList = [];
  useFormValue = true;
  drawerVisible: boolean;
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient) {
    this.searchForm = this.formBuilder.group({
      userId: [],
      mobile: [],
      corpId: [],
      operateType: [],
      logonType: [],
      logonResult: [],
      deviceType: [],
      operateTime: []
    });
  }

  ngOnInit(): void {
    this.queryUserLogonLog();
    this.listCorp();
    this.httpClient
      .post('/api/uas/user-info/match', {})
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        if (res.data) {
          const list = [];
          res.data.forEach(item => {
            list.push({
              value: item.userId,
              text: item.userName + '(' + item.mobile + ')'
            });
          });
          this.userIdList = list;
        }
      });
  }

  loadList(params: string): void {
    this.loading = true;
    this.httpClient
      .post('/api/uas/user-logon-log/query',
        params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const data = res.data;
        this.list = data.list || [];
        this.page.total = data.total;
      });
  }

  queryUserLogonLog(reset?: boolean) {
    if (reset) {
      this.page.pageNum = 1;
    }
    if (this.useFormValue) {
      this.params = Object.assign({}, this.searchForm.value);
    }
    // 操作时间
    const operateTime = this.searchForm.controls.operateTime.value;
    if (operateTime) {
      this.params.operateTimeStart = operateTime[0];
      this.params.operateTimeEnd = operateTime[1];
      this.params.operateTime = null;
    }
    this.params.pageNum = this.page.pageNum;
    this.params.pageSize = this.page.pageSize;
    this.loadList(this.params);
  }

  listCorp(corpName?: any) {
    const params = {matchFilter: corpName};
    this.corpLoading = true;
    this.httpClient
      .post('/api/uas/corp-registry/match', params)
      .pipe(
        finalize(() => {
          this.corpLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const list = [];
        res.data.forEach(item => {
          list.push({
            value: item.corpId,
            text: item.corpName
          });
        });
        this.corpList = list;
      });
  }

  matchCorp(corpName) {
    this.listCorp(corpName);
  }

  // 企业更换
  corpChange(corp) {
    if (!isNull(corp)) {
      this.searchForm.patchValue({
        userId: null
      });
      this.userIdList = [];
      this.httpClient
        .post('/api/uas/corp-user/match', {corpId: corp})
        .pipe(
          finalize(() => {
            this.corpLoading = false;
            this.cdf.markForCheck();
          })
        )
        .subscribe((res: any) => {
          const list = [];
          res.data.forEach(item => {
            list.push({
              value: item.userId,
              text: item.userName + '(' + item.mobile + ')'
            });
          });
          this.userIdList = list;
        });
    } else {
      this.httpClient
        .post('/api/uas/user-info/match', {})
        .pipe(
          finalize(() => {
            this.cdf.markForCheck();
          })
        )
        .subscribe((res: any) => {
          if (res.data) {
            const list = [];
            res.data.forEach(item => {
              list.push({
                value: item.userId,
                text: item.userName + '(' + item.mobile + ')'
              });
            });
            this.userIdList = list;
          }
        });
    }
  }

  matchUser(userName?: string) {
    if (isNull(this.searchForm.value.corpId) || this.searchForm.value.corpId === '0') {
      const params = {matchFilter: userName};
      this.httpClient
        .post('/api/uas/user-info/match', params)
        .pipe(
          finalize(() => {
            this.cdf.markForCheck();
          })
        )
        .subscribe((res: any) => {
          if (res.data) {
            const list = [];
            res.data.forEach(item => {
              list.push({
                value: item.userId,
                text: item.userName + '(' + item.mobile + ')'
              });
            });
            this.userIdList = list;
          }
        });
    } else {
      const payload = {
        corpId: this.searchForm.value.corpId,
        matchFilter: userName
      };
      this.httpClient
        .post('/api/uas/corp-user/match', payload)
        .pipe(
          finalize(() => {
            this.cdf.markForCheck();
          })
        )
        .subscribe((res: any) => {
          const list = [];
          res.data.forEach(item => {
            list.push({
              value: item.userId,
              text: item.userName + '(' + item.mobile + ')'
            });
          });
          this.userIdList = list;
        });
    }
  }

  openDrawer() {
    this.drawerVisible = true;
  }

  closeDrawer() {
    this.drawerVisible = false;
  }

  clearDrawer() {
    this.searchForm.reset();
  }
}
