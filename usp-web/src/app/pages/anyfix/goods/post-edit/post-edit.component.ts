import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {UserService} from '@core/user/user.service';
import {NzMessageService, NzModalService, UploadChangeParam, UploadFile} from 'ng-zorro-antd';
import {ZonValidators} from '@util/zon-validators';
import {Result} from '@core/interceptor/result';
import {isNull} from '@util/helpers';
import {ActivatedRoute} from '@angular/router';
import {AreaService} from '@core/area/area.service';
import {GoodsDetailAddComponent} from '../goods-detail/goods-detail-add.component';
import {GoodsBase} from '../goods-base';
import {GoodsDetailEditComponent} from '../goods-detail/goods-detail-edit.component';

@Component({
  selector: 'app-anyfix-goods-post-edit',
  templateUrl: 'post-edit.component.html',
  styleUrls: ['post-edit.component.less']
})
export class PostEditComponent extends GoodsBase implements OnInit {

  canSubmit = true;

  constructor(public formBuilder: FormBuilder,
              public cdf: ChangeDetectorRef,
              public activatedRoute: ActivatedRoute,
              public httpClient: HttpClient,
              public userService: UserService,
              public nzMessageService: NzMessageService,
              public areaService: AreaService,
              public modalService: NzModalService) {
    super(formBuilder, httpClient, nzMessageService, modalService, cdf, userService, areaService);

    this.postId = this.activatedRoute.snapshot.queryParams.postId;

    this.validateForm = this.formBuilder.group({
      id: [],
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
    this.consignCorp = this.currentCorp.corpId;
    this.initGoodsPostDetail().then(() => {
      this.initConsignBranch();
      this.matchConsignUser().then((userList: any[]) => {
        let match = false;
        userList.forEach(option => {
          if (option.userId === this.goodsPost.consignUser) {
            match = true;
          }
        });
        if (!match && this.goodsPost.consignUser !== '0') {
          this.consignUserList.unshift({
            userId: this.goodsPost.consignUser,
            userName: this.goodsPost.consignUserName
          });
        }
        if (!match && this.goodsPost.consignUser === '0') {
          this.consignUserList.unshift({
            userId: '0',
            userName: '[' + this.goodsPost.consignUserName + ']'
          });
        }
      });
      this.initReceiveBranch();
      this.matchReceiveUser().then((userList: any[]) => {
        let match = false;
        userList.forEach(option => {
          if (option.userId === this.goodsPost.receiver) {
            match = true;
          }
        });
        if (!match && this.goodsPost.receiver !== '0') {
          this.receiveUserList.unshift({
            userId: this.goodsPost.receiver,
            userName: this.goodsPost.receiverName
          });
        }
        if (!match && this.goodsPost.receiver === '0') {
          this.receiveUserList.unshift({
            userId: '0',
            userName: '[' + this.goodsPost.receiverName + ']'
          });
        }
      });
      this.listReceiveCorp();
      this.initFileList();
    });
    this.listArea().then();
    this.listTransportType();
  }

  /**
   * 查询寄送单详情
   */
  initGoodsPostDetail() {
    this.spinning = true;
    return this.httpClient
      .get('/api/anyfix/goods-post/detail/' + this.postId)
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .toPromise().then((result: Result) => {
        this.goodsPost = result.data;
        if (!this.goodsPost || isNull(this.goodsPost.id)) {
          this.nzMessageService.error('物品寄送单不存在！');
          history.go(-1);
          return;
        }
        if (this.goodsPost) {
          this.goodsList = this.goodsPost.goodsPostDetailDtoList || [];
          this.goodsList.forEach((goods: any) => {
            this.listFile(goods);
          });
          const workList: any[] = this.goodsPost.goodsPostWorkDtoList || [];
          this.workCheckedList = workList;
          let isFirst = true;
          workList.forEach((work: any) => {
            if (isFirst) {
              this.workCodes = work.workCode;
              isFirst = false;
            } else {
              this.workCodes = this.workCodes + ',' + work.workCode;
            }
          });
        }
        this.consignCorp = this.goodsPost.consignCorp;
        this.consignCorpName = this.goodsPost.consignCorpName;
        this.receiveCorp = this.goodsPost.receiveCorp;
        this.validateForm.patchValue({
          id: this.goodsPost.id,
          consignUserPhone: this.goodsPost.consignUserPhone,
          consignAreaList: this.areaService.getAreaListByDistrict(this.goodsPost.consignArea),
          consignAddress: this.goodsPost.consignAddress,
          consignNote: this.goodsPost.consignNote,
          transportType: this.goodsPost.transportType,
          consignType: this.goodsPost.consignType,
          expressType: this.goodsPost.expressType,
          expressCorpName: this.goodsPost.expressCorpName,
          expressNo: this.goodsPost.expressNo,
          consignTime: this.goodsPost.consignTime,
          boxNum: this.goodsPost.boxNum,
          receiveAreaList: this.areaService.getAreaListByDistrict(this.goodsPost.receiveArea),
          receiveAddress: this.goodsPost.receiveAddress,
          receiverPhone: this.goodsPost.receiverPhone,
          payWay: this.goodsPost.payWay === 0 ? null : this.goodsPost.payWay,
          postFee: this.goodsPost.postFee,
          goodsPostDetailDtoList: this.goodsList,
          goodsPostWorkDtoList: this.workCheckedList,
          goodsPostFileList: this.fileList,
          goodsPostFileIdList: this.fileIdList
        });
        this.validateForm.controls.consignCorp.setValue(this.goodsPost.consignCorp,
          {onlySelf: true, emitViewToModelChange: false});
        this.validateForm.controls.consignUser.setValue(this.goodsPost.consignUser,
          {onlySelf: true, emitViewToModelChange: false});
        this.validateForm.controls.consignUserName.setValue(this.goodsPost.consignUserName,
          {onlySelf: true, emitViewToModelChange: false});
        this.validateForm.controls.consignBranch.setValue(
          this.goodsPost.consignBranch === '0' ? null : this.goodsPost.consignBranch,
          {onlySelf: true, emitViewToModelChange: false});
        this.validateForm.controls.receiveCorp.setValue(this.goodsPost.receiveCorp,
          {onlySelf: true, emitViewToModelChange: false});
        this.validateForm.controls.receiveBranch.setValue(
          this.goodsPost.receiveBranch === '0' ? null : this.goodsPost.receiveBranch,
          {onlySelf: true, emitViewToModelChange: false});
        this.validateForm.controls.receiver.setValue(this.goodsPost.receiver,
          {onlySelf: true, emitViewToModelChange: false});
        this.validateForm.controls.receiverName.setValue(this.goodsPost.receiverName,
          {onlySelf: true, emitViewToModelChange: false});
      });
  }

  /**
   * 初始化附件列表
   */
  initFileList() {
    this.fileList = this.goodsPost.fileList || [];
    this.fileList.forEach((file: any) => {
      file.uid = file.fileId;
      file.name = file.fileName;
      file.status = 'done';
      file.url = this.showFileUrl + file.fileId;
      file.thumbUrl = file.url;
      file.response = {};
      file.response.data = {};
      file.response.data.fileId = file.fileId;
      file.response.data.url = file.url;
      const index = file.fileName.lastIndexOf('.');
      const ext = file.fileName.substr(index + 1);
      if (!this.isAssetTypeAnImage(ext)) {
        file.thumbUrl = 'assets/anyfix/file.svg';
      }
    });
  }

  /**
   * 初始化发货网点
   */
  initConsignBranch() {
    this.httpClient
      .get('/api/uas/sys-tenant/' + this.goodsPost.consignCorp)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        const consignCorpTenant = result.data;
        if (consignCorpTenant && consignCorpTenant.serviceProvider === 'Y') {
          this.consignBranchVisible = true;
          this.matchConsignBranch().then((branchList: any[]) => {
            let match = false;
            branchList.forEach(option => {
              if (option.branchId === this.goodsPost.consignBranch) {
                match = true;
              }
            });
            if (!match && this.goodsPost.consignBranch !== '0') {
              this.consignBranchList.unshift({
                branchId: this.goodsPost.consignBranch,
                branchName: this.goodsPost.consignBranchName
              });
            }
          });
        } else {
          this.consignBranchVisible = false;
        }
      });
  }

  /**
   * 初始化收货网点
   */
  initReceiveBranch() {
    this.httpClient
      .get('/api/uas/sys-tenant/' + this.goodsPost.receiveCorp)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        const consignCorpTenant = result.data;
        if (consignCorpTenant && consignCorpTenant.serviceProvider === 'Y') {
          this.receiveBranchVisible = true;
          this.matchReceiveBranch().then((branchList: any[]) => {
            let match = false;
            branchList.forEach(option => {
              if (option.branchId === this.goodsPost.receiveBranch) {
                match = true;
              }
            });
            if (!match && this.goodsPost.receiveBranch !== '0') {
              this.receiveBranchList.unshift({
                branchId: this.goodsPost.receiveBranch,
                branchName: this.goodsPost.receiveBranchName
              });
            }
          });
        } else {
          this.receiveBranchVisible = false;
        }
      });
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
      this.httpClient.post('/api/anyfix/goods-post/edit', this.validateForm.value)
        .pipe(
          finalize(() => {
            this.spinning = false;
          })
        )
        .subscribe((result: Result) => {
          if (result.code === 0) {
            this.nzMessageService.success('修改成功');
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
