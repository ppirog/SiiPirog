package com.sii.siipirog.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class CollectionBox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long fundraisingEventId;

    @ElementCollection
    private Map<String, BigDecimal> money = new HashMap<>();
}
