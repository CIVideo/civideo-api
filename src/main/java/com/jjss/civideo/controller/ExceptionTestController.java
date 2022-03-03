package com.jjss.civideo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ExceptionTestController {

    @RequestMapping(value = "/exceptionTest", method = RequestMethod.GET)
    public void exceptionTest() {
        throw new RuntimeException("test");
    }


    @RequestMapping(value = "/exceptionTest2", method = RequestMethod.GET)
    public void test() {
        throw new ("test");
    }

}
