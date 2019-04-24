package com.gabriel.springrecipe.converters;

import com.gabriel.springrecipe.commands.RecipeCommand;
import com.gabriel.springrecipe.domain.Recipe;
import lombok.AllArgsConstructor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;


@Controller
@AllArgsConstructor
public class RecipeToRecipeCommand implements Converter<Recipe, RecipeCommand> {

    NotesToNotesCommand notesToNotesCommand;
    IngredientToIngredientCommand ingredientToIngredientCommand;
    CategoryToCategoryCommand categoryToCategoryCommand;

    @Synchronized
    @Nullable
    @Override
    public RecipeCommand convert(Recipe source) {
        if(source==null){
            return null;
        }

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(source.getId());
        recipeCommand.setCookTime(source.getCookTime());
        recipeCommand.setDescription(source.getDescription());
        recipeCommand.setDifficulty(source.getDifficulty());
        recipeCommand.setDirections(source.getDirections());
        recipeCommand.setPrepTime(source.getPrepTime());
        recipeCommand.setServings(source.getServings());
        recipeCommand.setSource(source.getSource());
        recipeCommand.setUrl(source.getUrl());
        recipeCommand.setImage(source.getImage());
        recipeCommand.setNotes(notesToNotesCommand.convert(source.getNotes()));

        if(source.getCategories()!=null && source.getCategories().size()>0){
            source.getCategories().forEach(category -> {
                recipeCommand.getCategories().add(categoryToCategoryCommand.convert(category));
            });
        }
        if(source.getIngredients()!=null && source.getIngredients().size()>0){
            source.getIngredients().forEach(ingredient -> {
                recipeCommand.getIngredients().add(ingredientToIngredientCommand.convert(ingredient));
            });
        }

        return recipeCommand;
    }
}
