package cn.gathub.auth.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Builder(toBuilder = true)
public class Client {
  private String clientId;
  private String resourceIds;
  private Boolean secretRequire;
  private String clientSecret;
  private Boolean scopeRequire;
  private String scope;
  private String authorizedGrantTypes;
  private String webServerRedirectUri;
  private String authorities;
  private Integer accessTokenValidity;
  private Integer refreshTokenValidity;
}
