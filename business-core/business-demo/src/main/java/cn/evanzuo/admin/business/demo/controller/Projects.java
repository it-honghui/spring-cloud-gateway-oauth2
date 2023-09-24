package cn.evanzuo.admin.business.demo.controller;

import cn.evanzuo.admin.business.demo.DTO.ProjectDTO;
import cn.evanzuo.admin.business.demo.DTO.ProjectListDTO;
import cn.evanzuo.admin.business.demo.DTO.ProjectListSave;
import cn.evanzuo.admin.business.demo.VO.FileVo;
import cn.evanzuo.admin.business.demo.domain.Project;
import cn.evanzuo.admin.business.demo.domain.ProjectDate;
import cn.evanzuo.admin.business.demo.domain.ProjectDateFile;
import cn.evanzuo.admin.business.demo.service.imp.ProjectDateFileServiceDBImpl;
import cn.evanzuo.admin.business.demo.service.imp.ProjectDateServiceDBImpl;
import cn.evanzuo.admin.business.demo.service.imp.ProjectServiceDBImpl;
import cn.evanzuo.admin.business.demo.service.imp.UserServiceDBImpl;
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
import java.util.stream.Collectors;

/**
 * @author EvanZuo[739221432@qq.com] 2023/09/24
 */
@RestController()
@RequestMapping("/project")
public class Projects {

  @Autowired
  UserServiceDBImpl userService;

  @Autowired
  ProjectServiceDBImpl projectService;

  @Autowired
  ProjectDateServiceDBImpl projectDateServiceDB;

  @Autowired
  ProjectDateFileServiceDBImpl projectDateFileServiceDB;

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
    // project date 表
    // 如果不存在就建立或者update
    // 判断是否存在
    QueryWrapper queryWrapper = new QueryWrapper();
    queryWrapper.eq("project_id", dto.getProjectId());
    List<ProjectDate> projectDateList =  projectDateServiceDB.list(queryWrapper);
    ProjectDate projectDate = new ProjectDate();
    projectDate.setDate(dto.getDate());
    projectDate.setProjectId(dto.getProjectId());
    if (projectDateList != null) {
      projectDateList.stream().forEach(item -> {
        if (item.getDate().equals(projectDate.getDate())) {
          projectDate.setId(item.getId());
        }
      });
    }
    projectDateServiceDB.saveOrUpdate(projectDate);
    if (projectDate.getId() == null) {
      QueryWrapper queryWrapper2 = new QueryWrapper();
      queryWrapper2.eq("date", dto.getDate());
      queryWrapper2.eq("project_id", dto.getProjectId());
      ProjectDate searchProjectDate = projectDateServiceDB.getOne(queryWrapper2);
      projectDate.setId(searchProjectDate.getId());
    } else {
      // 先删除 project file 表格中的所有
      QueryWrapper queryWrapper1 = new QueryWrapper();
      queryWrapper1.eq("project_date_id", projectDate.getId());
      projectDateFileServiceDB.remove(queryWrapper1);
    }
    System.out.println(projectDate.getId());
    // project file 表格插入
    dto.getList().stream().forEach(item -> {
      item.setProjectDateId(projectDate.getId());
    });
    System.out.println(JSON.toJSONString(dto.getList()));
    projectDateFileServiceDB.saveBatch(dto.getList());
  }


  @PostMapping("/saveProject")
  public List<Project> saveProject(HttpServletRequest request, @RequestBody ProjectDTO p) {
    // 从Header中获取用户信息
    String userStr = request.getHeader("user");
    JSONObject userJsonObject = new JSONObject(userStr);
    String userId =  userJsonObject.getStr("id");
    QueryWrapper queryWrapper = new QueryWrapper();
    queryWrapper.eq("user_id", userId);
    queryWrapper.eq("project_name", p.getProjectName());
    List<Project> p1List = projectService.list(queryWrapper);
    if (p1List!= null && p1List.size() == 0) {
      Project project = new Project();
      project.setProjectName(p.getProjectName());
      project.setUserId(userId);
      projectService.save(project);

      QueryWrapper queryWrapper2 = new QueryWrapper();
      queryWrapper2.eq("user_id", userId);
      return projectService.list(queryWrapper2);
    }
    throw new RuntimeException("123");
  }
}

