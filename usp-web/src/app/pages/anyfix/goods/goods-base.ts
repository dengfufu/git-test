import {isNull} from '@util/helpers';
import {finalize} from 'rxjs/operators';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Corp, UserService} from '@core/user/user.service';
import {environment} from '@env/environment';
import {Result} from '@core/interceptor/result';
import {AddressParser} from '@util/address-parser';
import {NzMessageService, NzModalService, UploadFile} from 'ng-zorro-antd';
import {ChangeDetectorRef} from '@angular/core';
import {saveAs} from 'file-saver';
import {WorkSelectorComponent} from './work-selector/work-selector.component';
import {AreaService} from '@core/area/area.service';

export abstract class GoodsBase {

  validateForm: FormGroup;

  currentCorp = new Corp(); // 当前企业
  spinning = false;

  fileList: any[] = [];// 附件列表
  fileIdList: any[] = []; // 附件编号列表
  uploadAction = '/api/file/uploadFile';
  showFileUrl = environment.server_url + '/api/file/showFile?fileId=';
  downFileUrl = environment.server_url + '/api/file/downloadFile?fileId=';
  previewImage: any;
  previewVisible = false;

  consignCorp: string; // 发货企业
  consignCorpName: string; // 发货企业名称
  receiveCorp: string; // 收货企业

  receiveCorpList: any[] = []; // 收货企业列表
  consignBranchList: any[] = []; // 发货网点列表
  receiveBranchList: any[] = []; // 收货网点列表
  consignBranchVisible = false; // 发货网点是否可见
  receiveBranchVisible = false; // 收货网点是否可见
  consignTypeVisible = false; // 托运方式是否可见
  expressTypeVisible = false; // 快递类型是否可见
  expressCompanyVisible = false; // 快递公司是否可见
  expressNoVisible = false; // 快递单号是否可见

  consignUserList: any[] = []; // 发货人列表
  receiveUserList: any[] = [];// 收货人列表
  areaList: any[] = []; // 行政区划列表
  transportTypeList: any[] = [];// 运输方式列表
  consignTypeList: any[] = []; // 托运方式列表
  expressTypeList: any[] = []; // 快递类型列表
  expressCompanyList: any[] = []; // 快递公司列表

  postId: any; // 寄送单ID
  goodsPost: any = {}; // 物品寄送单
  goodsList: any[] = []; // 物品列表

  workCodes: string; // 选择的工单号
  workCheckedList: any[] = []; // 选择的工单列表

  nzFilterOption = () => true;

  constructor(public formBuilder: FormBuilder,
              public httpClient: HttpClient,
              public nzMessageService: NzMessageService,
              public modalService: NzModalService,
              public cdf: ChangeDetectorRef,
              public userService: UserService,
              public areaService: AreaService) {
  }

  /**
   * 初始化发货网点
   */
  initConsignBranch() {
    if (this.currentCorp.serviceProvider === 'Y') {
      this.consignBranchVisible = true;
      this.matchConsignBranch().then((branchList: any[]) => {
        this.listUserServiceBranch().then((userBranchList: any[]) => {
          if (userBranchList.length > 0) {
            const serviceBranch = userBranchList[0];
            let match = false;
            branchList.forEach(option => {
              if (option.branchId === serviceBranch.branchId) {
                match = true;
              }
            });
            if (!match) {
              this.consignBranchList.unshift(
                {branchId: serviceBranch.branchId, branchName: serviceBranch.branchName});
            }
            this.validateForm.controls.consignBranch.setValue(
              serviceBranch.branchId,
              {onlySelf: true, emitViewToModelChange: false});
            this.setConsignAreaAndAddress();
          }
        });
      });
    } else {
      this.setConsignAreaAndAddress();
      this.consignBranchVisible = false;
    }
  }

  /**
   * 获得当前用户的服务网点列表
   */
  listUserServiceBranch(): Promise<any> {
    return new Promise((resolve, reject) => {
      this.httpClient.get('/api/anyfix/service-branch/own-branch/list')
        .subscribe((res: Result) => {
          const list: any[] = res.data || [];
          resolve(list);
        }, e => {
          throw e;
        });
    });
  }

