import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';

@Component({
  selector: 'app-ware-property-right-add',
  templateUrl: 'ware-property-right-add.component.html'
})
export class WarePropertyRightAddComponent implements OnInit{

  validateForm: FormGroup;
  drawerVisible = false;
  enabledValue = true;

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private modal: NzModalRef) {
    this.validateForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      description: [],
      sortNo: [],
      enabled: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
  }

  submitForm(value) {
    // switch控件的True、False转换成Y N
    value.enabled = value.enabled ? 'Y' : 'N';
    this.httpClient.post('/api/wms/ware-property-right/insert', value)
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
