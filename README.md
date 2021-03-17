# spring-cloud-gateway-oauth2

## 前言
我们理想的微服务权限解决方案应该是这样的，认证服务负责认证，网关负责校验认证和鉴权，其他API服务负责处理自己的业务逻辑。安全相关的逻辑只存在于认证服务和网关服务中，其他服务只是单纯地提供服务而没有任何安全相关逻辑。
## 架构
通过认证服务(`oauth2-auth`)进行统一认证，然后通过网关（`oauth2-gateway`）来统一校验认证和鉴权。采用Nacos作为注册中心，Gateway作为网关，使用nimbus-jose-jwtJWT库操作JWT令牌。
- oauth2-auth：Oauth2认证服务，负责对登录用户进行认证，整合Spring Security Oauth2
- ouath2-gateway：网关服务，负责请求转发和鉴权功能，整合Spring Security Oauth2
- oauth2-resource：受保护的API服务，用户鉴权通过后可以访问该服务，不整合Spring Security Oauth2
## 具体实现
### 一、认证服务`oauth2-auth`
> 1、首先来搭建认证服务，它将作为Oauth2的认证服务使用，并且网关服务的鉴权功能也需要依赖它，在pom.xml中添加相关依赖，主要是Spring Security、Oauth2、JWT、Redis相关依赖
```java
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-oauth2</artifactId>
    </dependency>
    <dependency>
        <groupId>com.nimbusds</groupId>
        <artifactId>nimbus-jose-jwt</artifactId>
        <version>8.16</version>
    </dependency>
    <!-- redis -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
</dependencies>

```
> 2、在application.yml中添加相关配置，主要是Nacos和Redis相关配置
```yml
server:
  port: 9401
spring:
  profiles:
    active: dev
  application:
    name: oauth2-auth
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
  redis:
    database: 0
    port: 6379
    host: localhost
    password:
management:
  endpoints:
    web:
      exposure:
        include: "*"

```
> 3、使用keytool生成RSA证书jwt.jks，复制到resource目录下，在JDK的bin目录下使用如下命令即可
```shell
keytool -genkey -alias jwt -keyalg RSA -keystore jwt.jks
```
> 4、创建UserServiceImpl类实现Spring Security的UserDetailsService接口，用于加载用户信息
```Java
package cn.gathub.auth.service;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import cn.gathub.auth.constant.MessageConstant;
import cn.gathub.auth.domain.SecurityUser;
import cn.gathub.auth.domain.UserDTO;
import cn.hutool.core.collection.CollUtil;


/**
 * 用户管理业务类
 *
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@Service
public class UserServiceImpl implements UserDetailsService {

  private List<UserDTO> userList;
  private final PasswordEncoder passwordEncoder;

  public UserServiceImpl(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @PostConstruct
  public void initData() {
    String password = passwordEncoder.encode("123456");
    userList = new ArrayList<>();
    userList.add(new UserDTO(1L, "admin", password, 1, CollUtil.toList("ADMIN")));
    userList.add(new UserDTO(2L, "user", password, 1, CollUtil.toList("USER")));
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    List<UserDTO> findUserList = userList.stream().filter(item -> item.getUsername().equals(username)).collect(Collectors.toList());
    if (CollUtil.isEmpty(findUserList)) {
      throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
    }
    SecurityUser securityUser = new SecurityUser(findUserList.get(0));
    if (!securityUser.isEnabled()) {
      throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
    } else if (!securityUser.isAccountNonLocked()) {
      throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
    } else if (!securityUser.isAccountNonExpired()) {
      throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
    } else if (!securityUser.isCredentialsNonExpired()) {
      throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);
    }
    return securityUser;
  }

}

```
> 5、添加认证服务相关配置Oauth2ServerConfig，需要配置加载用户信息的服务UserServiceImpl及RSA的钥匙对KeyPair
```java
package cn.gathub.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

import cn.gathub.auth.component.JwtTokenEnhancer;
import cn.gathub.auth.service.UserServiceImpl;
import lombok.AllArgsConstructor;

/**
 * 认证服务器配置
 *
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@AllArgsConstructor
@Configuration
@EnableAuthorizationServer
public class Oauth2ServerConfig extends AuthorizationServerConfigurerAdapter {

  private final PasswordEncoder passwordEncoder;
  private final UserServiceImpl userDetailsService;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenEnhancer jwtTokenEnhancer;

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.inMemory()
        .withClient("client-app")
        .secret(passwordEncoder.encode("123456"))
        .scopes("all")
        .authorizedGrantTypes("password", "refresh_token")
        .accessTokenValiditySeconds(3600)
        .refreshTokenValiditySeconds(86400);
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
    TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
    List<TokenEnhancer> delegates = new ArrayList<>();
    delegates.add(jwtTokenEnhancer);
    delegates.add(accessTokenConverter());
    enhancerChain.setTokenEnhancers(delegates); //配置JWT的内容增强器
    endpoints.authenticationManager(authenticationManager)
        .userDetailsService(userDetailsService) //配置加载用户信息的服务
        .accessTokenConverter(accessTokenConverter())
        .tokenEnhancer(enhancerChain);
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) {
    security.allowFormAuthenticationForClients();
  }

  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
    jwtAccessTokenConverter.setKeyPair(keyPair());
    return jwtAccessTokenConverter;
  }

  @Bean
  public KeyPair keyPair() {
    // 从classpath下的证书中获取秘钥对
    KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "654321".toCharArray());
    return keyStoreKeyFactory.getKeyPair("jwt", "654321".toCharArray());
  }

}

```
> 6、如果你想往JWT中添加自定义信息的话，比如说登录用户的ID，可以自己实现TokenEnhancer接口
```java
package cn.gathub.auth.component;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import cn.gathub.auth.domain.SecurityUser;

/**
 * JWT内容增强器
 *
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@Component
public class JwtTokenEnhancer implements TokenEnhancer {
  @Override
  public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
    Map<String, Object> info = new HashMap<>();
    // 把用户ID设置到JWT中
    info.put("id", securityUser.getId());
    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
    return accessToken;
  }
}

```
> 7、由于我们的网关服务需要RSA的公钥来验证签名是否合法，所以认证服务需要有个接口把公钥暴露出来
```java
package cn.gathub.auth.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * 获取RSA公钥接口
 *
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@RestController
public class KeyPairController {

  private final KeyPair keyPair;

  public KeyPairController(KeyPair keyPair) {
    this.keyPair = keyPair;
  }

  @GetMapping("/rsa/publicKey")
  public Map<String, Object> getKey() {
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    RSAKey key = new RSAKey.Builder(publicKey).build();
    return new JWKSet(key).toJSONObject();
  }

}

```
> 9、还需要配置Spring Security，允许获取公钥接口的访问
```java
package cn.gathub.auth.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SpringSecurity配置
 *
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
        .antMatchers("/rsa/publicKey").permitAll()
        .anyRequest().authenticated();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}


```
> 10、创建一个资源服务ResourceServiceImpl，初始化的时候把资源与角色匹配关系缓存到Redis中，方便网关服务进行鉴权的时候获取
```java
package cn.gathub.auth.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import cn.gathub.auth.constant.RedisConstant;
import cn.hutool.core.collection.CollUtil;

/**
 * 资源与角色匹配关系管理业务类
 *
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@Service
public class ResourceServiceImpl {

  private final RedisTemplate<String, Object> redisTemplate;

  public ResourceServiceImpl(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @PostConstruct
  public void initData() {
    Map<String, List<String>> resourceRolesMap = new TreeMap<>();
    resourceRolesMap.put("/resource/hello", CollUtil.toList("ADMIN"));
    resourceRolesMap.put("/resource/user/currentUser", CollUtil.toList("ADMIN", "USER"));
    redisTemplate.opsForHash().putAll(RedisConstant.RESOURCE_ROLES_MAP, resourceRolesMap);
  }
}

```
### 二、网关服务`oauth2-gateway`
接下来搭建网关服务，它将作为Oauth2的资源服务、客户端服务使用，对访问微服务的请求进行统一的校验认证和鉴权操作
> 1、在pom.xml中添加相关依赖，主要是Gateway、Oauth2和JWT相关依赖
```java
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-config</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-oauth2-resource-server</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-oauth2-client</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-oauth2-jose</artifactId>
    </dependency>
    <dependency>
        <groupId>com.nimbusds</groupId>
        <artifactId>nimbus-jose-jwt</artifactId>
        <version>8.16</version>
    </dependency>
</dependencies>

```
> 2、在application.yml中添加相关配置，主要是路由规则的配置、Oauth2中RSA公钥的配置及路由白名单的配置
```yml
server:
  port: 9201
spring:
  profiles:
    active: dev
  application:
    name: oauth2-gateway
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
    gateway:
      routes: # 配置路由路径
        - id: oauth2-api-route
          uri: lb://oauth2-resource
          predicates:
            - Path=/resource/**
          filters:
            - StripPrefix=1
        - id: oauth2-auth-route
          uri: lb://oauth2-auth
          predicates:
            - Path=/auth/**
          filters:
            - StripPrefix=1
      discovery:
        locator:
          enabled: true # 开启从注册中心动态创建路由的功能
          lower-case-service-id: true # 使用小写服务名，默认是大写
  security:
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri: 'http://localhost:9401/rsa/publicKey' # 配置RSA的公钥访问地址
  redis:
    database: 0
    port: 6379
    host: localhost
    password:
secure:
  ignore:
    urls: # 配置白名单路径
      - "/actuator/**"
      - "/auth/oauth/token"

```
> 3、对网关服务进行配置安全配置，由于Gateway使用的是WebFlux，所以需要使用@EnableWebFluxSecurity注解开启
```java
package cn.gathub.gateway.config;


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

import cn.gathub.gateway.authorization.AuthorizationManager;
import cn.gathub.gateway.component.RestAuthenticationEntryPoint;
import cn.gathub.gateway.component.RestfulAccessDeniedHandler;
import cn.gathub.gateway.constant.AuthConstant;
import cn.gathub.gateway.filter.IgnoreUrlsRemoveJwtFilter;
import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * 资源服务器配置
 *
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
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

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
    // 1、自定义处理JWT请求头过期或签名错误的结果
    http.oauth2ResourceServer().authenticationEntryPoint(restAuthenticationEntryPoint);
    // 2、对白名单路径，直接移除JWT请求头
    http.addFilterBefore(ignoreUrlsRemoveJwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);
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

```
> 4、在WebFluxSecurity中自定义鉴权操作需要实现ReactiveAuthorizationManager接口
```java
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

```
> 5、这里我们还需要实现一个全局过滤器AuthGlobalFilter，当鉴权通过后将JWT令牌中的用户信息解析出来，然后存入请求的Header中，这样后续服务就不需要解析JWT令牌了，可以直接从请求的Header中获取到用户信息
```java
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

```
### 三、资源服务（API服务）`oauth2-resource`
最后我们搭建一个API服务，它不会集成和实现任何安全相关逻辑，全靠网关来保护它
> 1、在pom.xml中添加相关依赖，就添加了一个web依赖
```java
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```
> 2、在application.yml添加相关配置，很常规的配置
```yml
server:
  port: 9501
spring:
  profiles:
    active: dev
  application:
    name: oauth2-resource
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
management:
  endpoints:
    web:
      exposure:
        include: "*"

```
> 3、创建一个测试接口，网关验证通过即可访问
```java
package cn.gathub.resource.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@RestController
public class HelloController {

  @GetMapping("/hello")
  public String hello() {
    return "Hello World !";
  }

}

```
> 4、创建一个获取登录中的用户信息的接口，用于从请求的Header中直接获取登录用户信息
```java
package cn.gathub.resource.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import cn.gathub.resource.domain.UserDTO;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONObject;

/**
 * 获取登录用户信息接口
 *
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@RestController
@RequestMapping("/user")
public class UserController {

  @GetMapping("/currentUser")
  public UserDTO currentUser(HttpServletRequest request) {
    // 从Header中获取用户信息
    String userStr = request.getHeader("user");
    JSONObject userJsonObject = new JSONObject(userStr);
    UserDTO userDTO = new UserDTO();
    userDTO.setUsername(userJsonObject.getStr("user_name"));
    userDTO.setId(Convert.toLong(userJsonObject.get("id")));
    userDTO.setRoles(Convert.toList(String.class, userJsonObject.get("authorities")));
    return userDTO;

  }
}

```
## 功能演示
在此之前先启动我们的 Nacos 和 Redis 服务，然后依次启动`oauth2-auth`、`oauth2-gateway`及`oauth2-api`服务

