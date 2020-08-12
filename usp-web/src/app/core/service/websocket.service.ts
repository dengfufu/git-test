import {Injectable, OnDestroy} from '@angular/core';
import {UserService} from '@core/user/user.service';
import {NzNotificationService} from 'ng-zorro-antd';
import {environment} from '@env/environment';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService implements OnDestroy {

  // 有新消息
  hasNewNotice: boolean;
  closeWindow: boolean;
  connected: boolean;
  msConn = 0;

  websocket: WebSocket;

  constructor(private userService: UserService,
              private nzNotificationService: NzNotificationService) {
  }

  init() {
    console.log(`WebsocketService init`);
    this.connect();
  }

  connect(): void {
    if ('WebSocket' in window) {
      const userId = this.userService.userInfo.userId;
      if (userId) {
        this.websocket = new WebSocket(environment.websocket_url + `/websocket/${userId}`);
        // 连接发生错误的回调方法
        this.websocket.onerror = (event) => {
          console.log(event);
        };
        // 连接成功建立的回调方法
        this.websocket.onopen = (event) => {
          console.log(`Websocket Open.`);
          this.connected = true;
          this.msConn = 0;
          // console.log(event);
        };
        // 接收到消息的回调方法
        this.websocket.onmessage = (event: MessageEvent) => {
          console.log('Websocket Data:=' + event.data);

          // "tplName":"工单派工","appId":10001,"userIds":"1219133519697969154,1219140234216636440,1227480893271052290," +
          // "1227481848385376257,1227482032699871233,1227482362607046657,1227487017126858754,1227555839326294017,1227562274034552834," +
          // "1227570130205020161,1227575151613186050,1227814993827729409,1219140234250190892,1219140234250190912,1219140234162110477",
          //   "dataMap":{"msg":"您有一个工单[20200215000004]需要派单，请及时处理！","smallClassName":"自动取款机 ","workCode":"20200215000004",
          //   "customCorp":0,"customCorpName":"兴业银行深圳分行"},"type":2
          const data = JSON.parse(event.data);
          const title = '你有个新消息';
          const type = data.type || 0;
          let content  = '';
          if (type === 1) {
            content = data.content || '';
          } else {
            content = data.dataMap.msg || '';
          }
          this.nzNotificationService.info(title, content);
          this.hasNewNotice = true;
        };
        // 连接关闭的回调方法
        this.websocket.onclose = (event) => {
          console.log(`Websocket Close.`);
          this.connected = false;
          // console.log(event);
          if (!this.closeWindow) {
            if (this.msConn === 0) {
              this.msConn = 2000;
            } else {
              this.msConn = 30000;
            }
            setTimeout(()=>{
              if (!this.connected) {
                console.log('Websocket Reconnect...');
                this.connect();
              }
            }, this.msConn);
          }
        };
      } else {
        console.log('不存在userId');
      }
    } else {
      alert('Not support websocket');
    }
  }

  ngOnDestroy() {
    // 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    this.closeWindow = true;
    if (this.connected) {
      this.websocket.close();
    }
  }
}
