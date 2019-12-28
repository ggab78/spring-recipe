package com.gabriel.springrecipe.repositories;

import com.gabriel.springrecipe.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe,String> {

}
