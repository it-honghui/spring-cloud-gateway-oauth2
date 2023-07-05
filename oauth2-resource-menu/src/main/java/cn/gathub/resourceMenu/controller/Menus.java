package cn.gathub.resourceMenu.controller;

import cn.gathub.resourceMenu.DTO.ProjectListDTO;
import cn.gathub.resourceMenu.VO.MenuVo;
import cn.gathub.resourceMenu.domain.ProjectMenu;
import cn.gathub.resourceMenu.service.imp.ProjectMenuDBImpl;
import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController()
@RequestMapping("/menu")
public class Menus {

  @Autowired
  ProjectMenuDBImpl userService;

  @Autowired
  ProjectMenuDBImpl projectMenuDB;

  @PostMapping("/list")
  public List<ProjectMenu> project(HttpServletRequest request) {
    // 从Header中获取用户信息
    String userStr = request.getHeader("user");
    JSONObject userJsonObject = new JSONObject(userStr);
    System.out.println((userJsonObject));

    List<ProjectMenu> menus = projectMenuDB.list(null);
    return menus;
  }
}

