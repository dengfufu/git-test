import {ChangeDetectorRef,Input, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {ZonValidators} from '@util/zon-validators';


@Component({
  selector: 'app-application-config-add',
  templateUrl: 'remote-way-edit.component.html'
})
export class RemoteWayEditComponent implements OnInit {

  @Input() remoteWay;

  validateForm: FormGroup;
  spinning = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      name: ['', [ZonValidators.required('处理方式'),ZonValidators.notEmptyString(),ZonValidators.maxLength(20)]],
      serviceCorp: ['', [Validators.required]],
      description: ['',ZonValidators.maxLength(200)],
      enabled: ['', []],
      id: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.validateForm.controls.name.setValue(this.remoteWay.name);
    this.validateForm.controls.description.setValue(this.remoteWay.description);
    this.validateForm.controls.serviceCorp.setValue(this.remoteWay.serviceCorp);
    this.validateForm.controls.enabled.setValue(this.remoteWay.enabled === 'Y');
    this.validateForm.controls.id.setValue(this.remoteWay.id);
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
      .post('/api/anyfix/remote-way/update',
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
