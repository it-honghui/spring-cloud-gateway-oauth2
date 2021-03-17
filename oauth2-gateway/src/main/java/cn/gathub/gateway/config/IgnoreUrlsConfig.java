package cn.gathub.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 网关白名单配置
 *
 * @author Honghui [wanghonghui_work@163.com] 2021/3/16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Component
@ConfigurationProperties(prefix = "secure.ignore")
public class IgnoreUrlsConfig {
  private List<String> urls;
}
