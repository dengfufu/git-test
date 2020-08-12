import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {ZonValidators} from '@util/zon-validators';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'app-service-item-add',
  templateUrl: 'service-item-add.component.html'
})
export class ServiceItemAddComponent implements OnInit {

  validateForm: FormGroup;
  spinning = false;
  // 委托商下拉选项
  demanderCorpOptions: any[] = [];
  // 委托商下拉选项加载中
  demanderCorpOptionLoading = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient,
              public userService: UserService) {

    this.validateForm = this.formBuilder.group({
      name: ['', [ZonValidators.required('服务项目名称'),ZonValidators.maxLength(20),ZonValidators.notEmptyString()]],
      demanderCorp: [null, [ZonValidators.required('委托商')]],
      description: ['', []],
      enabled: [true, []]
    });
  }

  ngOnInit(): void {
  }

  // 查询委托商下拉框
  searchDemanderCorp() {
    const params = {
      serviceCorp: this.userService.currentCorp.corpId,
      enabled: 'Y'
    };
    this.demanderCorpOptionLoading = true;
    this.httpClient.post('/api/anyfix/demander-service/demander/list', params)
      .pipe(
        finalize(() => {
          this.demanderCorpOptionLoading = false;
        })
      ).subscribe((res: any) => {
      this.demanderCorpOptions = res.data;
    });
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
      .post('/api/anyfix/service-item/add',
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
