package com.dambae200.dambae200.global.rlock;

import com.dambae200.dambae200.global.error.exception.RLockTimeoutException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Order(1)
@Aspect
@Component
@RequiredArgsConstructor
public class RLockAspect {
    private final RedissonClient redissonClient;

    @Around("@annotation(RLockAop) && args(key, ..)") // 첫번째 파라미터가 key여야 함
    public Object aroundRLock(ProceedingJoinPoint joinPoint, String key) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        RLockAop rlockAop = methodSignature.getMethod().getAnnotation(RLockAop.class);
        RLockType lockType = rlockAop.type();
        String lockKey = lockType.getLockKey(key);
        RLock lock = redissonClient.getLock(lockKey);

        // 락 획득
        // 락 타입별로 타임아웃 정보를 lockType enum에 적어 놓았음
        if (!lock.tryLock(lockType.getWaitSeconds(), lockType.getLeaseSeconds(), TimeUnit.SECONDS)) {
            throw new RLockTimeoutException(); // 타임아웃되면 클라이언트가 재시도하게끔 유도
        }
        Object ret = null;
        try {
            ret = joinPoint.proceed();
        } finally {
            if (lock != null && lock.isLocked())
                lock.unlock(); // 락 놓기
        }

        return ret;
//        return joinPoint.proceed();
    }

}