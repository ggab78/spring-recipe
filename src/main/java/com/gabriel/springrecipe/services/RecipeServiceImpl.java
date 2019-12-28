package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.commands.RecipeCommand;
import com.gabriel.springrecipe.converters.RecipeCommandToRecipe;
import com.gabriel.springrecipe.converters.RecipeToRecipeCommand;
import com.gabriel.springrecipe.domain.Recipe;
import com.gabriel.springrecipe.exceptions.NotFoundException;
import com.gabriel.springrecipe.repositories.RecipeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeToRecipeCommand recipeToRecipeCommand;
    private final RecipeCommandToRecipe recipeCommandToRecipe;

    @Override
    public Set<Recipe> getRecipes() {

        log.debug("I'am in the service");

        Set<Recipe> recipes = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipe -> recipes.add(recipe));
        return recipes;
    }

    @Override
    public Recipe getRecipeById(String id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if(!recipeOptional.isPresent()){
            throw new NotFoundException("Recipe not found for ID value "+id);
        }
        return recipeOptional.get();
    }

    @Override
    @Transactional
    public RecipeCommand getRecipeCommandById(String id) {
        return recipeToRecipeCommand.convert(getRecipeById(id));
    }

    @Override
    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand command) {
        Recipe detachedRecipe = recipeCommandToRecipe.convert(command);
        Recipe savedRecipe = recipeRepository.save(detachedRecipe);
        log.debug("saved recipe id : "+savedRecipe.getId());
        return recipeToRecipeCommand.convert(savedRecipe);
    }

    @Override
    @Transactional
    public boolean deleteById(String id) throws Exception {
        if(recipeRepository.findById(id).isPresent()){
            recipeRepository.deleteById(id);
            return true;
        }else{
            throw new Exception("No recipe with id = "+id);
        }
    }
}
