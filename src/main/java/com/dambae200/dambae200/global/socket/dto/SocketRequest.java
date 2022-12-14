package com.dambae200.dambae200.global.socket.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SocketRequest<T> {

    @NonNull
    Long requestUserId;

    @NonNull
    String responseChannel;

    T content;

}
