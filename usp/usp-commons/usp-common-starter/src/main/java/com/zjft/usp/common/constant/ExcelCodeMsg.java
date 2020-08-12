package com.zjft.usp.common.constant;

/**
 * TODO
 *
 * @author ljzhu
 * @version 1.0
 * @date 2020-02-28 10:26
 **/
public class ExcelCodeMsg {

    private String msg;

    //通用的错误码
    public static ExcelCodeMsg OVER_MAX_USER_IMPORT_LIMIT = new ExcelCodeMsg("一次最多导入%s条");
    public static ExcelCodeMsg IMPORT_FIELD_FORMAT_ERROR = new ExcelCodeMsg("第%s行%s格式错误");
    public static ExcelCodeMsg IMPORT_FIELD_FORMATDATE_ERROR = new ExcelCodeMsg("第%s行%s格式错误,正确格式为%s");
    public static ExcelCodeMsg IMPORT_FIELD_IS_EMPTY = new ExcelCodeMsg("第%s行%s不能为空");
    public static ExcelCodeMsg IMPORT_FIELD_IS_NOTEXIT = new ExcelCodeMsg("第%s行%s在系统中不存在");
    public static ExcelCodeMsg IMPORT_FIELD_LENGTH_LIMIT = new ExcelCodeMsg("第%s行%s最多%s个字符");
    public static ExcelCodeMsg IMPORT_DUPLICATE_FIELD_ERROR = new ExcelCodeMsg("第%s行%s重复");
    public static ExcelCodeMsg IMPORT_FIELD_ALREADY_EXISTS = new ExcelCodeMsg("第%s行%s已存在");
    public static ExcelCodeMsg IMPORT_FIELD_IS_NUMBER = new ExcelCodeMsg("第%s行%s必须是整数");
    public static ExcelCodeMsg IMPORT_FIELD_CHOOSE = new ExcelCodeMsg("第%s行%s错误,可选的值为:%s");



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
        return msg;
    }

}
