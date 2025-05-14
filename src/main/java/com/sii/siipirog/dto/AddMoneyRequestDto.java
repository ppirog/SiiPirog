package com.sii.siipirog.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddMoneyRequestDto {
    private String currency;
    private BigDecimal amount;
}
