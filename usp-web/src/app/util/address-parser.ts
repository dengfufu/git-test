import {ngModuleJitUrl} from '@angular/compiler';

export class AddressParser {
  static MZ = ['蒙古族','回族','藏族','维吾尔族','苗族','彝族','壮族','布依族','朝鲜族','满族','侗族','瑶族',
    '白族','土家族','哈尼族','哈萨克族','傣族','黎族','僳僳族','佤族','畲族','高山族','拉祜族',
    '水族','东乡族','纳西族','景颇族','柯尔克孜族','土族','达斡尔族','仫佬族','羌族','布朗族',
    '撒拉族','毛南族','仡佬族','锡伯族','阿昌族','普米族','塔吉克族','怒族','乌孜别克族',
    '俄罗斯族','鄂温克族','德昂族','保安族','裕固族','京族','塔塔尔族','独龙族','鄂伦春族',
    '赫哲族','门巴族','珞巴族','基诺族'];

  static SUR_NAME1 =[
    '赵', '钱', '孙', '李', '周', '吴', '郑', '王', '冯', '陈',
    '褚', '卫', '蒋', '沈', '韩', '杨', '朱', '秦', '尤', '许',
    '何', '吕', '施', '张', '孔', '曹', '严', '华', '金', '魏',
    '陶', '姜', '戚', '谢', '邹', '喻', '柏', '水', '窦', '章',
    '云', '苏', '潘', '葛', '奚', '范', '彭', '郎', '鲁', '韦',
    '昌', '马', '苗', '凤', '花', '方', '俞', '任', '袁', '柳',
    '酆', '鲍', '史', '唐', '费', '廉', '岑', '薛', '雷', '贺',
    '倪', '汤', '滕', '殷', '罗', '毕', '郝', '邬', '安', '常',
    '乐', '于', '时', '傅', '皮', '卞', '齐', '康', '伍', '余',
    '元', '卜', '顾', '孟', '平', '黄', '和', '穆', '萧', '尹',
    '姚', '邵', '湛', '汪', '祁', '毛', '禹', '狄', '米', '贝',
    '明', '臧', '计', '伏', '成', '戴', '谈', '宋', '茅', '庞',
    '熊', '纪', '舒', '屈', '项', '祝', '董', '粱', '杜', '阮',
    '蓝', '闵', '席', '季', '麻', '强', '贾', '路', '娄', '危',
    '江', '童', '颜', '郭', '梅', '盛', '林', '刁', '钟', '徐',
    '邱', '骆', '高', '夏', '蔡', '田', '樊', '胡', '凌', '霍',
    '虞', '万', '支', '柯', '咎', '管', '卢', '莫', '经', '房',
    '裘', '缪', '干', '解', '应', '宗', '宣', '丁', '贲', '邓',
    '郁', '单', '杭', '洪', '包', '诸', '左', '石', '崔', '吉',
    '钮', '龚', '程', '嵇', '邢', '滑', '裴', '陆', '荣', '翁',
    '荀', '羊', '於', '惠', '甄', '麴', '封', '芮', '羿', // '加', '封',
    '储', '汲', '邴', '糜', '松', '井', '段', '富', '巫', '乌',
    '焦', '巴', '弓', '牧', '隗', '谷', '车', '侯', '宓', // '山', '谷',
    '蓬', '全', '郗', '班', '仰', '秋', '仲', '伊', '宫', '宁',
    '仇', '栾', '暴', '甘', '钭', '厉', '戎', '祖', '武', '符',
    '刘', '景', '詹', '束', '龙', '叶', '幸', '司', '韶', '郜',
    '黎', '蓟', '薄', '印', '宿', '白', '怀', '蒲', '台', '从',
    '鄂', '索', '咸', '籍', '赖', '卓', '蔺', '屠', '胥', '能',
    '苍', '双', '闻', '莘', '党', '翟', '谭', '贡', '劳', '逄',
    '姬', '申', '扶', '堵', '冉', '宰', '郦', '雍', '郤', '璩',
    '桑', '桂', '濮', '牛', '寿', '通', '边', '扈', '燕', '冀',
    '郏', '浦', '尚', '农', '温', '别', '庄', '晏', '柴', '瞿',
    '阎', '充', '慕', '连', '茹', '习', '宦', '艾', '鱼', '容',
    '向', '古', '易', '慎', '戈', '廖', '庚', '终', '暨', '居',
    '衡', '步', '都', '耿', '满', '弘', '匡', '国', '文', '寇',
    '广', '禄', '阙', '殴', '殳', '沃', '利', '蔚', '越', // '东', '殴',
    '夔', '隆', '师', '巩', '厍', '聂', '晁', '勾', '敖', '融',
    '冷', '訾', '辛', '阚', '那', '简', '饶', '空', '曾', '毋',
    '沙', '乜', '养', '鞠', '须', '丰', '巢', '关', '蒯', '相',
    '查', '后', '荆', '红', '游', '竺', '权', '逯', '盖', '益',
    '桓', '公', '万', '俟', '司', '马'];
  static SUR_NAME2 = ['上官', '欧阳', '夏侯', '诸葛',
    '闻人', '东方', '赫连', '皇甫', '尉迟', '公羊', '澹台', '公冶', '宗政', '濮阳',
    '淳于', '仲孙', '太叔', '申屠', '公孙', '乐正', '轩辕', '令狐', '钟离', '闾丘',
    '长孙', '慕容', '鲜于', '宇文', '司徒', '司空', '亓官', '司寇', '仉督', '子车',
    '颛孙', '端木', '巫马', '公西', '漆雕', '乐正', '壤驷', '公良', '拓拔', '夹谷',
    '宰父', '谷粱', '晋楚', '闫法', '汝鄢', '涂钦', '段干', '百里', '东郭', '南门',
    '呼延', '妫海', '羊舌', '微生', '岳帅', '缑亢', '况後', '有琴', '梁丘', '左丘',
    '东门', '西门', '商牟', '佘佴', '伯赏', '南宫', '墨哈', '谯笪', '年爱', '阳佟'
  ];
  static CALL_NAME = ['先生', '女士', '小姐', '师傅', '校长', '老师', '经理', '总监', '助理', '秘书', '主任', '处长', '科长', '主管', '厂长'];
  static REGION_NAME = ['省', '市', '区', '州', '县', '镇', '街', '乡', '村'];

