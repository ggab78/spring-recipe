package com.gabriel.springrecipe.domain;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Ingredient {

    @Id
    private String id = UUID.randomUUID().toString();
    private UnitOfMeasure uom;
    private String description;
    private BigDecimal amount;

    public Ingredient(String description, BigDecimal amount, UnitOfMeasure uom){
        this.uom=uom;
        this.description=description;
        this.amount=amount;
    }

}