  /**
   * 设置发货行政区划和详细地址
   */
  setConsignAreaAndAddress() {
    const params = {
      consignCorp: this.consignCorp,
      consignBranch: this.validateForm.get('consignBranch').value,
      addressQueryType: 1
    };
    this.httpClient.post('/api/anyfix/goods-post/address', params)
      .subscribe((res: Result) => {
        const goodsPost = res.data;
        if (goodsPost) {
          this.validateForm.patchValue({
            consignAddress: goodsPost.consignAddress
          });
        }
        this.consignAddressParse();
      });
  }

  /**
   * 设置收货行政区划和详细地址
   */
  setReceiveAreaAndAddress() {
    const params = {
      consignCorp: this.consignCorp,
      consignBranch: this.validateForm.get('consignBranch').value,
      receiveCorp: this.receiveCorp,
      receiveBranch: this.validateForm.get('receiveBranch').value,
      addressQueryType: 2
    };
    this.httpClient.post('/api/anyfix/goods-post/address', params)
      .subscribe((res: Result) => {
        const goodsPost = res.data;
        if (goodsPost) {
          if (goodsPost.receiver === '0') {
            this.receiveUserList.unshift({
              userId: '0',
              userName: '[' + goodsPost.receiverName + ']'
            });
          }
          this.validateForm.controls.receiver.setValue(goodsPost.receiver,
            {onlySelf: true, emitViewToModelChange: false});
          this.validateForm.controls.receiverName.setValue(goodsPost.receiverName,
            {onlySelf: true, emitViewToModelChange: false});
          this.validateForm.controls.receiverPhone.setValue(goodsPost.receiverPhone,
            {onlySelf: true, emitViewToModelChange: false});
          this.validateForm.controls.receiveAddress.setValue(goodsPost.receiveAddress);
        }
        this.receiveAddressParse();
      });
  }

  /**
   * 模糊查询发货网点
   */
  matchConsignBranch(branchName?: string): Promise<any> {
    return new Promise((resolve, reject) => {
      this.listServiceBranch(this.consignCorp, branchName).then((branchList: any[]) => {
        this.consignBranchList = branchList;
        resolve(this.consignBranchList);
      });
    });
  }

  /**
   * 查询服务网点
   * @param corpId
   * @param branchName
   */
  private listServiceBranch(corpId: string, branchName?: string): Promise<any> {
    return new Promise((resolve, reject) => {
      const payload = {
        serviceCorp: corpId,
        branchName
      };
      this.httpClient.post('/api/anyfix/service-branch/match', payload)
        .subscribe((res: Result) => {
          const list = res.data;
          resolve(list);
        }, e => {
          throw e;
        });
    });
  }

