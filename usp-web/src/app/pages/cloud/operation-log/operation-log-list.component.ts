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
  selector: 'app-operation-log-list',
  templateUrl: 'operation-log-list.component.html',
  styleUrls: ['operation-log-list.component.less']
})
export class OperationLogListComponent implements OnInit {

  aclRight = SYS_RIGHT;

  searchForm: FormGroup;
  page = new Page();
  params: any = {};
  userIdList = [];
  list: any;
  loading = false;
  corpLoading = false;
  corpList = [];
  useFormValue = true;
  drawerVisible: boolean;
  operTypeList = [
    {value: 1, text: '用户注册'},
    {value: 2, text: '修改昵称'},
    {value: 3, text: '修改登录名'},
    {value: 4, text: '修改手机号'},
    {value: 5, text: '实名认证'},
    {value: 6, text: '设置签名'},
    {value: 7, text: '设置邮箱'},
    {value: 8, text: '设置地区'},
    {value: 9, text: '添加地址'},
    {value: 10, text: '修改地址'},
    {value: 11, text: '删除地址'},
    {value: 12, text: '修改性别'},
    {value: 13, text: '设置密码'},
    {value: 14, text: '修改密码'},
    {value: 15, text: '忘记密码-修改密码'}
  ];

  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private userService: UserService,
              private modalService: NzModalService) {
    this.searchForm = this.formBuilder.group({
      corpId: [],
      userId: [],
      operType: [],
      operTime: []
    });
  }

  ngOnInit(): void {
    this.queryOperationLog();
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
      .post('/api/uas/user-oper/query',
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

  queryOperationLog(reset?: boolean) {
    if(reset) {
      this.page.pageNum = 1;
    }
    if (this.useFormValue) {
      this.params = Object.assign({},this.searchForm.value);
    }
    this.params.pageNum = this.page.pageNum;
    this.params.pageSize = this.page.pageSize;
    this.loadList(this.params);
  }

  listCorp(corpName?: any){
    this.params = {matchFilter: corpName};
    this.corpLoading = true;
    this.httpClient
      .post('/api/uas/corp-registry/match', this.params)
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
