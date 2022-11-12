package com.dambae200.dambae200.domain.cigarette.service;

import com.dambae200.dambae200.domain.cigarette.domain.Cigarette;
import com.dambae200.dambae200.domain.cigarette.dto.CigaretteDto;
import com.dambae200.dambae200.domain.cigarette.exception.OfficaialNameDuplicationException;
import com.dambae200.dambae200.domain.cigarette.repository.CigaretteRepository;
import com.dambae200.dambae200.domain.cigaretteOnList.domain.CigaretteOnList;
import com.dambae200.dambae200.domain.cigaretteOnList.repository.CigaretteOnListRepository;
import com.dambae200.dambae200.global.common.DeleteResponse;
import com.dambae200.dambae200.global.common.RepoUtils;
import com.dambae200.dambae200.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CigaretteUpdateService {

    private final CigaretteRepository cigaretteRepository;
    private final CigaretteOnListRepository cigaretteOnListRepository;
    private final RepoUtils repoUtils;

    public CigaretteDto.GetResponse addCigarette(CigaretteDto.CigaretteRequest request){

        checkDuplicate(request.getOfficialName());

        Cigarette cigarette = Cigarette.builder()
                .officialName(request.getOfficialName())
                .simpleName(request.getSimpleName())
                .build();

        Cigarette savedCigarette = cigaretteRepository.save(cigarette);
        return new CigaretteDto.GetResponse(savedCigarette);
    }

    public CigaretteDto.GetResponse updateCigarette(Long id, CigaretteDto.CigaretteRequest request){

        checkDuplicate(request.getOfficialName());

        Cigarette cigarette = repoUtils.getOneElseThrowException(cigaretteRepository, id);
        cigarette.updateCigarette(request.getOfficialName(), request.getSimpleName());

        return new CigaretteDto.GetResponse(cigarette);
    }

    public DeleteResponse deleteCigarette(Long id) throws EntityNotFoundException {
        Cigarette cigarette = repoUtils.getOneElseThrowException(cigaretteRepository, id);

        List<CigaretteOnList> cigaretteOnLists = cigaretteOnListRepository.findAllByCigaretteId(id);
        cigaretteOnListRepository.deleteAll(cigaretteOnLists);


        cigaretteRepository.delete(cigarette);
        return new DeleteResponse("cigarette", id);
    }


    private void checkDuplicate(String name) {
        if (cigaretteRepository.existsByOfficialName(name)) {
            throw new OfficaialNameDuplicationException();
        }
    }
}
