package cn.evanzuo.admin.business.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@EnableFeignClients(basePackages = "cn.evanzuo.admin.common.feign.client.clients")
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("cn.evanzuo.admin.demo.mapper")
public class BusinessDemoApp {

  public static void main(String[] args) {
    SpringApplication.run(BusinessDemoApp.class, args);
  }

}
