package com.gabriel.springrecipe.converters;

import com.gabriel.springrecipe.commands.RecipeCommand;
import com.gabriel.springrecipe.domain.Recipe;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe> {

    NotesCommandToNotes notesCommandToNotes;
    IngredientCommandToIngredient ingredientCommandToIngredient;
    CategoryCommandToCategory categoryCommandToCategory;

    @Override
    public Recipe convert(RecipeCommand source) {
        if(source == null){
            return null;
        }
        Recipe recipe = new Recipe();
        if(!source.getId().isEmpty()){
            recipe.setId(source.getId());
        }
        recipe.setSource(source.getSource());
        recipe.setServings(source.getServings());
        recipe.setUrl(source.getUrl());
        recipe.setDirections(source.getDirections());
        recipe.setDescription(source.getDescription());
        recipe.setDifficulty(source.getDifficulty());
        recipe.setPrepTime(source.getPrepTime());
        recipe.setCookTime(source.getCookTime());
        recipe.setImage(source.getImage());
        recipe.setNotes(notesCommandToNotes.convert(source.getNotes()));

        if(source.getCategories()!=null && source.getCategories().size()>0){
            source.getCategories().forEach(categoryCommand -> {
                recipe.getCategories().add(categoryCommandToCategory.convert(categoryCommand));
            });
        }

        if(source.getIngredients()!=null && source.getIngredients().size()>0){
            source.getIngredients().forEach(ingredientCommand -> {
                recipe.getIngredients().add(ingredientCommandToIngredient.convert(ingredientCommand));
            });

        }

        return recipe;
    }
}
