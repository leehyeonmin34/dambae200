package com.dambae200.dambae200.domain.cigaretteOnList.model;

import com.dambae200.dambae200.domain.cigaretteOnList.exception.InvalidOrderTypeException;
import com.dambae200.dambae200.domain.store.domain.StoreBrand;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum OrderType {
    DISPLAY("display"),
    COMPUTATIONAL("computational"),
    ;

    private String code;

    public OrderType of(String code){
        return Arrays.stream(OrderType.values())
                .filter(item -> item.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new InvalidOrderTypeException(code));
    }

}