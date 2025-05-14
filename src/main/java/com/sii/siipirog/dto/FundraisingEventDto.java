package com.sii.siipirog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class FundraisingEventDto {
    private Long id;
    private String name;
    private String currency;
    private BigDecimal accountBalance;
}
