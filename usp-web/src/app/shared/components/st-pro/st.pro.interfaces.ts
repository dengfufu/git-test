import {
  STChange,
  STColumn,
  STColumnBadge,
  STColumnTag,
  STData,
  STExportOptions,
  STPage,
  STReq,
  STRequestOptions,
  STRes,
  STWidthMode
} from '@delon/abc';
import {InjectionToken} from '@angular/core';


export interface STProColumn extends STColumn{
  
  tag?: STProColumnTag | null;
  badge?: STProColumnBadge | null;
  width:number;
  /**
   * 唯一
   */
  index:string;
  __fixed?:string;
  __title?:string;
  __show?:boolean
  __sortNum?:number
}

export interface STProColumnLite {
  index:string;
  __fixed?:string;
  __title:string;
  __show?:boolean;
  __sortNum:number;
  [key: string]:any;
}
export interface STProColumnSetting {
  [key: string]: STProColumnLite;
}
export interface STProData extends STData{

}

export interface STProPage extends STPage{

}
export interface STProColumnBadge extends STColumnBadge{

}
export interface STProColumnTag extends STColumnTag{

}

export interface STProChange extends STChange{
}

export interface STProReq extends STReq {

}
export interface STProRequestOptions extends STRequestOptions {

}
export interface STProRes extends STRes {

}
export interface STProWidthMode extends STWidthMode {

}
export interface STProExportOptions extends STExportOptions{
  limit? :number;
  _c?: STProColumn[];
}
export class Page {
  pageNum: any = 1; // 页码
  pageSize: any = 20; // 偏移列
}
export type STProOverflowMode = 'truncate' | 'wrap';

export interface STProSortIndicator
{
  ascend?: string | number;
  descend?: string | number;
}

export const ST_PRO_CONFIG = new InjectionToken<STProConfig>('st-pro-config');

export interface STProConfig {
  columnSetting?: ColumnSettingConfig;
}
export interface ColumnSettingConfig {
  persistUrl?:string; // 持久化设置的 URL
}
