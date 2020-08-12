import {Component, Input, OnInit} from '@angular/core';
import {NzModalRef} from 'ng-zorro-antd';

@Component({
  selector: 'app-delete-confirm',
  templateUrl: './delete-confirm.component.html',
  styleUrls: ['./delete-confirm.component.less']
})
export class DeleteConfirmComponent implements OnInit {

  @Input()
  title1 = "确定删除吗？";
  @Input()
  title2 = "清除后不可恢复"
  constructor(public modal: NzModalRef) { }

  ngOnInit() {
  }

}
