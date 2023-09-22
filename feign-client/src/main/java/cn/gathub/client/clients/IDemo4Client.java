package cn.gathub.client.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "business-demo")
public interface IDemo4Client {
    @RequestMapping(
            value = "/business-demo",
            method = RequestMethod.POST
    )
    String getUserIntroduce(@RequestHeader("user") String headers);
}
