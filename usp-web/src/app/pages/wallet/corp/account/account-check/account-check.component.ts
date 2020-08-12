import { Component, OnInit } from '@angular/core';
import {ModulePayStatus} from '../../../service/pay.service';
import {FormBuilder} from '@angular/forms';
import {ActivatedRoute, DefaultUrlSerializer, Router, UrlTree} from '@angular/router';

@Component({
  selector: 'app-account-check',
  templateUrl: './account-check.component.html',
  styleUrls: ['./account-check.component.less']
})
export class AccountCheckComponent implements OnInit {
  redirectUrl;
  redirectParams;
  role:ModulePayStatus;
  type = 'e';
  
  constructor(public formBuilder: FormBuilder,
              public router: Router,
              private activatedRoute: ActivatedRoute) {
    const params = this.activatedRoute.snapshot.queryParams;
  
    console.log('CreateAccountComponent get params:', params);
    const url:UrlTree = new DefaultUrlSerializer().parse(params.redirectUrl);
    this.redirectParams = url.queryParams;
    this.redirectUrl = params.redirectUrl.split('?')[0];
    this.type = params.type;
    this.role = params.role;
  }

  ngOnInit() {
  }
  
  onActive() {
    this.router.navigate([this.redirectUrl], {queryParams: this.redirectParams});
  }
}
