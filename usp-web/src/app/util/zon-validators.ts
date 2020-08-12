import {AbstractControl, FormGroup, ValidatorFn, Validators} from '@angular/forms';

/**
 * @author CK
 * @date 2018/12/6
 * @Description:
 * @Tips: db2长度使用byte,mysql长度为字符串长度
 */
export class ZonValidators {

  private static isPresent(obj: any): boolean {
    return obj !== undefined && obj !== null;
  }

  private static isValidDate(obj: any): boolean {
    return !/Invalid|NaN/.test(new Date(obj).toString());
  }

  private static getLengByte(str: any) {
    let leg = str.length;
    for (const i in str) {
      if (str.hasOwnProperty(i)) {
        const db = str[i].charCodeAt(0).toString(16).length === 4;
        if (db) {
          leg += 1;
        }
      }
    }
    return leg;
  }

  /**
   * 校验表单,返回错误信息
   * @param formGroup: 遍历获取错误信息
   */
  static getErrorExplain(formGroup: FormGroup) {
    let errorMsg;
    for (const item of Object.keys(formGroup.controls)) {
      const itemControl: any = formGroup.controls[item];
      if (itemControl.invalid) {
        // 嵌套formGroup递归
        if (itemControl.controls) {
          errorMsg = this.getErrorExplain(itemControl);
        } else {
          errorMsg = itemControl.errors.explain;
        }
        if (errorMsg) {
          return errorMsg;
        }
      }
    }
  }

