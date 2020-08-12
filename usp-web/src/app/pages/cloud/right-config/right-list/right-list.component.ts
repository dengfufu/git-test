import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {RightEditComponent} from './right-edit.component';
import {RightAddComponent} from './right-add.component';
import {SYS_RIGHT} from '@core/right/right';

export interface TreeNodeInterface {
  key: number;
  title: string;
  level?: number;
  expand?: boolean;
  children?: TreeNodeInterface[];
  parent?: TreeNodeInterface;
}

@Component({
  selector: 'app-right-list',
  templateUrl: 'right-list.component.html',
  styleUrls: ['right-list.component.less']
})
export class RightListComponent implements OnInit {

  aclRight = SYS_RIGHT;

  searchForm: FormGroup;
  list: any;
  loading = false;
  nzOptions: any;

  rightList = [];
  rightScopeList: any[] = [];
  scopeTypeListVisible = false;

  mapOfExpandedData: {[key: string]: TreeNodeInterface[]} = {};

  expandedList = [];

  drawerVisible: boolean;
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private userService: UserService,
              private modalService: NzModalService) {
    this.searchForm = this.formBuilder.group({
      right: [null],
      hasScope: [],
      scopeTypeList: [],
      serviceDemander: [],
      serviceProvider: [],
      deviceUser: [],
      cloudManager: []
    });
  }

  ngOnInit(): void {
    this.listRightScopeType();
    this.queryRight();
    this.listRight();
  }

  loadList(params: string): void {
    this.loading = true;
    this.httpClient
      .post('/api/uas/sys-right/tree', params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.list = res.data || [];
        this.list.forEach(item => {
          this.mapOfExpandedData[item.key] = this.convertTreeToList(item);
        });
      });
  }

  /**
   * 权限范围类型列表
   */
  listRightScopeType() {
    this.httpClient
      .get('/api/uas/sys-right-scope-type/type/list')
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.rightScopeList = res.data;
      });
  }

  hasScopeChange() {
    this.scopeTypeListVisible = false;
    const hasScope = this.searchForm.value.hasScope;
    if (hasScope === 'Y') {
      this.scopeTypeListVisible = true;
    }
  }

  queryRight() {
    this.loadList(this.getParams());
  }

  getParams() {
    let params: any = {};
    if (this.searchForm) {
      params = this.searchForm.value;
      if (this.searchForm.value.right) {
        const value = this.searchForm.value.right.value;
        params.rightId = value.rightId;
        params.appId = value.appId;
        params.parentId = value.parentId;
        params.right = null;
      }
    }
    return params;
  }

  // 进入添加页面
  addModal(parent?): void {
    const modal = this.modalService.create({
      nzTitle: '添加权限',
      nzContent: RightAddComponent,
      nzFooter: null,
      nzMaskClosable: false,
      nzWidth: 800,
      nzComponentParams: {
        sysRight: parent
      }
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.queryRight();
      }
    });
  }

  // 进入编辑页面
  editModal(data): void {
    const modal = this.modalService.create({
      nzTitle: '编辑权限',
      nzContent: RightEditComponent,
      nzFooter: null,
      nzMaskClosable: false,
      nzWidth: 800,
      nzComponentParams: {
        sysRight: data
      }
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.queryRight();
      }
    });
  }

  // 删除确认
  showDeleteConfirm(rightId): void {
    this.modalService.confirm({
      nzTitle: '确定删除吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteRight(rightId),
      nzCancelText: '取消'
    });
  }

  // 删除
  deleteRight(id) {
    this.httpClient
      .delete('/api/uas/sys-right/' + id)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        this.queryRight();
      });
  }

  listRight(rightName?) {
    const params = {
      rightName
    };

    this.httpClient
      .post('/api/uas/sys-right/match', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const listOfOption: Array<{value: string; text: string}> = [];
        res.data.forEach(item => {
          listOfOption.push({
            value: item,
            text: item.rightName
          });
        });
        this.rightList = listOfOption;
      });
  }

  collapse(array: TreeNodeInterface[], data: TreeNodeInterface, $event: boolean): void {
    this.setExpanded(data.key, $event);
    if ($event === false) {
      if (data.children) {
        data.children.forEach(d => {
          // tslint:disable-next-line:no-non-null-assertion
          const target = array.find(a => a.key === d.key)!;
          target.expand = false;
          this.collapse(array, target, false);
        });
      } else {
        return;
      }
    }
  }

  setExpanded(key, $event) {
    if ($event === true) {
      this.expandedList.push(key);
    } else {
      const index = this.expandedList.indexOf(key);
      if (index >= 0) {
        this.expandedList.splice(index, 1);
      }
    }
  }

  convertTreeToList(root: TreeNodeInterface): TreeNodeInterface[] {
    const stack: TreeNodeInterface[] = [];
    const array: TreeNodeInterface[] = [];
    const hashMap = {};

    let expanded = false;
    if (this.expandedList.includes(root.key)) {
      expanded = true;
    }
    stack.push({...root, level: 0, expand: expanded});

    while (stack.length !== 0) {
      // tslint:disable-next-line:no-non-null-assertion
      const node = stack.pop()!;
      this.visitNode(node, hashMap, array);
      if (node.children) {
        for (let i = node.children.length - 1; i >= 0; i--) {
          let childExpanded = false;
          if (this.expandedList.includes(node.children[i].key)) {
            childExpanded = true;
          }
          // tslint:disable-next-line:no-non-null-assertion
          stack.push({...node.children[i], level: node.level! + 1, expand: childExpanded, parent: node});
        }
      }
    }

    return array;
  }

  visitNode(node: TreeNodeInterface, hashMap: {[key: string]: boolean}, array: TreeNodeInterface[]): void {
    if (!hashMap[node.key]) {
      hashMap[node.key] = true;
      array.push(node);
    }
  }

  matchRight(rightName) {
    this.listRight(rightName);
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
