import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';

@Component({
  selector: 'app-ware-express-edit',
  templateUrl: 'ware-express-edit.component.html'
})
export class WareExpressEditComponent implements OnInit{

  @Input() wareExpress;
  validateForm: FormGroup;
  enabledValue = true;

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private modal: NzModalRef) {
    this.validateForm = this.formBuilder.group({
      id: [],
      name: ['', [Validators.required]],
      description: [],
      sortNo: [],
      enabled: ['', [Validators.required ]]
    });
  }
  ngOnInit(): void {
    this.validateForm.patchValue(this.wareExpress);
    // 后端Y N 转换成前端true false
    this.enabledValue = this.wareExpress.enabled === 'Y' ? true : false;
  }

  submitForm(value) {
    // switch控件的True、False转换成Y N
    value.enabled = value.enabled ? 'Y' : 'N';
    this.httpClient.post('/api/wms/express-company/update', value)
      .subscribe(() => {
        this.modal.destroy('submit');
      })
  }

  destroyModal() {
    this.modal.destroy();
  }
}
