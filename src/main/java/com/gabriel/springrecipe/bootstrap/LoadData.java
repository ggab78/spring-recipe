package com.gabriel.springrecipe.bootstrap;

import com.gabriel.springrecipe.domain.*;
import com.gabriel.springrecipe.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@AllArgsConstructor
public class LoadData implements ApplicationListener<ContextRefreshedEvent> {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final NotesRepository notesRepository;
    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        LoadData();
    }

    private void LoadData(){

        Recipe guacamole = new Recipe();
        guacamole.setDescription("Perfect guacamole");
        guacamole.setCookTime(1);
        guacamole.setPrepTime(30);

        Ingredient avocado = new Ingredient();
        avocado.setDescription("Avocado");
        avocado.setAmount(new BigDecimal("2.0"));
        avocado.setUom(unitOfMeasureRepository.findByDescription("Piece").get());
        guacamole.getIngredients().add(avocado);

        Notes notes = new Notes();
        notes.setRecipeNotes("The BEST guacamole!");
        guacamole.setNotes(notes);

        Category mexican = categoryRepository.findByDescription("Mexican").get();
        guacamole.getCategories().add(mexican);
        recipeRepository.save(guacamole);

        avocado.setRecipe(guacamole);
        ingredientRepository.save(avocado);

        notes.setRecipe(guacamole);
        notesRepository.save(notes);
    }
}
