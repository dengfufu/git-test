import {
  AfterContentInit, AfterViewChecked,
  AfterViewInit, ChangeDetectionStrategy, ChangeDetectorRef,
  Component,
  ElementRef, EventEmitter, HostBinding, Inject, Input,
  OnChanges,
  OnDestroy,
  OnInit, Optional, Output, TemplateRef,
  ViewChild, ViewChildren,
  ViewEncapsulation
} from '@angular/core';
import {from, of, Subject} from 'rxjs';
import {debounceTime, delay, finalize, takeUntil} from 'rxjs/operators';
import {NzDomEventService, NzPaginationComponent, NzTheadComponent} from 'ng-zorro-antd';
import {
  Page,
  STProChange,
  STProColumn,
  STProData,
  STProPage,
  STProReq,
  STProRes,
  STProColumnLite,
  STProColumnSetting,
  STProOverflowMode, STProWidthMode, STProExportOptions, ST_PRO_CONFIG
} from './st.pro.interfaces';
import {deepCopy, deepGet, deepMergeKey} from '@delon/util';
import {STComponent, STLoadOptions, STWidthMode, XlsxService} from '@delon/abc';
import {UserService} from '@core/user/user.service';
import {Error} from 'tslint/lib/error';
import {Router} from '@angular/router';
import {STData, STExportOptions} from '@delon/abc/table/table.interfaces';
import {HttpClient} from '@angular/common/http';
import {STProExport} from '@shared/components/st-pro/st-pro-export';
import {STProConfig} from '@shared/components/st-pro/st.pro.interfaces';

@Component({
  selector: 'st-pro',
  templateUrl: './st-pro.component.html',
  styleUrls: ['./st-pro.component.less'],
  changeDetection: ChangeDetectionStrategy.Default,
  encapsulation: ViewEncapsulation.None,
  providers: [NzDomEventService, STProExport]
})
export class StProComponent implements OnInit, AfterViewInit, OnDestroy, AfterViewChecked {

  @Input() quickFilter: string | TemplateRef<void>;
  @Input() defaultButtons: string | TemplateRef<void>;
  @Input() batchButtons: string | TemplateRef<void>;
  @Input() data: STProData;
  pageNum = 1;
  pageSize;
  total = 0;
  private defaultPage: STProPage = {
    pageSizes:[20,50,100,200,500],
    showSize: true,
    total: true,
    front: false
  };
  private _page: STProPage = deepCopy(this.defaultPage);
  @Input()
  get page() {
    return this._page;
  }
  set page(value: STProPage) {
    const item = deepMergeKey({}, true, this.defaultPage, value);
    this._page = item;
    this.pageSize = this._page.pageSizes[0];
  }

  @Input()
  sortInd = { ascend: 'asc', descend: 'desc' };
  @Input() scrollX:string;
  @Input() scrollY;


  widthMode:STProWidthMode = {
    type:'strict',
    strictBehavior: 'truncate'
  };

  private _overflow:STProOverflowMode;
  @Input()
  get overflow() {
    return this._overflow;
  }
  set overflow(value : STProOverflowMode) {
    this._overflow = value;
    this.widthMode.strictBehavior = this._overflow;
  }
  /**
   * 是否优化显示空值 使用 "--" 替换
   */
  @Input()
  prettyNull = false;

  /**
   * 选择列信息的本地存储key: [用户ID]@[企业ID]@[业务ID/路由url]
   */
  storageKey:string;

  /**
   * 表格 key，用于前端存储列设置
   */
  @Input()
  stProKey:string;
  
  /**
   * 默认不启用虚拟滚动
   * @type {boolean}
   */
  @Input()
  virtualScroll = false;
  virtualItemSize = 36;

  private orgColumns :STProColumn[] = [];
  settingColumns :STProColumnLite[] = [];
  private _columns :STProColumn[];

