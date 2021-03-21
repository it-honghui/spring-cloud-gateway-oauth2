package cn.gathub.auth.service.principal;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

import cn.gathub.auth.domain.entity.User;
import lombok.Data;

/**
 * 登录用户信息
 *
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@Data
public class UserPrincipal implements UserDetails {

  /**
   * ID
   */
  private Long id;
  /**
   * 用户名
   */
  private String username;
  /**
   * 用户密码
   */
  private String password;
  /**
   * 用户状态
   */
  private Boolean enabled;
  /**
   * 权限数据
   */
  private Collection<SimpleGrantedAuthority> authorities;

  public UserPrincipal(User user) {
    this.setId(user.getId());
    this.setUsername(user.getUsername());
    this.setPassword(user.getPassword());
    this.setEnabled(user.getStatus() == 1);
    if (user.getRoles() != null) {
      authorities = new ArrayList<>();
      user.getRoles().forEach(item -> authorities.add(new SimpleGrantedAuthority(item)));
    }
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }

}
