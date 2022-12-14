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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CigaretteOnListUpdateService {

    private final CigaretteOnListRepository cigaretteOnListRepository;
    private final StoreRepository storeRepository;
    private final CigaretteRepository cigaretteRepository;
    private final RepoUtils repoUtils;
    private final HashCacheModule hashCacheModule;
    private final CacheModule cacheModule;
    private final SetCacheModule setCacheModule;
    private final CigaretteOnListStopFlushingSchedulerManager cigaretteOnListStopFlushingSchedulerManager;

    //담배 개수 입력
    @Transactional
    public CigaretteOnListGetResponse inputCigaretteCount(Long storeId, Long id, int count){
        // 캐시에서 불러오기
        CigaretteOnList cigaretteOnList = hashCacheModule.getCacheOrLoad(CacheEnv.CIGARETTE_LIST, storeId, id
                , (_id) -> cigaretteOnListRepository.findById(_id).orElseThrow(EntityNotFoundException::new));

        log.info(cigaretteOnList.toString());

        // 처리
        cigaretteOnList.changeCount(count);

        // 캐시, DB에 저장
        hashCacheModule.writeThrough(CacheEnv.CIGARETTE_LIST, storeId, id, cigaretteOnList, cigaretteOnListRepository::save);
//        cigaretteOnListRepository.save(cigaretteOnList);
//        hashCacheModule.put(CacheEnv.CIGARETTE_LIST, storeId, id, cigaretteOnList);

        return new CigaretteOnListGetResponse(cigaretteOnList);
    }

    //담배 개수 초기화
    @Transactional
    public CigaretteOnListGetListResponse initializeCigaretteCount(Long storeId){
        // 캐시에서 불러오기
        List<CigaretteOnList> cigarList = hashCacheModule.getAllCacheOrLoad(CacheEnv.CIGARETTE_LIST, storeId
                , (_id) -> cigaretteOnListRepository.findAllByStoreId(_id)
                , CigaretteOnList::getId)
                .values().stream().collect(Collectors.toList());

        // 갯수 초기화 (값이 없음을 -1로 표현)
        List<CigaretteOnList> modified = cigarList.stream().map(cigar -> {
            cigar.changeCount(-1);
            return cigar;
        }).collect(Collectors.toList());

        return new CigaretteOnListGetListResponse(modified);
    }



    //customizeName 수정
    @Transactional
    public CigaretteOnListGetResponse modifyCustomizeName(Long cigarettOnListId, String customizedName){
        CigaretteOnList cigaretteOnList = repoUtils.getOneElseThrowException(cigaretteOnListRepository, cigarettOnListId);
        cigaretteOnList.changeCustomizedName(customizedName);

        return new CigaretteOnListGetResponse(cigaretteOnList);
    }


    @Transactional
    public CigaretteOnListReorderResponse reOrderAll(CigaretteOnListReorderRequest request) {
        // id 리스트로 한 번에 DB 조회
        List<CigaretteOnListReorderRequest.OrderInfo> orderInfos = request.getOrderInfos();
        List<Long> idList = orderInfos.stream().map(orderInfo -> orderInfo.getId()).collect(Collectors.toList());
        List<CigaretteOnList> cigarettesOnList = cigaretteOnListRepository.findAllById(idList);

        // 요청된 값을 엔티티에 입력
        for(int i = cigarettesOnList.size() - 1; i >= 0; i-- ){
            final int computerizedOrder = orderInfos.get(i).getComputerized_order();
            final int displayOrder = orderInfos.get(i).getDisplay_order();
            cigarettesOnList.get(i).changeOrderInfo(displayOrder, computerizedOrder);
        }

        List<CigaretteOnList> saved = cigaretteOnListRepository.saveAll(cigarettesOnList);

        return new CigaretteOnListReorderResponse(request);
    }



    //담배 추가(담배id) - 순서는 맨 마지막으로
    @Transactional
    public CigaretteOnListGetResponse addCigaretteOnListById(CigaretteOnListAddRequest request){

        checkDuplicate(request.getStoreId(), request.getCigaretteId());

        Store store = repoUtils.getOneElseThrowException(storeRepository, request.getStoreId());

        Cigarette cigarette = cigaretteRepository.findOneByIdCustom(request.getCigaretteId());

        Integer order = Integer.valueOf((int)cigaretteOnListRepository.count() + 1);

        CigaretteOnList cigaretteOnList = CigaretteOnList.builder()
                .cigarette(cigarette)
                .store(store)
                .customizedName(request.getCustomizedName())
                .displayOrder(order)
                .computerizedOrder(order)
                .count(-1) // 빈 값을 -1로 표현
                .build();

        CigaretteOnList saved = cigaretteOnListRepository.save(cigaretteOnList);

//        System.out.println(request.getRequestUserId());
        return new CigaretteOnListGetResponse(saved);
    }

    //삭제
    public DeleteResponse deleteCigaretteOnList(Long id) {
        CigaretteOnList cigaretteOnList = repoUtils.getOneElseThrowException(cigaretteOnListRepository, id);
        cigaretteOnListRepository.delete(cigaretteOnList);

        return new DeleteResponse("cigaretteOnList", id);
    }

    private void checkDuplicate(Long storeId, Long cigaretteId) {
        if (cigaretteOnListRepository.existsByStoreIdAndCigaretteId(storeId, cigaretteId)) {
            throw new DuplicateCigaretteOnListException(storeId, cigaretteId);
        }
    }

    private void putOnCacheAndFlushTimerOn(Long storeId, Long id, CigaretteOnList cigaretteOnList){
        // 목록 캐시에 저장
        hashCacheModule.put(CacheEnv.CIGARETTE_LIST, storeId, id, cigaretteOnList);

        // dirty entity 캐시에 추가
        setCacheModule.put(CacheEnv.CIGARETTE_DIRTY, storeId, cigaretteOnList);

        // dirty 캐시를 주기적으로 flush 해주는 타이머 ON
        cigaretteOnListStopFlushingSchedulerManager.startIfNotStarted(storeId);
    }


    // 아래 내용은 순서 재지정 내용인데 순서 변경 내용을 계산해서 적용하는 코드다.
    // 그러나 일단은 순서가 재지정될 때마다 전체 순서 정보를 받아오는 것으로 함.

    /*
    //담배이동(선택)
    public CigaretteOnListGetListResponse moveCigaretteBySelect(Long listId, List<Long> ids, Long selectedId){

        List<CigaretteOnList> cigaretteOnLists = cigaretteOnListRepository.findAllByInculdeIds(ids);
        /*
        List<CigaretteOnList> cigaretteOnLists = null;
        for (Long id: ids){
            cigaretteOnLists.add(repoUtils.getOneElseThrowException(cigaretteOnListRepository, id));
        }


        CigaretteOnList seletedCigaretteOnList = repoUtils.getOneElseThrowException(cigaretteOnListRepository, selectedId);

        int selectedCigaretteOrder = seletedCigaretteOnList.getDisplayOrder();
        int n = cigaretteOnLists.size();

        //selectedCigaretteOnList 보다 order가 큰 것들의 order를 +n 시킨다. => 업데이트 쿼리를 날리는게 낫지 않을까.
        List<CigaretteOnList> cigaretteOnListsBiggerOrder = cigaretteOnListRepository.findAllByStoreIdAndBiggerOrder(listId, selectedCigaretteOrder);

        for (CigaretteOnList co : cigaretteOnListsBiggerOrder){
            co.changeDisplayOrder(co.getDisplayOrder()+n);
        }

        // 선택된 담배들의 순서는 selectedCigaretteOnList의 순서보다  selectedCigaretteOrder +1+i;
        for(int i=0; i<n; i++){
            cigaretteOnLists.get(i).changeDisplayOrder(selectedCigaretteOrder+i+1);
        }

        return new CigaretteOnListGetListResponse(cigaretteOnLists);
    }
     */



    //드래그앤드롭(진열순서)
//    @Transactional
//    public CigaretteOnListGetListResponse moveDisplayOrderByDragAndDrop(Long storeId, Long afterElementId, Long draggableId){
//        CigaretteOnList afterElementCigarette = repoUtils.getOneElseThrowException(cigaretteOnListRepository, afterElementId);
//        CigaretteOnList draggableCigarette = repoUtils.getOneElseThrowException(cigaretteOnListRepository, draggableId);
//
//        int afterElementOrder = afterElementCigarette.getDisplayOrder();
//        int draggableOrder = draggableCigarette.getDisplayOrder();
//
//        if (afterElementOrder < draggableOrder){//아래서 위로 드래그 한경우
//            //after 이상, draggable 미만인 cigaretteOnList들 다 +1
//            cigaretteOnListRepository.dragAndDropDisplayOrderUp(storeId,afterElementOrder, draggableOrder);
//            //draggable 의 displayOrder는 after의 원래 순서로
//            //draggableCigarette.changeDisplayOrder(afterElementOrder);
//            cigaretteOnListRepository.updateDisplayOrder(draggableId,afterElementOrder);
//        }
//        else{//위에서 아래로 drag 한 경우
//            //after 미만, draggable보다 큰 cigaretteOnList들 다 -1
//            cigaretteOnListRepository.dragAndDropDisplayOrderDown(storeId,afterElementOrder, draggableOrder);
//            //draggable 의 displayOrder는 after의 -1로
//            //draggableCigarette.changeDisplayOrder(afterElementOrder-1);
//            cigaretteOnListRepository.updateDisplayOrder(draggableId,afterElementOrder-1);
//        }
//
//        return new CigaretteOnListGetListResponse(cigaretteOnListRepository.findAllByStoreId(storeId));
//    }

    //드래드앤드롭(전산순서)
//    @Transactional
//    public CigaretteOnListGetListResponse moveComputerizedOrderByDragAndDrop(Long storeId, Long afterElementId, Long draggableId){
//        CigaretteOnList afterElementCigarette = repoUtils.getOneElseThrowException(cigaretteOnListRepository, afterElementId);
//        CigaretteOnList draggableCigarette = repoUtils.getOneElseThrowException(cigaretteOnListRepository, draggableId);
//
//        int afterElementOrder = afterElementCigarette.getComputerizedOrder();
//        int draggableOrder = draggableCigarette.getComputerizedOrder();
//
//        if (afterElementOrder < draggableOrder){//아래서 위로 드래그 한경우
//            //after 이상, draggable 미만인 cigaretteOnList들 다 +1
//            cigaretteOnListRepository.dragAndDropComputerizedOrderUp(storeId,afterElementOrder, draggableOrder);
//            //draggable 의 displayOrder는 after의 원래 순서로
//            //draggableCigarette.changeComputerizedOrder(afterElementOrder);
//            cigaretteOnListRepository.updateComputerizedOrder(draggableId,afterElementOrder);
//        }
//        else{//위에서 아래로 drag 한 경우
//            //after 미만, draggable보다 큰 cigaretteOnList들 다 -1
//            cigaretteOnListRepository.dragAndDropComputerizedOrderDown(storeId,afterElementOrder, draggableOrder);
//            //draggable 의 displayOrder는 after의 -1로
//            cigaretteOnListRepository.updateComputerizedOrder(draggableId,afterElementOrder-1);
//        }
//        return new CigaretteOnListGetListResponse(cigaretteOnListRepository.findAllByStoreId(storeId));
//    }

}
