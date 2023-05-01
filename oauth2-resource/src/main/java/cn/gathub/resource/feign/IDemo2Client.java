package cn.gathub.resource.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "oauth2-auth")
public interface IDemo2Client {
    @RequestMapping(value = "/oauth/token", method = RequestMethod.POST)
    String getUserIntroduce();
}
