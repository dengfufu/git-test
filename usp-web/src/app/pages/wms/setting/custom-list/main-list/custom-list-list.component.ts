import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {CustomListAddComponent} from '../main-add/custom-list-add.component';
import {UserService} from '@core/user/user.service';
import {finalize} from 'rxjs/operators';
import {CustomListNameEditComponent} from '../main-name-edit/custom-list-name-edit.component';
import {CustomListDetailComponent} from '../detail-add/custom-list-detail.component';
import {WmsService} from '../../../wms.service';

@Component({
  selector: 'app-custom-list-list',
  templateUrl: 'custom-list-list.component.html',
  styleUrls: ['custom-list-list.component.less']
})
export class CustomListListComponent implements OnInit{

  searchForm: FormGroup;
  customList: any = [{name: '物料状态', type: '自定义列表'}];

  pageIndex = 1;
  pageSize = 10;
  total = 1;
  loading = false;
  corpId = this.userService.currentCorp.corpId;

  drawerVisible = false;
  switchLoading = false;

  constructor(private formBuilder: FormBuilder,
              private userService: UserService,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private msg: NzMessageService,
              private modalService: NzModalService) {
    this.searchForm = this.formBuilder.group({
      name: []
    });
  }

  ngOnInit(): void {
    this.queryConfig(false);
  }

  getParams() {
    const params = this.searchForm.value;
    params.pageNum = this.pageIndex;
    params.pageSize = this.pageSize;
    params.corpId = this.corpId;
    return params;
  }

  queryConfig(reset: boolean) {
    if (reset) {
      this.pageIndex = 1;
    }
    this.loading = true;
    this.httpClient.post('/api/wms/custom-list/pageBy',
      this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const data = res.data;
        if( data == null){
          this.customList = [];
          return;
        }
        this.customList = data.list;
        this.total = data.total;
      });
  }

  addCustomList() {
    const modal = this.modalService.create({
      nzTitle: '添加常用列表',
      nzContent: CustomListAddComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        type: 'add',
        id: null
      }
    });
    modal.afterOpen.subscribe();
    modal.afterClose.subscribe((result) => {
      if(result === 0){
        this.queryConfig(false);
        this.msg.success('常用列表添加成功');
      }
    });
  }

  // 进入编辑列表页面
  editDetailList(data): void {
    const modal = this.modalService.create({
      nzTitle: '编辑列表',
      nzContent: CustomListAddComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        type: 'edit',
        id: data.id
      }
    });
    modal.afterOpen.subscribe(() => console.log('[afterOpen] emitted!'));
    modal.afterClose.subscribe(result => {
      if(result === 0){
        this.msg.success('编辑列表成功');
        this.queryConfig(false);
      }
    });
  }

  // 列表名称修改
  editCustomListName(data) {
    const modal = this.modalService.create({
      nzTitle: '修改列表名称',
      nzContent: CustomListNameEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        id: data.id,
      }
    });
    modal.afterOpen.subscribe(() => console.log('[afterOpen] emitted!'));
    modal.afterClose.subscribe(result => {
      if(result === 0){
        this.msg.success('列表名称修改成功');
        this.queryConfig(false);
      }
    });
  }

  confirmDelete(customListId) {
    this.modalService.confirm({
      nzTitle: '确定删除该配置吗？',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteCustomList(customListId),
      nzCancelText: '取消'
    });
  }

  private deleteCustomList(customListId) {
    this.httpClient.delete('/api/wms/custom-list/' + customListId)
      .subscribe(() => {
        this.queryConfig(false);
        this.msg.success('常用列表删除成功');
      });
  }

  clearForm() {
    this.searchForm.reset();
    this.queryConfig(false);
  }

  openDrawer() {
    this.drawerVisible = true;
  }

  closeDrawer() {
    this.drawerVisible = false;
  }

  switch(event, data) {
    if(event === true){
      data.enabled = 'Y';
    }else if(event === false){
      data.enabled = 'N';
    }
    this.switchLoading = !this.switchLoading;
    this.httpClient
      .post('/api/wms/custom-list/updateCustomListMain', data)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        if(res.code === 0){
          this.switchLoading = false;
        }
      });
  }
}
