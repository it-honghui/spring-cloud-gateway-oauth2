package cn.gathub.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@EnableDiscoveryClient
@SpringBootApplication
public class Oauth2GatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(Oauth2GatewayApplication.class, args);
  }

}
