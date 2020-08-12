import {Component, Input, OnInit} from '@angular/core';
import {NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {environment} from '@env/environment';

@Component({
  selector: 'app-demander-cont-no',
  templateUrl: './demander-cont-no.component.html',
  styleUrls: ['./demander-cont-no.component.less']
})
export class DemanderContNoComponent implements OnInit {

  // 服务商编号
  // @Input() serviceCorp;
  // 委托商编号
  // @Input() demanderCorp;
  // 加载中
  @Input() contId;
  loading = true;
  // 委托协议详情
  detail: any = {};
  // 收费标准附件
  feeFileList: any[] = [];
  // 服务标准附件
  serviceFileList: any[] = [];
  // 显示图片
  showFileUrl = environment.server_url + '/api/file/showFile?fileId=';

  constructor(
    public modalRef: NzModalRef,
    public httpClient: HttpClient,
  ) { }

  ngOnInit() {
    this.finCont();
  }

  // 获取委托协议
  finCont() {
    // const params = {
    //   serviceCorp: this.serviceCorp,
    //   demanderCorp: this.demanderCorp
    // };
    this.loading = true;
    this.httpClient.get(`/api/anyfix/demander-cont/${this.contId}`)
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      ).subscribe((res: any) => {
        this.detail = res.data;
        if (this.detail) {
          this.feeFileList = this.detail.feeRuleFileList || [];
          this.serviceFileList = this.detail.serviceStandardFileList || [];
        }
    });
  }

  // 获取所有url
  getImageUrls(imageList: any[]) {
    imageList = imageList || [];
    const temp: string[] = [];
    imageList.forEach((fileId: any) => {
      temp.push(this.showFileUrl + fileId);
    });
    return temp;
  }

  // 关闭窗口
  cancel() {
    this.modalRef.close();
  }

}
