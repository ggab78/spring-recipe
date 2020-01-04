package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.commands.IngredientCommand;
import com.gabriel.springrecipe.converters.IngredientCommandToIngredient;
import com.gabriel.springrecipe.converters.IngredientToIngredientCommand;
import com.gabriel.springrecipe.converters.UnitOfMeasureCommandToUnitOfMeasure;
import com.gabriel.springrecipe.domain.Ingredient;

import com.gabriel.springrecipe.domain.Recipe;
import com.gabriel.springrecipe.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final RecipeService recipeService;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
    private final UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure;

    @Override
    public Mono<Ingredient> findIngredientByRecipeIdAndId(String recipeId, String ingId) {

        return recipeService.getRecipeById(recipeId)
                .flatMapIterable(Recipe::getIngredients)
                .filter(ingredient -> ingredient.getId().equals(ingId))
                .single()
                .map(ingredient -> {
                    return ingredient;
                });
    }

    @Override
    @Transactional
    public Mono<IngredientCommand> findIngredientCommandByRecipeIdAndId(String recipeId, String ingId) {

        return findIngredientByRecipeIdAndId(recipeId, ingId)
               .map(ingredient -> {
                   IngredientCommand ingredientCommand = ingredientToIngredientCommand.convert(ingredient);
                   ingredientCommand.setRecipeId(recipeId);
                   return ingredientCommand;
               });
    }

    @Override
    public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command) {


//            return recipeService.getRecipeById(command.getRecipeId())
//                    .flatMapIterable(Recipe::getIngredients)
//                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
//                    .singleOrEmpty()
//                    .map(ingredient -> {
//                        ingredient.setDescription(command.getDescription());
//                        ingredient.setAmount(command.getAmount());
////                      ingredient.setUom(unitOfMeasureReactiveRepository.findById(command.getId()).block());
//                        ingredient.setUom(unitOfMeasureCommandToUnitOfMeasure.convert(command.getUom()));
//                        IngredientCommand ingredientCommand = ingredientToIngredientCommand.convert(ingredient);
//                        ingredientCommand.setRecipeId(command.getRecipeId());
//                        return ingredientCommand;
//                    });


         Recipe recipe = recipeService.getRecipeById(command.getRecipeId()).block();

        if (recipe == null) {

            //todo toss error if not found!
            log.error("Recipe not found for id: " + command.getRecipeId());
            return Mono.just(new IngredientCommand());
        } else {

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
                    .findFirst();

            if (ingredientOptional.isPresent()) {
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(command.getDescription());
                ingredientFound.setAmount(command.getAmount());
                ingredientFound.setUom(unitOfMeasureReactiveRepository
                        .findById(command.getUom().getId()).block());

                if (ingredientFound.getUom() == null) {
                    new RuntimeException("UOM NOT FOUND");
                }


            } else {
                //add new Ingredient
                Ingredient ingredient = ingredientCommandToIngredient.convert(command);
                recipe.addIngredient(ingredient);
            }

            Recipe savedRecipe = recipeService.saveRecipe(recipe).block();

            Optional<Ingredient> savedIngredientOptional = savedRecipe
                    .getIngredients()
                    .stream()
                    .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
                    .findFirst();

            //check by description
            if (!savedIngredientOptional.isPresent()) {
                //not totally safe... But best guess
                savedIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                        .filter(recipeIngredients -> recipeIngredients.getUom().getId().equals(command.getUom().getId()))
                        .findFirst();
            }

            //todo check for fail

            //enhance with id value
            IngredientCommand ingredientCommandSaved = ingredientToIngredientCommand.convert(savedIngredientOptional.get());
            ingredientCommandSaved.setRecipeId(recipe.getId());

            return Mono.just(ingredientCommandSaved);
        }

    }

    @Override
    public Mono<Void> deleteIngredientByRecipeIdAndId(String recipeId, String ingId) throws Exception {


//        recipeReactiveRepository
//                .findById(recipeId)
//                .flatMapIterable(Recipe::getIngredients)
//                .filter(ingredient -> ingredient.getId().equals(ingId))
//                .single()
//                .map(ingredient ->
//                    recipeReactiveRepository.findById(recipeId)
//                            .map(recipe -> {
//                                recipe.getIngredients().remove(ingredient);
//                                return recipeReactiveRepository.save(recipe);
//                            })
//                );


        Recipe recipe = recipeService.getRecipeById(recipeId).block();

        if (recipe != null) {

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(ingId))
                    .findFirst();

            if(ingredientOptional.isPresent()){
                recipe.getIngredients().remove(ingredientOptional.get());
                recipeService.saveRecipe(recipe);
            }
        } else {
            throw new Exception("Recipe with id = " + recipeId + " not found");
        }

        return Mono.empty();
    }
}
