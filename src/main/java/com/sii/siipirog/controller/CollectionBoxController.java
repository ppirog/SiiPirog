package com.sii.siipirog.controller;

import com.sii.siipirog.dto.AddMoneyRequestDto;
import com.sii.siipirog.dto.CollectionBoxDto;
import com.sii.siipirog.mapper.CollectionBoxMapper;
import com.sii.siipirog.model.CollectionBox;
import com.sii.siipirog.service.CollectionBoxService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boxes")
@RequiredArgsConstructor
public class CollectionBoxController {

    private final CollectionBoxService collectionBoxService;

    @PostMapping
    public CollectionBox registerBox() {
        return collectionBoxService.registerBox();
    }

    @GetMapping
    public List<CollectionBoxDto> getAllBoxes() {
        return collectionBoxService.getAllBoxes().stream()
                .map(CollectionBoxMapper::toDto)
                .toList();
    }

    @DeleteMapping("/{id}")
    public void deleteBox(@PathVariable Long id) {
        collectionBoxService.deleteBox(id);
    }

    @PutMapping("/{boxId}/assign/{eventId}")
    public CollectionBox assignBoxToEvent(@PathVariable Long boxId, @PathVariable Long eventId) {
        return collectionBoxService.assignToEvent(boxId, eventId);
    }

    @PutMapping("/{boxId}/add-money")
    public CollectionBox addMoney(@PathVariable Long boxId, @RequestBody AddMoneyRequestDto requestDto) {
        return collectionBoxService.addMoney(boxId, requestDto.getCurrency(), requestDto.getAmount());
    }

    @PutMapping("/{boxId}/transfer")
    public void transferMoney(@PathVariable Long boxId) {
        collectionBoxService.transferMoney(boxId);
    }

}
