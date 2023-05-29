package com.dambae200.dambae200.domain.cigaretteOnList.service;

import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import com.dambae200.dambae200.domain.cigaretteOnList.exception.DuplicateCigaretteOnListException;
import com.dambae200.dambae200.domain.cigaretteOnList.repository.CigaretteOnListRepository;
import com.dambae200.dambae200.global.cache.config.CacheType;
import com.dambae200.dambae200.global.cache.rlock.RLockAop;
import com.dambae200.dambae200.global.cache.rlock.RLockType;
import com.dambae200.dambae200.global.cache.service.HashCacheModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CigaretteOnListTxService {

    private final HashCacheModule hashCacheModule;
    private final CigaretteOnListRepository cigaretteOnListRepository;

    @RLockAop(type = RLockType.CIGARETTE_LIST)
    public CigaretteOnList addIfNotExist(String key, Long storeId, Long cigaretteId, CigaretteOnList cigaretteOnList){
        checkDuplicate(storeId, cigaretteId);
        CigaretteOnList saved = writeThrough(storeId, cigaretteOnList);
        return saved;
    }

    private void checkDuplicate(Long storeId, Long cigaretteId) {
        boolean exist = hashCacheModule.getCacheOrLoad(CacheType.CIGARETTE_LIST, storeId, cigaretteId,
                (_id) -> cigaretteOnListRepository.existsByStoreIdAndCigaretteId(storeId, cigaretteId));
        if (exist) {
            throw new DuplicateCigaretteOnListException(storeId, cigaretteId);
        }
    }

    private CigaretteOnList writeThrough(Long storeId, CigaretteOnList modified){
        return hashCacheModule.writeThrough(CacheType.CIGARETTE_LIST, storeId, modified, CigaretteOnList::getId, cigaretteOnListRepository::save);
    }

}
