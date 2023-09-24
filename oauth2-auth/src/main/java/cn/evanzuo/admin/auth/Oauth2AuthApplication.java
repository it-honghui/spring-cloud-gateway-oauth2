package cn.evanzuo.admin.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("cn.evanzuo.admin.auth.mapper")
@EnableCaching(proxyTargetClass = true)
public class Oauth2AuthApplication {

  public static void main(String[] args) {
    SpringApplication.run(Oauth2AuthApplication.class, args);
  }

}
