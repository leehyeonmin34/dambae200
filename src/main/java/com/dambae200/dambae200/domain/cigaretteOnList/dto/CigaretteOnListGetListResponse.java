package com.dambae200.dambae200.domain.cigaretteOnList.dto;

import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@ToString
@NoArgsConstructor
public class CigaretteOnListGetListResponse implements Serializable{
    private List<CigaretteOnListGetResponse> cigaretteOnLists = new ArrayList<>();
    private int total = 0;

    public CigaretteOnListGetListResponse(List<CigaretteOnListGetResponse> cigaretteOnLists) {
        this.cigaretteOnLists = cigaretteOnLists;
        this.total = cigaretteOnLists.size();
    }

}