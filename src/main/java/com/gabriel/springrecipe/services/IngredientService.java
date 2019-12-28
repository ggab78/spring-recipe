package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.commands.IngredientCommand;
import com.gabriel.springrecipe.commands.RecipeCommand;
import com.gabriel.springrecipe.domain.Ingredient;
import com.gabriel.springrecipe.domain.Recipe;

public interface IngredientService {

    Ingredient findIngredientByRecipeIdAndId(String recipeId, String ingId);
    IngredientCommand findIngredientCommandByRecipeIdAndId(String recipeId, String ingId);
    IngredientCommand saveIngredientCommand(IngredientCommand command);
    void deleteIngredientByRecipeIdAndId(String recipeId, String ingId) throws Exception;
}

