import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {DataConfigService} from '../../../../data-config/data-config.service';
import {WorkConfigService} from '../../../../setting/work-config/work-config.service';

@Component({
  selector: 'app-work-fee-edit',
  templateUrl: './work-fee-edit.component.html',
  styleUrls: ['./work-fee-edit.component.less']
})
export class WorkFeeEditComponent implements OnInit {

  @Input() workId: any;
  @Input() demanderCorp: any;
  // 页面加载中
  spinning = false;
  // 提交加载中
  submitLoading = false;
  // 工单费用
  workFee: any = {};
  // 工单费用明细
  assortFeeList: any[] = [];
  implementFeeList: any[] = [];
  // 数据项配置
  configItem: any = {};
  WORK_ADD_NEED_QUOTE = this.workConfigService.WORK_ADD_NEED_QUOTE;

  constructor(
    private modalRef: NzModalRef,
    private httpClient: HttpClient,
    private formBuilder: FormBuilder,
    private messageService: NzMessageService,
    private dataConfigService: DataConfigService,
    private workConfigService: WorkConfigService
  ) {
  }

  ngOnInit() {
    this.spinning = true;
    this.httpClient.get(`/api/anyfix/work-fee/${this.workId}`)
      .pipe(
        finalize(() => {
          this.spinning = false;
        })
      ).subscribe((res: any) => {
        this.workFee = res.data;
        const detailList = res.data.detailList || [];
        this.assortFeeList = detailList.filter((item: any) => item.feeType === 1);
        this.initImplementFeeList();
    });
    this.getServiceConfig();
  }

  // 初始化需要填写的支出费用列表
  initImplementFeeList() {
    this.httpClient.post('/api/anyfix/work-fee-implement-define/listByWork', {workId: this.workId})
      .subscribe((res: any) => {
        this.implementFeeList = res.data;
        this.implementFeeList.forEach((implementFee: any) => {
          implementFee.feeType = 2;
        });
      });
  }

  getServiceConfig() {
    this.configItem[this.workConfigService.WORK_ADD_NEED_QUOTE] = {
      isShow: false,
      description: '费用报价',
      itemValue: 0,
      formName: 'basicServiceFee'
    };
    const itemIdList = [this.workConfigService.WORK_ADD_NEED_QUOTE];
    this.dataConfigService.getCorpConfigData(this.demanderCorp, itemIdList, this.configItem)
      .then((res: any) => {
        if(res.data && res.data.length > 0) {
          for ( const key of Object.keys(this.configItem)) {
            if (this.configItem[key].itemValue === '2') {
              const formName = this.configItem[key].formName;
              this.configItem[key].isShow = true;
            }
          }
        }
      })
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
      basicServiceFee: this.workFee.basicServiceFee,
      otherFee: this.workFee.otherFee,
      otherFeeNote: this.workFee.otherFeeNote,
      detailList: this.implementFeeList
    }
    this.submitLoading = true;
    this.httpClient.post('/api/anyfix/work-fee/update', params)
      .pipe(
        finalize(() => {
          this.submitLoading = false;
        })
      )
      .subscribe((res: any) => {
        this.messageService.success('修改成功！');
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
