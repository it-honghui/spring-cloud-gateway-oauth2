package cn.gathub.gateway.authorization;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import cn.gathub.gateway.constant.AuthConstant;
import cn.gathub.gateway.constant.RedisConstant;
import cn.hutool.core.convert.Convert;
import reactor.core.publisher.Mono;

/**
 * 鉴权管理器，用于判断是否有资源的访问权限
 *
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
  private final RedisTemplate<String, Object> redisTemplate;

  public AuthorizationManager(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
    // 1、从Redis中获取当前路径可访问角色列表
    URI uri = authorizationContext.getExchange().getRequest().getURI();
    Object obj = redisTemplate.opsForHash().get(RedisConstant.RESOURCE_ROLES_MAP, uri.getPath());
    List<String> authorities = Convert.toList(String.class, obj);
    authorities = authorities.stream().map(i -> i = AuthConstant.AUTHORITY_PREFIX + i).collect(Collectors.toList());
    // 2、认证通过且角色匹配的用户可访问当前路径
    return mono
        .filter(Authentication::isAuthenticated)
        .flatMapIterable(Authentication::getAuthorities)
        .map(GrantedAuthority::getAuthority)
        .any(authorities::contains)
        .map(AuthorizationDecision::new)
        .defaultIfEmpty(new AuthorizationDecision(false));
  }

}
