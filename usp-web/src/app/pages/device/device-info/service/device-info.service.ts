import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {UserService} from '@core/user/user.service';
import {ListService} from '@core/service/list.service';

@Injectable({
  providedIn: 'root'
})
export class DeviceInfoService extends ListService {

}


