package cn.gathub.bussinessDemo.controller;

import cn.gathub.client.clients.IDemo3Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@RestController
public class HelloController {
  private final static Logger LOGGER = LoggerFactory.getLogger(HelloController.class);
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
  public String feign(HttpServletRequest request) {

    // 从Header中获取用户信息
    String userStr = request.getHeader("user");
    String res = iDemo3Client.getUserIntroduce(userStr);
    LOGGER.info("/feign res:{}", res);
    return "Hello World ! feign";
  }
}

