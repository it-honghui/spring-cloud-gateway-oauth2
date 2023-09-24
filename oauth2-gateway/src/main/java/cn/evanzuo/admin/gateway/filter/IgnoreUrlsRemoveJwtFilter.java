package cn.evanzuo.admin.gateway.filter;

import cn.evanzuo.admin.gateway.config.IgnoreUrlsConfig;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import java.net.URI;
import java.util.List;

import reactor.core.publisher.Mono;

/**
 * 白名单路径访问时需要移除JWT请求头
 *
 * @author EvanZuo[739221432@qq.com] 2023/09/24
 */
@Component
public class IgnoreUrlsRemoveJwtFilter implements WebFilter {

  private final IgnoreUrlsConfig ignoreUrlsConfig;

  public IgnoreUrlsRemoveJwtFilter(IgnoreUrlsConfig ignoreUrlsConfig) {
    this.ignoreUrlsConfig = ignoreUrlsConfig;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();
    URI uri = request.getURI();
    PathMatcher pathMatcher = new AntPathMatcher();
    // 白名单路径移除JWT请求头
    List<String> ignoreUrls = ignoreUrlsConfig.getUrls();
    for (String ignoreUrl : ignoreUrls) {
      if (pathMatcher.match(ignoreUrl, uri.getPath())) {
        request = exchange.getRequest().mutate().header("Authorization", "").build();
        exchange = exchange.mutate().request(request).build();
        return chain.filter(exchange);
      }
    }
    return chain.filter(exchange);
  }

}
