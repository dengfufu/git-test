import {Component, OnInit} from '@angular/core';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {Result} from '@core/interceptor/result';

@Component({
  selector: 'app-device-info-import',
  templateUrl: './device-info-import.component.html',
  styleUrls: ['./device-info-import.component.less']
})
export class DeviceInfoImportComponent implements OnInit {

  fileList: any[] = [];
  fileTypes = ['application/vnd.ms-excel','application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'];
  msg: any = '';
  demanderCorpList: any[] = [];
  demanderCorp: any = null;

  constructor(
    private modalRef: NzModalRef,
    private httpClient: HttpClient,
    private modalService: NzModalService,
    private messageService: NzMessageService
  ) {
  }

  ngOnInit() {
    this.listDemanderCorp();
  }

  /**
   * 委托商列表
   */
  listDemanderCorp() {
    this.httpClient.get(`/api/anyfix/demander/list`).subscribe((result: Result) => {
      if (result.code === 0) {
        this.demanderCorpList = result.data || [];
        if (this.demanderCorpList.length > 1) {
          this.demanderCorp = null;
        } else {
          if (this.demanderCorpList.length === 1) {
            this.demanderCorp = this.demanderCorpList[0];
          } else {
            this.demanderCorp = null;
          }
        }
      }
    });
  }

  /**
   * 导入数据
   * @param item: 文件信息
   */
  uploadRequest = (item: any) => {
    // console.log(item.file);
    this.msg = '';
    if (!this.demanderCorp) {
      this.messageService.error('请先选择委托商');
      return;
    }
    this.modalService.confirm({
      nzTitle: '确认',
      nzContent: '确定导入 ' + item.file.name + ' 中数据? 委托商：' + this.demanderCorp.demanderCorpName,
      nzOkText: '确定',
      nzCancelText: '取消',
      nzOnOk: () => {
        const formData = new FormData();
        formData.append('file', item.file);
        this.httpClient.post('/api/device/device-excel/import?demanderCorp=' + this.demanderCorp.demanderCorp, formData)
          .subscribe((res: any) => {
            if (res.code === 0) {
              if (res.msg.startsWith('导入成功')) {
                item.onSuccess({}, {status: 'done'});
                this.modalService.info({
                  nzContent: res.msg,
                  nzOkText: '确定',
                  nzOnOk: () => {
                    this.modalRef.destroy('submit');
                  }
                });
              } else {
                this.msg = res.msg;
              }
            }
          })
      },
      nzOnCancel: () => {
        this.fileList = [];
      }
    });
  };

  destroyModal() {
    this.modalRef.destroy('cancel');
  }

}
