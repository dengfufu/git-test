import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {finalize} from 'rxjs/operators';
import {Page} from '@core/interceptor/result';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {environment} from '@env/environment';

@Component({
  selector: 'app-user-info-detail',
  templateUrl: 'user-info-detail.component.html',
  styleUrls: ['user-info-detail.component.less']
})
export class UserInfoDetailComponent implements OnInit {


  detail: any = {};
  loading = false;
  page =  new Page();
  customCorp = '';
  customId = '';
  demanderCorp = '';
  spinning = false;
  address = '';
  userId = '';
  showFileUrl = environment.server_url + '/api/file/showFile?fileId=';
  corpPage = new Page();
  corpList: any = [];
  corpLoading = false;
  loginPage = new Page();
  loginList: any = [];
  loginLoading = false;
  operationPage = new Page();
  operationList: any = [];
  operationLoading = false;
  roleMap = {};

  faceImgList = [];

  constructor(private httpClient: HttpClient,
              private modalService: NzModalService,
              private router: Router,
              private msg: NzMessageService,
              private activatedRoute: ActivatedRoute,
              private cdf: ChangeDetectorRef,
              private userService: UserService) {
    this.userId = this.activatedRoute.snapshot.queryParams.userId;
    this.queryDetail();
    this.getRoleUser(this.userId);
    this.queryCorpList();
    this.queryLoginList();
    this.queryOperationList();
  }

  ngOnInit() {
  }

  /**
   * 初始化网点数据
   */
  queryDetail() {
    this.spinning = true;
    this.httpClient
      .get('/api/uas/user-info/detail/' + this.userId )
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
          this.spinning = false;
        })
      )
      .subscribe((res: any) => {
        this.detail = res.data;
        this.faceImgList = [];
        this.faceImgList.push(this.showFileUrl + this.detail.faceImgBig);
      });
  }

  // 用户所在企业
  queryCorpList(){
    this.corpLoading = true;
    this.httpClient
      .post('/api/uas/corp-user/corp/list' , this.getCorpParams())
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
          this.corpLoading = false;
        })
      )
      .subscribe((res: any) => {
        this.corpList = res.data;
        this.corpPage.total = res.data.total;
      });
  }

  // 用户角色信息
  getRoleUser(userId?: string) {
    this.httpClient
      .get('/api/uas/sys-role/list/user/' + userId)
      .pipe(
        finalize(() => {
        })
      )
      .subscribe((res: any) => {
        if(res.data){
          res.data.forEach(item => {
            this.roleMap[item.corpId] = item.roleName;
          });
        }
      });
  }

  // 登录日志
  queryLoginList() {
    this.loginLoading = true;
    this.httpClient
      .post('/api/uas/user-logon-log/query', this.getLoginParams())
      .pipe(
        finalize(() => {
          this.loginLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        if(res.data == null){
          return;
        }
        this.loginList = res.data.list || [];
        this.loginPage.total =res.data.total;
      });
  }

  // 操作日志
  queryOperationList() {
    this.operationLoading = true;
    this.httpClient
      .post('/api/uas/user-oper/query', this.getOperationParams())
      .pipe(
        finalize(() => {
          this.operationLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        if(res.data == null){
          return;
        }
        this.operationList = res.data.list;
        this.operationPage.total =res.data.total;
      });
  }

  getCorpParams() {
    const params: any = {};
    params.pageNum = this.corpPage.pageNum;
    params.pageSize = this.corpPage.pageSize;
    params.userId = this.userId;
    return params;
  }

  getLoginParams() {
    const params: any = {};
    params.pageNum = this.loginPage.pageNum;
    params.pageSize = this.loginPage.pageSize;
    params.userId = this.userId;
    return params;
  }

  getOperationParams() {
    const params: any = {};
    params.pageNum = this.operationPage.pageNum;
    params.pageSize = this.operationPage.pageSize;
    params.userId = this.userId;
    return params;
  }

  goBack() {
    history.go(-1);
  }

  /*menuClick(name: string) {
    if(name === '设置管理员'){
       this.setAdmin();
    }
  }*/
}
