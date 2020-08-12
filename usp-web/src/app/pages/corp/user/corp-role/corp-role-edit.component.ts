import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute} from '@angular/router';
import {NzMessageService, NzModalService, NzTreeComponent, NzTreeNode} from 'ng-zorro-antd';
import {ZonValidators} from '@util/zon-validators';
import {RoleRightScopeComponent} from './right-scope/role-right-scope.component';
import {UserService} from '@core/user/user.service';
import {NzTreeNodeOptions} from 'ng-zorro-antd/core/tree/nz-tree-base-node';
import {RightScopeService} from '../right-scope.service';


@Component({
  selector: 'app-corp-role-edit',
  templateUrl: 'corp-role-edit.component.html',
  styleUrls: ['corp-role-edit.component.less']
})
export class CorpRoleEditComponent implements OnInit {

  @ViewChild('rightTree', {static: true}) rightTree: NzTreeComponent;

  role: any = {};
  roleId: any;
  corpId: any;

  validateForm: FormGroup;

  rightIdList = [];
  rightTreeNodes: NzTreeNodeOptions[] = [];

  // 选中的权限编号列表
  checkedRightIdList = [];
  // 选中的范围权限列表
  checkedScopeRightList = [];

  defaultCheckedKeys = [];
  defaultCheckedTempKeys = [];

  orgListMap: {[key: number]: any[]} = {};

  // 是否显示范围权限设置
  rightScopeVisible = false;
  spinning = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private activatedRoute: ActivatedRoute,
              private httpClient: HttpClient,
              private userService: UserService,
              private modalService: NzModalService,
              private nzMessageService: NzMessageService,
              private rightScopeService: RightScopeService) {
    this.corpId = this.activatedRoute.snapshot.queryParams.corpId;
    this.roleId = this.activatedRoute.snapshot.queryParams.roleId;
    this.validateForm = this.formBuilder.group({
      roleId: [null, [Validators.required]],
      roleName: [null, [ZonValidators.required('角色名称'), ZonValidators.notEmptyString(), ZonValidators.maxLength(50)]],
      description: [''],
      enabled: []
    });
  }

  ngOnInit(): void {
    // 非服务商无法设置范围权限, 坑！！
    const currentCorp = this.userService.currentCorp;
    if (currentCorp.serviceProvider === 'Y') {
      this.rightScopeVisible = true;
    }
    this.initData();
  }

  initData() {
    // 该角色已经设置的权限
    this.httpClient
      .get('/api/uas/sys-role/' + this.roleId)
      .subscribe((res: any) => {
        this.role = res.data;
        this.rightIdList = this.role.rightIdList;
        this.validateForm.controls.roleId.setValue(this.role.roleId);
        this.validateForm.controls.roleName.setValue(this.role.roleName);
        this.validateForm.controls.enabled.setValue(this.role.enabled === 'Y');
        this.validateForm.controls.description.setValue(this.role.description);

        this.treeCorpRight().then(() => {
          this.defaultCheckedKeys = this.defaultCheckedTempKeys;
          this.initRightScope();
        });
      });
  }

  /**
   * 该企业的所有权限
   */
  treeCorpRight() {
    const params = {corpId: this.role.corpId};
    return new Promise(resolve => {
      this.httpClient
        .post('/api/uas/sys-right/auth/tree', params).toPromise().then(
        (res: any) => {
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
      // 角色已经存在的权限: 默认勾选，
      if (this.rightIdList.includes(node.key)) {
        this.defaultCheckedTempKeys.push(node.key);
      }
      // 公司公共权限: 默认勾选，且不可编辑
      const currentCorp = this.userService.currentCorp;
      if ((node.serviceDemander === currentCorp.serviceDemander && node.serviceDemanderCommon === 'Y') ||
        (node.serviceProvider === currentCorp.serviceProvider && node.serviceProviderCommon === 'Y') ||
        (node.deviceUser === currentCorp.deviceUser && node.deviceUserCommon === 'Y') ||
        (node.cloudManager === currentCorp.cloudManager && node.cloudManagerCommon === 'Y') ||
        node.extraCorpCommon === 'Y') {
        node.disabled = true;
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
      .get('/api/uas/sys-role-right-scope/list/' + this.roleId).subscribe((res: any) => {
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

  // isContainsKey(item, roleRightScopeList: any[]) {
  //   if (roleRightScopeList && item) {
  //     for (const scope of roleRightScopeList) {
  //       if (scope.rightId === item.rightId && scope.scopeType === item.scopeType) {
  //         return true;
  //       }
  //     }
  //   }
  //   return false;
  // }

  /**
   * 设置范围权限
   * @param node 权限节点
   */
  editRightScope(node) {
    const modal = this.modalService.create({
      nzTitle: '设置角色范围[' + node.title + ']',
      nzContent: RoleRightScopeComponent,
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

  // 选中/反选权限树节点
  checkRightNode(event) {
    const treeNode: NzTreeNode = event.node;
    const isChecked = treeNode.isChecked;
    const isLeaf = treeNode.isLeaf;

    const parentNode: NzTreeNode = treeNode.getParentNode();
    this.checkedParentNode(parentNode, isChecked);

    if (!isLeaf) { // 父节点
      const childrenNodes: NzTreeNode[] = treeNode.getChildren();
      this.checkedChildrenNodes(childrenNodes, isChecked);
    }
  }

  // 选中/反选权限树父节点
  checkedParentNode(node: NzTreeNode, isChecked: boolean) {
    if (node) {
      const childrenNodes: NzTreeNode[] = node.getChildren();
      let num = 0;
      if (childrenNodes) {
        if (isChecked) {
          childrenNodes.forEach(childrenNode => {
            if (childrenNode.isChecked) {
              num = num + 1;
            }
          });
          if (num > 0) {
            node.isChecked = true;
          } else {
            node.isChecked = false;
          }
        }
      }
      const parentNode: NzTreeNode = node.getParentNode();
      this.checkedParentNode(parentNode, isChecked);
    }
  }

  // 选中/反选权限树子节点
  checkedChildrenNodes(nodes: NzTreeNode[], isChecked: boolean) {
    if (nodes) {
      if (isChecked) {
        nodes.forEach(node => {
          node.isChecked = true;
          const childrenNodes: NzTreeNode[] = node.getChildren();
          this.checkedChildrenNodes(childrenNodes, true);
        });
      } else {
        nodes.forEach(node => {
          node.isChecked = false;
          const childrenNodes: NzTreeNode[] = node.getChildren();
          this.checkedChildrenNodes(childrenNodes, false);
        });
      }
    }
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

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    const treeNodes = this.rightTree.getTreeNodes();
    this.checkedRightIdList = [];
    this.checkedScopeRightList = [];
    this.findCheckedIdList(treeNodes);
    this.findCheckedNodeList(this.rightTreeNodes);
    this.removeNoUsedAndRepeat();

    value.enabled = this.validateForm.value.enabled ? 'Y' : 'N';
    value.rightIdList = this.checkedRightIdList;
    value.roleRightScopeList = this.checkedScopeRightList;
    if(this.corpId){
      value.corpId = this.corpId;
    }

    this.spinning = true;
    this.httpClient
      .post('/api/uas/sys-role/update',
        value)
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res:any) => {
        if (res.code === 0) {
          this.nzMessageService.success('角色修改成功');
          history.go(-1);
        }
      });
  }

  goBack(): void {
    history.go(-1);
  }
}
