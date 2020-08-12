import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {finalize} from 'rxjs/operators';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Result} from '@core/interceptor/result';
import {isNull} from '@util/helpers';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';

@Component({
  selector: 'app-cloud-redis',
  templateUrl: 'redis.component.html',
  styleUrls: ['redis.component.less']
})
export class RedisComponent implements OnInit {

  key: string;
  redisValue: any;
  spinning = false;

  constructor(private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private messageService: NzMessageService,
              private modalService: NzModalService) {
  }

  ngOnInit(): void {
  }

  /**
   * 查询某个redis
   */
  queryRedis() {
    if (isNull(this.key)) {
      this.messageService.error('请输入键！');
      return;
    }
    this.spinning = true;
    const payload = new HttpParams().set('key', this.key);
    this.httpClient
      .get('/api/uas/redis/get', {params: payload})
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        this.redisValue = result.data;
      });
  }

  /**
   * 删除某个redis
   */
  deleteRedis() {
    if (isNull(this.key)) {
      this.messageService.error('请输入键！');
      return;
    }
    this.modalService.confirm({
      nzTitle: '确定要删除redis吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.doDeleteRedis(this.key),
      nzCancelText: '取消'
    });
  }

  /**
   * 删除redis操作
   */
  doDeleteRedis(key) {
    this.spinning = true;
    const payload = new HttpParams().set('key', key);
    this.httpClient
      .delete('/api/uas/redis/delete', {params: payload})
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        this.queryRedis();
      });
  }

  /**
   * 初始化初始化角色权限redis
   */
  initRoleRightRedis() {
    this.spinning = true;
    const payload = new HttpParams().set('key', 'right-role-init-flag');
    this.httpClient
      .delete('/api/uas/redis/delete', {params: payload})
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        this.httpClient
          .post('/api/uas/sys-role-right/redis/init', {})
          .pipe(
            finalize(() => {
              this.spinning = false;
              this.cdf.markForCheck();
            })
          )
          .subscribe((result: Result) => {
            this.messageService.info('成功初始化角色权限！');
          });
      });
  }

  /**
   * 初始化系统公共权限redis
   */
  initSysCommonRedis() {
    this.spinning = true;
    const payload = new HttpParams().set('key', 'right-common-init-flag');
    this.httpClient
      .delete('/api/uas/redis/delete', {params: payload})
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        this.httpClient
          .post('/api/uas/sys-right/common-right/redis/init', {})
          .pipe(
            finalize(() => {
              this.spinning = false;
              this.cdf.markForCheck();
            })
          )
          .subscribe((result: Result) => {
            this.messageService.info('成功初始化公共权限！');
          });
      });
  }

  /**
   * 初始化初始化租户类型redis
   */
  initCorpTenantRedis() {
    this.spinning = true;
    const payload = new HttpParams().set('key', 'tenant-init-flag');
    this.httpClient
      .delete('/api/uas/redis/delete', {params: payload})
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        this.httpClient
          .post('/api/uas/sys-tenant/redis/init', {})
          .pipe(
            finalize(() => {
              this.spinning = false;
              this.cdf.markForCheck();
            })
          )
          .subscribe((result: Result) => {
            this.messageService.info('成功初始化租户类型！');
          });
      });
  }

  /**
   * 初始化初始化租户权限redis
   */
  initTenantRightRedis() {
    this.spinning = true;
    const payload = new HttpParams().set('key', 'tenant-right-init-flag');
    this.httpClient
      .delete('/api/uas/redis/delete', {params: payload})
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        this.httpClient
          .post('/api/uas/sys-tenant/right/redis/init', {})
          .pipe(
            finalize(() => {
              this.spinning = false;
              this.cdf.markForCheck();
            })
          )
          .subscribe((result: Result) => {
            this.messageService.info('成功初始化租户权限！');
          });
      });
  }

}
