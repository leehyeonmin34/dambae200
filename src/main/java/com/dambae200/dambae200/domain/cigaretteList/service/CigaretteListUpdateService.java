package com.dambae200.dambae200.domain.cigaretteList.service;

import com.dambae200.dambae200.domain.cigaretteList.domain.CigaretteList;
import com.dambae200.dambae200.domain.cigaretteList.dto.CigaretteListDto;
import com.dambae200.dambae200.domain.cigaretteList.repository.CigaretteListRepository;
import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import com.dambae200.dambae200.domain.cigaretteOnList.repository.CigaretteOnListRepository;
import com.dambae200.dambae200.global.common.DeleteResponse;
import com.dambae200.dambae200.global.common.RepoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CigaretteListUpdateService {

    private final CigaretteListRepository cigaretteListRepository ;
    private final CigaretteOnListRepository cigaretteOnListRepository;
    private final RepoUtils repoUtils;

    //목록이름변경
    public CigaretteListDto.GetResponse updateCigaretteList(Long id, CigaretteListDto.UpdateRequest request){

        CigaretteList cigaretteList = repoUtils.getOneElseThrowException(cigaretteListRepository, id);
        cigaretteList.changeName(request.getName());

        return new CigaretteListDto.GetResponse(cigaretteList);
    }

    //목록 삭제
    public DeleteResponse deleteCigaretteList(Long id){
        CigaretteList cigaretteList = repoUtils.getOneElseThrowException(cigaretteListRepository, id);

        List<CigaretteOnList> cigaretteOnLists = cigaretteOnListRepository.findAllByCigaretteListId(id);
        cigaretteOnListRepository.deleteAll(cigaretteOnLists);

        cigaretteListRepository.delete(cigaretteList);

        return new DeleteResponse("cigaretteList", id);
    }


}
