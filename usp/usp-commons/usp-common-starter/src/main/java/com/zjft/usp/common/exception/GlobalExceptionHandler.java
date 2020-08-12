package com.zjft.usp.common.exception;

import com.zjft.usp.common.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result<Object> exceptionHandler(Exception e) {
        String msg = "";
        //如果是业务逻辑异常，返回具体的错误码与提示信息
        if (e instanceof AppException) {
            AppException appException = (AppException) e;
            msg = appException.getMessage();
            try {
                // 得到异常棧的首个元素
                StackTraceElement stackTraceElement = e.getStackTrace()[0];

                if (stackTraceElement != null) {
                    log.error("系统错误：类名：{}，方法：{}，异常信息：{}", stackTraceElement.getClassName(),
                            stackTraceElement.getMethodName() + "---" + stackTraceElement.getLineNumber(),
                            appException.getMessage());
                } else {
                    log.error("系统错误：" + appException.getMessage());
                }
            } catch (Exception e1) {
                log.error("系统异常：{}", e1.getMessage());
            }
        } else {
            //对系统级异常进行日志记录
            msg = "系统出现异常";
            log.error("系统异常：{}", e.getMessage(), e);
        }
        return Result.failed(msg);
    }
}
