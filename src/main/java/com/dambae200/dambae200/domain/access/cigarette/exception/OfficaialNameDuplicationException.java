package dambae200.dambae200.domain.cigarette.exception;

import dambae200.dambae200.global.error.exception.BusinessException;
import dambae200.dambae200.global.error.exception.ErrorCode;

public class OfficaialNameDuplicationException extends BusinessException {

    public OfficaialNameDuplicationException() {
        super(ErrorCode.OFFICIAL_DUPLICATION,"중복된 공식이름입니다.");
    }
}
