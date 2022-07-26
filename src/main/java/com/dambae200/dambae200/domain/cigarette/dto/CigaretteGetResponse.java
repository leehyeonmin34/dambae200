package com.dambae200.dambae200.domain.cigarette.dto;

import com.dambae200.dambae200.domain.cigarette.domain.Cigarette;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CigaretteGetResponse {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String officialName;
    private String simpleName;
    private String filePathMedium;
    private String filePathLarge;
    private boolean vertical;


    public CigaretteGetResponse(Cigarette cigarette) {
        id = cigarette.getId();
        createdAt = cigarette.getCreatedAt();
        updatedAt = cigarette.getUpdatedAt();
        officialName = cigarette.getOfficialName();
        simpleName = cigarette.getSimpleName();
        filePathMedium = cigarette.getFilePathMedium();
        filePathLarge = cigarette.getFilePathLarge();
        vertical = cigarette.isVertical();
    }
}
