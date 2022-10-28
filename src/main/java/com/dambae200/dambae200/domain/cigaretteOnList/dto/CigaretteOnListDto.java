package dambae200.dambae200.domain.cigaretteOnList.dto;

import dambae200.dambae200.domain.cigarette.domain.Cigarette;
import dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import dambae200.dambae200.domain.section.domain.Section;
import dambae200.dambae200.global.common.BaseDto;
import dambae200.dambae200.global.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class CigaretteOnListDto {

    @Getter
    public static class UpdateCountRequest{
        @NotBlank
        private int count;
    }

    @Getter
    static public class AddCigaretteOnList{
        private Long cigaretteListId;
        private Long cigaretteId;
    }



    @Getter
    static public class GetCigaretteResponse extends BaseDto {
        private Long id;
        private String officialName;
        private String customizedName;
        private int count;

        public GetCigaretteResponse(CigaretteOnList cigaretteOnList) {
            Cigarette cigarette = cigaretteOnList.getCigarette();

            this.id = cigaretteOnList.getId();
            this.officialName = cigarette.getOfficialName();
            this.customizedName = cigarette.getCustomizedName();
            this.count = cigaretteOnList.getCount();
            createdAt = cigaretteOnList.getCreatedAt();
            updateAt = cigaretteOnList.getUpdatedAt();
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

    @Getter
    static public class GetCigaretteByComputerizedOrderResponse extends BaseDto {
        private Long id;
        private String officialName;
        private String customizedName;
        private int count;

        public GetCigaretteByComputerizedOrderResponse(CigaretteOnList cigaretteOnList){
            Cigarette cigarette = cigaretteOnList.getCigarette();

            this.id = cigaretteOnList.getId();
            this.officialName = cigarette.getOfficialName();
            this.count = cigaretteOnList.getCount();
            createdAt = cigaretteOnList.getCreatedAt();
            updateAt = cigaretteOnList.getUpdatedAt();
        }
    }

    @Getter
    public static class GetListCigaretteByComputerizedOrderResponse{
        private List<GetCigaretteByComputerizedOrderResponse> cigaretteOnLists = new ArrayList<>();
        private int total = 0;

        public GetListCigaretteByComputerizedOrderResponse(List<CigaretteOnList> cigaretteOnList) {
            this.cigaretteOnLists = cigaretteOnList.stream()
                    .map(GetCigaretteByComputerizedOrderResponse::new)
                    .collect(Collectors.toList());
            this.total = cigaretteOnLists.size();
        }
    }

    @Getter
    public static class GetSectionResponse{
        private Long id;
        private String sectionName;

        public GetSectionResponse(CigaretteOnList cigaretteOnList){

        }
    }

    @Getter
    @Builder
    public static class GetSectionListReponse{
        private List<CigaretteOnListDto.GetSectionResponse> cigaretteOnLists;
        private int total;
    }



}






