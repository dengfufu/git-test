import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Corp, UserService} from '@core/user/user.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {environment} from '@env/environment';
import {finalize} from 'rxjs/operators';
import {Result} from '@core/interceptor/result';
import {isNull} from '@util/helpers';
import {ActivatedRoute, Router} from '@angular/router';
import {ANYFIX_RIGHT} from '@core/right/right';
import {ACLService} from '@delon/acl';

@Component({
  selector: 'app-anyfix-goods-post-detail',
  templateUrl: 'post-detail.component.html',
  styleUrls: ['post-detail.component.less']
})
export class PostDetailComponent implements OnInit {

  anyfixRight = ANYFIX_RIGHT;

  currentCorp = new Corp(); // 当前企业
  spinning = false;

  ifCanEdit = false; // 是否可编辑
  ifCanDelete = false; // 是否可删除
  ifCanSign = false; // 是否可签收

  checkedAll = false;
  indeterminate = false;
  setOfCheckedId = new Set<string>();

  showFileUrl = environment.server_url + '/api/file/showFile?fileId=';
  downFileUrl = environment.server_url + '/api/file/downloadFile?fileId=';

  imgList: any[] = [];
  notImgList: any[] = [];

  workCodes: string; // 工单号
  receiveNote: string; // 收货备注
  postId: any; // 寄送单ID
  goodsPost: any = {};// 寄送单详情
  goodsList: any[] = []; // 物品列表

  constructor(private cdf: ChangeDetectorRef,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private httpClient: HttpClient,
              private userService: UserService,
              private nzMessageService: NzMessageService,
              private nzModalService: NzModalService,
              private aclService: ACLService) {
    this.postId = this.activatedRoute.snapshot.queryParams.postId;
  }

  ngOnInit(): void {
    this.checkedAll = false;
    this.indeterminate = false;
    this.setOfCheckedId = new Set<string>();
    this.initGoodsPostDetail();
    this.initOperateRight();
  }

  /**
   * 查询寄送单详情
   */
  initGoodsPostDetail() {
    this.spinning = true;
    this.httpClient
      .get('/api/anyfix/goods-post/detail/' + this.postId)
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        this.goodsPost = result.data;
        if (!this.goodsPost || isNull(this.goodsPost.id)) {
          this.nzMessageService.error('物品寄送单不存在！');
          return;
        }
        if (this.goodsPost) {
          this.listFile(this.goodsPost.fileList);
          this.goodsList = this.goodsPost.goodsPostDetailDtoList || [];
          this.goodsList.forEach((goods: any) => {
            if (goods.signed === 'Y') {
              goods.disabled = true;
            } else {
              goods.disabled = false;
            }
            this.listGoodsFile(goods);
          });
        }
      });
  }

  /**
   * 初始化操作权限
   */
  initOperateRight() {
    this.ifCanEdit = false;
    this.ifCanSign = false;
    this.ifCanDelete = false;
    const currentCorp = this.userService.currentCorp;
    this.httpClient
      .get('/api/anyfix/goods-post/ifCorpUser/' + this.postId)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        const goodsPostDto = result.data;
        const rightIdList = this.aclService.data.abilities;
        if (goodsPostDto) {
          // 未签收且是发货企业人员
          if ((goodsPostDto.signStatus === 0
            || goodsPostDto.signStatus === 1)
            && goodsPostDto.ifConsignCorpUser === 'Y'
            && currentCorp.corpId === goodsPostDto.consignCorp
            && rightIdList.includes(this.anyfixRight.GOODS_POST_DEL)) {
            this.ifCanDelete = true;
          }
          // 未全部签收
          if (goodsPostDto.signStatus !== 3
            && goodsPostDto.ifConsignCorpUser === 'Y'
            && currentCorp.corpId === goodsPostDto.consignCorp
            && rightIdList.includes(this.anyfixRight.GOODS_POST_EDIT)) {
            this.ifCanEdit = true;
          }
          // 未全部签收
          if (goodsPostDto.signStatus !== 3
            && goodsPostDto.ifReceiveCorpUser === 'Y'
            && currentCorp.corpId === goodsPostDto.receiveCorp
            && rightIdList.includes(this.anyfixRight.GOODS_POST_SIGN)) {
            this.ifCanSign = true;
          }
        }
      });
  }

  listGoodsFile(goods: any) {
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

  listFile(fileList: any[]) {
    if (fileList) {
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
      this.imgList = imgList;
      this.notImgList = notImgList;
    }
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

  /**
   * 删除物品寄送单
   */
  delGoodsPost(): void {
    this.nzModalService.confirm({
      nzTitle: '确定删除物品寄送单吗？',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => {
        this.doDelGoodsPost();
      },
      nzCancelText: '取消'
    });
  }

  /**
   * 删除物品寄送单
   */
  doDelGoodsPost() {
    this.spinning = true;
    this.httpClient
      .delete('/api/anyfix/goods-post/' + this.postId)
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        this.nzMessageService.info('删除成功！');
        this.goBack();
      });
  }

  /**
   * 编辑物品寄送单
   */
  editGoodsPost() {
    this.router.navigate(['../post-edit'],
      {
        queryParams: {postId: this.postId},
        relativeTo: this.activatedRoute
      });
  }

  updateCheckedSet(id, checked: boolean): void {
    if (checked) {
      this.setOfCheckedId.add(id);
    } else {
      this.setOfCheckedId.delete(id);
    }
  }

  refreshCheckedStatus(): void {
    this.checkedAll = this.goodsList.filter((goods: any) => !goods.disabled)
      .every((goods: any) => this.setOfCheckedId.has(goods.id));
    this.indeterminate = this.goodsList.some((goods: any) => this.setOfCheckedId.has(goods.id)) && !this.checkedAll;
  }

  onItemChecked(id, checked: boolean): void {
    this.updateCheckedSet(id, checked);
    this.refreshCheckedStatus();
  }

  onAllChecked(checked: boolean): void {
    this.goodsList.filter((goods: any) => !goods.disabled)
      .forEach((goods: any) => this.updateCheckedSet(goods.id, checked));
    this.refreshCheckedStatus();
  }

  /**
   * 签收
   */
  signGoods(): void {
    if (!isNull(this.receiveNote) && this.receiveNote.length > 200) {
      this.nzMessageService.error('收货备注：长度需小于等于200');
      return;
    }
    if (this.setOfCheckedId.size === 0) {
      this.nzMessageService.error('请勾选要签收的物品');
      return;
    }
    const params = {
      postId: this.postId,
      receiveNote: this.receiveNote,
      detailIdList: [...this.setOfCheckedId]
    };
    this.spinning = true;
    this.httpClient.post('/api/anyfix/goods-post/sign', params)
      .pipe(
        finalize(() => {
          this.spinning = false;
        })
      )
      .subscribe((result: Result) => {
        if (result.code === 0) {
          this.nzMessageService.success('签收成功');
          this.initGoodsPostDetail();
          this.initOperateRight();
        }
      });
  }

  toWorkDetail(workId) {
    this.router.navigate(['/anyfix/work-detail'],
      {queryParams: {workId}, relativeTo: this.activatedRoute});
  }

  goBack(): void {
    history.go(-1);
  }
}
