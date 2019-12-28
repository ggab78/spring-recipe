package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.commands.RecipeCommand;
import com.gabriel.springrecipe.domain.Recipe;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface RecipeService {

    Set<Recipe> getRecipes();

    Recipe getRecipeById(String id);

    RecipeCommand saveRecipeCommand(RecipeCommand command);

    Recipe saveRecipe(Recipe recipe);

    RecipeCommand getRecipeCommandById(String id);

    boolean deleteById(String id) throws Exception;
}
