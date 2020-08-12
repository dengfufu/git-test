import {NzCascaderOption, NzShowSearchOptions} from 'ng-zorro-antd';

export class ZorroUtils {

  // Cascader: 模糊匹配
  static nodes = [];
  static inputValue = '';
  static tm = 0;

  static showSearchOptions: NzShowSearchOptions = {
    filter(inputValue: string, path: NzCascaderOption[]): boolean {
      // tslint:disable-next-line:no-non-null-assertion
      const b = path.some(p => p.label!.indexOf(inputValue) !== -1);
      if (b) {
        if (inputValue !== ZorroUtils.inputValue || (Date.now() - ZorroUtils.tm > 1000)) {
          ZorroUtils.nodes = [];
          ZorroUtils.inputValue = inputValue;
          ZorroUtils.tm = Date.now();
        }
        if (path.length >= 3 && path[2].label.indexOf(inputValue) === -1) {
          // 去掉区
          path.pop();
          if (path[1].label.indexOf(inputValue) === -1) {
            // 去掉市
            path.pop();
            if (!ZorroUtils.nodes.includes(path[0].label)) {
              ZorroUtils.nodes.push(path[0].label);
              return true;
            } else {
              return false;
            }
          } else {
            if (!ZorroUtils.nodes.includes(path[1].label)) {
              ZorroUtils.nodes.push(path[1].label);
              return true;
            } else {
              return false;
            }
          }
        } else if (path.length === 2 && path[1].label.indexOf(inputValue) === -1) {
          // 去掉市
          path.pop();
          if (!ZorroUtils.nodes.includes(path[0].label)) {
            ZorroUtils.nodes.push(path[0].label);
            return true;
          } else {
            return false;
          }
        }
      }
      return b;
    }
  };
}



