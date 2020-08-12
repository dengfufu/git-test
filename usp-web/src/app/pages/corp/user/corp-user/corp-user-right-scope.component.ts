import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {UserService} from '@core/user/user.service';
import {NzMessageService, NzModalService, NzTreeComponent} from 'ng-zorro-antd';
import {ActivatedRoute} from '@angular/router';
import {Result} from '@core/interceptor/result';
import {NzTreeNodeOptions} from 'ng-zorro-antd/core/tree/nz-tree-base-node';
import {RightScopeService} from '../right-scope.service';
import {UserRightScopeComponent} from './right-scope/user-right-scope.component';

@Component({
  selector: 'app-corp-user-right-scope',
  templateUrl: 'corp-user-right-scope.component.html',
  styleUrls: ['corp-user-right-scope.component.less']
})
export class CorpUserRightScopeComponent implements OnInit {

  @ViewChild('rightTree', {static: true}) rightTree: NzTreeComponent;

  spinning = false;

  rightIdList: any[] = [];
  rightTreeNodes = [];

  // 选中的权限编号列表
  checkedRightIdList = [];
  // 选中的范围权限列表
  checkedScopeRightList = [];

  defaultCheckedKeys = [];
  defaultCheckedTempKeys = [];

  // 是否显示范围权限设置
  rightScopeVisible = false;

