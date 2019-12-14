package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.commands.RecipeCommand;
import com.gabriel.springrecipe.converters.RecipeCommandToRecipe;
import com.gabriel.springrecipe.converters.RecipeToRecipeCommand;
import com.gabriel.springrecipe.domain.Recipe;
import com.gabriel.springrecipe.exceptions.NotFoundException;
import com.gabriel.springrecipe.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;

    @Mock
    RecipeRepository recipeRepository;
    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;
    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository,recipeToRecipeCommand, recipeCommandToRecipe);
    }

    @Test
    public void getRecipes() {

        Recipe recipe = new Recipe();
        recipe.setDescription("good");
        Recipe recipe2 = new Recipe();
        recipe2.setDescription("better");

        Set<Recipe> recipesData = new HashSet<>();

        recipesData.add(recipe);
        recipesData.add(recipe2);

        when(recipeRepository.findAll()).thenReturn(recipesData);

        Set<Recipe> recipes = recipeService.getRecipes();

        assertEquals(2,recipes.size());
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    public void getRecipeById() {

        Long recipeId = 1L;
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);

        Optional<Recipe> recipeOptional = Optional.of(recipe);
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        assertEquals(recipeId, recipeService.getRecipeById(recipeId).getId());
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, never()).findAll();
    }

    @Test(expected = NotFoundException.class)
    public void getRecipeByIdNotFound() {

        Optional<Recipe> recipeOptional = Optional.empty();

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        recipeService.getRecipeById(1L);

    }



    @Test
    public void getRecipeCommandById() {
        //given
        Long recipeId = 3L;
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(recipeId);

        Recipe recipe = new Recipe();
        recipe.setId(33L);
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        //when
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        //then
        assertEquals(recipeId, recipeService.getRecipeCommandById(recipeId).getId());
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeToRecipeCommand, times(1)).convert(any());
    }

    @Test
    public void saveRecipeCommand() {
        //given
        Long recipeId = 1L;
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(69L);

        //when
        when(recipeCommandToRecipe.convert(any())).thenReturn(recipe);
        when(recipeRepository.save(any())).thenReturn(recipe);
        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        //then
        RecipeCommand command = recipeService.saveRecipeCommand(recipeCommand);
        assertEquals(((Long)69L), command.getId());
        verify(recipeCommandToRecipe, times(1)).convert(any());
        verify(recipeRepository, times(1)).save(any());
        verify(recipeToRecipeCommand, times(1)).convert(any());
    }

    @Test (expected = Exception.class)
    public void deleteById() throws Exception {
        Long deleteId = 3L;
        Recipe recipe = new Recipe();
        recipe.setId(deleteId);
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(deleteId)).thenReturn(recipeOptional);
        recipeService.deleteById(deleteId);
        verify(recipeRepository, times(1)).deleteById(deleteId);

        try{
            deleteId = 4L;
            recipeService.deleteById(deleteId);
        }catch (Exception re) {
            String message = "No recipe with id = "+deleteId;
            assertEquals(message, re.getMessage());
            throw re;
        }
    }
}