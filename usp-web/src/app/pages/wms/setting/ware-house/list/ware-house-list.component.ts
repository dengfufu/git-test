import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzModalService} from 'ng-zorro-antd';
import {WareHouseAddComponent} from '../add/ware-house-add.component';
import {WareHouseEditComponent} from '../edit/ware-house-edit.component';
import {Page, Result} from '@core/interceptor/result';
import {finalize} from 'rxjs/operators';

export interface TreeNodeInterface {
  id: number;
  name: string;
  userNames?: string;
  parentId: number;
  enabled?: string;
  sortNo: number;
  level?: number;
  expand?: boolean;
  children?: TreeNodeInterface[];
  parent?: TreeNodeInterface;
}

@Component({
  selector: 'app-ware-house-list',
  templateUrl: 'ware-house-list.component.html'
})
export class WareHouseListComponent implements OnInit{

  searchForm: FormGroup;
  wareHouseList: any = [];// [{name:'深圳库房',type:'',parentId:'深圳库房',userId:'张三',enabled:'Y'}];
  page = new Page();
  loading = true;
  drawerVisible = false;

  mapOfExpandedData: { [key: string]: TreeNodeInterface[] } = {};

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private modalService: NzModalService) {
    this.searchForm = this.formBuilder.group({
      name: []
    });
  }

  ngOnInit(): void {
    this.queryWareHouse();
  }

  queryWareHouse(reset: boolean = false) {
    this.loadWareHouse(this.param(), reset);
  }

  loadWareHouse(params: string, reset?: boolean) {
    if(reset){
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient.get('/api/wms/ware-depot/tree')
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: Result) => {
        this.wareHouseList = res.data;
        this.wareHouseList.forEach(item => {
          this.mapOfExpandedData[item.id] = this.convertTreeToList(item);
        });
      })
  }

  param() {
    let param: any = {};
    if(this.searchForm){
      param = this.searchForm.value;
    }
    param.pageNum = this.page.pageNum;
    param.pageSize = this.page.pageSize;
    return param;
  }

  addWareHouse() {
    const modal = this.modalService.create({
      nzTitle: '添加库房',
      nzContent: WareHouseAddComponent,
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterOpen.subscribe();
    modal.afterClose.subscribe((result) => {
      if(result === 'submit'){
        this.queryWareHouse();
      }
    });
  }

  editWareHouse(data) {
    const modal = this.modalService.create({
      nzTitle: '编辑库房',
      nzContent: WareHouseEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        wareHouse: data
      }
    });
    modal.afterOpen.subscribe();
    modal.afterClose.subscribe((result) => {
      if(result === 'submit'){
        this.queryWareHouse();
      }
    });
  }

  confirmDelete(wareHouseId) {
    this.modalService.confirm({
      nzTitle: '确定删除该配置吗？',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteWareHouse(wareHouseId),
      nzCancelText: '取消'
    });
  }

  private deleteWareHouse(wareHouseId) {
    this.httpClient.delete('/api/wms/ware-depot/' + wareHouseId)
      .subscribe(() => {
        this.queryWareHouse();
      });
  }

  clearForm() {
    this.searchForm.reset();
  }

  openDrawer() {
    this.drawerVisible = true;
  }

  closeDrawer() {
    this.drawerVisible = false;
  }

  collapse(array: TreeNodeInterface[], data: TreeNodeInterface, $event: boolean): void {
    if ($event === false) {
      if (data.children) {
        data.children.forEach(d => {
          // tslint:disable-next-line:no-non-null-assertion
          const target = array.find(a => a.id === d.id)!;
          target.expand = false;
          this.collapse(array, target, false);
        });
      } else {
        return;
      }
    }
  }

  convertTreeToList(root: TreeNodeInterface): TreeNodeInterface[] {
    const stack: TreeNodeInterface[] = [];
    const array: TreeNodeInterface[] = [];
    const hashMap = {};
    stack.push({ ...root, level: 0, expand: false });

    while (stack.length !== 0) {
      // tslint:disable-next-line:no-non-null-assertion
      const node = stack.pop()!;
      this.visitNode(node, hashMap, array);
      if (node.children) {
        for (let i = node.children.length - 1; i >= 0; i--) {
          // tslint:disable-next-line:no-non-null-assertion
          stack.push({ ...node.children[i], level: node.level! + 1, expand: false, parent: node });
        }
      }
    }
    return array;
  }

  visitNode(node: TreeNodeInterface, hashMap: { [key: string]: boolean }, array: TreeNodeInterface[]): void {
    if (!hashMap[node.id]) {
      hashMap[node.id] = true;
      array.push(node);
    }
  }
}