  userId: any;
  corpId = this.userService.currentCorp.corpId;
  user: any = {}; // 人员信息

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private userService: UserService,
              private modalService: NzModalService,
              private messageService: NzMessageService,
              private activatedRoute: ActivatedRoute,
              private rightScopeService: RightScopeService) {
    this.activatedRoute.queryParams.subscribe(queryParams => {
      this.userId = queryParams.userId;
    });
  }

  ngOnInit(): void {
    this.initCorpUser();
    if (this.userService.currentCorp.serviceProvider === 'Y') {
      this.rightScopeVisible = true;
    }
    this.initUserRight();
  }

  /**
   * 初始化人员信息
   */
  initCorpUser() {
    this.spinning = true;
    this.httpClient
      .get('/api/uas/corp-user/' + this.userId + '/' + this.corpId)
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        this.user = result.data;
      });
  }

  initUserRight() {
    // 该角色已经设置的权限
    this.httpClient
      .get('/api/uas/user-right/list/' + this.userId + '/' + this.corpId)
      .subscribe((result: Result) => {
        this.rightIdList = result.data;

        this.treeCorpRight().then(() => {
          this.defaultCheckedKeys = this.defaultCheckedTempKeys;
          this.initRightScope();
        });
      });
  }

  // 企业权限树（去除公共权限）
  treeCorpRight() {
    const params = {corpId: this.userService.currentCorp.corpId};
    return new Promise(resolve => {
      this.httpClient
        .post('/api/uas/sys-right/auth/tree', params)
        .pipe(
          finalize(() => {
            this.cdf.markForCheck();
          })
        )
        .subscribe((res: any) => {
          this.rightTreeNodes = res.data || [];
          // 初始化树节点的选中，必须放在这里，因为要先加载出树的数据
          this.setDefaultKeys(this.rightTreeNodes);
          resolve(this.rightTreeNodes);
        });
    });
  }

  /**
   * 根据角色已经设置的权限，递归得到选中树节点
   * @param nodes: 该企业所有权限
   */
  private setDefaultKeys(nodes: NzTreeNodeOptions[]) {
    nodes.forEach((node: NzTreeNodeOptions) => {
      // 已经存在的权限: 默认勾选，
      if (this.rightIdList.includes(node.key)) {
        this.defaultCheckedTempKeys.push(node.key);
      }
      node.disableCheckbox = true;  // 不可勾选
      // 公司公共权限: 默认勾选
      const currentCorp = this.userService.currentCorp;
      if ((node.serviceDemander === currentCorp.serviceDemander && node.serviceDemanderCommon === 'Y') ||
        (node.serviceProvider === currentCorp.serviceProvider && node.serviceProviderCommon === 'Y') ||
        (node.deviceUser === currentCorp.deviceUser && node.deviceUserCommon === 'Y') ||
        (node.cloudManager === currentCorp.cloudManager && node.cloudManagerCommon === 'Y') ||
        node.extraCorpCommon === 'Y') {
        this.defaultCheckedTempKeys.push(node.key);
      }
      if (node.children) {
        this.setDefaultKeys(node.children);
      }
    });
  }

  /**
   * 初始化范围权限
   */
  initRightScope() {
    this.httpClient
      .get('/api/uas/sys-user-right-scope/list/' + this.userId + '/' + this.corpId).subscribe((res: any) => {
      const roleRightScopeList: any [] = res.data || [];
      this.initTreeNodeScope(this.rightTreeNodes, roleRightScopeList);
    });
  }

  initTreeNodeScope(nodes: any[], roleRightScopeList: any[]) {
    nodes.forEach((node: any) => {
      const list: any[] = [];
      let names = '';
      roleRightScopeList.forEach((item: any) => {
        if (node.key === item.rightId) {
          list.push(item);
          const scopeNames = this.rightScopeService.findScopeNames(item);
          item.scopeNames = scopeNames;
          if (scopeNames) {
            names = names.length > 0 ? names + ';' + scopeNames : scopeNames;
          }
        }
      });
      if (names.length > 50) {
        names = names.substr(0, 50) + '...';
      }
      node.scopeNames = names;
      node.roleRightScopeList = [...list];
      if (node.children) {
        this.initTreeNodeScope(node.children, roleRightScopeList);
      }
    });
  }

  /**
   * 获得选中权限编号列表
   * @param nodes 节点
   */
  findCheckedIdList(nodes: any[]) {
    nodes.forEach(node => {
      if (node.isChecked || node.isHalfChecked) {
        this.checkedRightIdList.push(node.key);
      }
      if (node.children) {
        this.findCheckedIdList(node.children);
      }
    });
  }

  /**
   * 获得选中节点列表
   * @param nodes 节点
   */
  findCheckedNodeList(nodes: any[]) {
    nodes.forEach(node => {
      if (this.checkedRightIdList
        && this.checkedRightIdList.includes(node.key)
        && node.roleRightScopeList) {
        node.roleRightScopeList.forEach(item => {
          this.checkedScopeRightList.push(item);
        });
      }
      if (node.children) {
        this.findCheckedNodeList(node.children);
      }
    });
  }

  /**
   * 去除无用和重复项
   */
  removeNoUsedAndRepeat() {
    const repeatList = [];
    const list = [];
    this.checkedScopeRightList.forEach((item: any) => {
      const str = item.rightId + '-' + item.scopeType;
      if (this.checkedRightIdList.includes(item.rightId)
        && !repeatList.includes(str)) {
        list.push(item);
        repeatList.push(str);
      }
    });
    this.checkedScopeRightList = [...list];
  }

  submitForm(): void {
    const treeNodes = this.rightTree.getTreeNodes();
    this.checkedRightIdList = [];
    this.checkedScopeRightList = [];
    this.findCheckedIdList(treeNodes);
    this.findCheckedNodeList(this.rightTreeNodes);
    this.removeNoUsedAndRepeat();

    const params = {
      userId: this.userId,
      corpId: this.corpId,
      userRightScopeList: this.checkedScopeRightList
    };
    this.spinning = true;
    this.httpClient
      .post('/api/uas/sys-user-right-scope/set', params)
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        this.messageService.info('设置范围成功！');
        this.goBack();
      });
  }

  /**
   * 设置范围权限
   * @param node 权限节点
   */
  editRightScope(node) {
    const modal = this.modalService.create({
      nzTitle: '设置人员[' + this.user.userName + ']范围[' + node.title + ']',
      nzContent: UserRightScopeComponent,
      nzFooter: null,
      nzMaskClosable: false,
      nzWidth: 800,
      nzComponentParams: {
        rightId: node.key,
        roleRightScopeList: node.origin.roleRightScopeList
      }
    });
    modal.afterClose.subscribe((scopeList: any[]) => {
      if (scopeList && scopeList.length > 0) {
        this.setRightScope(scopeList, node.key, this.rightTreeNodes);
      }
    });
  }

  /**
   * 设置范围
   * @param scopeList 范围信息列表
   * @param key 权限编号
   * @param treeNodes 权限树节点
   */
  setRightScope(scopeList: any[], key: any, treeNodes: any[]) {
    if (treeNodes) {
      treeNodes.forEach((node: any) => {
        if (node.key === key) {
          node.roleRightScopeList = scopeList;
          let names = '';
          scopeList.forEach((scope: any) => {
            if (scope.scopeNames) {
              names = names.length > 0 ? names + ';' + scope.scopeNames : scope.scopeNames;
            }
          });
          if (names.length > 50) {
            names = names.substr(0, 50) + '...';
          }
          node.scopeNames = names;
        }
        if (node.children) {
          this.setRightScope(scopeList, key, node.children);
        }
      });
    }
  }

  goBack(): void {
    history.go(-1);
  }

}
