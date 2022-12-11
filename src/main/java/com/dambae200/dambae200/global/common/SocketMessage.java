package com.dambae200.dambae200.global.common;

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
