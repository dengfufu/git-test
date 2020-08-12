import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzModalService} from 'ng-zorro-antd';
import {WareCatalogAddComponent} from '../add/ware-catalog-add.component';
import {WareCatalogEditComponent} from '../edit/ware-catalog-edit.component';
import {Page, Result} from '@core/interceptor/result';
import {finalize} from 'rxjs/operators';

export interface TreeNodeInterface {
  id: number;
  name: string;
  description?: string;
  parentId: number;
  enabled?: string;
  sortNo: number;
  level?: number;
  expand?: boolean;
  children?: TreeNodeInterface[];
  parent?: TreeNodeInterface;
}

@Component({
  selector: 'app-ware-class-list',
  templateUrl: 'ware-catalog-list.component.html'
})
export class WareCatalogListComponent implements OnInit{

  searchForm: FormGroup;
  wareCatalogList: TreeNodeInterface[] = [];
  page = new Page();
  loading = false;
  drawerVisible = false;

  mapOfExpandedData: { [key: string]: TreeNodeInterface[] } = {};

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private modalService: NzModalService,
              private cdf: ChangeDetectorRef) {
    this.searchForm = this.formBuilder.group({
      name: []
    });
  }

  ngOnInit(): void {
    this.queryWareCatalog();
  }

  queryWareCatalog(reset: boolean = false) {
    this.loadWareCatalog();
  }

  loadWareCatalog(){
    this.loading = true;
    this.httpClient.get('/api/wms/ware-catalog/tree', {})
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: Result) => {
        this.wareCatalogList = res.data;
        this.wareCatalogList.forEach(item => {
          this.mapOfExpandedData[item.id] = this.convertTreeToList(item);
        });
      })
  }

  addWareCatalog() {
    const modal = this.modalService.create({
      nzTitle: '添加分类',
      nzContent: WareCatalogAddComponent,
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterOpen.subscribe();
    modal.afterClose.subscribe((result) => {
      if(result === 'submit'){
        this.queryWareCatalog();
      }
    });
  }

  editWareCatalog(data) {
    const modal = this.modalService.create({
      nzTitle: '编辑分类',
      nzContent: WareCatalogEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        wareCatalog: data
      }
    });
    modal.afterOpen.subscribe();
    modal.afterClose.subscribe((result) => {
      if(result === 'submit'){
        this.queryWareCatalog();
      }
    });
  }

  confirmDelete(wareCatalogId) {
    this.modalService.confirm({
      nzTitle: '确定删除该配置吗？',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteWareCatalog(wareCatalogId),
      nzCancelText: '取消'
    });
  }

  private deleteWareCatalog(wareCatalogId) {
    this.httpClient.delete('/api/wms/ware-catalog/' + wareCatalogId)
      .subscribe();
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
