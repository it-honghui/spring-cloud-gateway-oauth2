package cn.evanzuo.admin.common.feign.client.clients;

import cn.evanzuo.admin.common.feign.client.config.Feignconfigs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "javademo", configuration = Feignconfigs.class)
public interface IResourceClient {
    @RequestMapping(value = "/user/introduce", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
    Object getUserIntroduce();
}
