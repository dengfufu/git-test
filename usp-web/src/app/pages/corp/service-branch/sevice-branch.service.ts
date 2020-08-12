import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject} from 'rxjs';
import {Page} from '@core/interceptor/result';

@Injectable({
  providedIn: 'root'
})
export class ServiceBranchService {

  // 服务网点
  serviceBranch$: BehaviorSubject<any> = new BehaviorSubject({});
  // 网点人员列表
  branchUserList$: BehaviorSubject<any[]> = new BehaviorSubject([]);
  page$: BehaviorSubject<Page> = new BehaviorSubject(new Page());
  page = new Page();

  constructor(private httpClient: HttpClient) {
  }

  /**
   * 获得服务网点详情
   * @param branchId 网点编号
   */
  loadBranchDetail(branchId) {
    return this.findBranchDetail(branchId).then((res: any) => {
      const serviceBranch = res;
      this.serviceBranch$.next(serviceBranch);
      return 0;
    });
  }

  private findBranchDetail(branchId): Promise<any> {
    return new Promise((resolve, reject) => {
      this.httpClient.get('/api/anyfix/service-branch/' + branchId)
        .toPromise().then((data) => {
        resolve(data);
      }).catch(e => {
        console.log(e);
        throw e;
      });
    });
  }

  /**
   * 查询网点人员
   * @param params 查询参数
   * @param reset 是否重置
   */
  queryBranchUser(params: any, reset?: boolean) {
    return this.listBranchUser(params, reset).then((res: any) => {
      const branchUserList = res.data.list;
      this.page.total = res.data.total;
      this.branchUserList$.next(branchUserList);
      this.page$.next(this.page);
    });
  }

  listBranchUser(params: any, reset?: boolean): Promise<any> {
    if (!params.pageNum) {
      params.pageNum = this.page.pageNum;
      params.pageSize = this.page.pageSize;
    }
    if (reset) {
      this.page.pageNum = 1;
      params.pageNum = 1;
    }
    return new Promise((resolve, reject) => {
      this.httpClient
        .post('/api/anyfix/service-branch-user/query', params)
        .toPromise().then((res) => {
        resolve(res);
      });
    });
  }

}
