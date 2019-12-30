package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.commands.RecipeCommand;
import com.gabriel.springrecipe.domain.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface RecipeService {

    Flux<Recipe> getRecipes();

    Mono<Recipe> getRecipeById(String id);

    Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command);

    Mono<Recipe> saveRecipe(Recipe recipe);

    Mono<RecipeCommand> getRecipeCommandById(String id);

    Mono<Void> deleteById(String id) throws Exception;
}
