import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewEncapsulation} from '@angular/core';
import {CdkDragDrop, moveItemInArray} from '@angular/cdk/drag-drop';
import { deepCopy } from '@delon/util';
import {STProColumnLite} from '@shared/components/st-pro';

@Component({
  selector: 'col-selector',
  templateUrl: './col-selector.component.html',
  styleUrls: ['./col-selector.component.less'],
  encapsulation: ViewEncapsulation.None,
})
export class ColSelectorComponent implements OnInit {
  
  @Input()
  columns: STProColumnLite[];
  
  fixLeftColumns: STProColumnLite[];
  
  middleColumns: STProColumnLite[];
  
  fixRightColumns: STProColumnLite[];
  
  kw: string;
  
  open: boolean;
  
  // @Output() readonly onCancel = new EventEmitter<void>();
  
  @Output() readonly onOk = new EventEmitter<STProColumnLite[]>();
  
  @Output() readonly onReset = new EventEmitter<STProColumnLite[]>();
  
  constructor() {
  }
  
  ngOnInit() {
    this.parseFixLeftColumns();
    this.parseFixRightColumns();
    this.parseMiddleColumns();
  }

  parseFixLeftColumns() {
    const columns = this.columns.filter(e => e.__fixed === 'left');
    columns.forEach(e => e.__show = true);
    this.fixLeftColumns = columns;
  }
  
  parseFixRightColumns() {
    const columns = this.columns.filter(e => e.__fixed === 'right');
    columns.forEach(e => e.__show = true);
    this.fixRightColumns = columns;
  }
  
  parseMiddleColumns() {
    const originalColumns = deepCopy(this.columns);
    const columns = originalColumns.filter(e => e.__fixed !== 'left' && e.__fixed !== 'right');
    this.middleColumns = columns;
  }
  
  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.middleColumns, event.previousIndex, event.currentIndex);
  }
  
  select() {
  }
  
  search() {
    this.middleColumns.map(c => {
      c.isMatched = this.kw.trim() && c.__title.indexOf(this.kw.trim()) >= 0;
    });
    setTimeout(() => {
      if (this.kw) {
        const els = document.querySelectorAll('.sortable-item.search-matched');
        if (els.length > 0) {
          els[0].scrollIntoView();
        }
      } else {
        const els = document.querySelectorAll('.sortable-item');
        if (els.length > 0) {
          els[0].scrollIntoView();
        }
      }
    }, 10);
  }
  
  submit() {
    const cols = [].concat(this.fixLeftColumns, this.middleColumns, this.fixRightColumns);
    const sortedCols: STProColumnLite[] = cols.map((e, idx) => {
      return {
        index: e.index,
        __fixed: e.__fixed,
        __title: e.__title,
        __show: e.__show,
        __sortNum: idx
      };
    });
    this.columns = sortedCols;
    this.onOk.emit(sortedCols);
    this.open = false;
  }
  
  handle(visible:boolean) {
  
  }
  reset() {
    this.parseMiddleColumns();
  }
}
