package cn.gathub.resource.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import cn.gathub.resource.domain.UserDTO;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONObject;

/**
 * 获取登录用户信息接口
 *
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@RestController
@RequestMapping("/user")
public class UserController {

  @GetMapping("/currentUser")
  public UserDTO currentUser(HttpServletRequest request) {
    // 从Header中获取用户信息
    String userStr = request.getHeader("user");
    JSONObject userJsonObject = new JSONObject(userStr);
    UserDTO userDTO = new UserDTO();
    userDTO.setUsername(userJsonObject.getStr("user_name"));
    userDTO.setId(Convert.toLong(userJsonObject.get("id")));
    userDTO.setRoles(Convert.toList(String.class, userJsonObject.get("authorities")));
    return userDTO;

  }

  @GetMapping
  public JSONObject findUser(HttpServletRequest request) {
    String userStr = request.getHeader("user");
    return new JSONObject(userStr);
  }
}
