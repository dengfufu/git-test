import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {ZonValidators} from '@util/zon-validators';

import {Result} from '@core/interceptor/result';

@Component({
  selector: 'app-file-config-add',
  templateUrl: 'file-config-add.component.html',
  styleUrls: ['file-config-add.component.less']
})
export class FileConfigAddComponent implements OnInit {

  @Input() isForUpdate = false;
  @Input() data;
  @Input() refId;
  validateForm: FormGroup;
  avatarUrl: string;
  loading= false;
  fileList: Array<any> = [];
  logo: string;
  previewVisible = false;
  previewImage : any;
  /*文件上传路径*/
  uploadAction = '/api/file/uploadFile';

  spinning = false;
  workTypeOptions: any[] = [];
  demanderCorpList: any[] = [];
  formTypeOptions: any[] = [];
  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      groupName: [null, [ZonValidators.required('类型名称'),ZonValidators.maxLength(50),ZonValidators.notEmptyString()]],
      description: ['', []],
      formType:[1, ZonValidators.required('')],
      minNum: [1, [ZonValidators.required('附件数量'), ZonValidators.min(1, '附件数量'), this.shouldBeLess] ],
      maxNum: [1, [ZonValidators.required('附件数量'), ZonValidators.min(1, '附件数量'), ZonValidators.max(10, '附件数量'), this.shouldBeMore]],
      workType: [null, ZonValidators.required('工单类型')],
      refId: [this.refId, [ZonValidators.required('委托商')]],
      id: []
    });
  }

  ngOnInit(): void {
    this.listDemanderCorp();
    this.validateForm.controls.refId.setValue(this.refId);
    this.loadWorkType();
    this.setFormTypeData();
    if(this.isForUpdate) {
      this.patchData();
    }
  }

  patchData() {

    this.validateForm.patchValue({
      groupName: this.data.groupName,
      description: this.data.description,
      formType: this.data.formType,
      minNum: this.data.minNum,
      maxNum: this.data.maxNum,
      workType: this.data.workType,
      id: this.data.id,
    });
  }

  shouldBeLess(control: FormControl) {
    // tslint:disable-next-line:no-non-null-assertion
    if (!control || !control.parent) {
      return null;
    }
    if ( control.value) {
      const maxNum = control.parent.get('maxNum').value;
      if(maxNum && control.value > maxNum) {
        return {shouldBeLess: true, error: true, explain: '不能大于最多附件数量'};
      }
    }
    return null;
  }



  shouldBeMore(control: FormControl) {
    if (!control || !control.parent) {
      return null;
    }
    if ( control.value) {
      const minNum = control.parent.get('minNum').value;
      control.parent.get('minNum').setValue(minNum);
      if(minNum && control.value < minNum) {
        return {shouldBeLess: true, error: true, explain: '不能小于最少附件数量'};
      }
    }
    return null;
  }


  setFormTypeData() {
    this.formTypeOptions = [
      {
        value: 1,
        name: '服务完成'
      },
    ]
  }
  /**
   * 委托商列表
   */
  listDemanderCorp() {
    this.httpClient.get(`/api/anyfix/demander/list`).subscribe((result: Result) => {
      if (result.code === 0) {
        this.demanderCorpList = result.data || [];
      }
    });
  }

  submitForm(): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    this.spinning = true;
    const suffix = this.isForUpdate? 'update': 'add';
    this.httpClient
      .post('/api/anyfix/file-config/' + suffix,
        this.validateForm.value)
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        this.modal.destroy('submit');
      });
  }

  destroyModal(): void {
    this.modal.destroy();
  }


  /**
   * 列出工单类型
   */
  loadWorkType() {
    this.httpClient.post(`/api/anyfix/work-type/list`, {demanderCorp: 0, enabled: 'Y'})
      .subscribe((result: Result) => {
      if (result.code === 0) {
        this.workTypeOptions = result.data || [];
      }
    });
  }

}
