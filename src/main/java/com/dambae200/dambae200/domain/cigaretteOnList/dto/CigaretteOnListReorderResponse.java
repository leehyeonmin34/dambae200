package com.dambae200.dambae200.domain.cigaretteOnList.dto;

import lombok.Getter;

@Getter
public class CigaretteOnListReorderResponse {

    private Long requestUserId;

    private Long storeId;

    private int fromIdx;
    private int toIdx;
    private int insertBeforeCigarId;
    private int movedCigarId;
    private String orderTypeCode;

    public CigaretteOnListReorderResponse(CigaretteOnListReorderRequest request){
        this.requestUserId = request.getRequestUserId();
        this.storeId = request.getStoreId();
        this.fromIdx = request.getFromIdx();
        this.toIdx = request.getToIdx();
        this.insertBeforeCigarId = request.getInsertBeforeCigarId();
        this.movedCigarId = request.getMovedCigarId();
        this.orderTypeCode = request.getOrderTypeCode();
    }
}