我这里测试使用的 Docker 跑的单机版的 Nacos
```shell
docker pull nacos/nacos-server
docker run --env MODE=standalone --name nacos -d -p 8848:8848 nacos/nacos-server
```
> 1、使用密码模式获取JWT令牌，访问地址：http://localhost:9201/auth/oauth/token
![image](https://user-images.githubusercontent.com/35522446/111439039-e9322880-873f-11eb-87ef-2ba4f26869c2.png)
> 2、使用获取到的JWT令牌访问需要权限的接口，访问地址：http://localhost:9201/resource/hello
![image](https://user-images.githubusercontent.com/35522446/111439499-652c7080-8740-11eb-96da-c0da0baee5fa.png)
> 3、使用获取到的JWT令牌访问获取当前登录用户信息的接口，访问地址：http://localhost:9201/resource/user/currentUser
![image](https://user-images.githubusercontent.com/35522446/111439662-8f7e2e00-8740-11eb-9680-d2cfa47c6fae.png)
> 4、当token不存在时
![image](https://user-images.githubusercontent.com/35522446/111439903-c6ecda80-8740-11eb-9f7c-92cd69e21c1b.png)
> 5、当JWT令牌过期时，使用refresh_token获取新的JWT令牌，访问地址：http://localhost:9201/auth/oauth/token
![image](https://user-images.githubusercontent.com/35522446/111440641-817cdd00-8741-11eb-9273-2250fcd94736.png)
> 6、使用没有访问权限的`user`账号登录，访问接口时会返回如下信息，访问地址：http://localhost:9201/resource/hello
![image](https://user-images.githubusercontent.com/35522446/111440858-b9842000-8741-11eb-9f1b-c4b971635393.png)
## 项目源码地址
https://github.com/it-wwh/spring-cloud-gateway-oauth2
## 公众号
![image](https://user-images.githubusercontent.com/35522446/111441584-69f22400-8742-11eb-8ca6-617554f54605.png)






