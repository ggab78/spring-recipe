package com.gabriel.springrecipe.controllers;

import com.gabriel.springrecipe.services.RecipeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
//@RequestMapping({"/recipes"})
@AllArgsConstructor
public class RecipeController {

    private RecipeService recipeService;

    @RequestMapping({"/recipes/index","/recipes/index.html"})
    public String getRecipeList(Model model){

        model.addAttribute("recipes",recipeService.getRecipes());
        log.debug("getting recipes/index page");

        return "recipes/index";
    }

    @RequestMapping({"/recipe/show/{id}",})
    public String getRecipeById(Model model, @PathVariable String id){

        model.addAttribute("recipeById",recipeService.getRecipeById(Long.parseLong(id)));
        log.debug("getting recipes/show page");

        return "recipes/show";
    }
}
