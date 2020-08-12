import {ChangeDetectorRef, Input, Output, Component, EventEmitter, OnInit, OnChanges, SimpleChanges} from '@angular/core';
import {FormBuilder, FormGroup, FormControl, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {ZonValidators} from '@util/zon-validators';
import {isNull} from '@util/helpers';

@Component({
  selector: 'custom-field',
  templateUrl: 'custom-field.component.html'
})
export class CustomFieldComponent implements OnInit, OnChanges {

  @Input() corpId;            // 企业id
  @Input() formType;          // 表单业务类型
  @Input() data: any[];

  customForm: FormGroup;

  @Output() customFieldDataList: EventEmitter<{fieldId: any; fieldValue: any;}[]>
    = new EventEmitter<{fieldId: any; fieldValue: any;}[]>();


  /*@Output() ValidForm = new EventEmitter<any>();*/

  customFieldList: any[];
  customFieldData: any = {};
  fieldValueStr = '';
  fieldDate: Date;
  checkOptionsOne: any = [];  // 自定义字段多选值

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient) {
    this.customForm = formBuilder.group({});
  }

  ngOnInit(): void {
    if (!isNull(this.corpId)) {
      this.query();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (!isNull(this.corpId)) {
      this.query();
    }
  }


  // 自定义字段查询
  query() {
    this.httpClient.post('/api/anyfix/custom-field/list', {
      corpId: this.corpId,
      formType: this.formType
    })
      .subscribe((res: any) => {
        this.customFieldList = res.data;
        this.customFieldList.forEach(item => {
          if (item.required === 'Y') {
            this.customForm.addControl(item.fieldId, new FormControl(null, [ZonValidators.required()]));
          } else {
            this.customForm.addControl(item.fieldId, new FormControl(null, []));
          }
          if (item.fieldType === 60) {
            this.checkOptionsOne = [];
            item.customFieldDataSourceList.forEach(dataSource => {
              this.checkOptionsOne.push({
                label: dataSource.sourceValue,
                value: dataSource.sourceValue,
                disabled: dataSource.enabled === 'N',
                checked: false
              });
            });
          }
          item.checkBox = this.checkOptionsOne;
        });
        if (this.data && this.data.length > 0) {
          this.data.forEach(customFieldData => {
            if (this.customForm.controls[customFieldData.fieldId]) {
              this.customForm.controls[customFieldData.fieldId].setValue(customFieldData.fieldValue);
            }
          });
        }
      });
    /*this.onTabChange.emit({ key: '123',tab: '456'});*/
  }

  updateSingleChecked(event) {
    /*console.log(event);*/
  }

  submit() {
    const errorList: any[] = [];
    // 存入自定义字段数据
    if (this.customFieldList !== null && this.customFieldList !== undefined) {
      const list: any[] = [];
      this.customFieldList.forEach(item => {
        this.fieldValueStr = '';
        if (this.customForm.value[item.fieldId]) {
          if (item.fieldType === 40) {        // 下拉多选
            this.customForm.value[item.fieldId].forEach(value => {
              if (this.fieldValueStr === '') {
                this.fieldValueStr = value;
              } else {
                this.fieldValueStr = this.fieldValueStr + ',' + value;
              }
            });
          } else if (item.fieldType === 60) {      // 多选框
            this.customForm.value[item.fieldId].forEach(box => {
              if (box.checked === true) {
                if (this.fieldValueStr === '') {
                  this.fieldValueStr = box.value;
                } else {
                  this.fieldValueStr = this.fieldValueStr + ',' + box.value;
                }
              }
            });
          } else if (item.fieldType === 70) {      // 日期
            this.fieldDate = this.customForm.value[item.fieldId];
            this.fieldValueStr = this.fieldDate.getFullYear() + '-' + (this.fieldDate.getMonth() + 1) + '-' + this.fieldDate.getDate();
          } else if (item.fieldType === 80) {      // 时间
            this.fieldDate = this.customForm.value[item.fieldId];
            this.fieldValueStr = this.fieldDate.getHours() + ': ' + this.fieldDate.getMinutes() + ': ' + this.fieldDate.getSeconds();
          } else if (item.fieldType === 90) {
            this.fieldValueStr = '' + this.customForm.value[item.fieldId];
          } else {
            this.fieldValueStr = this.customForm.value[item.fieldId];
          }
          this.customFieldData = {
            fieldId: item.fieldId,
            fieldValue: this.fieldValueStr
          };

          list.push(this.customFieldData);
        } else {
          if (item.required==='Y') {
            errorList.push(item.fieldName);
          }
        }
      });
      this.customFieldDataList.emit(list);
      /*this.ValidForm.emit(this.customForm);*/
    }
    return errorList;
  }
}
