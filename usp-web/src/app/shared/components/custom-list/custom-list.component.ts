import {ChangeDetectorRef, Input, Component, OnInit} from '@angular/core';
import {NzModalRef} from 'ng-zorro-antd';
@Component({
  selector: 'app-custom-list',
  templateUrl: 'custom-list.component.html',
  styleUrls: ['custom-list.less']
})
export class CustomListComponent implements OnInit {

  @Input() userId;
  @Input() titleList;
  @Input() type;
  spinning = false;
  detail: any = {};
  avatarUrl = '';
  previewVisible = false;
  avatarUrlBig = '';
  previewImage: any;
  isAllCheck: any;
  indeterminate: any;
  totalWidth = 0;
  oneList: any = [];
  twoList: any = [];
  threeList: any = [];
  fourList: any = [];
  nullOneList: any = [];
  nullTwoList: any = [];
  nullThreeList: any = [];
  nullFourList: any = [];

  submitList: any = [];

  constructor(
      private cdf: ChangeDetectorRef,
      private modal: NzModalRef
  ) {
  }

  ngOnInit(): void {
    this.titleList.forEach( item => {
      this.totalWidth += item.width;
    });
    // tslint:disable-next-line:prefer-for-of
    for(let i=0;i< this.titleList.length;i++)  {
        if(i<10){
          this.oneList.push(this.titleList[i]);
        }else if(i<20){
          this.twoList.push(this.titleList[i]);
        }else if(i<30){
          this.threeList.push(this.titleList[i]);
        }else {
          this.fourList.push(this.titleList[i]);
        }
    }
    for(let i= this.oneList.length;i< 10;i++)  {
      this.nullOneList.push({});
    }
    for(let i= this.twoList.length;i< 10;i++)  {
      this.nullTwoList.push({});
    }
    for(let i= this.threeList.length;i< 10;i++)  {
      this.nullThreeList.push({});
    }
    for(let i= this.fourList.length;i< 10;i++)  {
      this.nullFourList.push({});
    }
    this.refreshStatus();
  }

  dataFormat() {
    // const objList = [];
    // for(let item of this.titleList) {
    //   const obj = {
    //     item.checked = false;
    //   }
    // }
  }


  destroyModal() {
    this.modal.destroy();
  }

  submit() {
    if(this.type){
      const list = JSON.stringify(this.submitList);
      localStorage.setItem(this.type, list);
    }
    this.modal.destroy(this.submitList);
  }

  allChecked() {
    this.indeterminate = false;
    this.oneList.forEach( item => {
      item.checked = this.isAllCheck;
    });
    this.twoList.forEach( item => {
      item.checked = this.isAllCheck;
    });
    this.threeList.forEach( item => {
      item.checked = this.isAllCheck;
    });
    this.fourList.forEach( item => {
      item.checked = this.isAllCheck;
    });
  }

  refreshStatus() {
    this.submitList = [];
    this.oneList.forEach( item => {
      this.submitList.push(item);
    });
    this.twoList.forEach( item => {
      this.submitList.push(item);
    });
    this.threeList.forEach( item => {
      this.submitList.push(item);
    });
    this.fourList.forEach( item => {
      this.submitList.push(item);
    });
    if (this.submitList.every(item => !item.checked)) {
      this.isAllCheck = false;
      this.indeterminate = false;
    } else if (this.submitList.every(item => item.checked)) {
      this.isAllCheck = true;
      this.indeterminate = false;
    } else {
      this.isAllCheck = false;
      this.indeterminate = true;
    }
  }
}
