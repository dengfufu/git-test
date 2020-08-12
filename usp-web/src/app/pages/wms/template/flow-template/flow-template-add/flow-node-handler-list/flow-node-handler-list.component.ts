import {ChangeDetectorRef, Component, Input, OnInit, } from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {FlowNodeHandlerEditComponent} from '../flow-node-handler-edit/flow-node-handler-edit.component';

@Component({
  selector: 'flow-node-handler-list',
  templateUrl: 'flow-node-handler-list.component.html'
})
export class FlowNodeHandlerListComponent implements OnInit {

  @Input() handlerList: any;
  @Input() mapOfHandler: any;
  loading = false;
  sortFlag = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private userService: UserService,
              private modal: NzModalRef,
              private msg: NzMessageService,
              private messageService: NzMessageService,
              private modalService: NzModalService) {
  }

  ngOnInit(): void {
    /* console.log(this.handlerList);
     console.log(this.mapOfHandler);*/
  }

  // 添加处理人
  addHandler(): void {
    if(this.handlerList){
      const modal = this.modalService.create({
        nzTitle: '添加处理人',
        nzContent: FlowNodeHandlerEditComponent,
        nzWidth: 700,
        nzComponentParams: {
          type: 'add',
          handler: {},
          handlerType: this.handlerList[0].handlerTypeId
        },
        nzOnOk: (res: any) => {
          this.sortFlag = false;
          if (res.handlerForm.value) {
            this.handlerList.forEach(item => {
              if(item.handlerId === res.handlerForm.value.handlerId){
                this.msg.warning('该处理人已添加，请勿重复添加！')
                this.sortFlag = true;
              }
            });
            if(this.sortFlag){
              return false;
            }
            if(!res.handlerForm.value.handlerId){
              this.messageService.warning('处理人不能为空，请重新填写');
              return false;
            }else  if(!res.handlerForm.value.isMainDirector){
              this.messageService.warning('是否主处理人不能为空，请重新填写');
              return false;
            }
            this.handlerList.push(res.handlerForm.value);
            this.cdf.markForCheck();
          }
        }
      });
    }else {
      this.msg.error('该节点为默认节点，无处理人，请勿重试！')
    }
    /*modal.afterOpen.subscribe(() => console.log('[afterOpen] emitted!'));
    modal.afterClose.subscribe(result => {
      if(result === 0){
        this.msg.success('处理人添加成功');
      }
    });*/
  }


  // 编辑处理人
  editHandler(data, index): void {
    const modal = this.modalService.create({
      nzTitle: '编辑处理人',
      nzContent: FlowNodeHandlerEditComponent,
      nzWidth: 700,
      nzComponentParams: {
        type: 'edit',
        handler: data,
        handlerType: data.handlerTypeId
      },
      nzOnOk: (res: any) => {
        this.sortFlag = false;
        if (res.handlerForm.value) {
          if(this.handlerList[index].handlerId !== res.handlerForm.value.handlerId){
            this.handlerList.forEach(item => {
              if(item.handlerId === res.handlerForm.value.handlerId){
                this.sortFlag = true;
              }
            });
          }
          if(this.sortFlag){
            this.msg.warning('该处理人已存在，请勿重复编辑！')
            return false;
          }
          this.handlerList[index] = res.handlerForm.value;
          this.cdf.markForCheck();
        }
      }
    });
  }


  // 删除确认
  deleteConfirm(index): void {
    this.modalService.confirm({
      nzTitle: '确定删除该处理人吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteHandler(index),
      nzCancelText: '取消'
    });
  }

  // 删除处理人
  deleteHandler(index){
    this.handlerList = this.handlerList.filter( (item, i)=> i !== index);
    this.msg.success('删除该处理人成功');
  }
}
