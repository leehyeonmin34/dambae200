package com.dambae200.dambae200.domain.cigaretteOnList.dto;

import com.dambae200.dambae200.domain.cigarette.domain.Cigarette;
import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import com.dambae200.dambae200.global.common.BaseDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class CigaretteOnListDto{

    @Getter
    public static class UpdateCountRequest{
        @Nullable
        private int count;
    }

    @Getter
    public static class DragAndDropRequest{
        private Long afterElementId;
        private Long draggableId;
    }

    @Getter
    public static class ModifyRequest{
        private String customizedName;
    }

    @Getter
    static public class AddCigaretteOnList{
        private Long storeId;
        private Long cigaretteId;
        private String customizedName;
    }

    /*
    @Getter
    static public class ReorderRequest{
        private List<OrderInfo> orderInfos;
    }
    */

    @Getter
    static public class ReorderRequest{
        private Long id;
        private int display_order;
        private int computerized_order;
    }



    @Getter
    static public class GetCigaretteResponse extends BaseDto {
        private String officialName;
        private String customizedName;
        private int count;
        private Long cigaretteId;
        private boolean vertical ;
        private String filePathMedium;
        private String filePathLarge;

        private int computerizedOrder;
        private int displayOrder;

        public GetCigaretteResponse(CigaretteOnList cigaretteOnList) {
            Cigarette cigarette = cigaretteOnList.getCigarette();

            this.id = cigaretteOnList.getId();
            this.officialName = cigarette.getOfficialName();
            this.customizedName = cigaretteOnList.getCustomizedName();
            this.count = cigaretteOnList.getCount();
            this.cigaretteId = cigarette.getId();
            this.computerizedOrder = cigaretteOnList.getComputerizedOrder();
            this.displayOrder = cigaretteOnList.getDisplayOrder();
            this.vertical = cigarette.isVertical();
            this.filePathLarge = cigarette.getFilePathLarge();
            this.filePathMedium = cigarette.getFilePathMedium();
            this.createdAt = cigaretteOnList.getCreatedAt();
            this.updatedAt = cigaretteOnList.getUpdatedAt();
        }
    }

    @Getter
    public static class GetListCigaretteResponse {
        private List<GetCigaretteResponse> cigaretteOnLists = new ArrayList<>();
        private int total = 0;

        public GetListCigaretteResponse(List<CigaretteOnList> cigaretteOnList) {
            this.cigaretteOnLists = cigaretteOnList.stream()
                    .map(GetCigaretteResponse::new)
                    .collect(Collectors.toList());
            this.total = cigaretteOnLists.size();
        }
    }





}