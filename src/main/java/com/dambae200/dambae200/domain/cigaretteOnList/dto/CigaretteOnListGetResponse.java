package com.dambae200.dambae200.domain.cigaretteOnList.dto;

import com.dambae200.dambae200.domain.cigarette.domain.Cigarette;
import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import com.dambae200.dambae200.global.common.dto.BaseDto;
import lombok.Getter;

@Getter
public class CigaretteOnListGetResponse extends BaseDto {
    private String officialName;
    private String customizedName;
    private int count;
    private Long cigaretteId;
    private boolean vertical ;
    private String filePathMedium;
    private String filePathLarge;

    private int computerizedOrder;
    private int displayOrder;

    public CigaretteOnListGetResponse(CigaretteOnList cigaretteOnList) {

        Cigarette cigarette = cigaretteOnList.getCigarette();

        this.id = cigaretteOnList.getId();
        this.officialName = cigarette.getOfficialName();
        this.customizedName = cigaretteOnList.getCustomizedName();
        this.count = cigaretteOnList.getCount();
        this.cigaretteId = cigarette.getId();
        this.computerizedOrder = cigaretteOnList.getComputerizedOrder();
        this.displayOrder = cigaretteOnList.getDisplayOrder();
        this.vertical = cigarette.isVertical();
        this.filePathLarge = cigarette.getFilePathLarge();
        this.filePathMedium = cigarette.getFilePathMedium();
        this.createdAt = cigaretteOnList.getCreatedAt();
        this.updatedAt = cigaretteOnList.getUpdatedAt();
    }
}