  @Input()
  get columns() :STProColumn[]{
    return this._columns;
  }
  set columns(value: STProColumn[]) {
    this.orgColumns = value;
    if(value) {
      const e = value.find(v => v.type === 'no');
      if(e && !e.noIndex) {
        e.noIndex = (item: STProData, col: STProColumn, idx: number) => {
          return ((this.pageNum - 1) * this.pageSize) + idx +1;
        }
      }
    }
    value.forEach((e,idx)=>{
      this.initSort(e);
      if(this.prettyNull) {
        this.initFormat(e)
      }
      if(e.type === 'checkbox') {
        e.__title = '复选框';
      } else if(e.type === 'radio') {
        e.__title = '单选框';
      } else {
        // 强制转为 string
        e.__title = (e.title + '');
      }
      e.__show = e.__show === undefined ? true : !!e.__show;
      e.__sortNum = idx;
      e.__fixed = e.fixed;
    });

    const setting:STProColumnSetting = this.loadColumnSettingLocal();
    if(setting){
      value.forEach((e:STProColumn, idx:number)=>{
        if(!e.index) {
          throw new Error(`Column {type:${e.type}, title:${e.title}} missing [index]`);
        }
        const c:STProColumnLite = setting[e.index];
        if(c) {
          // 从本地存储获取列设置信息
          e.__show = c.__show === undefined? true : !!c.__show;
          e.__sortNum = c.__sortNum !== undefined ? c.__sortNum : idx;
        }
      });
    }
    // 排序
    value.sort((a,b)=>a.__sortNum>b.__sortNum?1:-1);

    // 优化列设置信息
    value.forEach((e,idx)=>{
      e.__show = e.__show === undefined ? true: e.__show;
      // 重新设置顺序号，列定义变更，可能导致顺序号不连续
      e.__sortNum = idx;
      e.iif = ()=> e.__show
    });

    this._columns = value;
    this.settingColumns = value.map(e => ({
      index: e.index,
      __fixed: e.__fixed,
      __title: e.__title ,
      __show: e.__show,
      __sortNum: e.__sortNum
    }));
    
    // 如果出现列定义变更，可能需要考虑将合并后的列配置保存起来
  }

  @ViewChild('st', { static: false }) st: STComponent;

  @ViewChildren(NzPaginationComponent) pager: NzPaginationComponent;
  @ViewChild(NzTheadComponent, { static: false }) header: NzTheadComponent;

  @Output() readonly columnChange = new EventEmitter<STProColumnLite[]>();
  @Output() readonly pageChange = new EventEmitter<{pageNum,pageSize}>();
  @Output() readonly dblClick = new EventEmitter<{index:number,item:STProData,e:Event}>();
  @Output() readonly selectRow = new EventEmitter<STProData[]>();

  isBatchOperate = false;
  batchRows = 0;
  showColumnSelector = true;
  private destroy$ = new Subject<void>();

  private defaultReq:STProReq = {
    reName:{
      pi: 'pageNum',
      ps: 'pageSize'
    },
    method: 'POST',
    allInBody: true
  };
  private _req:STProReq = deepCopy(this.defaultReq);
  @Input()
  get req() {
    return this._req;
  }
  set req(value: STProReq) {
    const item = deepMergeKey({}, true, this.defaultReq, value);
    this._req = item;
  }

  private defaultRes:STProRes = {
    reName: {
      total: 'data.total',
      list: 'data.list'
    },
  };
  private _res:STProRes = deepCopy(this.defaultRes);
  @Input()
  get res() {
    return this._res;
  }
  set res(value: STProRes) {
    const item = deepMergeKey({}, true, this.defaultRes, value);
    this._res = item;
  }

  el: HTMLElement;



  // selectMode: 'single' | 'multiple' = 'single';

  lastClickedRow: STProData;

  currentClickRow: STProData;

  clickedRowClassName = 'st-pro-row-clicked';

  @HostBinding('class') rootClass = 'st-pro';

  config: STProConfig;
  
  private domLoaded = false;
  
  constructor(
    elementRef: ElementRef,
    private router: Router,
    private readonly cdr: ChangeDetectorRef,
    private readonly xlsx: XlsxService,
    private httpClient: HttpClient,
    private exportSrv: STProExport,
    private readonly userService: UserService,
    private readonly nzDomEventService: NzDomEventService,
    @Optional() @Inject(ST_PRO_CONFIG) config?: STProConfig) {
    this.el = elementRef.nativeElement;
    if(!this.stProKey) {
      this.stProKey = this.router.url;
    }

    this.storageKey = this.userService.userInfo.userId+'@'+this.userService.currentCorp.corpId + '@' + this.stProKey;

    this.config = config || {};

  }

