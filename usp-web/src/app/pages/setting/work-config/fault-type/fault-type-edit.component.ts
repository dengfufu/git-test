import {ChangeDetectorRef, Input, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {ZonValidators} from '@util/zon-validators';
import {Result} from '@core/interceptor/result';


@Component({
  selector: 'app-fault-type-edit',
  templateUrl: 'fault-type-edit.component.html'
})
export class FaultTypeEditComponent implements OnInit {

  @Input() faultType;

  validateForm: FormGroup;
  spinning = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient) {

    this.validateForm = this.formBuilder.group({
      name: ['', [ZonValidators.required('故障名称'), ZonValidators.maxLength(50), ZonValidators.notEmptyString()]],
      demanderCorp: ['', [Validators.required]],
      description: ['', []],
      enabled: ['', []],
      id: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.validateForm.controls.name.setValue(this.faultType.name);
    this.validateForm.controls.description.setValue(this.faultType.description);
    this.validateForm.controls.demanderCorp.setValue(this.faultType.demanderCorp);
    this.validateForm.controls.enabled.setValue(this.faultType.enabled === 'Y');
    this.validateForm.controls.id.setValue(this.faultType.id);
  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    value.enabled = this.validateForm.value.enabled ? 'Y' : 'N';
    this.spinning = true;
    this.httpClient
      .post('/api/anyfix/fault-type/update',
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
