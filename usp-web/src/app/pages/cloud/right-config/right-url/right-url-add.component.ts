import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {ZonValidators} from '@util/zon-validators';

@Component({
  selector: 'app-right-url-add',
  templateUrl: 'right-url-add.component.html'
})
export class RightUrlAddComponent implements OnInit {

  validateForm: FormGroup;
  spinning = false;

  rightTree = [];

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      rightId: [],
      isCommon: [false, [Validators.required]],
      uri: [null, [ZonValidators.required('请求路径'),ZonValidators.maxLength(100),ZonValidators.notEmptyString()]],
      pathMethod: [],
      rightType: [],
      description: ['']
    });
  }

  ngOnInit(): void {
    this.loadRightTree();
  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    value.rightType = this.validateForm.value.isCommon ? 1 : 0;
    this.spinning = true;
    this.httpClient
      .post('/api/uas/sys-right-url/add',
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

  /**
   * 加载权限树
   */
  loadRightTree() {
    this.httpClient
      .post('/api/uas/sys-right/tree', {})
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.rightTree = res.data;
      });
  }

  destroyModal(): void {
    this.modal.destroy();
  }
}
