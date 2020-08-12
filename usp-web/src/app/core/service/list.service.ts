import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {UserService} from '@core/user/user.service';

@Injectable({
  providedIn: 'root'
})
/**
 * 列表服务类
 */
export class ListService {
  paramsValue: any; // 筛选条件值
}


