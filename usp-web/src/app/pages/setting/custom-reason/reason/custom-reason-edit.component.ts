import {ChangeDetectorRef, Input, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {ZonValidators} from '@util/zon-validators';
import {Result} from '@core/interceptor/result';


@Component({
  selector: 'custom-reason-edit',
  templateUrl: 'custom-reason-edit.component.html'
})
export class CustomReasonEditComponent implements OnInit {

  @Input() customReason;

  validateForm: FormGroup;
  spinning = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      description: ['', []],
      reasonType: ['', [Validators.required]],
      enabled: ['', []],
      id: ['', [Validators.required]],
      customCorp: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.validateForm.controls.name.setValue(this.customReason.name);
    this.validateForm.controls.description.setValue(this.customReason.description);
    this.validateForm.controls.reasonType.setValue(this.customReason.reasonType);
    this.validateForm.controls.customCorp.setValue(this.customReason.customCorp);
    this.validateForm.controls.enabled.setValue(this.customReason.enabled === 'Y');
    this.validateForm.controls.id.setValue(this.customReason.id);
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
      .post('/api/anyfix/custom-reason/update',
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
