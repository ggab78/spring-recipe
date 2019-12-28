package com.gabriel.springrecipe.converters;

import com.gabriel.springrecipe.commands.CategoryCommand;
import com.gabriel.springrecipe.domain.Category;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryToCategoryCommandTest {

    CategoryToCategoryCommand categoryToCategoryCommand;

    public static final String DESCRIPTION = "description";
    public static final String ID = "1";
    @Before
    public void setUp() throws Exception {
        categoryToCategoryCommand = new CategoryToCategoryCommand();
    }

    @Test
    public void testNull(){
        assertNull(categoryToCategoryCommand.convert(null));
    }

    @Test
    public void testEmpty(){
        assertNotNull(categoryToCategoryCommand.convert(new Category()));
    }

    @Test
    public void convert() {
        //given
        Category category = new Category();
        category.setId(ID);
        category.setDescription(DESCRIPTION);

        //when
        CategoryCommand categoryCommand = categoryToCategoryCommand.convert(category);

        //then
        assertNotNull(categoryCommand);
        assertEquals(ID, categoryCommand.getId());
        assertEquals(DESCRIPTION, categoryCommand.getDescription());

    }
}