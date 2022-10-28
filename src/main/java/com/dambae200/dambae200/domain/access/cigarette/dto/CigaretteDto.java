package dambae200.dambae200.domain.cigarette.dto;

import dambae200.dambae200.domain.cigarette.domain.Cigarette;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CigaretteDto {

    @Getter
    static public class CigaretteRequest {
        @NotBlank(message = "공식이름을 입력해주세요")
        private String officialName;

        @NotNull(message = "간편이름을 입력해주세요")
        private String customizedName;
    }


    @Getter
    @NoArgsConstructor
    static public class GetResponse {
        private Long id;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String officialName;


        public GetResponse(Cigarette cigarette) {
            id = cigarette.getId();
            createdAt = cigarette.getCreatedAt();
            updatedAt = cigarette.getUpdatedAt();
            officialName = cigarette.getOfficialName();
        }
    }

    @Getter
    static public class GetListResponse {

        private List<GetResponse> cigarettes;
        private int total;

        public GetListResponse(List<Cigarette> cigarettes) {
            this.cigarettes = cigarettes.stream().map(GetResponse::new)
                    .collect(Collectors.toList());
            this.total = cigarettes.size();
        }
    }
}
