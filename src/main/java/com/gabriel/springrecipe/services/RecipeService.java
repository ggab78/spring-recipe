package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.domain.Recipe;
import java.util.Set;

public interface RecipeService {

    Set<Recipe> getRecipes();
}
