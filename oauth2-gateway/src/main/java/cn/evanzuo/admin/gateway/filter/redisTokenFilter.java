package cn.evanzuo.admin.gateway.filter;

import cn.evanzuo.admin.gateway.api.CommonResult;
import cn.evanzuo.admin.gateway.config.IgnoreUrlsConfig;
import cn.evanzuo.admin.gateway.utils.redis.RedisService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nimbusds.jose.JWSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;

/**
 * 白名单路径访问时需要移除JWT请求头
 *
 * @author EvanZuo[739221432@qq.com] 2023/09/24
 */
@Component
public class redisTokenFilter implements WebFilter {
  @Autowired
  RedisService redisService;
  private final static Logger LOGGER = LoggerFactory.getLogger(redisTokenFilter.class);

  private final IgnoreUrlsConfig ignoreUrlsConfig;

  public redisTokenFilter(IgnoreUrlsConfig ignoreUrlsConfig) {
    this.ignoreUrlsConfig = ignoreUrlsConfig;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String token = exchange.getRequest().getHeaders().getFirst("Authorization");
    if (StrUtil.isEmpty(token)) {
      return chain.filter(exchange);
    }
    try {
      // 从token中解析用户信息并设置到Header中去
      String realToken = token.replace("Bearer ", "");
      JWSObject jwsObject = JWSObject.parse(realToken);
      String userStr = jwsObject.getPayload().toString();
      LOGGER.info("AuthGlobalFilter.tokenFilter() user:{}", userStr);
      JSONObject userJsonObject = new JSONObject(userStr);
      String username = (String) userJsonObject.get("user_name");
      LOGGER.info("AuthGlobalFilter.tokenFilter() username:{}", username);
      String userLogin = (String) redisService.get(username);
      LOGGER.error(userLogin);
      if (userLogin == null) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        String body = JSONUtil.toJsonStr(CommonResult.unauthorized(null));
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
      }
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    return chain.filter(exchange);
  };
}