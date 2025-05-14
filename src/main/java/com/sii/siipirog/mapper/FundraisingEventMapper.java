package com.sii.siipirog.mapper;

import com.sii.siipirog.dto.FundraisingEventDto;
import com.sii.siipirog.model.FundraisingEvent;

public class FundraisingEventMapper {

    public static FundraisingEventDto toDto(FundraisingEvent event) {
        return new FundraisingEventDto(
                event.getId(),
                event.getName(),
                event.getCurrency(),
                event.getAccountBalance()
        );
    }
}
