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
    private Long id;
    private Long recipeId;
    private UnitOfMeasureCommand uom;
    private BigDecimal amount;

}
