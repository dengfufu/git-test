import {Inject, Injectable} from '@angular/core';
import {startOfMonth, endOfMonth, format, subMonths, startOfYear, endOfYear} from 'date-fns';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '@env/environment';
import {UserService} from '@core/user/user.service';
import {DA_SERVICE_TOKEN, ITokenService} from '@delon/auth';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {Result} from '@core/interceptor/result';

export class CdaResult {
  metadata: any[];
  queryInfo: any[];
  resultset: any[];
}

export const DefaultRangeDate = [startOfYear(new Date()), endOfYear(new Date())];

export const RangeDates = {
  本月: [startOfMonth(new Date()), endOfMonth(new Date())],
  上月: [startOfMonth(subMonths(new Date(), 1)), endOfMonth(subMonths(new Date(), 1))],
  最近三个月: [startOfMonth(subMonths(new Date(), 2)), endOfMonth(new Date())],
  今年: [startOfYear(new Date()), endOfYear(new Date())]
};

@Injectable()
export class WorkplaceService {

  baseUrl = environment.dip_url + '/ba/plugin/cda/api/doQuery?_allow_anonymous=true';
  path = '/public/cda/usp_workplace.cda';

  constructor(private httpClient: HttpClient,
              private userService: UserService,
              @Inject(DA_SERVICE_TOKEN) private tokenService: ITokenService,) {
  }

  initUserInfoData(): Observable<any> {
    return this.httpClient.get('/api/uas/sys-role/list/user/' + this.userService.userInfo.userId)
      .pipe(
        map((result: Result) => {
          const roleList: any[] = result.data;
          const currentCorpRoleList = [];
          roleList.forEach((role: any) => {
            if (role.corpId === this.userService.currentCorp.corpId) {
              currentCorpRoleList.push(role.roleName);
            }
          });
          return currentCorpRoleList;
        })
      );
  }

  initCloudInfoData(): Observable<any> {
    const payload = new HttpParams()
      .set('path', this.path)
      .set('dataAccessId', 'information_platform')
      .set('token', this.tokenService.get().token)
      .set('tenantid', this.userService.currentCorp.corpId);
    return this.httpClient.get(this.baseUrl, {params: payload})
      .pipe(
        map((result: CdaResult) => {
          return result.resultset.length === 0 ? {} : {
            workAmount: result.resultset[0][0], // 平台总工单数
            providerAmount: result.resultset[0][1], // 服务商总数
            demanderAmount: result.resultset[0][2], // 委托商总数
            corpAmount: result.resultset[0][3], // 总企业数
            engineerAmount: result.resultset[0][4], // 总工程师数
            deviceAmount: result.resultset[0][5], // 总设备数
          };
        })
      );
  }

  initDeviceTypeData(rangeDate: Date[]): Observable<any> {
    const payload = new HttpParams()
      .set('path', this.path)
      .set('dataAccessId', 'large_equipment_chart')
      .set('token', this.tokenService.get().token)
      .set('tenantid', this.userService.currentCorp.corpId)
      .set('paramstartDate', format(rangeDate[0], 'YYYY-MM-DD'))
      .set('paramendDate', format(rangeDate[1], 'YYYY-MM-DD'));
    return this.httpClient.get(this.baseUrl, {params: payload})
      .pipe(
        map((result: CdaResult) => {
          return result.resultset.length === 0 ? undefined : result.resultset;
        })
      );
  }

  initWorkCityData(rangeDate: Date[]): Observable<any> {
    const payload = new HttpParams()
      .set('path', this.path)
      .set('dataAccessId', 'work_city_chart')
      .set('token', this.tokenService.get().token)
      .set('tenantid', this.userService.currentCorp.corpId)
      .set('paramstartDate', format(rangeDate[0], 'YYYY-MM-DD'))
      .set('paramendDate', format(rangeDate[1], 'YYYY-MM-DD'));
    return this.httpClient.get(this.baseUrl, {params: payload})
      .pipe(
        map((result: CdaResult) => {
          return result.resultset.length === 0 ? undefined : this.convertCityResultSet(result.resultset);
        })
      );
  }

  initWorkDemanderData(rangeDate: Date[],typeList: number[]): Observable<any> {
    const payload = new HttpParams()
      .set('path', this.path)
      .set('dataAccessId', 'work_entrust_chart')
      .set('token', this.tokenService.get().token)
      .set('tenantid', this.userService.currentCorp.corpId)
      .set('paramstartDate', format(rangeDate[0], 'YYYY-MM-DD'))
      .set('paramendDate', format(rangeDate[1], 'YYYY-MM-DD'))
      .set('paramtypeList', typeList.join(','));
    return this.httpClient.get(this.baseUrl, {params: payload})
      .pipe(
        map((result: CdaResult) => {
          return result.resultset.length === 0 ? undefined : this.convertBarResultSet(result.resultset);
        })
      );
  }

