package com.sii.siipirog.controller;

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
    public List<CollectionBox> getAllBoxes() {
        return collectionBoxService.getAllBoxes();
    }

    @DeleteMapping("/{id}")
    public void deleteBox(@PathVariable Long id) {
        collectionBoxService.deleteBox(id);
    }

    @PutMapping("/{boxId}/assign/{eventId}")
    public CollectionBox assignBoxToEvent(@PathVariable Long boxId, @PathVariable Long eventId) {
        return collectionBoxService.assignToEvent(boxId, eventId);
    }
}
