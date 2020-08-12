export function isUndefined(value: any) {
  return typeof value === 'undefined';
}

export function isFunction(value: any) {
  return typeof value === 'function';
}

export function isNumber(value: any) {
  return typeof value === 'number';
}

export function isString(value: any) {
  return typeof value === 'string';
}

export function isBoolean(value: any) {
  return typeof value === 'boolean';
}

export function isObject(value: any) {
  return value !== null && typeof value === 'object';
}

export function isNumberFinite(value: any) {
  return isNumber(value) && isFinite(value);
}

export function applyPrecision(num: number, precision: number) {
  if (precision <= 0) {
    return Math.round(num);
  }

  const tho = 10 ** precision;

  return Math.round(num * tho) / tho;
}

export function extractDeepPropertyByMapKey(obj: any, map: string): any {
  const keys = map.split('.');
  const head = keys.shift();

  return keys.reduce((prop: any, key: string) => {
    return !isUndefined(prop) && !isUndefined(prop[key])
      ? prop[key]
      : undefined;
  }, obj[head || '']);
}

export function getKeysTwoObjects(obj: any, other: any): any {
  return [...Object.keys(obj), ...Object.keys(other)]
    .filter((key, index, array) => array.indexOf(key) === index);
}

export function isDeepEqual(obj: any, other: any): any {
  if (!isObject(obj) || !isObject(other)) {
    return obj === other;
  }

  return getKeysTwoObjects(obj, other).every((key: any): boolean => {
    if (!isObject(obj[key]) && !isObject(other[key])) {
      return obj[key] === other[key];
    }
    if (!isObject(obj[key]) || !isObject(other[key])) {
      return false;
    }

    return isDeepEqual(obj[key], other[key]);
  });
}

function coerceBooleanProperty(value) {
  return value != null && `${value}` !== 'false';
}

function coerceNumberProperty(value, fallbackValue = 0) {
  // parseFloat(value) handles most of the cases we're interested in (it treats null, empty string,
  // and other non-number values as NaN, where Number just uses 0) but it considers the string
  // '123hello' to be a valid number. Therefore we also check if Number(value) is NaN.
  return isNaN(parseFloat((value))) || isNaN(Number(value)) ? fallbackValue : Number(value);
}

export function toBoolean(value: boolean | string): boolean {
  return coerceBooleanProperty(value);
}

export function toNumber(value: number | string, fallback: number): number {
  return coerceNumberProperty(value, fallback);
}

export function isNotNil(value: any): boolean {
  return (typeof (value) !== 'undefined') && value !== null;
}

export function objectKeysToLowerCase(input) {
  if (typeof input !== 'object') {
    return input;
  }
  if (Array.isArray(input)) {
    return input.map(objectKeysToLowerCase);
  }
  return Object.keys(input).reduce((newObj, key) => {
      const val = input[key];
      const newVal = (val && typeof val === 'object') ? objectKeysToLowerCase(val) : val;
      newObj[key.toLowerCase()] = newVal;
      return newObj;
    }
    ,
    {}
  );
}


/**
 * 判断是否为空
 * @param input 输入
 */
export function isNull(input: any) {
  if (input === undefined || typeof input === 'undefined' || input == null) {
    return true;
  }
  let str = input.toString();
  str = escapeSpace(str);
  str = str.replace(/\r\n/g, ''); // 去掉回车符
  if (str === '') {
    return true;
  }
  return false;
}

/**
 * 使用正则去掉所有类型的空格，包括特殊空格E2 80 86
 * @param s 字符串
 */
export function escapeSpace(s: string): string {
  return s.replace(/\s*/g, '');
}

/**
 * 获得字节长度
 * @param str 输入
 */
export function getLengthByte(str: any) {
  if (isNull(str)) {
    return 0;
  }
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
 * 检查是否全部为数字
 */
export function verifyNum(numValue) {
  const reg = /^[0-9]*$/;
  const temp = numValue.match(reg);
  return (temp === numValue) ? true : false;
}
