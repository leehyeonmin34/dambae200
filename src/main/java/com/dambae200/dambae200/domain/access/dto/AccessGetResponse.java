package com.dambae200.dambae200.domain.access.dto;

import com.dambae200.dambae200.domain.access.domain.Access;
import lombok.Getter;

@Getter
public class AccessGetResponse{
    private Long id;
    private String accessTypeCode;
    private String storeName;
    private String userNickname;


    public AccessGetResponse(Access access){
        this.id = access.getId();
        this.accessTypeCode = access.getAccessType().getCode();
        this.storeName = access.getStore().getFullname();
        this.userNickname = access.getUser().getNickname();
    }

}