package dambae200.dambae200.domain.cigaretteOnList.service;

import dambae200.dambae200.domain.cigarette.domain.Cigarette;
import dambae200.dambae200.domain.cigarette.repository.CigaretteRepository;
import dambae200.dambae200.domain.cigaretteList.domain.CigaretteList;
import dambae200.dambae200.domain.cigaretteList.repository.CigaretteListRepository;
import dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import dambae200.dambae200.domain.cigaretteOnList.dto.CigaretteOnListDto;
import dambae200.dambae200.domain.cigaretteOnList.exception.DuplicateCigaretteOnListException;
import dambae200.dambae200.domain.cigaretteOnList.repository.CigaretteOnListRepository;
import dambae200.dambae200.domain.section.domain.Section;
import dambae200.dambae200.domain.section.repository.SectionRepository;
import dambae200.dambae200.global.common.DeleteResponse;
import dambae200.dambae200.global.common.RepoUtils;
import dambae200.dambae200.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CigaretteOnListUpdateService {

    private final CigaretteOnListRepository cigaretteOnListRepository;
    private final CigaretteListRepository cigaretteListRepository;
    private final CigaretteRepository cigaretteRepository;
    private final RepoUtils repoUtils;

    //담배 개수 입력
    public CigaretteOnListDto.GetCigaretteResponse inputCigaretteCount(Long id, int count) throws EntityNotFoundException {
        CigaretteOnList cigaretteOnList = repoUtils.getOneElseThrowException(cigaretteOnListRepository, id);
        cigaretteOnList.changeCount(count);

        return new CigaretteOnListDto.GetCigaretteResponse(cigaretteOnList);
    }

    //담배 추가(담배id)
    public CigaretteOnListDto.GetCigaretteResponse addCigaretteOnListById(CigaretteOnListDto.AddCigaretteOnList request) throws DuplicateCigaretteOnListException {

        checkDuplicate(request.getCigaretteListId(), request.getCigaretteId());

        CigaretteList cigaretteList = cigaretteListRepository.findOneByIdCustom(request.getCigaretteListId());

        Cigarette cigarette = cigaretteRepository.findOneByIdCustom(request.getCigaretteId());

        CigaretteOnList cigaretteOnList = CigaretteOnList.createCigaretteOnList(cigarette);

        List<CigaretteOnList> cigaretteOnLists = cigaretteOnListRepository.findAllByCigaretteListId(request.getCigaretteListId());
        //진열순서 초기값
        cigaretteOnList.changeDisplayOrder(cigaretteOnLists.size()+1);

        //전산순서 초기값

        cigaretteList.addCigaretteOnList(cigaretteOnList);

        //저장 후 전산순서 확정->그럼 전산순서를 이름순서로 해서 그 사이에 넣는다는건데 이게 이름 순서로 안되어 있을 수 있어 마지막에 넣어야 하나??

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

    //드래그앤드랍
    public void moveCigaretteByDragAndDrop(Long listId, Long dragId, Long dropId){
        CigaretteOnList dragCigaretteOnList = repoUtils.getOneElseThrowException(cigaretteOnListRepository, dragId);
        CigaretteOnList dropCigaretteOnList = repoUtils.getOneElseThrowException(cigaretteOnListRepository, dropId);

        int order = dropCigaretteOnList.getDisplayOrder();
        //dropCigaretteOnList의 order보다 크거나 같은것들은 다 +1;
        //dropCI~ 보다 order큰것을  다 가져와서
        dropCigaretteOnList.changeDisplayOrder(order+1);
        List<CigaretteOnList> cigaretteOnListsBiggerOrder = cigaretteOnListRepository.findAllByCigaretteListIdAndBiggerOrder(listId, order);
        for (CigaretteOnList co : cigaretteOnListsBiggerOrder){
            co.changeDisplayOrder(co.getDisplayOrder()+1);
        }
        dragCigaretteOnList.changeDisplayOrder(order);
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
