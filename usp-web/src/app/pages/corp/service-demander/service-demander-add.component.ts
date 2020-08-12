import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'app-corp-service-demander-add',
  templateUrl: 'service-demander-add.component.html'
})
export class ServiceDemanderAddComponent implements OnInit {

  @Input() isForUpdate = false;
  @Input() data: any;
  validateForm: FormGroup;
  demanderCorpOptionLoading = false;
  demanderCorpOptions = [];
  spinning = false;
  detail : any = {};
  nzFilterOption = () => true;


  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private userService: UserService,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      id: [],
      serviceCorp: [this.userService.currentCorp.corpId, []],
      demanderCorp: [null, [Validators.required]],
      description: [],
      enabled: [true, []]
    });
  }

  ngOnInit(): void {
    this.isForUpdate ? this.setDataForUpdate() : this.matchDemanderCorp();
  }

  setDataForUpdate() {
    if(this.data) {
      this.demanderCorpOptions = [{
        corpId: this.data.demanderCorp,
        corpName: this.data.demanderCorpName
      }];
      this.validateForm.patchValue({
          id: this.data.id,
          demanderCorp: this.data.demanderCorp,
          description: this.data.description
        }
      );
      this.detail.region = this.data.region;
      this.detail.address = this.data.address;
      this.detail.telephone = this.data.telephone;
    }
  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    value.enabled = this.validateForm.value.enabled ? 'Y' : 'N';
    this.spinning = true;
    const suffix = this.isForUpdate ? 'demander/update' : 'add';
    this.httpClient
      .post('/api/anyfix/demander-service/' + suffix,
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
  matchDemanderCorp(corpName?: string): void {
    const params = {
      matchFilter: corpName,
      forDemander: true,
      enabled : 'Y'
    };
    this.demanderCorpOptionLoading = true;
    this.httpClient
      .post('/api/anyfix/demander-service/service/list/available', params)
      .pipe(
        finalize(() => {
          this.demanderCorpOptionLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.demanderCorpOptions = res.data;
      });
  }

  demanderCorpChange(event) {
    if(!event) {
      return false;
    }
    this.spinning = true;
    this.httpClient
      .get('/api/uas/corp-registry/' + event )
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
