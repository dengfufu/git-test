import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Page, Result} from '@core/interceptor/result';
import {UserService} from '@core/user/user.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzModalRef} from 'ng-zorro-antd';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'app-oauth-client-add',
  templateUrl: 'oauth-client-add.component.html'
})
export class OauthClientAddComponent implements OnInit {

  validateForm: FormGroup;
  spinning: boolean;

  constructor(private httpClient: HttpClient,
              private formBuilder: FormBuilder,
              private modal: NzModalRef,
              private userService: UserService) {
    const reg = '^(https?|http):\\/\\/[^\\s$.?#].[^\\s]*$';
    this.validateForm = this.formBuilder.group({
      clientId: [null, [Validators.required]],
      clientSecret: [null, [Validators.required]],
      webServerRedirectUri: [null, [Validators.pattern(reg)]],
      corpId: [null, [Validators.required]]
    });
  }

  ngOnInit() {
    this.validateForm.controls.corpId.setValue(this.userService.currentCorp.corpId);
  }

  submit() {
    this.spinning = true;
    this.httpClient.post('/api/auth/clients/add', this.validateForm.value).pipe(finalize(() => this.spinning = false)).subscribe(() => {
      this.modal.destroy('submit');
    });
  }

  destroyModal(): void {
    this.modal.destroy();
  }

}
