package com.gabriel.springrecipe.controllers;

import com.gabriel.springrecipe.repositories.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/recipes"})
@AllArgsConstructor
public class RecipeController {

    private RecipeRepository recipeRepository;

    @RequestMapping({"","/","/index","/index.html"})
    public String getRecipyList(Model model){

        model.addAttribute("recipes",recipeRepository.findAll());

        return "recipes/index";
    }

}
