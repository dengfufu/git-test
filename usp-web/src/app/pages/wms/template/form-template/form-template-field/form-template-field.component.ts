import {ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {FormTemplateFieldEditComponent} from '../form-template-field-edit/form-template-field-edit.component';

@Component({
  selector: 'form-template-field',
  templateUrl: 'form-template-field.component.html'
})
export class FormTemplateFieldComponent implements OnInit {

  @Input() id: any;
  @Input() url: any;
  @Input() flag: any;

  loading = false;
  spinning = false;
  list: any;

  referenceList:any;
  mapOfRefer: { [key: string]: string } =  {};
  values: any[] | null = null;

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
    this.loadList();
  }


  queryConfig() {
    this.loadList();
  }

  loadList(): void {
    this.loading = true;
    this.httpClient
      .get('/api/wms/form-template-field/listAllBy/'+ this.id )
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const data = res.data;
        if( data == null){
          this.list = [];
          return;
        }
        this.list = data;
        data.forEach(item => {
          if(item.fieldType === 50) {
            item.referenceList = item.sysListId;
          }else if (item.fieldType === 60) {
            this.findRefer();
            item.referenceList = 100;
          } else {
            item.referenceList = 0;
          }
        });
      });
  }

  findRefer(){
    this.httpClient
      .post('/api/wms/custom-list-main/listEnabledBy', {})
      .pipe(
      )
      .subscribe((res: any) => {
        const data = res.data;
        data.forEach(item => {
          this.mapOfRefer[item.id] = item.name;
        });
      });
  }

  // 进入编辑表单模板页面
  editFormTemplate(data): void {
    if(this.flag === 'Y' ){
      this.messageService.error('该表单模板为内置表单模板，字段不可编辑');
    }else {
      const modal = this.modalService.create({
        nzTitle: '编辑字段',
        nzContent: FormTemplateFieldEditComponent,
        nzFooter: null,
        nzWidth: 700,
        nzComponentParams: {
          object: {id: data.id, formTemplateId: data.formTemplateId,fieldName: data.fieldName, fieldClass: data.fieldClass,
            fieldType: data.fieldType, sysListId: data.sysListId, customListMainId: data.customListMainId, enabled: data.enabled
            , notnull: data.notnull, sortNo: data.sortNo},
        }
      });
      modal.afterOpen.subscribe(() => console.log('[afterOpen] emitted!'));
      modal.afterClose.subscribe(result => {
        if(result === 0){
          this.msg.success('字段编辑成功');
          this.queryConfig();
        }
      });
    }
  }


  // 删除确认
  showDeleteConfirm(data): void {
    if(this.flag === 'Y' ){
      this.messageService.error('该表单模板为内置表单模板，字段不能删除');
    }else {
      this.modalService.confirm({
        nzTitle: '确定删除该字段吗?',
        nzOkText: '确定',
        nzOkType: 'danger',
        nzOnOk: () => this.deleteFormTemplate(data.id),
        nzCancelText: '取消'
      });
    }
    /*if(data.fieldCode ===  null || data.fieldCode === undefined ||data.fieldCode === ''){
      this.messageService.error('该字段为数据库表中已有的字段，不能删除');
    }*/
  }

  // 删除字段
  deleteFormTemplate(id){
    this.httpClient
      .delete('/api/wms/form-template/'+id,
        {})
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        this.msg.success('删除该字段成功');
        this.queryConfig();
      });
  }

  submitForm(): void {
    if(this.flag === 'Y' ){
      this.modal.destroy(1);
    }else {
      this.modal.destroy(0);
    }
  }

  destroyModal(): void {
    this.modal.destroy();
  }
}
