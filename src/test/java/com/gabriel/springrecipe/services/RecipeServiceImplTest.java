package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.commands.RecipeCommand;
import com.gabriel.springrecipe.converters.RecipeCommandToRecipe;
import com.gabriel.springrecipe.converters.RecipeToRecipeCommand;
import com.gabriel.springrecipe.domain.Recipe;
import com.gabriel.springrecipe.exceptions.NotFoundException;
import com.gabriel.springrecipe.repositories.RecipeRepository;
import com.gabriel.springrecipe.repositories.reactive.RecipeReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;

    @Mock
    RecipeReactiveRepository recipeReactiveRepository;
    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;
    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        recipeService = new RecipeServiceImpl(recipeReactiveRepository,recipeToRecipeCommand, recipeCommandToRecipe);
    }

    @Test
    public void getRecipes() {

        Recipe recipe = new Recipe();
        recipe.setDescription("good");
        Recipe recipe2 = new Recipe();
        recipe2.setDescription("better");



        when(recipeReactiveRepository.findAll()).thenReturn(Flux.just(recipe, recipe2));

        List<Recipe> recipes = recipeService.getRecipes().collectList().block();

        assertEquals(2,recipes.size());
        verify(recipeReactiveRepository, times(1)).findAll();
    }

    @Test
    public void getRecipeById() {

        String recipeId = "1";
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        assertEquals(recipeId, recipeService.getRecipeById(recipeId).block().getId());
        verify(recipeReactiveRepository, times(1)).findById(anyString());
        verify(recipeReactiveRepository, never()).findAll();
    }



    @Test
    public void getRecipeCommandById() {
        //given
        String recipeId = "3";
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(recipeId);

        Recipe recipe = new Recipe();
        recipe.setId("33");


        //when
        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));
        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        //then
        assertEquals(recipeId, recipeService.getRecipeCommandById(recipeId).block().getId());
        verify(recipeReactiveRepository, times(1)).findById(anyString());
        verify(recipeToRecipeCommand, times(1)).convert(any());
    }

    @Test
    public void saveRecipeCommand() {
        //given
        String recipeId = "1";
        Recipe recipe = new Recipe();
        recipe.setId(recipeId);

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("69");

        //when
        when(recipeCommandToRecipe.convert(any())).thenReturn(recipe);
        when(recipeReactiveRepository.save(any())).thenReturn(Mono.just(recipe));
        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        //then
        RecipeCommand command = recipeService.saveRecipeCommand(recipeCommand).block();
        assertEquals("69", command.getId());
        verify(recipeCommandToRecipe, times(1)).convert(any());
        verify(recipeReactiveRepository, times(1)).save(any());
        verify(recipeToRecipeCommand, times(1)).convert(any());
    }

    @Test (expected = Exception.class)
    public void deleteById() throws Exception {
        String deleteId = "3";
        Recipe recipe = new Recipe();
        recipe.setId(deleteId);


        when(recipeReactiveRepository.findById(deleteId)).thenReturn(Mono.just(recipe));
        recipeService.deleteById(deleteId);
        verify(recipeReactiveRepository, times(1)).deleteById(deleteId);

        try{
            deleteId = "4";
            recipeService.deleteById(deleteId);
        }catch (Exception re) {
            String message = "No recipe with id = "+ deleteId;
            assertEquals(message, re.getMessage());
            throw re;
        }
    }
}