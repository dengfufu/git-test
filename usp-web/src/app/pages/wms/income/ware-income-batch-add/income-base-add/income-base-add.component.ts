import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {WmsService} from '../../../wms.service';

@Component({
  selector: 'app-add-income-base',
  templateUrl: './income-base-add.component.html',
  styleUrls: ['./income-base-add.component.less']
})
export class IncomeBaseAddComponent implements OnInit {

  @Input()
  incomeMain: any;

  // 入库主表表单
  addBaseForm: FormGroup;

  constructor(private modalRef: NzModalRef,
              private formBuilder: FormBuilder,
              public wmsService: WmsService
  ) {
    this.addBaseForm = this.formBuilder.group({
      smallClassId: ['', [Validators.required]],
      smallClassName: [''],
      depotId: ['', [Validators.required]],
      depotName: [''],
      incomeDate: [''],
      propertyRight: ['', [Validators.required]],
      propertyRightName: [''],
      contId: ['', [Validators.maxLength(50)]],
      purchaseDetailId: ['', [Validators.maxLength(50)]],
      // supplierId: [''],
      // supplierName: ['']
      secondHand: ['']
    })
  }

  ngOnInit() {
    if(this.incomeMain) {
      this.addBaseForm.patchValue({
        smallClassId: this.incomeMain.smallClassId,
        smallClassName: this.incomeMain.smallClassName,
        depotId: this.incomeMain.depotId,
        depotName: this.incomeMain.depotName,
        propertyRight: this.incomeMain.propertyRight,
        propertyRightName: this.incomeMain.propertyRightName,
        contId: this.incomeMain.contId,
        purchaseDetailId: this.incomeMain.purchaseDetailId,
        secondHand: this.incomeMain.secondHand,
        supplierId: this.incomeMain.supplierId,
        supplierName: this.incomeMain.supplierName
      })
    }else {
      this.addBaseForm.patchValue({
        secondHand: 'N'
      })
    }
    this.wmsService.queryDepotList('');
    this.wmsService.queryPropertyRightList('');
  }

  depotChange(event) {
    if(event && event.length > 0) {
      this.wmsService.selectOptions.depotList.forEach(depot => {
        if(event === depot.id) {
          this.addBaseForm.controls.depotName.setValue(depot.name);
        }
      })
    }else{
      this.addBaseForm.controls.depotName.reset();
    }
  }

  propertyRightChange(event) {
    if(event && event > 0) {
      this.wmsService.selectOptions.propertyRightList.forEach(propertyRight => {
        if(event === propertyRight.id) {
          this.addBaseForm.controls.propertyRightName.setValue(propertyRight.name);
        }
      })
    }else{
      this.addBaseForm.controls.propertyRightName.reset();
    }
  }

}
