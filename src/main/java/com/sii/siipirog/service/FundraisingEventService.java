package com.sii.siipirog.service;

import com.sii.siipirog.model.FundraisingEvent;
import com.sii.siipirog.repository.FundraisingEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FundraisingEventService {

    private final FundraisingEventRepository fundraisingEventRepository;

    public FundraisingEvent createEvent(String name) {
        FundraisingEvent event = new FundraisingEvent();
        event.setName(name);
        event.setCurrency("PLN");
        event.setAccountBalance(BigDecimal.ZERO);
        return fundraisingEventRepository.save(event);
    }

    public List<FundraisingEvent> getAllEvents() {
        return fundraisingEventRepository.findAll();
    }

    public Optional<FundraisingEvent> getEventById(Long id) {
        return fundraisingEventRepository.findById(id);
    }
}