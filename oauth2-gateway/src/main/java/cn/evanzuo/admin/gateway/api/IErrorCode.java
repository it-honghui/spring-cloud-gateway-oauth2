package cn.evanzuo.admin.gateway.api;

/**
 * 封装API的错误码
 *
 * @author EvanZuo[739221432@qq.com] 2023/09/24
 */
public interface IErrorCode {
  long getCode();

  String getMessage();
}
