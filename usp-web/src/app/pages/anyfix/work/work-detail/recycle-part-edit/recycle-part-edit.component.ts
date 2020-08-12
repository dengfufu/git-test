import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'app-recycle-part-edit',
  templateUrl: './recycle-part-edit.component.html',
  styleUrls: ['./recycle-part-edit.component.less']
})
export class RecyclePartEditComponent implements OnInit {

  @Input() type: string;
  @Input() recyclePart: any;
  // 表单
  form: FormGroup;
  // 加载中
  isLoading = false;
  // 分类下拉选项
  catalogOptions: any[] = [];
  // 品牌下拉选项
  brandOptions: any[] = [];
  // 型号下拉选项
  modelOptions: any[] = [];

  constructor(
    private modalRef: NzModalRef,
    private httpClient: HttpClient,
    private formBuilder: FormBuilder,
    private messageService: NzMessageService,
    private userService: UserService
  ) {
    this.form = this.formBuilder.group({
      catalogName: [],
      brandName: [],
      modelName: [],
      wareSerial: [],
      quantity: []
    });
  }

  ngOnInit() {
    if (this.type === 'mod') {
      this.form.patchValue({
        catalogName: this.recyclePart.catalogName,
        brandName: this.recyclePart.brandName,
        modelName: this.recyclePart.modelName,
        wareSerial: this.recyclePart.wareSerial,
        quantity: this.recyclePart.quantity
      });
    }
  }

  // 加载分类选项
  loadCatalogOptions(name) {
    const params = {
      catalogName: name,
      corpId: this.userService.currentCorp.corpId
    };
    this.httpClient.post('/api/anyfix/work-ware/catalog/match', params)
      .subscribe((res: any) => {
        this.catalogOptions = res.data;
      });
  }

  // 分类输入事件
  catalogInput(event) {
    this.recyclePart.catalogName = event.value;
    this.loadCatalogOptions(event.value);
  }

  // 分类选择事件
  selectCatalog(event) {
    this.recyclePart.catalogName = event.nzLabel;
  }

  // 加载品牌选项
  loadBrandOptions(name) {
    const params = {
      brandName: name,
      corpId: this.userService.currentCorp.corpId
    };
    this.httpClient.post('/api/anyfix/work-ware/brand/match', params)
      .subscribe((res: any) => {
        this.brandOptions = res.data;
      });
  }

  // 品牌输入事件
  brandInput(event) {
    this.recyclePart.brandName = event.value;
    this.loadBrandOptions(event.value);
  }

  // 品牌选择事件
  selectBrand(event) {
    this.recyclePart.brandName = event.nzLabel;
  }

  // 加载型号选项
  loadModelOptions(name) {
    // 加上分页是限制记录数
    const params = {
      modelName: name,
      catalogName: this.recyclePart.catalogName,
      brandName: this.recyclePart.brandName,
      corpId: this.userService.currentCorp.corpId
    };
    this.httpClient.post('/api/anyfix/work-ware/model/match', params)
      .subscribe((res: any) => {
        this.modelOptions = res.data;
      });
  }

  // 型号输入事件
  modelInput(event) {
    this.recyclePart.modelName = event.value;
    this.loadModelOptions(event.value);
  }

  // 型号选择事件
  selectModel(event) {
    this.recyclePart.modelName = event.nzLabel;
  }

  // 数字框格式
  formatterRmb = (value: number) => `￥ ${value}`;
  parserRmb = (value: string) => value.replace('￥ ', '');

  // 取消
  cancel() {
    this.modalRef.destroy('cancel');
  }

  // 提交
  submit() {
    const params: any = this.form.value;
    params.workId = this.recyclePart.workId;
    params.catalogName = this.recyclePart.catalogName;
    params.brandName = this.recyclePart.brandName;
    params.modelName = this.recyclePart.modelName;
    let url = '';
    let msg = '';
    // 添加提交
    if (this.type === 'add') {
      url = '/api/anyfix/work-ware/recycle/add';
      msg = '添加成功';
      // 修改提交
    } else if (this.type === 'mod') {
      params.recycleId = this.recyclePart.recycleId;
      url = '/api/anyfix/work-ware/recycle/update';
      msg = '修改成功';
    }
    this.httpClient.post(url, params)
      .subscribe((res: any) => {
        this.messageService.success(msg);
        this.modalRef.destroy('submit');
      });
  }

}
