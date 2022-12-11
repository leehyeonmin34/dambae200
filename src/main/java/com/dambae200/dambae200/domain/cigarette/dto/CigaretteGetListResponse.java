package com.dambae200.dambae200.domain.cigarette.dto;

import com.dambae200.dambae200.domain.cigarette.domain.Cigarette;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CigaretteGetListResponse {

    private List<CigaretteGetResponse> cigarettes;
    private int total;

    public CigaretteGetListResponse(List<Cigarette> cigarettes) {
        this.cigarettes = cigarettes.stream().map(CigaretteGetResponse::new)
                .collect(Collectors.toList());
        this.total = cigarettes.size();
    }
}