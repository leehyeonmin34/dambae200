package com.dambae200.dambae200.domain.user.dto;

import com.dambae200.dambae200.domain.access.domain.Access;
import com.dambae200.dambae200.domain.user.domain.User;
import com.dambae200.dambae200.global.common.BaseDto;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDto {

    @Getter
    public static class AddRequest{
        @NotBlank(message = "이메일을 입력해주세요")
        private String email;
        @Pattern(regexp = "^[A-Za-z0-9]{6,20}$",message = "비밀번호는 영문과 숫자를 포함해 6자 ~ 20자 이상이어야 합니다.")
        private String pw;
        @NotBlank
        private String nickname;
    }

    @Getter
    public static class UpdateRequest{
        @NotBlank
        private String nickname;
    }

    @Getter
    public static class ChangePasswordRequest{
        @NotBlank
        private String oldPw;
        @Pattern(regexp = "^[A-Za-z0-9]{6,20}$",message = "비밀번호는 영문과 숫자를 포함해 6자 ~ 20자 이상이어야 합니다.")
        private String newPw;
    }

    @Getter
    public static class GetResponse extends BaseDto {
        private String email;
        private String nickname;

        public GetResponse(User user){
            id = user.getId();
            createdAt = user.getCreatedAt();
            updatedAt = user.getUpdatedAt();
            email = user.getEmail();
            nickname = user.getNickname();
        }

    }

    @Getter
    public static class GetListResponse{
        private List<UserDto.GetResponse> users = new ArrayList<>();
        private int total = 0;

        public GetListResponse(List<User> users){
            this.users = users.stream()
                    .map(UserDto.GetResponse::new)
                    .collect(Collectors.toList());
            this.total = users.size();
        }

    }


}
