import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {UserService} from '@core/user/user.service';
import {NzMessageService, NzModalService, NzTreeComponent, NzTreeNode} from 'ng-zorro-antd';
import {ZonValidators} from '@util/zon-validators';
import {RoleRightScopeComponent} from './right-scope/role-right-scope.component';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-corp-role-manage-add',
  templateUrl: 'corp-role-add.component.html',
  styleUrls: ['corp-role-add.component.less']
})
export class CorpRoleAddComponent implements OnInit {

  @ViewChild('rightTree', {static: true}) rightTree: NzTreeComponent;

  validateForm: FormGroup;

  corpId: any;

  // 选中的权限编号列表
  checkedRightIdList = [];
  // 选中的范围权限列表
  checkedScopeRightList = [];

  rightTreeNodes = [];

  // 是否显示范围权限设置
  rightScopeVisible = false;
  spinning = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private userService: UserService,
              private activatedRoute: ActivatedRoute,
              private nzMessageService: NzMessageService,
              private modalService: NzModalService) {
    this.corpId = this.activatedRoute.snapshot.queryParams.corpId;
    this.validateForm = this.formBuilder.group({
      roleName: [null, [ZonValidators.required('角色名称'), ZonValidators.notEmptyString(), ZonValidators.maxLength(50)]],
      description: [''],
      enabled: [true, []]
    });
  }

  ngOnInit(): void {
    const currentCorp = this.userService.currentCorp;
    if (currentCorp.serviceProvider === 'Y') {
      this.rightScopeVisible = true;
    }
    this.treeCorpRight();
  }

  // 企业权限树（去除公共权限）
  treeCorpRight() {
    const params = {corpId: this.corpId};
    this.httpClient
      .post('/api/uas/sys-right/auth/tree', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.rightTreeNodes = res.data || [];
      });
  }

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
      .post('/api/uas/sys-role/add',
        value)
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res:any) => {
        if (res.code === 0) {
          this.nzMessageService.success('角色添加成功');
          history.go(-1);
        }
      });
  }

  goBack(): void {
    history.go(-1);
  }
}
