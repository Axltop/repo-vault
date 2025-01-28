package com.vault.backend.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReactController {
    @RequestMapping(value = {"/{path:[^\\.]*}", "/{path:^(?!error).*}"})
    public String redirect() {
        return "forward:/index.html";
    }
}
