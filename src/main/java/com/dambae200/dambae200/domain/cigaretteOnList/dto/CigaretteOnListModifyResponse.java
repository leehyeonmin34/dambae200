package com.dambae200.dambae200.domain.cigaretteOnList.dto;

import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import lombok.Getter;

@Getter
public class CigaretteOnListModifyResponse {

    private Long id;

    private String customizedName;

    public CigaretteOnListModifyResponse(CigaretteOnList cigaretteOnList){
        this.id = cigaretteOnList.getId();
        this.customizedName = cigaretteOnList.getCustomizedName();
    }

}
