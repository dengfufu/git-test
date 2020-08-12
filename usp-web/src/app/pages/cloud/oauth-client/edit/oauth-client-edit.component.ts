import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Page, Result} from '@core/interceptor/result';
import {UserService} from '@core/user/user.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzModalRef} from 'ng-zorro-antd';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'app-oauth-client-edit',
  templateUrl: 'oauth-client-edit.component.html'
})
export class OauthClientEditComponent implements OnInit {

  @Input() client;

  validateForm: FormGroup;
  spinning: boolean;

  constructor(private httpClient: HttpClient,
              private formBuilder: FormBuilder,
              private modal: NzModalRef,
              private userService: UserService) {
    const reg = '^(https?|http):\\/\\/[^\\s$.?#].[^\\s]*$';
    this.validateForm = this.formBuilder.group({
      clientId: [null, [Validators.required]],
      clientSecret: [null, []],
      webServerRedirectUri: [null, [Validators.pattern(reg)]],
      corpId: [null, [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.validateForm.patchValue({
      clientId: this.client.clientId,
      webServerRedirectUri: this.client.webServerRedirectUri,
      corpId: this.userService.currentCorp.corpId
    });
  }

  submitForm(): void {
    this.spinning = true;
    this.httpClient.post('/api/auth/clients/update', this.validateForm.value).pipe(finalize(() => this.spinning = false)).subscribe(() => {
      this.modal.destroy('submit');
    });
  }

  destroyModal(): void {
    this.modal.destroy();
  }

}
