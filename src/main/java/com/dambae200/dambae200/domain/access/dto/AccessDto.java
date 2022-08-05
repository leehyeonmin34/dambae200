package com.dambae200.dambae200.domain.access.dto;

import com.dambae200.dambae200.domain.access.domain.Access;
import com.dambae200.dambae200.domain.access.domain.AccessType;
import com.dambae200.dambae200.domain.notification.domain.Notification;
import com.dambae200.dambae200.domain.store.domain.Store;
import com.dambae200.dambae200.domain.store.dto.StoreDto;
import com.dambae200.dambae200.domain.user.domain.User;
import com.dambae200.dambae200.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccessDto {

    @Getter
    static public class ApplyRequest{
        @NotNull(message = "storeId가 누락되었습니다.")
        private Long storeId;
        @NotNull(message = "userId가 누락되었습니다.")
        private Long userId;
    }


    @Getter
    static public class GetResponse{
        private Long id;
        private String accessTypeCode;
        private String storeName;
        private String userNickname;


        public GetResponse(Access access){
            this.id = access.getId();
            this.accessTypeCode = access.getAccessType().getCode();
            this.storeName = access.getStore().getFullname();
            this.userNickname = access.getUser().getNickname();
        }

    }

    @Getter
    static public class GetUserResponse {
        private Long id;
        private Long userId;
        private String userNickname;
        private String userEmail;
        private String accessTypeCode;

        public GetUserResponse(Access access) {
            User user = access.getUser();

            this.id = access.getId();
            this.accessTypeCode = access.getAccessType().getCode();
            this.userId = user.getId();
            this.userNickname = user.getNickname();
            this.userEmail = user.getEmail();
        }
    }

    @Getter
    public static class GetListUserResponse{
        private List<AccessDto.GetUserResponse> accesses = new ArrayList<>();
        private int total = 0;

        public GetListUserResponse(List<Access> access){
            this.accesses = access.stream()
                    .map(AccessDto.GetUserResponse::new)
                    .collect(Collectors.toList());
            this.total = accesses.size();
        }

    }

    @Getter
    public static class GetStoreResponse{
        private Long id;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        String accessTypeCode;
        private String storeName;
        private String brandCode;
        private Long storeId;
        private boolean applicatorExists;

        @Builder
        public GetStoreResponse(Access access, boolean applicatorExists){
            Store store = access.getStore();
            id = access.getId();
            createdAt = store.getCreatedAt();
            updatedAt = store.getUpdatedAt();
            storeName = store.getName();
            brandCode = store.getBrand().getCode();
            accessTypeCode = access.getAccessType().getCode();
            storeId = store.getId();
            this.applicatorExists = applicatorExists;
        }
    }

    @Getter
    @Builder
    static public class GetStoreListResponse{
        private List<AccessDto.GetStoreResponse> accesses;
        private int total;

//        public GetStoreListResponse(List<Access> accesses) {
//            this.accesses = accesses.stream().map(GetStoreResponse::new)
//                    .collect(Collectors.toList());
//            this.total = accesses.size();
//        }
    }




}
