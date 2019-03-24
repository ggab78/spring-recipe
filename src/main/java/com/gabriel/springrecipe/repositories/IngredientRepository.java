package com.gabriel.springrecipe.repositories;

import com.gabriel.springrecipe.domain.Ingredient;
import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository extends CrudRepository<Ingredient, Long> {

}
