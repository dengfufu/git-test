import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'app-work-post-edit',
  templateUrl: './work-post-edit.component.html',
  styleUrls: ['./work-post-edit.component.less']
})
export class WorkPostEditComponent implements OnInit {

  // 页面类型 add为添加页面，mod为编辑页面
  @Input() type: any;
  // 编辑页面传入
  @Input() workPost: any;
  // 表单
  form: FormGroup;
  // 加载中
  isLoading = false;
  // 快递公司下拉选项
  postCorpOptions: any[] = [];

  constructor(
    private modalRef: NzModalRef,
    private httpClient: HttpClient,
    private formBuilder: FormBuilder,
    private messageService: NzMessageService,
    private userService: UserService
  ) {
    this.form = this.formBuilder.group({
      postWay: [],
      postCorpName: [],
      postNumber: [],
      postage: ['']
    });
  }

  ngOnInit() {
    // 如果是修改页面，则初始化表单
    if (this.type === 'mod') {
      this.form.patchValue({
        postWay: this.workPost.postWay.toString(),
        postCorpName: this.workPost.postCorpName,
        postNumber: this.workPost.postNumber,
        postage: this.workPost.postage
      });
    }
  }

  // 加载快递公司下拉选项
  loadPostCorpOptions(name) {
    // 加上分页为限制记录数
    const params = {
      corpId: this.userService.currentCorp.corpId,
      postCorpName: name
    };
    this.httpClient.post('/api/anyfix/work-post/matchCorp', params)
      .subscribe((res: any) => {
        this.postCorpOptions = res.data;
      });
  }

  // 输入搜索快递公司
  postCorpInput(event) {
    this.workPost.postCorpName = event.value;
    this.loadPostCorpOptions(event.value);
  }

  // 选中快递公司
  selectPostCorp(event) {
    this.workPost.postCorpName = event.nzValue;
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
    const params: any = {
      workId: this.workPost.workId,
      postWay: this.form.value.postWay,
      postCorpName: this.workPost.postCorpName,
      postNumber: this.form.value.postNumber || 0,
      postage: this.form.value.postage || 0
    };
    let url = '';
    let msg = '';
    if (this.type === 'add') {
      url = '/api/anyfix/work-post/add';
      msg = '添加成功！';
    } else if (this.type === 'mod') {
      params.postId = this.workPost.postId;
      url = '/api/anyfix/work-post/update';
      msg = '修改成功！';
    }
    this.httpClient.post(url, params)
      .subscribe((res: any) => {
        this.messageService.success(msg);
        this.modalRef.destroy('submit');
      });
  }

}
