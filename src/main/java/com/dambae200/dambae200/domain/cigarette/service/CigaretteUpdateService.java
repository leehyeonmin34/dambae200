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
import com.dambae200.dambae200.global.common.DeleteResponse;
import com.dambae200.dambae200.global.common.RepoUtils;
import com.dambae200.dambae200.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CigaretteUpdateService {

    private final CigaretteRepository cigaretteRepository;
    private final CigaretteOnListRepository cigaretteOnListRepository;
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

        Cigarette savedCigarette = cigaretteRepository.save(cigarette);
        return new CigaretteGetResponse(savedCigarette);
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


        List<Cigarette> savedCigarettes = cigaretteRepository.saveAll(cigarettes);
        return new CigaretteGetListResponse(savedCigarettes);
    }

    public CigaretteGetResponse updateCigarette(Long id, CigaretteUpdateRequest request){

        checkDuplicate(request.getOfficial_name());

        Cigarette cigarette = repoUtils.getOneElseThrowException(cigaretteRepository, id);
        cigarette.updateCigarette(request.getOfficial_name(), request.getCustomized_name());

        return new CigaretteGetResponse(cigarette);
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