  static PHONE_PATTERN = /(0\d{10,11}(-\d{1,5})?)|(1[3-9]\d(-)?\d{8})|(0\d{2,3}-\d{7,8}(-\d{1,5})?)/;
  static PHONE_PATTERN2 = /(\d{7,8}(-\d{1,5})?)/;

  /**
   * 从地址中解析行政区划
   * @param str 详细地址，包含联系人和电话
   * @param districtsTree 区划数据（树状）
   */
  static parseDistrict(str, districtsTree) {
    str = (str ||'').trim();
    str = str.replace(/(详细地址|通讯地址|联系地址|地址|联系人|联系电话|电话|座机|电话号码|手机号码|手机号)/g, '');
    const splittorPattern = /([\n\s,，:：。;；　])+/g;
    str = str.replace(splittorPattern, '').trim();
    return this.parseArea(str, districtsTree);
  }

  /**
   * 从地址中解析行政区划、联系人、电话。例如：南山区XX路XX号,李先生13912345678
   * @param str 详细地址，包含联系人和电话
   * @param districtsTree 区划数据（树状）
   */
  static parseAll(str, districtsTree) {
    str = (str ||'').trim();
    str = str.replace(/(详细地址|通讯地址|联系地址|地址|联系人|联系电话|电话|座机|电话号码|手机号码|手机号)/g, ' ');
    str = this.splittorTrim(str);
    const splittorPattern = /([\n\s,，:：。])+/g;

    let addr = str;
    let matches = this.PHONE_PATTERN.exec(str);
    let phone = '';
    let contact = '';
    if (matches) {
      // console.log(matches);
      phone = matches[0];
      addr = str.substring(0, matches.index);
      contact = str.substring(matches.index + phone.length);
    } else {
      matches = this.PHONE_PATTERN2.exec(str);
      if (matches) {
        phone = matches[0];
        addr = str.substring(0, matches.index);
        contact = str.substring(matches.index + phone.length);
      }
    }
    addr = addr.replace(splittorPattern, ' ').trim();
    contact = contact.replace(splittorPattern, ' ').trim();
    if (contact.length > addr.length) {
      const tmp = addr;
      addr = contact;
      contact = tmp;
    }
    // 是否常用称呼
    if (this.hasCallName(addr)) {
      const name = this.parseName(addr);
      if (name) {
        addr = contact + addr.substring(0, addr.length - name.length);
        contact = name;
      }
    }
    // 是否地址
    if (contact && this.isAddress(contact)) {
      const tmp = addr;
      addr = contact;
      contact = tmp;
    }
    if (!contact) {
      matches = /([\s])+/.exec(addr);
      if (matches) {
        const splittor = matches[0];
        contact = addr.substring(matches.index + splittor.length);
        addr = addr.substring(0, matches.index);
        contact = contact.replace(/([\s])+/g, '').trim();
        if (contact.length > addr.length) {
          const tmp = addr;
          addr = contact;
          contact = tmp;
        }
      }
      if (addr.length > 0 && !contact) {
        // after
      } else if (contact.length > 1) {
        if (this.hasCallName(contact)) {
          const name = this.parseName(contact);
          if (name) {
            addr = addr + contact.substring(0, contact.length - name.length);
            contact = name;
          }
        } else {
          let name = this.parseName(contact);
          if (name) {
            addr = addr + contact.substring(0, contact.length - name.length);
            contact = name;
          } else {
            name = this.parseName(addr);
            if (name) {
              addr = addr.substring(0, addr.length - name.length) + contact;
              contact = name;
            }
          }
        }
      }
    }
    str = addr;

    if (str.startsWith('中国')) {
      str = str.substring(2);
    }

    if (str.length < 2){
      if (phone || contact) {
        const rs: any = {};
        if (str.length > 0){
          rs.address = str;
        }
        if (phone) {
          rs.phone = phone;
        }
        if (contact) {
          rs.contact = contact;
        }
        return rs;
      }
      return null;
    }

    let area = this.parseArea(str, districtsTree);

    if (!contact && area && area.address && area.address.length >= 2) {
      addr = area.address;
      if (!this.REGION_NAME.includes(addr.substring(addr.length - 1))) {
        let flag = true;
        if (addr.length >= 3) {
          let last = addr.substring(addr.length - 3);
          if (addr.length >= 4) {
            last = addr.substring(addr.length - 4);
          }
          if (last.indexOf('道') >= 0 || last.indexOf('单元') >= 0
            || last.indexOf('房') >= 0 || last.indexOf('路') >= 0
            || last.indexOf('胡同') >= 0 || last.indexOf('小区') >= 0 || last.indexOf('花园') >= 0) {
            flag = false;
          }
        }
        if (this.hasCallName(addr)) {
          flag = true;
        }
        if (flag) {
          const name = this.parseName(addr);
          if (name) {
            area.address = addr.substring(0, addr.length - name.length);
            contact = name;
          }
        }
      }
    }
    if (!area && (phone || contact)) {
      area = {};
    }
    if (area && phone) {
      area.phone = phone;
    }
    if (area && contact) {
      area.contact = contact;
    }
    return area;
  }

