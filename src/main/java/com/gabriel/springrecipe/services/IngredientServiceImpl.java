package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.commands.IngredientCommand;
import com.gabriel.springrecipe.converters.IngredientToIngredientCommand;
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
}
