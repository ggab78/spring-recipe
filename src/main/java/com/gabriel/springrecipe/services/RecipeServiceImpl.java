package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.commands.RecipeCommand;
import com.gabriel.springrecipe.converters.RecipeCommandToRecipe;
import com.gabriel.springrecipe.converters.RecipeToRecipeCommand;
import com.gabriel.springrecipe.domain.Recipe;
import com.gabriel.springrecipe.repositories.reactive.RecipeReactiveRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
@Service
@AllArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeReactiveRepository recipeReactiveRepository;
    private final RecipeToRecipeCommand recipeToRecipeCommand;
    private final RecipeCommandToRecipe recipeCommandToRecipe;

    @Override
    public Flux<Recipe> getRecipes() {

        log.debug("I'am in the service");
        return recipeReactiveRepository.findAll();

    }

    @Override
    public Mono<Recipe> getRecipeById(String id) {

        return recipeReactiveRepository.findById(id);
    }

    @Override
    public Mono<RecipeCommand> getRecipeCommandById(String id) {
        return getRecipeById(id).map(recipe -> {
            RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipe);
            recipeCommand.getIngredients().forEach(ingredientCommand -> {
                ingredientCommand.setRecipeId(recipeCommand.getId());
            });
            return recipeCommand;
        });
    }

    @Override
    public Mono<Recipe> saveRecipe(Recipe recipe) {
        return recipeReactiveRepository.save(recipe);
    }

    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command) {
        Recipe detachedRecipe = recipeCommandToRecipe.convert(command);

        return recipeReactiveRepository.save(detachedRecipe).map(recipeToRecipeCommand::convert);
    }

    @Override
    public Mono<Void> deleteById(String id) throws Exception {
        recipeReactiveRepository.deleteById(id).block();
        return Mono.empty();
    }
}
