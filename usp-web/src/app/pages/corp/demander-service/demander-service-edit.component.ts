import {ChangeDetectorRef, Input, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';


@Component({
  selector: 'app-corp-demander-service-edit',
  templateUrl: 'demander-service-edit.component.html'
})
export class DemanderServiceEditComponent implements OnInit {

  @Input() demanderService;

  validateForm: FormGroup;
  serviceCorpName: any;
  spinning = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private userService: UserService,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      demanderCorp: [null, [Validators.required]],
      serviceDescription: [''],
      enabled: [],
      id: [null, [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.validateForm.controls.serviceDescription.setValue(this.demanderService.serviceDescription);
    this.validateForm.controls.demanderCorp.setValue(this.demanderService.demanderCorp);
    this.validateForm.controls.enabled.setValue(this.demanderService.enabled === 'Y');
    this.validateForm.controls.id.setValue(this.demanderService.id);

    this.serviceCorpName = this.demanderService.serviceCorpName;
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
      .post('/api/anyfix/demander-service/update',
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
