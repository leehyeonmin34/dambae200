package com.dambae200.dambae200.domain.cigaretteOnList.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CigaretteOnListUpdateCountRequest implements Serializable {

    private Long storeId;

    private Long cigarOnListId;

    private int num;

}
