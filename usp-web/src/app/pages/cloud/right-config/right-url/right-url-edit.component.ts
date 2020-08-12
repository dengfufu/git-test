import {ChangeDetectorRef, Input, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {ZonValidators} from '@util/zon-validators';


@Component({
  selector: 'app-right-url-edit',
  templateUrl: 'right-url-edit.component.html'
})
export class RightUrlEditComponent implements OnInit {

  @Input() sysAuthRight;

  validateForm: FormGroup;
  spinning = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      id:[null, [Validators.required]],
      rightId: [],
      isCommon: [null, [Validators.required]],
      uri: [null, [ZonValidators.required('请求路径'),ZonValidators.maxLength(100),ZonValidators.notEmptyString()]],
      pathMethod: [],
      rightType: [],
      description: ['']
    });
  }

  ngOnInit(): void {
    this.validateForm.controls.id.setValue(this.sysAuthRight.id);
    this.validateForm.controls.rightId.setValue(this.sysAuthRight.rightId);
    this.validateForm.controls.uri.setValue(this.sysAuthRight.uri);
    this.validateForm.controls.pathMethod.setValue(this.sysAuthRight.pathMethod);
    this.validateForm.controls.description.setValue(this.sysAuthRight.description);
    this.validateForm.controls.isCommon.setValue(this.sysAuthRight.rightType === 1);
  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    value.rightType = this.validateForm.value.isCommon ? 1 : 0;
    this.spinning = true;
    this.httpClient
      .post('/api/uas/sys-right-url/update',
        value)
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

}
