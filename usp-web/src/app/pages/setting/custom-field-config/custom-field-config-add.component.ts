import {ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {CustomFieldDataSourceAddComponent} from './source/custom-field-data-source-add.component';
import {ZonValidators} from '@util/zon-validators';
@Component({
  selector: 'custom-field-config-add',
  templateUrl: 'custom-field-config-add.component.html'
})
export class CustomFieldConfigAddComponent implements OnInit {

  @Input() url;
  @Input() object;
  @Input() type;
  validateForm: FormGroup;
  values: any[] | null = null;

  corpId = this.userService.currentCorp.corpId;     // 委托方企业id
  spinning = false;
  flag = false;
  fieldName: any;
  fieldType: any;
  checkBox : any ;
  isContained: boolean;
  fieldOptions: any = {
    fieldTypeList: [
      {value: 10, text: '单行文本'},
      {value: 20, text: '多行文本'},
      {value: 30, text: '下拉单选列表'},
      // {value: 40, text: '下拉多选列表'},
      // {value: 50, text: '单选框'},
      // {value: 60, text: '多选框'},
      // {value: 70, text: '日期'},
      // {value: 80, text: '时间'},
      // {value: 90, text: '纯数字'},
      // {value: 100, text: '小数'},
      // {value: 110, text: '邮箱'},
    ],
    formTypeList: [
      {value: 10, text: '建立工单'},
      // {value: 20, text: '受理工单'},
      // {value: 30, text: '撤回派单'},
      // {value: 40, text: '认领工单'},
      // {value: 50, text: '拒绝工单'},
      // {value: 60, text: '退回派单'},
      // {value: 70, text: '预约工单'},
      // {value: 80, text: '现场签到'},
       {value: 90, text: '完成工单'},
      {value: 100, text: '设备档案'},
    ]
  };

  checkOptionsOne = [];
  radioList = [];
  dataSourceList: any = [];
  IS = true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private modalService: NzModalService,
              private msg: NzMessageService,
              private userService: UserService,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      fieldName: ['', [ZonValidators.required('字段名称'),ZonValidators.maxLength(20),ZonValidators.notEmptyString()]],
      required: ['', [Validators.required]],
      fieldType: ['', [Validators.required]],
      formType: ['', [Validators.required]],
      common: ['', [Validators.required]],
      corpId: [''],
      text: [],
      multilineText: [],
      dropRadioList: [],
      dropMultipleSelectionList: [],
      radio: [],
      checkbox: [],
      date: [],
      time: [],
      number: [],
      decimal: [],
      email: []
    });
  }

  ngOnInit(): void {

    if(this.object !== null && this.object !== undefined && this.object !== {}){
      this.validateForm.controls.fieldName.setValue(this.object.fieldName);
      this.validateForm.controls.corpId.setValue(this.object.corpId);
      this.fieldName = this.object.fieldName;
      this.validateForm.controls.required.setValue(this.object.required);
      this.validateForm.controls.fieldType.setValue(this.object.fieldType);
      this.fieldType = this.object.fieldType;
      this.validateForm.controls.formType.setValue(this.object.formType);
      this.validateForm.controls.common.setValue(this.object.common);
      if(this.object.customFieldDataSourceList !== undefined&&this.object.customFieldDataSourceList.length > 0){
        switch (this.object.fieldType) {
          case 30:
            this.flag = true;
            this.dataSourceList = this.object.customFieldDataSourceList;
            break;
          case 40:
            this.flag = true;
            this.dataSourceList = this.object.customFieldDataSourceList;
            break;
          case 50:
            this.flag = true;
            this.dataSourceList = this.object.customFieldDataSourceList;
            this.dataSourceList.forEach(item => {
                this.radioList.push({
                  label: item.sourceValue,
                  value: item.sourceValue,
                  enabled:item.enabled
                });
            });
            break;
          case 60:
            this.flag = true;
            this.dataSourceList = this.object.customFieldDataSourceList;
            this.dataSourceList.forEach(item => {
              this.checkOptionsOne.push({
                label: item.sourceValue,
                value: item.sourceValue,
                disabled: item.enabled === 'N',
                checked: false
              });
            });
            this.checkBox = this.checkOptionsOne;
            break;
        }
      }
    }
  }

  changeFieldType(event) {
      switch (event) {
        case 10:
          this.fieldType = 10;
          this.dataInit();
          break;
        case 20:
          this.fieldType = 20;
          this.dataInit();
          break;
        case 30:
          this.fieldType = 30;
          this.dataInit();
          break;
        case 40:
          this.fieldType = 40;
          this.dataInit();
          break;
        case 50:
          this.fieldType = 50;
          this.dataInit();
          this.radioListInit();
          break;
        case 60:
          this.fieldType = 60;
          this.dataInit();
          this.checkboxInit();
          break;
        case 70:
          this.fieldType = 70;
          this.dataInit();
          break;
        case 80:
          this.fieldType = 80;
          this.dataInit();
          break;
        case 90:
          this.fieldType = 90;
          this.dataInit();
          break;
        case 100:
          this.fieldType = 100;
          this.dataInit();
          break;
        case 110:
          this.fieldType = 110;
          this.dataInit();
          break;
      }
      if(this.fieldType === 30 || this.fieldType === 40 ||
            this.fieldType === 50 || this.fieldType ===60) {
        this.flag = true;
      } else {
        this.flag = false;
      }
  }

  dataInit() {
    this.dataSourceList = [];
    this.radioList.pop();
    this.checkOptionsOne.pop();
  }

  // 初始化单选框
  radioListInit() {
    if(this.dataSourceList.length > 0){
      this.dataSourceList.forEach(item => {
        if(this.radioList.length === 0){
          this.radioList.push({
            label: item.sourceValue,
            value: item.sourceValue,
            enabled:item.enabled
          });
        }else {
          this.IS = true;
          this.radioList.forEach(i => {
            if(i.label === item.sourceValue){
              this.IS = false;
            }
          });
          // 当radioList里无重复时，添加
          if(this.IS){
            this.radioList.push({
              label: item.sourceValue,
              value: item.sourceValue,
              enabled:item.enabled
            });
          }
        }
      });
    }
  }

  // 初始化多选框
  checkboxInit() {
    this.checkOptionsOne = [];
    if(this.dataSourceList.length > 0){
      this.dataSourceList.forEach(item => {
        if(this.checkOptionsOne.length === 0){
          this.checkOptionsOne =[{
            label: item.sourceValue,
            value: item.sourceValue,
            disabled: item.enabled === 'N',
            checked : false
          }]
        }else {
          this.IS = true;
          this.checkOptionsOne.forEach(i => {
            if(i.label === item.sourceValue){
              this.IS = false;
            }
          });
          // 当checkOptionsOne里无重复时，添加
          if(this.IS){
            this.checkOptionsOne = [ ...this.checkOptionsOne, {
              label: item.sourceValue,
              value: item.sourceValue,
              disabled: item.enabled === 'N',
              checked: false
            }]
          }
        }
      });
      this.checkBox = this.checkOptionsOne;
     }
  }

  submitForm(value: any): void {
    this.spinning = true;
    if(this.type === 'update'){
        value.fieldId = this.object.fieldId;
        value.customCorp = this.object.customCorp;
    }else if(this.type === 'add'){
      value.corpId = this.corpId;
    }
    if(this.dataSourceList.length > 0){
      value.customFieldDataSourceList = this.dataSourceList;
    }else {
      value.customFieldDataSourceList = [];
    }
    this.httpClient
      .post(this.url, value)
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res) => {
        this.modal.destroy(res);
      });
  }

  destroyModal(): void {
    this.modal.destroy();
  }

  log(value: object[]): void {
    console.log(value);
  }

  fieldNameChange(){
    if(this.validateForm.controls.fieldName.value !== null || this.validateForm.controls.fieldName.value !== ''
    ||this.validateForm.controls.fieldName.value !== undefined){
      this.fieldName = this.validateForm.controls.fieldName.value;
    }
  }

  // 添加自定义字段数据源
  addDataSource(): void {
      this.modalService.create({
        nzTitle: '添加数据源',
        nzWidth: 400,
        nzContent: CustomFieldDataSourceAddComponent,
        nzComponentParams: {
          type:'add',
          source: {}
        },
        nzOnOk: (res: any) => {
          if(!res.sourceForm.value.sourceValue) {
            this.msg.warning('请输入数据源值');
            return false;
          }
          this.refreshWidget(res.sourceForm.value,null);
          if(this.isContained) {
            return false;
          }
        }
      })
  }

  refreshWidget(value, index){
    this.isContained = false;
    if(index === null){
      for(const item of this.dataSourceList){
        if(item.sourceValue === value.sourceValue){
          this.msg.warning('该选项已添加，请勿重复添加！');
          this.isContained = true;
          return;
        }
      }
      this.dataSourceList = [ ...this.dataSourceList, value];
    } else {
      console.log('哈哈哈');
      for ( let i =0 ; i < this.dataSourceList.length; i++ ) {
        console.log(this.dataSourceList[i]);
        if( i === index) {
          continue;
        } else {
          if(this.dataSourceList[i].sourceValue === value.sourceValue) {
            this.msg.warning('该选项已添加，请勿重复添加！');
            this.isContained = true;
            return;
          }
        }
      }
      this.dataSourceList[index] = value;
    }

    if(value.enabled === true){
      value.enabled = 'Y';
    }else if(value.enabled === false){
      value.enabled = 'N';
    }
     if(this.fieldType === 50){
      this.radioListInit();
    }else if (this.fieldType === 60){
      this.checkboxInit();
    }
  }

  // 修改数据源
  editDataSource(data, index): void {
    if(this.fieldType === 30||this.fieldType === 40||this.fieldType === 50||this.fieldType === 60){
      this.modalService.create({
        nzTitle: '编辑数据源',
        nzWidth: 400,
        nzContent: CustomFieldDataSourceAddComponent,
        nzComponentParams: {
          type:'edit',
          source: data
        },
        nzOnOk: (res: any) => {
          if(!res.sourceForm.value.sourceValue) {
            this.msg.warning('请输入数据源值');
            return false;
          }
          this.refreshWidget(res.sourceForm.value,index);
          if( this.isContained) {
            return false;
          }
        }
      })
    }else {
      this.msg.error('该字段类型无数据源，无法修改');
    }
  }

  // 删除列表值
  showDeleteDataSource(data, index) {
    this.modalService.confirm({
      nzTitle: '确定删除该列表值吗？',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteDataSource(data, index),
      nzCancelText: '取消'
    });
  }

  deleteDataSource(data, index) {
    // 删除数据
    this.dataSourceList = this.dataSourceList.filter( (item, i)=> i !== index);
    if(this.fieldType === 50){
      this.radioList.splice(index,1);
      this.msg.success('删除该单选框选项成功');
    }else if(this.fieldType === 60){
      this.checkOptionsOne.splice(index,1);
      this.msg.success('删除该多选框选项成功');
    }
  }

  commonChange() {
    if(this.validateForm.controls.common.value === 'Y') {
      this.validateForm.controls.formType.setValidators(null);
    } else {
      this.validateForm.controls.formType.setValidators(Validators.required);
    }
    this.validateForm.controls.formType.updateValueAndValidity();
    this.validateForm.controls.formType.markAsDirty();
  }
}
