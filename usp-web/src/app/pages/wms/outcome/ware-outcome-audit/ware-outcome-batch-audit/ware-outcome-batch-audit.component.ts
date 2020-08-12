import { Component, OnInit } from '@angular/core';
import {Checkbox, Page} from '@core/interceptor/result';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzMessageService} from 'ng-zorro-antd';
import {finalize} from 'rxjs/operators';
import {WmsService} from '../../../wms.service';
import {OutcomeStatusEnum} from '@core/service/enums.service';

@Component({
  selector: 'app-ware-outcome-batch-audit',
  templateUrl: './ware-outcome-batch-audit.component.html',
  styleUrls: ['./ware-outcome-batch-audit.component.less']
})
export class WareOutcomeBatchAuditComponent implements OnInit {

  page = new Page();
  loading = false;
  searchForm: FormGroup;
  auditForm: FormGroup;
  checkBox: Checkbox = new Checkbox();

  wareOutComeList: any[] = [];

  constructor(private httpClient: HttpClient,
              private formBuilder: FormBuilder,
              private messageService: NzMessageService,
              public wmsService: WmsService
  ) {
    this.searchForm = this.formBuilder.group({
      depotId: [],
      smallClassId: [],
      propertyRight: []
    });
    this.auditForm = this.formBuilder.group({
      nodeEndTypeId: [null, [Validators.required]],
      auditNote: [null, Validators.maxLength(200)]
    })
  }

  ngOnInit() {
    this.queryWareIncomeList(false);
    this.wmsService.queryDepotList('');
    this.wmsService.queryPropertyRightList('');
  }

  queryWareIncomeList(reset: boolean) {
    if(reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient.post('/api/wms/outcome-common/list', this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      ).subscribe((res: any) => {
        this.wareOutComeList = res.data.list;
        this.page.total = res.data.total;
        // 重新赋值dataIdList
        this.checkBox.dataIdList = [];
        if(this.wareOutComeList.length > 0) {
          this.wareOutComeList.forEach(income => {
            this.checkBox.dataIdList.push(income.id);
            const normsValue = JSON.parse(income.normsValue);
            income.normsValueName = Object.keys(normsValue).map(key => {
              const val = normsValue[key];
              return key + ':' + val;
            }).join(',');
          })
        }
    })
  }

  getParams() {
    const params = Object.assign({}, this.searchForm.value);
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    params.flowNodeTypeList = [20,30,60];
    params.outcomeStatusList = [OutcomeStatusEnum.NO_OUTCOME];
    return params;
  }

  endTypeChange(event) {
    if(event !== '10') {
      this.auditForm.get('nodeEndTypeId').setValidators([Validators.required, Validators.maxLength(200)]);
      this.auditForm.updateValueAndValidity();
    }else{
      this.auditForm.get('nodeEndTypeId').setValidators(Validators.maxLength(200));
      this.auditForm.updateValueAndValidity();
    }
  }

  goBack() {
    history.back();
  }

  auditSubmit() {
    const checkedIdList = this.checkBox.getCheckedDataList();
    if(!checkedIdList || checkedIdList.length <= 0) {
      this.messageService.warning('请至少选择一条记录审批！');
    }
    if(!this.auditForm.valid) {
      this.auditForm.markAsDirty();
      return;
    }
    const params: any = {};
    params.idList = this.checkBox.getCheckedDataList();
    params.smallClassId = 10;
    params.nodeEndTypeId = this.auditForm.value.nodeEndTypeId;
    params.auditNote = this.auditForm.value.auditNote;
    this.httpClient.post('/api/wms/outcome-common/batchAudit', params)
      .subscribe((res: any) => {
        this.messageService.success('审批成功！');
        this.queryWareIncomeList(true);
      })
  }

}
