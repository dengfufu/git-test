import {
  Input, Component, OnInit,
  ElementRef, ViewChild
} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {HttpClient, HttpParams} from '@angular/common/http';
import {format} from 'date-fns';
import {UserService} from '@core/user/user.service';
import {environment} from '@env/environment';
import {
  NzContextMenuService,
  NzDropdownMenuComponent,
  NzMessageService, NzModalService,
  UploadFile
} from 'ng-zorro-antd';
import {Page} from '@core/interceptor/result';
import {FileService} from '@core/service/file.service';
import {DatePipe} from '@angular/common';
import {ACLService} from '@delon/acl';
import {ANYFIX_RIGHT} from '@core/right/right';
import {MsgItem} from '@shared/components/chat-window/msg-item';
import {Observable, Observer} from 'rxjs';

@Component({
  selector: 'chat-window',
  templateUrl: 'chat-window.component.html',
  styleUrls: ['chat-window.component.less']
})
export class ChatWindowComponent implements OnInit {

  @Input() workId;            // 企业id
  content : any;

  list  = [];
  prefixFile = environment.server_url + '/api/file/showFile?fileId=';
  showQueryMore = false;
  page = new Page();
  myId =  this.userService.userInfo.userId;
  TEXT = '1';
  IMAGE = '2';
  FILE = '5';
  fileId = 0;
  thumbnail = 0;
  previewVisible= false;
  avatarUrlBig: any;
  sending = false;
  uploading = false;
  imgWidth = 500;
  imgTop = 0;
  showHint = false;
  firstOpen = true;
  imgCount = 0;
  queryType = 0;
  scrollTop = 0;
  removeIndex = 0;
  FIRST_QUERY = 0;
  QUERY_MORE = 1;
  QUERY_LATEST = 2;
  RECALL = 1;
  orderForLatest = 0; // 根据顺序顺序来查询最新的
  orderForMore = 0; // 查询更多的
  lastImageCount = 0;
  curItem : any;
  canRecall = false;
  isCurItemMine = false;
  imgFileUrlList = [] ;
  // 自定义字段多选值
  user = {
    userName: this.userService.userInfo.userName,
    faceImg: this.userService.userInfo.faceImgUrl
  };
  defaultImage='assets/chat/fail.png';
  DELETE = 3;
  @ViewChild('nzList', {read: ElementRef, static: false}) nzList: ElementRef;
  @ViewChild('text', {read: ElementRef, static: false}) text: ElementRef;
  aclRight = ANYFIX_RIGHT;
  constructor(private formBuilder: FormBuilder,
              private userService : UserService,
              private fileService :FileService,
              private msgService : NzMessageService,
              private datePipe : DatePipe,
              private el: ElementRef,
              private modalService :NzModalService,
              private nzContextMenuService: NzContextMenuService,
              private aclService: ACLService,
              private httpClient : HttpClient) {
    this.page.pageSize = 20;
  }



  contextMenu($event: MouseEvent, menu: NzDropdownMenuComponent, item: any, index) {
    this.curItem = item;
    this.isCurItemMine = this.curItem.userId === this.myId;
    this.removeIndex = index;
    if (item.userId === this.myId) {
      this.canRecall =  !this.isOverTime();
    }
    this.nzContextMenuService.create($event, menu);
  }

  isOverTime() {
    return  (new Date().getTime() - this.curItem.createTime) / 1000 >= 120;
  }

  closeMenu(){
    this.nzContextMenuService.close();
  }

  recallChat() {
    this.closeMenu();
    if(this.isOverTime()) {
      this.msgService.error('发送时间已超过2分钟，无法撤回！');
      return;
    }
    this.operateMsg(this.RECALL);
  }

  deleteChat() {
    this.closeMenu();
    if (this.curItem) {
      this.confirmDelete();
    }
  }

  confirmDelete() {
    this.modalService.confirm({
      nzTitle: '确定删除该条信息吗？',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.operateMsg(this.DELETE),
      nzCancelText: '取消'
    });
  }

  ngOnInit(): void {
    this.query(this.FIRST_QUERY);
  }

  nav(workId): void {
    this.workId = workId;
    this.query(this.FIRST_QUERY);
  }

  onShow(): void {
    if (this.firstOpen) {
      this.scrollToBottom();
      this.firstOpen = false;
    } else {
      try {
        setTimeout(()=> {
          this.nzList.nativeElement.scrollTop = this.scrollTop;
        }, 10);
      } catch(err) { }
    }
  }

