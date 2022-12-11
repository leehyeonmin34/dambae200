package com.dambae200.dambae200.global.common;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class SocketRequest<T> {

    @NonNull
    Long requestUserId;

    @NonNull
    String responseChannel;

    T content;

}
