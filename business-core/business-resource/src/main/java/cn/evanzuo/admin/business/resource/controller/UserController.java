package cn.evanzuo.admin.business.resource.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import cn.evanzuo.admin.business.resource.domain.User;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONObject;

/**
 * 获取登录用户信息接口
 *
 * @author EvanZuo[739221432@qq.com] 2023/09/24
 */
@RestController
@RequestMapping("/user")
public class UserController {

  @GetMapping("/currentUser")
  public User currentUser(HttpServletRequest request) {
    // 从Header中获取用户信息
    String userStr = request.getHeader("user");
    JSONObject userJsonObject = new JSONObject(userStr);
    return User.builder()
        .username(userJsonObject.getStr("user_name"))
        .id(Convert.toLong(userJsonObject.get("id")))
        .roles(Convert.toList(String.class, userJsonObject.get("authorities"))).build();
  }

  @GetMapping
  public JSONObject findUser(HttpServletRequest request) {
    // 从Header中获取用户信息
    String userStr = request.getHeader("user");
    return new JSONObject(userStr);
  }
}
