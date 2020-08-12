import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'app-corp-demander-service-add',
  templateUrl: 'demander-service-add.component.html'
})
export class DemanderServiceAddComponent implements OnInit {

  validateForm: FormGroup;
  serviceCorpOptionLoading = false;
  serviceCorpOptions: any = [];
  spinning = false;
  detail: any = {};
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private userService: UserService,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      demanderCorp: [this.userService.currentCorp.corpId, []],
      serviceCorp: [null, [Validators.required]],
      serviceDescription: [''],
      enabled: [true, []]
    });
  }

  ngOnInit(): void {
    this.matchServiceCorp();
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
      .post('/api/anyfix/demander-service/add',
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

  /**
   * 模糊查询企业
   * @param filter 企业名称
   */
  matchServiceCorp(corpName?: string): void {
    const params = {
      matchFilter: corpName,
      serviceDemander: 'Y'
    };
    this.serviceCorpOptionLoading = true;
    this.httpClient
      .post('/api/anyfix/demander-service/service/list/available', params)
      .pipe(
        finalize(() => {
          this.serviceCorpOptionLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.serviceCorpOptions = res.data || [];
      });
  }

  serviceCorpChange(event) {
    if (!event) {
      return false;
    }
    this.spinning = true;
    this.httpClient
      .get('/api/uas/corp-registry/' + event)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
          this.spinning = false;
        })
      )
      .subscribe((res: any) => {
        this.detail = res.data;
      });
  }
}
