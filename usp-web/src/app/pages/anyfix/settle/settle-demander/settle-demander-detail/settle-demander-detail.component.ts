import {Component, OnInit} from '@angular/core';
import {ANYFIX_RIGHT, PAY_RIGHT} from '@core/right/right';
import {Page} from '@core/interceptor/result';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {NzDrawerService, NzMessageService, NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {finalize} from 'rxjs/operators';
import {SettleDemanderCheckComponent} from '../settle-demander-check/settle-demander-check.component';
import {SettleDemanderReceiptComponent} from '../settle-demander-receipt/settle-demander-receipt.component';
import {SettleDemanderInvoiceComponent} from '../settle-demander-invoice/settle-demander-invoice.component';
import {SettleDemanderPayComponent} from '../settle-demander-pay/settle-demander-pay.component';
import {environment} from '@env/environment';
import {SettleDemanderModComponent} from '../settle-demander-mod/settle-demander-mod.component';
import {WorkFeeVerifyDetailComponent} from '../../../work/work-fee-verify/detail/work-fee-verify-detail.component';
import {DemanderContNoComponent} from '../demander-cont-no/demander-cont-no.component';
import {ACLService} from '@delon/acl';
import {PayApplyService} from '../../../../wallet/components/pay/pay-apply/pay-apply.service';
import {SettleService} from '../settle.service';


@Component({
  selector: 'app-settle-demander-detail',
  templateUrl: './settle-demander-detail.component.html',
  styleUrls: ['./settle-demander-detail.component.less']
})
export class SettleDemanderDetailComponent implements OnInit {

  // 结算单编号
  settleId: any;
  // 权限
  aclRight = ANYFIX_RIGHT;
  payRight = PAY_RIGHT;

  // 分页
  page = new Page();
  // 结算单
  settleDemander: any | undefined = {};
  settleDemanderPay: any = {}; // 付款详情
  // 结算单明细
  detailList: any[] = [];
  // 页面加载中
  loading = false;
  // 结算明细列表加载中
  tableLoading = false;
  // 当前企业
  corpId = this.userService.currentCorp.corpId;

  // 图片附件
  payImgList: any = [];
  invoiceImgList: any = [];
  receiptImgList: any = [];
  // 非图片附件
  payNotImgList: any = [];
  invoiceNotImgList: any = [];
  receiptNotImgList: any = [];

  showFileUrl = environment.server_url + '/api/file/showFile?fileId=';
  downFileUrl = environment.server_url + '/api/file/downloadFile?fileId=';

  detailTitle = '';
  invoiceTableTitle: string; // 开票表格标题
  payTableTitle: string; // 付款表格标题
  receiptTableTitle: string; // 收款表格标题

  constructor(
    private activatedRoute: ActivatedRoute,
    private httpClient: HttpClient,
    private modalService: NzModalService,
    public userService: UserService,
    private messageService: NzMessageService,
    private router: Router,
    private payApplyService: PayApplyService,
    private drawerService: NzDrawerService,
    private aclService: ACLService,
    public settleService: SettleService
  ) {
    this.settleId = this.activatedRoute.snapshot.queryParams.settleId;
  }

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.initSettleDemander();
    this.initSettleDemanderPay();
  }

  /**
   * 初始化结算单详情
   */
  initSettleDemander() {
    this.loading = true;
    this.httpClient.get(`/api/anyfix/settle-demander/${this.settleId}`)
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      ).subscribe((res: any) => {
      this.settleDemander = res.data;
      // 明细标题
      this.detailTitle = this.settleDemander.settleWay === this.settleService.SETTLE_WORK ? '结算工单' : '结算对账单';
      this.querySettleDemanderDetail();
      this.invoiceTableTitle = '开票信息--[' + this.settleDemander.invoiceStatusName + ']';
      this.payTableTitle = '付款信息--[' + this.settleDemander.payStatusName + ']';
      this.receiptTableTitle = '收款信息--[' + this.settleDemander.receiptStatusName + ']';
    });
  }

  /**
   * 初始化付款详情
   */
  initSettleDemanderPay() {
    this.httpClient.get(`/api/anyfix/settle-demander-payment/detail/${this.settleId}`)
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      ).subscribe((res: any) => {
      this.settleDemanderPay = res.data;
      if (!this.settleDemanderPay) {
        this.settleDemanderPay = {};
      }
      const payFileList: any[] = this.settleDemanderPay.payFileList || [];
      const payFileArray = this.imgFiltrate(payFileList);
      this.payImgList = payFileArray[0];
      this.payNotImgList = payFileArray[1];
      const invoiceFileList: any[] = this.settleDemanderPay.invoiceFileList || [];
      const invoiceFileArray = this.imgFiltrate(invoiceFileList);
      this.invoiceImgList = invoiceFileArray[0];
      this.invoiceNotImgList = invoiceFileArray[1];
      const receiptFileList: any[] = this.settleDemanderPay.receiptFileList || [];
      const receiptFileArray = this.imgFiltrate(receiptFileList);
      this.receiptImgList = receiptFileArray[0];
      this.receiptNotImgList = receiptFileArray[1];
    });
  }

  // 查看委托协议
  showContNo() {
    const modal = this.modalService.create({
      nzTitle: '查看委托协议',
      nzContent: DemanderContNoComponent,
      nzWidth: 600,
      nzComponentParams: {
        contId: this.settleDemander.contId
        // serviceCorp: this.settleDemander.serviceCorp,
        // demanderCorp: this.settleDemander.demanderCorp
      },
      nzFooter: null
    });
  }

  // 返回
  goBack() {
    history.back();
  }

  // 查询结算单明细
  querySettleDemanderDetail(reset?: boolean) {
    if (reset) {
      this.page.pageNum = 1;
    }
    const params = {
      pageNum: this.page.pageNum,
      pageSize: this.page.pageSize,
      settleId: this.settleId
    };
    this.tableLoading = true;
    // 按周期结算
    let url = '/api/anyfix/settle-demander-detail/querySettleVerify';
    // 按单结算
    if (this.settleDemander.settleWay === this.settleService.SETTLE_WORK) {
      url = '/api/anyfix/settle-demander-detail/querySettleWork';
    }
    this.httpClient.post(url, params)
      .pipe(
        finalize(() => {
          this.tableLoading = false;
        })
      ).subscribe((res: any) => {
      this.detailList = res.data.list;
      this.page.total = res.data.total;
    });
  }

  // 查看对账单详情
  toVerifyDetail(verifyId) {
    this.router.navigate(['../../work-fee-verify/detail'],
      {queryParams: {verifyId}, relativeTo: this.activatedRoute});
  }

  // 查看工单详情
  toWorkDetail(workId) {
    this.router.navigate(['../../work-detail'],
      {queryParams: {workId}, relativeTo: this.activatedRoute});
  }

  // 修改结算单
  mod() {
    const modal = this.modalService.create({
      nzTitle: '修改结算单 ' + this.settleDemander.settleCode,
      nzContent: SettleDemanderModComponent,
      nzWidth: 1000,
      nzComponentParams: {
        settleId: this.settleDemander.settleId
      },
      nzFooter: null
    });
    modal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.loadData();
      }
    });
  }

  // 确认
  check() {
    const modal = this.modalService.create({
      nzTitle: '确认结算单',
      nzContent: SettleDemanderCheckComponent,
      nzWidth: 800,
      nzComponentParams: {
        settleDemander: this.settleDemander
      },
      nzFooter: null
    });
    modal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.loadData();
      }
    });
  }

  // 是否显示开票按钮
  showInvoice(): boolean {
    const rightIdList = this.aclService.data.abilities || [];
    if (rightIdList.includes(this.aclRight.SETTLE_DEMANDER_FEE_INVOICE)
      && this.settleDemander.serviceCorp === this.corpId
      && this.settleDemander.invoiceStatus !== 2) {
      return true;
    }
    return false;
  }

  // 开票
  invoice() {
    const modal = this.modalService.create({
      nzTitle: '结算单开票',
      nzContent: SettleDemanderInvoiceComponent,
      nzWidth: 800,
      nzComponentParams: {
        settleId: this.settleDemander.settleId
      },
      nzFooter: null
    });
    modal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.loadData();
      }
    });
  }

  // 是否显示付款按钮
  showPay(): boolean {
    const rightIdList = this.aclService.data.abilities || [];
    if (rightIdList.includes(this.aclRight.SETTLE_DEMANDER_FEE_PAY)
      && this.settleDemander.demanderCorp === this.corpId
      && this.settleDemander.payStatus !== 2
      && this.settleDemander.payStatus !== 3) {
      return true;
    }
    return false;
  }

  // 付款
  pay() {
    const modal = this.modalService.create({
      nzTitle: '结算单付款',
      nzContent: SettleDemanderPayComponent,
      nzWidth: 800,
      nzComponentParams: {
        settleId: this.settleDemander.settleId
      },
      nzFooter: null
    });
    modal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.loadData();
      }
    });
  }

  // 是否显示收款按钮
  showReceipt(): boolean {
    const rightIdList = this.aclService.data.abilities || [];
    if (rightIdList.includes(this.aclRight.SETTLE_DEMANDER_FEE_RECEIPT)
      && this.settleDemander.serviceCorp === this.corpId
      && this.settleDemander.payStatus !== 3) {
      return true;
    }
    return false;
  }

  // 收款
  receipt() {
    const modal = this.modalService.create({
      nzTitle: '结算单收款',
      nzContent: SettleDemanderReceiptComponent,
      nzWidth: 800,
      nzComponentParams: {
        settleId: this.settleDemander.settleId
      },
      nzFooter: null
    });
    modal.afterClose.subscribe((res: any) => {
      if (res === 'submit') {
        this.loadData();
      }
    });
  }

  // 弹出删除确认框
  deleteConfirm() {
    this.modalService.confirm({
      nzTitle: '确定删除该结算单吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.delete(this.settleId),
      nzCancelText: '取消'
    });
  }

  // 删除
  delete(settleId) {
    this.httpClient.delete(`/api/anyfix/settle-demander/${settleId}`)
      .subscribe((res: any) => {
        this.messageService.success('删除成功！');
        history.back();
      });
  }

  getImageUrls(imageList: any[]) {
    const temp: string[] = [];
    imageList.forEach((file: any) => {
      temp.push(this.showFileUrl + file.fileId);
    });
    return temp;
  }

  // 筛选附件种类
  imgFiltrate(fileList: any[]) {
    const imgList = [];
    const notImgList = [];
    fileList.forEach((file: any) => {
      // 获取后缀
      const index = file.fileName.lastIndexOf('.');
      const ext = file.fileName.substr(index + 1);
      if (this.isAssetTypeAnImage(ext)) {
        imgList.push(file);
      } else {
        notImgList.push(file);
      }
    });
    const list: any[] = [];
    list.push(imgList);
    list.push(notImgList);
    return list;
  }

  // 判断是否是图片
  isAssetTypeAnImage(ext) {
    return [
      'png', 'jpg', 'jpeg', 'bmp', 'gif', 'webp', 'psd', 'svg', 'tiff'].indexOf(ext.toLowerCase()) !== -1;
  }

  payOnline() {
    this.payApplyService.createDemanderPayApply({
      payerCorpId: this.settleDemander.demanderCorp,
      payeeCorpId: this.settleDemander.serviceCorp,
      orderId: this.settleDemander.settleId,
      orderName: `委托商结算-${this.settleDemander.settleCode}`,
      orderAmount: this.settleDemander.settleFee
    }).subscribe(() => {
      this.loadData();
    });
  }
}
