import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';

@Component({
  selector: 'app-application-config-add',
  templateUrl: 'app-config-add.component.html'
})
export class AppConfigAddComponent implements OnInit {

  validateForm: FormGroup;
  selectedValue: any;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      appId: ['', [Validators.required]],
      configKey: ['', [Validators.required]],
      configValue: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {

  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    this.httpClient
      .post('/api/config/config/add',
        value)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        this.modal.destroy();
      });
  }

  destroyModal(): void {
    this.modal.destroy();
  }

}
