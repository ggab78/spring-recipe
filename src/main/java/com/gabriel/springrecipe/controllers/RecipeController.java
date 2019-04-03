package com.gabriel.springrecipe.controllers;

import com.gabriel.springrecipe.repositories.RecipeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping({"/recipes"})
@AllArgsConstructor
public class RecipeController {

    private RecipeRepository recipeRepository;

    @RequestMapping({"","/","/index","/index.html"})
    public String getRecipeList(Model model){

        model.addAttribute("recipes",recipeRepository.findAll());
        log.debug("getting recipes/index page");

        return "recipes/index";
    }

}
