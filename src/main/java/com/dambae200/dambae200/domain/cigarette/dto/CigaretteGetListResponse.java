package com.dambae200.dambae200.domain.cigarette.dto;

import com.dambae200.dambae200.domain.cigarette.domain.Cigarette;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@ToString
public class CigaretteGetListResponse implements Serializable {

    private List<CigaretteGetResponse> cigarettes;
    private int total;

    public CigaretteGetListResponse(List<CigaretteGetResponse> cigarettes) {
        this.cigarettes = cigarettes;
        this.total = cigarettes.size();
    }

    public static CigaretteGetListResponse of(List<Cigarette> cigarettes) {
        List<CigaretteGetResponse> cigars = cigarettes.stream().map(CigaretteGetResponse::new).collect(Collectors.toList());
        return new CigaretteGetListResponse(cigars);
    }

}