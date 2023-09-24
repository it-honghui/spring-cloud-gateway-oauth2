package cn.evanzuo.admin.gateway.component;

import cn.evanzuo.admin.gateway.api.CommonResult;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.nio.charset.StandardCharsets;

import cn.hutool.json.JSONUtil;
import reactor.core.publisher.Mono;

/**
 * 自定义返回结果：没有权限访问时
 *
 * @author EvanZuo[739221432@qq.com] 2023/09/24
 */
@Component
public class RestfulAccessDeniedHandler implements ServerAccessDeniedHandler {
  @Override
  public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(HttpStatus.FORBIDDEN);
    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    String body = JSONUtil.toJsonStr(CommonResult.forbidden(denied.getMessage()));
    DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
    return response.writeWith(Mono.just(buffer));
  }
}
