import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'app-corp-demander-custom-add',
  templateUrl: 'demander-custom-add.component.html'
})
export class DemanderCustomAddComponent implements OnInit {

  validateForm: FormGroup;
  spinning = false;
  customCorpList: Array<any> = [];
  showDetail = false;
  detail: any;
  nzFilterOption = () => true;
  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private userService: UserService,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      customCorp: [],
      customCorpName: ['', [Validators.required]],
      description: [''],
      enabled: [true, []]
    });
  }

  ngOnInit(): void {
    this.matchCustomCorp('');
  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    value.enabled = this.validateForm.value.enabled ? 'Y' : 'N';
    value.demanderCorp = this.userService.currentCorp.corpId;
    if (this.customCorpList.length > 0) {
      this.customCorpList.forEach(corp => {
        if (this.validateForm.controls.customCorpName.value === corp.corpId) {
          value.customCorp = corp.corpId;
          value.customCorpName = corp.corpName;
        }
      });
    }
    this.spinning = true;
    this.httpClient
      .post('/api/anyfix/demander-custom/add',
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
   * @param corpName 企业名称
   */
  matchCustomCorp(corpName?: string): void {
    const params = {
      corpName
    };
    this.httpClient
      .post('/api/anyfix/demander-custom/custom/match', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.customCorpList = res.data;
      });
  }

  onCustomInput(input): void {
    this.showDetail = false;
    if (this.customCorpList.length > 0) {
      this.customCorpList.forEach(corp => {
        if (this.validateForm.controls.customCorpName.value === corp.corpName) {
          this.getDetail(corp.corpId);
        }
      });
    }
    this.matchCustomCorp(input.value);
  }

  getDetail(id){
    this.spinning = true;
    this.httpClient
      .get('/api/uas/corp-registry/' + id)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
          this.spinning = false;
        })
      )
      .subscribe((res: any) => {
        this.detail = res.data;
        this.showDetail = true;
      });
  }
}
