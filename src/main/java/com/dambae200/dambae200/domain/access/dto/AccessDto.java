package com.dambae200.dambae200.domain.access.dto;

import com.dambae200.dambae200.domain.access.domain.Access;
import com.dambae200.dambae200.domain.access.domain.AccessType;
import com.dambae200.dambae200.domain.notification.domain.Notification;
import com.dambae200.dambae200.domain.user.domain.User;
import com.dambae200.dambae200.domain.user.dto.UserDto;
import lombok.Getter;

import javax.validation.constraints.NotNull;
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
        private AccessType accessType;
        private String storeName;
        private String userNickname;


        public GetResponse(Access access){
            this.id = access.getId();
            this.accessType = access.getAccessType();
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
        private AccessType accessType;

        public GetUserResponse(Access access) {
            User user = access.getUser();

            this.id = access.getId();
            this.accessType = access.getAccessType();
            this.userId = user.getId();
            this.userNickname = user.getNickname();
            this.userEmail = user.getEmail();
        }
    }

    @Getter
    public static class GetListUserResponse{
        private List<AccessDto.GetUserResponse> users = new ArrayList<>();
        private int total = 0;

        public GetListUserResponse(List<Access> access){
            this.users = access.stream()
                    .map(AccessDto.GetUserResponse::new)
                    .collect(Collectors.toList());
            this.total = users.size();
        }

    }


}
