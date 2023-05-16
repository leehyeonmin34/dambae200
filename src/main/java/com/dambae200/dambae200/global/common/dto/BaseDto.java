package com.dambae200.dambae200.global.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class BaseDto {
    protected Long id;

    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
}
