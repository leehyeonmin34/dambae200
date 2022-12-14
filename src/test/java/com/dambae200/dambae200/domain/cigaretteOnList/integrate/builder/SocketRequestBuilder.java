package com.dambae200.dambae200.domain.cigaretteOnList.integrate.builder;

import com.dambae200.dambae200.global.socket.dto.SocketRequest;

public class SocketRequestBuilder {
    public static <T> SocketRequest<T> build(T content){
        return SocketRequest.<T>builder()
                .requestUserId(7L)
                .responseChannel("/user/sub/store/1089/cigar_num")
                .content(content)
                .build();
    }
}
