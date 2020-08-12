/**
 * 后端接口格式
 */
export class Result {
  code: number;
  msg: string;
  data: ListWrapper | any;
}

/**
 * 分页返回数据格式
 */
export class ListWrapper {
  total: number; // 总数
  list: any; // 数据列表
}

/**
 * 分页请求参数
 */
export class Page {
  pageNum: any = 1; // 页码
  pageSize: any = 10; // 偏移列
  total = 0; // 总记录数
  pageSizes;
  constructor(pn?,ps?,pss?){
    this.pageNum = pn || 1;
    this.pageSize = ps || 10;
    this.pageSizes = pss || [10,20,50,100];
  }
  getNo(index: number) {
    return (this.pageNum - 1) * this.pageSize + 1 + index;
  }
}

/**
 * 多选框
 */
export class Checkbox {
  // 记录Id的集合
  dataIdList: any[] = [];
  // 是否全选
  isAllChecked = false;
  // 是否至少选中一条记录且不为全选
  isIndeterminate = false;
  // 记录是否选中
  mapOfCheckedId: {[key: string]: boolean} = {};

  // 刷新选中状态
  refreshStatus(): void {
    if (typeof this.dataIdList === 'object' && this.dataIdList !== null
      && this.dataIdList.length > 0) {
      this.isAllChecked = this.dataIdList.every(item => this.mapOfCheckedId[item]);
      this.isIndeterminate =
        this.dataIdList.some(item => this.mapOfCheckedId[item]) && !this.isAllChecked;
    } else {
      this.isAllChecked = false;
      this.isIndeterminate = false;
    }
  }

  // 全选或全不选
  checkAll(value: boolean): void {
    if (typeof this.dataIdList === 'object' && this.dataIdList !== null) {
      this.dataIdList.forEach(item => (this.mapOfCheckedId[item] = value));
      this.refreshStatus();
    }
  }

  // 获取当前选中的所有id
  getCheckedDataList() {
    const checkedList = [];
    if (typeof this.dataIdList === 'object' && this.dataIdList !== null) {
      this.dataIdList.forEach(item => {
        if (this.mapOfCheckedId[item]) {
          checkedList.push(item);
        }
      });
    }
    return checkedList;
  }
}


