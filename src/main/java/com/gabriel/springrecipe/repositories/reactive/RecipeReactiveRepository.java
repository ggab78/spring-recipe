package com.gabriel.springrecipe.repositories.reactive;

import com.gabriel.springrecipe.domain.Recipe;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface RecipeReactiveRepository extends ReactiveMongoRepository<Recipe, String> {


}
