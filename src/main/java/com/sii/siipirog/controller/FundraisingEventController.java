package com.sii.siipirog.controller;

import com.sii.siipirog.dto.FinancialReportDto;
import com.sii.siipirog.dto.FundraisingEventDto;
import com.sii.siipirog.mapper.FundraisingEventMapper;
import com.sii.siipirog.model.FundraisingEvent;
import com.sii.siipirog.service.FundraisingEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class FundraisingEventController {

    private final FundraisingEventService fundraisingEventService;

    @PostMapping
    public FundraisingEvent createEvent(@RequestParam String name) {
        return fundraisingEventService.createEvent(name);
    }

    @GetMapping
    public List<FundraisingEventDto> getAllEvents() {
        return fundraisingEventService.getAllEvents().stream()
                .map(FundraisingEventMapper::toDto)
                .toList();
    }

    @GetMapping("/report")
    public List<FinancialReportDto> getFinancialReport() {
        return fundraisingEventService.getFinancialReport();
    }

}
