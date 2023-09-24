package cn.evanzuo.admin.gateway.exception;

import cn.evanzuo.admin.gateway.api.CommonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局处理Oauth2抛出的异常
 *
 * @author EvanZuo[739221432@qq.com] 2023/09/24
 */
@RestControllerAdvice
public class Oauth2ExceptionHandler {

  @ExceptionHandler(value = RuntimeException.class)
  @ResponseBody
  public CommonResult<String> handleOauthDemo(Exception e) {
    return CommonResult.failed(e.getMessage());
  }
}
