package cn.gathub.resource.controller;

import cn.gathub.resource.service.imp.UserServiceDBImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@RestController()
@RequestMapping("/project")
public class Project {

  @Autowired
  UserServiceDBImpl userService;
  @GetMapping("/list")
  public Object project() {
    return userService.getBaseMapper().getData("1", "1");
  }
}

