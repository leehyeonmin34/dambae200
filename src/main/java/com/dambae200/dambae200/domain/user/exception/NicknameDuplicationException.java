package com.dambae200.dambae200.domain.user.exception;

import com.dambae200.dambae200.global.error.exception.BusinessException;
import com.dambae200.dambae200.global.error.exception.ErrorCode;

public class NicknameDuplicationException extends BusinessException {


    public NicknameDuplicationException() {
        super(ErrorCode.NICKNAME_DUPLICATION, "중복된 닉네임입니다.");
    }
}
