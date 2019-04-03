package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.domain.Recipe;
import com.gabriel.springrecipe.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository);
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
}