package com.example.demo;

import com.example.demo.config.HelloService;
import com.example.demo.config.UserConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {
    @Value("${user.userName}")
    private String userName;
    @Value("${user.userAge}")
    private Integer userAge;
    @Autowired
    UserConfig userConfig;
    @Autowired
    HelloService helloService;


    @RequestMapping("/")
    public String index() {
        return "hello demo";
    }
    @RequestMapping("/getUserInfo")
    public String getInfo(){
        return "userName:"+userName+"-"+"userAge:"+userAge;
    }

    @RequestMapping("/getUserInfo_2")
    public String getInfo_2(){
        return "userName:"+userConfig.getUserName()+"-"+"userAge:"+userConfig.getUserAge();
    }

    @RequestMapping("/getHelloMsg")
    public String getHelloMsg(){
        return helloService.getMsg();
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}