  imgLoaded(item) {
    if (this.queryType === this.QUERY_MORE) {
      return;
    }
    item.isLastImage ? this.lastImageCount -- : this.imgCount --;
    if(this.lastImageCount === 0 || this.imgCount ===0 ) {
      this.scrollToBottom();
    }
  }

  onScroll() {
    this.scrollTop = this.nzList.nativeElement.scrollTop;
  }

  scrollToItem(msgOrder) {
    try {
      setTimeout(()=> {
        const y = this.el.nativeElement.querySelector('#id' + msgOrder).offsetTop - 60;
        this.nzList.nativeElement.scrollTop = y > 0 ? y : 0;
      }, 100);
    } catch(err) { }
  }

  // 自定义字段查询
  query(queryType: number) {
    const params = {
      workId : this.workId,
      pageSize : this.page.pageSize,
      pageNum : this.page.pageNum,
      orderForMore : 0,
      orderForLatest: 0,
      queryType: 0
    };
    this.queryType = queryType;
    if(queryType === this.FIRST_QUERY) {
      params.queryType = this.FIRST_QUERY;
      this.imgFileUrlList = [];
      this.list = [];
      this.imgCount = 0;
      this.orderForLatest = 0;
      this.orderForMore = 0;
      this.firstOpen = true;
    }
    if(queryType === this.QUERY_MORE){
      params.queryType = this.QUERY_MORE;
      params.orderForMore = this.orderForMore
    }
    if(queryType === this.QUERY_LATEST) {
      params.queryType = this.QUERY_LATEST;
      params.orderForLatest = this.orderForLatest;
    }
    this.httpClient.post('/api/anyfix/work-chat/query',params)
      .subscribe((res: any) => {
        const length = res.data.length;
        if(queryType !== this.QUERY_LATEST) {
          length === this.page.pageSize ? this.showQueryMore = true: this.showQueryMore =false;
        }
        if(length > 0) {
          for(const item of res.data) {
            if (item.img && item.thumbnail === '0') {
              item.thumbnail = item.fileId;
            }
          }
          if(queryType === this.QUERY_MORE) {
            for(const item of res.data) {
              if(item.img){
                this.imgFileUrlList.unshift(this.prefixFile + item.fileId);
              }
              this.list.unshift(item);
            }
            this.scrollToItem(this.orderForMore);
            this.orderForMore = res.data[length-1].msgOrder;
          }
          else {
            const tempList = [];
            if(queryType === this.FIRST_QUERY) {
              for(let i= length-1;i >=0; i--){
                const item = res.data[i];
                tempList.push(item);
                if (item.img) {
                  this.imgFileUrlList.push(this.prefixFile + item.fileId);
                  item.isLastImage = false;
                  this.imgCount++;
                }
              }
              this.list = tempList;
              // 设置顺序号
              this.orderForMore =  tempList[0].msgOrder;
            } else {
              for(let i= length-1;i >=0; i--){
                const item = res.data[i];
                if(item.img) {
                  this.imgFileUrlList.push(this.prefixFile + item.fileId);
                  item.isLastImage = true;
                  this.lastImageCount ++ ;
                }
                this.list.push(item);
              }
            }
            this.orderForLatest = this.list[this.list.length-1].msgOrder + 1;
            this.scrollToBottom();
          }
        }
        this.text.nativeElement.focus();
      });
  }

  updateSingleChecked(event) {
    /*console.log(event);*/
  }
  beforeUpload = (file: UploadFile) => {
    return new Observable((observer: Observer<boolean>) => {
      const isLt20M = file.size / 1024 / 1024 < 20;
      if (!isLt20M) {
        this.msgService.error('文件大小不能超过20MB!');
        observer.complete();
        return;
      }
      this.uploading = true;
      const isImg = this.isImage(file);
      if(isImg) {
        this.fileService.uploadImg(file, 500, 200).subscribe((res: any) => {
          this.fileId = res[0].fileId;
          if (res.length && res.length > 1) {
            this.thumbnail = res[1].fileId;
          } else {
            this.thumbnail = 0;
          }
          this.submit(this.IMAGE);
        }, (error: any)=>{
          this.uploading = false;
        }, () => {
          this.uploading = false;
        });
      } else {
        this.fileService.upload(file).subscribe((fileId: any) => {
          this.fileId = fileId;
          this.submit(this.FILE);
        }, (error: any)=>{}, () => {
          this.uploading = false;
        });
      }
      observer.complete();
      return;

    });
  };



