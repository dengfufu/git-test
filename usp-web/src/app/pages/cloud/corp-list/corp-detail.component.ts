import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {finalize} from 'rxjs/operators';
import {Page} from '@core/interceptor/result';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {SetAdminComponent} from './set-admin/set-admin.component';
import {UserService} from '@core/user/user.service';
import {environment} from '@env/environment';
import {SYS_RIGHT} from '@core/right/right';
import {CorpUserEditComponent} from '@shared/components/corp-user-edit/corp-user-edit.component';
import {FormBuilder, FormGroup} from '@angular/forms';
import {ACLService} from '@delon/acl';
import {CorpRegistryEditComponent} from './corp-registry-edit/corp-registry-edit.component';
import {CorpVerifyEditComponent} from './corp-verify-edit/corp-verify-edit.component';
import {CorpRegUserEditComponent} from './corp-regUser-edit/corp-regUser-edit.component';

@Component({
  selector: 'app-corp-detail',
  templateUrl: 'corp-detail.component.html',
  styleUrls: ['corp-detail.component.less']
})
export class CorpDetailComponent implements OnInit {

  aclRight = SYS_RIGHT;

  detail: any = {};
  deviceList: any = [];
  workList: any = [];
  loading = false;
  userPage = new Page();
  rolePage = new Page();
  customCorp = '';
  customId = '';
  demanderCorp = '';
  workLoading = false;
  deviceLoading = false;
  spinning = false;
  address = '';
  corpId = '';
  menuOptions = [
    {
      name: '设置管理员'
    },
    {
      name: '设置紫金管理员'
    }
  ];

  showFileUrl = environment.server_url + '/api/file/showFile?fileId=';
  industryMap = {};
  staffMap = {};
  drawerVisible = false;
  corpUserForm: FormGroup;
  useFormValue = true;
  currentCorp = this.userService.currentCorp;
  userOptions = [];
  roleOptions = [];