  static required(name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (!this.isPresent(Validators.required(control))) {
        return null;
      }
      if(!name) {
        return {required: true, error: true, explain: '不能为空' };
      }
      return {required: true, error: true, explain: '请输入' + name};
    };
  }

  static isEmail(name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      // tslint:disable-next-line:max-line-length
      if (control.value.match(/[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/)) {
        return null;
      } else {
        if (!name) {
          return {isEmail: true, error: true, explain: '无效邮箱'};
        } else {
          return {isEmail: true, error: true, explain: name + ':邮箱格式不正确'};
        }
      }
    };
  }

  static notEmptyString(name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      // tslint:disable-next-line:max-line-length
      if (control.value.match(/[^\s]+/)) {
        return null;
      } else {
        return {empty: true, error: true, explain:  '不能为空格'};
      }
    };
  }

  static isZip(name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      if (control.value.match(/^(?:[1-9][0-9]?|1[01][0-9]|120)$/i)) {
        return null;
      } else {
        if (!name) {
          return {isZip: true, error: true, explain: '无效邮编'};
        } else {
          return {isZip: true, error: true, explain: name + ':邮编格式不正确'};
        }
      }
    };
  }


  static isMobile(name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      if (control.value.match(/^1\d{10}$/i)) {
        return null;
      } else {
        if (!name) {
          return {isMobile: true, error: true, explain: '无效手机'};
        } else {
          return {isMobile: true, error: true, explain: name + ':手机格式不正确'};
        }
      }

    };
  }

  static isIdCard(name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      if (control.value.match(/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x|\*)$)/)) {
        return null;
      } else {
        if (!name) {
          return {isIdCard: true, error: true, explain: '身份证输入不合法'};
        } else {
          return {isIdCard: true, error: true, explain: name + ':身份证输入不合法'};
        }
      }
    };
  }

  static isPhone(name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      if (control.value.match(/^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i)) {
        return null;
      } else {
        if (!name) {
          return {isPhone: true, error: true, explain: '无效电话'};
        } else {
          return {isPhone: true, error: true, explain: name + ':电话格式不正确'};
        }
      }
    };
  }


  static phoneOrMobile(name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      if (control.value.match(/^((0\d{2,3}-\d{7,8}(-\d{1,5})?)|(1\d{10}))$/i)) {
        return null;
      } else {
        if (!name) {
          return {phoneOrMobile: true, error: true, explain: '无效手机或座机号码(区号-开头)。'};
        } else {
          return {phoneOrMobile: true, error: true, explain: name + ':无效手机或座机号码(区号-开头)'};
        }
      }
    };
  }

  static isEnglish(name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      if (control.value.match(/^[A-Za-z]+$/i)) {
        return null;
      } else {
        if (!name) {
          return {isEnglish: true, error: true, explain: '无效英文'};
        } else {
          return {isEnglish: true, error: true, explain: name + ':非英文字母'};
        }
      }
    };
  }

  static isChinese(name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      if (control.value.match(/^[\u0391-\uFFE5]+$/)) {
        return null;
      } else {
        if (!name) {
          return {isChinese: true, error: true, explain: '无效中文'};
        } else {
          return {isChinese: true, error: true, explain: name + ':非中文'};
        }
      }
    };
  }

  static isAge(name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      if (('' + control.value).match(/^(?:[1-9][0-9]?|1[01][0-9]|120)$/i)) {
        return null;
      } else {
        if (!name) {
          return {isAge: true, error: true, explain: '无效年龄'};
        } else {
          return {isAge: true, error: true, explain: name + ':无效年龄'};
        }
      }
    };
  }

  static isInt(name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      if (('' + control.value).match(/^[0-9]*[1-9][0-9]*$/i)) {
        return null;
      } else {
        if (!name) {
          return {isInt: true, error: true, explain: '请输入正整数'};
        } else {
          return {isInt: true, error: true, explain: name + ':请输入正整数'};
        }
      }
    };
  }


  static intOrFloat(name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      if (('' + control.value).match(/^\d+(\.\d+)?$/i)) {
        return null;
      } else {
        if (!name) {
          return {intOrFloat: true, error: true, explain: '请输入正整数或正小数'};
        } else {
          return {intOrFloat: true, error: true, explain: name + ':请输入正整数或正小数'};
        }
      }
    };
  }

  static intOrZero(name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      if (('' + control.value).match(/^(0|[1-9]\d*)$/i)) {
        return null;
      } else {
        if (!name) {
          return {intOrZero: true, error: true, explain: '请输入非负整数'};
        } else {
          return {intOrZero: true, error: true, explain: name + ':请输入非负整数'};
        }
      }
    };
  }

  static len(len: number, name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      const v: string = control.value;
      if (!name) {
        return v.length === len ? null : {len: true, error: true, explain: '长度需为' + len};
      } else {
        return v.length === len ? null : {len: true, error: true, explain: name + ':长度需为' + len};
      }
    };
  }

  static minLength(minLen: number): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      const v: string = control.value;
      if (!name) {
        return v.length >= minLen ? null : {minLength: true, error: true, explain: '长度需大于等于' + minLen};
      } else {
        return v.length >= minLen ? null : {minLength: true, error: true, explain: name + ':长度需大于等于' + minLen};
      }
    };
  }

  static maxLength(maxLen: number, name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      const v: string = control.value;
      if( v.length > maxLen ) {
        control.setValue(v.substr(0,maxLen));
        if(!name) {
          name = '';
        }
        return  {maxLength: true, error: true, explain:  name + `长度需小于等于${maxLen}`};
      }
    };
  }

  static rangeLength(rangeLen: Array<number>, name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      const v: string = control.value;
      if (!name) {
        return v.length >= rangeLen[0] && v.length <= rangeLen[1] ? null : {
          rangeLength: true, error: true, explain: '长度需介于' + rangeLen[0] + '和' + rangeLen[1] + '之间'
        };
      } else {
        return v.length >= rangeLen[0] && v.length <= rangeLen[1] ? null : {
          rangeLength: true, error: true, explain: name + ':长度需介于' + rangeLen[0] + '和' + rangeLen[1] + '之间'
        };
      }

    };
  }

  static lenByByte(len: number, name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      const v: string = control.value;
      if (!name) {
        return this.getLengByte(v) === len ? null : {
          len: true,
          error: true,
          explain: '长度需为' + len + '(汉字长度为2,英文字母长度为1)'
        };
      } else {
        return this.getLengByte(v) === len ? null : {
          len: true,
          error: true,
          explain: name + ':长度需为' + len + '(汉字长度为2,英文字母长度为1)'
        };
      }
    };
  }

  static minLengthByByte(minLen: number): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      const v: string = control.value;
      if (!name) {
        return this.getLengByte(v) >= minLen ? null : {
          minLength: true,
          error: true,
          explain: '长度需大于等于' + minLen + '(汉字长度为2,英文字母长度为1)'
        };
      } else {
        return this.getLengByte(v) >= minLen ? null : {
          minLength: true,
          error: true,
          explain: name + ':长度需大于等于' + minLen + '(汉字长度为2,英文字母长度为1)'
        };
      }
    };
  }

  static maxLengthByByte(maxLen: number, name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      const v: string = control.value;
      if (!name) {
        return this.getLengByte(v) <= maxLen ? null : {
          maxLength: true,
          error: true,
          explain: `长度(当前${this.getLengByte(v)})需小于等于${maxLen}` + '(汉字长度为2,英文字母长度为1)'
        };
      } else {
        return this.getLengByte(v) <= maxLen ? null : {
          maxLength: true,
          error: true,
          explain: name + ':长度需小于等于' + maxLen + '(汉字长度为2,英文字母长度为1)'
        };
      }
    };
  }

  static rangeLengthByByte(rangeLen: Array<number>, name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      const v: string = control.value;
      if (!name) {
        return this.getLengByte(v) >= rangeLen[0] && this.getLengByte(v) <= rangeLen[1] ? null : {
          rangeLength: true,
          error: true,
          explain: '长度需介于' + rangeLen[0] + '和' + rangeLen[1] + '之间' + '(汉字长度为2,英文字母长度为1)'
        };
      } else {
        return this.getLengByte(v) >= rangeLen[0] && this.getLengByte(v) <= rangeLen[1] ? null : {
          rangeLength: true,
          error: true,
          explain: name + ':长度需介于' + rangeLen[0] + '和' + rangeLen[1] + '之间' + '(汉字长度为2,英文字母长度为1)'
        };
      }

    };
  }


  static isDate(name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      const v: string = control.value;
      if (!name) {
        return this.isValidDate(v) ? null : {isDate: true, error: true, explain: '无效日期'};
      } else {
        return this.isValidDate(v) ? null : {isDate: true, error: true, explain: name + '日期格式不正确'};
      }
    };
  }

  // tslint:disable-next-line:variable-name
  static minDate(_minDate: any, name?: string): ValidatorFn {

    if (!this.isValidDate(_minDate) && !(_minDate instanceof Function)) {
      throw Error('_minDate value must be or return a formatted date');
    }

    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }

      const d: Date = new Date(control.value);

      if (!this.isValidDate(d)) {
        if (!name) {
          return {minDate: true, error: true, explain: '无效日期'};
        } else {
          return {minDate: true, error: true, explain: name + ':日期格式不正确'};
        }
      }
      if (_minDate instanceof Function) {
        _minDate = _minDate();
      }

      if (!name) {
        return d >= new Date(_minDate) ? null : {minDate: true, error: true, explain: '日期需大于等于' + _minDate};
      } else {
        return d >= new Date(_minDate) ? null : {minDate: true, error: true, explain: name + ':日期需大于等于' + _minDate};
      }
    };
  }

  // tslint:disable-next-line:variable-name
  static maxDate(_maxDate: any, name?: string): ValidatorFn {
    if (!this.isValidDate(_maxDate) && !(_maxDate instanceof Function)) {
      throw Error('_maxDate value must be or return a formatted date');
    }
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      const d: Date = new Date(control.value);
      if (!this.isValidDate(d)) {
        if (!name) {
          return {maxDate: true, error: true, explain: '无效日期'};
        } else {
          return {maxDate: true, error: true, explain: name + ':日期格式不正确'};
        }
      }
      if (_maxDate instanceof Function) {
        _maxDate = _maxDate();
      }

      if (!name) {
        return d <= new Date(_maxDate) ? null : {maxDate: true, error: true, explain: '日期需小于等于' + _maxDate};
      } else {
        return d <= new Date(_maxDate) ? null : {maxDate: true, error: true, explain: name + ':日期需小于等于' + _maxDate};
      }
    };
  }

  static min(minNum: number, name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      const v: number = +control.value;
      if (!name) {
        return v >= +minNum ? null : {min: true, error: true, explain: '值需大于等于' + minNum};
      } else {
        return v >= +minNum ? null : {min: true, error: true, explain: name + ':值需大于等于' + minNum};
      }
    };
  }


  static lessThen(num: number, name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      const v: number = +control.value;
      if (!name) {
        return v < +num ? null : {lessThen: true, error: true, explain: '值需小于' + num};
      } else {
        return v < +num ? null : {lessThen: true, error: true, explain: name + ':值需小于' + num};
      }
    };
  }

  static max(maxNum: number, name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      const v: number = +control.value;
      if (!name) {
        return v <= +maxNum ? null : {max: true, error: true, explain: '值需小于等于' + maxNum};
      } else {
        return v <= +maxNum ? null : {max: true, error: true, explain: name + ':值需小于等于' + maxNum};
      }
    };
  }

  static greatThen(num: number, name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      const v: number = +control.value;
      if (!name) {
        return v > +num ? null : {greatThen: true, error: true, explain: '值需大于' + num};
      } else {
        return v > +num ? null : {greatThen: true, error: true, explain: name + ':值需大于' + num};
      }
    };
  }

  // tslint:disable-next-line:variable-name
  static range(_range: Array<number>, name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      const v: number = +control.value;
      if (!name) {
        return v >= _range[0] && v <= _range[1] ? null : {
          range: true,
          error: true,
          explain: '值需介于' + _range[0] + '和' + _range[1] + '之间'
        };
      } else {
        return v >= _range[0] && v <= _range[1] ? null : {
          range: true,
          error: true,
          explain: name + ':值需介于' + _range[0] + '和' + _range[1] + '之间'
        };
      }
    };
  }

  // tslint:disable-next-line:variable-name
  static rangeInt(_range: Array<number>, name?: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (this.isPresent(Validators.required(control))) {
        return null;
      }
      const v: string = '' + control.value;
      if (!name) {
        // tslint:disable-next-line:radix
        return v.match(/^[0-9]*[1-9][0-9]*$/i) && Number.parseInt(v) >= _range[0] && Number.parseInt(v) <= _range[1] ? null : {
          rangeInt: true,
          error: true,
          explain: '值需介于' + _range[0] + '和' + _range[1] + '之间的整数'
        };
      } else {
        // tslint:disable-next-line:radix
        return v.match(/^[0-9]*[1-9][0-9]*$/i) && Number.parseInt(v) >= _range[0] && Number.parseInt(v) <= _range[1] ? null : {
          rangeInt: true,
          error: true,
          explain: name + ':值需介于' + _range[0] + '和' + _range[1] + '之间的整数'
        };
      }
    };
  }
}


