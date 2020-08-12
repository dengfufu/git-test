import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {finalize} from 'rxjs/operators';
import {Page} from '@core/interceptor/result';
import {UserService} from '@core/user/user.service';
import {ActivatedRoute} from '@angular/router';
import {isNumber} from '@util/helpers';

@Component({
  selector: 'app-work-fee-verify-verify',
  templateUrl: './work-fee-verify.component.html',
  styleUrls: ['./work-fee-verify.component.less']
})
export class WorkFeeVerifyComponent implements OnInit {

  // 对账单编号
  verifyId: any;
  // 对账明细列表
  detailList: any[] = [];
  // 对账单详情
  workFeeVerify: any = {};
  // 分页
  page = new Page();
  // 页面加载中
  loading = false;
  // 提交加载中
  submitLoading = false;
  editCache: { [key: string]: { edit: boolean, data: any } } = {};
  verifiedMap: {[key: string]: any} = {};

  constructor(
    public httpClient: HttpClient,
    public messageService: NzMessageService,
    // public modalRef: NzModalRef,
    public userService: UserService,
    public activatedRoute: ActivatedRoute
  ) {
    this.verifyId = this.activatedRoute.snapshot.queryParams.verifyId;
  }

  ngOnInit() {
    this.findDetail();
    this.queryDetail();
  }

  // 查询对账单详情
  findDetail() {
    this.httpClient.get(`/api/anyfix/work-fee-verify/${this.verifyId}`)
      .subscribe((res: any) => {
        this.workFeeVerify = res.data;
      });
  }

  // 分页查询明细列表
  queryDetail(reset?: boolean) {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient.post('/api/anyfix/work-fee-verify-detail/query', this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      ).subscribe((res: any) => {
        this.detailList = res.data.list || [];
        this.page.total = res.data.total || 0;
        // 初始化编辑行数据
        this.detailList.forEach((detail: any) => {
          this.editCache[detail.detailId] = {
            edit: false,
            data: Object.assign({}, detail)
          };
        });
        this.detailList.forEach((detail: any) => {
          if (this.verifiedMap[detail.detailId]) {
            Object.assign(detail, this.verifiedMap[detail.detailId].data);
          }
        });
    })
  }

  // 获取查询参数
  getParams() {
    const params = {
      pageNum: this.page.pageNum,
      pageSize: this.page.pageSize,
      verifyId: this.verifyId
    };
    return params;
  }

  // 开始编辑
  startEdit(detailId) {
    this.editCache[detailId].edit = true;
  }

  // 保存编辑
  saveEdit(detailId, index) {
    if (!this.editCache[detailId].data.verifyAmount || this.editCache[detailId].data.verifyAmount <= 0) {
      this.editCache[detailId].data.verifyAmount = 0;
    }
    Object.assign(this.detailList[index], this.editCache[detailId].data);
    this.editCache[detailId].edit = false;
    this.verifiedMap[detailId] = this.editCache[detailId].data;
  }

  // 取消编辑
  cancelEdit(detailId, index) {
    this.editCache[detailId] = {
      edit: false,
      data: Object.assign({}, this.detailList[index])
    }
  }

  // 取消
  cancel() {
    this.goBack();
  }

  goBack() {
    history.back();
  }

  // 提交
  submit() {
    // 编辑中的单元格自动保存
    Object.keys(this.editCache).forEach(detailId => {
      if (this.editCache[detailId].edit) {
        this.editCache[detailId].edit = false;
        if (!isNumber(this.editCache[detailId].data.verifyAmount) || this.editCache[detailId].data.verifyAmount <= 0) {
          this.editCache[detailId].data.verifyAmount = 0;
        }
        this.verifiedMap[detailId] = this.editCache[detailId].data;
      }
    })
    const params = {
      verifiedMap: this.verifiedMap,
      verifyId: this.verifyId
    };
    this.submitLoading = true;
    this.httpClient.post('/api/anyfix/work-fee-verify/verify', params)
      .pipe(
        finalize(() => {
          this.submitLoading = false;
        })
      ).subscribe((res: any) => {
        this.messageService.success('对账成功');
        // this.modalRef.destroy('submit');
        this.goBack();
    })
  }

}
