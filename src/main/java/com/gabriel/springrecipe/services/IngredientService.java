package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.commands.IngredientCommand;
import com.gabriel.springrecipe.domain.Ingredient;
import reactor.core.publisher.Mono;


public interface IngredientService {

    Mono<Ingredient> findIngredientByRecipeIdAndId(String recipeId, String ingId);
    Mono<IngredientCommand> findIngredientCommandByRecipeIdAndId(String recipeId, String ingId);
    Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command);
    Mono<Void> deleteIngredientByRecipeIdAndId(String recipeId, String ingId) throws Exception;
}

