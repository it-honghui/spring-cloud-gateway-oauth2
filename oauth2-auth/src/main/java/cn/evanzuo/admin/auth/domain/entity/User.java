package cn.evanzuo.admin.auth.domain.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotEmpty;

/**
 * @author EvanZuo[739221432@qq.com] 2023/09/24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class User implements UserDetails {
  private Long id;
  private String username;
  @NotEmpty(message = "密码不能为空")
  private String password;

  @NotEmpty(message = "密码错误")
  String mobile;
  String pwd;
  // 全局配置
//    @TableLogic(value = "0", delval = "1")
  Integer deleted;
  @TableField(select = false)
  Integer notExistField;
  @Version
  Integer version;
  String avatarUrl;
  private boolean enabled = true;
  public boolean credentialsNonExpired = true;
  public boolean accountNonExpired = true;
  public boolean accountNonLocked = true;
  @TableField(exist = false)
  private List<String> roles;
  @TableField(exist = false)
  List<Menu> menuList;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List authorities = new ArrayList<>();
    this.roles.forEach(item -> authorities.add(new SimpleGrantedAuthority(item)));
    return authorities;
  }
}
