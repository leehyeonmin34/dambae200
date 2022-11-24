package com.dambae200.dambae200.domain.cigaretteOnList.service;

import com.dambae200.dambae200.domain.cigarette.domain.Cigarette;
import com.dambae200.dambae200.domain.cigarette.repository.CigaretteRepository;
import com.dambae200.dambae200.domain.store.domain.Store;
import com.dambae200.dambae200.domain.store.repository.StoreRepository;
import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import com.dambae200.dambae200.domain.cigaretteOnList.dto.CigaretteOnListDto;
import com.dambae200.dambae200.domain.cigaretteOnList.exception.DuplicateCigaretteOnListException;
import com.dambae200.dambae200.domain.cigaretteOnList.repository.CigaretteOnListRepository;
import com.dambae200.dambae200.global.common.DeleteResponse;
import com.dambae200.dambae200.global.common.RepoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CigaretteOnListUpdateService {

    private final CigaretteOnListRepository cigaretteOnListRepository;
    private final StoreRepository storeRepository;
    private final CigaretteRepository cigaretteRepository;
    private final RepoUtils repoUtils;

    //담배 개수 입력
    @Transactional
    public CigaretteOnListDto.GetCigaretteResponse inputCigaretteCount(Long id, int count){
        CigaretteOnList cigaretteOnList = repoUtils.getOneElseThrowException(cigaretteOnListRepository, id);
        cigaretteOnList.changeCount(count);

        return new CigaretteOnListDto.GetCigaretteResponse(cigaretteOnList);
    }

    //담배 개수 초기화
    @Transactional
    public CigaretteOnListDto.GetListCigaretteResponse initializeCigaretteCount(Long storeId){
        List<CigaretteOnList> cigarettesOnList = cigaretteOnListRepository.findAllByStoreId(storeId);
        List<CigaretteOnList> modified = cigarettesOnList.stream().map(cigar -> {
            cigar.changeCount(-1);
            return cigar;
        }).collect(Collectors.toList());
        return new CigaretteOnListDto.GetListCigaretteResponse(modified);
    }


    //드래그앤드롭(진열순서)
//    @Transactional
//    public CigaretteOnListDto.GetListCigaretteResponse moveDisplayOrderByDragAndDrop(Long storeId, Long afterElementId, Long draggableId){
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
//        return new CigaretteOnListDto.GetListCigaretteResponse(cigaretteOnListRepository.findAllByStoreId(storeId));
//    }

    //드래드앤드롭(전산순서)
//    @Transactional
//    public CigaretteOnListDto.GetListCigaretteResponse moveComputerizedOrderByDragAndDrop(Long storeId, Long afterElementId, Long draggableId){
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
//        return new CigaretteOnListDto.GetListCigaretteResponse(cigaretteOnListRepository.findAllByStoreId(storeId));
//    }

    //customizeName 수정
    @Transactional
    public CigaretteOnListDto.GetCigaretteResponse modifyCustomizeName(Long cigarettOnListId, String customizedName){
        CigaretteOnList cigaretteOnList = repoUtils.getOneElseThrowException(cigaretteOnListRepository, cigarettOnListId);
        cigaretteOnList.changeCustomizedName(customizedName);

        return new CigaretteOnListDto.GetCigaretteResponse(cigaretteOnList);
    }

    //reOrder(displayOrder)
    @Transactional
    public CigaretteOnListDto.GetListCigaretteResponse reOrderDisplay(List<CigaretteOnListDto.ReorderRequest> request){
        //List<CigaretteOnListDto.OrderInfo> orderInfos = request.getOrderInfos();

        for(CigaretteOnListDto.ReorderRequest orderInfo : request){
            CigaretteOnList cigaretteOnList = repoUtils.getOneElseThrowException(cigaretteOnListRepository, orderInfo.getId());
            cigaretteOnList.changeDisplayOrder(orderInfo.getDisplay_order());
            cigaretteOnList.changeComputerizedOrder(orderInfo.getComputerized_order());
        }

        //store 구하기
        CigaretteOnList cigaretteOnList = repoUtils.getOneElseThrowException(cigaretteOnListRepository, request.get(0).getId());
        List<CigaretteOnList> cigaretteOnLists = cigaretteOnListRepository.findAllByStoreIdOrderByDisplay(cigaretteOnList.getStore().getId());

        return new CigaretteOnListDto.GetListCigaretteResponse(cigaretteOnLists);
    }


    //reOrder(computerizedOrder)
    @Transactional
    public CigaretteOnListDto.GetListCigaretteResponse reOrderComputerized(List<CigaretteOnListDto.ReorderRequest> request){
        //List<CigaretteOnListDto.OrderInfo> orderInfos = request.getOrderInfos();

        for(CigaretteOnListDto.ReorderRequest orderInfo : request){
            CigaretteOnList cigaretteOnList = repoUtils.getOneElseThrowException(cigaretteOnListRepository, orderInfo.getId());
            cigaretteOnList.changeDisplayOrder(orderInfo.getDisplay_order());
            cigaretteOnList.changeComputerizedOrder(orderInfo.getComputerized_order());
        }

        //store 구하기
        CigaretteOnList cigaretteOnList = repoUtils.getOneElseThrowException(cigaretteOnListRepository, request.get(0).getId());
        List<CigaretteOnList> cigaretteOnLists = cigaretteOnListRepository.findAllByStoreIdOrderByComputerized(cigaretteOnList.getStore().getId());

        return new CigaretteOnListDto.GetListCigaretteResponse(cigaretteOnLists);
    }

    @Transactional
    public CigaretteOnListDto.GetListCigaretteResponse reOrderAll(List<CigaretteOnListDto.ReorderRequest> request) {

        // id 리스트로 한 번에 DB 조회
        List<Long> idList = request.stream().map(orderInfo -> orderInfo.getId()).collect(Collectors.toList());
        List<CigaretteOnList> cigarettesOnList = cigaretteOnListRepository.findAllById(idList);

        // 요청된 값을 엔티티에 입력
        for(int i = cigarettesOnList.size() - 1; i >= 0; i-- ){
            final int computerizedOrder = request.get(i).getComputerized_order();
            final int displayOrder = request.get(i).getDisplay_order();
            cigarettesOnList.get(i).changeOrderInfo(displayOrder, computerizedOrder);
        }

        List<CigaretteOnList> saved = cigaretteOnListRepository.saveAll(cigarettesOnList);

        return new CigaretteOnListDto.GetListCigaretteResponse(saved);
    }



    //담배 추가(담배id) - 순서는 맨 마지막으로
    @Transactional
    public CigaretteOnListDto.GetCigaretteResponse addCigaretteOnListById(CigaretteOnListDto.AddCigaretteOnList request){

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

        return new CigaretteOnListDto.GetCigaretteResponse(saved);
    }

    //담배 추가(담배id) - 이름순으로 전산순서 정보 수정
    @Transactional
    public CigaretteOnListDto.GetCigaretteResponse addCigaretteOnListByIdOrderByName(CigaretteOnListDto.AddCigaretteOnList request){

        checkDuplicate(request.getStoreId(), request.getCigaretteId());

        Store store = repoUtils.getOneElseThrowException(storeRepository, request.getStoreId());

        Cigarette cigarette = cigaretteRepository.findOneByIdCustom(request.getCigaretteId());

        CigaretteOnList cigaretteOnList = CigaretteOnList.createCigaretteOnList(store, cigarette,request.getCustomizedName());

        //진열순서 초기값
        List<CigaretteOnList> cigaretteOnLists = cigaretteOnListRepository.findAllByStoreId(request.getStoreId());
        cigaretteOnList.changeDisplayOrder(cigaretteOnLists.size()-1);

        //전산순서 초기값
        //저장 후 전산순서 확정->그럼 전산순서를 이름순서로 해서 그 사이에 넣는다는건데 이게 이름 순서로 안되어 있을 수 있어 마지막에 넣어야 하나??
        //지금 저장되어 있는 담배온리스트 중 이름순으로 몇번째인지 찾아보고 전산순서 결정
        //결정하기전 그 이후의 전산순서를 가지는 담배들 모두 전산순서 +1씩
        List<CigaretteOnList> smallerThanOfficialName = cigaretteOnListRepository.findAllBySmallThanOfficialName(store.getId(), cigarette.getOfficialName());
        //전산순서가 ~이상인것들은 +1 업데이트 쿼리 날려야해 => 벌크성 수정 쿼리
        cigaretteOnListRepository.updateByOrderGreaterThanPlus(store.getId(), smallerThanOfficialName.size());

        //cigaretteOnList.changeComputerizedOrder(smallerThanOfficialName.size());
        cigaretteOnListRepository.updateComputerizedOrder(cigaretteOnList.getId(), smallerThanOfficialName.size());

        return new CigaretteOnListDto.GetCigaretteResponse(cigaretteOnList);
    }

    /*
    //담배이동(선택)
    public CigaretteOnListDto.GetListCigaretteResponse moveCigaretteBySelect(Long listId, List<Long> ids, Long selectedId){

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

        return new CigaretteOnListDto.GetListCigaretteResponse(cigaretteOnLists);
    }
     */


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
}
