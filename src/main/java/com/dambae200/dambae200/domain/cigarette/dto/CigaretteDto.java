package com.dambae200.dambae200.domain.cigarette.dto;

import com.dambae200.dambae200.domain.cigarette.domain.Cigarette;
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
        private String official_name;

        @NotNull(message = "간편이름을 입력해주세요")
        private String customized_name;

        private Long id;

        private String file_path_medium;

        private String file_path_large;

        private boolean vertical;
    }


    @Getter
    @NoArgsConstructor
    static public class GetResponse {
        private Long id;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String officialName;
        private String simpleName;
        private String filePathMedium;
        private String filePathLarge;
        private boolean vertical;


        public GetResponse(Cigarette cigarette) {
            id = cigarette.getId();
            createdAt = cigarette.getCreatedAt();
            updatedAt = cigarette.getUpdatedAt();
            officialName = cigarette.getOfficialName();
            simpleName = cigarette.getSimpleName();
            filePathMedium = cigarette.getFilePathMedium();
            filePathLarge = cigarette.getFilePathLarge();
            vertical = cigarette.isVertical();
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
