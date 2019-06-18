package com.lunaticf.BottomBuzz.controller;

import com.lunaticf.BottomBuzz.service.UserService;
import com.lunaticf.BottomBuzz.utils.HelpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;


    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String register(Model model, @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam(value = "rember", defaultValue = "0") int rember,
                           HttpServletResponse httpServletResponse) {
        try {
            Map<String, Object> map = userService.reg(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rember > 0) {
                    cookie.setMaxAge(3600*24*5);
                }
                httpServletResponse.addCookie(cookie);
                return HelpUtils.getJSONString(0, "注册成功");
            } else {
                return HelpUtils.getJSONString(1, map);
            }

        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            return HelpUtils.getJSONString(1, "注册异常");
        }
    }

    @RequestMapping({"login"})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rember", defaultValue = "0") int rember,
                        HttpServletResponse httpServletResponse) {
        try {
            Map<String, Object> map = userService.login(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rember > 0) {
                    cookie.setMaxAge(3600*24*5);
                }
                httpServletResponse.addCookie(cookie);
                return HelpUtils.getJSONString(0, "注册成功");
            } else {
                return HelpUtils.getJSONString(1, map);
            }

        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            return HelpUtils.getJSONString(1, "注册异常");
        }
    }

    @RequestMapping(value = "/logout/", method = {RequestMethod.GET})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }

}
