import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {environment} from '@env/environment';
import {NzMessageService, NzModalService, UploadFile} from 'ng-zorro-antd';
import {saveAs} from 'file-saver';

@Injectable({
  providedIn: 'root'
})
export class SettleService {

  downFileUrl = environment.server_url + '/api/file/downloadFile?fileId=';

  TO_INVOICE = 1; // 未开票
  INVOICED = 2; // 已开票

  TO_PAY = 1; // 待付款
  TO_RECEIPT = 2; // 已付款
  RECEIPTED = 3; // 已收款

  // 结算方式
  SETTLE_PERIOD = 1; // 按周期结算
  SETTLE_WORK = 2;  // 按单结算

  invoiceStatusList = [
    {value: this.TO_INVOICE, label: '未开票'},
    {value: this.INVOICED, label: '已开票'},
  ];
  payStatusList = [
    {value: this.TO_PAY, label: '待付款'},
    {value: this.TO_RECEIPT, label: '已付款'},
    {value: this.RECEIPTED, label: '已收款'},
  ];
  settleWayList = [
    {value: this.SETTLE_PERIOD, label: '按周期结算'},
    {value: this.SETTLE_WORK, label: '按单结算'}
  ];

  constructor(public httpClient: HttpClient,
              public nzMessageService: NzMessageService,
              public modalService: NzModalService) {
  }

  /**
   * 下载确认
   */
  downloadConfirm(file): void {
    this.modalService.confirm({
      nzTitle: '确定下载吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.downloadFile(file),
      nzCancelText: '取消'
    });
  }

  /**
   * 下载非图片文件
   * @param file
   */
  downloadFile(file: UploadFile) {
    let isFileSaverSupported = false;
    try {
      isFileSaverSupported = !!new Blob();
    } catch {
    }
    if (!isFileSaverSupported) {
      console.log('浏览器版本过低');
      return;
    }
    let fileId = 0;
    if (file && file.response && file.response.data) {
      fileId = file.response.data.fileId;
    }
    this.httpClient.request('get', this.downFileUrl + fileId, {
      params: {},
      responseType: 'blob',
      observe: 'response'
    }).subscribe(
      (res: HttpResponse<Blob>) => {
        if (res.status !== 200 || (res.body && res.body.size <= 0)) {
          return;
        }
        const disposition = this.getDisposition(res.headers.get('content-disposition'));
        let fileName = file.name;
        fileName =
          fileName || disposition[`filename*`] || disposition[`filename`] || res.headers.get('filename') || res.headers.get('x-filename');
        saveAs(res.body, decodeURI(fileName as string));
      },
      err => this.nzMessageService.error(err)
    );
  }

  private getDisposition(data: string | null) {
    const arr: Array<{}> = (data || '').split(';').filter(i => i.includes('=')).map(v => {
      const strArr = v.split('=');
      const utfId = `UTF-8''`;
      let value = strArr[1];
      if (value.startsWith(utfId)) {
        value = value.substr(utfId.length);
      }
      return {[strArr[0].trim()]: value};
    });
    // tslint:disable-next-line:variable-name
    return arr.reduce((_o, item) => item, {});
  }

  getBase64(file: File): Promise<string | ArrayBuffer | null> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result);
      reader.onerror = error => reject(error);
    });
  }

  // 判断是否是图片
  isAssetTypeAnImage(ext) {
    return [
      'png', 'jpg', 'jpeg', 'bmp', 'gif', 'webp', 'psd', 'svg', 'tiff'].indexOf(ext.toLowerCase()) !== -1;
  }

}
