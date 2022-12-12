package com.dambae200.dambae200.global.common;

import lombok.Getter;

@Getter
public class StandardRequest<T> {
    String accessToken;
    T requestDto;
}
