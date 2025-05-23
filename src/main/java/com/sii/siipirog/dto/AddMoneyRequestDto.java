package com.sii.siipirog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AddMoneyRequestDto {
    private String currency;
    private BigDecimal amount;
}
