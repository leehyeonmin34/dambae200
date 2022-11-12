package dambae200.dambae200.domain.cigaretteOnList.service;

import dambae200.dambae200.domain.cigarette.domain.Cigarette;
import dambae200.dambae200.domain.cigarette.repository.CigaretteRepository;
import dambae200.dambae200.domain.cigaretteList.domain.CigaretteList;
import dambae200.dambae200.domain.cigaretteList.repository.CigaretteListRepository;
import dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import dambae200.dambae200.domain.cigaretteOnList.dto.CigaretteOnListDto;
import dambae200.dambae200.domain.cigaretteOnList.exception.DuplicateCigaretteOnListException;
import dambae200.dambae200.domain.cigaretteOnList.repository.CigaretteOnListRepository;
import dambae200.dambae200.global.common.DeleteResponse;
import dambae200.dambae200.global.common.RepoUtils;
import dambae200.dambae200.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CigaretteOnListUpdateService {

    private final CigaretteOnListRepository cigaretteOnListRepository;
    private final CigaretteListRepository cigaretteListRepository;
    private final CigaretteRepository cigaretteRepository;
    private final RepoUtils repoUtils;

    //담배 개수 입력
    @Transactional
    public CigaretteOnListDto.GetCigaretteResponse inputCigaretteCount(Long id, int count) throws EntityNotFoundException {
        CigaretteOnList cigaretteOnList = repoUtils.getOneElseThrowException(cigaretteOnListRepository, id);
        cigaretteOnList.changeCount(count);

        return new CigaretteOnListDto.GetCigaretteResponse(cigaretteOnList);
    }


    //드래그앤드롭(진열순서)
    @Transactional
    public CigaretteOnListDto.GetListCigaretteResponse moveDisplayOrderByDragAndDrop(Long cigaretteListId, Long afterElementId, Long draggableId){
        CigaretteOnList afterElementCigarette = repoUtils.getOneElseThrowException(cigaretteOnListRepository, afterElementId);
        CigaretteOnList draggableCigarette = repoUtils.getOneElseThrowException(cigaretteOnListRepository, draggableId);

        int afterElementOrder = afterElementCigarette.getDisplayOrder();
        int draggableOrder = draggableCigarette.getDisplayOrder();

        if (afterElementOrder < draggableOrder){//아래서 위로 드래그 한경우
            //after 이상, draggable 미만인 cigaretteOnList들 다 +1
            cigaretteOnListRepository.dragAndDropDisplayOrderUp(cigaretteListId,afterElementOrder, draggableOrder);
            //draggable 의 displayOrder는 after의 원래 순서로
            //draggableCigarette.changeDisplayOrder(afterElementOrder);
            cigaretteOnListRepository.updateDisplayOrder(draggableId,afterElementOrder);
        }
        else{//위에서 아래로 drag 한 경우
            //after 미만, draggable보다 큰 cigaretteOnList들 다 -1
            cigaretteOnListRepository.dragAndDropDisplayOrderDown(cigaretteListId,afterElementOrder, draggableOrder);
            //draggable 의 displayOrder는 after의 -1로
            //draggableCigarette.changeDisplayOrder(afterElementOrder-1);
            cigaretteOnListRepository.updateDisplayOrder(draggableId,afterElementOrder-1);
        }

        return new CigaretteOnListDto.GetListCigaretteResponse(cigaretteOnListRepository.findAllByCigaretteListId(cigaretteListId));
    }

    //드래드앤드롭(전산순서)
    @Transactional
    public CigaretteOnListDto.GetListCigaretteResponse moveComputerizedOrderByDragAndDrop(Long cigaretteListId, Long afterElementId, Long draggableId){
        CigaretteOnList afterElementCigarette = repoUtils.getOneElseThrowException(cigaretteOnListRepository, afterElementId);
        CigaretteOnList draggableCigarette = repoUtils.getOneElseThrowException(cigaretteOnListRepository, draggableId);

        int afterElementOrder = afterElementCigarette.getComputerizedOrder();
        int draggableOrder = draggableCigarette.getComputerizedOrder();

        if (afterElementOrder < draggableOrder){//아래서 위로 드래그 한경우
            //after 이상, draggable 미만인 cigaretteOnList들 다 +1
            cigaretteOnListRepository.dragAndDropComputerizedOrderUp(cigaretteListId,afterElementOrder, draggableOrder);
            //draggable 의 displayOrder는 after의 원래 순서로
            //draggableCigarette.changeComputerizedOrder(afterElementOrder);
            cigaretteOnListRepository.updateComputerizedOrder(draggableId,afterElementOrder);
        }
        else{//위에서 아래로 drag 한 경우
            //after 미만, draggable보다 큰 cigaretteOnList들 다 -1
            cigaretteOnListRepository.dragAndDropComputerizedOrderDown(cigaretteListId,afterElementOrder, draggableOrder);
            //draggable 의 displayOrder는 after의 -1로
            cigaretteOnListRepository.updateComputerizedOrder(draggableId,afterElementOrder-1);
        }
        return new CigaretteOnListDto.GetListCigaretteResponse(cigaretteOnListRepository.findAllByCigaretteListId(cigaretteListId));
    }


    //담배 추가(담배id)
    @Transactional
    public CigaretteOnListDto.GetCigaretteResponse addCigaretteOnListById(CigaretteOnListDto.AddCigaretteOnList request) throws DuplicateCigaretteOnListException {

        checkDuplicate(request.getCigaretteListId(), request.getCigaretteId());

        CigaretteList cigaretteList = cigaretteListRepository.findOneByIdCustom(request.getCigaretteListId());

        Cigarette cigarette = cigaretteRepository.findOneByIdCustom(request.getCigaretteId());

        CigaretteOnList cigaretteOnList = CigaretteOnList.createCigaretteOnList(cigarette,request.getCustomizedName());

        cigaretteList.addCigaretteOnList(cigaretteOnList);

        //진열순서 초기값
        List<CigaretteOnList> cigaretteOnLists = cigaretteOnListRepository.findAllByCigaretteListId(request.getCigaretteListId());
        cigaretteOnList.changeDisplayOrder(cigaretteOnLists.size()-1);

        //전산순서 초기값
        //저장 후 전산순서 확정->그럼 전산순서를 이름순서로 해서 그 사이에 넣는다는건데 이게 이름 순서로 안되어 있을 수 있어 마지막에 넣어야 하나??
        //지금 저장되어 있는 담배온리스트 중 이름순으로 몇번째인지 찾아보고 전산순서 결정
        //결정하기전 그 이후의 전산순서를 가지는 담배들 모두 전산순서 +1씩
        List<CigaretteOnList> smallerThanOfficialName = cigaretteOnListRepository.findAllBySmallThanOfficialName(cigaretteList.getId(), cigarette.getOfficialName());
        //전산순서가 ~이상인것들은 +1 업데이트 쿼리 날려야해 => 벌크성 수정 쿼리
        cigaretteOnListRepository.updateByOrderGreaterThanPlus(cigaretteList.getId(), smallerThanOfficialName.size());

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
        List<CigaretteOnList> cigaretteOnListsBiggerOrder = cigaretteOnListRepository.findAllByCigaretteListIdAndBiggerOrder(listId, selectedCigaretteOrder);

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
    public DeleteResponse deleteCigaretteOnList(Long id) throws EntityNotFoundException {
        CigaretteOnList cigaretteOnList = repoUtils.getOneElseThrowException(cigaretteOnListRepository, id);
        CigaretteList cigaretteList = cigaretteOnList.getCigaretteList();
        cigaretteList.deleteCigaretteOnList(cigaretteOnList);

        cigaretteOnListRepository.delete(cigaretteOnList);

        return new DeleteResponse("cigaretteOnList", id);
    }

    private void checkDuplicate(Long cigaretteListId, Long cigaretteId) {
        if (cigaretteOnListRepository.existsByCigaretteListIdAndCigaretteId(cigaretteListId, cigaretteId)) {
            throw new DuplicateCigaretteOnListException(cigaretteListId, cigaretteId);
        }
    }
}
