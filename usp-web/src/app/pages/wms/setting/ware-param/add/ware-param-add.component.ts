import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';

@Component({
  selector: 'app-ware-system-param-add',
  templateUrl: 'ware-param-add.component.html'
})
export class WareParamAddComponent implements OnInit{

  validateForm: FormGroup;
  drawerVisible = false;

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private modal: NzModalRef) {
    this.validateForm = this.formBuilder.group({
      paramCode: ['', [Validators.required]],
      paramName: ['', [Validators.required]],
      paramValue: ['', [Validators.required]],
      description: []
    });
  }

  ngOnInit(): void {
  }

  submitForm(value) {
    this.httpClient.post('/api/wms/ware-param/insert', value)
      .subscribe(() => {
        this.modal.destroy('submit');
      })
  }

  destroyModal() {
    this.modal.destroy();
  }

  clearForm() {
    this.validateForm.reset();
  }

  openDrawer() {
    this.drawerVisible = true;
  }

  closeDrawer() {
    this.drawerVisible = false;
  }
}
