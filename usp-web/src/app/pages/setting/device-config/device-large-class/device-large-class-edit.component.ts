import {ChangeDetectorRef, Input, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {ZonValidators} from '@util/zon-validators';
import {ValueEnum} from '@core/service/enums.service';
import {Result} from '@core/interceptor/result';

@Component({
  selector: 'app-device-large-class-edit',
  templateUrl: 'device-large-class-edit.component.html'
})
export class DeviceLargeClassEditComponent implements OnInit {

  @Input() deviceLargeClass;
  validateForm: FormGroup;

  spinning = false;

  demanderCorpList = [];

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      id: [],
      name: [null, [ZonValidators.required('设备大类名称'),ZonValidators.maxLength(50),ZonValidators.notEmptyString()]],
      sortNo: ['', [Validators.required,Validators.max(ValueEnum.MAX_INT)]],
      corp: ['', [Validators.required]],
      description: ['', []],
      enabled: ['', []]
    });
  }

  ngOnInit(): void {
    this.listDemanderCorp();
    this.initValue();
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

  initValue(){
    this.validateForm.controls.id.setValue(this.deviceLargeClass.id);
    this.validateForm.controls.name.setValue(this.deviceLargeClass.name);
    this.validateForm.controls.sortNo.setValue(this.deviceLargeClass.sortNo);
    this.validateForm.controls.corp.setValue(this.deviceLargeClass.corp);
    this.validateForm.controls.description.setValue(this.deviceLargeClass.description);
    this.validateForm.controls.enabled.setValue(this.deviceLargeClass.enabled === 'Y');
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
      .post('/api/device/device-large-class/update',
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
