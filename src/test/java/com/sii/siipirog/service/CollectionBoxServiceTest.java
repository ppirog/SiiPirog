package com.sii.siipirog.service;

import com.sii.siipirog.exception.ErrorMessageException;
import com.sii.siipirog.model.CollectionBox;
import com.sii.siipirog.repository.CollectionBoxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CollectionBoxServiceTest {

    @Mock
    private CollectionBoxRepository collectionBoxRepository;

    @InjectMocks
    private CollectionBoxService collectionBoxService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void assignToEvent_shouldAssignWhenBoxIsEmptyAndUnassigned() {
        CollectionBox box = new CollectionBox();
        box.setId(1L);
        box.setFundraisingEventId(null);
        box.setMoney(Map.of("EUR", BigDecimal.ZERO));

        when(collectionBoxRepository.findById(1L)).thenReturn(Optional.of(box));
        when(collectionBoxRepository.save(any(CollectionBox.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CollectionBox result = collectionBoxService.assignToEvent(1L, 10L);

        assertEquals(10L, result.getFundraisingEventId());
        verify(collectionBoxRepository, times(1)).save(result);
    }

    @Test
    void assignToEvent_shouldThrowWhenAlreadyAssigned() {
        CollectionBox box = new CollectionBox();
        box.setId(1L);
        box.setFundraisingEventId(5L);

        when(collectionBoxRepository.findById(1L)).thenReturn(Optional.of(box));

        ErrorMessageException exception = assertThrows(ErrorMessageException.class, () -> {
            collectionBoxService.assignToEvent(1L, 10L);
        });

        assertEquals("CollectionBox is already assigned to an event", exception.getMessage());
    }

    @Test
    void assignToEvent_shouldThrowWhenNotEmpty() {
        CollectionBox box = new CollectionBox();
        box.setId(1L);
        box.setFundraisingEventId(null);
        box.setMoney(Map.of("EUR", BigDecimal.valueOf(100)));

        when(collectionBoxRepository.findById(1L)).thenReturn(Optional.of(box));

        ErrorMessageException exception = assertThrows(ErrorMessageException.class, () -> {
            collectionBoxService.assignToEvent(1L, 10L);
        });

        assertEquals("CollectionBox is not empty. Cannot assign to event.", exception.getMessage());
    }

    @Test
    void assignToEvent_shouldThrowWhenBoxNotFound() {
        when(collectionBoxRepository.findById(1L)).thenReturn(Optional.empty());

        ErrorMessageException exception = assertThrows(ErrorMessageException.class, () -> {
            collectionBoxService.assignToEvent(1L, 10L);
        });

        assertEquals("CollectionBox not found", exception.getMessage());
    }
}
