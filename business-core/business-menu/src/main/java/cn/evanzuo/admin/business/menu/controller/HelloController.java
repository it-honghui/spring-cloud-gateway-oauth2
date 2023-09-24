package cn.evanzuo.admin.business.menu.controller;

import cn.evanzuo.admin.common.feign.client.clients.IDemo3Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@RestController
public class HelloController {

  @Autowired
  IDemo3Client iDemo3Client;

  @GetMapping("/hello")
  public String hello() {
    return "Hello World !";
  }

  @GetMapping("/demo")
  public String demo() {
    return "Hello World ! demo";
  }

  @GetMapping("/feign")
  public String feign() {
//    String res = iDemo3Client.getUserIntroduce();
//    System.out.println(res);
    return "Hello World ! feign";
  }
}

