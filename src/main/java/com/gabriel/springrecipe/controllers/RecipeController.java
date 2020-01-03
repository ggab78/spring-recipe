package com.gabriel.springrecipe.controllers;

import com.gabriel.springrecipe.commands.RecipeCommand;
import com.gabriel.springrecipe.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Mono;


@Slf4j
@Controller

public class RecipeController {

    private RecipeService recipeService;

    public RecipeController(RecipeService recipeService){
        this.recipeService=recipeService;
    }

    private WebDataBinder webDataBinder;
    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        this.webDataBinder=webDataBinder;
    }



    @RequestMapping("/recipe/{id}/show")
    public String getRecipeById(Model model, @PathVariable String id){

        model.addAttribute("recipe",recipeService.getRecipeById(id));
        log.debug("getting recipe/{id}/show page");
        return "recipes/show";
    }

    @GetMapping("/recipe/new")
    public String saveRecipe(Model model){
        model.addAttribute("recipe", new RecipeCommand());
        return "recipes/recipeform";
    }

    @GetMapping("/recipe/{id}/update")
    public String updateRecipe(Model model, @PathVariable String id){

        Mono<RecipeCommand> recipeCommandMono = recipeService.getRecipeCommandById(id);

        model.addAttribute("recipe", recipeCommandMono);
        log.debug("getting recipe/{id}/update page");
        return "recipes/recipeform";
    }

    @PostMapping("/post")
    public String saveOrUpdate(@ModelAttribute("recipe") RecipeCommand command){

        webDataBinder.validate();
        BindingResult bindingResult = webDataBinder.getBindingResult();

        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> {
                log.debug(objectError.toString());
            });
            return "recipes/recipeform";
        }



        RecipeCommand recipeCommand = recipeService.saveRecipeCommand(command).block();
        return "redirect:/recipe/"+ recipeCommand.getId() +"/show";
    }

    @RequestMapping("/recipe/{id}/delete")
    public String deleteById(@PathVariable String id) throws Exception{
        recipeService.deleteById(id);
        return "redirect:/";
    }
}
