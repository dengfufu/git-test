import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';

@Component({
  selector: 'app-ware-outcome-audit',
  templateUrl: './ware-outcome-audit.component.html',
  styleUrls: ['./ware-outcome-audit.component.less']
})
export class WareOutcomeAuditComponent implements OnInit {

  @Input() outcomeDetail:any;

  loading = false;
  auditForm: FormGroup;
  constructor(private httpClient: HttpClient,
              private formBuilder: FormBuilder,
              private modal: NzModalRef,
              private messageService: NzMessageService,
  ) {
    this.auditForm = this.formBuilder.group({
      nodeEndTypeId: [null, [Validators.required]],
      auditNote: [null, Validators.maxLength(200)]
    })
  }

  ngOnInit() {
  }

  getParams() {
    const params: any = this.auditForm.value;
    params.smallClassId = this.outcomeDetail.smallClassId;
    params.flowInstanceId = this.outcomeDetail.flowInstanceId;
    params.id = this.outcomeDetail.id;
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
    this.modal.destroy({code: 1});
  }

  auditSubmit() {
    if(!this.auditForm.valid) {
      this.auditForm.markAsDirty();
      return;
    }
    this.httpClient.post('/api/wms/outcome-common/audit', this.getParams())
      .subscribe((res: any) => {
        this.messageService.success('审批成功！');
        this.modal.destroy({code: 0});
      })
  }

}
