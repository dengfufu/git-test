import {Input, Component, OnInit, ChangeDetectorRef} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {ANYFIX_RIGHT} from '@core/right/right';
import {ACLService} from '@delon/acl';
import {finalize} from 'rxjs/operators';
import {UserService} from '@core/user/user.service';


@Component({
  selector: 'work-review-list',
  templateUrl: 'work-review-list.component.html',
  styleUrls: ['work-review-list.component.less']
})
export class WorkReviewListComponent implements OnInit {
  @Input() workId;

  workReviewList : any[] = [];
  aclRight = ANYFIX_RIGHT;
  aclRightIdList: any[] = [];
  initLoading = true;

  currentUserId = this.userService.userInfo.userId;
  IS_SOLVEMAP = {
    1: '已解决',
    2: '未解决',
    3: '客户未联系上'
  };

  constructor(private msgService: NzMessageService,
              private modalService: NzModalService,
              private changeDetectorRef: ChangeDetectorRef,
              private httpClient: HttpClient,
              private aclService: ACLService,
              private userService: UserService) {

  }


  ngOnInit(): void {
    this.aclRightIdList = this.aclService.data.abilities || [];
    this.queryWorkReview(this.workId);
  }

  // 客户回访记录
  queryWorkReview(workId) {
    this.httpClient.post('/api/anyfix/work-review/list', {workId})
      .subscribe((res: any) => {
        this.initLoading = false;
        this.workReviewList = res.data;
      });
  }

  hasWorkReviewDelRight():boolean {
    return this.aclRightIdList.includes(this.aclRight.WORK_REVIEW_DELETE);
  }

  del(item) {
    this.modalService.confirm({
      nzTitle: '删除',
      nzContent: '确定删除本条回访记录?',
      nzOkText: '确定',
      nzCancelText: '取消',
      nzOnOk: () => {
        this.httpClient.post('/api/anyfix/work-review/del', {id: item.id ,workId: item.workId})
          .pipe(
            finalize(() => {
              this.changeDetectorRef.markForCheck();
            })
          )
          .subscribe(() => {
            this.initLoading = true;
            this.queryWorkReview(this.workId);
          });
      },
      nzOnCancel: () => {
      }
    });

  }
}
