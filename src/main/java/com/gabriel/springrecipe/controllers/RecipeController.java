package com.gabriel.springrecipe.controllers;

import com.gabriel.springrecipe.commands.RecipeCommand;
import com.gabriel.springrecipe.services.RecipeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@AllArgsConstructor
public class RecipeController {

    private RecipeService recipeService;

    @RequestMapping("/recipe/{id}/show")
    public String getRecipeById(Model model, @PathVariable String id){

        model.addAttribute("recipe",recipeService.getRecipeById(Long.parseLong(id)));
        log.debug("getting recipe/{id}/show page");
        return "recipes/show";
    }

    @RequestMapping("/recipe/new")
    public String saveRecipe(Model model){
        model.addAttribute("newrecipe", new RecipeCommand());
        return "recipes/recipeform";
    }

    @RequestMapping("/recipe/{id}/update")
    public String updateRecipe(Model model, @PathVariable String id){
        model.addAttribute("newrecipe", recipeService.getRecipeCommandById(Long.parseLong(id)));
        log.debug("getting recipe/{id}/update page");
        return "recipes/recipeform";
    }

    @PostMapping("/post")
    public String saveOrUpdate(@ModelAttribute RecipeCommand command){
        RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);
        return "redirect:/recipe/"+ savedCommand.getId() +"/show";
    }
}
