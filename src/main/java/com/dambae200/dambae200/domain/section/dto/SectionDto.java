package dambae200.dambae200.domain.section.dto;

import dambae200.dambae200.domain.section.domain.Section;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class SectionDto {

    @Getter
    static public class SectionRequest {
        @NotBlank(message = "섹션 이름을 입력해 주세요.")
        private String name;
    }

    @Getter
    @NoArgsConstructor
    static public class GetResponse{

        private Long id;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String name;
        private int order;

        public GetResponse(Section section) {
            id= section.getId();
            createdAt = section.getCreatedAt();
            updatedAt = section.getUpdatedAt();
            name = section.getName();
            order = section.getSectionOrder();
        }
    }

    @Getter
    static public class GetListResponse{

        private List<SectionDto.GetResponse> sections;
        private int total;

        public GetListResponse(List<Section> sections) {
            this.sections = sections.stream().map(GetResponse::new)
                    .collect(Collectors.toList());
            this.total = sections.size();
        }
    }
}
