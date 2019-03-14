package com.gabriel.springrecipe.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping({"", "/", "index", "index.html"})
    public String getIndexPage(Model model) {
        String text = "milk is good and hot ...";
        model.addAttribute("recipe", text);
        return "index";
    }

}
