package com.gabriel.springrecipe.controllers;

import com.gabriel.springrecipe.commands.RecipeCommand;
import com.gabriel.springrecipe.services.RecipeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@AllArgsConstructor
public class RecipeController {

    private RecipeService recipeService;

    @RequestMapping("/recipe/show/{id}")
    public String getRecipeById(Model model, @PathVariable String id){

        model.addAttribute("recipe",recipeService.getRecipeById(Long.parseLong(id)));
        log.debug("getting recipe/show/{id} page");

        return "recipes/show";
    }

    @RequestMapping("/recipe/new")
    public String newRecipe(Model model){
        model.addAttribute("newrecipe", new RecipeCommand());
        return "recipes/recipeform";
    }

    @PostMapping
    @RequestMapping(name="new_recipe")
    public String saveOrUpdate(@ModelAttribute RecipeCommand command){

        RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);

        return "redirect:/recipe/show/" + savedCommand.getId();
    }
}
