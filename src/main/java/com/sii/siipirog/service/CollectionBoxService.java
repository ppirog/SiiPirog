package com.sii.siipirog.service;

import com.sii.siipirog.exception.ErrorMessageException;
import com.sii.siipirog.model.CollectionBox;
import com.sii.siipirog.model.FundraisingEvent;
import com.sii.siipirog.repository.CollectionBoxRepository;
import com.sii.siipirog.repository.FundraisingEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CollectionBoxService {

    private final CollectionBoxRepository collectionBoxRepository;
    private final FundraisingEventRepository fundraisingEventRepository;

    public CollectionBox registerBox() {
        CollectionBox box = new CollectionBox();
        return collectionBoxRepository.save(box);
    }

    public List<CollectionBox> getAllBoxes() {
        return collectionBoxRepository.findAll();
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

        boolean eventExists = fundraisingEventRepository.existsById(eventId);
        if (!eventExists) {
            throw new ErrorMessageException("FundraisingEvent not found");
        }

        box.setFundraisingEventId(eventId);
        return collectionBoxRepository.save(box);
    }


    public void transferMoney(Long boxId) {
        CollectionBox box = collectionBoxRepository.findById(boxId)
                .orElseThrow(() -> new ErrorMessageException("CollectionBox not found"));

        if (box.getFundraisingEventId() == null) {
            throw new ErrorMessageException("CollectionBox is not assigned to any event");
        }

        FundraisingEvent event = fundraisingEventRepository.findById(box.getFundraisingEventId())
                .orElseThrow(() -> new ErrorMessageException("FundraisingEvent not found"));

        BigDecimal totalPLN = BigDecimal.ZERO;

        for (Map.Entry<String, BigDecimal> entry : box.getMoney().entrySet()) {
            String currency = entry.getKey();
            BigDecimal amount = entry.getValue();

            if (amount.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal rateToPLN = EXCHANGE_RATES.getOrDefault(currency, BigDecimal.ONE);
                BigDecimal converted = amount.multiply(rateToPLN);
                totalPLN = totalPLN.add(converted);
            }
        }

        event.setAccountBalance(event.getAccountBalance().add(totalPLN));

        box.getMoney().replaceAll((k, v) -> BigDecimal.ZERO);

        collectionBoxRepository.save(box);
        fundraisingEventRepository.save(event);
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


    private static final Map<String, BigDecimal> EXCHANGE_RATES = Map.ofEntries(
            Map.entry("USD", BigDecimal.valueOf(4.00)),
            Map.entry("EUR", BigDecimal.valueOf(4.30)),
            Map.entry("GBP", BigDecimal.valueOf(5.00)),
            Map.entry("CHF", BigDecimal.valueOf(4.50)),
            Map.entry("JPY", BigDecimal.valueOf(0.027)),
            Map.entry("AUD", BigDecimal.valueOf(2.70)),
            Map.entry("CAD", BigDecimal.valueOf(3.00)),
            Map.entry("SEK", BigDecimal.valueOf(0.38)),
            Map.entry("NOK", BigDecimal.valueOf(0.40)),
            Map.entry("DKK", BigDecimal.valueOf(0.58)),
            Map.entry("CZK", BigDecimal.valueOf(0.18)),
            Map.entry("HUF", BigDecimal.valueOf(0.0115)),
            Map.entry("CNY", BigDecimal.valueOf(0.55)),
            Map.entry("INR", BigDecimal.valueOf(0.048)),
            Map.entry("BRL", BigDecimal.valueOf(0.85)),
            Map.entry("ZAR", BigDecimal.valueOf(0.22)),
            Map.entry("MXN", BigDecimal.valueOf(0.24)),
            Map.entry("SGD", BigDecimal.valueOf(3.00)),
            Map.entry("HKD", BigDecimal.valueOf(0.52)),
            Map.entry("NZD", BigDecimal.valueOf(2.50)),
            Map.entry("TRY", BigDecimal.valueOf(0.13)),
            Map.entry("ILS", BigDecimal.valueOf(1.20)),
            Map.entry("RUB", BigDecimal.valueOf(0.045)),
            Map.entry("KRW", BigDecimal.valueOf(0.0032)),
            Map.entry("AED", BigDecimal.valueOf(1.10)),
            Map.entry("SAR", BigDecimal.valueOf(1.08)),
            Map.entry("MYR", BigDecimal.valueOf(0.95)),
            Map.entry("THB", BigDecimal.valueOf(0.12)),
            Map.entry("PHP", BigDecimal.valueOf(0.075)),
            Map.entry("IDR", BigDecimal.valueOf(0.00027)),
            Map.entry("ARS", BigDecimal.valueOf(0.0045)),
            Map.entry("CLP", BigDecimal.valueOf(0.0052)),
            Map.entry("EGP", BigDecimal.valueOf(0.13)),
            Map.entry("VND", BigDecimal.valueOf(0.00017))
    );

}
