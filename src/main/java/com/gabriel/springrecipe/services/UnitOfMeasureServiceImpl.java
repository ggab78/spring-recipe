package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.commands.UnitOfMeasureCommand;
import com.gabriel.springrecipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.gabriel.springrecipe.domain.UnitOfMeasure;
import com.gabriel.springrecipe.repositories.UnitOfMeasureRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    UnitOfMeasureRepository unitOfMeasureRepository;
    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;


    @Override
    public Set<UnitOfMeasure> findAllUom() {
        Set<UnitOfMeasure> unitOfMeasureSet = new HashSet<>();
        unitOfMeasureRepository.findAll().forEach(unitOfMeasure -> {
            unitOfMeasureSet.add(unitOfMeasure);
        });
        return unitOfMeasureSet;
    }

    @Override
    public Set<UnitOfMeasureCommand> findAllUomCommand() {
        Set<UnitOfMeasureCommand> unitOfMeasureCommandSet = new HashSet<>();
        findAllUom().forEach(unitOfMeasure -> {
            unitOfMeasureCommandSet.add(unitOfMeasureToUnitOfMeasureCommand.convert(unitOfMeasure));
        });
        return unitOfMeasureCommandSet;
    }
}
