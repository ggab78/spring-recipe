package com.gabriel.springrecipe.converters;

import com.gabriel.springrecipe.commands.IngredientCommand;
import com.gabriel.springrecipe.domain.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class IngredientCommandToIngredient implements Converter<IngredientCommand, Ingredient> {

    private UnitOfMeasureCommandToUnitOfMeasure uomConverter;

    @Synchronized
    @Nullable
    @Override
    public Ingredient convert(IngredientCommand source) {
        if(source==null){
            return null;
        }

        final Ingredient ingredient = new Ingredient();

        if(!source.getId().isEmpty()){
            ingredient.setId(source.getId());
        }
        ingredient.setDescription(source.getDescription());
        ingredient.setAmount(source.getAmount());
        ingredient.setUom(uomConverter.convert(source.getUom()));
        return ingredient;
    }
}
