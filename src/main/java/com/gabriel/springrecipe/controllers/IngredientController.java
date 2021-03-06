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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@AllArgsConstructor
public class IngredientController {

    RecipeService recipeService;
    IngredientService ingredientService;
    UnitOfMeasureService unitOfMeasureService;

    @RequestMapping("/recipe/{id}/ingredients")
    public String listIngredients(Model model, @PathVariable String id) {
        model.addAttribute("recipe", recipeService.getRecipeCommandById(Long.parseLong(id)));
        return "recipes/ingredients/list";
    }

    @RequestMapping("/recipe/{recipeId}/ingredients/{ingId}/show")
    public String showIngredient(Model model, @PathVariable String recipeId, @PathVariable String ingId){

        IngredientCommand ingredientCommand
                = ingredientService.findIngredientCommandByRecipeIdAndId(Long.parseLong(recipeId),Long.parseLong(ingId));
        model.addAttribute("ingredient", ingredientCommand);

        return "recipes/ingredients/show";
    }

    @RequestMapping("/recipe/{recipeId}/ingredients/{ingId}/update")
    public String updateIngredient(Model model, @PathVariable String recipeId, @PathVariable String ingId){
        IngredientCommand ingredientCommand
                = ingredientService.findIngredientCommandByRecipeIdAndId(Long.parseLong(recipeId),Long.parseLong(ingId));
        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", unitOfMeasureService.findAllUomCommand());
        return "recipes/ingredients/ingredientform";
    }
    @RequestMapping("/recipe/{recipeId}/ingredients/new")
    public String newIngredient(Model model, @PathVariable String recipeId){

        //make sure that we have recipe
        RecipeCommand recipeCommand = recipeService.getRecipeCommandById(Long.parseLong(recipeId));
        //todo throw exception if recipeCommand is not found

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(Long.parseLong(recipeId));
        ingredientCommand.setUom(new UnitOfMeasureCommand());

        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", unitOfMeasureService.findAllUomCommand());
        return "recipes/ingredients/ingredientform";
    }

    @PostMapping("/recipe/{recipeId}/postingredient")
    public String saveOrUpdate (@ModelAttribute IngredientCommand command){
        IngredientCommand ingredientCommand = ingredientService.saveIngredientCommand(command);
        return "redirect:/recipe/"+ingredientCommand.getRecipeId()+"/ingredients/"+ingredientCommand.getId()+"/show";
    }

    @RequestMapping("/recipe/{recipeId}/ingredients/{ingId}/delete")
    public String deleteIngredient(@PathVariable String recipeId, @PathVariable String ingId){
        try{
            ingredientService.deleteIngredientByRecipeIdAndId(Long.parseLong(recipeId),Long.parseLong(ingId));
        }catch (Exception e){
            e.getMessage();
        }
        return "redirect:/recipe/"+recipeId+"/ingredients";
    }
}
