import {ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'flow-node-handler-edit',
  templateUrl: 'flow-node-handler-edit.component.html'
})
export class FlowNodeHandlerEditComponent implements OnInit {

  @Input() type: any;
  @Input() handler: any;
  @Input() handlerType: any;
  corpId = this.userService.currentCorp.corpId;
  handlerForm: FormGroup;
  flag = false;
  text = '请选择指定角色';
  spinning = false;
  textList: any[] = [];

  handlerTypeList = [
    {id: 10, name: '发起人'},
    {id: 20, name: '指定角色'},
    {id: 30, name: '指定用户'}
  ];

  constructor(private formBuilder: FormBuilder,
              private userService: UserService,
              private httpClient: HttpClient) {
    this.handlerForm = this.formBuilder.group({
      handlerTypeId: ['', [Validators.required]],
      handlerId: ['', [Validators.required]],
      isMainDirector: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    if(this.type === 'edit'){
      if(this.handlerType === 20){
        this.text = '请选择指定角色';
        this.textList = [
          {id: '10', name: '张三'},
          {id: '20', name: '李四'}
        ]
      }else if(this.handlerType === 30){
        this.text = '请选择指定用户';
        this.httpClient
          .post('/api/uas/corp-user/user/list', {corpId: this.corpId})
          .pipe(
          )
          .subscribe((res: any) => {
            const data = res.data;
            data.forEach(item => {
              this.textList.push({
                id: item.userId,
                name: item.userName
              });
            });
          });
      }
      this.handlerForm.controls.handlerTypeId.setValue(this.handler.handlerTypeId);
      this.handlerForm.controls.handlerId.setValue(this.handler.handlerId);
      this.handlerForm.controls.isMainDirector.setValue(this.handler.isMainDirector);
    } else if(this.type === 'add'){
      if(this.handlerType === 20){
        this.handlerForm.controls.handlerTypeId.setValue(20);
        this.text = '请选择指定角色';
        this.textList = [
          {id: '10', name: '张三'},
          {id: '20', name: '李四'}
        ]
      }else if(this.handlerType === 30){
        this.handlerForm.controls.handlerTypeId.setValue(30);
        this.text = '请选择指定用户';
        this.httpClient
          .post('/api/uas/corp-user/user/list', {corpId: this.corpId})
          .pipe(
          )
          .subscribe((res: any) => {
            const data = res.data;
            data.forEach(item => {
              this.textList.push({
                id: item.userId,
                name: item.userName
              });
            });
          });
      }
    }
  }

  changeHandlerType(event){
    this.textList.splice(0);
    /*this.mapOfHandler = {};*/
    if(event === 10) {
      this.flag = true;
      this.text = '请选择发起人';
    }else if (event === 20) {
      this.flag = false;
      this.text = '请选择指定角色';
      this.textList = [
        {id: '10', name: '张三'},
        {id: '20', name: '李四'}
      ]
    } else if (event === 30) {
      this.flag = false;
      this.text = '请选择指定用户';
      this.httpClient
        .post('/api/uas/corp-user/user/list', {corpId: this.corpId})
        .pipe(
        )
        .subscribe((res: any) => {
          const data = res.data;
          data.forEach(item => {
            this.textList.push({
              id: item.userId,
              name: item.userName
            });
          });
        });
    }
  }
}
