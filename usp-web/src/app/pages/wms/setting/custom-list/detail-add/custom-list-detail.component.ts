import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';

@Component({
  selector: 'custom-list-detail',
  templateUrl: 'custom-list-detail.component.html'
})
export class CustomListDetailComponent implements OnInit{

  @Input() detail;
  @Input() type;
  detailForm: FormGroup;

  spinning = false;
  enabled: any;

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private modal: NzModalRef) {
    this.detailForm = this.formBuilder.group({
      name: [null, Validators.required],
      enabled: [null, Validators.required],
      sortNo: [],
      description: []
    });
  }

  ngOnInit() {
    this.enabled = false;
    if(this.type === 'edit'){
      this.detailForm.patchValue(this.detail);
      if(this.detail.enabled === 'Y'){
        this.detailForm.value.enabled = true;
      }else if(this.detail.enabled === 'N'){
        this.detailForm.value.enabled = false;
      }
    }
  }

  switch(event) {
    if(event === true){
      this.detailForm.value.enabled = 'Y';
    }else if(event === false){
      this.detailForm.value.enabled = 'N';
    }
  }
}