  /**
   * 发货网点改变
   */
  consignBranchChange() {
    this.validateForm.controls.consignUser.setValue(null,
      {onlySelf: true, emitViewToModelChange: false});
    this.validateForm.controls.consignUserName.setValue(null,
      {onlySelf: true, emitViewToModelChange: false});
    this.validateForm.controls.consignUserPhone.setValue(null,
      {onlySelf: true, emitViewToModelChange: false});
    this.httpClient
      .get('/api/anyfix/service-branch/' + this.validateForm.get('consignBranch').value)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        const serviceBranch = result.data;
        if (serviceBranch) {
          this.validateForm.patchValue({
            consignAddress: serviceBranch.address
          });
          this.setConsignAreaAndAddress();
        }
      });
    this.matchConsignUser().then();
  }

  /**
   * 初始化发货人
   */
  initConsignUser() {
    this.matchConsignUser(this.userService.userInfo.userName).then((userList: any) => {
      if (userList.length > 0) {
        if (userList[0].userId === '0') {
          this.validateForm.controls.consignUser.setValue('0',
            {onlySelf: true, emitViewToModelChange: false});
          this.validateForm.controls.consignUserName.setValue('[' + this.userService.userInfo.userName + ']',
            {onlySelf: true, emitViewToModelChange: false});
          this.validateForm.controls.consignUserPhone.setValue(this.userService.userInfo.mobile,
            {onlySelf: true, emitViewToModelChange: false});
        } else {
          this.validateForm.controls.consignUser.setValue(this.userService.userInfo.userId,
            {onlySelf: true, emitViewToModelChange: false});
          this.validateForm.controls.consignUserName.setValue(this.userService.userInfo.userName,
            {onlySelf: true, emitViewToModelChange: false});
          this.validateForm.controls.consignUserPhone.setValue(this.userService.userInfo.mobile,
            {onlySelf: true, emitViewToModelChange: false});
        }
      }
    });
  }

  /**
   * 模糊查询发货人
   */
  matchConsignUser(userName?: string): Promise<any> {
    const consignBranch = this.validateForm.get('consignBranch').value;
    return new Promise<any>((resolve) => {
      if (!isNull(userName)) {
        this.listCorpUser(this.consignCorp, userName).then((userList: any[]) => {
          this.consignUserList = userList;
          resolve(this.consignUserList);
        });
      } else {
        if (isNull(consignBranch)) {
          this.listCorpUser(this.consignCorp, userName).then((userList: any[]) => {
            this.consignUserList = userList;
            resolve(this.consignUserList);
          });
        } else {
          this.listServiceBranchUser(this.consignCorp, consignBranch, userName).then((userList: any[]) => {
            this.consignUserList = userList;
            resolve(this.consignUserList);
          });
        }
      }
    });
  }

  /**
   * 服务网点人员列表
   */
  listServiceBranchUser(corpId: string, branchId: string, userName?: string): Promise<any> {
    if (isNull(branchId) || isNull(corpId)) {
      return new Promise<any>((resolve) => {
        resolve([]);
      });
    }
    return new Promise((resolve, reject) => {
      const params = {
        corpId,
        branchId,
        matchFilter: userName
      };
      this.httpClient
        .post('/api/anyfix/service-branch-user/match', params)
        .pipe(
          finalize(() => {
            this.cdf.markForCheck();
          })
        )
        .subscribe((result: Result) => {
          const list: any[] = result.data || [];
          if (!isNull(userName)) {
            let match = false;
            list.forEach(option => {
              if (option.userName === userName) {
                match = true;
              }
            });
            if (!match) {
              list.unshift({userId: '0', userName: '[' + userName + ']'});
            }
          }
          resolve(list);
        }, e => {
          throw e;
        });
    });
  }

  /**
   * 企业人员列表
   */
  listCorpUser(corpId: string, userName?: string): Promise<any> {
    if (isNull(corpId)) {
      return new Promise<any>((resolve) => {
        resolve([]);
      });
    }
    return new Promise((resolve, reject) => {
      const params = {
        corpId,
        matchFilter: userName
      };
      this.httpClient
        .post('/api/uas/corp-user/match', params)
        .pipe(
          finalize(() => {
            this.cdf.markForCheck();
          })
        )
        .subscribe((result: Result) => {
          const list: any[] = result.data || [];
          if (!isNull(userName)) {
            let match = false;
            list.forEach(option => {
              if (option.userName === userName) {
                match = true;
              }
            });
            if (!match) {
              list.unshift({userId: '0', userName: '[' + userName + ']'});
            }
          }
          resolve(list);
        }, e => {
          throw e;
        });
    });
  }

  /**
   * 发货人改变
   */
  consignUserChange() {
    const consignUser = this.validateForm.get('consignUser').value;
    this.validateForm.patchValue({
      consignUserPhone: null
    });
    if (this.consignUserList) {
      this.consignUserList.forEach((user: any) => {
        if (user.userId !== '0' && user.userId === consignUser) {
          this.validateForm.patchValue({
            consignUserName: user.userName,
            consignUserPhone: user.mobile
          });
        }
        if (user.userId === '0') {
          this.validateForm.patchValue({
            consignUserName: user.userName.substring(1, user.userName.length - 1)
          });
        }
      });
    }
  }

  /**
   * 行政区划列表
   */
  listArea(): Promise<any> {
    return new Promise((resolve, reject) => {
      this.httpClient
        .get('/api/uas/area/list')
        .pipe(
          finalize(() => {
            this.cdf.markForCheck();
          })
        )
        .subscribe((result: Result) => {
          this.areaList = result.data;
          resolve(this.areaList);
        });
    });
  }

  /**
   * 发货地址解析行政区划
   */
  consignAddressParse() {
    const rs = AddressParser.parseDistrict(this.validateForm.value.consignAddress, this.areaList);
    if (rs) {
      if (rs.districts) {
        this.validateForm.patchValue({
          consignAreaList: rs.districts
        });
      }
    }
  }

  /**
   * 收货地址解析行政区划
   */
  receiveAddressParse() {
    const rs = AddressParser.parseDistrict(this.validateForm.value.receiveAddress, this.areaList);
    if (rs) {
      if (rs.districts) {
        this.validateForm.patchValue({
          receiveAreaList: rs.districts
        });
      }
    }
  }

  /**
   * 运输方式列表
   */
  listTransportType() {
    this.httpClient
      .get('/api/anyfix/goods-post/transport-type/list')
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        this.transportTypeList = result.data;
      });
  }

  /**
   * 运输方式改变
   */
  transportTypeChange() {
    const transportType: number = this.validateForm.get('transportType').value || 0;
    if (transportType === 2) {
      // 托运
      this.consignTypeVisible = true;
      this.expressTypeVisible = false;
      this.expressCompanyVisible = false;
      this.expressNoVisible = false;
      this.listConsignType();
    } else if (transportType === 3) {
      // 快递
      this.consignTypeVisible = false;
      this.expressTypeVisible = true;
      this.expressCompanyVisible = true;
      this.expressNoVisible = true;
      this.listExpressType();
      this.matchExpressCompany();
    } else {
      this.consignTypeVisible = false;
      this.expressTypeVisible = false;
      this.expressCompanyVisible = false;
      this.expressNoVisible = false;
    }
  }

  /**
   * 托运方式列表
   */
  listConsignType() {
    this.httpClient
      .get('/api/anyfix/goods-post/consign-type/list')
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        this.consignTypeList = result.data;
      });
  }

  /**
   * 快递方式列表
   */
  listExpressType() {
    this.httpClient
      .get('/api/anyfix/goods-post/express-type/list')
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        this.expressTypeList = result.data;
      });
  }

  /**
   * 模糊查询快递公司
   */
  matchExpressCompany(corpName?: string) {
    const params = {
      corpId: this.userService.currentCorp.corpId,
      name: corpName
    };
    this.httpClient
      .post('/api/anyfix/express-company/match', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        this.expressCompanyList = result.data || [];
        if (!isNull(corpName)) {
          let match = false;
          this.expressCompanyList.forEach(option => {
            if (option.name === corpName) {
              match = true;
            }
          });
          if (!match) {
            this.expressCompanyList.unshift({id: '0', name: corpName + '[新增]'});
          }
        }
      });
  }

  /**
   * 收货企业列表
   */
  listReceiveCorp() {
    this.httpClient
      .get('/api/anyfix/demander-service/relates/' + this.consignCorp)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        this.receiveCorpList = result.data;
      });
  }

  /**
   * 收货企业改变
   */
  receiveCorpChange() {
    this.receiveCorp = this.validateForm.get('receiveCorp').value;
    if (isNull(this.receiveCorp)) {
      return;
    }
    this.validateForm.controls.receiver.setValue(null,
      {onlySelf: true, emitViewToModelChange: false});
    this.validateForm.controls.receiverName.setValue(null,
      {onlySelf: true, emitViewToModelChange: false});
    this.validateForm.controls.receiverPhone.setValue(null,
      {onlySelf: true, emitViewToModelChange: false});
    this.matchReceiveUser().then(() => {
      this.initReceiveBranch();
    });
  }

  /**
   * 初始化收货网点
   */
  initReceiveBranch() {
    this.httpClient
      .get('/api/uas/sys-tenant/' + this.receiveCorp)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        const receiveCorpTenant = result.data;
        if (receiveCorpTenant && receiveCorpTenant.serviceProvider === 'Y') {
          this.receiveBranchVisible = true;
          this.matchReceiveBranch().then(() => {
            this.listUserServiceBranch().then((userBranchList: any[]) => {
              if (userBranchList.length > 0) {
                userBranchList.forEach((serviceBranch: any) => {
                  this.receiveBranchList.unshift(
                    {branchId: serviceBranch.branchId, branchName: serviceBranch.branchName});
                });
                this.validateForm.controls.receiveBranch.setValue(
                  this.receiveBranchList[0].branchId,
                  {onlySelf: true, emitViewToModelChange: false});
                this.setReceiveAreaAndAddress();
              }
            });
          });
        } else {
          this.setReceiveAreaAndAddress();
          this.receiveBranchVisible = false;
        }
      });
  }

  /**
   * 模糊查询收货网点
   */
  matchReceiveBranch(branchName?: string): Promise<any> {
    return new Promise((resolve, reject) => {
      // 服务网点
      this.listServiceBranch(this.receiveCorp, branchName).then((branchList: any[]) => {
        this.receiveBranchList = branchList;
        resolve(this.receiveBranchList);
      });
    });
  }

  /**
   * 收货网点改变
   */
  receiveBranchChange() {
    this.validateForm.controls.receiver.setValue(null,
      {onlySelf: true, emitViewToModelChange: false});
    this.validateForm.controls.receiverName.setValue(null,
      {onlySelf: true, emitViewToModelChange: false});
    this.validateForm.controls.receiverPhone.setValue(null,
      {onlySelf: true, emitViewToModelChange: false});
    this.httpClient
      .get('/api/anyfix/service-branch/' + this.validateForm.get('receiveBranch').value)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        const serviceBranch = result.data;
        if (serviceBranch) {
          this.validateForm.patchValue({
            receiveAddress: serviceBranch.address
          });
          this.setReceiveAreaAndAddress();
        }
      });
    this.matchReceiveUser().then();
  }

  /**
   * 模糊查询收货人
   */
  matchReceiveUser(userName?: string): Promise<any> {
    const receiveBranch = this.validateForm.get('receiveBranch').value;
    return new Promise((resolve, reject) => {
      if (!isNull(userName)) {
        this.listCorpUser(this.receiveCorp, userName).then((userList: any[]) => {
          this.receiveUserList = userList;
          resolve(this.receiveUserList);
        });
      } else {
        if (isNull(receiveBranch)) {
          this.listCorpUser(this.receiveCorp, userName).then((userList: any[]) => {
            this.receiveUserList = userList;
            resolve(this.receiveUserList);
          });
        } else {
          this.listServiceBranchUser(this.receiveCorp, receiveBranch, userName).then((userList: any[]) => {
            this.receiveUserList = userList;
            resolve(this.receiveUserList);
          });
        }
      }
    });
  }

  /**
   * 收货人改变
   */
  receiveUserChange() {
    const receiver = this.validateForm.get('receiver').value;
    this.validateForm.patchValue({
      receiverPhone: null
    });
    if (this.receiveUserList) {
      this.receiveUserList.forEach((user: any) => {
        if (user.userId !== '0' && user.userId === receiver) {
          this.validateForm.patchValue({
            receiverName: user.userName,
            receiverPhone: user.mobile
          });
        }
        if (user.userId === '0') {
          this.validateForm.patchValue({
            receiverName: user.userName.substring(1, user.userName.length - 1)
          });
        }
      });
    }
  }

  /**
   * 选择工单
   */
  selectWork() {
    if (isNull(this.consignCorp) || this.consignCorp === '0') {
      this.nzMessageService.error('发货企业不能为空！');
      return;
    }
    if (isNull(this.receiveCorp) || this.receiveCorp === '0') {
      this.nzMessageService.error('请先选择收货企业！');
      return;
    }
    const modal = this.modalService.create({
      nzTitle: '选择工单',
      nzContent: WorkSelectorComponent,
      nzWidth: 1000,
      nzFooter: null,
      nzStyle: {'margin-top': '-50px'},
      nzBodyStyle: {'padding-top': '5px', 'padding-bottom': '5px'},
      nzComponentParams: {
        consignCorpId: this.consignCorp,
        receiveCorpId: this.receiveCorp,
        consignBranchId: this.validateForm.get('consignBranch').value,
        receiveBranchId: this.validateForm.get('receiveBranch').value,
        workCheckedList: [...this.workCheckedList]
      }
    });
    modal.afterClose.subscribe(result => {
      if (result && result.data) {
        this.workCheckedList = [...result.data];
        if (this.workCheckedList && this.workCheckedList.length > 0) {
          let isFirst = true;
          this.workCheckedList.forEach((work: any) => {
            if (isFirst) {
              this.workCodes = work.workCode;
              isFirst = false;
            } else {
              this.workCodes = this.workCodes + ',' + work.workCode;
            }
          });
        } else {
          this.workCodes = '';
        }
      }
    });
  }

  handlePreview = async (file: UploadFile) => {
    const index = file.fileName.lastIndexOf('.');
    const ext = file.fileName.substr(index + 1);
    if (this.isAssetTypeAnImage(ext)) {
      if (!file.url && !file.preview) {
        file.preview = await this.getBase64(file.originFileObj!);
      }
      this.previewImage = file.url || file.preview;
      this.previewVisible = true;
    } else {
      this.downloadConfirm(file);
    }
  };

  /**
   * 下载确认
   */
  downloadConfirm(file): void {
    this.modalService.confirm({
      nzTitle: '确定下载吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.downloadFile(file),
      nzCancelText: '取消'
    });
  }

  /**
   * 下载非图片文件
   * @param file
   */
  downloadFile(file: UploadFile) {
    let isFileSaverSupported = false;
    try {
      isFileSaverSupported = !!new Blob();
    } catch {
    }
    if (!isFileSaverSupported) {
      console.log('浏览器版本过低');
      return;
    }
    let fileId = 0;
    if (file && file.response && file.response.data) {
      fileId = file.response.data.fileId;
    }
    this.httpClient.request('get', this.showFileUrl + fileId, {
      params: {},
      responseType: 'blob',
      observe: 'response'
    }).subscribe(
      (res: HttpResponse<Blob>) => {
        if (res.status !== 200 || (res.body && res.body.size <= 0)) {
          return;
        }
        const disposition = this.getDisposition(res.headers.get('content-disposition'));
        let fileName = file.name;
        fileName =
          fileName || disposition[`filename*`] || disposition[`filename`] || res.headers.get('filename') || res.headers.get('x-filename');
        saveAs(res.body, decodeURI(fileName as string));
      },
      err => this.nzMessageService.error(err)
    );
  }

  private getDisposition(data: string | null) {
    const arr: Array<{}> = (data || '').split(';').filter(i => i.includes('=')).map(v => {
      const strArr = v.split('=');
      const utfId = `UTF-8''`;
      let value = strArr[1];
      if (value.startsWith(utfId)) {
        value = value.substr(utfId.length);
      }
      return {[strArr[0].trim()]: value};
    });
    // tslint:disable-next-line:variable-name
    return arr.reduce((_o, item) => item, {});
  }

  getBase64(file: File): Promise<string | ArrayBuffer | null> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result);
      reader.onerror = error => reject(error);
    });
  }

  listFile(goods: any) {
    const fileList = goods.fileList || [];
    const imgList: any[] = [];
    const notImgList: any[] = [];
    fileList.forEach(file => {
      // 获取后缀
      const index = file.fileName.lastIndexOf('.');
      const ext = file.fileName.substr(index + 1);
      if (this.isAssetTypeAnImage(ext)) {
        imgList.push(file);
      } else {
        notImgList.push(file);
      }
    });
    goods.imgList = imgList;
    goods.notImgList = notImgList;
  }

  getImageUrls(imageList: any[]) {
    imageList = imageList || [];
    const temp: string[] = [];
    imageList.forEach((file: any) => {
      temp.push(this.showFileUrl + file.fileId);
    });
    return temp;
  }

  // 判断是否是图片
  isAssetTypeAnImage(ext) {
    return [
      'png', 'jpg', 'jpeg', 'bmp', 'gif', 'webp', 'psd', 'svg', 'tiff'].indexOf(ext.toLowerCase()) !== -1;
  }

}
