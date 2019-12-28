package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.commands.IngredientCommand;
import com.gabriel.springrecipe.converters.IngredientCommandToIngredient;
import com.gabriel.springrecipe.converters.IngredientToIngredientCommand;
import com.gabriel.springrecipe.converters.UnitOfMeasureCommandToUnitOfMeasure;
import com.gabriel.springrecipe.domain.Ingredient;

import com.gabriel.springrecipe.domain.Recipe;
import com.gabriel.springrecipe.repositories.IngredientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final RecipeService recipeService;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure;

    @Override
    public Ingredient findIngredientByRecipeIdAndId(String recipeId, String ingId) {

        Recipe recipe = recipeService.getRecipeById(recipeId);
        if(recipe.getIngredients().size() == 0){
            throw new RuntimeException("Recipe doesn't contain ingredients");
        }

        for(Ingredient ingredient:recipe.getIngredients()){
            if(ingredient.getId().equals(ingId)){
                return ingredient;

            }
        }
        throw new RuntimeException("Ingredient Id not found");
    }

    @Override
    @Transactional
    public IngredientCommand findIngredientCommandByRecipeIdAndId(String recipeId, String ingId) {
        IngredientCommand ingredientCommand = ingredientToIngredientCommand.convert(findIngredientByRecipeIdAndId(recipeId, ingId));
        ingredientCommand.setRecipeId(recipeId);
        return ingredientCommand;
    }

    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {

        Recipe recipe = recipeService.getRecipeById(command.getRecipeId());

        Ingredient saveIngredient;

        Optional<Ingredient> ingredientOptional = recipe
                .getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(command.getId()))
                .findFirst();
        if(ingredientOptional.isPresent()){
            saveIngredient = ingredientOptional.get();
            saveIngredient.setDescription(command.getDescription());
            saveIngredient.setAmount(command.getAmount());
            saveIngredient.setUom(unitOfMeasureCommandToUnitOfMeasure.convert(command.getUom()));
        }else{
            saveIngredient = ingredientCommandToIngredient.convert(command);
            recipe.addIngredient(saveIngredient);
        }

        ingredientRepository.save(saveIngredient);
        recipeService.saveRecipe(recipe);

        IngredientCommand savedCommand = ingredientToIngredientCommand.convert(saveIngredient);
        savedCommand.setRecipeId(recipe.getId());

        log.debug("Recipe Id in Command "+savedCommand.getRecipeId());

        return savedCommand;
    }

    @Override
    public void deleteIngredientByRecipeIdAndId(String recipeId, String ingId) throws Exception{

        Recipe recipe = recipeService.getRecipeById(recipeId);
        if(recipe !=null){
            if(recipe.getIngredients().size()>0){
                recipe.getIngredients().forEach(ingredient -> {
                    if(ingredient.getId().equals(ingId)){
//                        ingredient.setRecipe(null);
                        ingredientRepository.delete(ingredient);
                        recipeService.saveRecipe(recipe);
                    }
                });
            }else{
                throw new Exception("Recipe doesn't contain ingredients");
            }
        }else{
            throw new Exception("Recipe with id = "+recipeId+" not found");
        }
    }
}
