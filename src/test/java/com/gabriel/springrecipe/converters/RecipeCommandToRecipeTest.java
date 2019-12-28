package com.gabriel.springrecipe.converters;

import com.gabriel.springrecipe.commands.CategoryCommand;
import com.gabriel.springrecipe.commands.IngredientCommand;
import com.gabriel.springrecipe.commands.NotesCommand;
import com.gabriel.springrecipe.commands.RecipeCommand;
import com.gabriel.springrecipe.domain.Category;
import com.gabriel.springrecipe.domain.Difficulty;
import com.gabriel.springrecipe.domain.Notes;
import com.gabriel.springrecipe.domain.Recipe;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class RecipeCommandToRecipeTest {

    public static final String RECIPE_ID = "1";
    public static final Integer COOK_TIME = Integer.valueOf("5");
    public static final Integer PREP_TIME = Integer.valueOf("7");
    public static final String DESCRIPTION = "My Recipe";
    public static final String DIRECTIONS = "Directions";
    public static final Difficulty DIFFICULTY = Difficulty.EASY;
    public static final Integer SERVINGS = Integer.valueOf("3");
    public static final String SOURCE = "Source";
    public static final String URL = "Some URL";
    public static final String CAT_ID_1 = "1";
    public static final String CAT_ID_2 = "2";
    public static final String INGRED_ID_1 = "3";
    public static final String INGRED_ID_2 = "4";
    public static final String NOTES_ID = "9";


    RecipeCommandToRecipe recipeCommandToRecipe;

    @Mock
    NotesCommandToNotes notesCommandToNotes;

    @Mock
    IngredientCommandToIngredient ingredientCommandToIngredient;

    @Mock
    CategoryCommandToCategory categoryCommandToCategory;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        recipeCommandToRecipe = new RecipeCommandToRecipe(notesCommandToNotes,
                ingredientCommandToIngredient, categoryCommandToCategory);
    }


    @Test
    public void testNull() throws Exception{
        assertNull(recipeCommandToRecipe.convert(null));
    }

    @Test
    public void testEmpty() throws Exception{
        Notes notes = new Notes();
        when(notesCommandToNotes.convert(any())).thenReturn(notes);
        assertNotNull(recipeCommandToRecipe.convert(new RecipeCommand()));
    }

    @Test
    public void convert() throws Exception{
        //given
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setUrl(URL);
        recipeCommand.setSource(SOURCE);
        recipeCommand.setServings(SERVINGS);
        recipeCommand.setPrepTime(PREP_TIME);
        recipeCommand.setDirections(DIRECTIONS);
        recipeCommand.setDifficulty(DIFFICULTY);
        recipeCommand.setDescription(DESCRIPTION);
        recipeCommand.setCookTime(COOK_TIME);
        recipeCommand.setId(RECIPE_ID);

        NotesCommand notesCommand = new NotesCommand();
        notesCommand.setId(NOTES_ID);
        recipeCommand.setNotes(notesCommand);

        Set<CategoryCommand> categorySet = new HashSet<>();
        CategoryCommand cat = new CategoryCommand();
        cat.setId(CAT_ID_1);
        categorySet.add(cat);
        cat = new CategoryCommand();
        cat.setId(CAT_ID_2);
        categorySet.add(cat);
        recipeCommand.setCategories(categorySet);

        Set<IngredientCommand> ingredientSet = new HashSet<>();
        IngredientCommand ing = new IngredientCommand();
        ing.setId(INGRED_ID_1);
        ingredientSet.add(ing);
        ing = new IngredientCommand();
        ing.setId(INGRED_ID_2);
        ingredientSet.add(ing);
        recipeCommand.setIngredients(ingredientSet);

        //when
        Notes notes = new Notes();
        notes.setId(NOTES_ID);
        when(notesCommandToNotes.convert(any())).thenReturn(notes);

        Recipe recipe = recipeCommandToRecipe.convert(recipeCommand);

        //then
        assertNotNull(recipe);
        assertEquals(RECIPE_ID, recipe.getId());
        assertEquals(DESCRIPTION, recipe.getDescription());
        assertEquals(DIFFICULTY, recipe.getDifficulty());
        assertEquals(SERVINGS, recipe.getServings());
        assertEquals(SOURCE, recipe.getSource());
        assertEquals(NOTES_ID, recipe.getNotes().getId());
        verify(ingredientCommandToIngredient,times(2)).convert(any());
        verify(categoryCommandToCategory, times(2)).convert(any());
        verify(notesCommandToNotes, times(1)).convert(any());


    }
}