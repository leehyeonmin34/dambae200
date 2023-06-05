package com.dambae200.dambae200.global.common.dto;

public interface EntityBasedDTO<DTO extends EntityBasedDTO, E extends BaseEntity> {
    default DTO of(E e){ return dontUseOf(e); }
    DTO dontUseOf(E e);

    default E toEntity(DTO dto){ return dontUseToEntity(dto);}
    E dontUseToEntity(DTO dto);
}
