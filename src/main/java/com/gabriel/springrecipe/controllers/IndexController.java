package com.gabriel.springrecipe.controllers;

import com.gabriel.springrecipe.services.RecipeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@Slf4j
@AllArgsConstructor
public class IndexController {

    private RecipeService recipeService;

    @RequestMapping({"/index","/index.html"})
    public String getRecipeList(Model model){

        model.addAttribute("recipes",recipeService.getRecipes());
        log.debug("getting index page");

        return "recipes/index";
    }
}
