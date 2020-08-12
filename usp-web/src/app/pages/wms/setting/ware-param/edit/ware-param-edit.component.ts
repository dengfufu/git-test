import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';

@Component({
  selector: 'app-ware-system-param-edit',
  templateUrl: 'ware-param-edit.component.html'
})
export class WareParamEditComponent implements OnInit{

  @Input() wareParam;
  validateForm: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private modal: NzModalRef) {
    this.validateForm = this.formBuilder.group({
      id: [],
      paramCode: ['', [Validators.required]],
      paramName: ['', [Validators.required]],
      paramValue: ['', [Validators.required]],
      description: []
    });
  }
  ngOnInit(): void {
    this.validateForm.patchValue(this.wareParam);
  }

  submitForm(value) {
    this.httpClient.post('/api/wms/ware-param/update', value)
      .subscribe(() => {
        this.modal.destroy('submit');
      })
  }

  destroyModal() {
    this.modal.destroy();
  }
}
