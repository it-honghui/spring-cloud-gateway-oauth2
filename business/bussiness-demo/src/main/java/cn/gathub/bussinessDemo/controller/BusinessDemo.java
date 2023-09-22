package cn.gathub.bussinessDemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class BusinessDemo {
    private final static Logger LOGGER = LoggerFactory.getLogger(BusinessDemo.class);
    @GetMapping("/business-demo")
    public String hello(HttpServletRequest request) {
        String userStr = request.getHeader("user");
        LOGGER.info("user:{}", userStr);
        return "business-demo!" + userStr;
    }
}
