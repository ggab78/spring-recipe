package com.gabriel.springrecipe.repositories;

import com.gabriel.springrecipe.domain.Ingredient;
import com.gabriel.springrecipe.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IngredientRepository extends CrudRepository<Ingredient, String> {

    //Optional<Ingredient> findByRecipeAndId(Recipe recipe, String id);

}
