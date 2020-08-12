import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {ActivatedRoute} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {WmsService} from '../../wms.service';

@Component({
  selector: 'app-ware-income-audit',
  templateUrl: './ware-income-audit.component.html',
  styleUrls: ['./ware-income-audit.component.less']
})
export class WareIncomeAuditComponent implements OnInit {

  // 入库单数据
  incomeDetail: any = {};
  // 审核表单
  auditForm: FormGroup;
  // 选中的标签页
  selectedIndex = 0;
  // 销账明细列表
  reverseDetailList: any[] = [];
  // 审批节点列表
  flowInstanceNodeList: any[] = [];
  // 审批历史列表
  flowInstanceTraceList: any[] = [];

  objectKeys = Object.keys;

  constructor(
    private formBuilder: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private httpClient: HttpClient,
    private messageService: NzMessageService,
    public wmsService:WmsService
  ) {
    this.auditForm = this.formBuilder.group({
      nodeEndTypeId: [null, [Validators.required]],
      auditNote: [null],
    });
  }

  ngOnInit() {
    this.incomeDetail.id = this.activatedRoute.snapshot.queryParams.id;
    this.httpClient.post(`/api/wms/income-common/detail/${this.incomeDetail.id}`, {})
      .subscribe((res: any) => {
        this.incomeDetail = res.data;
        if (this.incomeDetail.normsValue && this.incomeDetail.normsValue.length > 0) {
          this.incomeDetail.normsValueObj = JSON.parse(this.incomeDetail.normsValue);
        }
        this.reverseDetailList = this.incomeDetail.bookSaleBorrowResultDtoList;
        this.flowInstanceNodeList = this.incomeDetail.flowInstanceNodeList;
        this.flowInstanceTraceList = this.incomeDetail.flowInstanceTraceDtoList;
      });
  }

  goBack() {
    history.back();
  }

  endTypeChange(event) {
    // 不通过时审批意见必填
    if (event && event !== 10) {
      this.auditForm.controls.auditNote.setValidators(Validators.required);
    } else {
      this.auditForm.controls.auditNote.setValidators(null);
    }
    this.auditForm.controls.auditNote.updateValueAndValidity();
    this.auditForm.controls.auditNote.markAsDirty();
  }

  auditSubmit() {
    if (!this.auditForm.valid) {
      this.objectKeys(this.auditForm.controls).map(key => {
        this.auditForm.controls[key].markAsDirty();
      });
      this.auditForm.updateValueAndValidity();
      return;
    }
    this.incomeDetail.nodeEndTypeId = this.auditForm.value.nodeEndTypeId;
    this.incomeDetail.auditNote = this.auditForm.value.auditNote;
    this.httpClient.post('/api/wms/income-common/audit', this.incomeDetail)
      .subscribe((res: any) => {
        this.messageService.success('审批成功！');
        this.goBack();
      })
  }

}
