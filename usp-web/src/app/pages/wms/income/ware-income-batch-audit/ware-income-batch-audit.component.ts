import { Component, OnInit } from '@angular/core';
import {Checkbox, Page} from '@core/interceptor/result';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzMessageService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {finalize} from 'rxjs/operators';
import {WmsService} from '../../wms.service';

@Component({
  selector: 'app-ware-income-audit',
  templateUrl: './ware-income-batch-audit.component.html',
  styleUrls: ['./ware-income-batch-audit.component.less']
})
export class WareIncomeBatchAuditComponent implements OnInit {

  // 表格分页信息
  page = new Page();
  loading = false;
  // 表格复选框
  checkBox: Checkbox = new Checkbox();
  // 表格数据
  wareInComeList: any[] = [];
  // 查询条件表单
  searchForm: FormGroup;
  // 审批填写表单
  auditForm: FormGroup;

  constructor(private httpClient: HttpClient,
              private formBuilder: FormBuilder,
              private messageService: NzMessageService,
              private userService: UserService,
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
    this.httpClient.post('/api/wms/income-common/listIncome', this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      ).subscribe((res: any) => {
        this.wareInComeList = res.data.list;
        this.page.total = res.data.total;
        // 重新赋值dataIdList
        this.checkBox.dataIdList = [];
        if(this.wareInComeList.length > 0) {
          this.wareInComeList.forEach(income => {
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
    const params: any = this.searchForm.value;
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    params.curAuditUserId = this.userService.userInfo.userId;
    params.corpId = this.userService.currentCorp.corpId;
    return params;
  }

  endTypeChange(event) {
    if(event !== '10') {
      this.auditForm.get('nodeEndTypeId').setValidators([Validators.required, Validators.maxLength(200)]);
    }else{
      this.auditForm.get('nodeEndTypeId').setValidators(Validators.maxLength(200));
    }
    this.auditForm.controls.auditNote.updateValueAndValidity();
    this.auditForm.controls.auditNote.markAsDirty();
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
    params.incomeIdList = this.checkBox.getCheckedDataList();
    params.smallClassId = 10;
    params.nodeEndTypeId = this.auditForm.value.nodeEndTypeId;
    params.auditNote = this.auditForm.value.auditNote;
    console.log(params);
    this.httpClient.post('/api/wms/income-common/batchAudit', params)
      .subscribe((res: any) => {
        this.messageService.success('审批成功！');
        this.queryWareIncomeList(true);
      })
  }

}
