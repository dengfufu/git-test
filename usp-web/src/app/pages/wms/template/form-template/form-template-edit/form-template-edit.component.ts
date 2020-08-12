import {ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'form-template-edit',
  templateUrl: 'form-template-edit.component.html'
})
export class FormTemplateEditComponent implements OnInit {

  @Input() url;
  @Input() object;
  validateForm: FormGroup;
  values: any[] | null = null;

  id: any;
  spinning = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private userService: UserService,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      sortNo: ['', [Validators.required]],
      enabled: ['', [Validators.required]],
      description: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    if(this.object !== null && this.object !== undefined && this.object !== {}){
      this.validateForm.controls.name.setValue(this.object.name);
      this.validateForm.controls.sortNo.setValue(this.object.sortNo);
      this.validateForm.controls.enabled.setValue(this.object.enabled);
      this.validateForm.controls.description.setValue(this.object.description);
      this.id = this.object.id;
    }
  }

  submitForm(value: any): void {
    this.spinning = true;
    value.id = this.id;
    this.httpClient
      .post(this.url, value)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        this.spinning = false;
        this.modal.destroy(0);
      });
  }

  destroyModal(): void {
    this.modal.destroy();
  }
}
