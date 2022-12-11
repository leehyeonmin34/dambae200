package com.dambae200.dambae200.domain.cigaretteOnList.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CigaretteOnListReorderRequest {

    private Long storeId;
    private Long requestUserId;
    private List<OrderInfo> orderInfos;

    private int fromIdx;
    private int toIdx;
    private int insertBeforeCigarId;
    private int movedCigarId;
    private String orderTypeCode;


    @Getter
    public static class OrderInfo{
        private Long id;
        private int display_order;
        private int computerized_order;
    }
}
