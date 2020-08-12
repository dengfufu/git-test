import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';

@Component({
  selector: 'app-implement-fee-view',
  templateUrl: './implement-fee-view.component.html',
  styleUrls: ['./implement-fee-view.component.less']
})
export class ImplementFeeViewComponent implements OnInit {

  @Input() implementId;
  // 工单支出费用定义对象
  implementFee: any = {};

  constructor(
    public httpClient: HttpClient,
    public modalRef: NzModalRef
  ) { }

  ngOnInit() {
    this.getImplementFee();
  }

  // 获取工单支出费用明细
  getImplementFee() {
    this.httpClient.get(`/api/anyfix/work-fee-implement-define/${this.implementId}`)
      .subscribe((res: any) => {
        this.implementFee = res.data;
      })
  }

  // 关闭
  destroyModal() {
    this.modalRef.destroy('close');
  }

}