  // -------------------------------------------------------------------------------------------------

  private static provinceTrim(value: string) {
    const value2 = value.replace(/(壮族|回族|维吾尔)/g, '');
    if (value === value2) {
      return [value.replace(/(省|自治区|特别行政区)/g, ''), value];
    } else {
      return [value2.replace(/(省|自治区|特别行政区)/g, ''), value2, value];
    }
  }

  private static replaceMZ(value: string) {
    this.MZ.forEach((mz)=>{
      value = value.replace(mz,'');
    });
    return value;
  }

  private static cityTrim(value: string) {
    if (value.endsWith('自治州')) {
      const value2 = this.replaceMZ(value);
      const value3 = value2.substring(0, value2.length - 3);
      if (value3) {
        return [value3, value3 + '州', value];
      } else  {
        return [value];
      }
    }
    if (value.endsWith('地区')) {
      return [value.substring(0, value.length - 2), value];
    }
    if (value.endsWith('市') && value.length > 2) {
      return [value.substring(0, value.length - 1), value];
    }
    if (value.endsWith('盟') && value.length > 2) {
      return [value.substring(0, value.length - 1), value];
    }
    return [value];
  }

  private static districTrim(value: string) {
    if (value.endsWith('自治县')) {
      const value2 = this.replaceMZ(value);
      const value3 = value2.substring(0, value2.length - 3);
      if (value3) {
        return [value3, value3 + '县', value2, value];
      } else {
        return [value2, value];
      }
    }
    if (value === '莫力达瓦达斡尔族自治旗') {
      return ['莫旗', '莫力达瓦旗', '莫力达瓦自治旗', value];
    }
    if (value === '鄂温克族自治旗') {
      return ['鄂温克自治旗', value];
    }
    if (value === '鄂伦春自治旗') {
      return ['鄂伦春旗', value];
    }
    if (value.endsWith('族区') && value.length > 2) {
      const value2 = this.replaceMZ(value);
      return [value2, value];
    }
    if (value.endsWith('市') && value.length > 2) {
      return [value.substring(0, value.length - 1), value];
    }
    if (value.endsWith('县') && value.length > 2) {
      return [value.substring(0, value.length - 1), value];
    }
    if (value.endsWith('区') && value.length > 2) {
      return [value.substring(0, value.length - 1), value];
    }
    if (value.endsWith('旗') && value.length > 3) {
      return [value.substring(0, value.length - 1), value];
    }
    return [value];
  }

