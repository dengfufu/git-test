import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {UserService} from '@core/user/user.service';
import {NzMessageService, NzModalService, UploadChangeParam, UploadFile} from 'ng-zorro-antd';
import {ZonValidators} from '@util/zon-validators';
import {Result} from '@core/interceptor/result';
import {isNull} from '@util/helpers';
import {GoodsBase} from '../goods-base';
import {GoodsDetailAddComponent} from '../goods-detail/goods-detail-add.component';
import {GoodsDetailEditComponent} from '../goods-detail/goods-detail-edit.component';
import {AreaService} from '@core/area/area.service';

@Component({
  selector: 'app-anyfix-goods-post-add',
  templateUrl: 'post-add.component.html',
  styleUrls: ['post-add.component.less']
})
export class PostAddComponent extends GoodsBase implements OnInit {

  canSubmit = true;

  constructor(public formBuilder: FormBuilder,
              public cdf: ChangeDetectorRef,
              public httpClient: HttpClient,
              public userService: UserService,
              public nzMessageService: NzMessageService,
              public modalService: NzModalService,
              public areaService: AreaService) {

    super(formBuilder, httpClient, nzMessageService, modalService, cdf, userService, areaService);

    this.validateForm = this.formBuilder.group({
      consignCorp: [null, [ZonValidators.required('发货企业')]],
      consignBranch: [], // 发货网点
      consignUser: [null, [ZonValidators.required('发货人')]],
      consignUserName: [null, [ZonValidators.required('发货人'),
        ZonValidators.maxLength(50, '发货人')]],
      consignUserPhone: [null, [ZonValidators.required('发货人联系电话'),
        ZonValidators.phoneOrMobile('发货人联系电话'),
        ZonValidators.maxLength(20, '发货人联系电话')]],
      consignAreaList: [null, [ZonValidators.required('发货行政区划')]],
      consignAddress: [null, [ZonValidators.required('发货地址'),
        ZonValidators.maxLength(100, '发货地址')]],
      consignNote: [null, [ZonValidators.maxLength(200, '发货备注')]],
      transportType: [null, [ZonValidators.required('运输方式')]],
      consignType: [], // 托运方式
      expressType: [], // 快递类型
      expressCorpName: [null, [ZonValidators.maxLength(50, '快递公司名称')]],
      expressNo: [null, [ZonValidators.maxLength(50, '快递单号')]],
      consignTime: [null, [ZonValidators.required('发货时间')]],
      boxNum: [1, [ZonValidators.required('总箱数'),
        ZonValidators.isInt('总箱数'),
        ZonValidators.min(1, '总箱数'),
        ZonValidators.max(100, '总箱数')]],
      goodsPostWorkDtoList: [], // 工单
      receiveCorp: [null, [ZonValidators.required('收货企业')]],
      receiveBranch: [], // 收货网点
      receiveAreaList: [null, [ZonValidators.required('收货行政区划')]],
      receiveAddress: [null, [ZonValidators.required('收货地址'),
        ZonValidators.maxLength(100, '收货地址')]],
      receiver: [null, [ZonValidators.required('收货人')]],
      receiverName: [null, [ZonValidators.required('收货人'),
        ZonValidators.maxLength(50, '收货人')]],
      receiverPhone: [null, [ZonValidators.required('收货人联系电话'),
        ZonValidators.phoneOrMobile('收货人联系电话'),
        ZonValidators.maxLength(20, '收货人联系电话')]],
      payWay: [],
      postFee: [null, [ZonValidators.intOrFloat('邮寄费'),
        ZonValidators.min(0, '邮寄费'),
        ZonValidators.max(99999999, '邮寄费')]],
      goodsPostDetailDtoList: [],
      goodsPostFileList: [],
      goodsPostFileIdList: []
    });
  }

  ngOnInit(): void {
    this.currentCorp = this.userService.currentCorp;
    this.consignCorp = this.currentCorp.corpId;
    this.validateForm.patchValue({
      consignCorp: this.currentCorp.corpId
    });
    this.listArea().then(() => {
      this.initConsignBranch();
    });
    this.initConsignUser();
    this.listTransportType();
    this.listReceiveCorp();
  }

  uploadHandleChange(info: UploadChangeParam): void {
    if (info.file.status === 'uploading') {
      this.canSubmit = false;
    } else {
      this.canSubmit = true;
    }
    let fileList = [...info.fileList];
    fileList = fileList.map(file => {
      if (file.response) {
        file.fileId = file.response.data.fileId;
        file.url = file.response.data.url;
        file.fileName = file.name;
        const index = file.fileName.lastIndexOf('.');
        const ext = file.fileName.substr(index + 1);
        if (!this.isAssetTypeAnImage(ext)) {
          file.thumbUrl = 'assets/anyfix/file.svg';
        }
      }
      return file;
    });
    this.fileList = fileList;
  }

