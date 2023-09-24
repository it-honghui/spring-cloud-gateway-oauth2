package cn.evanzuo.admin.auth.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author EvanZuo[739221432@qq.com] 2023/09/24
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
