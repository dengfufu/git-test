import {ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
@Component({
  selector: 'form-template-add',
  templateUrl: 'form-template-add.component.html'
})
export class FormTemplateAddComponent implements OnInit {

  @Input() url;
  validateForm: FormGroup;
  values: any[] | null = null;

  corpId = this.userService.currentCorp.corpId;
  referId: any;
  selectedValue: any;

  spinning = false;

  templateOptions: Array<{ value: any; text: string }> = [];

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private userService: UserService,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      referId: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.httpClient
      .post('/api/wms/form-template/listForSelectBy', {corpId: this.corpId})
      .pipe(
      )
      .subscribe((res: any) => {
        const data = res.data;
        const listOfOption: Array<{ value: any; text: string }> = [];
        data.forEach(item => {
          listOfOption.push({
            value: item.id,
            text: item.name
          });
        });
        this.templateOptions = listOfOption;
        this.selectedValue = listOfOption[0].value;
      });
  }

  submitForm(value: any): void {
    this.spinning = true;
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