  ngOnInit() {
    this.calcScrollX();
  }

  /**
   * 加载本地存储设置
   */
  loadColumnSettingLocal(): STProColumnSetting{
    const s = localStorage.getItem(this.storageKey);
    if(s){
      return JSON.parse(s);
    }
    return null;
  }
  
  ngAfterViewInit(): void {
    this.nzDomEventService
    .registerResizeListener()
    .pipe(
      debounceTime(200),
      takeUntil(this.destroy$),
      finalize(() => this.nzDomEventService.unregisterResizeListener())
    )
    .subscribe(() => {
      this.resizeHeight();
    });
  }

  ngAfterViewChecked() {
    if(!this.domLoaded) {
      // 初次加载，需要根据初次渲染结果做高度调整
      this.resizeHeight();
    }
  }



  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  tableChange(e: STProChange) {
    // this.pageNum = e.pi;
    if(e.type === 'loaded') {
      this.total = e.total;
    } else if(e.type === 'checkbox') {
      this.isBatchOperate = e.type === 'checkbox' && e.checkbox.length > 0;
      this.batchRows = e.checkbox.length;
      this.selectRow.emit(e.checkbox);
    } else if(e.type === 'pi' || e.type === 'ps') {
      // 记录页码和分页大小，用于展示行号
      if(e.type === 'ps') {
        this.pageSize = e.ps;
      } else if(e.type === 'pi') {
        this.pageNum = e.pi;
      }
      this.pageChange.emit({pageSize: e.ps, pageNum: e.pi})
      this.resizeColumnHeight();
    } else if(e.type === 'click') {
      this.clickRow(e.click.item);

    } else if(e.type === 'dblClick') {
      this.dblClick.emit({
        index: e.dblClick.index,
        item: e.dblClick.item,
        e: e.dblClick.e
      });
    }
  }

  clickRow(row:STProData){
    this.lastClickedRow = this.currentClickRow;
    this.currentClickRow = row;
    if(this.lastClickedRow) {
      this.lastClickedRow._rowClassName = '';
    }
    this.currentClickRow._rowClassName = this.clickedRowClassName;
  }
  
  resizeHeight() {
    // TODO 待优化，组件的dom生成后，计算滚动区高度
    const toolbar = this.el.querySelector('.st-pro-toolbar') as HTMLElement;
    const batchToolbar = this.el.querySelector('.st-pro-batch-toolbar') as HTMLElement;
    const header = this.el.querySelector('thead.ant-table-thead ') as HTMLElement;
    const pager = this.el.querySelector('nz-pagination > .ant-pagination') as HTMLElement;
    if ((toolbar || batchToolbar) && header && pager ) {
      // 判断组件是否渲染结束
      if (this.domLoaded) {
        this.domLoaded = true;
      }
      
      const headerMarginTop = 8;
      const pagerMarginTop = 16;
      const pagerHeight = 24;   // 渲染过程会出现32，最后变为24，此处使用固定值24
      this.scrollY = (this.el.offsetHeight
        - toolbar.offsetHeight
        - batchToolbar.offsetHeight
        - headerMarginTop
        - header.offsetHeight
        - pagerHeight
        - pagerMarginTop) + 'px';
      this.cdr.detectChanges();
      this.resizeColumnHeight();
    }
  }

  resizeColumnHeight() {
    if (!this.virtualScroll) {
      return;
    }
    setTimeout(()=>{
      // TODO 待优化
      const el = this.el.querySelector('tbody.ant-table-tbody tr') as HTMLElement;
      if(el) {
        this.virtualItemSize = el.offsetHeight;
        // console.log('resizeColumnHeight to ' + this.virtualItemSize);
        this.st.cdkVirtualScrollViewport.checkViewportSize();
      }
    },0)
  }

