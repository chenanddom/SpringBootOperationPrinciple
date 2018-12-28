package com.example.demo.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: chendom
 * Date: 2018-12-28
 * Time: 8:35
 */
public class HelloService {
private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
