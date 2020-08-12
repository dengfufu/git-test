import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Result} from '@core/interceptor/result';
import {environment} from '@env/environment';
import {ACLService} from '@delon/acl';
import * as pinyin from 'pinyin';

// TODO: 增加详细类型
export class UserInfo {
  appId: string;
  cityName: string;
  current: string;
  districtName: string;
  email: string;
  faceImg: string;
  faceImgBig: string;
  isUserReal: boolean;
  logonId: string;
  mobile: string;
  nickname: string;
  orders: string;
  pages: string;
  password: string;
  provinceName: string;
  publicKey: string;
  records: string;
  regionCode: string;
  regionName: string;
  searchCount: string;
  sex: string;
  signature: string;
  size: string;
  smsCode: string;
  total: string;
  userId: string;
  userName: string;
  existPassword: boolean;
  // add
  faceImgUrl: string; // 头像url

  [key: string]: any;
}

export class Corp {
  corpId = '0'; // 企业编号
  corpName: string; // 企业名称
  shortName: string; // 企业简称
  serviceDemander: string; // 委托商
  serviceProvider: string; // 服务商
  deviceUser: string; // 设备使用商
  cloudManager: string; // 平台管理
  pinyin?: string; // 拼音缩写，前端使用
  [key: string]: any;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {

  userInfo: UserInfo = new UserInfo(); // 当前用户详细个人信息

  currentCorp: Corp = new Corp(); // 用户当前所选所属企业

  corpList: Corp[] = []; // 用户所属所有企业
  lastUserId: string;   // 记住上次选择的用户
  lastCorpId: string;   // 记住上次选择的企业
  pageSizeOptions = [5, 10, 15, 20, 50, 100, 200, 500];

  constructor(private httpClient: HttpClient,
              private aclService: ACLService) {
    this.lastUserId = localStorage.getItem('userId');
    this.lastCorpId = localStorage.getItem('corpId');
  }

  /**
   * 当前用户信息
   */
  initUserInfo(): Promise<any> {
    return this.httpClient.get('/api/uas/account/info').toPromise().then(
      (res: Result) => {
        if (res && res.code === 0) {
          this.userInfo = res.data;
          const userReal = this.userInfo.isUserReal;
          // 实名认证
          if (userReal) {
            this.userInfo.isUserReal = true;
          } else {
            this.userInfo.isUserReal = false;
          }
          // 性别
          const gender = this.userInfo.sex;
          if (gender === '1') {
            this.userInfo.sexLabel = '男';
          } else if (gender === '2') {
            this.userInfo.sexLabel = '女';
          } else {
            this.userInfo.sexLabel = '未知';
          }
          // 方便直接使用头像url
          if (this.userInfo && this.userInfo.faceImg && this.userInfo.faceImg !== '0') {
            this.userInfo.faceImgUrl = environment.server_url + '/api/file/showFile?fileId=' + this.userInfo.faceImg;
          } else {
            // 默认头像
            this.userInfo.faceImgUrl = gender === '2' ? 'assets/user/female-portrait.svg' : 'assets/user/male-portrait.svg';
          }
          if (this.lastUserId && this.lastUserId === this.userInfo.userId) {
          } else {
            if (this.lastCorpId) {
              localStorage.removeItem('corpId');
            }
            this.lastCorpId = null;
            // 新用户登录，service更新内容，同时更新缓存
            this.lastUserId = this.userInfo.userId;
            localStorage.setItem('userId', this.lastUserId);
          }
        }
      });
  }

  /**
   * 获取当前登录用户权限
   */
  initRightList() {
    if (this.currentCorp.corpId) {
      return this.httpClient.get('/api/uas/user-right/list').toPromise().then(
        (res: Result) => {
          if (res && res.code === 0) {
            if (res.data && res.data.length >= 0) {
              this.aclService.setAbility(res.data);
            }
          }
        }).catch(e => {
        this.aclService.setAbility([]);
      });
    }
    return null;
  }

  /**
   * 获取当前企业类型
   */
  initCurrentCorpType() {
    return this.httpClient.get('/api/uas/sys-tenant/' + this.currentCorp.corpId).toPromise().then(
      (res: Result) => {
        if (res && res.code === 0) {
          const data = res.data;
          if (data) {
            this.currentCorp.serviceDemander = data.serviceDemander;
            this.currentCorp.serviceProvider = data.serviceProvider;
            this.currentCorp.deviceUser = data.deviceUser;
            this.currentCorp.cloudManager = data.cloudManager;
          }
        }
      });
  }

  /**
   * 获取当前用户所属公司
   */
  initCustomCorps() {
    return this.httpClient.post('/api/uas/corp-user/corp/list', {}).toPromise().then(
      (res: Result) => {
        if (res && res.code === 0) {
          const corpList: any[] = res.data;
          if (corpList && corpList.length) {
            // 增加公司的拼音缩写信息
            corpList.map(corp => corp.pinyin = pinyin(corp.shortName, {style: pinyin.STYLE_FIRST_LETTER}).join(''));
            this.corpList = corpList;
            if (this.lastCorpId) {
              this.currentCorp = corpList.find(v => v.corpId === this.lastCorpId);
            }
            if (this.currentCorp.corpId === '0') { // new Corp() 默认corpId为'0'
              // 如果没有历史记录，则默认选中第一个
              this.changeCorp(res.data[0]);
            } else {
              this.initCurrentCorpType().then(() => {
              });
            }
          } else {
            // 不属于任何公司
          }
        }
        return this.initRightList();
      });
  }

  /**
   * 当前用户信息
   */
  addUserDefinedSetting(k: string, v: string) {
    const param = {
      settingKey: k,
      settingValue: v
    };
    this.httpClient.post('/api/uas/user-defined-setting/merge', param).subscribe(
      (res: Result) => {
        console.log(res);
      });
  }

  /**
   * 当前用户信息
   */
  loadUserDefinedSetting(): Promise<any> {
    return this.httpClient.get('/api/uas/user-defined-setting/list').toPromise().then(
      (res: Result) => {
        if (res && res.code === 0) {
          (res.data as any[]).forEach(i => {
            if (i && i.settingKey && i.settingValue) {
              localStorage.setItem(i.settingKey, i.settingValue);
            }
          });
        }
      });
  }

  changeCorp(corp: any) {
    this.currentCorp = corp;
    this.initCurrentCorpType().then(() => {
    });
    this.lastCorpId = this.currentCorp.corpId;
    localStorage.setItem('corpId', this.lastCorpId);
  }

  /**
   * 保存用户设备信息
   */
  saveUserDevice() {
    const payload = new HttpParams()
      .set('userId', this.userInfo.userId);
    this.httpClient.post('/api/uas/user-device/add', payload)
      .subscribe(() => {
      });
  };

  clear() {
    this.userInfo = new UserInfo();
    this.currentCorp = new Corp();
    this.corpList = [];
  }

  /**
   * 用于支付审核的用户
   * @returns {boolean}
   */
  isPayAuditUser() {
    return this.userInfo.userId === '1265148696960389121';
  }

  getPayAuditMenu(): {name, path}[] {
    return [
      {name: '服务报价', path: '/anyfix/pay/service-offer'},
      {name: '我的工单', path: '/anyfix/pay/work-order'},
      {name: '待支付工单', path: '/anyfix/pay/settle-demander'}
    ];
  }
}
