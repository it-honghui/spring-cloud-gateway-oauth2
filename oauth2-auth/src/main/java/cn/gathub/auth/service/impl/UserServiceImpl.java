package cn.gathub.auth.service.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import cn.gathub.auth.constant.MessageConstant;
import cn.gathub.auth.domain.entity.User;
import cn.gathub.auth.service.UserService;
import cn.hutool.core.collection.CollUtil;

/**
 * 用户管理业务类
 *
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@Service
public class UserServiceImpl implements UserService {
  private List<User> userList;
  private final PasswordEncoder passwordEncoder;
  @Autowired
  private UserServiceDBImpl userServiceDB;

  public UserServiceImpl(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    System.out.println(username);
    User userListDemo = userServiceDB.getBaseMapper().getUserInfo(username);
    if (userListDemo == null) {
      System.out.println("no user");
    } else {
      System.out.println("userListDemo");
      System.out.println(userListDemo);
    }
    try {
      List<String> roleList = userServiceDB.getBaseMapper().getData(username);
      System.out.println("Role list");
      System.out.println(roleList);
      userListDemo.setRoles(roleList);
    } catch (Exception e) {
      System.out.println(e);
    }
    System.out.println("result !!!");
    System.out.println(JSON.toJSONString(userListDemo));
    // BD 获取 user  => role
//    List<User> findUserList = userList.stream().filter(item -> item.getUsername().equals(username)).collect(Collectors.toList());
//    System.out.println("findUserList");
//    System.out.println(JSON.toJSONString(findUserList));
    if (userListDemo == null) {
      throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
    }
    System.out.println(userListDemo);
    System.out.println(userListDemo.isEnabled());
    if (!userListDemo.isEnabled()) {
      throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
    } else if (!userListDemo.isAccountNonLocked()) {
      throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
    } else if (!userListDemo.isAccountNonExpired()) {
      throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
    } else if (!userListDemo.isCredentialsNonExpired()) {
      throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);
    }
    return userListDemo;
  }
}
