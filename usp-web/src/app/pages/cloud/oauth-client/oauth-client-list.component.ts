import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Page, Result} from '@core/interceptor/result';
import {UserService} from '@core/user/user.service';
import {finalize} from 'rxjs/operators';
import {NzModalService} from 'ng-zorro-antd';
import {OauthClientEditComponent} from './edit/oauth-client-edit.component';
import {OauthClientAddComponent} from './add/oauth-client-add.component';

@Component({
  selector: 'app-oauth-client-list',
  templateUrl: 'oauth-client-list.component.html',
  styles: [
      `
          nz-layout {
              min-height: 75vh;
          }
          .img-filtrate {
              font-size: 10px;
              margin-right: 23px;
              border-color: #FFA349;
              width: 60px;
              height: 20px;
              color: #FFA349;
              right: 50px;
          }

          .icon-add {
              font-size: 10px;
              margin-right: 15px;
              border-color: #FFA349;
              width: 60px;
              height: 20px;
              color: #FFA349;
              right: 140px;
          }
    `
  ]
})
export class OauthClientListComponent implements OnInit {

  page = new Page();
  list: any;
  loading: boolean;
  drawerVisible: boolean;
  clientIdFilter: string;

  constructor(private httpClient: HttpClient,
              private modalService: NzModalService,
              private userService: UserService) {
  }

  ngOnInit() {
    this.loadData(true);
  }

  loadData(reset?: boolean) {
    if (reset) {
      this.page.pageNum = 1;
    }
    const param = {
      clientId: this.clientIdFilter,
      corpId: this.userService.currentCorp.corpId,
      pageNum: this.page.pageNum,
      pageSize: this.page.pageSize
    };
    this.loading = true;
    this.httpClient.post('/api/auth/clients/list', param).pipe(finalize(() => this.loading = false)).subscribe((result: Result) => {
      if (result && result.code === 0) {
        this.list = result.data.list || [];
        this.page.total = result.data.total;
      }
    });
  }

  addModal() {
    const modal = this.modalService.create({
      nzTitle: '添加客户端',
      nzContent: OauthClientAddComponent,
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.loadData(true);
      }
    });
  }

  editModal(data): void {
    const modal = this.modalService.create({
      nzTitle: '编辑客户端',
      nzContent: OauthClientEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        client: data
      }
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.loadData(false);
      }
    });
  }

  showDeleteConfirm(data: any): void {
    this.modalService.confirm({
      nzTitle: '确定删除吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteClient(data),
      nzCancelText: '取消'
    });
  }

  deleteClient(data: any) {
    this.httpClient.post('/api/auth/clients/delete', {
      clientId: data.clientId,
      corpId: data.corpId,
    }).subscribe(() => {
      this.loadData(true);
    });
  }

  openDrawer() {
    this.drawerVisible = true;
  }

  closeDrawer() {
    this.drawerVisible = false;
  }

  clearDrawer() {
    this.clientIdFilter = '';
  }
}
