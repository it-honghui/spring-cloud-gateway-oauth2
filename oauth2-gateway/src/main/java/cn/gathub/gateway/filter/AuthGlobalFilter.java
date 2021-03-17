package cn.gathub.gateway.filter;

import com.nimbusds.jose.JWSObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.text.ParseException;

import cn.hutool.core.util.StrUtil;
import reactor.core.publisher.Mono;

/**
 * 将登录用户的JWT转化成用户信息的全局过滤器
 *
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

  private final static Logger LOGGER = LoggerFactory.getLogger(AuthGlobalFilter.class);

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String token = exchange.getRequest().getHeaders().getFirst("Authorization");
    if (StrUtil.isEmpty(token)) {
      return chain.filter(exchange);
    }
    try {
      // 从token中解析用户信息并设置到Header中去
      String realToken = token.replace("Bearer ", "");
      JWSObject jwsObject = JWSObject.parse(realToken);
      String userStr = jwsObject.getPayload().toString();
      LOGGER.info("AuthGlobalFilter.filter() user:{}", userStr);
      ServerHttpRequest request = exchange.getRequest().mutate().header("user", userStr).build();
      exchange = exchange.mutate().request(request).build();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return chain.filter(exchange);
  }

  @Override
  public int getOrder() {
    return 0;
  }
}
