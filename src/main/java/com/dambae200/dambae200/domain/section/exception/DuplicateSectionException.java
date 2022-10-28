package dambae200.dambae200.domain.section.exception;

import dambae200.dambae200.global.error.exception.BusinessException;
import dambae200.dambae200.global.error.exception.ErrorCode;

public class DuplicateSectionException extends BusinessException {

    public DuplicateSectionException() {
        super(ErrorCode.DUPLICATE_SECTION,"해당이름의 섹션이 이미 존재합니다.");
    }
}


