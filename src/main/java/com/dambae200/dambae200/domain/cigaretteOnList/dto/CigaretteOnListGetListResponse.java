package com.dambae200.dambae200.domain.cigaretteOnList.dto;

import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter
public class CigaretteOnListGetListResponse {
    private List<CigaretteOnListGetResponse> cigaretteOnLists = new ArrayList<>();
    private int total = 0;

    public CigaretteOnListGetListResponse(List<CigaretteOnList> cigaretteOnList) {
        this.cigaretteOnLists = cigaretteOnList.stream()
                .map(item -> new CigaretteOnListGetResponse(item))
                .collect(Collectors.toList());
        this.total = cigaretteOnLists.size();
    }
}