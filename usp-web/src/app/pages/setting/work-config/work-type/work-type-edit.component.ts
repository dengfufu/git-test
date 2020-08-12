import {ChangeDetectorRef, Input, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {ZonValidators} from '@util/zon-validators';
import {Result} from '@core/interceptor/result';


@Component({
  selector: 'app-work-type-edit',
  templateUrl: 'work-type-edit.component.html'
})
export class WorkTypeEditComponent implements OnInit {

  @Input() workType;

  validateForm: FormGroup;
  workSysTypeList = [];

  spinning = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      name: [null, [ZonValidators.required('工单类型'), ZonValidators.maxLength(20), ZonValidators.notEmptyString()]],
      sysType: [null, [Validators.required]],
      demanderCorp: [null, [Validators.required]],
      description: ['', []],
      enabled: [null, []],
      id: [null, [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.loadWorkSysType();
    this.validateForm.controls.name.setValue(this.workType.name);
    this.validateForm.controls.description.setValue(this.workType.description);
    this.validateForm.controls.sysType.setValue(this.workType.sysType);
    this.validateForm.controls.demanderCorp.setValue(this.workType.demanderCorp);
    this.validateForm.controls.enabled.setValue(this.workType.enabled === 'Y');
    this.validateForm.controls.id.setValue(this.workType.id);
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
      .post('/api/anyfix/work-type/update',
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
