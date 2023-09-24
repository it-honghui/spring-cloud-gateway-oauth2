package cn.evanzuo.admin.common.feign.client.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "business-menu")
public interface IDemo3Client {
    @RequestMapping(
            value = "/menu/list",
            method = RequestMethod.POST
    )
    String getUserIntroduce(@RequestHeader("user") String headers);
}
