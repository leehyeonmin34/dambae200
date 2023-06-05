package com.dambae200.dambae200.domain.cigaretteOnList.service;

import com.dambae200.dambae200.domain.cigarette.domain.Cigarette;
import com.dambae200.dambae200.domain.cigarette.dto.CigaretteGetResponse;
import com.dambae200.dambae200.domain.cigarette.repository.CigaretteRepository;
import com.dambae200.dambae200.domain.cigaretteOnList.dto.*;
import com.dambae200.dambae200.domain.store.domain.Store;
import com.dambae200.dambae200.domain.store.repository.StoreRepository;
import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import com.dambae200.dambae200.domain.cigaretteOnList.exception.DuplicateCigaretteOnListException;
import com.dambae200.dambae200.domain.cigaretteOnList.repository.CigaretteOnListRepository;
import com.dambae200.dambae200.global.cache.config.CacheType;
import com.dambae200.dambae200.global.cache.service.CacheableRepository;
import com.dambae200.dambae200.global.cache.service.HashCacheableRepository;
import com.dambae200.dambae200.global.common.dto.DeleteResponse;
import com.dambae200.dambae200.global.cache.config.CacheEnvOld;
import com.dambae200.dambae200.global.cache.service.HashCacheModule;
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
    private final CacheableRepository<Cigarette, CigaretteGetResponse, Long, CigaretteRepository> cigaretteCacheableRepository;
    private final HashCacheableRepository<CigaretteOnList, CigaretteOnListGetResponse, Long, Long, CigaretteOnListRepository> cigaretteOnListCacheableRepository;
    private final StoreRepository storeRepository;
    private final HashCacheModule hashCacheModule;
    private final CigaretteOnListTxService cigaretteOnListTxService;

    //담배 개수 입력
    public CigaretteOnListUpdateCountResponse inputCigaretteCount(final Long storeId, final Long id, final int count){
        // 캐시, DB에서 불러오기
        final CigaretteOnList cigaretteOnList = cigaretteOnListCacheableRepository.getEntityCacheOrLoad(storeId, id);

        // 처리
        cigaretteOnList.changeCount(count);

        // 캐시, DB에 저장
        cigaretteOnListCacheableRepository.writeThrough(storeId, cigaretteOnList);

        return new CigaretteOnListUpdateCountResponse(cigaretteOnList);
    }

    //담배 개수 초기화
    public void initializeCigaretteCount(final Long storeId){
        // 캐시, DB에서 불러오기
        List<CigaretteOnList> cigarList = cigaretteOnListCacheableRepository
                .getAllEntityCacheOrLoad(storeId, cigaretteOnListRepository::findAllByStoreId)
                .values().stream().collect(Collectors.toList());

        // 갯수 초기화 (값이 없음을 -1로 표현)
        List<CigaretteOnList> modified = cigarList.stream().map(cigar -> {
            cigar.changeCount(-1);
            return cigar;
        }).collect(Collectors.toList());

        // 캐시, DB에 저장
        cigaretteOnListCacheableRepository.writeAllThrough(storeId, modified);
    }

    public CigaretteOnListModifyResponse modifyCustomizeName(final Long storeId, Long id, final String customizedName){
        final CigaretteOnList cigaretteOnList = cigaretteOnListCacheableRepository.getEntityCacheOrLoad(storeId, id);
        cigaretteOnList.changeCustomizedName(customizedName);
        final CigaretteOnList saved = cigaretteOnListCacheableRepository.writeThrough(storeId, cigaretteOnList);
        return new CigaretteOnListModifyResponse(saved);
    }


    @Transactional
    public CigaretteOnListReorderResponse reOrderAll(final CigaretteOnListReorderRequest request) {

        // 요청으로 들어온 순서정보를 id 순서로 정렬
        List<CigaretteOnListReorderRequest.OrderInfo> orderInfos = request.getOrderInfos().stream()
                .sorted(Comparator.comparing(CigaretteOnListReorderRequest.OrderInfo::getId))
                .collect(Collectors.toList());

        // id 리스트로 한 번에 DB 조회 및 id 순서로 정렬
        List<Long> idList = orderInfos.stream().map(orderInfo -> orderInfo.getId()).collect(Collectors.toList());
        List<CigaretteOnList> cigars = cigaretteOnListCacheableRepository.getAllEntityCacheOrLoadByHashKeys(request.getStoreId(), idList)
                        .values().stream().sorted(Comparator.comparing(CigaretteOnList::getId))
                        .collect(Collectors.toList());

        // 요청된 값을 엔티티에 입력
        for(int i = cigars.size() - 1; i >= 0; i-- ){
            int computerizedOrder = orderInfos.get(i).getComputerized_order();
            int displayOrder = orderInfos.get(i).getDisplay_order();
            cigars.get(i).changeOrderInfo(displayOrder, computerizedOrder);
        }

        cigaretteOnListCacheableRepository.writeAllThrough(request.getStoreId(), cigars);

        return new CigaretteOnListReorderResponse(request);
    }



    //담배 추가(담배id) - 순서는 맨 마지막으로
    public CigaretteOnListGetResponse addCigaretteOnList(CigaretteOnListAddRequest request){



        Store store = storeRepository.findById(request.getStoreId()).orElseThrow(() -> new EntityNotFoundException());

        Cigarette cigarette = cigaretteCacheableRepository.getEntityCacheOrLoad(request.getCigaretteId());

        Integer order = Integer.valueOf(9999); // 마지막 순서

        CigaretteOnList cigaretteOnList = new CigaretteOnList.Builder(store, order)
                .cigarette(cigarette)
                .customizedName(request.getCustomizedName())
                .build();

        // Setnx 트랜잭션
        CigaretteOnList saved = cigaretteOnListTxService.addIfNotExist(String.valueOf(store.getId()), store.getId(), cigarette.getId(), cigaretteOnList);

        return new CigaretteOnListGetResponse(saved);
    }

    //삭제
    public DeleteResponse deleteCigaretteOnList(Long storeId, Long id) {
        cigaretteOnListCacheableRepository.deleteThrough(storeId, id);
        return new DeleteResponse("cigaretteOnList", id);
    }




}
