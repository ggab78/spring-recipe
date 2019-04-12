package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.commands.RecipeCommand;
import com.gabriel.springrecipe.domain.Recipe;
import java.util.Set;

public interface RecipeService {

    Set<Recipe> getRecipes();

    Recipe getRecipeById(Long id);

    RecipeCommand saveRecipeCommand(RecipeCommand command);
}
