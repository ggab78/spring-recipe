package com.gabriel.springrecipe.controllers;

import com.gabriel.springrecipe.commands.IngredientCommand;
import com.gabriel.springrecipe.commands.RecipeCommand;
import com.gabriel.springrecipe.commands.UnitOfMeasureCommand;
import com.gabriel.springrecipe.services.IngredientService;
import com.gabriel.springrecipe.services.RecipeService;
import com.gabriel.springrecipe.services.UnitOfMeasureService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@Slf4j
public class IngredientController {

    RecipeService recipeService;
    IngredientService ingredientService;
    UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    private WebDataBinder webDataBinder;
    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        this.webDataBinder=webDataBinder;
    }

    @RequestMapping("/recipe/{id}/ingredients")
    public String listIngredients(Model model, @PathVariable String id) {
        model.addAttribute("recipe", recipeService.getRecipeCommandById(id));
        return "recipes/ingredients/list";
    }

    @RequestMapping("/recipe/{recipeId}/ingredients/{ingId}/show")
    public String showIngredient(Model model, @PathVariable String recipeId, @PathVariable String ingId) {

        model.addAttribute("ingredient", ingredientService.findIngredientCommandByRecipeIdAndId(recipeId, ingId));

        return "recipes/ingredients/show";
    }

    @RequestMapping("/recipe/{recipeId}/ingredients/{ingId}/update")
    public String updateIngredient(Model model, @PathVariable String recipeId, @PathVariable String ingId) {

        model.addAttribute("ingredient", ingredientService.findIngredientCommandByRecipeIdAndId(recipeId, ingId).block());
        model.addAttribute("uomList", unitOfMeasureService.findAllUomCommand());
        return "recipes/ingredients/ingredientform";
    }

    @RequestMapping("/recipe/{recipeId}/ingredients/new")
    public String newIngredient(Model model, @PathVariable String recipeId) {

        //make sure that we have recipe
        //RecipeCommand recipeCommand = recipeService.getRecipeCommandById(recipeId).block();
        //todo throw exception if recipeCommand is not found

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);
        ingredientCommand.setUom(new UnitOfMeasureCommand());

        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", unitOfMeasureService.findAllUomCommand());
        return "recipes/ingredients/ingredientform";
    }

    @PostMapping("/recipe/{recipeId}/postingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command, Model model) {

        webDataBinder.validate();
        BindingResult bindingResult = webDataBinder.getBindingResult();

        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> {
                log.debug(objectError.toString());
            });
            model.addAttribute("uomList", unitOfMeasureService.findAllUomCommand());
            return "recipes/ingredients/ingredientform";
        }
        
        IngredientCommand ingredientCommand = ingredientService.saveIngredientCommand(command).block();
        return "redirect:/recipe/" + ingredientCommand.getRecipeId() + "/ingredients/" + ingredientCommand.getId() + "/show";
    }

    @RequestMapping("/recipe/{recipeId}/ingredients/{ingId}/delete")
    public String deleteIngredient(@PathVariable String recipeId, @PathVariable String ingId) {
        try {
            ingredientService.deleteIngredientByRecipeIdAndId(recipeId, ingId);
        } catch (Exception e) {
            e.getMessage();
        }
        return "redirect:/recipe/" + recipeId + "/ingredients";
    }
}
