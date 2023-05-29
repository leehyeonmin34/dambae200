package com.dambae200.dambae200.global.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RedisDto {
    String key;
    int value;
}
