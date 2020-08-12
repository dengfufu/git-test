import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {CustomListDetailComponent} from '../detail-add/custom-list-detail.component';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'app-custom-list',
  templateUrl: 'custom-list-add.component.html'
})
export class CustomListAddComponent implements OnInit{

  @Input() type;
  @Input() id;

  validateForm: FormGroup;
  customListMain: any = {};
  selectOptions: any = {
    listClassList: [
      {id: 10, name: '系统自定义列表'},
      {id: 20, name: '用户自定义列表'}
    ],
    textList: [],
  };
  customDetailList: any = [];

  constructor(private formBuilder: FormBuilder,
              private modalService: NzModalService,
              private msg: NzMessageService,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private modal: NzModalRef) {
    this.validateForm = this.formBuilder.group({
      name: [null, Validators.required],
      listClass: [null, Validators.required],
      sortNo: [],
      description: [],
    });
  }

  ngOnInit(): void {
    if(this.type === 'edit'){
      this.queryConfig();
    }
  }

  queryConfig() {
    this.httpClient
      .get('/api/wms/custom-list/' + this.id)
      .subscribe((res: any) => {
        this.customListMain = res.data;
        this.validateForm.controls.name.setValue(res.data.name);
        this.validateForm.controls.listClass.setValue(res.data.listClass);
        this.validateForm.controls.description.setValue(res.data.description);
        this.validateForm.controls.sortNo.setValue(res.data.sortNo);
        if(res.data.customListDetailList !== null){
          this.customDetailList = res.data.customListDetailList;
        }
      });
  }

  submitForm(value) {
    if(this.customDetailList === null){
      this.msg.error('常用列表明细不能为空，请添加明细');
    }else {
      this.customListMain.name = value.name;
      this.customListMain.listClass = value.listClass;
      if(value.sortNo !== null){
        this.customListMain.sortNo = value.sortNo;
      }
      if(value.description !== null){
        this.customListMain.description = value.description;
      }
      this.customListMain.customListDetailList = this.customDetailList;
    }
    if(this.type === 'add'){
      this.httpClient.post('/api/wms/custom-list/insert', this.customListMain)
        .subscribe(() => {
          this.modal.destroy(0);
        });
    }else if(this.type === 'edit') {
      this.httpClient
        .post('/api/wms/custom-list/update', this.customListMain)
        .pipe(
          finalize(() => {
            this.cdf.markForCheck();
          })
        )
        .subscribe(() => {
          this.modal.destroy(0);
        });
    }
  }

  destroyModal() {
    this.modal.destroy();
  }

  // 添加列表值
  addCustomDetail(): void {
    this.modalService.create({
      nzTitle: '添加列表值',
      nzWidth: 400,
      nzContent: CustomListDetailComponent,
      nzComponentParams: {
            type:'add'
          },
      nzOnOk: (res: any) => {
        if (res.detailForm.value) {
          console.log(res.detailForm.value);
          if(!res.detailForm.value.name){
            this.msg.warning('请填写列表值名称');
            return;
          }
          if(res.detailForm.value.enabled === true){
            res.detailForm.value.enabled = 'Y';
          }else if(res.detailForm.value.enabled === false){
            res.detailForm.value.enabled = 'N';
          }
          this.customDetailList = [ ...this.customDetailList, res.detailForm.value];
          this.cdf.markForCheck();
        }
      }
    })
  }

  // 编辑列表值
  editCustomDetail(data, index): void {
    this.modalService.create({
      nzTitle: '编辑列表值',
      nzWidth: 400,
      nzContent: CustomListDetailComponent,
      nzComponentParams: {
        type:'edit',
        detail: data
      },
      nzOnOk: (res: any) => {
        if (res.detailForm.value) {
          this.customDetailList[index] = res.detailForm.value;
          // 刷新列表
          this.customDetailList = [...this.customDetailList];
        }
      }
    })
  }

  // 删除列表值
  showDeleteCustomDetail(data, index) {
    this.modalService.confirm({
      nzTitle: '确定删除该列表值吗？',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteCustomDetail(data.id, index),
      nzCancelText: '取消'
    });
  }

  private deleteCustomDetail(id, index) {
    // 删除数据
    this.customDetailList = this.customDetailList.filter( (item, i)=> i !== index);
  }
}
