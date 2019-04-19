package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.commands.IngredientCommand;
import com.gabriel.springrecipe.converters.IngredientCommandToIngredient;
import com.gabriel.springrecipe.converters.IngredientToIngredientCommand;
import com.gabriel.springrecipe.converters.UnitOfMeasureCommandToUnitOfMeasure;
import com.gabriel.springrecipe.domain.Ingredient;

import com.gabriel.springrecipe.domain.Recipe;
import com.gabriel.springrecipe.repositories.IngredientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final RecipeService recipeService;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure;


    @Override
    public Ingredient findIngredientByRecipeIdAndId(Long recipeId, Long ingId) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        if(recipe.getIngredients().size() == 0){
            throw new RuntimeException("Recipe doesn't contain ingredients");
        }
        Optional<Ingredient> ingredientOptional = ingredientRepository.findByRecipeAndId(recipe, ingId);
        if(!ingredientOptional.isPresent()){
            throw new RuntimeException("Ingredient with id = " + ingId +" doesn't exist");
        }
        return ingredientOptional.get();
    }

    @Override
    @Transactional
    public IngredientCommand findIngredientCommandByRecipeIdAndId(Long recipeId, Long ingId) {
        return ingredientToIngredientCommand.convert(findIngredientByRecipeIdAndId(recipeId, ingId));
    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {

        Recipe recipe = recipeService.getRecipeById(command.getRecipeId());

        Optional<Ingredient> ingredientOptional = recipe
                .getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(command.getId()))
                .findFirst();
        if(ingredientOptional.isPresent()){
            Ingredient foundIngredient = ingredientOptional.get();
            foundIngredient.setDescription(command.getDescription());
            foundIngredient.setAmount(command.getAmount());
            foundIngredient.setUom(unitOfMeasureCommandToUnitOfMeasure.convert(command.getUom()));
        }else{
            recipe.getIngredients().add(ingredientCommandToIngredient.convert(command));
        }

        Recipe savedRecipe = recipeService.saveRecipe(recipe);

        return ingredientToIngredientCommand.convert(
                savedRecipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(command.getId()))
                .findFirst().get());
    }
}
