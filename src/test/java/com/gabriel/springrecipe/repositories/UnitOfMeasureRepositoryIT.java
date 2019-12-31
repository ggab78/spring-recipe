package com.gabriel.springrecipe.repositories;

import com.gabriel.springrecipe.bootstrap.RecipeBootstrap;
import com.gabriel.springrecipe.domain.UnitOfMeasure;
import com.gabriel.springrecipe.repositories.reactive.CategoryReactiveRepository;
import com.gabriel.springrecipe.repositories.reactive.RecipeReactiveRepository;
import com.gabriel.springrecipe.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataMongoTest
public class UnitOfMeasureRepositoryIT {

    @Autowired
    UnitOfMeasureReactiveRepository unitOfMeasureRepository;

    @Autowired
    CategoryReactiveRepository categoryRepository;

    @Autowired
    RecipeReactiveRepository recipeRepository;


    @Before
    public void setUp() throws Exception {
        RecipeBootstrap recipeBootstrap = new RecipeBootstrap(categoryRepository, recipeRepository, unitOfMeasureRepository);
        recipeBootstrap.onApplicationEvent(null);
    }


    @Test
    public void findByDescription() throws Exception{
        UnitOfMeasure unitOfMeasure = unitOfMeasureRepository.findByDescription("Teaspoon").block();
        assertEquals("Teaspoon", unitOfMeasure.getDescription());
    }
    @Test
    public void findByDescriptionCup() throws Exception{
        UnitOfMeasure unitOfMeasure = unitOfMeasureRepository.findByDescription("Cup").block();
        assertEquals("Cup", unitOfMeasure.getDescription());
    }
}