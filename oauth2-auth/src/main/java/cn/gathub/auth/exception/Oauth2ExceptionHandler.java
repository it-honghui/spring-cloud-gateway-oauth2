package cn.gathub.auth.exception;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.gathub.auth.api.CommonResult;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局处理Oauth2抛出的异常
 *
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@RestControllerAdvice
public class Oauth2ExceptionHandler {

  @ExceptionHandler(value = Exception.class)
  @ResponseBody
  public CommonResult<String> handleOauthDemo(Exception e) {
    return CommonResult.failed(e.getMessage());
  }
}
