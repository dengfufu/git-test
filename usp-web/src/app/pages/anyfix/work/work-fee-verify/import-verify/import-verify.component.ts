import {Component, Input, OnInit} from '@angular/core';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-import-verify',
  templateUrl: './import-verify.component.html',
  styleUrls: ['./import-verify.component.less']
})
export class ImportVerifyComponent implements OnInit {

  // 对账单编号
  @Input() verifyId;
  // 导入文件列表
  fileList: any[];
  // 允许导入的文件类型
  fileTypes = ['application/vnd.ms-excel','application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'];
  //
  msg = '';

  constructor(
    public modalRef: NzModalRef,
    public modalService: NzModalService,
    public httpClient: HttpClient,
    public messageService: NzMessageService
  ) { }

  ngOnInit() {
  }

  // 上传提交请求
  uploadRequest = (item: any) => {
    this.msg = '';
    this.modalService.confirm({
      nzTitle: '确认',
      nzContent: '确定导入 ' + item.file.name + ' 中对账数据? ',
      nzOkText: '确定',
      nzCancelText: '取消',
      nzOnOk: () => {
        const formData = new FormData();
        formData.append('file', item.file);
        this.httpClient.post('/api/anyfix/work-fee-verify/verify/import?verifyId=' + this.verifyId, formData)
          .subscribe((res: any) => {
            if (res.code === 0) {
              if (res.msg.startsWith('导入对账成功')) {
                item.onSuccess({}, {status: 'done'});
                this.modalService.info({
                  nzContent: res.msg,
                  nzOkText: '确定',
                  nzOnOk: () => {
                    this.modalRef.destroy('submit');
                  }
                });
              } else {
                this.messageService.error(res.msg);
                this.msg = res.msg;
                this.fileList = [];
              }
            }
          })
      },
      nzOnCancel: () => {
        this.fileList = [];
      }
    });
  };

  // 复制报错信息
  doCopy(errorInfo: string) {
    const errorMsg = errorInfo.replace(/<br\/>/g, '\n\r');
    const oInput = document.createElement('input');
    oInput.value = errorMsg;
    document.body.appendChild(oInput);
    oInput.select();
    document.execCommand('Copy');
    oInput.style.display = 'none';
    this.messageService.success('复制成功');
  }

  // 关闭弹框
  destroyModal() {
    this.modalRef.destroy('cancel');
  }

}
