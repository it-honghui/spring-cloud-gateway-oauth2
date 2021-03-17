package cn.gathub.auth.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class UserDTO {
  private Long id;
  private String username;
  private String password;
  private Integer status;
  private List<String> roles;

}
