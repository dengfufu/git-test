package com.zjft.usp.anyfix.work.request.enums;

/**
 * 状态码，错误码
 * @author ljzhu
 */
public class ExcelCodeMsg {

    private String msg;

    //通用的错误码
    public static ExcelCodeMsg FILE_NULL = new ExcelCodeMsg("文件有误");
    public static ExcelCodeMsg FILE_FORMAT = new ExcelCodeMsg("文件格式有误,请按照模板导入");
    public static ExcelCodeMsg FILE_IS_EAMPTY = new ExcelCodeMsg("文件为空");
    public static ExcelCodeMsg OVER_MAX_USER_IMPORT_LIMIT = new ExcelCodeMsg("一次最多导入%s条");

    public static ExcelCodeMsg IMPORT_FIELD_FORMAT_ERROR = new ExcelCodeMsg("第%s行%s格式错误");
    public static ExcelCodeMsg IMPORT_FIELD_FORMATDATE_ERROR = new ExcelCodeMsg("第%s行%s格式错误,正确格式为%s");
    public static ExcelCodeMsg IMPORT_FIELD_IS_EAMPTY = new ExcelCodeMsg("第%s行%s不能为空%s");
    public static ExcelCodeMsg IMPORT_FIELD_IS_NOTEXIT = new ExcelCodeMsg("第%s行%s在系统中不存在");
    public static ExcelCodeMsg IMPORT_FIELD_LENGTH_LIMIT = new ExcelCodeMsg("第%s行%s最多%s个字符");

    public static ExcelCodeMsg IMPORT_FIELD_NOT_MATCH_CUSTOMER = new ExcelCodeMsg("第%s行%s不匹配客户");
    public static ExcelCodeMsg IMPORT_FIELD_DEMANDER = new ExcelCodeMsg("委托商数据错误,一次只能导入同一个委托商的数据,请保持委托商名称准确");
    public static ExcelCodeMsg IMPORT_FIELD_NOT_MATCH_DEMANDER = new ExcelCodeMsg("第%s行%s不匹配委托商");

    public static ExcelCodeMsg IMPORT_FIELD_IS_NUMBER = new ExcelCodeMsg("第%s行%s必须是整数");
    public static ExcelCodeMsg IMPORT_FIELD_IS_POSITIVE_NUMBER = new ExcelCodeMsg("第%s行%s必须是大于等于0的数字");
    public static ExcelCodeMsg IMPORT_FIELD_LOWER_THEN = new ExcelCodeMsg("第%s行%s个数必须小于等于设备数量");
    public static ExcelCodeMsg IMPORT_FIELD_CHOOSE = new ExcelCodeMsg("第%s行%s错误,可选的值为:%s");

    public static ExcelCodeMsg IMPORT_FIELD_OTHER_IS_NOTEXIT = new ExcelCodeMsg("第%s行%s已经填写,设备类型和设备品牌至少填写一个否则无法匹配");

    public static ExcelCodeMsg  IMPORT_FIELD_TIME_BEGIN = new ExcelCodeMsg("第%s行%s");

    public static ExcelCodeMsg IMPORT_DUPLICATE_FIELD_ERROR = new ExcelCodeMsg("第%s行相同设备重复建单");


    private ExcelCodeMsg() {
    }

    private ExcelCodeMsg(String msg) {
        this.msg = msg;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ExcelCodeMsg fillArgs(Object... args) {
        String message = String.format(this.msg, args);
        return new ExcelCodeMsg(message);
    }

    @Override
    public String toString() {
        return msg + ";<br/>";
    }


}
