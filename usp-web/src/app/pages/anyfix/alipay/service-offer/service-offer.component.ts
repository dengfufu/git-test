import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-service-offer',
  templateUrl: './service-offer.component.html',
  styleUrls: ['./service-offer.component.less']
})
export class ServiceOfferComponent implements OnInit {
  validateForm!: FormGroup;

  constructor(private fb: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private router: Router,) { }

  ngOnInit() {
    this.validateForm = this.fb.group({
      company: [null, [Validators.required]],
      demand: [null, [Validators.required]],
      name: [null, [Validators.required]],
      phone: [null, [Validators.required]],
    });
  }

  //发起工单
  addWork() {
    this.router.navigate(['/anyfix/work-list/add'],
      {queryParams:  {}, relativeTo: this.activatedRoute});
  }

  //提交申请
  submitForm(): void {
    for (const i in this.validateForm.controls) {
      this.validateForm.controls[i].markAsDirty();
      this.validateForm.controls[i].updateValueAndValidity();
    }
  }

}
