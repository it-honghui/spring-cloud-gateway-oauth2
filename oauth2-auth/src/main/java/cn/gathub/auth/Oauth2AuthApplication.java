package cn.gathub.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@EnableDiscoveryClient
@SpringBootApplication
public class Oauth2AuthApplication {

  public static void main(String[] args) {
    SpringApplication.run(Oauth2AuthApplication.class, args);
  }

}
