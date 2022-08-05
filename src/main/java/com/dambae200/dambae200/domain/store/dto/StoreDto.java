package com.dambae200.dambae200.domain.store.dto;

import com.dambae200.dambae200.domain.access.domain.Access;
import com.dambae200.dambae200.domain.store.domain.Store;
import com.dambae200.dambae200.domain.store.domain.StoreBrand;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class StoreDto {

    @Getter
    static public class UpdateRequest{

        @NotBlank(message = "매장 이름을 입력해주세요")
        private String name;

        private String storeBrandCode;
    }

    @Getter
    static public class AddRequest extends UpdateRequest{
        @NotNull(message = "관리자 id가 누락되었습니다")
        private Long adminId;
    }

    @Getter
    @NoArgsConstructor
    static public class GetResponse{

        private Long id;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String name;
        private String brandCode;

        @Builder
        public GetResponse(Store store){
            id = store.getId();
            createdAt = store.getCreatedAt();
            updatedAt = store.getUpdatedAt();
            name = store.getName();
            brandCode = store.getBrand().getCode();
        }

    }

    @Getter
    static public class GetListResponse {

        private List<StoreDto.GetResponse> stores;
        private int total;

        public GetListResponse(List<Store> stores) {
            this.stores = stores.stream().map(GetResponse::new)
                    .collect(Collectors.toList());
            this.total = stores.size();
        }

    }




}
