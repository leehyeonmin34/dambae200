package com.dambae200.dambae200.domain.cigarette.service;

import com.dambae200.dambae200.domain.cigarette.domain.Cigarette;
import com.dambae200.dambae200.domain.cigarette.dto.CigaretteAddRequest;
import com.dambae200.dambae200.domain.cigarette.dto.CigaretteGetListResponse;
import com.dambae200.dambae200.domain.cigarette.dto.CigaretteGetResponse;
import com.dambae200.dambae200.domain.cigarette.dto.CigaretteUpdateRequest;
import com.dambae200.dambae200.domain.cigarette.exception.OfficaialNameDuplicationException;
import com.dambae200.dambae200.domain.cigarette.repository.CigaretteRepository;
import com.dambae200.dambae200.domain.cigaretteOnList.repository.CigaretteOnListRepository;
import com.dambae200.dambae200.global.cache.config.CacheEnvOld;
import com.dambae200.dambae200.global.cache.config.CacheType;
import com.dambae200.dambae200.global.cache.service.CacheableRepository;
import com.dambae200.dambae200.global.cache.service.HashCacheModule;
import com.dambae200.dambae200.global.common.dto.DeleteResponse;
import com.dambae200.dambae200.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CigaretteUpdateService {

    private final CigaretteRepository cigaretteRepository;
    private final CigaretteOnListRepository cigaretteOnListRepository;
    private final CacheableRepository<Cigarette, CigaretteGetResponse, Long, CigaretteRepository> cigaretteCacheableRepository;
    private final HashCacheModule hashCacheModule;

    @Transactional
    public CigaretteGetResponse addCigarette(final CigaretteAddRequest request){

        checkDuplicate(request.getOfficial_name());

        final Cigarette cigarette = createCigarette(request);

        cigaretteCacheableRepository.evict(0L); // 목록 캐시 evict
        final Cigarette saved = cigaretteRepository.save(cigarette);
        return new CigaretteGetResponse(saved);
    }

    @Transactional
    public CigaretteGetListResponse addAllCigarette(final List<CigaretteAddRequest> request){

        final List<Cigarette> cigarettes = new ArrayList<>();

        for(final CigaretteAddRequest requestItem : request) {
            checkDuplicate(requestItem.getOfficial_name());
            final Cigarette cigarette = createCigarette(requestItem);
            cigarettes.add(cigarette);
        }

        cigaretteCacheableRepository.evict(0L); // 목록 캐시 evict
        final List<Cigarette> saved = cigaretteRepository.saveAll(cigarettes);
        return CigaretteGetListResponse.of(saved);
    }


    private Cigarette createCigarette(CigaretteAddRequest request){
        return Cigarette.builder()
                .officialName(request.getOfficial_name())
                .simpleName(request.getCustomized_name())
                .filePathLarge(request.getFile_path_large())
                .filePathMedium(request.getFile_path_medium())
                .vertical(request.isVertical())
                .id(request.getId())
                .build();
    }

    @Transactional
    public CigaretteGetResponse updateCigarette(final Long id, final CigaretteUpdateRequest request){

        checkDuplicate(request.getOfficial_name());

        // TODO - DTO에 toEntity, toDto 메서드를 강제 정의할 순 없을까?
        final Cigarette cigarette = cigaretteCacheableRepository.getEntityCacheOrLoad(id);

        cigarette.updateCigarette(request.getOfficial_name(), request.getCustomized_name());

        cigaretteCacheableRepository.evict(0L); // 목록 캐시 evict
        Cigarette saved = cigaretteCacheableRepository.writeThrough(id, cigarette);

        return new CigaretteGetResponse(saved);
    }

    @Transactional
    public DeleteResponse deleteCigarette(final Long id) throws EntityNotFoundException {
        // 연관된 엔티티 제거
        Set<Long> relatedStoreIdSet = new HashSet<>();
        Set<Long> cigaretteOnListIdSet = new HashSet<>();

        cigaretteOnListRepository.findAllByCigaretteId(id)
                .forEach(item -> {
                    relatedStoreIdSet.add(item.getStore().getId());
                    cigaretteOnListIdSet.add(item.getId());
                });
        cigaretteOnListRepository.deleteAllById(cigaretteOnListIdSet);

        // 관련 캐시 제거
        relatedStoreIdSet.forEach(storeId
                -> hashCacheModule.evictAll(CacheType.CIGARETTE_LIST, storeId));

        // 목표 엔티티 제거
        cigaretteCacheableRepository.evict(0L); // 목록 캐시 evict
        cigaretteCacheableRepository.deleteThrough(id);
        return new DeleteResponse("cigarette", id);
    }

    private void checkDuplicate(String name) {
        if (cigaretteRepository.existsByOfficialName(name)) {
            throw new OfficaialNameDuplicationException();
        }
    }
}
