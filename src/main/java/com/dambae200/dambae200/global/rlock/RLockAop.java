package com.dambae200.dambae200.global.rlock;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RLockAop {
    RLockType type(); // RLock에 사용되는 정보
}
