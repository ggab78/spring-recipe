package com.gabriel.springrecipe.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class Ingredient {

    @Id
    private String id;

    @DBRef
    private UnitOfMeasure uom;
    private String description;
    private BigDecimal amount;

    public Ingredient(String description, BigDecimal amount, UnitOfMeasure uom){
        this.uom=uom;
        this.description=description;
        this.amount=amount;
    }

}
