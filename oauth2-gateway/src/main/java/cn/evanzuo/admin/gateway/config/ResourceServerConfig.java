package cn.evanzuo.admin.gateway.config;


import cn.evanzuo.admin.gateway.authorization.AuthorizationManager;
import cn.evanzuo.admin.gateway.filter.redisTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import cn.evanzuo.admin.gateway.component.RestAuthenticationEntryPoint;
import cn.evanzuo.admin.gateway.component.RestfulAccessDeniedHandler;
import cn.evanzuo.admin.gateway.constant.AuthConstant;
import cn.evanzuo.admin.gateway.filter.IgnoreUrlsRemoveJwtFilter;
import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * 资源服务器配置
 *
 * @author EvanZuo[739221432@qq.com] 2023/09/24
 */
@AllArgsConstructor
@Configuration
@EnableWebFluxSecurity
public class ResourceServerConfig {
  private final AuthorizationManager authorizationManager;
  private final IgnoreUrlsConfig ignoreUrlsConfig;
  private final RestfulAccessDeniedHandler restfulAccessDeniedHandler;
  private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
  private final IgnoreUrlsRemoveJwtFilter ignoreUrlsRemoveJwtFilter;

  @Autowired
  private redisTokenFilter redisTokenFilters;
  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
    // 1、自定义处理JWT请求头过期或签名错误的结果
    // 需要判断jwt是否在redis中
    http.oauth2ResourceServer().authenticationEntryPoint(restAuthenticationEntryPoint);
    // 2、对白名单路径，直接移除JWT请求头
    // @xiangbo 移除JWT请求头有什么意义？是否移除了header之后就获取不到user的信息了。
    http.addFilterBefore(ignoreUrlsRemoveJwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);
    http.addFilterAfter(redisTokenFilters, SecurityWebFiltersOrder.AUTHENTICATION);
    http.authorizeExchange()
        .pathMatchers(ArrayUtil.toArray(ignoreUrlsConfig.getUrls(), String.class)).permitAll() // 白名单配置
        .anyExchange().access(authorizationManager) // 鉴权管理器配置
        .and().exceptionHandling()
        .accessDeniedHandler(restfulAccessDeniedHandler) // 处理未授权
        .authenticationEntryPoint(restAuthenticationEntryPoint) // 处理未认证
        .and().csrf().disable();
    return http.build();
  }

  @Bean
  public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    jwtGrantedAuthoritiesConverter.setAuthorityPrefix(AuthConstant.AUTHORITY_PREFIX);
    jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(AuthConstant.AUTHORITY_CLAIM_NAME);
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
    return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
  }

}
