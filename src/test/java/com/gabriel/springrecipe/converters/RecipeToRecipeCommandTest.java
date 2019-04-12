package com.gabriel.springrecipe.converters;


import com.gabriel.springrecipe.commands.NotesCommand;
import com.gabriel.springrecipe.commands.RecipeCommand;
import com.gabriel.springrecipe.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class RecipeToRecipeCommandTest {
    public static final Long RECIPE_ID = 1L;
    public static final Integer COOK_TIME = Integer.valueOf("5");
    public static final Integer PREP_TIME = Integer.valueOf("7");
    public static final String DESCRIPTION = "My Recipe";
    public static final String DIRECTIONS = "Directions";
    public static final Difficulty DIFFICULTY = Difficulty.EASY;
    public static final Integer SERVINGS = Integer.valueOf("3");
    public static final String SOURCE = "Source";
    public static final String URL = "Some URL";
    public static final Long CAT_ID_1 = 1L;
    public static final Long CAT_ID_2 = 2L;
    public static final Long INGRED_ID_1 = 3L;
    public static final Long INGRED_ID_2 = 4L;
    public static final Long NOTES_ID = 9L;


    RecipeToRecipeCommand recipeToRecipeCommand;

    @Mock
    NotesToNotesCommand notesToNotesCommand;

    @Mock
    IngredientToIngredientCommand ingredientToIngredientCommand;

    @Mock
    CategoryToCategoryCommand categoryToCategoryCommand;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        recipeToRecipeCommand = new RecipeToRecipeCommand(notesToNotesCommand, ingredientToIngredientCommand,
                categoryToCategoryCommand);
    }

    @Test
    public void testNull() throws Exception{
        assertNull(recipeToRecipeCommand.convert(null));
    }

    @Test
    public void testEmpty() throws Exception{
        NotesCommand notesCommand = new NotesCommand();
        when(notesToNotesCommand.convert(any())).thenReturn(notesCommand);
        assertNotNull(recipeToRecipeCommand.convert(new Recipe()));
    }

    @Test
    public void convert() throws Exception{
        //given
        Recipe recipe = new Recipe();
        recipe.setUrl(URL);
        recipe.setSource(SOURCE);
        recipe.setServings(SERVINGS);
        recipe.setPrepTime(PREP_TIME);
        recipe.setDirections(DIRECTIONS);
        recipe.setDifficulty(DIFFICULTY);
        recipe.setDescription(DESCRIPTION);
        recipe.setCookTime(COOK_TIME);
        recipe.setId(RECIPE_ID);

        Notes notes = new Notes();
        notes.setId(NOTES_ID);
        recipe.setNotes(notes);

        Set<Category> categorySet = new HashSet<>();
        Category cat = new Category();
        cat.setId(CAT_ID_1);
        categorySet.add(cat);
        cat = new Category();
        cat.setId(CAT_ID_2);
        categorySet.add(cat);
        recipe.setCategories(categorySet);

        Set<Ingredient> ingredientSet = new HashSet<>();
        Ingredient ing = new Ingredient();
        ing.setId(INGRED_ID_1);
        ingredientSet.add(ing);
        ing = new Ingredient();
        ing.setId(INGRED_ID_2);
        ingredientSet.add(ing);
        recipe.setIngredients(ingredientSet);

        //when
        NotesCommand notesCommand = new NotesCommand();
        notesCommand.setId(NOTES_ID);
        when(notesToNotesCommand.convert(any())).thenReturn(notesCommand);
        RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipe);

        //then
        assertNotNull(recipe);
        assertEquals(RECIPE_ID, recipeCommand.getId());
        assertEquals(DESCRIPTION, recipeCommand.getDescription());
        assertEquals(DIFFICULTY, recipeCommand.getDifficulty());
        assertEquals(SERVINGS, recipeCommand.getServings());
        assertEquals(SOURCE, recipeCommand.getSource());
        verify(ingredientToIngredientCommand,times(2)).convert(any());
        verify(categoryToCategoryCommand, times(2)).convert(any());
    }
}