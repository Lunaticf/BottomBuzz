package com.lunaticf.BottomBuzz.controller;

import com.lunaticf.BottomBuzz.model.HostHolder;
import com.lunaticf.BottomBuzz.model.News;
import com.lunaticf.BottomBuzz.model.ViewObject;
import com.lunaticf.BottomBuzz.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = {"/", "/home"}, method = {RequestMethod.GET, RequestMethod.POST})
        public String index(Model model, @RequestParam(value = "pop", defaultValue = "0") int pop) {
            model.addAttribute("vos", newsService.getLatestNews(0, 0, 30));
            if (hostHolder.getUser() != null) {
                pop = 0;
            }
            model.addAttribute("pop", pop);
            return "home";
    }

    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId, @RequestParam(value = "pop", defaultValue = "0") int pop) {
        model.addAttribute("vos", newsService.getLatestNews(userId, 0, 10));
        if (hostHolder.getUser() != null) {
            pop = 0;
        }
        model.addAttribute("pop", pop);
        return "home";
    }
}
