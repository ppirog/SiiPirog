package com.sii.siipirog.service;

import com.sii.siipirog.dto.FinancialReportDto;
import com.sii.siipirog.model.FundraisingEvent;
import com.sii.siipirog.repository.FundraisingEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class FundraisingEventServiceTest {

    @Mock
    private FundraisingEventRepository fundraisingEventRepository;

    @InjectMocks
    private FundraisingEventService fundraisingEventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // report
    @Test
    void getFinancialReport_shouldReturnCorrectDtos() {
        FundraisingEvent event1 = new FundraisingEvent();
        event1.setId(1L);
        event1.setName("CARITAS");
        event1.setCurrency("PLN");
        event1.setAccountBalance(BigDecimal.valueOf(2048.00));

        FundraisingEvent event2 = new FundraisingEvent();
        event2.setId(2L);
        event2.setName("WOSP");
        event2.setCurrency("PLN");
        event2.setAccountBalance(BigDecimal.valueOf(512.64));

        when(fundraisingEventRepository.findAll()).thenReturn(List.of(event1, event2));

        List<FinancialReportDto> report = fundraisingEventService.getFinancialReport();

        assertEquals(2, report.size());

        FinancialReportDto first = report.get(0);
        assertEquals("CARITAS", first.getEventName());
        assertEquals(BigDecimal.valueOf(2048.00), first.getAmount());
        assertEquals("PLN", first.getCurrency());

        FinancialReportDto second = report.get(1);
        assertEquals("WOSP", second.getEventName());
        assertEquals(BigDecimal.valueOf(512.64), second.getAmount());
        assertEquals("PLN", second.getCurrency());

        verify(fundraisingEventRepository, times(1)).findAll();
    }
}