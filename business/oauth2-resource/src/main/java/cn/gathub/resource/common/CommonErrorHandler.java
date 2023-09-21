package cn.gathub.resource.common;

import cn.gathub.resource.api.CommonResult;
import cn.gathub.resource.api.IErrorCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonErrorHandler {

    // @ExceptionHandler用来设置处理哪个异常
    @ExceptionHandler({RuntimeException.class})
    public CommonResult passwordException(Exception e) {
        return CommonResult.failed(new IErrorCode() {
            @Override
            public long getCode() {
                return 1234;
            }

            @Override
            public String getMessage() {
                return e.getMessage();
            }
        }, e.getMessage());
    }

}
