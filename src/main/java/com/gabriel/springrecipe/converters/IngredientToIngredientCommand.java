package com.gabriel.springrecipe.converters;

import com.gabriel.springrecipe.commands.IngredientCommand;
import com.gabriel.springrecipe.domain.Ingredient;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class IngredientToIngredientCommand implements Converter<Ingredient, IngredientCommand> {

    private UnitOfMeasureToUnitOfMeasureCommand uomConverter;

    @Override
    public IngredientCommand convert(Ingredient source) {
        if(source==null){
            return null;
        }

        final IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(source.getId());
        ingredientCommand.setDescription(source.getDescription());
        ingredientCommand.setAmount(source.getAmount());
        ingredientCommand.setUom(uomConverter.convert(source.getUom()));

        return ingredientCommand;
    }
}