  initWorkNewData(rangeDate: Date[]) {
    const payload = new HttpParams()
      .set('path', this.path)
      .set('dataAccessId', 'work_number_chart')
      .set('token', this.tokenService.get().token)
      .set('tenantid', this.userService.currentCorp.corpId)
      .set('paramstartDate', format(rangeDate[0], 'YYYY-MM-DD'))
      .set('paramendDate', format(rangeDate[1], 'YYYY-MM-DD'));
    return this.httpClient.get(this.baseUrl, {params: payload})
      .pipe(
        map((result: CdaResult) => {
          return result.resultset.length === 0 ? {} : {
            newAmount: result.resultset[0][0], // 新增工单总量
            finishedAmount: result.resultset[0][1], // 已完成总量
            finishedPercent: result.resultset[0][2], // 完成比例
          };
        })
      );
  }

  initWorkProviderData(rangeDate: Date[]): Observable<any> {
    const payload = new HttpParams()
      .set('path', this.path)
      .set('dataAccessId', 'work_service_chart')
      .set('token', this.tokenService.get().token)
      .set('tenantid', this.userService.currentCorp.corpId)
      .set('paramstartDate', format(rangeDate[0], 'YYYY-MM-DD'))
      .set('paramendDate', format(rangeDate[1], 'YYYY-MM-DD'));
    return this.httpClient.get(this.baseUrl, {params: payload})
      .pipe(
        map((result: CdaResult) => {
          return result.resultset.length === 0 ? undefined : this.convertBarResultSet(result.resultset);
        })
      );
  }


  initWorkStatusData(rangeDate: Date[]): Observable<any> {
    const payload = new HttpParams()
      .set('path', this.path)
      .set('dataAccessId', 'work_status_chart')
      .set('token', this.tokenService.get().token)
      .set('tenantid', this.userService.currentCorp.corpId)
      .set('paramstartDate', format(rangeDate[0], 'YYYY-MM-DD'))
      .set('paramendDate', format(rangeDate[1], 'YYYY-MM-DD'));
    return this.httpClient.get(this.baseUrl, {params: payload})
      .pipe(
        map((result: CdaResult) => {
          return result.resultset.length === 0 ? undefined : result.resultset;
        })
      );
  }

  initWorkTodoData(): Observable<any> {
    const param = {
      pageNum: 1,
      pageSize: 10,
      queryScope: 3,
      workStatuses: '10,20,30,40,50,60,70'
    };
    return this.httpClient.post('/api/anyfix/work-request/user/query', param);
  }

  initWorkTypeData(rangeDate: Date[]): Observable<any> {
    const payload = new HttpParams()
      .set('path', this.path)
      .set('dataAccessId', 'work_type_chart')
      .set('token', this.tokenService.get().token)
      .set('tenantid', this.userService.currentCorp.corpId)
      .set('paramstartDate', format(rangeDate[0], 'YYYY-MM-DD'))
      .set('paramendDate', format(rangeDate[1], 'YYYY-MM-DD'));
    return this.httpClient.get(this.baseUrl, {params: payload})
      .pipe(
        map((result: CdaResult) => {
          return result.resultset.length === 0 ? undefined : result.resultset;
        })
      );
  }

