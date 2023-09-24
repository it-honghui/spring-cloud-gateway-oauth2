package cn.evanzuo.admin.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 网关白名单配置
 *
 * @author EvanZuo[739221432@qq.com] 2023/09/24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Component
@ConfigurationProperties(prefix = "secure.ignore")
public class IgnoreUrlsConfig {
  private List<String> urls;
}
