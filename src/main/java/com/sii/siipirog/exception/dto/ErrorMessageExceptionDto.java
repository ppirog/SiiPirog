package com.sii.siipirog.exception.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessageExceptionDto {
    private String message;
}
