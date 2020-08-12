import {ChangeDetectorRef, Input, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';


@Component({
  selector: 'app-corp-demander-custom-edit',
  templateUrl: 'demander-custom-edit.component.html'
})
export class DemanderCustomEditComponent implements OnInit {

  @Input() demanderCustom;

  validateForm: FormGroup;
  customCorpList: Array<any> = [];
  spinning = false;
  customCorp = null;
  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      customCorpName: [null, [Validators.required]],
      customCorp: [],
      description: [],
      enabled: [],
      customId: [null, [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.matchCustomCorp(this.demanderCustom.customCorpName);
    this.validateForm.controls.description.setValue(this.demanderCustom.description);
    this.validateForm.controls.customCorpName.setValue(this.demanderCustom.customCorpName);
    this.validateForm.controls.enabled.setValue(this.demanderCustom.enabled === 'Y');
    this.validateForm.controls.customId.setValue(this.demanderCustom.customId);
  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    value.enabled = this.validateForm.controls.enabled.value ? 'Y' : 'N';
    if (this.customCorpList.length > 0) {
      this.customCorpList.forEach(corp => {
        if (this.validateForm.controls.customCorpName.value === corp.corpId) {
          value.customCorp = corp.corpId;
          value.customCorpName = corp.corpName;
        } else if (this.validateForm.controls.customCorpName.value === corp.corpName
          && this.demanderCustom.customCorp === corp.corpId) {
          value.customCorp = corp.corpId;
          value.customCorpName = corp.corpName;
        }
      });
    }
    this.customCorp = value.customCorp;
    this.spinning = true;
    this.httpClient
      .post('/api/anyfix/demander-custom/update',
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
    this.matchCustomCorp(input.value);
  }

}
