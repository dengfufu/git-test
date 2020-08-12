import {Component, Input, OnInit} from '@angular/core';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {FormBuilder} from '@angular/forms';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'app-work-fee-add',
  templateUrl: './work-fee-add.component.html',
  styleUrls: ['./work-fee-add.component.less']
})
export class WorkFeeAddComponent implements OnInit {

  @Input() workId: any;
  // 页面加载中
  spinning = false;
  // 提交加载中
  submitLoading = false;
  // 工单费用
  workFee: any = {};
  // 工单费用明细
  implementFeeList: any[] = [];
  // 费用录入状态
  workFeeStatus = 2;

  constructor(
    private modalRef: NzModalRef,
    private httpClient: HttpClient,
    private formBuilder: FormBuilder,
    private messageService: NzMessageService
  ) {
  }

  ngOnInit() {
    this.spinning = true;
    this.httpClient.get(`/api/anyfix/work-request/detail/${this.workId}`)
      .pipe(
        finalize(() => {
          this.spinning = false;
        })
      ).subscribe((res: any) => {
      this.workFee = {
        wareUseFee: res.data.workFeeDto.wareUseFee,
        otherFee: res.data.workFeeDto.otherFee,
        otherFeeNote: res.data.workFeeDto.otherFeeNote
      };
      const detailList = res.data.workFeeDetailDtoList || [];
      this.initImplementFeeList();
    });
  }

  // 初始化实施发生费用列表
  initImplementFeeList() {
    this.httpClient.post('/api/anyfix/work-fee-implement-define/listByWork', {workId: this.workId})
      .subscribe((res: any) => {
        this.implementFeeList = res.data;
      });
  }

  // 取消
  cancel() {
    this.modalRef.destroy('cancel');
  }

  submit() {
    if (!this.checkData()) {
      return;
    }
    const params = {
      workId: this.workId,
      workFeeStatus: this.workFeeStatus,
      workFeeDto: this.workFee,
      workFeeImplementDtoList: this.implementFeeList
    };
    this.submitLoading = true;
    this.httpClient.post('/api/anyfix/work-finish/update/work-fee', params)
      .pipe(
        finalize(() => {
          this.submitLoading = false;
        })
      )
      .subscribe((res: any) => {
        this.messageService.success('填写成功！');
        this.modalRef.destroy('submit');
      })
  }

  // 校验数据
  checkData(): boolean {
    if (this.workFee.otherFee && this.workFee.otherFee > 0 && (this.workFee.otherFeeNote || '').length <= 0) {
      this.messageService.error('填写了其他费用，备注必填');
      return false;
    }
    return true;
  }

}
