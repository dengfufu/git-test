import {ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
@Component({
  selector: 'custom-list-name-edit',
  templateUrl: 'custom-list-name-edit.component.html'
})
export class CustomListNameEditComponent implements OnInit {

  @Input() id;
  validateForm: FormGroup;
  values: any[] | null = null;
  customListMain: any={};

  corpId = this.userService.currentCorp.corpId;

  spinning = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private userService: UserService,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      name: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.httpClient
      .get('/api/wms/custom-list/' + this.id)
      .subscribe((res: any) => {
        this.customListMain = res.data;
        this.validateForm.controls.name.setValue(res.data.name);
      });
  }

  submitForm(value: any): void {
    this.customListMain.name = value.name;
    this.spinning = true;
    this.httpClient
      .post('/api/wms/custom-list/updateCustomListMain', this.customListMain)
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
