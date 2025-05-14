package com.sii.siipirog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class FinancialReportDto {
    private String eventName;
    private BigDecimal amount;
    private String currency;
}
