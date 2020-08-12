import {ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
@Component({
  selector: 'custom-field-data-source-add',
  templateUrl: 'custom-field-data-source-add.component.html'
})
export class CustomFieldDataSourceAddComponent implements OnInit {

  @Input() source;
  @Input() type;
  sourceForm: FormGroup;

  spinning = false;
  enabled : any;

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private modal: NzModalRef) {
    this.sourceForm = this.formBuilder.group({
      sourceValue: [null, Validators.required],
      enabled: [null, Validators.required]
    });
  }

  ngOnInit() {
    this.enabled = true;
    if (this.type === 'edit') {
      this.sourceForm.controls.sourceValue.setValue(this.source.sourceValue);
      if (this.source.enabled === 'Y') {
        this.enabled = true;
        this.sourceForm.value.enabled = true;
      } else if (this.source.enabled === 'N') {
        this.enabled = false;
        this.sourceForm.value.enabled = false;
      }
    }
  }

  switch(event) {
    if (event === true) {
      this.sourceForm.value.enabled = 'Y';
    } else if (event === false) {
      this.sourceForm.value.enabled = 'N';
    }
  }
}
