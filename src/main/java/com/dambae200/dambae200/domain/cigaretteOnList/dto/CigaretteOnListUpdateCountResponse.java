package com.dambae200.dambae200.domain.cigaretteOnList.dto;

import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import lombok.Getter;

@Getter
public class CigaretteOnListUpdateCountResponse {

    private Long id;

    private int count;

    public CigaretteOnListUpdateCountResponse(CigaretteOnList cigaretteOnList){
        this.id = cigaretteOnList.getId();
        this.count = cigaretteOnList.getCount();
    }
}
