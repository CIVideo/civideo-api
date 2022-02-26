package com.jsss.civideo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ExceptionTestController {

    @RequestMapping(value = "/exceptionTest", method = RequestMethod.GET)
    public void test() {
        throw new RuntimeException("test");
    }

}
