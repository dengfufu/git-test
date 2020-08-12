import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {ZonValidators} from '@util/zon-validators';
import {Result} from '@core/interceptor/result';

@Component({
  selector: 'app-work-type-add',
  templateUrl: 'work-type-add.component.html'
})
export class WorkTypeAddComponent implements OnInit {

  validateForm: FormGroup;
  workSysTypeList = [];
  // 委托商列表
  demanderCorpList = [];

  spinning = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      demanderCorp: [null, [ZonValidators.required('委托商')]],
      name: [null, [ZonValidators.required('工单类型'), ZonValidators.maxLength(20), ZonValidators.notEmptyString()]],
      sysType: [null, [Validators.required]],
      description: [''],
      enabled: [true, []]
    });
  }

  ngOnInit(): void {
    this.loadDemanderCorp();
    this.loadWorkSysType();
  }

  // 加载委托商下拉列表
  loadDemanderCorp() {
    this.httpClient.get(`/api/anyfix/demander/list`).subscribe((result: Result) => {
      if (result.code === 0) {
        this.demanderCorpList = result.data || [];
        if (this.demanderCorpList.length === 1) {
          this.validateForm.patchValue({
            demanderCorp: this.demanderCorpList[0].demanderCorp
          });
        }
      }
    });
  }

  loadWorkSysType() {
    this.httpClient
      .get('/api/anyfix/work-sys-type/list')
      .subscribe((res: any) => {
        this.workSysTypeList = res.data;
      });
  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    value.enabled = this.validateForm.controls.enabled.value ? 'Y' : 'N';
    this.spinning = true;
    this.httpClient
      .post('/api/anyfix/work-type/add',
        value)
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

}