  aclRightIdList: any[] = [];
  verifyInfo: any = {};
  corpImageList: any = [];
  licenseImageList: any = [];
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private modalService: NzModalService,
              private router: Router,
              private msg: NzMessageService,
              private activatedRoute: ActivatedRoute,
              private cdf: ChangeDetectorRef,
              private nzMessageService: NzMessageService,
              private aclService: ACLService,
              private userService: UserService) {
    this.corpId = this.activatedRoute.snapshot.queryParams.corpId;
    this.corpUserForm = this.formBuilder.group({
      userId: [],
      roleId: [],
      type: [],
      hidden: []
    });
  }

  ngOnInit() {
    // 设置权限
    this.aclRightIdList = this.aclService.data.abilities || [];

    this.queryDetail();
    this.queryUserList();
    this.matchUser();
    this.matchRole();
    this.queryRoleList();
    this.listIndustry();
    this.listStaff();
    this.queryCorpVerify(this.corpId);
  }

  /**
   * 初始化企业数据
   */
  queryDetail() {
    this.spinning = true;
    this.httpClient
      .get('/api/uas/corp-registry/' + this.corpId )
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
          this.spinning = false;
        })
      )
      .subscribe((res: any) => {
        this.detail = res.data;
        this.corpImageList = [];
        if(this.detail.logoImg !== 0){
          this.corpImageList.push(this.detail.logoImg);
        }
      });
  }

  queryUserList(reset?: boolean){
    if (reset) {
      this.userPage.pageNum = 1;
    }
    this.deviceLoading = true;
    this.httpClient
      .post('/api/uas/corp-user/queryCorpUser' , this.getUserParams())
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
          this.deviceLoading = false;
        })
      )
      .subscribe((res: any) => {
        this.deviceList = res.data.list;
        this.userPage.total = res.data.total;
      });
  }
  /**
   * 加载工单记录
   */
  queryRoleList() {
    this.workLoading = true;
    this.httpClient
      .post('/api/uas/sys-role/query', this.getRoleParams())
      .pipe(
        finalize(() => {
          this.workLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        if(res.data == null){
          return;
        }
        this.workList = res.data.list;
        this.rolePage.total =res.data.total;
      });
  }

  getRoleParams() {
    const params: any = {};
    params.pageNum = this.rolePage.pageNum;
    params.pageSize = this.rolePage.pageSize;
    params.corpId = this.corpId;
    return params;
  }

  getUserParams() {
    let params: any = {};
    if (this.useFormValue) {
      params = Object.assign({}, this.corpUserForm.value);
    }
    params.pageNum = this.userPage.pageNum;
    params.pageSize = this.userPage.pageSize;
    params.corpId = this.corpId;
    return params;
  }

  goBack() {
    history.go(-1);
  }

  menuClick(name: string) {
    switch (name) {
      case '设置管理员':
        this.setAdmin();
        break;
      case '设置紫金管理员':
        this.setAdmin2();
        break;
    }
  }

  // 编辑企业
  editCorpRegistry(): void {
    const modal = this.modalService.create({
      nzTitle: '编辑企业',
      nzContent: CorpRegistryEditComponent,
      nzFooter: null,
      nzWidth: 1000,
      nzStyle:{'margin-top': '-40px'},
      nzComponentParams:{
        corpId : this.corpId
      }
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.nzMessageService.success('修改成功');
        this.queryDetail();
      }
    });
  }

  // 企业认证
  corpVerify(): void {
    const modal = this.modalService.create({
      nzTitle: '企业认证',
      nzContent: CorpVerifyEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams:{
        corpId : this.corpId,
        key: 'verify'
      }
    });
    modal.afterClose.subscribe(result => {
      if (result === 'verify') {
        this.nzMessageService.success('认证成功');
        this.queryDetail();
        this.queryCorpVerify(this.corpId);
      }
    });
  }

  // 编辑认证信息
  editCorpVerify(): void {
    const modal = this.modalService.create({
      nzTitle: '编辑认证信息',
      nzContent: CorpVerifyEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzStyle:{'margin-top': '-40px'},
      nzComponentParams:{
        corpId : this.corpId,
        key: 'edit'
      }
    });
    modal.afterClose.subscribe(result => {
      if (result === 'edit') {
        this.nzMessageService.success('修改成功');
        this.queryCorpVerify(this.corpId);
      }
    });
  }

  // 进入设置页面
  setAdmin(): void {
    const modal = this.modalService.create({
      nzTitle: '设置为管理员',
      nzContent: SetAdminComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams:{
        corpId : this.corpId
      }
    });
    modal.afterClose.subscribe(result => {
      if (result !== undefined) {
        this.msg.success(result.msg);
        if(result.data === 2 ){
          this.queryUserList();
          this.queryRoleList();
        } else if( result.data === 1){
          this.queryUserList();
        }
      }
    });
  }
  // 进入设置页面
  setAdmin2(): void {
    const modal = this.modalService.create({
      nzTitle: '设置为紫金管理员',
      nzContent: SetAdminComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams:{
        corpId : this.userService.currentCorp.corpId,
        toCorpId: this.corpId,
        key : 'isZiJin'
      }
    });
    modal.afterClose.subscribe(result => {
      if (result !== undefined) {
        this.msg.success(result.msg);
        if(result.data === 2 ){
          this.queryUserList();
          this.queryRoleList();
        } else if( result.data === 1){
          this.queryUserList();
        }
      }
    });
  }

  listIndustry() {
    this.httpClient.get('/api/uas/industry/list')
      .pipe(
        finalize(() => {

        })
      )
      .subscribe((res: any) => {
        if(res.data){
          res.data.forEach(item => {
            this.industryMap[item.code] = item.name;
          });
        }
      });
  }

  listStaff() {
    this.httpClient.get('/api/uas/corp-registry/staff-size/list').pipe(
      finalize(() => {

      })
    ).subscribe((res: any) => {
      if(res.data){
        res.data.forEach(item => {
          this.staffMap[item.code] = item.name;
        });
      }
    });
  }


  queryCorpVerify(corpId) {
    this.httpClient.get('/api/uas/corp-verify/'+ corpId ).pipe(
      finalize(() => {

      })
    ).subscribe((res: any) => {
      if(res.data){
        this.verifyInfo = res.data;
        this.licenseImageList = [];
        if(this.verifyInfo.licenseFileId !== 0){
          this.licenseImageList.push(this.verifyInfo.licenseFileId);
        }
      }
    });
  }

  openDrawer() {
    this.drawerVisible = true;
  }

  closeDrawer() {
    this.drawerVisible = false;
  }

  clearDrawer() {
    this.corpUserForm.reset();
    this.queryUserList();
  }

  // 模糊查询企业人员
  matchUser(userName?: string) {
    const payload = {
      corpId: this.corpId,
      matchFilter: userName
    };
    this.httpClient
      .post('/api/uas/corp-user/matchCorpUserByCorp', payload)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.userOptions = res.data;
      });
  }

  // 模糊查询角色
  matchRole(roleName?: string) {
    const params = {
      matchFilter: roleName,
      corpId: this.corpId
    };
    this.httpClient
      .post('/api/uas/sys-role/match', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.roleOptions = res.data || [];
      });
  }

  /**
   * 隐藏、显示企业人员
   */
  hiddenOrAndShowCorpUser(id, corp, isHidden) {
    this.httpClient.post('/api/uas/corp-user/hidden',{userId: id, corpId: corp, hidden: isHidden}).pipe(
      finalize(() => {

      })
    ).subscribe((res: any) => {
      if (res.code === 0) {
        this.queryUserList();
        if (isHidden === 1) {
          this.msg.success('隐藏企业人员成功');
        } else if (isHidden === 0) {
          this.msg.success('显示企业人员成功');
        }
      } else {
        if (isHidden === 1) {
          this.msg.error('隐藏企业人员失败，请重试');
        } else if (isHidden === 0) {
          this.msg.error('显示企业人员失败，请重试');
        }
      }
    });
  }


  // 进入编辑员工页面
  editModal(data): void {
    const modal = this.modalService.create({
      nzTitle: '编辑员工',
      nzContent: CorpUserEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        corpUser: data
      }
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.queryUserList();
      }
    });
  }

  // 删除员工确认
  showDeleteConfirm(corpId,userId): void {
    this.modalService.confirm({
      nzTitle: '确定删除该员工吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteUser(corpId,userId),
      nzCancelText: '取消'
    });
  }

  // 删除企业人员
  deleteUser(corpId,userId) {
    console.log(corpId,userId);
    this.httpClient
      .delete('/api/uas/corp-user/' + corpId + '/' + userId)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        // 如果是为第n页第一条，需要调整为n-1页才有数据显示
        if (this.deviceList.length === 1 && this.userPage.pageNum > 1) {
          this.userPage.pageNum = this.userPage.pageNum - 1;
        }
        this.queryUserList();
      });
  }

  // 进入编辑角色页面
  editRole(roleId, corpId): void {
    this.router.navigate(['/corp-user/role-edit'],
      {
        queryParams: {roleId, corpId},
        relativeTo: this.activatedRoute
      });
  }

  // 删除角色确认
  DeleteRoleConfirm(roleId): void {
    this.modalService.confirm({
      nzTitle: '确定删除角色吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteRole(roleId),
      nzCancelText: '取消'
    });
  }

  // 删除角色
  deleteRole(roleId) {
    this.httpClient
      .delete('/api/uas/sys-role/' + roleId)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        // 如果是为第n页第一条，需要调整为n-1页才有数据显示
        if (this.workList.length === 1 && this.rolePage.pageNum > 1) {
          this.rolePage.pageNum = this.rolePage.pageNum - 1;
        }
        this.queryRoleList();
      });
  }

  // 进入添加角色页面
  addRole(corpId): void {
    this.router.navigate(['/corp-user/role-add'],
      {
        queryParams: {corpId},
        relativeTo: this.activatedRoute
      });
  }

  // 修改注册人
  editRegUser() {
    const modal = this.modalService.create({
      nzTitle: '修改注册人',
      nzContent: CorpRegUserEditComponent,
      nzComponentParams: {
        corpId: this.corpId,
        userId: this.detail.regUserId
      },
      nzFooter: null,
      nzWidth: 600
    });
    modal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.queryDetail();
      }
    });
  }

  getImageUrls(imageList: any[], justId: boolean = false) {
    const temp: string[] = [];
    if (justId) {
      imageList.forEach((file: any) => {
        temp.push(this.showFileUrl + file);
      });
    } else {
      imageList.forEach((file: any) => {
        temp.push(this.showFileUrl + file.fileId);
      });
    }

    return temp;
  }

  // 企业认证确认
  verifyConfirm(corpId): void {
    this.modalService.confirm({
      nzTitle: '确定企业认证吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.verify(corpId),
      nzCancelText: '取消'
    });
  }

  // 企业认证
  verify(id) {
    this.httpClient
      .post('/api/uas/corp-verify/update',{corpId: id, key: 'verify'})
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        if (res.code === 0) {
          this.nzMessageService.success('认证成功');
          this.queryDetail();
          this.queryCorpVerify(this.corpId);
        }
      });
  }

  // 企业认证撤消确认
  unverifyConfirm(corpId): void {
    this.modalService.confirm({
      nzTitle: '确定改为未认证吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.unverify(corpId),
      nzCancelText: '取消'
    });
  }

  // 撤消企业认证
  unverify(id) {
    this.httpClient
      .post('/api/uas/corp-verify/update',{corpId: id, key: 'unverify'})
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        if (res.code === 0) {
          this.nzMessageService.success('撤消认证成功');
          this.queryDetail();
          this.queryCorpVerify(this.corpId);
        }
      });
  }

  canHidden(): boolean {
    return this.aclRightIdList.includes(this.aclRight.HIDDEN);
  }

  canQueryVerify(): boolean {
    return this.aclRightIdList.includes(this.aclRight.VERIFY_DETAILS);
  }

  canVerify(): boolean {
    return this.aclRightIdList.includes(this.aclRight.VERIFY);
  }

  canRegistryEdit(): boolean {
    return this.aclRightIdList.includes(this.aclRight.REGISTRY_EDIT);
  }

  canVerifyEdit(): boolean {
    return this.aclRightIdList.includes(this.aclRight.VERIFY_EDIT);
  }

  canEditRegUser(): boolean {
    return this.aclRightIdList.includes(this.aclRight.REG_USER_EDIT);
  }
}

