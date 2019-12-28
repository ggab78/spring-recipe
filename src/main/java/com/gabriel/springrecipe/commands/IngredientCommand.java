package com.gabriel.springrecipe.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class IngredientCommand {

    private String description;
    private String id;
    private String recipeId;
    private UnitOfMeasureCommand uom;
    private BigDecimal amount;

}
