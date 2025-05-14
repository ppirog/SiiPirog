package com.sii.siipirog.controller;

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
    public FundraisingEvent createEvent(@RequestParam String name, @RequestParam String currency) {
        return fundraisingEventService.createEvent(name, currency);
    }

    @GetMapping
    public List<FundraisingEvent> getAllEvents() {
        return fundraisingEventService.getAllEvents();
    }
}
