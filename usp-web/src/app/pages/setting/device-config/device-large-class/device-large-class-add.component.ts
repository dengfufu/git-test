import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {ZonValidators} from '@util/zon-validators';
import {ValueEnum} from '@core/service/enums.service';
import {Result} from '@core/interceptor/result';

@Component({
  selector: 'app-device-large-class-add',
  templateUrl: 'device-large-class-add.component.html'
})
export class DeviceLargeClassAddComponent implements OnInit {

  validateForm: FormGroup;

  spinning = false;

  demanderCorpList: any[] = [];

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      name: [null, [ZonValidators.required('设备大类名称'),ZonValidators.maxLength(50),ZonValidators.notEmptyString()]],
      sortNo: [null, [Validators.required,Validators.max(ValueEnum.MAX_INT)]],
      description: ['', []],
      enabled: [true, []],
      corp:[null,ZonValidators.required('委托商')]

    });
  }

  ngOnInit(): void {
    this.listDemanderCorp();
    this.getMaxSortNo();
  }

  getMaxSortNo(){
    const corp = this.validateForm.value.corp || 0;
    this.httpClient
      .get('/api/device/device-large-class/max/sortNo/' + corp)
      .subscribe((res: any) => {
        let sortNo = res.data;
        sortNo = sortNo ? (sortNo + 1) : 1;
        this.validateForm.patchValue({
          sortNo
        });
      });
  }

  /**
   * 委托商列表
   */
  listDemanderCorp() {
    this.httpClient.get(`/api/anyfix/demander/list`).subscribe((result: Result) => {
      if (result.code === 0) {
        this.demanderCorpList = result.data || [];
        if (this.demanderCorpList.length === 1) {
          this.validateForm.patchValue({
            corp: this.demanderCorpList[0].demanderCorp
          });
        }
      }
    });
  }

  /**
   * 委托商变化重新获取编号值
   */
  demanderCorpChange(e){
    this.validateForm.controls.sortNo.reset()
    this.getMaxSortNo();
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
      .post('/api/device/device-large-class/add',
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
