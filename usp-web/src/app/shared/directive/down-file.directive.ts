import {Directive, ElementRef, EventEmitter, HostListener, Input, Output} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {saveAs} from 'file-saver';
import {NzMessageService} from 'ng-zorro-antd';

@Directive({
  selector: '[pro-down-file]',
  exportAs: 'proDownFile',
})
export class DownFileDirective {
  isFileSaverSupported = true;

  /** URL请求参数 */
  @Input() httpParams: {};

  /** URL请求参数 */
  @Input() httpBody: {};

  /** 请求类型 */
  @Input() httpMethod = 'get';

  /** 下载地址 */
  @Input() httpUrl: string;

  /** 指定文件名，若为空从服务端返回的 `header` 中获取 `filename`、`x-filename` */
  @Input() fileName: string | ((rep: HttpResponse<Blob>) => string);

  /** 文件数量 */
  @Input() fileNum: number;

  /** 提示信息 */
  @Input() msg: string;

  /** 成功回调 */
  @Output() readonly downSuccess = new EventEmitter<HttpResponse<Blob>>();

  /** 错误回调 */
  @Output() readonly downError = new EventEmitter<any>();

  constructor(private el: ElementRef<HTMLButtonElement>,
              private nzMessageService: NzMessageService,
              private httpClient: HttpClient) {
    let isFileSaverSupported = false;
    try {
      isFileSaverSupported = !!new Blob();
    } catch {
    }
    this.isFileSaverSupported = isFileSaverSupported;
    if (!isFileSaverSupported) {
      console.log('浏览器版本过低');
    }
  }

  @HostListener('click', ['$event'])
  click(event: MouseEvent) {
    // console.log('click', event);
    if (!this.isFileSaverSupported) {
      return;
    }
    // 判断导出数量
    if (this.fileNum >= 100000) {
      this.nzMessageService.warning('超过10万条的限制，请重新查询后导出');
      return;
    }
    // 提示信息
    this.setDisabled(true);
    if(this.msg !== undefined&&this.msg !== null){
      this.nzMessageService.info(this.msg);
    }else {
      this.nzMessageService.info('下载附件');
    }
    this.httpClient.request(this.httpMethod, this.httpUrl, {
      params: this.httpParams || {},
      responseType: 'blob',
      observe: 'response',
      body: this.httpBody,
    }).subscribe(
      (res: HttpResponse<Blob>) => {
        if (res.status !== 200 || (res.body && res.body.size <= 0)) {
          this.downError.emit(res);
          return;
        }
        // if (res.body.size >= 100000) {
        //   this.nzMessageService.warning('超过10万条的限制，请重新查询后导出');
        //   return;
        // }

        // console.log(res.headers);
        const disposition = this.getDisposition(res.headers.get('content-disposition'));
        // console.log(disposition);
        let fileName = this.fileName;
        if (typeof fileName === 'function') {
          fileName = fileName(res);
        }
        fileName =
          fileName || disposition[`filename*`] || disposition[`filename`] || res.headers.get('filename') || res.headers.get('x-filename');
        saveAs(res.body, decodeURI(fileName as string));
        this.downSuccess.emit(res);
      },
      err => this.downError.emit(err),
      () => this.setDisabled(false)
    );
  }

  /**
   * "attachment; filename=%E5%AF%BC%E5%85%A5%E5%B7%A5%E5%8D%95%E6%A8%A1%E6%9D%BF.xlsx"
   *  -->
   * "%E5%AF%BC%E5%85%A5%E5%B7%A5%E5%8D%95%E6%A8%A1%E6%9D%BF.xlsx"
   */
  private getDisposition(data: string | null) {
    // console.log(data);
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

  private setDisabled(status: boolean): void {
    const el = this.el.nativeElement;
    el.disabled = status;
  }
}
