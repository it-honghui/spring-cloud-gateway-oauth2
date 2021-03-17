package cn.gathub.resource.domain;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserDTO {
  private Long id;
  private String username;
  private String password;
  private List<String> roles;
}
