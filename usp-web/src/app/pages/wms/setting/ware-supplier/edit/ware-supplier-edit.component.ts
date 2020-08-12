import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';

@Component({
  selector: 'app-ware-supplier-edit',
  templateUrl: 'ware-supplier-edit.component.html'
})
export class WareSupplierEditComponent implements OnInit{

  @Input() wareSupplier;
  validateForm: FormGroup;
  enabledValue = true;

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private modal: NzModalRef) {
    this.validateForm = this.formBuilder.group({
      id: [],
      name: ['', [Validators.required]],
      type: ['', [Validators.required]],
      description: [],
      sortNo: [],
      enabled: ['', [Validators.required]]
    });
  }
  ngOnInit(): void {
    this.validateForm.patchValue(this.wareSupplier);
    // 后端Y N 转换成前端true false
    this.enabledValue = this.wareSupplier.enabled === 'Y' ? true : false;
  }

  submitForm(value) {
    // switch控件的True、False转换成Y N
    value.enabled = value.enabled ? 'Y' : 'N';
    this.httpClient.post('/api/wms/ware-supplier/update', value)
      .subscribe(() => {
        this.modal.destroy('submit');
      })
  }

  destroyModal() {
    this.modal.destroy();
  }
}
