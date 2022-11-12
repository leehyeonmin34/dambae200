package dambae200.dambae200.domain.cigaretteList.dto;

import dambae200.dambae200.domain.cigaretteList.domain.CigaretteList;
import dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import dambae200.dambae200.domain.cigaretteOnList.dto.CigaretteOnListDto;
import dambae200.dambae200.domain.store.dto.StoreDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CigaretteListDto {

    @Getter
    static public class UpdateRequest {
        @NotBlank(message = "목록 이름을 입력해주세요")
        private String name;
    }

    @Getter
    @NoArgsConstructor
    static public class GetResponse{
        private Long id;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String name;

        public GetResponse(CigaretteList cigaretteList) {
            id = cigaretteList.getId();
            createdAt = cigaretteList.getCreatedAt();
            updatedAt = cigaretteList.getUpdatedAt();
            name = cigaretteList.getName();
        }
    }

    @Getter
    static public class GetListResponse {
        private List<CigaretteListDto.GetResponse> cigaretteLists;
        private int total;

        public GetListResponse(List<CigaretteList> cigaretteLists) {
            this.cigaretteLists = cigaretteLists.stream()
                    .map(GetResponse::new).collect(Collectors.toList());
            this.total = cigaretteLists.size();
        }
    }


}
