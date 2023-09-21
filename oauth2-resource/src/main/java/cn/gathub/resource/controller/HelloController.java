package cn.gathub.resource.controller;

import cn.gathub.client.clients.IDemo3Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@RestController
public class HelloController {

  @Autowired
  IDemo3Client iDemo2Client;

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
    String res = iDemo2Client.getUserIntroduce();
    System.out.println(res);
    return "Hello World ! feign";
  }
}

