package com.dambae200.dambae200.domain.cigaretteOnList.service;

import com.dambae200.dambae200.domain.cigarette.domain.Cigarette;
import com.dambae200.dambae200.domain.cigarette.repository.CigaretteRepository;
import com.dambae200.dambae200.domain.cigaretteOnList.dto.*;
import com.dambae200.dambae200.domain.cigaretteOnList.service.scheduler.CigaretteOnListStopFlushingSchedulerManager;
import com.dambae200.dambae200.domain.store.domain.Store;
import com.dambae200.dambae200.domain.store.repository.StoreRepository;
import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import com.dambae200.dambae200.domain.cigaretteOnList.exception.DuplicateCigaretteOnListException;
import com.dambae200.dambae200.domain.cigaretteOnList.repository.CigaretteOnListRepository;
import com.dambae200.dambae200.global.cache.service.CacheableRepository;
import com.dambae200.dambae200.global.common.dto.DeleteResponse;
import com.dambae200.dambae200.global.common.service.RepoUtils;
import com.dambae200.dambae200.global.cache.config.CacheEnv;
import com.dambae200.dambae200.global.cache.service.CacheModule;
import com.dambae200.dambae200.global.cache.service.HashCacheModule;
import com.dambae200.dambae200.global.cache.service.SetCacheModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CigaretteOnListUpdateService {

    private final CigaretteOnListRepository cigaretteOnListRepository;
    private final CacheableRepository<Long, Cigarette, CigaretteRepository> cigaretteCacheableRepository;
    private final StoreRepository storeRepository;
    private final CigaretteRepository cigaretteRepository;
    private final HashCacheModule hashCacheModule;
    private final CacheModule cacheModule;

    //담배 개수 입력
    @Transactional
    public CigaretteOnListGetResponse inputCigaretteCount(Long storeId, Long id, int count){
        // 캐시, DB에서 불러오기
        CigaretteOnList cigaretteOnList = getCacheOrLoad(storeId, id);

        // 처리
        cigaretteOnList.changeCount(count);

        // 캐시, DB에 저장
        writeThrough(storeId, cigaretteOnList);

        return new CigaretteOnListGetResponse(cigaretteOnList);
    }

    //담배 개수 초기화
    @Transactional
    public CigaretteOnListGetListResponse initializeCigaretteCount(Long storeId){
        // 캐시, DB에서 불러오기
        List<CigaretteOnList> cigarList = getAllCacheOrLoad(storeId);

        // 갯수 초기화 (값이 없음을 -1로 표현)
        List<CigaretteOnList> modified = cigarList.stream().map(cigar -> {
            cigar.changeCount(-1);
            return cigar;
        }).collect(Collectors.toList());

        // 캐시, DB에 저장
        writeAllThrough(storeId, modified);

        return new CigaretteOnListGetListResponse(modified);
    }



    @Transactional
    public CigaretteOnListGetResponse modifyCustomizeName(Long storeId, Long id, String customizedName){
        CigaretteOnList cigaretteOnList = getCacheOrLoad(storeId, id);
        cigaretteOnList.changeCustomizedName(customizedName);
        CigaretteOnList saved = writeThrough(storeId, cigaretteOnList);
        return new CigaretteOnListGetResponse(saved);
    }


    @Transactional
    public CigaretteOnListReorderResponse reOrderAll(CigaretteOnListReorderRequest request) {
        // id 리스트로 한 번에 DB 조회
        List<CigaretteOnListReorderRequest.OrderInfo> orderInfos = request.getOrderInfos();
        List<Long> idList = orderInfos.stream().map(orderInfo -> orderInfo.getId()).collect(Collectors.toList());
        List<CigaretteOnList> cigars = hashCacheModule.getAllCacheOrLoadByHashKeys(CacheEnv.CIGARETTE_LIST
                , request.getStoreId(), idList
                , cigaretteOnListRepository::findAllById
                , CigaretteOnList::getId)
                .values().stream().sorted(Comparator.comparing(CigaretteOnList::getId))
                .collect(Collectors.toList());

        // 요청된 값을 엔티티에 입력
        for(int i = cigars.size() - 1; i >= 0; i-- ){
            final int computerizedOrder = orderInfos.get(i).getComputerized_order();
            final int displayOrder = orderInfos.get(i).getDisplay_order();
            cigars.get(i).changeOrderInfo(displayOrder, computerizedOrder);
        }

        List<CigaretteOnList> saved = writeAllThrough(request.getStoreId(), cigars);
        log.info(saved.toString());

        return new CigaretteOnListReorderResponse(request);
    }



    //담배 추가(담배id) - 순서는 맨 마지막으로
    @Transactional
    public CigaretteOnListGetResponse addCigaretteOnListById(CigaretteOnListAddRequest request){

        checkDuplicate(request.getStoreId(), request.getCigaretteId());

        Store store = storeRepository.findById(request.getStoreId()).orElseThrow(() -> new EntityNotFoundException());

        Cigarette cigarette = cigaretteCacheableRepository.getCacheOrLoad(request.getCigaretteId());

        Integer order = Integer.valueOf((int)cigaretteOnListRepository.count() + 1); // 마지막 순서

        CigaretteOnList cigaretteOnList = CigaretteOnList.builder()
                .cigarette(cigarette)
                .store(store)
                .customizedName(request.getCustomizedName())
                .displayOrder(order)
                .computerizedOrder(order)
                .count(-1) // 빈 값을 -1로 표현
                .build();

        CigaretteOnList saved = writeThrough(store.getId(), cigaretteOnList);

        return new CigaretteOnListGetResponse(saved);
    }

    //삭제
    public DeleteResponse deleteCigaretteOnList(Long storeId, Long id) {
        hashCacheModule.deleteThrough(CacheEnv.CIGARETTE_LIST, storeId, id, cigaretteOnListRepository::deleteById);
        return new DeleteResponse("cigaretteOnList", id);
    }

    private void checkDuplicate(Long storeId, Long cigaretteId) {
        if (cigaretteOnListRepository.existsByStoreIdAndCigaretteId(storeId, cigaretteId)) {
            throw new DuplicateCigaretteOnListException(storeId, cigaretteId);
        }
    }

    private CigaretteOnList getCacheOrLoad(Long storeId, Long id){
        return hashCacheModule.getCacheOrLoad(CacheEnv.CIGARETTE_LIST, storeId, id
                , (_id) -> cigaretteOnListRepository.findById(_id).orElseThrow(EntityNotFoundException::new));
    }

    private List<CigaretteOnList> getAllCacheOrLoad(Long storeId){
        return hashCacheModule.getAllCacheOrLoad(CacheEnv.CIGARETTE_LIST, storeId
                        , (_id) -> cigaretteOnListRepository.findAllByStoreId(_id)
                        , CigaretteOnList::getId)
                .values().stream().collect(Collectors.toList());
    }

    private List<CigaretteOnList> writeAllThrough(Long storeId, List<CigaretteOnList> modified){
        return hashCacheModule.writeAllThrough(CacheEnv.CIGARETTE_LIST, storeId, modified, CigaretteOnList::getId, cigaretteOnListRepository::saveAll);
    }

    private CigaretteOnList writeThrough(Long storeId, CigaretteOnList modified){
        return hashCacheModule.writeThrough(CacheEnv.CIGARETTE_LIST, storeId, modified, CigaretteOnList::getId, cigaretteOnListRepository::save);
    }


}
