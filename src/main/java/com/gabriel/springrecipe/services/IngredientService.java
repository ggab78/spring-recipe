package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.commands.IngredientCommand;
import com.gabriel.springrecipe.commands.RecipeCommand;
import com.gabriel.springrecipe.domain.Ingredient;
import com.gabriel.springrecipe.domain.Recipe;

public interface IngredientService {

    Ingredient findIngredientByRecipeIdAndId(Long recipeId, Long ingId);
    IngredientCommand findIngredientCommandByRecipeIdAndId(Long recipeId, Long ingId);
    IngredientCommand saveIngredientCommand(IngredientCommand command);
    void deleteIngredientByRecipeIdAndId(Long recipeId, Long ingId) throws Exception;
}

