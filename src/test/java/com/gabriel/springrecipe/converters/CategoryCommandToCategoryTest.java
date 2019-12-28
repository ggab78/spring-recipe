package com.gabriel.springrecipe.converters;

import com.gabriel.springrecipe.commands.CategoryCommand;
import com.gabriel.springrecipe.domain.Category;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryCommandToCategoryTest {

    CategoryCommandToCategory categoryCommandToCategory;

    public static final String DESCRIPTION = "description";
    public static final String ID = "1";

    @Before
    public void setUp() throws Exception {
        categoryCommandToCategory = new CategoryCommandToCategory();
    }

    @Test
    public void testNull(){
        assertNull(categoryCommandToCategory.convert(null));
    }

    @Test
    public void testEmpty(){
        assertNotNull(categoryCommandToCategory.convert(new CategoryCommand()));
    }

    @Test
    public void convert() {
        //given
        CategoryCommand categoryCommand = new CategoryCommand();
        categoryCommand.setId(ID);
        categoryCommand.setDescription(DESCRIPTION);

        //when
        Category category = categoryCommandToCategory.convert(categoryCommand);

        //then
        assertEquals(ID, category.getId());
        assertEquals(DESCRIPTION, category.getDescription());
    }
}