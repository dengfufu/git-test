import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {ZonValidators} from '@util/zon-validators';

export class DeviceSpecification {
  id: string;
  name: string;
  enabled: string;
};

@Component({
  selector: 'app-device-specification-edit',
  templateUrl: 'device-specification-edit.component.html'
})
export class DeviceSpecificationEditComponent implements OnInit {

  @Input() object: DeviceSpecification;

  validateForm: FormGroup;

  constructor(private formBuilder: FormBuilder) {
    this.validateForm = this.formBuilder.group({
      id: [], // ID
      name: [null, [ZonValidators.required('规格名称'), ZonValidators.maxLength(50, '规格名称')]], // 规格名称
      enabled: [true] // 是否可用
    });
  }

  ngOnInit(): void {
    if(this.object){
      this.validateForm.patchValue({
        id: this.object.id,
        name: this.object.name,
        enabled: this.object.enabled === 'Y'
      });
    }
  }
}
