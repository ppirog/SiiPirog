package com.sii.siipirog.mapper;

import com.sii.siipirog.dto.CollectionBoxDto;
import com.sii.siipirog.model.CollectionBox;

import java.math.BigDecimal;

public class CollectionBoxMapper {

    public static CollectionBoxDto toDto(CollectionBox box) {
        boolean isAssigned = box.getFundraisingEventId() != null;
        boolean isEmpty = box.getMoney().values().stream()
                .allMatch(amount -> amount.compareTo(BigDecimal.ZERO) == 0);

        return CollectionBoxDto.builder()
                .id(box.getId())
                .isAssigned(isAssigned)
                .isEmpty(isEmpty)
                .build();
    }
}