  // 常用称呼
  private static hasCallName(str) {
    if (str.length > 2) {
      if (this.CALL_NAME.includes(str.substring(str.length - 2))) {
        return true;
      }
    }
    return false;
  }

  // 解析姓名
  private static parseName(str) {
    let callName = '';
    if (str.length >= 2) {
      // 常用称呼
      callName = str.substring(str.length - 2);
      if (this.CALL_NAME.includes(callName)) {
        str = str.substring(0, str.length - 2);
      } else {
        callName = '';
      }
    }

    let name = '';
    if (str.length >= 4) {
      name = str.substring(str.length - 4);
      const surname2 = name.substring(0, 2);
      if (this.SUR_NAME2.includes(surname2)) {
        // 复姓
        return name + callName;
      }
      name = str.substring(str.length - 3);
    }
    if (str.length >= 3) {
      name = str.substring(str.length - 3);
    } else if (str.length >= 2) {
      name = str.substring(str.length - 2);
    } else if (str.length >= 1) {
      name = str.substring(str.length - 1);
    }

    if (name.length >= 2) {
      let surname = name.substring(0, 2);
      if (this.SUR_NAME2.includes(surname)) {
        // 复姓
        return name + callName;
      } else {
        surname = name.substring(0, 1);
        if (this.SUR_NAME1.includes(surname)) {
          return name + callName;
        } else {
          if (name.length >= 3) {
            name = name.substring(1);
            surname = name.substring(0, 1);
            if (this.SUR_NAME1.includes(surname)) {
              return name + callName;
            }
            surname = name.substring(0, 2);
            if (this.SUR_NAME2.includes(surname)) {
              // 复姓
              return name + callName;
            }
          }
          name = name.substring(1);
        }
      }
    }

    if (name.length >= 1) {
      if (callName) {
        return name + callName;
      }
      return '';
    }
  }

