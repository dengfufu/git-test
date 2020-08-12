import {ChangeDetectorRef, Component, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzModalRef} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {FlowTemplateService} from '../../flow-template.service';
@Component({
  selector: 'flow-type',
  templateUrl: 'flow-type.component.html'
})
export class FlowTypeComponent implements OnInit {

  @Input() flowType: any;
  @Input() type: any;

  flowTypeForm: FormGroup;
  values: any[] | null = null;

  largeClassName: any = this.flowTemplateService.flowTemplate.largeClassName;
  smallClassName: any = this.flowTemplateService.flowTemplate.smallClassName;

  spinning = false;

  ClassOptions: any = {
    largeClass: [
      {value: 10, text: '入库'},
      {value: 20, text: '出库'},
      {value: 30, text: '调拨'},
      {value: 40, text: '盘点'},
      {value: 50, text: '维修'},
      {value: 60, text: '组装'},
      {value: 70, text: '报废'},
      {value: 80, text: '拆分'},
    ],
    smallClass: []
  };

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private flowTemplateService: FlowTemplateService,
              private userService: UserService) {
    this.flowTypeForm = this.formBuilder.group({
      largeClassId: ['', [Validators.required]],
      smallClassId: ['', [Validators.required]],
      name: ['', [Validators.required]],
      sortNo: ['', [Validators.required]],
      enabled: ['', [Validators.required]],
      description: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    if(this.type === 'edit'){
      if(this.flowType) {
        this.flowTypeForm.patchValue({
          largeClassId: this.flowType.largeClassId,
          smallClassId: this.flowType.smallClassId,
          name: this.flowType.name,
          sortNo: this.flowType.sortNo,
          enabled: this.flowType.enabled,
          description: this.flowType.description,
        })
      }
    }
  }

  largeClassChange(event) {
    if(event === 10){
      this.ClassOptions.smallClass = [
        {value: 10, text: '公司采购入库'},
        {value: 20, text: '厂商物料入库'},
        {value: 30, text: '现有物料入库'},
        {value: 40, text: '厂商返还入库'},
        {value: 50, text: '销售借用归还入库'},
        {value: 60, text: '领用退料入库'}
      ]
    }else if(event === 20){
      this.ClassOptions.smallClass = [
        {value: 70, text: '销售借用出库'},
        {value: 80, text: '公司销售出库'},
        {value: 90, text: '归还厂商出库'},
        {value: 100, text: '物料领用出库'}
      ];
    }else if(event === 30){
      this.ClassOptions.smallClass = [
        {value: 110, text: '物料库存调拨'},
        {value: 120, text: '物料快速转库'},
        {value: 130, text: '待修物料返还'}
      ];
    }else {
      this.ClassOptions.smallClass = [];
    }
    if(event) {
      this.ClassOptions.largeClass.forEach(item => {
        if(event === item.value) {
          this.largeClassName = item.text;
        }
      })
    }
  }

  smallClassChange(event) {
    if(event) {
      this.ClassOptions.smallClass.forEach(item => {
        if(event === item.value) {
          this.smallClassName = item.text;
        }
      })
    }
  }
}
