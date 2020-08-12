import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {ZonValidators} from '@util/zon-validators';
import {Result} from '@core/interceptor/result';

@Component({
  selector: 'app-fault-type-add',
  templateUrl: 'fault-type-add.component.html'
})
export class FaultTypeAddComponent implements OnInit {

  validateForm: FormGroup;
  spinning = false;
  // 委托商列表
  demanderCorpList = [];

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient) {

    this.validateForm = this.formBuilder.group({
      demanderCorp: [null, [ZonValidators.required('委托商')]],
      name: ['', [ZonValidators.required('故障名称'), ZonValidators.maxLength(50), ZonValidators.notEmptyString()]],
      description: ['', []],
      enabled: [true, []]
    });
  }

  ngOnInit(): void {
    this.loadDemanderCorp();
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

  submitForm(value: any): void {
    this.spinning = true;
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    value.enabled = this.validateForm.value.enabled ? 'Y' : 'N';
    this.httpClient
      .post('/api/anyfix/fault-type/add',
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