  private convertBarResultSet(resultSet: any[]) {
    const corpList = [];
    const typeList = [];
    const resultData = [];
    resultSet.forEach((result: any[]) => {
      const corp: string = result[0];
      const type: string = result[1];
      const count: number = result[2];
      const index1 = corpList.indexOf(corp);
      const index2 = typeList.indexOf(type);
      if (index1 < 0) {
        corpList.push(corp);
      }
      if (index2 < 0) {
        typeList.push(type);
      }
    });

    // 第一行
    const firstRow = ['企业'];
    typeList.forEach((type: string) => {
      firstRow.push(type);
    });

    // 数据行
    const detailRow = [];
    corpList.forEach((corp) => {
      const row = [];
      row.push(corp);
      typeList.forEach((type) => {
        const tmp = resultSet.find((result) => {
          const rCorp = result[0];
          const rType = result[1];
          return rCorp === corp && rType === type;
        });
        if (tmp) {
          row.push(tmp[2]);
        } else {
          row.push(0);
        }
      });
      detailRow.push(row);
    });
    // 类型数量
    const tmpTypeCount = firstRow.length > 0 ? firstRow.length - 1 : 0;
    // 数据行排序
    detailRow.sort((temA: any[], temB: any[]) => {
      let tmpACount = 0;
      let tmpBCount = 0;
      for (let i = 1; i <= tmpTypeCount; i++) {
        tmpACount = tmpACount + temA[i];
        tmpBCount = tmpBCount + temB[i];
      }
      return tmpACount - tmpBCount;
    });
    // 总工单数
    let tmpTypeValueCount = 0;
    detailRow.forEach((tem) => {
      let tmpCount = 0;
      for (let i = 1; i <= tmpTypeCount; i++) {
        tmpCount = tmpCount + tem[i];
      }
      tmpTypeValueCount = tmpTypeValueCount + tmpCount;
    });
    resultData.push(firstRow);
    const tempDataset = resultData.concat(detailRow);
    const tempDataset4table = resultData.concat(detailRow.reverse());
    return {
      dataset: tempDataset, // 二维数组
      dataset4table: tempDataset4table,// 表格使用
      typeCount: tmpTypeCount,// 类型数量
      corpTotalCount: detailRow.length || 0, // 总企业数
      workTotalCount: tmpTypeValueCount // 总工单数
    };
  }

  private convertWorkTypeResultSet(resultSet: any[],option: any[]) {
    const corpList = [];
    const typeList = option;
    const resultData = [];
    resultSet.forEach((result: any[]) => {
      const corp: string = result[0];
      const type: string = result[1];
      const count: number = result[2];
      const index1 = corpList.indexOf(corp);
      const index2 = typeList.indexOf(type);
      if (index1 < 0) {
        corpList.push(corp);
      }
      if (index2 < 0) {
        typeList.push(type);
      }
    });

    // 第一行
    const firstRow = ['企业'];
    typeList.forEach((type: string) => {
      firstRow.push(type);
    });

    // 数据行
    let detailRow = [];
    corpList.forEach((corp) => {
      const row = [];
      row.push(corp);
      typeList.forEach((type) => {
        const tmp = resultSet.find((result) => {
          const rCorp = result[0];
          const rType = result[1];
          return rCorp === corp && rType === type;
        });
        if (tmp) {
          row.push(tmp[2]);
        } else {
          row.push(0);
        }
      });
      detailRow.push(row);
    });
    // 类型数量
    const tmpTypeCount = firstRow.length > 0 ? firstRow.length - 1 : 0;
    // 数据行排序
    detailRow.sort((temA: any[], temB: any[]) => {
      let tmpACount = 0;
      let tmpBCount = 0;
      for (let i = 1; i <= tmpTypeCount; i++) {
        tmpACount = tmpACount + temA[i];
        tmpBCount = tmpBCount + temB[i];
      }
      return tmpACount - tmpBCount;
    });
    // 截取50条数据行
    detailRow = detailRow.slice(-50);
    // 总工单数
    let tmpTypeValueCount = 0;
    detailRow.forEach((tem) => {
      let tmpCount = 0;
      for (let i = 1; i <= tmpTypeCount; i++) {
        tmpCount = tmpCount + tem[i];
      }
      tmpTypeValueCount = tmpTypeValueCount + tmpCount;
    });
    resultData.push(firstRow);
    const tempDataset = resultData.concat(detailRow);
    const tempDataset4table = resultData.concat(detailRow.reverse());
    return {
      dataset: tempDataset, // 二维数组
      dataset4table: tempDataset4table,// 表格使用
      typeCount: tmpTypeCount,// 类型数量
      corpTotalCount: detailRow.length || 0, // 总企业数
      workTotalCount: tmpTypeValueCount // 总工单数
    };
  }

  private convertCityResultSet(resultSet: any[]) {
    const cityDataList = [];
    const provinceList = [];
    const countList = [];
    resultSet.forEach((result: any[]) => {
      const province: string = result[1];
      const status: string = result[2];
      const count: number = result[3];
      const index = provinceList.indexOf(province);
      if (index >= 0) {
        const cityData = cityDataList[index];
        const allCount = cityData.value + count;
        countList[index] = allCount;
        cityData.value = allCount;
        cityData.status.push({name: status, value: count});
      } else {
        provinceList.push(province);
        countList.push(count);
        cityDataList.push({
          name: province,
          value: count,
          status: [{name: status, value: count}]
        });
      }
    });
    return {
      cityDate: cityDataList, // 工单按照省份
      totalCount: countList.reduce((prev, curr) => {
        return prev + curr;
      }), // 工单总数
      countRange: [Math.min(...countList), Math.max(...countList)] // 工单按照省份最大和最小值
    };
  }
}
