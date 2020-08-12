import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {environment} from '@env/environment';
import {UserService} from '@core/user/user.service';

/**
 * 企业信息
 */
@Component({
  selector: 'app-corp-info',
  templateUrl: 'corp-info.component.html',
  styleUrls: ['corp-info.component.less']
})
export class CorpInfoComponent implements OnInit {

  corpList: any[] = [];
  loading = true;

  constructor(private httpClient: HttpClient,
              public userService: UserService) {
  }

  ngOnInit() {
    this.httpClient.get('/api/uas/corp-registry/list/' + this.userService.userInfo.userId)
    .pipe(
      finalize(()=>{
        this.loading = false;
      })
    )
    .subscribe((res:any)=>{
      this.corpList = res.data;
      if(this.corpList != null && this.corpList.length > 0){
        for(const corp of this.corpList){
          corp.logoImgUrl = environment.server_url + '/api/file/showFile?fileId=' + corp.logoImg;
          let roleArr:any=[];
          for(const roleItem of corp.sysRoleList){
            roleArr.push(roleItem.roleName);
          }
          corp.roleArr = roleArr.join(',');
        }
      }
    })
  }
  edit(corp: any) {
    console.log(corp);
  }

}