  /**
   * 添加物品
   */
  addGoods(): void {
    const modal = this.modalService.create({
      nzTitle: '添加物品',
      nzContent: GoodsDetailAddComponent,
      nzFooter: null,
      nzMaskClosable: false,
      nzWidth: 800,
      nzComponentParams: {
        boxNum: this.validateForm.get('boxNum').value || 1
      }
    });
    modal.afterClose.subscribe((result: Result) => {
      if (result && result.code === 0) {
        const goods: any = result.data;
        this.goodsList = [...this.goodsList, goods];
        this.goodsList.forEach((goods: any) => {
          this.listFile(goods);
        });
      }
    });
  }

  /**
   * 编辑物品
   */
  editGoods(rowNum: number): void {
    const modal = this.modalService.create({
      nzTitle: '编辑物品',
      nzContent: GoodsDetailEditComponent,
      nzFooter: null,
      nzMaskClosable: false,
      nzWidth: 800,
      nzComponentParams: {
        boxNum: this.validateForm.get('boxNum').value || 1,
        goods: this.goodsList[rowNum]
      }
    });
    modal.afterClose.subscribe((result: Result) => {
      if (result && result.code === 0) {
        const goods: any = result.data;
        this.goodsList[rowNum] = goods;
        this.goodsList.forEach((goods: any) => {
          this.listFile(goods);
        });
      }
    });
  }

  /**
   * 删除行数据
   */
  deleteRow(rowNum: number): void {
    this.goodsList = this.goodsList.filter((item, index) => index !== rowNum);
  }

  /**
   * 提交
   */
  submitForm(): void {
    this.fileList.forEach((file: UploadFile) => {
      if (file.response && file.response.data) {
        this.fileIdList.push(file.response.data.fileId);
      }
    });
    this.validateForm.patchValue({
      goodsPostDetailDtoList: this.goodsList,
      goodsPostWorkDtoList: this.workCheckedList,
      goodsPostFileList: this.fileList,
      goodsPostFileIdList: this.fileIdList
    });
    if (this.validateForm.valid) {
      const transportType: number = this.validateForm.get('transportType').value || 0;
      const consignType: number = this.validateForm.get('consignType').value || 0;
      const expressType: number = this.validateForm.get('expressType').value || 0;
      if (transportType === 2) {
        if (consignType === 0) {
          this.nzMessageService.error('托运方式不能为空！');
          return;
        }
      }
      if (transportType === 3) {
        if (expressType === 0) {
          this.nzMessageService.error('快递类型不能为空！');
          return;
        } else {
          const expressCorpName = this.validateForm.get('expressCorpName').value;
          const expressNo = this.validateForm.get('expressNo').value;
          if (isNull(expressCorpName)) {
            this.nzMessageService.error('快递公司不能为空！');
            return;
          }
          if (expressCorpName.indexOf('[新增]') > 0) {
            this.validateForm.patchValue({
              expressCorpName: expressCorpName.substring(0, expressCorpName.indexOf('[新增]'))
            });
          }
          if (isNull(expressNo)) {
            this.nzMessageService.error('快递单号不能为空！');
            return;
          }
        }
      }
      if (!this.goodsList || this.goodsList.length === 0) {
        this.nzMessageService.error('物品清单不能为空！');
        return;
      } else {
        const subBoxNumList: number[] = [];
        this.goodsList.forEach((goods: any) => {
          subBoxNumList.push(goods.subBoxNum);
        });
        const minBoxNum = Math.min(...subBoxNumList);
        const maxBoxNum = Math.max(...subBoxNumList);
        const boxNum: number = this.validateForm.get('boxNum').value;
        if (minBoxNum > boxNum) {
          this.nzMessageService.error('总箱数小于物品明细中的分箱数！');
          return;
        }
        if (maxBoxNum > boxNum) {
          this.nzMessageService.error('总箱数小于物品明细中的分箱数！');
          return;
        }
      }
      this.spinning = true;
      this.httpClient.post('/api/anyfix/goods-post/add', this.validateForm.value)
        .pipe(
          finalize(() => {
            this.spinning = false;
          })
        )
        .subscribe((result: Result) => {
          if (result.code === 0) {
            this.nzMessageService.success('添加成功');
            history.go(-1);
          }
        });
    } else {
      // tslint:disable-next-line:forin
      for (const i in this.validateForm.controls) {
        this.validateForm.controls[i].markAsDirty();
        this.validateForm.controls[i].updateValueAndValidity();
      }
    }
  }

  goBack(): void {
    history.go(-1);
  }
}
