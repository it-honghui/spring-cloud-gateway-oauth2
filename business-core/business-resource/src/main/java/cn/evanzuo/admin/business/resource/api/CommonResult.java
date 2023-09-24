package cn.evanzuo.admin.business.resource.api;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 通用返回对象
 *
 * @author EvanZuo[739221432@qq.com] 2023/09/24
 */
@Data
public class CommonResult<T> {
  private String timestamp;
  private long code;
  private String message;
  private T data;

  protected CommonResult() {
  }

  protected CommonResult(long code, String message, T data) {
    this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    this.code = code;
    this.message = message;
    this.data = data;
  }

  /**
   * 成功返回结果
   *
   * @param data 获取的数据
   */
  public static <T> CommonResult<T> success(T data) {
    return new CommonResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
  }

  /**
   * 成功返回结果
   *
   * @param data    获取的数据
   * @param message 提示信息
   */
  public static <T> CommonResult<T> success(T data, String message) {
    return new CommonResult<T>(ResultCode.SUCCESS.getCode(), message, data);
  }

  /**
   * 失败返回结果
   *
   * @param errorCode 错误码
   */
  public static <T> CommonResult<T> failed(IErrorCode errorCode) {
    return new CommonResult<T>(errorCode.getCode(), errorCode.getMessage(), null);
  }

  /**
   * 失败返回结果
   *
   * @param errorCode 错误码
   * @param message   错误信息
   */
  public static <T> CommonResult<T> failed(IErrorCode errorCode, String message) {
    return new CommonResult<T>(errorCode.getCode(), message, null);
  }

  /**
   * 失败返回结果
   *
   * @param message 提示信息
   */
  public static <T> CommonResult<T> failed(String message) {
    return new CommonResult<T>(ResultCode.FAILED.getCode(), message, null);
  }

  /**
   * 失败返回结果
   */
  public static <T> CommonResult<T> failed() {
    return failed(ResultCode.FAILED);
  }

  /**
   * 参数验证失败返回结果
   */
  public static <T> CommonResult<T> validateFailed() {
    return failed(ResultCode.VALIDATE_FAILED);
  }

  /**
   * 参数验证失败返回结果
   *
   * @param message 提示信息
   */
  public static <T> CommonResult<T> validateFailed(String message) {
    return new CommonResult<T>(ResultCode.VALIDATE_FAILED.getCode(), message, null);
  }

  /**
   * 未登录返回结果
   */
  public static <T> CommonResult<T> unauthorized(T data) {
    return new CommonResult<T>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage(), data);
  }

  /**
   * 未授权返回结果
   */
  public static <T> CommonResult<T> forbidden(T data) {
    return new CommonResult<T>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage(), data);
  }

  public long getCode() {
    return code;
  }

  public void setCode(long code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
