package com.dambae200.dambae200.domain.store.exception;

import com.dambae200.dambae200.global.error.exception.BusinessException;
import com.dambae200.dambae200.global.error.exception.ErrorCode;
import com.dambae200.dambae200.global.error.exception.InvalidEnumCodeException;

public class InvalidStoreBrandCode extends InvalidEnumCodeException {

    public InvalidStoreBrandCode(String code) {
        super(ErrorCode.INVALID_STORE_BRAND_CODE, "Store", code);
    }
}
