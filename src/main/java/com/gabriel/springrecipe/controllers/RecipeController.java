package com.gabriel.springrecipe.controllers;

import com.gabriel.springrecipe.commands.RecipeCommand;
import com.gabriel.springrecipe.exceptions.NotFoundException;
import com.gabriel.springrecipe.services.RecipeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Slf4j
@Controller
@AllArgsConstructor
public class RecipeController {

    private RecipeService recipeService;

    @RequestMapping("/recipe/{id}/show")
    public String getRecipeById(Model model, @PathVariable String id){

        Long parsedID;
        try {
            parsedID = Long.parseLong(id);
        }catch(NumberFormatException e){
            //No need try_catch block since Long.parseLong(String s) throws NumberFormatException
            //using this gives opportunity to edit error msg
            throw new NumberFormatException("For input string: "+"\""+id+"\"");
        }

        model.addAttribute("recipe",recipeService.getRecipeById(parsedID));
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
    public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand command, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> {
                log.debug(objectError.toString());
            });
            return "recipes/recipeform";
        }

        RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);
        return "redirect:/recipe/"+ savedCommand.getId() +"/show";
    }

    @RequestMapping("/recipe/{id}/delete")
    public String deleteById(@PathVariable String id) throws Exception{
        recipeService.deleteById(Long.parseLong(id));
        return "redirect:/";
    }

}