  private static splittorTrim(str) {

    const provPattern0 = /(北京|天津|上海|重庆|广西|内蒙古|西藏|宁夏|新疆|河北|山西|辽宁|吉林|黑龙江|江苏|浙江|安徽)(.)*([\n\s,，:：。])+(.)+(市|州|地区)/;
    const provPattern01 = /(北京|天津|上海|重庆|广西|内蒙古|西藏|宁夏|新疆|河北|山西|辽宁|吉林|黑龙江|江苏|浙江|安徽)([\n\s,，:：。])+/;
    const provPattern1 = /(福建|江西|山东|河南|湖北|湖南|广东|海南|四川|贵州|云南|陕西|甘肃|青海|台湾|香港|澳门)(.)*([\n\s,，:：。])+(.)+(市|州|地区)/;
    const provPattern11 = /(福建|江西|山东|河南|湖北|湖南|广东|海南|四川|贵州|云南|陕西|甘肃|青海|台湾|香港|澳门)([\n\s,，:：。])+/;
    const provPattern2 = /(省|自治区|特别行政区)([\n\s,，:：。])+/;
    const addressPattern2 = /(市|州|地区|盟)([\n\s,，:：。])+(.)+(市|县|区|旗)/;
    const addressPattern3 = /(市|县|区|旗)([\n\s,，:：。])+(.)+(县|区|镇|乡|村|街道)/;
    const splittorPattern = /([\n\s,，:：。])+/g;
    let matches = provPattern0.exec(str);
    if (matches) {
      const splittor = matches[0];
      const splittor2 = splittor.replace(splittorPattern, '').trim();
      str = str.substring(0, matches.index) + splittor2 + str.substring(matches.index + splittor.length);
    }
    matches = provPattern01.exec(str);
    if (matches) {
      const splittor = matches[0];
      const splittor2 = splittor.replace(splittorPattern, '').trim();
      str = str.substring(0, matches.index) + splittor2 + str.substring(matches.index + splittor.length);
    }
    matches = provPattern1.exec(str);
    if (matches) {
      const splittor = matches[0];
      const splittor2 = splittor.replace(splittorPattern, '').trim();
      str = str.substring(0, matches.index) + splittor2 + str.substring(matches.index + splittor.length);
    }
    matches = provPattern11.exec(str);
    if (matches) {
      const splittor = matches[0];
      const splittor2 = splittor.replace(splittorPattern, '').trim();
      str = str.substring(0, matches.index) + splittor2 + str.substring(matches.index + splittor.length);
    }
    matches = provPattern2.exec(str);
    if (matches) {
      const splittor = matches[0];
      const splittor2 = splittor.replace(splittorPattern, '').trim();
      str = str.substring(0, matches.index) + splittor2 + str.substring(matches.index + splittor.length);
    }
    matches = addressPattern2.exec(str);
    if (matches) {
      const splittor = matches[0];
      const splittor2 = splittor.replace(splittorPattern, '').trim();
      str = str.substring(0, matches.index) + splittor2 + str.substring(matches.index + splittor.length);
    }
    matches = addressPattern3.exec(str);
    if (matches) {
      const splittor = matches[0];
      const splittor2 = splittor.replace(splittorPattern, '').trim();
      str = str.substring(0, matches.index) + splittor2 + str.substring(matches.index + splittor.length);
    }
    return str;
  }

  private static isAddress(str) {
    if (str.startsWith('中国')) {
      str = str.substring(2);
    }
    const provPattern0 = /^(北京|天津|上海|重庆|广西|内蒙古|西藏|宁夏|新疆|河北|山西|辽宁|吉林|黑龙江|江苏|浙江|安徽)(.)*/;
    const provPattern1 = /^(福建|江西|山东|河南|湖北|湖南|广东|海南|四川|贵州|云南|陕西|甘肃|青海|台湾|香港|澳门)(.)*/;
    const addressPattern1 = /(省|自治区|特别行政区)(.)+(市|州|地区)/;
    const addressPattern2 = /(市|州|地区|盟)(.)+(市|县|区|旗)/;
    const addressPattern3 = /(市|县|区|旗)(.)+(县|区|镇|乡|村|街道)/;
    if (provPattern0.test(str)) {
      return true;
    }
    if (provPattern1.test(str)) {
      return true;
    }
    if (addressPattern1.test(str)) {
      return true;
    }
    if (addressPattern2.test(str)) {
      return true;
    }
    if (addressPattern3.test(str)) {
      return true;
    }
  }

