package com.dambae200.dambae200.domain.store.exception;

import com.dambae200.dambae200.global.error.exception.BusinessException;
import com.dambae200.dambae200.global.error.exception.ErrorCode;

public class DuplicateStoreException extends BusinessException {

    public DuplicateStoreException(String name, String brandCode) {
        super(ErrorCode.DUPLICATE_STORE, String.format("brand %s, storeName %s에 해당하는 store가 이미 존재합니다.",  brandCode, name));
    }

}
