package com.dambae200.dambae200.domain.store.exception;

import com.dambae200.dambae200.global.error.exception.DuplicatedEntityException;
import com.dambae200.dambae200.global.error.exception.ErrorCode;

public class DuplicatedStoreException extends DuplicatedEntityException {
    public DuplicatedStoreException(String name, String brandCode) {
        super(ErrorCode.DUPLICATE_STORE, String.format("brand %s, storeName %s에 해당하는 store가 이미 존재합니다.",  brandCode, name));
    }
}
