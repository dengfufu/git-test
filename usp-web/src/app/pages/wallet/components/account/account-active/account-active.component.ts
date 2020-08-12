import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzMessageService} from 'ng-zorro-antd';
import {finalize} from 'rxjs/operators';
import {environment} from '@env/environment';
import {UserService} from '@core/user/user.service';
import {Result} from '@core/interceptor/result';
import {ModulePayStatus, PayService} from '../../../service/pay.service';


@Component({
  selector: 'app-account-active',
  templateUrl: './account-active.component.html',
  styleUrls: ['./account-active.component.less']
})
export class AccountActiveComponent implements OnInit {
  @Input()
  type:'e'|'c' = 'e';
  @Input()
  role: ModulePayStatus;
  @Output() readonly active = new EventEmitter<void>();
  
  form: FormGroup;
  protocol:any;
  serverUrl = environment.server_url;
  submitLoading = false;
  constructor(public formBuilder: FormBuilder,
              public httpClient: HttpClient,
              public payService: PayService,
              private nzMessageService: NzMessageService,
              private userService: UserService) {
    if(this.type === 'e'){
      
      this.payService.getPaymentProtocol().subscribe((result:Result)=>{
        if(result.code === 0){
          const data = result.data[0];
          data.url = this.serverUrl + data.url + '?t=' + data.updateDate;
          this.protocol = data
        }
      })
    }
    
    this.form = formBuilder.group({
      agreement:[false,[Validators.requiredTrue]]
    });
  
  }

  ngOnInit() {
  }
  
  createCorpAccount() {
    this.submitLoading = true;
    const param = {
      corpId: this.userService.currentCorp.corpId,
      protocolIds: [this.protocol.id]
    };
    this.payService.createCorpAccount(param)
    .pipe(finalize(()=>this.submitLoading=false))
    .subscribe((result: Result) =>{
      if(result.code === 0) {
        this.nzMessageService.success('开通成功');
        this.active.emit();
      } else {
        this.nzMessageService.error('开通失败: '+result.msg);
      }
    })
  }
  
  reSignProtocol(){
    this.submitLoading = true;
    const param = {
      corpId: this.userService.currentCorp.corpId,
      protocolIds: [this.protocol.id]
    };
    this.payService.signToB(param)
    .pipe(finalize(()=>this.submitLoading=false))
    .subscribe((result: Result) =>{
      if(result.code === 0) {
        this.nzMessageService.success('重新签约成功');
        this.active.emit();
      } else {
        this.nzMessageService.error('重新签约成功失败: '+result.msg);
      }
    })
  }
  
}
