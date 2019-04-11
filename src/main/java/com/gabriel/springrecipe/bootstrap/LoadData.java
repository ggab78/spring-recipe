package com.gabriel.springrecipe.bootstrap;

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
        guacamole.setCookTime(10);
        guacamole.setPrepTime(30);
        guacamole.setDifficulty(Difficulty.EASY);
        guacamole.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
        guacamole.setServings(4);
        guacamole.setSource("Simply Recipes");
        guacamole.setDirections("1. Put a teaspoon each of the chilli, onion and coriander into a pestle and mortar, " +
                "along with a pinch of coarse salt, and grind to a paste.\n" +
                "\n" +
                "2. Peel the avocados and remove the stone. Cut into cubes, then mash into a chunky paste, leaving " +
                "some pieces intact.\n" +
                "\n" +
                "3. Stir the chilli paste into the avocado, and then gently fold in the tomatoes and the rest of " +
                "the onions, chilli and coriander. Add lime juice and salt to taste. Serve immediately, " +
                "or cover the surface with cling film and refrigerate.");

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
