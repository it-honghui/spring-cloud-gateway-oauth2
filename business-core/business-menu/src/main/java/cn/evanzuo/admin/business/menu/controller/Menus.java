package cn.evanzuo.admin.business.menu.controller;

import cn.evanzuo.admin.business.menu.VO.MenuVo;
import cn.evanzuo.admin.business.menu.domain.ProjectMenu;
import cn.evanzuo.admin.business.menu.service.imp.ProjectMenuDBImpl;
import cn.hutool.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/menu")
public class Menus {
  private final static Logger LOGGER = LoggerFactory.getLogger(Menus.class);

  @Autowired
  ProjectMenuDBImpl projectMenuDB;

  @PostMapping("/list")
  public MenuVo project(HttpServletRequest request) {
    // 从Header中获取用户信息
    String userStr = request.getHeader("user");
    JSONObject userJsonObject = new JSONObject(userStr);
    System.out.println((userJsonObject));

    List<String> authorities = (List<String>)userJsonObject.get("authorities");
    String authoritiesStr = authorities.stream()
            .map(item -> "`" + item + "`")
            .map(item -> item.replace("`", "'"))
            .collect(Collectors.joining(","));
    List<ProjectMenu> allMenus = projectMenuDB.getBaseMapper().getRoleNames(authoritiesStr);
    MenuVo menuVo = new MenuVo();
    List<ProjectMenu> projectMenus = allMenus.stream()
              .filter(item -> item.getParentCid() == 1)
            .peek(item -> item.setChildren(Menus.getChildren(item, allMenus)))
            .sorted(Comparator.comparingInt(ProjectMenu::getSort).reversed())
              .collect(Collectors.toList());
    menuVo.setList(projectMenus);
    LOGGER.info(String.valueOf(projectMenus.size()));
    menuVo.setTotal(projectMenus.size());
    return menuVo;
  }

  public static List<ProjectMenu> getChildren(ProjectMenu root, List<ProjectMenu> allMenus) {
    return allMenus.stream()
           .filter(item -> Objects.equals(item.getParentCid(), root.getCatId()))
           .peek(item -> item.setChildren(Menus.getChildren(item, allMenus)))
            .sorted(Comparator.comparingInt(ProjectMenu::getSort).reversed())
            .collect(Collectors.toList());
  }
}

