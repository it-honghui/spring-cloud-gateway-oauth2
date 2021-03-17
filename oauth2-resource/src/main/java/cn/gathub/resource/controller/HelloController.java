package cn.gathub.resource.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@RestController
public class HelloController {

  @GetMapping("/hello")
  public String hello() {
    return "Hello World !";
  }

}
