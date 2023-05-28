package cn.gathub.resource.controller;

import cn.gathub.resource.DTO.ProjectListDTO;
import cn.gathub.resource.DTO.ProjectListSave;
import cn.gathub.resource.Oauth2ResourceApplication;
import cn.gathub.resource.VO.FileVo;
import cn.gathub.resource.domain.ProjectDateFile;
import cn.gathub.resource.domain.User;
import cn.gathub.resource.service.imp.ProjectServiceDBImpl;
import cn.gathub.resource.service.imp.UserServiceDBImpl;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@RestController()
@RequestMapping("/project")
public class Project {

  @Autowired
  UserServiceDBImpl userService;

  @Autowired
  ProjectServiceDBImpl projectService;

  @PostMapping("/list")
  public List<FileVo> project(HttpServletRequest request, @RequestBody @Validated ProjectListDTO dto) {
    // 从Header中获取用户信息
    String userStr = request.getHeader("user");
    JSONObject userJsonObject = new JSONObject(userStr);

    Map<String, List<ProjectDateFile>> obj =  userService
            .getBaseMapper().getData(userJsonObject.getStr("id"), dto.getProjectId())
            .stream().collect(Collectors.groupingBy(ProjectDateFile::getDate));
    System.out.println(JSON.toJSONString(obj));
    List<FileVo> fileFormatList = new ArrayList();
    obj.forEach((key, value) -> {
      FileVo tem = new FileVo();
      tem.setDate(key);
      tem.setList(value);
      fileFormatList.add(tem);
    });
    return fileFormatList;
  }

  @PostMapping("/userProjectList")
  public List<Project> getUserProjectList(HttpServletRequest request) {
    // 从Header中获取用户信息
    String userStr = request.getHeader("user");
    JSONObject userJsonObject = new JSONObject(userStr);

    QueryWrapper queryWrapper = new QueryWrapper();
    queryWrapper.eq("user_id", userJsonObject.getStr("id"));
    List<Project> obj =  projectService.list(queryWrapper);
    System.out.println(JSON.toJSONString(obj));
    return obj;
  }

  @PostMapping("/save")
  public void saveProjectFileInfo(HttpServletRequest request, @RequestBody ProjectListSave dto) {
    // 从Header中获取用户信息
    String userStr = request.getHeader("user");
    JSONObject userJsonObject = new JSONObject(userStr);
    String userId =  userJsonObject.getStr("id");
    System.out.println(dto);
    System.out.println(userId);
  }
}

