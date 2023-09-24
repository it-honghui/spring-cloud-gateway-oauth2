package cn.evanzuo.admin.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
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

import cn.evanzuo.admin.auth.component.JwtTokenEnhancer;
import cn.evanzuo.admin.auth.service.ClientService;
import cn.evanzuo.admin.auth.service.UserService;
import lombok.AllArgsConstructor;

/**
 * 认证服务器配置
 *
 * @author EvanZuo[739221432@qq.com] 2023/09/24
 */
@AllArgsConstructor
@Configuration
@EnableAuthorizationServer
public class Oauth2ServerConfig extends AuthorizationServerConfigurerAdapter {

  private final UserService userService;
  private final ClientService clientService;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenEnhancer jwtTokenEnhancer;

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//    clients.inMemory()
//        // 1、密码模式
//        .withClient("client-app")
//        .secret(passwordEncoder.encode("123456"))
//        .scopes("read,write")
//        .authorizedGrantTypes("password", "refresh_token")
//        .accessTokenValiditySeconds(3600)
//        .refreshTokenValiditySeconds(86400)
//        .and()
//        // 2、授权码授权
//        .withClient("client-app-2")
//        .secret(passwordEncoder.encode("123456"))
//        .scopes("read")
//        .authorizedGrantTypes("authorization_code", "refresh_token")
//        .accessTokenValiditySeconds(3600)
//        .refreshTokenValiditySeconds(86400)
//        .redirectUris("https://www.gathub.cn", "https://www.baidu.com");
    clients.withClientDetails(clientService);
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
    TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
    List<TokenEnhancer> delegates = new ArrayList<>();
    delegates.add(jwtTokenEnhancer);
    delegates.add(accessTokenConverter());
    enhancerChain.setTokenEnhancers(delegates); //配置JWT的内容增强器
    endpoints.authenticationManager(authenticationManager)
        .userDetailsService(userService) //配置加载用户信息的服务
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
