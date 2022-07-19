package com.dambae200.dambae200.domain.store.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StoreBrandConverter implements AttributeConverter<StoreBrand, String> {
    @Override
    public String convertToDatabaseColumn(StoreBrand attribute) {
        return attribute != null ? attribute.getCode() : "-1";
    }

    @Override
    public StoreBrand convertToEntityAttribute(String dbData) {
        return dbData != null ? StoreBrand.ofCode(dbData) : null;
    }
}
