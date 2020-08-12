import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {ZonValidators} from '@util/zon-validators';
import {Result} from '@core/interceptor/result';

@Component({
  selector: 'app-custom-reason-add',
  templateUrl: 'custom-reason-add.component.html'
})
export class CustomReasonAddComponent implements OnInit {

  @Input() reasonType = 0;

  validateForm: FormGroup;
  spinning = false;
  // 委托商列表
  demanderCorpList = [];

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      customCorp: [null, [ZonValidators.required('委托商')]],
      name: ['', [Validators.required]],
      description: [''],
      reasonType: ['', [Validators.required]],
      enabled: [true, []]
    });
  }

  ngOnInit(): void {
    this.validateForm.controls.reasonType.setValue(this.reasonType);
    this.loadDemanderCorp();
  }

  // 加载委托商下拉列表
  loadDemanderCorp() {
    this.httpClient.get(`/api/anyfix/demander/list`).subscribe((result: Result) => {
      if (result.code === 0) {
        this.demanderCorpList = result.data || [];
        if (this.demanderCorpList.length === 1) {
          this.validateForm.patchValue({
            customCorp: this.demanderCorpList[0].demanderCorp
          });
        }
      }
    });
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
      .post('/api/anyfix/custom-reason/add',
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
