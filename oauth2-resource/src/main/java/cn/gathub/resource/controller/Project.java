package cn.gathub.resource.controller;

import cn.gathub.resource.DTO.ProjectListDTO;
import cn.gathub.resource.Oauth2ResourceApplication;
import cn.gathub.resource.VO.FileVo;
import cn.gathub.resource.domain.ProjectDateFile;
import cn.gathub.resource.domain.User;
import cn.gathub.resource.service.imp.UserServiceDBImpl;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@RestController()
@RequestMapping("/project")
public class Project {

  @Autowired
  UserServiceDBImpl userService;
  @GetMapping("/list")
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
}