  onColumnSelect(settingColumns: STProColumnLite[]) {
    const setting:STProColumnSetting = {};
    settingColumns.forEach((e,idx)=>{
       setting[e.index] = {
         index: e.index,
         __fixed: e.__fixed,
         __title: e.__title,
         __show: e.__show,
         __sortNum: e.__sortNum
       }
     });
    // 本地保存
    if(this.stProKey) {
      localStorage.setItem(this.storageKey,JSON.stringify(setting));
      if(this.config.columnSetting && this.config.columnSetting.persistUrl) {
        // console.log('将用户配置存储到后台');
        const param = {
          settingKey: this.storageKey,
          settingValue: JSON.stringify(setting)
        };
        this.httpClient.post(this.config.columnSetting.persistUrl, param).pipe().subscribe(res=>{
          // console.log(res);
        });
      } else {
        this.userService.addUserDefinedSetting(this.storageKey, JSON.stringify(setting));
      }
    }

    // 更新顺序和是否显示状态
    this.columns.forEach((e:STProColumn, idx:number)=>{
      const c:STProColumnLite = setting[e.index];
      if(c) {
        // 从本地存储获取列设置信息
        e.__show = c.__show !== undefined ? c.__show : true;
        e.__sortNum = c.__sortNum !== undefined ? c.__sortNum : idx
      }
    });
    // 排序
    this.columns.sort((a,b)=>a.__sortNum>b.__sortNum?1:-1);

    // 优化列设置信息
    this.columns.forEach((e,idx)=>{
      e.__show = e.__show === undefined ? true: e.__show;
      // 重新设置顺序号
      e.__sortNum = idx;
      e.iif = ()=> e.__show
    });


    this.st.resetColumns({
      emitReload: true
    }).then(()=>{
      this.calcScrollX();
      this.resizeHeight();
      this.cdr.detectChanges();
      // console.log('更新列成功');

    });

    this.settingColumns = settingColumns;
    this.columnChange.emit(settingColumns);

  }

  /**
   * 重新计算宽
   */
  private calcScrollX() {
    // setTimeout(()=>{
      this.scrollX = this.columns.filter(e=>e.__show).reduce((acc, v) => acc + v.width, 0) + 'px';
    // },10)
  }

  private initSort(e:STProColumn) {
    if (e.sort === true) {
      e.sort = {
        reName: this.sortInd
      };
    } else if (typeof e.sort === 'string') {
      const sortFieldIndex = e.sort;
      e.sort = {
        key: sortFieldIndex,
        reName: this.sortInd
      };
    } else if (e.sort != null) {
      if (!e.sort.reName) {
        e.sort.reName = this.sortInd;
      } else {
        e.sort.reName = {
          descend: e.sort.reName.descend + '',
          ascend: e.sort.reName.ascend + ''
        };
      }
    }
  }

  private initFormat(e:STProColumn) {
    if(e.index && !e.type && !e.format && !e.buttons) {
      if(e.type === 'date') {
        // console.log(e);
      }
      e.format = (item: STProData, col: STProColumn, index: number) => {
        let v = deepGet(item, col.index,'');
        if(v === null || v === undefined || v ==='') {
          v = '--';
        } else {
          v = v+'';
        }
        return v;
      }
    }
  }

  reset(extraParams?: any, options?: STLoadOptions) {
    this.st.reset(extraParams, options);
  }
  // exportWork() {
  //   const param = this.getParams();
  //   const page = new Page(1, 99999);
  //   this.loading = true;
  //   this.httpClient
  //   .post('/api/anyfix/work-request/query', {...page, ...param})
  //   .pipe(
  //     finalize(() => {
  //       this.loading = false;
  //       this.cdf.markForCheck();
  //     })
  //   )
  //   .subscribe((res: any) => {
  //     if (res.data) {
  //       // this.stPro.export(res.data.list);
  //       this.stPro.export();
  //     }
  //   });
  // }

  // export(data:STProData, option?: STProExportOptions) {
  //   const filterType = ['checkbox','radiobox','img',]
  //   const opt = deepMergeKey({}, true, {limit: 10000}, option);
  //   const title = [this.columns.filter(i=>{
  //     if( )
  //   }).map(i => i.title)];
  //
  //   this.httpClient.request()
  // }
  // export(newData?: STData[] | true, opt?: STExportOptions) {
  //
  //   this.st.export(newData, opt);
  // }

  export(newData?: STData[] | true, opt?: STExportOptions) {
    if(opt && opt.filename.indexOf('xlsx') < 0) {
      opt.filename = opt.filename + '.xlsx';
    }
    (newData === true ? from(this.st.filteredData) : of(newData || this.st._data)).subscribe((res: STData[]) =>
      this.exportSrv.export({
        ...opt,
        _d: res,
        _c: this.columns,
      }),
    );
  }

}
