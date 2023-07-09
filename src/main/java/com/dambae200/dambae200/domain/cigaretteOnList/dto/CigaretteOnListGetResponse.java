package com.dambae200.dambae200.domain.cigaretteOnList.dto;

import com.dambae200.dambae200.domain.cigarette.domain.Cigarette;
import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import com.dambae200.dambae200.domain.store.domain.Store;
import com.dambae200.dambae200.global.common.dto.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
public class CigaretteOnListGetResponse extends BaseDto implements Serializable {
    private String officialName;
    private String customizedName;
    private String simpleName;
    private int count;
    private Long cigaretteId;
    private boolean vertical ;
    private String filePathMedium;
    private String filePathLarge;

    private int computerizedOrder;
    private int displayOrder;
    private Long storeId;

    public CigaretteOnListGetResponse(CigaretteOnList cigaretteOnList) {

        Cigarette cigarette = cigaretteOnList.getCigarette();

        // CIGARETTE_ON_LIST 정보
        this.id = cigaretteOnList.getId();
        this.customizedName = cigaretteOnList.getCustomizedName();
        this.count = cigaretteOnList.getCount();
        this.computerizedOrder = cigaretteOnList.getComputerizedOrder();
        this.displayOrder = cigaretteOnList.getDisplayOrder();
        this.createdAt = cigaretteOnList.getCreatedAt();
        this.updatedAt = cigaretteOnList.getUpdatedAt();
        this.storeId = cigaretteOnList.getStore().getId();


        // 연관 관계의 CIGARETTE 정보
        this.cigaretteId = cigarette.getId();
        this.officialName = cigarette.getOfficialName();
        this.vertical = cigarette.isVertical();
        this.filePathLarge = cigarette.getFilePathLarge();
        this.filePathMedium = cigarette.getFilePathMedium();
        this.simpleName = cigarette.getSimpleName();
    }

    public static CigaretteOnList toEntity(CigaretteOnListGetResponse dto) {


        return CigaretteOnList.builder()
                .id(dto.getId())
                .computerizedOrder(dto.getComputerizedOrder())
                .count(dto.getCount())
                .customizedName(dto.getCustomizedName())
                .displayOrder(dto.getDisplayOrder())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .cigarette(buildCigarette(dto))
                .store(buildStore(dto))
                .build();
    }

    private static Cigarette buildCigarette(CigaretteOnListGetResponse dto){
        return Cigarette.builder()
                .id(dto.getCigaretteId())
                .officialName(dto.getOfficialName())
                .vertical(dto.isVertical())
                .filePathMedium(dto.getFilePathMedium())
                .filePathLarge(dto.getFilePathLarge())
                .simpleName(dto.getSimpleName())
                .build();
    }

    private static Store buildStore(CigaretteOnListGetResponse dto){
        return new Store(dto.getStoreId());
    }


}