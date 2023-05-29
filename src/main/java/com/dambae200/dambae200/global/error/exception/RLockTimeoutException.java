package com.dambae200.dambae200.global.error.exception;

public class RLockTimeoutException extends BusinessException{
    public RLockTimeoutException() {
        super(ErrorCode.RLOCK_TIMEOUT);
    }

}
