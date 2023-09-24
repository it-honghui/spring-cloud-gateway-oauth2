package cn.evanzuo.admin.auth.service.impl;

import cn.evanzuo.admin.auth.service.UserService;
import cn.evanzuo.admin.auth.utils.redis.RedisService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import cn.evanzuo.admin.auth.constant.MessageConstant;
import cn.evanzuo.admin.auth.domain.entity.User;

/**
 * 用户管理业务类
 *
 * @author EvanZuo[739221432@qq.com] 2023/09/24
 */
@Service
public class UserServiceImpl implements UserService {
  private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  private List<User> userList;
  private final PasswordEncoder passwordEncoder;
  @Autowired
  private UserServiceDBImpl userServiceDB;

  @Autowired
  RedisService redisService;

  public UserServiceImpl(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    LOGGER.info("loadUserByUsername:{}", username);
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
    // 之前的方式只能控制到/auth/token的方法 请求的时候，target是实时的权限控制
    // redis记录 user的信息，然后 gateway鉴权进行检验，实现实时的权限控制，当然这也必需要保证redis的高可用，因为redis已经成为核心流程
    redisService.set(username, "login", 60 * 10L);
    return userListDemo;
  }
}
