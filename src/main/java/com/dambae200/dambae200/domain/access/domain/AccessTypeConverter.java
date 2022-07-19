package com.dambae200.dambae200.domain.access.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class AccessTypeConverter implements AttributeConverter<AccessType, String> {

    @Override
    public String convertToDatabaseColumn(AccessType attribute) {
        return attribute.getCode() != null ? attribute.getCode() : null;
    }

    @Override
    public AccessType convertToEntityAttribute(String dbData) {

        return dbData != null ? AccessType.ofCode(dbData) : null;
    }
}