  // 解析行政区划
  private static parseArea(addr, districtsTree) {

    if (addr.startsWith('中国')) {
      addr = addr.substring(2);
    }

    let area1 = null;
    let area2 = null;
    let area2p = null;
    let area3 = null;
    let area3p = null;
    let area3c = null;
    let len1 = 0;
    let len2 = 0;
    let len3 = 0;
    if (addr.length < 2) {
      return {address: addr};
    }
    // 省判断
    districtsTree.forEach((province: any) => {
      const pl = this.provinceTrim(province.label);
      pl.forEach((p)=>{
        if (addr.startsWith(p)) {
          const newlen = p.length;
          if (newlen >= len1 && newlen >= len2 &&  newlen >= len3) {
            area1 = province;
            len1 = p.length;
          }
        }
      });

      // 市判断
      if (province.children !== null) {
        province.children.forEach((city: any) => {
          const cl = this.cityTrim(city.label);
          pl.forEach((p)=>{
            cl.forEach((c)=>{
              if (addr.startsWith(p + c)) {
                const newlen = p.length + c.length;
                if (newlen >= len1 && newlen >= len2 &&  newlen >= len3) {
                  area2 = city;
                  area2p = province;
                  len2 = p.length + c.length;
                }
              } else if (addr.startsWith(c) ) {
                const newlen = c.length;
                if (newlen >= len1 && newlen >= len2 &&  newlen >= len3) {
                  area2 = city;
                  area2p = province;
                  len2 = c.length;
                }
              }
            });
          });

          // 区判断
          if (city.children !== null) {
            city.children.forEach((distric: any) => {
              const dl = this.districTrim(distric.label);
              pl.forEach((p)=>{
                cl.forEach((c)=>{
                  dl.forEach((d)=>{
                    if (addr.startsWith(p + c + d)) {
                      const newlen = p.length + c.length + d.length;
                      if (newlen >= len1 && newlen >= len2 &&  newlen >= len3) {
                        area3 = distric;
                        area3c = city;
                        area3p = province;
                        len3 = p.length + c.length + d.length;
                      }
                    } else if (addr.startsWith(c + d)) {
                      const newlen = c.length + d.length;
                      if (newlen >= len1 && newlen >= len2 &&  newlen >= len3) {
                        area3 = distric;
                        area3c = city;
                        area3p = province;
                        len3 = c.length + d.length;
                      }
                    } else if (addr.startsWith(p + d)) {
                      const pd = p + d;
                      const newlen = p.length + d.length;
                      if (newlen >= len1 && newlen >= len2 &&  newlen >= len3) {
                        area3 = distric;
                        area3c = city;
                        area3p = province;
                        len3 = pd.length;
                      }
                      // // 明确按市县区搜索
                      // if (d.endsWith('市') || d.endsWith('县') || d.endsWith('区') || d.endsWith('旗')
                      //   || addr.length === pd.length) {
                      //   area3 = distric;
                      //   area3c = city;
                      //   area3p = province;
                      //   len3 = pd.length;
                      // } else {
                      //   if (addr.length > pd.length) {
                      //     const after = addr.substring(pd.length);
                      //     if (after.startsWith('市') || after.startsWith('县') || after.startsWith('区')
                      //       || after.startsWith('旗') || after.startsWith('自治旗') || after.startsWith('自治县')) {
                      //       // 明确按市县区搜索
                      //     } else {
                      //       area3 = distric;
                      //       area3c = city;
                      //       area3p = province;
                      //       len3 = pd.length;
                      //     }
                      //   }
                      // }
                    } else if (addr.startsWith(d)) {
                      const newlen = d.length;
                      if (newlen >= len1 && newlen >= len2 &&  newlen >= len3) {
                        area3 = distric;
                        area3c = city;
                        area3p = province;
                        len3 = d.length;
                      }
                      //   // 明确按市县区搜索
                      // if (d.endsWith('市') || d.endsWith('县') || d.endsWith('区') || d.endsWith('旗') || addr.length === d.length) {
                      //   area3 = distric;
                      //   area3c = city;
                      //   area3p = province;
                      //   len3 = d.length;
                      // }
                    }
                  });
                });
              });
            });
          }
        });
      }
    });

    let area = null;
    if (area3 && len3 >= len2 && len3 >= len1) {
      // console.log(area3p.label + '-' + area3c.label + '-' + area3.label);
      const address = addr.substr(len3);
      area = {districts: [area3p.value, area3c.value, area3.value], address};
    } else if (area2 && len2 >= len3 && len2 >= len1) {
      // console.log(area2p.label + '-' + area2.label);
      const address = addr.substr(len2);
      area = {districts: [area2p.value, area2.value], address};
    } else if (area1 && len1 >= len2 && len1 >= len3) {
      // console.log(area1.label);
      const address = addr.substr(len1);
      area = {districts: [area1.value], address};
    } else {
      area = {address: addr};
    }
    return area;
  }
}