  isImage(file: UploadFile){
    const fileName= file.name;
    const suffixIndex = fileName.lastIndexOf('.');
    if(suffixIndex === -1){
      return false;
    }
    const suffix=fileName.substring(suffixIndex+1).toUpperCase();
    return suffix === 'BMP' || suffix === 'JPG' || suffix === 'JPEG' || suffix === 'JPE' || suffix === 'PNG'
      || suffix === 'GIF';
  }
  validateParams() {
    if( this.content === '' || this.content.length > 500) {
      this.msgService.error('超出所允许的发送字数');
      return false;
    }
    return true;
  }
  submit(msgType){
    if (this.sending) {
      return;
    }
    if(msgType === this.TEXT){
      if(!this.validateParams()) {
        return;
      }
    }
    this.sending = true;
    const params = this.getSubmitParams(msgType);
    this.httpClient.post('/api/anyfix/work-chat/send',
      params)
      .subscribe((res: any) => {
        this.content = '';
        this.page.pageNum = 1;
        this.query(this.QUERY_LATEST);
      }, (error: any)=>{}, () => {
        this.sending = false;
      });
  }


  scrollToBottom(){
    try {
      setTimeout(()=> {
        this.nzList.nativeElement.scrollTop = this.nzList.nativeElement.scrollHeight;
      }, 10);
      } catch(err) { }
  }

  keyEnter(event: KeyboardEvent) {
    // 禁止回车的默认换行
    event.preventDefault();
    this.submit(this.TEXT);
  }

  keyCtrlEnter(event: KeyboardEvent) {
    const textarea = this.text.nativeElement;
    textarea.setRangeText('\n');
    // 在未选中文本的情况下，重新设置光标位置
    textarea.selectionStart += '\n'.length;
  }

  getParams(fileId) {
    return new HttpParams().set('fileId', fileId);
  }

  fileIcon(extension: any) {
    if (extension === 'docx' || extension === 'doc') {
      return 'assets/chat/word.svg';
    } else if (extension === 'xlsx' || extension === 'xls') {
      return 'assets/chat/excel.svg';
    } else if (extension === 'pptx' || extension === 'ppt') {
      return 'assets/chat/ppt.svg';
    } else if (extension === 'zip' || extension === 'rar') {
      return 'assets/chat/compressed.svg';
    } else {
      return 'assets/chat/unknown.svg';
    }
  }

  getSize(size) {
    const sizeWithMB = parseInt(size/1024/1024 +'' ,10);
    if(sizeWithMB >= 1) {
      return sizeWithMB + 'MB';
    } else {
     return parseInt(sizeWithMB* 1024 + '',10) + 'KB';
    }
  }

  parseTime(createTime: any) {
    const date = new Date(createTime);
    const tempDate = new Date(format(createTime, 'YYYY-MM-DD'));
    const curDay = new Date(format(new Date(), 'YYYY-MM-DD'));
    const delta = (curDay.getTime() - tempDate.getTime()) / 1000/60/ 60/24;
    const day = parseInt(delta + '', 10);
    if( day === 0 || day === 1) {
      let str = '';
      if(day === 1) {
        str += '昨天'
      }
      if(date.getHours() < 12 ) {
        str += '早上'
      } else {
        str += '下午';
      }
      return str + ' ' + this.datePipe.transform(date,'HH:mm');
    }
    return this.datePipe.transform(date,'yyyy-MM-dd HH:mm')
  }

  toggleHint() {
    this.showHint = !this.showHint;
  }

  getSubmitParams(msgType) {
    const msgItem = new MsgItem();
    msgItem.workId = this.workId;
    msgItem.userId = this.userService.userInfo.userId;
    msgItem.msgType = msgType;
    if(msgType === this.FILE || msgType === this.IMAGE) {
      msgItem.fileId = this.fileId;
    }
    if(msgType === this.IMAGE) {
      msgItem.thumbnail = this.thumbnail;
    }
    if(msgType === this.TEXT) {
      msgItem.content = this.content;
    }
    return msgItem;
  }

  operateMsg(operateType) {
    let operateTypeUrl = '';
    if(operateType === this.RECALL) {
      operateTypeUrl = 'recall';
    } else if(operateType === this.DELETE) {
      operateTypeUrl = 'delete';
    }
    this.httpClient.post('/api/anyfix/work-chat/'+ operateTypeUrl,{id: this.curItem.id})
    .subscribe((res: any) => {
      this.list.splice(this.removeIndex,1);
    }, (error: any)=>{});
  }

  getImageUrls() {
    return this.imgFileUrlList;
  }

  getImageIndex(fileId) {

    return this.imgFileUrlList.indexOf(fileId) + 1;
  }

  getDefaultImg(event: any) {
    event.target.src = this.defaultImage;
  }
}
