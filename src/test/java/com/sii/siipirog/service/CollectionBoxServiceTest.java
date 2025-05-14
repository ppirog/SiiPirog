package com.sii.siipirog.service;

import com.sii.siipirog.exception.ErrorMessageException;
import com.sii.siipirog.model.CollectionBox;
import com.sii.siipirog.model.FundraisingEvent;
import com.sii.siipirog.repository.CollectionBoxRepository;
import com.sii.siipirog.repository.FundraisingEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CollectionBoxServiceTest {

    @Mock
    private CollectionBoxRepository collectionBoxRepository;

    @Mock
    private FundraisingEventRepository fundraisingEventRepository;

    @InjectMocks
    private CollectionBoxService collectionBoxService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    // assing to event
    @Test
    void assignToEvent_shouldAssignWhenBoxIsEmptyAndUnassigned() {
        CollectionBox box = new CollectionBox();
        box.setId(1L);
        box.setFundraisingEventId(null);
        box.setMoney(Map.of("EUR", BigDecimal.ZERO));

        when(collectionBoxRepository.findById(1L)).thenReturn(Optional.of(box));
        when(fundraisingEventRepository.existsById(10L)).thenReturn(true);
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

        ErrorMessageException exception = assertThrows(ErrorMessageException.class, () ->
                collectionBoxService.assignToEvent(1L, 10L)
        );

        assertEquals("CollectionBox is already assigned to an event", exception.getMessage());
    }

    @Test
    void assignToEvent_shouldThrowWhenEventNotExists() {
        CollectionBox box = new CollectionBox();
        box.setId(1L);
        box.setFundraisingEventId(null);
        box.setMoney(new HashMap<>());

        when(collectionBoxRepository.findById(1L)).thenReturn(Optional.of(box));
        when(fundraisingEventRepository.existsById(10L)).thenReturn(false);

        ErrorMessageException exception = assertThrows(ErrorMessageException.class, () -> {
            collectionBoxService.assignToEvent(1L, 10L);
        });

        assertEquals("FundraisingEvent not found", exception.getMessage());
    }

    @Test
    void assignToEvent_shouldThrowWhenNotEmpty() {
        CollectionBox box = new CollectionBox();
        box.setId(1L);
        box.setFundraisingEventId(null);
        box.setMoney(Map.of("EUR", BigDecimal.valueOf(100)));

        when(collectionBoxRepository.findById(1L)).thenReturn(Optional.of(box));

        ErrorMessageException exception = assertThrows(ErrorMessageException.class, () ->
                collectionBoxService.assignToEvent(1L, 10L)
        );

        assertEquals("CollectionBox is not empty. Cannot assign to event.", exception.getMessage());
    }

    @Test
    void assignToEvent_shouldThrowWhenBoxNotFound() {
        when(collectionBoxRepository.findById(1L)).thenReturn(Optional.empty());

        ErrorMessageException exception = assertThrows(ErrorMessageException.class, () ->
                collectionBoxService.assignToEvent(1L, 10L)
        );

        assertEquals("CollectionBox not found", exception.getMessage());
    }

    @Test
    void deleteBox_shouldCallRepositoryDelete() {
        Long boxId = 1L;

        collectionBoxService.deleteBox(boxId);

        verify(collectionBoxRepository, times(1)).deleteById(boxId);
    }


    // add money
    @Test
    void addMoney_shouldAddNewCurrencyIfNotExists() {
        CollectionBox box = new CollectionBox();
        box.setId(1L);
        box.setMoney(new HashMap<>());

        when(collectionBoxRepository.findById(1L)).thenReturn(Optional.of(box));
        when(collectionBoxRepository.save(any(CollectionBox.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CollectionBox result = collectionBoxService.addMoney(1L, "EUR", BigDecimal.valueOf(100));

        assertEquals(BigDecimal.valueOf(100), result.getMoney().get("EUR"));
    }

    @Test
    void addMoney_shouldAddToExistingCurrency() {
        CollectionBox box = new CollectionBox();
        box.setId(1L);
        box.setMoney(new HashMap<>(Map.of("EUR", BigDecimal.valueOf(50))));

        when(collectionBoxRepository.findById(1L)).thenReturn(Optional.of(box));
        when(collectionBoxRepository.save(any(CollectionBox.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CollectionBox result = collectionBoxService.addMoney(1L, "EUR", BigDecimal.valueOf(25));

        assertEquals(BigDecimal.valueOf(75), result.getMoney().get("EUR"));
    }

    @Test
    void addMoney_shouldThrowIfBoxNotFound() {
        when(collectionBoxRepository.findById(1L)).thenReturn(Optional.empty());

        ErrorMessageException exception = assertThrows(ErrorMessageException.class, () ->
                collectionBoxService.addMoney(1L, "EUR", BigDecimal.valueOf(100))
        );

        assertEquals("CollectionBox not found", exception.getMessage());
    }

    @Test
    void addMoney_shouldThrowIfAmountIsNotPositive() {
        CollectionBox box = new CollectionBox();
        box.setId(1L);
        box.setMoney(new HashMap<>());

        when(collectionBoxRepository.findById(1L)).thenReturn(Optional.of(box));

        ErrorMessageException exception = assertThrows(ErrorMessageException.class, () ->
                collectionBoxService.addMoney(1L, "EUR", BigDecimal.ZERO)
        );

        assertEquals("Amount must be positive", exception.getMessage());
    }


    // transfer money
    @Test
    void transferMoney_shouldTransferAndEmptyBox() {
        CollectionBox box = new CollectionBox();
        box.setId(1L);
        box.setFundraisingEventId(10L);
        Map<String, BigDecimal> money = new HashMap<>();
        money.put("USD", BigDecimal.valueOf(100));
        money.put("EUR", BigDecimal.valueOf(50));
        box.setMoney(money);

        FundraisingEvent event = new FundraisingEvent();
        event.setId(10L);
        event.setAccountBalance(BigDecimal.valueOf(0));
        event.setCurrency("PLN");

        when(collectionBoxRepository.findById(1L)).thenReturn(Optional.of(box));
        when(fundraisingEventRepository.findById(10L)).thenReturn(Optional.of(event));

        collectionBoxService.transferMoney(1L);

        // USD (100 x 4.00 PLN) + EUR (50 x 4.30 PLN) = 400 + 215 = 615
        assertEquals(BigDecimal.valueOf(615.00), event.getAccountBalance());

        assertTrue(box.getMoney().values().stream().allMatch(amount -> amount.compareTo(BigDecimal.ZERO) == 0));

        verify(collectionBoxRepository, times(1)).save(box);
        verify(fundraisingEventRepository, times(1)).save(event);
    }

    @Test
    void transferMoney_shouldThrowWhenBoxNotFound() {
        when(collectionBoxRepository.findById(1L)).thenReturn(Optional.empty());

        ErrorMessageException exception = assertThrows(ErrorMessageException.class, () ->
                collectionBoxService.transferMoney(1L)
        );

        assertEquals("CollectionBox not found", exception.getMessage());
    }

    @Test
    void transferMoney_shouldThrowWhenBoxNotAssigned() {
        CollectionBox box = new CollectionBox();
        box.setId(1L);
        box.setFundraisingEventId(null);

        when(collectionBoxRepository.findById(1L)).thenReturn(Optional.of(box));

        ErrorMessageException exception = assertThrows(ErrorMessageException.class, () ->
                collectionBoxService.transferMoney(1L)
        );

        assertEquals("CollectionBox is not assigned to any event", exception.getMessage());
    }

    @Test
    void transferMoney_shouldThrowWhenEventNotFound() {
        CollectionBox box = new CollectionBox();
        box.setId(1L);
        box.setFundraisingEventId(10L);

        when(collectionBoxRepository.findById(1L)).thenReturn(Optional.of(box));
        when(fundraisingEventRepository.findById(10L)).thenReturn(Optional.empty());

        ErrorMessageException exception = assertThrows(ErrorMessageException.class, () ->
                collectionBoxService.transferMoney(1L)
        );

        assertEquals("FundraisingEvent not found", exception.getMessage());
    }
}
