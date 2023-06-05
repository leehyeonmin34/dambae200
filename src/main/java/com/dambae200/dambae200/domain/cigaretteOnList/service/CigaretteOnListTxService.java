package com.dambae200.dambae200.domain.cigaretteOnList.service;

import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import com.dambae200.dambae200.domain.cigaretteOnList.dto.CigaretteOnListGetResponse;
import com.dambae200.dambae200.domain.cigaretteOnList.exception.DuplicateCigaretteOnListException;
import com.dambae200.dambae200.domain.cigaretteOnList.repository.CigaretteOnListRepository;
import com.dambae200.dambae200.global.cache.config.CacheType;
import com.dambae200.dambae200.global.cache.service.HashCacheableRepository;
import com.dambae200.dambae200.global.rlock.RLockAop;
import com.dambae200.dambae200.global.rlock.RLockType;
import com.dambae200.dambae200.global.cache.service.HashCacheModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CigaretteOnListTxService {

    private final HashCacheModule hashCacheModule;
    private final CigaretteOnListRepository cigaretteOnListRepository;
    private final HashCacheableRepository<CigaretteOnList, CigaretteOnListGetResponse, Long, Long, CigaretteOnListRepository> cigaretteOnListCacheableRepository;

    @RLockAop(type = RLockType.CIGARETTE_LIST)
    public CigaretteOnList addIfNotExist(String key, Long storeId, Long cigaretteId, CigaretteOnList cigaretteOnList){
        checkDuplicate(storeId, cigaretteId);
        CigaretteOnList saved = writeThrough(storeId, cigaretteOnList);
        return saved;
    }

    private void checkDuplicate(Long storeId, Long cigaretteId) {
        CigaretteOnListGetResponse cached = cigaretteOnListCacheableRepository.get(storeId, cigaretteId);
        if (cached == null) {
            boolean saved = cigaretteOnListRepository.existsByStoreIdAndCigaretteId(storeId, cigaretteId);
            if (!saved) {
                throw new DuplicateCigaretteOnListException(storeId, cigaretteId);
            }
        }
    }

    private CigaretteOnList writeThrough(Long storeId, CigaretteOnList modified){
        return cigaretteOnListCacheableRepository.writeThrough(storeId, modified);
    }

}
