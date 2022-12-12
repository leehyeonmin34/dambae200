package com.dambae200.dambae200.domain.user.service.socket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class SocketMessage<T> {

    Long requestUserId;

    T content;

}
