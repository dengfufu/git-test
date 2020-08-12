import {ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'form-template-field-edit',
  templateUrl: 'form-template-field-edit.component.html'
})
export class FormTemplateFieldEditComponent implements OnInit {

  @Input() object;
  validateForm: FormGroup;
  values: any[] | null = null;

  referenceList:any;
  fieldOptions: Array<{ value: any; text: string }> = [];

  id: any;
  formTemplateId: any;
  spinning = false;

  flag = false;
  referFlag= false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private userService: UserService,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      fieldName: ['', [Validators.required]],
      fieldClass: ['', [Validators.required]],
      fieldType: ['', [Validators.required]],
      referenceList: ['', [Validators.required]],
      enabled: ['', [Validators.required]],
      notnull: ['', [Validators.required]],
      sortNo: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    if(this.object !== null && this.object !== undefined && this.object !== {}){
      this.validateForm.controls.fieldName.setValue(this.object.fieldName);
      this.validateForm.controls.fieldClass.setValue(this.object.fieldClass);
      if(this.object.fieldClass === 10) {
        this.flag = true;
        this.referFlag = true;
      }
      this.validateForm.controls.fieldType.setValue(this.object.fieldType);
      if(this.object.fieldType === 50) {
        this.validateForm.controls.referenceList.setValue(this.object.sysListId);
        this.initSysListData();
      }else if (this.object.fieldType === 60) {
        this.validateForm.controls.referenceList.setValue(this.object.customListMainId);
        this.initCustomListData();
      } else {
        this.validateForm.controls.referenceList.setValue(0);
      }
      this.validateForm.controls.enabled.setValue(this.object.enabled);
      this.validateForm.controls.notnull.setValue(this.object.notnull);
      this.validateForm.controls.sortNo.setValue(this.object.sortNo);
      this.id = this.object.id;
      this.formTemplateId = this.object.formTemplateId;
    }
  }

  submitForm(value: any): void {
    this.spinning = true;
    value.id = this.id;
    value.formTemplateId = this.formTemplateId;
    if(value.fieldType === 50) {
      value.sysListId = value.referenceList;
    }else if (value.fieldType === 60) {
      value.customListMainId = value.referenceList;
    }
    this.httpClient
      .post('/api/wms/form-template-field/modField', value)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        this.spinning = false;
        this.modal.destroy(0);
      });
  }

  destroyModal(): void {
    this.modal.destroy();
  }

  changeFieldType(event){
    this.fieldOptions.splice(0);
    if(event === 50) {
      this.referFlag= false;
      this.validateForm.controls.referenceList.setValue(this.object.sysListId);
      this.initSysListData();
    }else if (event === 60) {
      this.referFlag= false;
      this.validateForm.controls.referenceList.setValue(this.object.customListMainId);
      this.initCustomListData();
    } else {
      this.validateForm.controls.referenceList.setValue(0);
      this.fieldOptions = [{value: 0, text: ''}];
      this.referFlag = true;
    }
  }

  initCustomListData(){
    this.httpClient
      .post('/api/wms/custom-list-main/listEnabledBy', {})
      .pipe(
      )
      .subscribe((res: any) => {
        const data = res.data;
        const listOfOption: Array<{ value: any; text: string }> = [];
        data.forEach(item => {
          listOfOption.push({
            value: item.id,
            text: item.name
          });
        });
        this.fieldOptions = listOfOption;
      });
  }

  initSysListData(){
    this.fieldOptions.push(
      {
        value: 10,
        text: '品牌列表',
      },
      {
        value: 20,
        text: '型号列表',
      },
      {
        value: 30,
        text: '分类列表',
      },
      {
        value: 40,
        text: '库房列表',
      },
      {
        value: 50,
        text: '状态列表',
      },
      {
        value: 60,
        text: '产权列表',
      },
      {
        value: 70,
        text: '区域列表',
      },
      {
        value: 80,
        text: '用户列表',
      },
      {
        value: 90,
        text: '供应商列表',
      }
    )
  }
}
