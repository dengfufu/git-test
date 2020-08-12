import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {Page} from '@core/interceptor/result';
import {SYS_RIGHT} from '../../../core/right/right';
import { ZorroUtils } from '@util/zorro-utils';
import {AnyfixModuleService} from '@core/service/anyfix-module.service';
import {UserInfoService} from './service/user-info.service';
import {ActivatedRoute, Router} from '@angular/router';
import {isNull} from '@util/helpers';

@Component({
  selector: 'app-user-info-list',
  templateUrl: 'user-info-list.component.html',
  styleUrls: ['user-info-list.component.less']
})
export class UserInfoListComponent implements OnInit {
  ZorroUtils = ZorroUtils;
  aclRight = SYS_RIGHT;
  userIdList = [];
  areaInfoOption = [];

  searchForm: FormGroup;
  page = new Page();
  list: any;
  loading = false;
  corpLoading = false;
  corpList = [];
  useFormValue: any;
  drawerVisible: boolean;
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private userInfoService : UserInfoService,
              private router: Router,
              private activatedRoute : ActivatedRoute,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private anyfixModuleService: AnyfixModuleService) {
    this.searchForm = this.formBuilder.group({
      corpId: [],
      userId: [],
      mobile: [],
      nickname: [],
      sex: [],
      country: [],
      area: [],
      district: [],
      status: [],
      regTime: []
    });
  }

  ngOnInit(): void {
    this.getParamsValue();
    this.anyfixModuleService.getAreaInfoOption().subscribe((res: any) => {
      this.areaInfoOption = res.data;
    });
    this.queryUserList();
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

  getParamsValue() {
    const params = this.userInfoService.paramsValue;
    if( params) {
      this.searchForm.setValue(params.formValue);
      this.page = params.page;
      this.userInfoService.paramsValue = null;
    }
  }

  saveParamsValue() {
    const params = {
      formValue : this.searchForm.value,
      page : this.page,
    };
    this.useFormValue = params;
  }

  getParams() {
    const params = Object.assign({}, this.searchForm.value);
    // 注册时间
    const regTime = this.searchForm.controls.regTime.value;
    if (regTime) {
      params.regTimeStart = regTime[0];
      params.regTimeTimeEnd = regTime[1];
      params.regTime = null;
    }
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    return params;
  }

  queryUserList(reset?: boolean) {
    if(reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.saveParamsValue();
    this.httpClient
      .post('/api/uas/user-info/query',this.getParams())
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

  listCorp(corpName?: any){
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

  // 行政区划改变
  areaChange(event) {
    if (event === null || event.length === 0) {
      this.searchForm.controls.district.setValue(null);
      return;
    }
    if (event !== undefined){
      if(event.length > 2) {
        this.searchForm.controls.district.setValue(event[2]);
      }else if(event.length > 1) {
        this.searchForm.controls.district.setValue(event[1]);
      } else if(event.length === 1) {
        this.searchForm.controls.district.setValue(event[0]);
      }
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

  toDetail(Id) {
    this.userInfoService.paramsValue = this.useFormValue;
    this.router.navigate(['../user-info/detail'],
      {queryParams: {userId: Id}, relativeTo: this.activatedRoute});
  }
}
