import { Injectable, Optional } from '@angular/core';
import { XlsxService } from '@delon/abc/xlsx';
import { deepGet } from '@delon/util';
import {STProColumn, STProExportOptions} from '@shared/components/st-pro/st.pro.interfaces';

/**
 * 将数字转为 excel 列编号，如 0->A, 25->Z。来自 sheetjs
 * @param col
 * @returns {string}
 */
export function encode_col(col) {
  if (col < 0) {
    throw new Error('invalid column ' + col);
  }
  let s = '';
  for (++col; col; col = Math.floor((col - 1) / 26)) {
    s = String.fromCharCode(((col - 1) % 26) + 65) + s;
  }
  return s;
}


@Injectable()
export class STProExport {
  constructor(@Optional() private xlsxSrv: XlsxService) {}

  private _stGet(item: any, col: STProColumn, index: number): any {
    const ret: { [key: string]: any } = { t: 's', v: '' };

    if (col.format) {
      ret.v = col.format(item, col, index);
    } else {
      const val = deepGet(item, col.index, '');
      ret.v = val;
      switch (col.type) {
        case 'currency':
          ret.t = 'n';
          break;
        case 'date':
          if(ret.v === null || ret.v === '') {
            // 空字符串，设置为日期格式，会导致 excel 打开报错
            ret.v = '';
          } else {
            ret.t = 'd';
          }
          break;
        case 'yn':
          ret.v = ret.v === col.ynTruth ? col.ynYes || '是' : col.ynNo || '否';
          break;
        case 'badge':
          ret.v = col.badge && col.badge[ret.v] && col.badge[ret.v].text || ret.v;
          break;
        case 'tag':
          ret.v = col.tag && col.tag[ret.v] && col.tag[ret.v].text || ret.v;
          break;
        case 'no':
          ret.v = index + 1;
          break;
      }
    }

    ret.v = ret.v || '';

    return ret;
  }

  private genSheet(opt: STProExportOptions): { [sheet: string]: {} } {
    const sheets: { [sheet: string]: {} } = {};
    const sheet = (sheets[opt.sheetname || 'Sheet1'] = {});
    const colData = opt._c!.filter(w => w.exported !== false
      && w.index // Pro 版必须包含 index，包括 CheckBox
      && (!w.buttons || w.buttons.length === 0)
      && (w.type === undefined
        || (w.type !== 'checkbox' && w.type !== 'radio'))
      );
    const cc = colData.length;
    const dc = opt._d!.length;

    // column
    for (let i = 0; i < cc; i++) {
      const tit = colData[i].title;
      sheet[`${encode_col(i)}1`] = {
        t: 's',
        v: typeof tit === 'object' ? tit.text : tit,
      };
    }

    // content
    for (let i = 0; i < dc; i++) {
      for (let j = 0; j < cc; j++) {
        sheet[`${encode_col(j)}${i + 2}`] = this._stGet(opt._d![i], colData[j], i);
      }
    }
    if (cc > 0 && dc > 0) {
      sheet['!ref'] = `A1:${encode_col(cc)}${dc + 1}`;
    }

    return sheets;
  }

  export(opt: STProExportOptions) {
    const sheets = this.genSheet(opt);
    return this.xlsxSrv.export({
      sheets,
      filename: opt.filename,
      callback: opt.callback,
    });
  }
}
