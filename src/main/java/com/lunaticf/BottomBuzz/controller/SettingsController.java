package com.lunaticf.BottomBuzz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SettingsController {
    @RequestMapping("/settings")
    @ResponseBody
    public String settings() {
        return "settings: my set！！";
    }
}
