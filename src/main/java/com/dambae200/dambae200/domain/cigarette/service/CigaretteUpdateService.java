package com.dambae200.dambae200.domain.cigarette.service;

import com.dambae200.dambae200.domain.cigarette.domain.Cigarette;
import com.dambae200.dambae200.domain.cigarette.dto.CigaretteAddRequest;
import com.dambae200.dambae200.domain.cigarette.dto.CigaretteGetListResponse;
import com.dambae200.dambae200.domain.cigarette.dto.CigaretteGetResponse;
import com.dambae200.dambae200.domain.cigarette.dto.CigaretteUpdateRequest;
import com.dambae200.dambae200.domain.cigarette.exception.OfficaialNameDuplicationException;
import com.dambae200.dambae200.domain.cigarette.repository.CigaretteRepository;
import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import com.dambae200.dambae200.domain.cigaretteOnList.repository.CigaretteOnListRepository;
import com.dambae200.dambae200.global.cache.config.CacheEnv;
import com.dambae200.dambae200.global.cache.service.CacheModule;
import com.dambae200.dambae200.global.cache.service.CacheableRepository;
import com.dambae200.dambae200.global.cache.service.HashCacheModule;
import com.dambae200.dambae200.global.common.dto.DeleteResponse;
import com.dambae200.dambae200.global.common.service.RepoUtils;
import com.dambae200.dambae200.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CigaretteUpdateService {

    private final CigaretteRepository cigaretteRepository;
    private final CigaretteOnListRepository cigaretteOnListRepository;
    private final CacheableRepository<Long, Cigarette, CigaretteRepository> cigaretteCacheableRepository;
    private final HashCacheModule hashCacheModule;
//    private final CacheableRepository<Long, CigaretteOnList, CigaretteOnListRepository> cigaretteOnListCacheableRepository;
    private final RepoUtils repoUtils;

    public CigaretteGetResponse addCigarette(CigaretteAddRequest request){

        checkDuplicate(request.getOfficial_name());

        Cigarette cigarette = Cigarette.builder()
                .officialName(request.getOfficial_name())
                .simpleName(request.getCustomized_name())
                .filePathLarge(request.getFile_path_large())
                .filePathMedium(request.getFile_path_medium())
                .vertical(request.isVertical())
                .id(request.getId())
                .build();

        cigaretteCacheableRepository.evict(0L); // 목록 evict
        Cigarette saved = cigaretteRepository.save(cigarette);
        return new CigaretteGetResponse(saved);
    }

    public CigaretteGetListResponse addAllCigarette(List<CigaretteAddRequest> request){

        List<Cigarette> cigarettes = new ArrayList<>();

        for(CigaretteAddRequest requestItem : request) {
            checkDuplicate(requestItem.getOfficial_name());

            Cigarette cigarette = Cigarette.builder()
                    .officialName(requestItem.getOfficial_name())
                    .simpleName(requestItem.getCustomized_name())
                    .filePathLarge(requestItem.getFile_path_large())
                    .filePathMedium(requestItem.getFile_path_medium())
                    .vertical(requestItem.isVertical())
                    .id(requestItem.getId())
                    .build();
            cigarettes.add(cigarette);
        }

        cigaretteCacheableRepository.evict(0L); // 목록 evict
        List<Cigarette> saved = cigaretteRepository.saveAll(cigarettes);
        return new CigaretteGetListResponse(saved);
    }

    public CigaretteGetResponse updateCigarette(Long id, CigaretteUpdateRequest request){

        checkDuplicate(request.getOfficial_name());

        Cigarette cigarette = cigaretteCacheableRepository.getCacheOrLoad(id);
        cigarette.updateCigarette(request.getOfficial_name(), request.getCustomized_name());

        cigaretteCacheableRepository.evict(0L); // 목록 evict
        Cigarette saved = cigaretteRepository.save(cigarette);

        return new CigaretteGetResponse(saved);
    }

    public DeleteResponse deleteCigarette(Long id) throws EntityNotFoundException {
        Cigarette cigarette = cigaretteCacheableRepository.getCacheOrLoad(id);;

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
                -> hashCacheModule.evictAll(CacheEnv.CIGARETTE_LIST, storeId));

        // 목표 엔티티 제거
        cigaretteCacheableRepository.evict(0L); // 목록 evict
        cigaretteRepository.delete(cigarette);
        return new DeleteResponse("cigarette", id);
    }

    private void checkDuplicate(String name) {
        if (cigaretteRepository.existsByOfficialName(name)) {
            throw new OfficaialNameDuplicationException();
        }
    }
}
