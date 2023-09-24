package cn.evanzuo.admin.gateway.authorization;


import cn.evanzuo.admin.gateway.service.imp.UserServiceDBImpl;
import cn.evanzuo.admin.gateway.utils.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import cn.evanzuo.admin.gateway.constant.AuthConstant;
import cn.hutool.core.convert.Convert;
import reactor.core.publisher.Mono;

/**
 * 鉴权管理器，用于判断是否有资源的访问权限
 *
 * @author EvanZuo[739221432@qq.com] 2023/09/24
 */
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
  private final static Logger LOGGER = LoggerFactory.getLogger(AuthorizationManager.class);
  private final RedisTemplate<String, Object> redisTemplate;
  public AuthorizationManager(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Autowired
  UserServiceDBImpl userServiceDB;

  @Autowired
  RedisService redisService;

  @Override
  public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
    // xiangbo 当前用户？？？？？？
    // 1、从Redis中获取当前路径可访问角色列表
    URI uri = authorizationContext.getExchange().getRequest().getURI();
    Object authoritiesFromMysql = userServiceDB.getBaseMapper().getData(uri.getPath());
    System.out.println(uri.getPath());
    LOGGER.info("current path:{}", uri.getPath());
    LOGGER.info("authoritiesFromMysql:{}", authoritiesFromMysql);
    List<String> authorities = Convert.toList(String.class, authoritiesFromMysql);
    authorities = authorities.stream().map(i -> i = AuthConstant.AUTHORITY_PREFIX + i).collect(Collectors.toList());
    LOGGER.info(authorities.toString());
    // 2、认证通过且角色匹配的用户可访问当前路径

//      .any(roleId -> {
//          // roleId是请求用户的角色(格式:ROLE_{roleId})，authorities是请求资源所需要角色的集合
//          log.info("访问路径：{}", path);
//          log.info("用户角色roleId：{}", roleId);
//          log.info("资源需要权限authorities：{}", authorities);
//          return authorities.contains(roleId);
//      })

      return mono
        .filter(Authentication::isAuthenticated)
        .flatMapIterable(Authentication::getAuthorities)
        .map(GrantedAuthority::getAuthority)
        .any(authorities::contains)
        .map(AuthorizationDecision::new)
        .defaultIfEmpty(new AuthorizationDecision(false));
  }
}
