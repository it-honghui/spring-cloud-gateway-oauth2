package cn.evanzuo.admin.auth.controller;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;

import cn.evanzuo.admin.auth.api.CommonResult;
import cn.evanzuo.admin.auth.domain.dto.Oauth2TokenDto;

/**
 * 自定义Oauth2获取令牌接口
 *
 * @author EvanZuo[739221432@qq.com] 2023/09/24
 */
@RestController
@RequestMapping("/oauth")
public class AuthController {

  private final TokenEndpoint tokenEndpoint;

  public AuthController(TokenEndpoint tokenEndpoint) {
    this.tokenEndpoint = tokenEndpoint;
  }

  /**
   * Oauth2登录认证
   */
  @PostMapping("/token")
  public CommonResult<Oauth2TokenDto> postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
    OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
    Oauth2TokenDto oauth2TokenDto = Oauth2TokenDto.builder()
        .token(Objects.requireNonNull(oAuth2AccessToken).getValue())
        .refreshToken(oAuth2AccessToken.getRefreshToken().getValue())
        .expiresIn(oAuth2AccessToken.getExpiresIn())
        .tokenHead("Bearer ").build();
    return CommonResult.success(oauth2TokenDto);
  }

}
