package com.gabriel.springrecipe.services;

import com.gabriel.springrecipe.commands.UnitOfMeasureCommand;
import com.gabriel.springrecipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.gabriel.springrecipe.domain.UnitOfMeasure;
import com.gabriel.springrecipe.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class UnitOfMeasureServiceImplTest {

    UnitOfMeasureServiceImpl unitOfMeasureService;

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    @Mock
    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        unitOfMeasureService = new UnitOfMeasureServiceImpl(unitOfMeasureRepository, unitOfMeasureToUnitOfMeasureCommand);
        mockMvc = MockMvcBuilders.standaloneSetup(unitOfMeasureService).build();
    }

    @Test
    public void findAllUom() {
        //given
        Set<UnitOfMeasure> unitOfMeasureSet = new HashSet<>();
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId("1");
        unitOfMeasureSet.add(unitOfMeasure);
        unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId("2");
        unitOfMeasureSet.add(unitOfMeasure);
        Iterable<UnitOfMeasure> unitOfMeasureIterable = unitOfMeasureSet.stream().collect(Collectors.toSet());

        //when
        when(unitOfMeasureRepository.findAll()).thenReturn(unitOfMeasureIterable);

        //then
        assertEquals(2, unitOfMeasureService.findAllUom().size());

    }

    @Test
    public void findAllUomCommand() {
        //given
        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        Set<UnitOfMeasure> unitOfMeasureSet = new HashSet<>();
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId("1");
        unitOfMeasureSet.add(unitOfMeasure);
        unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId("2");
        unitOfMeasureSet.add(unitOfMeasure);
        Iterable<UnitOfMeasure> unitOfMeasureIterable = unitOfMeasureSet.stream().collect(Collectors.toSet());

        //when
        when(unitOfMeasureRepository.findAll()).thenReturn(unitOfMeasureIterable);
        when(unitOfMeasureToUnitOfMeasureCommand.convert(any())).thenReturn(unitOfMeasureCommand);

        //then
        assertEquals(2,unitOfMeasureService.findAllUom().size());

        //unitofMeasureCommand is the same that is why only one is added and invoked twice
        assertEquals(1, unitOfMeasureService.findAllUomCommand().size());
        verify(unitOfMeasureToUnitOfMeasureCommand, times(2)).convert(any());
    }
}