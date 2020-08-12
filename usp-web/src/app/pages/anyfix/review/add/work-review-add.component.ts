import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';


@Component({
  selector: 'app-work-review-add',
  templateUrl: 'work-review-add.component.html'
})
export class WorkReviewAddComponent implements OnInit {

  @Input() workId;

  validateForm: FormGroup;

  isSubmit = false;
  spinning = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      isSolved: ['', [Validators.required]],
      record: ['', [Validators.required, Validators.maxLength(200)]],
    });
  }

  ngOnInit() {


  }


  submitForm(value: any) {
    this.isSubmit = true;
    if (this.validateForm.valid) {
      let params: {};
      params = {
        isSolved: this.validateForm.value.isSolved,
        record: this.validateForm.value.record,
        workId: this.workId,
      };

      const url = '/api/anyfix/work-review/add';
      this.spinning = true;
      this.httpClient
        .post(url, params)
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
  }

  destroyModal() {
    this.modal.destroy();
  }
}
