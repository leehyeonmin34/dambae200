package com.dambae200.dambae200.domain.user.dto;

import com.dambae200.dambae200.domain.access.dto.AccessGetStoreListResponse;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserHomeDto{
    boolean newNotification;
    AccessGetStoreListResponse myStores;
}
