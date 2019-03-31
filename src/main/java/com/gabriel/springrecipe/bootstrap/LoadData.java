package com.gabriel.springrecipe.bootstrap;

import com.fasterxml.jackson.annotation.OptBoolean;
import com.gabriel.springrecipe.domain.*;
import com.gabriel.springrecipe.repositories.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class LoadData implements ApplicationListener<ContextRefreshedEvent> {

    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        recipeRepository.saveAll(getRecipes());
        log.debug("Loading Bootstrap data");
    }

    private List<Recipe> getRecipes(){

        List<Recipe> recipes = new ArrayList<>();

        Recipe guacamole = new Recipe();
        guacamole.setDescription("Perfect guacamole");
        guacamole.setCookTime(1);
        guacamole.setPrepTime(30);
        guacamole.setDifficulty(Difficulty.EASY);

        Ingredient avocado = new Ingredient();
        avocado.setDescription("Avocado");
        avocado.setAmount(new BigDecimal("2.0"));
        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription("Piece");
        if(uomOptional.isPresent()){
            avocado.setUom(unitOfMeasureRepository.findByDescription("Piece").get());
        }else{
            throw new RuntimeException("unit of measure must be defined");
        }

        guacamole.addIngredient(avocado); //bidirectional method which sets also recipe to ingredient

        Notes notes = new Notes();
        notes.setRecipeNotes("The BEST guacamole!");
        guacamole.setNotes(notes);//set notes method modified to be bidirectional

        Category mexican = categoryRepository.findByDescription("Mexican").get();
        Category american = categoryRepository.findByDescription("American").get();
        guacamole.getCategories().add(mexican);
        guacamole.getCategories().add(american);

        recipes.add(guacamole);

        return recipes;
    }
}
