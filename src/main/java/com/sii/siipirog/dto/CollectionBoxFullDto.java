package com.sii.siipirog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@AllArgsConstructor
public class CollectionBoxFullDto {
    private Long id;
    private Long fundraisingEventId;
    private Map<String, BigDecimal> money;
}
