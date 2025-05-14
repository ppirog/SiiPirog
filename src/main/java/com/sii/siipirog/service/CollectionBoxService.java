package com.sii.siipirog.service;

import com.sii.siipirog.exception.ErrorMessageException;
import com.sii.siipirog.model.CollectionBox;
import com.sii.siipirog.repository.CollectionBoxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CollectionBoxService {

    private final CollectionBoxRepository collectionBoxRepository;

    public CollectionBox registerBox() {
        CollectionBox box = new CollectionBox();
        return collectionBoxRepository.save(box);
    }

    public List<CollectionBox> getAllBoxes() {
        return collectionBoxRepository.findAll();
    }

    public Optional<CollectionBox> getBoxById(Long id) {
        return collectionBoxRepository.findById(id);
    }

    public void deleteBox(Long id) {
        collectionBoxRepository.deleteById(id);
    }

    public CollectionBox assignToEvent(Long boxId, Long eventId) {
        CollectionBox box = collectionBoxRepository.findById(boxId)
                .orElseThrow(() -> new ErrorMessageException("CollectionBox not found"));

        if (box.getFundraisingEventId() != null) {
            throw new ErrorMessageException("CollectionBox is already assigned to an event");
        }

        if (!box.getMoney().isEmpty() && box.getMoney().values().stream().anyMatch(amount -> amount.compareTo(BigDecimal.ZERO) > 0)) {
            throw new ErrorMessageException("CollectionBox is not empty. Cannot assign to event.");
        }

        box.setFundraisingEventId(eventId);
        return collectionBoxRepository.save(box);
    }

    public CollectionBox addMoney(Long boxId, String currency, BigDecimal amount) {
        CollectionBox box = collectionBoxRepository.findById(boxId)
                .orElseThrow(() -> new ErrorMessageException("CollectionBox not found"));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ErrorMessageException("Amount must be positive");
        }

        BigDecimal current = box.getMoney().getOrDefault(currency, BigDecimal.ZERO);
        box.getMoney().put(currency, current.add(amount));

        return collectionBoxRepository.save(box);
    }

    public CollectionBox save(CollectionBox box) {
        return collectionBoxRepository.save(box);
    }
}
